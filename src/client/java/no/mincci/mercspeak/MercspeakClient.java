package no.mincci.mercspeak;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class MercspeakClient implements ClientModInitializer {
	private static final int VMENU_COOLDOWN_TICKS = 40;
	private static final int VMENU_ANIM_MS = 800; // TODO: pegged to ticks for now, migrate to ms. Add ``
	private static final int
			VMENU_X = 3, VMENU_Y = 55,
			VMENU_U = 0, VMENU_V = 0,
			VMENU_R_WIDTH = 40 * 2, VMENU_R_HEIGHT = 64 * 2,
			VMENU_T_WIDTH = 40 * 2, VMENU_T_HEIGHT = 64 * 2;
	private static final int COLOR_CHAT = 0x256D8D;



	private static final KeyMappingLock<VCmd> VC_LOCK = new KeyMappingLock<>(3);
	private static final KeyMappingLock<VNum> VN_LOCK = new KeyMappingLock<>(8);
	private static final Map<Pair<VCmd, VNum>, String> VL_MAP = new HashMap<>(8 + 8 + 8);

	private static final Identifier texVMenu = Mercspeak.resolveIdPath("textures/vmenu.png");

	private static MsTimer timerVMenuFade = new MsTimer(VMENU_ANIM_MS); // for both fade-in and fade-out; fade-out == !isFadeIn
	private static AgnosticTimer timerVMenuCooldown = new AgnosticTimer(VMENU_COOLDOWN_TICKS);
	private static boolean isFadeIn = true;

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
		HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Mercspeak.resolveIdPath("voice_menu"), MercspeakClient::render_hud);

		/*
		 * KEYBINDS
		 */
		KeyMapping.Category categoryVCmd = KeyMapping.Category.register(Mercspeak.resolveIdPath("voice_cmd"));
		KeyMapping.Category categoryVNum = KeyMapping.Category.register(Mercspeak.resolveIdPath("voice_num"));

		KeyMapping bindingVCmdA = KeyBindingHelper.registerKeyBinding(new ToggleKeyMapping("key.mercspeak.voice_cmd_a", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, categoryVCmd, () -> true, false));
		KeyMapping bindingVCmdB = KeyBindingHelper.registerKeyBinding(new ToggleKeyMapping("key.mercspeak.voice_cmd_b", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X, categoryVCmd, () -> true, false));
		KeyMapping bindingVCmdC = KeyBindingHelper.registerKeyBinding(new ToggleKeyMapping("key.mercspeak.voice_cmd_c", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, categoryVCmd, () -> true, false));

		KeyMapping bindingVNum1 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_1", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_1, categoryVNum));
		KeyMapping bindingVNum2 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_2", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_2, categoryVNum));
		KeyMapping bindingVNum3 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_3", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_3, categoryVNum));
		KeyMapping bindingVNum4 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_4", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_4, categoryVNum));
		KeyMapping bindingVNum5 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_5", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_5, categoryVNum));
		KeyMapping bindingVNum6 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_6", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_6, categoryVNum));
		KeyMapping bindingVNum7 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_7", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_7, categoryVNum));
		KeyMapping bindingVNum8 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_8", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_8, categoryVNum));
//		KeyMapping bindingVNum9 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_9", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_9, categoryVNum));
//		KeyMapping bindingVNum0 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_0", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_0, categoryVNum));

		VL_MAP.put(Pair.of(VCmd.VCMD_A, VNum.VNUM_1), "medic");
		VL_MAP.put(Pair.of(VCmd.VCMD_A, VNum.VNUM_2), "thanks");
		VL_MAP.put(Pair.of(VCmd.VCMD_A, VNum.VNUM_3), "go");
		VL_MAP.put(Pair.of(VCmd.VCMD_A, VNum.VNUM_4), "move");
		VL_MAP.put(Pair.of(VCmd.VCMD_A, VNum.VNUM_5), "left");
		VL_MAP.put(Pair.of(VCmd.VCMD_A, VNum.VNUM_6), "right");
		VL_MAP.put(Pair.of(VCmd.VCMD_A, VNum.VNUM_7), "yes");
		VL_MAP.put(Pair.of(VCmd.VCMD_A, VNum.VNUM_8), "no");

		VL_MAP.put(Pair.of(VCmd.VCMD_B, VNum.VNUM_1), "incoming");
		VL_MAP.put(Pair.of(VCmd.VCMD_B, VNum.VNUM_2), "spy");
		VL_MAP.put(Pair.of(VCmd.VCMD_B, VNum.VNUM_3), "sentry_ahead");
		VL_MAP.put(Pair.of(VCmd.VCMD_B, VNum.VNUM_4), "teleporter");
		VL_MAP.put(Pair.of(VCmd.VCMD_B, VNum.VNUM_5), "dispenser");
		VL_MAP.put(Pair.of(VCmd.VCMD_B, VNum.VNUM_6), "sentry_here");
		VL_MAP.put(Pair.of(VCmd.VCMD_B, VNum.VNUM_7), "ubercharge");
		VL_MAP.put(Pair.of(VCmd.VCMD_B, VNum.VNUM_8), "ubercharge_ready");

		VL_MAP.put(Pair.of(VCmd.VCMD_C, VNum.VNUM_1), "help");
		VL_MAP.put(Pair.of(VCmd.VCMD_C, VNum.VNUM_2), "cry");
		VL_MAP.put(Pair.of(VCmd.VCMD_C, VNum.VNUM_3), "cheer");
		VL_MAP.put(Pair.of(VCmd.VCMD_C, VNum.VNUM_4), "jeer");
		VL_MAP.put(Pair.of(VCmd.VCMD_C, VNum.VNUM_5), "positive");
		VL_MAP.put(Pair.of(VCmd.VCMD_C, VNum.VNUM_6), "negative");
		VL_MAP.put(Pair.of(VCmd.VCMD_C, VNum.VNUM_7), "niceshot");
		VL_MAP.put(Pair.of(VCmd.VCMD_C, VNum.VNUM_8), "goodjob");

		VC_LOCK.register(bindingVCmdA, VCmd.VCMD_A);
		VC_LOCK.register(bindingVCmdB, VCmd.VCMD_B);
		VC_LOCK.register(bindingVCmdC, VCmd.VCMD_C);

		VN_LOCK.register(bindingVNum1, VNum.VNUM_1);
		VN_LOCK.register(bindingVNum2, VNum.VNUM_2);
		VN_LOCK.register(bindingVNum3, VNum.VNUM_3);
		VN_LOCK.register(bindingVNum4, VNum.VNUM_4);
		VN_LOCK.register(bindingVNum5, VNum.VNUM_5);
		VN_LOCK.register(bindingVNum6, VNum.VNUM_6);
		VN_LOCK.register(bindingVNum7, VNum.VNUM_7);
		VN_LOCK.register(bindingVNum8, VNum.VNUM_8);
