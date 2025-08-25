package net.kaupenjoe.livestreammods.recipe;

import net.kaupenjoe.livestreammods.block.ModBlocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class KaupenFurnaceRecipe extends AbstractCookingRecipe {
    public KaupenFurnaceRecipe(String pGroup, CookingBookCategory pCategory,
                               Ingredient pIngredient, ItemStack pResult, float pExperience, int pCookingTime) {
        super(ModRecipes.KAUPEN_FURNACE_TYPE.get(), pGroup, pCategory, pIngredient, pResult, pExperience, pCookingTime);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.KAUPEN_FURANCE.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.KAUPEN_FURNACE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.KAUPEN_FURNACE_TYPE.get();
    }

    public static class Serializer extends SimpleCookingSerializer<KaupenFurnaceRecipe> {
        public Serializer(int defaultCookingTime) {
            super(KaupenFurnaceRecipe::new, defaultCookingTime);
        }
    }
}
