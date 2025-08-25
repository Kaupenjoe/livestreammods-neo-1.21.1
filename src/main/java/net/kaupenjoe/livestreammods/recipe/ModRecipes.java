package net.kaupenjoe.livestreammods.recipe;

import net.kaupenjoe.livestreammods.LivestreamMods;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, LivestreamMods.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, LivestreamMods.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PedestalRecipe>> PEDESTAL_SERIALIZER =
            SERIALIZERS.register("pedestal_crafting", PedestalRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<PedestalRecipe>> PEDESTAL_TYPE =
            TYPES.register("pedestal_crafting", () -> new RecipeType<PedestalRecipe>() {
                @Override
                public String toString() {
                    return "pedestal_crafting";
                }
            });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<KaupenFurnaceRecipe>> KAUPEN_FURNACE_SERIALIZER =
            SERIALIZERS.register("kaupen_furnace_crafting", () -> new KaupenFurnaceRecipe.Serializer(200));
    public static final DeferredHolder<RecipeType<?>, RecipeType<KaupenFurnaceRecipe>> KAUPEN_FURNACE_TYPE =
            TYPES.register("kaupen_furnace_crafting", () -> new RecipeType<KaupenFurnaceRecipe>() {
                @Override
                public String toString() {
                    return "kaupen_furnace_crafting";
                }
            });


    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
