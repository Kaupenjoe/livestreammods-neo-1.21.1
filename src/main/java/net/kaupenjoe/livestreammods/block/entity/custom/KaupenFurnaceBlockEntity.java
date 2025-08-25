package net.kaupenjoe.livestreammods.block.entity.custom;

import net.kaupenjoe.livestreammods.block.entity.ModBlockEntities;
import net.kaupenjoe.livestreammods.recipe.KaupenFurnaceRecipe;
import net.kaupenjoe.livestreammods.recipe.ModRecipes;
import net.kaupenjoe.livestreammods.screen.custom.KaupenFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class KaupenFurnaceBlockEntity extends AbstractFurnaceBlockEntity implements MenuProvider {
    private Map<Item, Integer> BURN_DURATION_MAP =
            Map.of(Items.COAL, 1200,
                    Items.BLAZE_POWDER, 600);

    public KaupenFurnaceBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.KAUPEN_FURNACE_BLOCK_ENTITY.get(), pPos, pBlockState, ModRecipes.KAUPEN_FURNACE_TYPE.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Kaupen Furnace");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new KaupenFurnaceMenu(pContainerId, pInventory, this, this.dataAccess);
    }

    @Override
    protected int getBurnDuration(ItemStack pFuel) {
        return BURN_DURATION_MAP.getOrDefault(pFuel.getItem(), 0);
    }
}
