package no.mincci.mercspeak;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public record MSoundStopC2SPayload(String soundIdFrag, UUID uuid) implements CustomPacketPayload {
    public static final Identifier MSOUND_STOP_PAYLOAD_ID = Mercspeak.resolveId("msound_stop");
    public static final CustomPacketPayload.Type<MSoundStopC2SPayload> ID = new CustomPacketPayload.Type<>(MSOUND_STOP_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, MSoundStopC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, MSoundStopC2SPayload::soundIdFrag,
            ByteBufCodecs.STRING_UTF8.map(UUID::fromString, UUID::toString), MSoundStopC2SPayload::uuid,
            MSoundStopC2SPayload::new);

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}