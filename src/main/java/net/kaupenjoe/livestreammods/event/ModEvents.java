package net.kaupenjoe.livestreammods.event;

import net.kaupenjoe.livestreammods.LivestreamMods;
import net.kaupenjoe.livestreammods.block.entity.custom.PedestalBlockEntity;
import net.kaupenjoe.livestreammods.networking.ClientPayloadHandler;
import net.kaupenjoe.livestreammods.networking.PedestalCraftCountS2C;
import net.kaupenjoe.livestreammods.networking.ResetSacrificedEntityS2C;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.stream.Stream;

@EventBusSubscriber(modid = LivestreamMods.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onAnimalSacrifice(LivingDeathEvent event) {
        Level level = event.getEntity().level();
        AABB blocksAround = AABB.ofSize(event.getEntity().blockPosition().getCenter(), 5, 3, 5);
        Stream<BlockPos> position = BlockPos.betweenClosedStream(blocksAround);

        position.forEach(pos -> {
            if(level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestalBlockEntity) {
                pedestalBlockEntity.setSacrificedEntity(event.getEntity().getType());
            }
        });
    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        // Sets the current network version
        final PayloadRegistrar registrar = event.registrar("1")
                .executesOn(HandlerThread.MAIN);

        registrar.commonToClient(
                ResetSacrificedEntityS2C.TYPE,
                ResetSacrificedEntityS2C.STREAM_CODEC, ClientPayloadHandler::handleSacrificeOnClient);

        registrar.commonToClient(
                PedestalCraftCountS2C.TYPE,
                PedestalCraftCountS2C.STREAM_CODEC, ClientPayloadHandler::handleCountOnClient);
    }
}
