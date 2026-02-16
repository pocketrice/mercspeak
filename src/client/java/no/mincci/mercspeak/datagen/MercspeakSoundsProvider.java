package no.mincci.mercspeak.datagen;

import com.google.gson.Gson;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import no.mincci.mercspeak.Mercenary;
import no.mincci.mercspeak.Mercspeak;
import no.mincci.mercspeak.SoundEventStyle;
import no.mincci.mercspeak.SoundTypeEntryBuilderExt;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MercspeakSoundsProvider extends FabricSoundsProvider {
    private static final Pattern RE_MERC_SUFFIX = Pattern.compile("\\*");
    private static final int MS_ATTENUATION = 32;

    protected MercspeakSoundsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registryLookup, SoundExporter exporter) {
        Gson gson = new Gson();
        MercsoundsData data = gson.fromJson("",  MercsoundsData.class);

        for (Mercenary merc : Mercenary.values()) {
            // Merc-exclusive contextuals
            for (String merc_ctx : data.merc_contextual.get(merc.toString())) {
                addSound(exporter,
                        merc,
                        String.format("%s:%s/contextual/%s_%s", Mercspeak.MOD_ID, merc, merc, merc_ctx),
                        String.format("%s.contextual.%s", merc, merc_ctx)
                );
            }

            // Merc-universal voices
            for (String v : data.voice) {
                addSound(exporter,
                        merc,
                        String.format("%s:%s/voice/%s_%s", Mercspeak.MOD_ID, merc, merc, v),
                        String.format("%s.voice.%s", merc, v)
                );
            }

            // Merc-universal contextuals
            for (String ctx : data.contextual) {
                if (RE_MERC_SUFFIX.matcher(ctx).hasMatch()) {
                    addSoundMerc(exporter,
                            String.format("%s:%s/contextual/%s_%s_*", Mercspeak.MOD_ID, merc, merc, ctx),
                            String.format("%s.contextual.%s.*", merc, ctx)
                    );
                } else {
                    addSound(exporter,
                            merc,
                            String.format("%s:%s/contextual/%s_%s", Mercspeak.MOD_ID, merc, merc, ctx),
                            String.format("%s.contextual.%s", merc, ctx)
                    );
                }
            }
        }
    }

    @Override
    public @NonNull String getName() {
        return "Mercsounds";
    }

    /// Exports all numbered files under the given prefix (ex. `mercspeak:demo/voice/demo_thanks1`);
    public static void addSound(SoundExporter exporter, Mercenary merc, String filePrefix, String id) {
        Identifier soundId = resolveSoundId(merc, id);
        SoundTypeBuilder soundType = SoundTypeBuilder.of()
                .subtitle(String.format("merc_ctx.%s", soundId));

        // TODO search directory for number of values
        int len = 5;

        for (int i = 1; i <= len; i++) {
            SoundTypeBuilder.EntryBuilder sound = SoundTypeBuilder.EntryBuilder.ofFile(Identifier.parse(String.format("%s%d", filePrefix, i)))
                    .attenuationDistance(MS_ATTENUATION);
            sound = ((SoundTypeEntryBuilderExt) sound).mercspeak$style(SoundEventStyle.RNDWAVE);

            soundType.sound(sound);
        }

        exporter.add(soundId, soundType);
    }

    /// Exports all merc-suffixed files under the given prefix (ex. `mercspeak:engie/voice/spy_*`).
    /// This will search for numbered files as well!
    public static void addSoundMerc(SoundExporter exporter, String filePrefix, String idPrefix) {
        Matcher matcher_file = RE_MERC_SUFFIX.matcher(filePrefix);
        Matcher matcher_id = RE_MERC_SUFFIX.matcher(idPrefix);

        for (Mercenary merc : Mercenary.values()) {
            addSound(exporter, merc, matcher_file.replaceFirst(merc.toString()), matcher_id.replaceFirst(merc.toString()));
        }
    }

    public static Identifier resolveSoundId(Mercenary merc, String sound) {
        return Mercspeak.resolveId(String.format("%s.%s", merc, sound));
    }

    private static class MercsoundsData {
       Map<String, String[]> merc_contextual;
        String[] voice;
        String[] contextual;
        String[] contextual_suffixed;
    }
}
