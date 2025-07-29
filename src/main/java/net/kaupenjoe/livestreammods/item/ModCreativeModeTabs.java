package net.kaupenjoe.livestreammods.item;

import net.kaupenjoe.livestreammods.LivestreamMods;
import net.kaupenjoe.livestreammods.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LivestreamMods.MOD_ID);

    public static final Supplier<CreativeModeTab> CUSTOM_ITEMS = CREATIVE_MODE_TABS.register("custom_items",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.MAIN_PEDESTAL))
                    .title(Component.translatable("creativetab.bismuth_items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.MAIN_PEDESTAL);
                        output.accept(ModBlocks.SIDE_PEDESTAL);
                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
