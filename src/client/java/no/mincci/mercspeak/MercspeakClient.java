package no.mincci.mercspeak;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;

import java.nio.charset.Charset;
import java.util.UUID;

public class MercspeakClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		/*
		 * PARTICLE(s)
		 */
		ParticleFactoryRegistry.getInstance().register(Mercspeak.CHITCHAT_PARTICLE, EndRodParticle.Provider::new);

		/*
		 * HUD
		 */
		HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Mercspeak.resolveId("voice_menu"), VPanel::render);

		/*
		 * COMMANDS
		 */
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			ClassSelCommand.register(dispatcher);
		});

		ArgumentTypeRegistry.registerArgumentType( // must register both server and client
				Mercspeak.resolveId("mercenary"),
				MercenaryArgumentType.class,
				SingletonArgumentInfo.contextFree(MercenaryArgumentType::new)
		);

		/*
		 * etc.
		 */

		ModBinds.initialize();
		VPanel.initialize();
	}

	public FriendlyByteBuf assembleSoundPlayPacket(Identifier soundId, UUID uuid) {
		FriendlyByteBuf buf = PacketByteBufs.create(); // { Identifier }
		buf.writeIdentifier(soundId);
		buf.writeUUID(uuid);
		return buf;
	}


	public FriendlyByteBuf assembleSoundStopPacket(String soundIdFrag, UUID uuid) {
		FriendlyByteBuf buf = PacketByteBufs.create();
		buf.writeCharSequence(soundIdFrag, Charset.defaultCharset());
		buf.writeUUID(uuid);
		return buf;
	}
}

