package no.mincci.mercspeak;

/// All {@link net.minecraft.client.sounds.WeighedSoundEvents WeighedSoundEvents} variations (ex. {@link RndWaveSoundEvents})
public enum SoundEventStyle {
    DEFAULT,
    RNDWAVE;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
