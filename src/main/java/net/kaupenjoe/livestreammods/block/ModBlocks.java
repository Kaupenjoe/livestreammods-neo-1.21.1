package net.kaupenjoe.livestreammods.block;

import net.kaupenjoe.livestreammods.LivestreamMods;
import net.kaupenjoe.livestreammods.block.custom.PedestalBlock;
import net.kaupenjoe.livestreammods.block.custom.SidePedestalBlock;
import net.kaupenjoe.livestreammods.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(LivestreamMods.MOD_ID);

    public static final DeferredBlock<Block> MAIN_PEDESTAL = registerBlock("main_pedestal",
            () -> new PedestalBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final DeferredBlock<Block> SIDE_PEDESTAL = registerBlock("side_pedestal",
            () -> new SidePedestalBlock(BlockBehaviour.Properties.of().noOcclusion()));


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
