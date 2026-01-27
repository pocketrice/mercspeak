package no.mincci.mercspeak;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class MercspeakClient implements ClientModInitializer {
	private static final int VC_COOLDOWN_MS = 2000;

	private static boolean isVCBindEdge = false; // is binding_vc_X rising/falling edge?

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		KeyMapping.Category category_vc = KeyMapping.Category.register(Mercspeak.resolveIdPath("voice_cmd"));
		KeyMapping.Category category_vnum = KeyMapping.Category.register(Mercspeak.resolveIdPath("voice_num"));

		KeyMapping binding_vc_a = KeyBindingHelper.registerKeyBinding(new ToggleKeyMapping("key.mercspeak.voice_cmd_a", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, category_vc, () -> true, false));
		KeyMapping binding_vc_b = KeyBindingHelper.registerKeyBinding(new ToggleKeyMapping("key.mercspeak.voice_cmd_b", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X, category_vc, () -> true, false));
		KeyMapping binding_vc_c = KeyBindingHelper.registerKeyBinding(new ToggleKeyMapping("key.mercspeak.voice_cmd_c", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, category_vc, () -> true, false));

		KeyMapping binding_vnum_1 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_1", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_1, category_vnum));
		KeyMapping binding_vnum_2 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_2", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_2, category_vnum));
		KeyMapping binding_vnum_3 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_3", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_3, category_vnum));
		KeyMapping binding_vnum_4 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_4", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_4, category_vnum));
		KeyMapping binding_vnum_5 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_5", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_5, category_vnum));
		KeyMapping binding_vnum_6 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_6", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_6, category_vnum));
		KeyMapping binding_vnum_7 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_7", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_7, category_vnum));
		KeyMapping binding_vnum_8 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_8", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_8, category_vnum));
		KeyMapping binding_vnum_9 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_9", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_9, category_vnum));
		KeyMapping binding_vnum_0 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_0", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_0, category_vnum));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;

			Mercspeak.LOGGER.info("A={} B={} C={}", binding_vc_a.isDown(), binding_vc_b.isDown(), binding_vc_c.isDown());

			if (binding_vc_a.isDown()) {
				binding_vc_b.setDown(false);
				binding_vc_c.setDown(false);

				if (binding_vnum_1.consumeClick()) {
					client.player.displayClientMessage(Component.translatable("text.demo.medic"), true);
				} else if (binding_vnum_2.consumeClick()) {
					client.player.displayClientMessage(Component.translatable("text.demo.thanks"), true);
				} else if (binding_vnum_3.consumeClick()) {
					client.player.displayClientMessage(Component.translatable("text.demo.go"), true);
				} else if (binding_vnum_4.consumeClick()) {
					client.player.displayClientMessage(Component.translatable("text.demo.move"), true);
				}
			}

			if (binding_vc_b.isDown()) {
				binding_vc_a.setDown(false);
				binding_vc_c.setDown(false);

				if (binding_vnum_1.consumeClick()) {
					client.player.displayClientMessage(Component.translatable("text.demo.incoming"), true);
				} else if (binding_vnum_2.consumeClick()) {
					client.player.displayClientMessage(Component.translatable("text.demo.spy"), true);
				} else if (binding_vnum_3.consumeClick()) {
					client.player.displayClientMessage(Component.translatable("text.demo.sentry_ahead"), true);
				}
			}

			if (binding_vc_c.isDown()) {
				binding_vc_a.setDown(false);
				binding_vc_b.setDown(false);

				if (binding_vnum_1.consumeClick()) {
					client.player.displayClientMessage(Component.translatable("text.demo.help"), true);
				} else if (binding_vnum_2.consumeClick()) {
					client.player.displayClientMessage(Component.translatable("text.demo.cry"), true);
				} else if (binding_vnum_3.consumeClick()) {
					client.player.displayClientMessage(Component.translatable("text.demo.cheer"), true);
				}
			}
		});
	}
}