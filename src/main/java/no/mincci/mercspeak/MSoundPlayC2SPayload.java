package no.mincci.mercspeak;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public record MSoundPlayC2SPayload(Identifier soundId, UUID uuid) implements CustomPacketPayload {
    public static final Identifier MSOUND_PLAY_PAYLOAD_ID = Mercspeak.resolveId("msound_play");
    public static final CustomPacketPayload.Type<MSoundPlayC2SPayload> ID = new CustomPacketPayload.Type<>(MSOUND_PLAY_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, MSoundPlayC2SPayload> CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, MSoundPlayC2SPayload::soundId,
            ByteBufCodecs.STRING_UTF8.map(UUID::fromString, UUID::toString), MSoundPlayC2SPayload::uuid,
            MSoundPlayC2SPayload::new);

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
