package no.mincci.mercspeak.mixin.client;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.client.sounds.Weighted;
import no.mincci.mercspeak.WeighedSoundEventsExt;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(WeighedSoundEvents.class)
abstract class WeighedSoundEventsMixin implements WeighedSoundEventsExt {
    @Shadow @Final
    private List<Weighted<Sound>> list;

    @Unique @Override
    public Weighted<Sound> mercspeak$getSounds(int i) {
        return list.get(i);
    }

    @Unique @Override
    public int mercspeak$lenSounds() {
        return list.size();
    }
}