package no.mincci.mercspeak.mixin.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import net.minecraft.client.resources.sounds.SoundEventRegistrationSerializer;
import net.minecraft.util.GsonHelper;
import no.mincci.mercspeak.SoundEventRegistrationExt;
import no.mincci.mercspeak.SoundEventStyle;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SoundEventRegistration.class)
abstract class SoundEventRegistrationMixin implements SoundEventRegistrationExt {
    @Mutable
    @Unique @Final
    private boolean mercspeak$isRndWave;

    /// Initialise RNDWAVE flag.
    @Inject(method = "<init>*", at = @At("TAIL"))
    private void ctorInitRndWave(List<Sound> sounds, boolean replace, String subtitle, CallbackInfo ci) {
        mercspeak$isRndWave = false;
    }

    /// Read RNDWAVE flag.
    @Override
    public boolean mercspeak$isRndWave() {
        return this.mercspeak$isRndWave;
    }

    /// Intended to be set once in constructor but mixin cons limitations.
    @Override
    public void mercspeak$setRndWave(boolean isRndWave) {
        this.mercspeak$isRndWave = isRndWave;
    }
}

@Mixin(SoundEventRegistrationSerializer.class)
abstract class SoundEventRegistrationSerializerMixin {
    /// Check and set RNDWAVE flag from deserialisation.
    @Redirect(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/resources/sounds/SoundEventRegistration;", at = @At(value = "NEW", target = "Lnet/minecraft/client/resources/sounds/SoundEventRegistration;"))
    public SoundEventRegistration ctorStyledSoundEventRegistration(List<Sound> sounds, boolean replace, String subtitle, @Local JsonObject jsonObject) throws JsonParseException {
        boolean isRndWave = !GsonHelper.getAsString(jsonObject, "soundEventType", "").equals(SoundEventStyle.RNDWAVE.toString());

        SoundEventRegistration registration = new SoundEventRegistration(sounds, replace, subtitle);
        ((SoundEventRegistrationExt) registration).mercspeak$setRndWave(isRndWave);

        return registration;
    }
}