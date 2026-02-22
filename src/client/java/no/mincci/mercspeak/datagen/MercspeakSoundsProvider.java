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

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MercspeakSoundsProvider extends FabricSoundsProvider {
    private static final String MS_MERC_WILDCARD = "*";
    private static final Path MS_SOUNDS_ROOT = Path.of(String.format("../resources/main/assets/%s/sounds", // target in build directory
            Mercspeak.MOD_ID)
    );
    private static final Path MS_SOUNDS_JSON = Path.of("../resources/client/mercspeak.json");
    private static final String MS_SOUNDS_CONTEXTUAL_DIR = "%s/contextual/";
    private static final String MS_SOUNDS_VOICE_DIR = "%s/voice/";
    private static final int MS_ATTENUATION = 32;

    protected MercspeakSoundsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.@NonNull Provider registryLookup, @NonNull SoundExporter exporter) {
        Gson gson = new Gson();

        try {
            MercsoundsData data = gson.fromJson(new FileReader(MS_SOUNDS_JSON.toFile().getCanonicalFile()), MercsoundsData.class);

            for (Mercenary merc : Mercenary.values()) {
                Path soundsContextualDir = Path.of(String.format(MS_SOUNDS_CONTEXTUAL_DIR, merc));
                Path soundsVoiceDir = Path.of(String.format(MS_SOUNDS_VOICE_DIR, merc));

                // Merc-exclusive contextuals
                for (String merc_ctx : data.merc_contextual.get(merc.toString())) {
                    addSound(exporter,
                            merc,
                            soundsContextualDir,
                            String.format("%s_%s", merc, merc_ctx),
                            String.format("%s.contextual.%s", merc, merc_ctx)
                    );
                }

                // Merc-universal voices
                for (String v : data.voice) {
                    addSound(exporter,
                            merc,
                            soundsVoiceDir,
                            String.format("%s_%s", merc, v),
                            String.format("%s.voice.%s", merc, v)
                    );
                }

                // Merc-universal contextuals
                for (String ctx : data.contextual) {
                    String prefix = String.format("%s_%s", merc, ctx.replace('.', '_'));
                    String id =  String.format("%s.contextual.%s", merc, ctx);

                    if (ctx.contains(MS_MERC_WILDCARD)) {
                        addSoundMerc(exporter,
                                soundsContextualDir,
                                prefix,
                                id
                        );
                    } else {
                        addSound(exporter,
                                merc,
                                soundsContextualDir,
                                prefix,
                                id
                        );
                    }
                }
            }
        } catch (Exception e) {
            Mercspeak.LOGGER.error("Failed to load mercsounds.json");
        }
    }

    @Override
    public @NonNull String getName() {
        return "Mercsounds";
    }

    /// Exports all numbered files under the given prefix (ex. `mercspeak:demo/voice/demo_thanks1`);
    public static void addSound(SoundExporter exporter, Mercenary merc, Path soundsDir, String filePrefix, String id) {
        Identifier soundId = Mercspeak.resolveId(id);
        SoundTypeBuilder soundType = SoundTypeBuilder.of()
                .subtitle(String.format("sound.%s.%s", Mercspeak.MOD_ID, id));

        // IF no num-suffix... shortcut to 1x file
        // ELSE... search dir for consecutive successive num-suffixed files (wow)

        Path nonSuffixFile = MS_SOUNDS_ROOT.resolve(soundsDir).resolve(filePrefix + ".ogg");
        boolean isNonSuffix = Files.isRegularFile(nonSuffixFile, LinkOption.NOFOLLOW_LINKS);

        int index = 1;
        Path suffixFile = nonSuffixFile.resolveSibling(filePrefix + index + ".ogg");


        while (isNonSuffix || Files.isRegularFile(suffixFile, LinkOption.NOFOLLOW_LINKS)) { // NOTE: this assumes you have set up the audio file as valid! hence no granular checks.
            Path fileLoc = soundsDir.resolve((isNonSuffix) ? filePrefix : filePrefix + index);
            SoundTypeBuilder.EntryBuilder sound = SoundTypeBuilder.EntryBuilder.ofFile(Mercspeak.resolveId(fileLoc.toString()))
                    .attenuationDistance(MS_ATTENUATION);
            sound = ((SoundTypeEntryBuilderExt) sound).mercspeak$style(SoundEventStyle.RNDWAVE);
            soundType.sound(sound);

            ++index;
            suffixFile = suffixFile.resolveSibling(filePrefix + index + ".ogg"); // select next numbered item

            if (isNonSuffix) break;
        }

        if (index > 1) {
            exporter.add(soundId, soundType);
        }
    }

    /// Exports all merc-suffixed files under the given prefix (ex. `mercspeak:engie/voice/spy_*`).
    /// This will search for numbered files as well!
    public static void addSoundMerc(SoundExporter exporter, Path soundsDir, String filePrefix, String idPrefix) {
        for (Mercenary merc : Mercenary.values()) {
            addSound(exporter, merc, soundsDir, filePrefix.replace(MS_MERC_WILDCARD, merc.toString()), idPrefix.replace(MS_MERC_WILDCARD, merc.toString()));
        }
    }

    private static class MercsoundsData {
       Map<String, String[]> merc_contextual;
        String[] voice;
        String[] contextual;
    }
}
