package net.kaupenjoe.livestreammods.block.entity.custom;

import net.kaupenjoe.livestreammods.block.ModBlocks;
import net.kaupenjoe.livestreammods.block.entity.ModBlockEntities;
import net.kaupenjoe.livestreammods.recipe.ModRecipes;
import net.kaupenjoe.livestreammods.recipe.PedestalRecipe;
import net.kaupenjoe.livestreammods.recipe.PedestalRecipeInput;
import net.kaupenjoe.livestreammods.screen.custom.PedestalMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.List;
import java.util.Optional;

public class PedestalBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    private float rotation;
    private float count = 0f;
    private float maxCount = 40f;

    public static List<Vector2i> offsets = List.of(
            new Vector2i(3, 0),
            new Vector2i(2, 2),
            new Vector2i(0, 3),
            new Vector2i(-2, 2),

            new Vector2i(-3, 0),
            new Vector2i(-2, -2),
            new Vector2i(0, -3),
            new Vector2i(2, -2));


    public PedestalBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.MAIN_PEDESTAL_BE.get(), pPos, pBlockState);
    }

    public void clearContents() {
        inventory.setStackInSlot(0, ItemStack.EMPTY);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for(int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put("inventory", inventory.serializeNBT(pRegistries));
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        inventory.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
    }

    public float getRenderingRotation() {
        rotation += 0.5f;
        if(rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (!hasRecipe())
            return;

        if(countFinished()) {
            count = 0;
            exchangeItemInMainPedestal();
            removeItemsFromSidePedestals();
            spawnVisualLightningBolt((ServerLevel) level, blockPos);
            spawnExplosionParticles((ServerLevel) level);
        } else {
            count++;
            spawnCraftingParticles(level);
        }
    }

    private void spawnVisualLightningBolt(ServerLevel level, BlockPos blockPos) {
        EntityType.LIGHTNING_BOLT.spawn(level, blockPos, MobSpawnType.TRIGGERED).setVisualOnly(true);
    }

    private void spawnExplosionParticles(ServerLevel level) {
        double x = this.getBlockPos().getX();
        double y = this.getBlockPos().getY();
        double z = this.getBlockPos().getZ();
        level.sendParticles(ParticleTypes.EXPLOSION_EMITTER,
                x + 0.5f, y + 1.2f, z + 0.5f, 0, 0, 0, 0, 0.25f);
    }

    private void spawnCraftingParticles(Level level) {
        offsets.forEach(offset -> {
            ItemStack stack = ((SidePedestalBlockEntity) level.getBlockEntity(this.getBlockPos().offset(offset.x, 0, offset.y))).inventory.getStackInSlot(0);
            double x = this.getBlockPos().offset(offset.x, 0, offset.y).getX();
            double y = this.getBlockPos().offset(offset.x, 0, offset.y).getY();
            double z = this.getBlockPos().offset(offset.x, 0, offset.y).getZ();

            BlockPos direction = getBlockPos().subtract(this.getBlockPos().offset(offset.x, 0, offset.y));

            ((ServerLevel) level).sendParticles(new ItemParticleOption
                    (ParticleTypes.ITEM, stack), x + 0.5f, y + 1.2f, z + 0.5f, 0, direction.getX(), direction.getY(), direction.getZ(), 0.25f);
        });
    }

    private boolean countFinished() {
        return count >= maxCount;
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<PedestalRecipe>> recipe = getCurrentRecipe();
        return recipe.isPresent();
    }

    private Optional<RecipeHolder<PedestalRecipe>> getCurrentRecipe() {
        return this.level.getRecipeManager()
                .getRecipeFor(ModRecipes.PEDESTAL_TYPE.get(), new PedestalRecipeInput(
                        this.inventory.getStackInSlot(0),
                        offsets.stream().map(offset -> {
                            if(hasSidePedestals()) {
                                return ((SidePedestalBlockEntity) level.getBlockEntity(this.getBlockPos().offset(offset.x, 0, offset.y))).
                                    inventory.getStackInSlot(0);
                        } else {
                            return ItemStack.EMPTY;
                        }}).toList()), level);
    }

    private void exchangeItemInMainPedestal() {
        Optional<RecipeHolder<PedestalRecipe>> recipe = getCurrentRecipe();

        if(recipe.isPresent()) {
            this.inventory.extractItem(0, 64, false);
            this.inventory.insertItem(0, recipe.get().value().getResultItem(null), false);
        }
    }

    private void removeItemsFromSidePedestals() {
        offsets.forEach(offset -> ((SidePedestalBlockEntity) level.getBlockEntity(this.getBlockPos().offset(offset.x, 0, offset.y)))
                .inventory.setStackInSlot(0, ItemStack.EMPTY));
    }

    private boolean hasItemInMainPedestal() {
        return this.inventory.getStackInSlot(0).is(Items.DIAMOND);
    }

    private boolean doSidePedestalsHaveItems() {
        return offsets.stream().allMatch(offset -> hasItemInInventory(offset, Items.REDSTONE));
    }

    private boolean hasSidePedestals() {
        return offsets.stream().allMatch(this::isSidePedestal);
    }

    private boolean isSidePedestal(Vector2i offset) {
        return level.getBlockState(this.getBlockPos().offset(offset.x, 0, offset.y)).is(ModBlocks.SIDE_PEDESTAL);
    }

    private boolean hasItemInInventory(Vector2i offset, Item item) {
        return ((SidePedestalBlockEntity) level.getBlockEntity(this.getBlockPos().offset(offset.x, 0, offset.y)))
                .inventory.getStackInSlot(0).is(item);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("blockentity.livestreammods.pedestal");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new PedestalMenu(pContainerId, pPlayerInventory, this);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }
}
