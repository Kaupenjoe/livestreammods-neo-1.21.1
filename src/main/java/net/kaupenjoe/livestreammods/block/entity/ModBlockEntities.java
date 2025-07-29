package net.kaupenjoe.livestreammods.block.entity;

import net.kaupenjoe.livestreammods.LivestreamMods;
import net.kaupenjoe.livestreammods.block.ModBlocks;
import net.kaupenjoe.livestreammods.block.entity.custom.PedestalBlockEntity;
import net.kaupenjoe.livestreammods.block.entity.custom.SidePedestalBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, LivestreamMods.MOD_ID);

    public static final Supplier<BlockEntityType<PedestalBlockEntity>> MAIN_PEDESTAL_BE =
            BLOCK_ENTITIES.register("main_pedestal_be", () -> BlockEntityType.Builder.of(
                    PedestalBlockEntity::new, ModBlocks.MAIN_PEDESTAL.get()).build(null));

    public static final Supplier<BlockEntityType<SidePedestalBlockEntity>> SIDE_PEDESTAL_BE =
            BLOCK_ENTITIES.register("side_pedestal_be", () -> BlockEntityType.Builder.of(
                    SidePedestalBlockEntity::new, ModBlocks.SIDE_PEDESTAL.get()).build(null));



    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
