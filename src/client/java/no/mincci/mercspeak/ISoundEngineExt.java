package no.mincci.mercspeak;

import net.minecraft.sounds.SoundSource;
import org.jspecify.annotations.Nullable;

public interface ISoundEngineExt {
    default void mercspeak_1_21_11$stopSub(String soundNameFrag, @Nullable SoundSource category) {}
}
