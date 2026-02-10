package no.mincci.mercspeak.mixin.client;

import com.google.common.collect.Multimap;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;
import no.mincci.mercspeak.ISoundEngineExt;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin(SoundEngine.class)
public class SoundEngineMixin implements ISoundEngineExt {
    @Final @Shadow
    private Multimap<SoundSource, SoundInstance> instanceBySource;
    @Final @Shadow
    private Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel;

    @Shadow
    public void stop(SoundInstance sound) {}

    @Unique @Override
    public void mercspeak_1_21_11$stopSub(String soundNameFrag, @Nullable SoundSource category) {
        if (category != null) {
            for (SoundInstance soundInstance : this.instanceBySource.get(category)) {
                if (soundInstance.getIdentifier().toString().startsWith(soundNameFrag)) {
                    this.stop(soundInstance);
                }
            }
        } else {
            for (SoundInstance soundInstancex : this.instanceToChannel.keySet()) {
                if (soundInstancex.getIdentifier().toString().startsWith(soundNameFrag)) {
                    this.stop(soundInstancex);
                }
            }
        }
    }
}
