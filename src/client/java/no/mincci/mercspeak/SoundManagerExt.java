package no.mincci.mercspeak;

import net.minecraft.sounds.SoundSource;
import org.jspecify.annotations.Nullable;

public interface SoundManagerExt {
    void mercspeak$stopPrefix(String soundNameFrag, @Nullable SoundSource category);
}
