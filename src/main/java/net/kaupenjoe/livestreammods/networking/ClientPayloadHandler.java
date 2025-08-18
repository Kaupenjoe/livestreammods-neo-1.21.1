package net.kaupenjoe.livestreammods.networking;

import net.kaupenjoe.livestreammods.block.entity.custom.PedestalBlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
    public static void handleSacrificeOnClient(final ResetSacrificedEntityS2C data, final IPayloadContext context) {
        ((PedestalBlockEntity) context.player().level().getBlockEntity(data.blockEntityPosition())).setSacrificedEntity(null);
    }

    public static void handleCountOnClient(final PedestalCraftCountS2C data, final IPayloadContext context) {
        ((PedestalBlockEntity) context.player().level().getBlockEntity(data.blockEntityPosition())).setCount(data.count());
    }
}