package no.mincci.mercspeak.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import no.mincci.mercspeak.RndWaveSoundEvents;
import no.mincci.mercspeak.SoundEngineExt;
import no.mincci.mercspeak.SoundEventRegistrationExt;
import no.mincci.mercspeak.SoundManagerExt;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class)
abstract class SoundManagerMixin implements SoundManagerExt {
    @Shadow @Final
    private SoundEngine soundEngine;

    @Unique @Override
    public void mercspeak$stopSub(String soundNameFrag, @Nullable SoundSource category) {
        ((SoundEngineExt) this.soundEngine).mercspeak$stopSub(soundNameFrag, category);
    }
}

@Mixin(targets = "net.minecraft.client.sounds.SoundManager$Preparations")
abstract class SoundManagerPreparationsMixin {
    /// Instantiate the correct style SoundEvent (RNDWAVE or default).
    @Redirect(method = "handleRegistration", at = @At(value = "NEW", target = "Lnet/minecraft/client/sounds/WeighedSoundEvents;"))
    private WeighedSoundEvents ctorStyledSoundEvent(Identifier location, String subtitleKey, @Local(argsOnly = true) SoundEventRegistration registration) {
        return ((SoundEventRegistrationExt) registration).mercspeak$isRndWave() ? new RndWaveSoundEvents(location, subtitleKey) : new WeighedSoundEvents(location, subtitleKey);
    }

    /// Validate RNDWAVE-style SoundEvent to prepare inds buffer!
    @Inject(method = "handleRegistration", at = @At(value = "RETURN", ordinal = 0))
    private void validateStyledSoundEvent(Identifier location, SoundEventRegistration registration, CallbackInfo ci, @Local WeighedSoundEvents weighedSoundEvents) {
        if (((SoundEventRegistrationExt) registration).mercspeak$isRndWave()) {
            ((RndWaveSoundEvents) weighedSoundEvents).validate();
        }
    }
}