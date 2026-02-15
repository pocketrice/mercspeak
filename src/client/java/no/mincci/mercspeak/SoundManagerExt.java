package no.mincci.mercspeak;

import net.minecraft.sounds.SoundSource;
import org.jspecify.annotations.Nullable;

public interface SoundManagerExt {
    void mercspeak$stopSub(String soundNameFrag, @Nullable SoundSource category);
}
