package net.kaupenjoe.livestreammods.block.entity.custom;

import net.kaupenjoe.livestreammods.block.ModBlocks;
import net.kaupenjoe.livestreammods.block.entity.ModBlockEntities;
import net.kaupenjoe.livestreammods.screen.custom.PedestalMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.List;

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

    List<Vector2i> offsets = List.of(
            new Vector2i(3, 0),
            new Vector2i(2, 2),
            new Vector2i(0, 3),
            new Vector2i(2, -2),

            new Vector2i(-2, 2),
            new Vector2i(-2, -2),
            new Vector2i(0, -3),
            new Vector2i(-3, 0));


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
        if(hasSidePedestals()) {
            if (doSidePedestalsHaveItems()) {
                if(hasItemInMainPedestal()) {
                    removeItemsFromSidePedestals();
                    exchangeItemInMainPedestal();
                }
            }
        }
    }

    private void exchangeItemInMainPedestal() {
        this.inventory.extractItem(0, 64, false);
        this.inventory.insertItem(0, new ItemStack(Items.NETHER_BRICK), false);
    }

    private void removeItemsFromSidePedestals() {
        offsets.stream().forEach(offset -> ((SidePedestalBlockEntity) level.getBlockEntity(this.getBlockPos().offset(offset.x, 0, offset.y)))
                .inventory.setStackInSlot(0, ItemStack.EMPTY));
    }

    private boolean hasItemInMainPedestal() {
        return this.inventory.getStackInSlot(0).is(Items.DIAMOND);
    }

    private boolean doSidePedestalsHaveItems() {
        return offsets.stream().allMatch(offset -> ((SidePedestalBlockEntity) level.getBlockEntity(this.getBlockPos().offset(offset.x, 0, offset.y)))
                .inventory.getStackInSlot(0).is(Items.REDSTONE));
    }

    private boolean hasSidePedestals() {
        return offsets.stream().allMatch(offset -> level.getBlockState(this.getBlockPos().offset(offset.x, 0, offset.y)).is(ModBlocks.SIDE_PEDESTAL));
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
