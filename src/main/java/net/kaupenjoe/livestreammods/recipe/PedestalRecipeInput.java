package net.kaupenjoe.livestreammods.recipe;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record PedestalRecipeInput(ItemStack mainPedestalItem, List<ItemStack> sidePedestalItems, EntityType<?> entityType) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {
        if(i == 0) {
            return mainPedestalItem;
        }
        return sidePedestalItems.get(i - 1);
    }

    @Override
    public int size() {
        return 9;
    }
}
