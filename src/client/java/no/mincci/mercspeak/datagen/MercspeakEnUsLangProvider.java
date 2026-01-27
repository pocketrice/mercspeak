package no.mincci.mercspeak.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class MercspeakEnUsLangProvider extends FabricLanguageProvider {
    protected MercspeakEnUsLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("text.demo.medic", "MEDIC!");
        translationBuilder.add("text.demo.thanks", "Thanks!");
        translationBuilder.add("text.demo.go", "Go Go Go!");
        translationBuilder.add("text.demo.move", "Move Up!");
        translationBuilder.add("text.demo.left", "Go Left");
        translationBuilder.add("text.demo.right", "Go Right");
        translationBuilder.add("text.demo.yes", "Yes");
        translationBuilder.add("text.demo.no", "No");
        // pass time

        translationBuilder.add("text.demo.incoming", "Incoming");
        translationBuilder.add("text.demo.spy", "Spy!");
        translationBuilder.add("text.demo.sentry_ahead", "Sentry Ahead!");
        translationBuilder.add("text.demo.teleporter", "Teleporter Here");
        translationBuilder.add("text.demo.dispenser", "Dispenser Here");
        translationBuilder.add("text.demo.sentry_here", "Sentry Here");
        translationBuilder.add("text.demo.ubercharge", "Activate Übercharge!");
        // pass time

        translationBuilder.add("text.demo.help", "Help!");
        translationBuilder.add("text.demo.cry", "Battle Cry");
        translationBuilder.add("text.demo.cheer", "Cheers");
        translationBuilder.add("text.demo.jeer", "Jeers");
        translationBuilder.add("text.demo.positive", "Positive");
        translationBuilder.add("text.demo.negative", "Negative");
        translationBuilder.add("text.demo.niceshot", "Nice Shot");
        translationBuilder.add("text.demo.goodjob", "Good Job");
    }
}
