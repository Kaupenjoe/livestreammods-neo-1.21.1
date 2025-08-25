package net.kaupenjoe.livestreammods.screen.custom;

import net.kaupenjoe.livestreammods.recipe.ModRecipes;
import net.kaupenjoe.livestreammods.screen.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class KaupenFurnaceMenu extends AbstractFurnaceMenu {
    public KaupenFurnaceMenu(int id, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(id, inventory, new SimpleContainer(3));
    }

    public KaupenFurnaceMenu(int pContainerId, Inventory pPlayerInventory, Container container, ContainerData data) {
        super(ModMenuTypes.KAUPEN_FURNACE_MENU.get(), ModRecipes.KAUPEN_FURNACE_TYPE.get(), RecipeBookType.FURNACE, pContainerId, pPlayerInventory, container, data);

        this.slots.clear();

        super.addSlot(new Slot(container, 0, 116, 35));
        super.addSlot(new FurnaceFuelSlot(this, container, 1, 56, 53));
        super.addSlot(new FurnaceResultSlot(pPlayerInventory.player, container, 2, 56, 17));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                super.addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            super.addSlot(new Slot(pPlayerInventory, k, 8 + k * 18, 142));
        }
    }

    public KaupenFurnaceMenu(int id, Inventory inventory, SimpleContainer container) {
        super(ModMenuTypes.KAUPEN_FURNACE_MENU.get(), ModRecipes.KAUPEN_FURNACE_TYPE.get(), RecipeBookType.FURNACE, id, inventory, container, new SimpleContainerData(4));

        this.slots.clear();

        super.addSlot(new Slot(container, 0, 116, 35));
        super.addSlot(new FurnaceFuelSlot(this, container, 1, 56, 53));
        super.addSlot(new FurnaceResultSlot(inventory.player, container, 2, 56, 17));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                super.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            super.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    protected Slot addSlot(Slot slot) {
        return slot;
    }

    @Override
    protected boolean isFuel(ItemStack pStack) {
        return true;
    }
}
