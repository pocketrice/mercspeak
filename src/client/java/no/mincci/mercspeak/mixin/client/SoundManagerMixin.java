package no.mincci.mercspeak.mixin.client;

import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import no.mincci.mercspeak.ISoundEngineExt;
import no.mincci.mercspeak.ISoundManagerExt;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SoundManager.class)
public class SoundManagerMixin implements ISoundManagerExt {
    @Shadow @Final
    private SoundEngine soundEngine;

    @Unique @Override
    public void mercspeak_1_21_11$stopSub(String soundNameFrag, @Nullable SoundSource category) {
        ((ISoundEngineExt) this.soundEngine).mercspeak_1_21_11$stopSub(soundNameFrag, category);
    }
}
