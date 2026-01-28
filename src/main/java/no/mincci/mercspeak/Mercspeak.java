package no.mincci.mercspeak;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mercspeak implements ModInitializer {
	public static final String MOD_ID = "mercspeak";

	public static final SimpleParticleType CHITCHAT_PARTICLE = FabricParticleTypes.simple();

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier resolveIdPath(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("holy dooley!");
		ModSounds.initialize();
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, Mercspeak.resolveIdPath("chitchat_particle"), CHITCHAT_PARTICLE);
	}
}