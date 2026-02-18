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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MercspeakSoundsProvider extends FabricSoundsProvider {
    private static final Pattern RE_MERC_SUFFIX = Pattern.compile("\\*");
    private static final Path MS_SOUNDS_ROOT = Path.of(String.format("%smain/resources/assets/%s/sounds",
            "../".repeat(Mercspeak.MOD_PACKAGE.split("\\.").length),
            Mercspeak.MOD_ID)
    );
    private static final Path MS_SOUNDS_JSON = MS_SOUNDS_ROOT.getParent().resolve("mercsounds.json");
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
            MercsoundsData data = gson.fromJson(new FileReader(MS_SOUNDS_JSON.toFile()), MercsoundsData.class);

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
                    if (RE_MERC_SUFFIX.matcher(ctx).hasMatch()) {
                        addSoundMerc(exporter,
                                soundsContextualDir,
                                String.format("%s_%s_*", merc, ctx),
                                String.format("%s.contextual.%s.*", merc, ctx)
                        );
                    } else {
                        addSound(exporter,
                                merc,
                                soundsContextualDir,
                                String.format("%s:%s/contextual/%s_%s", Mercspeak.MOD_ID, merc, merc, ctx),
                                String.format("%s.contextual.%s", merc, ctx)
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
        Identifier soundId = resolveSoundId(merc, id);
        SoundTypeBuilder soundType = SoundTypeBuilder.of()
                .subtitle(String.format("merc_ctx.%s", soundId));

        // search directory for all succeeding numerical suffixed files (wow)
        int len = 1;
        Path suffixedFile = MS_SOUNDS_ROOT.resolve(soundsDir).resolve(filePrefix + len);

        while (Files.exists(suffixedFile, LinkOption.NOFOLLOW_LINKS)) {
            SoundTypeBuilder.EntryBuilder sound = SoundTypeBuilder.EntryBuilder.ofFile(Identifier.parse(suffixedFile.toString()))
                    .attenuationDistance(MS_ATTENUATION);
            sound = ((SoundTypeEntryBuilderExt) sound).mercspeak$style(SoundEventStyle.RNDWAVE);
            soundType.sound(sound);

            ++len;
            suffixedFile = suffixedFile.getParent().resolve(filePrefix + len); // select next numbered item
        }

        exporter.add(soundId, soundType);
    }

    /// Exports all merc-suffixed files under the given prefix (ex. `mercspeak:engie/voice/spy_*`).
    /// This will search for numbered files as well!
    public static void addSoundMerc(SoundExporter exporter, Path soundsDir, String filePrefix, String idPrefix) {
        Matcher matcher_file = RE_MERC_SUFFIX.matcher(filePrefix);
        Matcher matcher_id = RE_MERC_SUFFIX.matcher(idPrefix);

        for (Mercenary merc : Mercenary.values()) {
            addSound(exporter, merc, soundsDir, matcher_file.replaceFirst(merc.toString()), matcher_id.replaceFirst(merc.toString()));
        }
    }

    public static Identifier resolveSoundId(Mercenary merc, String sound) {
        return Mercspeak.resolveId(String.format("%s.%s", merc, sound));
    }

    private static class MercsoundsData {
       Map<String, String[]> merc_contextual;
        String[] voice;
        String[] contextual;
    }
}
