package no.mincci.mercspeak.mixin.client;

import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.impl.datagen.client.SoundTypeBuilderImpl;
import no.mincci.mercspeak.SoundEventStyle;
import no.mincci.mercspeak.SoundTypeEntryBuilderExt;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SoundTypeBuilderImpl.EntryBuilderImpl.class)
abstract class SoundTypeEntryBuilderMixin implements SoundTypeEntryBuilderExt {
    @Mutable
    @Unique @Final
    private SoundEventStyle style;

    @Override
    public SoundTypeBuilder.EntryBuilder mercspeak$style(SoundEventStyle style) {
        this.style = style;
        return ((SoundTypeBuilder.EntryBuilder)(Object)this);
    }
}
