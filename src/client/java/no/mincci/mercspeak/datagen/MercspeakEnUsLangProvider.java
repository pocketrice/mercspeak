package no.mincci.mercspeak.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class MercspeakEnUsLangProvider extends FabricLanguageProvider {
    protected MercspeakEnUsLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("text.mercspeak.medic", "MEDIC!");
        translationBuilder.add("text.mercspeak.thanks", "Thanks!");
        translationBuilder.add("text.mercspeak.go", "Go Go Go!");
        translationBuilder.add("text.mercspeak.move", "Move Up!");
        translationBuilder.add("text.mercspeak.left", "Go Left");
        translationBuilder.add("text.mercspeak.right", "Go Right");
        translationBuilder.add("text.mercspeak.yes", "Yes");
        translationBuilder.add("text.mercspeak.no", "No");
        // pass time

        translationBuilder.add("text.mercspeak.incoming", "Incoming");
        translationBuilder.add("text.mercspeak.spy", "Spy!");
        translationBuilder.add("text.mercspeak.sentry_ahead", "Sentry Ahead!");
        translationBuilder.add("text.mercspeak.teleporter", "Teleporter Here");
        translationBuilder.add("text.mercspeak.dispenser", "Dispenser Here");
        translationBuilder.add("text.mercspeak.sentry_here", "Sentry Here");
        translationBuilder.add("text.mercspeak.ubercharge", "ÜberCharge!");
        translationBuilder.add("text.mercspeak.schadenfreude", "Schadenfreude");
        // pass time

        translationBuilder.add("text.mercspeak.help", "Help!");
        translationBuilder.add("text.mercspeak.cry", "Battle Cry");
        translationBuilder.add("text.mercspeak.cheer", "Cheers");
        translationBuilder.add("text.mercspeak.jeer", "Jeers");
        translationBuilder.add("text.mercspeak.positive", "Positive");
        translationBuilder.add("text.mercspeak.negative", "Negative");
        translationBuilder.add("text.mercspeak.niceshot", "Nice Shot");
        translationBuilder.add("text.mercspeak.goodjob", "Good Job");

        translationBuilder.add("text.mercspeak.cancel", "Cancel");

        translationBuilder.add("text.mercspeak.chat_prefix", "(Voice) %1$s");
        translationBuilder.add("text.mercspeak.chat_sep", ": ");
    }
}
