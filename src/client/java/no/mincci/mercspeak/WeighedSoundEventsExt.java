package no.mincci.mercspeak;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.sounds.Weighted;

public interface WeighedSoundEventsExt {
    Weighted<Sound> mercspeak$getSounds(int i);
    int mercspeak$lenSounds();
}