//		VN_LOCK.register(bindingVNum9, VNum.VNUM_9);
//		VN_LOCK.register(bindingVNum0, VNum.VNUM_0);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;

			timerVMenuCooldown.step(); // note that AgnosticTimers do not step if not active!

			VC_LOCK.update();
			VN_LOCK.update();

			Optional<VCmd> vcCand = VC_LOCK.poll();
			Optional<VNum> vnCand = VN_LOCK.poll();

			if (vcCand.isPresent() && vnCand.isPresent() && timerVMenuCooldown.poll()) {
				client.player.displayClientMessage(
						Component.translatable("text.mercspeak.chat_prefix", client.player.getDisplayName()).withColor(COLOR_CHAT)
								.append(Component.translatable("text.mercspeak.chat_sep")).withStyle(ChatFormatting.WHITE)
								.append(Component.translatable(String.format("text.mercspeak.%s", VL_MAP.get(Pair.of(vcCand.get(), vnCand.get()))))), false);
				timerVMenuCooldown.reset();
			}
		});
	}

	private static void render_hud(GuiGraphics context, DeltaTracker tickCounter) {
		Optional<VCmd> vcCand = VC_LOCK.poll();

		if (vcCand.isPresent()) {
			VCmd vc =  vcCand.get();
			context.blit(RenderPipelines.GUI_TEXTURED, texVMenu, VMENU_X, VMENU_Y, VMENU_U, VMENU_V, VMENU_R_WIDTH, VMENU_R_HEIGHT, VMENU_T_WIDTH, VMENU_T_HEIGHT);

			int i = 1;
			for (VNum vn : VNum.values()) {
				context.drawString(
						Minecraft.getInstance().font,
						String.format("%d. %s", i, Component.translatable(String.format("text.mercspeak.%s", VL_MAP.get(Pair.of(vc, vn)))).getString()),
						10, 50 + 15 * i,
						0xB0EFEFEF
						);
				i = (i == 8)
						? 0
						: i + 1;
			}
		}
	}

	enum VCmd {
		VCMD_A,
		VCMD_B,
		VCMD_C,
	}

	enum VNum {
		VNUM_1,
		VNUM_2,
		VNUM_3,
		VNUM_4,
		VNUM_5,
		VNUM_6,
		VNUM_7,
		VNUM_8,
//		VNUM_9,
//		VNUM_0,
	}
}

/// Lock mechanism for first of n [KeyMapping] polled in non-guaranteed order.
/// It is recommended to ensure the invariant of only one keymapping being down,
/// otherwise use [NKeyMappingLock].
class KeyMappingLock<E> {
	public KeyMappingLock(int n) {
		keymappings = new HashMap<>(n);
		lastPoll = null;
	}

	public void register(@NotNull KeyMapping mapping, @NotNull E enumVal) {
        keymappings.put(mapping, enumVal);
	}

	public void update() { // TODO: does `parallelStream` guarantee insertion order priority?
		this.lastPoll = this.keymappings.entrySet().parallelStream()
					.filter((e) -> e.getKey().isDown())
					.findFirst()
					.map(Map.Entry::getValue)
					.orElse(null);
	}

	public Optional<@NotNull E> poll() {
		return Optional.ofNullable(this.lastPoll);
	}


	private final Map<@NotNull KeyMapping, @NotNull E> keymappings;
	private @Nullable E lastPoll;
}