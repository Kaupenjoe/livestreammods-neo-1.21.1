package net.kaupenjoe.livestreammods.networking;

import io.netty.buffer.ByteBuf;
import net.kaupenjoe.livestreammods.LivestreamMods;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ResetSacrificedEntityS2C(String name, BlockPos blockEntityPosition) implements CustomPacketPayload {

    public static final Type<ResetSacrificedEntityS2C> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LivestreamMods.MOD_ID, "reset_entity"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'name' will be encoded and decoded as a string
    // 'age' will be encoded and decoded as an integer
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, ResetSacrificedEntityS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ResetSacrificedEntityS2C::name,
            BlockPos.STREAM_CODEC,
            ResetSacrificedEntityS2C::blockEntityPosition,
            ResetSacrificedEntityS2C::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}