package no.mincci.mercspeak;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class Mercspeak implements ModInitializer {
	public static final String MOD_ID = "mercspeak";

	public static final SimpleParticleType CHITCHAT_PARTICLE = FabricParticleTypes.simple();

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod soundId as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier resolveId(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static String resolveIdFrag(String pathFrag) {
		assert pathFrag.matches("([a-z]+\\.)*[a-z]+") : String.format("Invalid path frag %s", pathFrag);
		return String.format("%s.%s",  MOD_ID, pathFrag);
	}

	private Random rng;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		rng = new Random();

		LOGGER.info("holy dooley!");
		ModSounds.initialize();
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, Mercspeak.resolveId("chitchat_particle"), CHITCHAT_PARTICLE);

		PlayerDeathCallback.EVENT.register((player, damageSource) -> {
			player.level().playSound(player, BlockPos.containing(player.position()),
					ModSounds.DEMO_PAIN_SHARP, SoundSource.PLAYERS,
					1f, 1f);
			return InteractionResult.FAIL;
		});


		PayloadTypeRegistry.playC2S().register(MSoundStopC2SPayload.ID, MSoundStopC2SPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(MSoundPlayC2SPayload.ID, MSoundPlayC2SPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(MSoundPlayC2SPayload.ID, (payload, context) -> {
			ServerPlayer player = context.server().getPlayerList().getPlayer(payload.uuid());
			SoundEvent sound = BuiltInRegistries.SOUND_EVENT.getValue(payload.soundId());
			//Mercspeak.LOGGER.info("{}...", payload.soundId());

			if (player != null && sound != null) {
				//Mercspeak.LOGGER.info("OK\n");
				//player.playSound(sound);
				player.level().playSeededSound(
						player, player,
						Holder.direct(sound), SoundSource.PLAYERS, 1f, 1f, rng.nextLong()); // <-- `ClientBoundEntitySoundPacket` (for tracked locative) requires seed parameter, hence `playSeededSound` is only option!
			}
		});

//		ArgumentTypeRegistry.registerArgumentType( // must register on both server & client
//				Mercspeak.resolveIdPath("mercenary"),
//				MercenaryArgumentType.class,
//				SingletonArgumentInfo.contextFree(MercenaryArgumentType::new)
//		);

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

		});
	}
}