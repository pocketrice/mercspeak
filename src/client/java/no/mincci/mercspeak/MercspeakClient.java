package no.mincci.mercspeak;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class MercspeakClient implements ClientModInitializer {
	private static final int VMENU_COOLDOWN_TICKS = 40;
	private static final int VMENU_ANIM_MS = 100;
	private static final int
			VMENU_X = 3, VMENU_Y = 55,
			VMENU_U = 0, VMENU_V = 0,
			VMENU_R_WIDTH = 40 * 2, VMENU_R_HEIGHT = 64 * 2,
			VMENU_T_WIDTH = 40 * 2, VMENU_T_HEIGHT = 64 * 2;
	private static final int COLOR_CHAT = 0x256D8D;

	private static final KeyMappingLatch<VCmd> VC_LATCH = new KeyMappingLatch<>();
	private static final KeyMappingLatch<VNum> VN_LATCH = new KeyMappingLatch<>();
	private static final Map<Pair<VCmd, VNum>, String> VL_MAP = new HashMap<>(8 + 8 + 8);

	private static final Identifier texVMenu = Mercspeak.resolveIdPath("textures/vmenu.png");

	private static final MsTimer timerVMenuFade = new MsTimer(VMENU_ANIM_MS); // for both fade-in and fade-out; fade-out == !isFadeIn
	private static final AgnosticTimer timerVMenuCooldown = new AgnosticTimer(VMENU_COOLDOWN_TICKS);
	private static @Nullable VCmd activeVMenu = null;
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

		KeyMapping bindingVCmdA = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_cmd_a", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, categoryVCmd));
		KeyMapping bindingVCmdB = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_cmd_b", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X, categoryVCmd));
		KeyMapping bindingVCmdC = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_cmd_c", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, categoryVCmd));

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

		VC_LATCH.register(VCmd.VCMD_A, bindingVCmdA);
		VC_LATCH.register(VCmd.VCMD_B, bindingVCmdB);
		VC_LATCH.register(VCmd.VCMD_C, bindingVCmdC);

		VN_LATCH.register(VNum.VNUM_1, bindingVNum1);
		VN_LATCH.register(VNum.VNUM_2, bindingVNum2);
		VN_LATCH.register(VNum.VNUM_3, bindingVNum3);
		VN_LATCH.register(VNum.VNUM_4, bindingVNum4);
		VN_LATCH.register(VNum.VNUM_5, bindingVNum5);
		VN_LATCH.register(VNum.VNUM_6, bindingVNum6);
		VN_LATCH.register(VNum.VNUM_7, bindingVNum7);
		VN_LATCH.register(VNum.VNUM_8, bindingVNum8);
//		VN_LOCK.register(bindingVNum9, VNum.VNUM_9);
//		VN_LOCK.register(bindingVNum0, VNum.VNUM_0);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;

			timerVMenuCooldown.step(); // note that AgnosticTimers do not step if not vc!

			VC_LATCH.update_depress();
			VN_LATCH.update_depress();

			Optional<VCmd> vcCand = VC_LATCH.poll_depress();
			Optional<VNum> vnCand = VN_LATCH.poll_depress();

            vcCand.ifPresent(vc -> activeVMenu = (activeVMenu == vc)  // close if same keyed, otherwise set to new key.
                    ? null
                    : vc);

			if (activeVMenu != null && vnCand.isPresent() && timerVMenuCooldown.poll() != TimerState.RUNNING) {
				client.player.displayClientMessage(
						Component.translatable("text.mercspeak.chat_prefix", client.player.getDisplayName()).withColor(COLOR_CHAT)
								.append(Component.translatable("text.mercspeak.chat_sep")).withStyle(ChatFormatting.WHITE)
								.append(Component.translatable(String.format("text.mercspeak.%s", VL_MAP.get(Pair.of(activeVMenu, vnCand.get()))))), false);

				timerVMenuCooldown.reset();
				activeVMenu = null; // if call is done, close the menu!
			}
		});
	}

	private static void render_hud(GuiGraphics context, DeltaTracker tickCounter) {
		if (activeVMenu != null) {
			timerVMenuFade.start();
			float lerpFade = (isFadeIn) ? 1f - timerVMenuFade.lerp() : timerVMenuFade.lerp();
			int opacityFade = ((int)(0xB0 * lerpFade) << 0x18) + 0x00EFEFEF;
			context.blit(RenderPipelines.GUI_TEXTURED, texVMenu, VMENU_X, VMENU_Y, VMENU_U, VMENU_V, VMENU_R_WIDTH, VMENU_R_HEIGHT, VMENU_T_WIDTH, VMENU_T_HEIGHT, opacityFade);

			int i = 1;
			for (VNum vn : VNum.values()) {
				context.drawString(
						Minecraft.getInstance().font,
						String.format("%d. %s", i, Component.translatable(String.format("text.mercspeak.%s", VL_MAP.get(Pair.of(activeVMenu, vn)))).getString()),
						10, 50 + 15 * i,
						opacityFade
						);
				i++;
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

/// Latch mechanism for first of n [KeyMapping] polled guaranteed in natural ordering.
class KeyMappingLatch<E> {
	public KeyMappingLatch() {
		keymappings = new TreeMap<>();
		lastTogglePoll = lastDepressPoll = null;
	}

	public void register(@NotNull E enumVal, @NotNull KeyMapping mapping) {
        keymappings.put(enumVal, mapping);
	}

	public void update_toggle() {
		this.lastTogglePoll = this.keymappings.entrySet().stream() // considered ParallelStream, but possibly too small size to sync multithreading.
					.filter((e) -> e.getValue().isDown())
					.findFirst()
					.map(Map.Entry::getKey)
					.orElse(null);
	}

	public void update_depress() {
		this.lastDepressPoll = this.keymappings.entrySet().stream() // considered ParallelStream, but possibly too small size to sync multithreading.
				.filter((e) -> e.getValue().consumeClick())
				.findFirst()
				.map(Map.Entry::getKey)
				.orElse(null);
	}

	public void update() {
		this.update_toggle();
		this.update_depress();
	}

	public @NonNull Optional<E> poll_toggle() {
		return Optional.ofNullable(lastTogglePoll);
	}

	public @NonNull Optional<E> poll_depress() {
		return Optional.ofNullable(lastDepressPoll);
	}

	public Pair<@NonNull Optional<E>, @NonNull Optional<E>> poll() {
		return Pair.of(this.poll_toggle(), this.poll_depress());
	}

	private final TreeMap<@NotNull E, @NotNull KeyMapping> keymappings;
	private @Nullable E lastTogglePoll, lastDepressPoll;
}
//
//class NKeyMappingLatch<E> {
//	public NKeyMappingLatch(int n) {
//		this.keymappings = new HashMap<>(n);
//		this.lastPoll = new HashMap<>(n); // should not resize lastPoll!
//		this.capacity = n;
//	}
//
//	public void register(@NotNull KeyMapping mapping, @NotNull E enumVal) {
//		assert this.keymappings.size() <= this.capacity : "Cannot register beyond NKeyMappingLock capacity";
//		this.keymappings.put(mapping, enumVal);
//		this.lastPoll.put(enumVal, false);
//	}
//
//	public void update() {
//		for (Map.Entry<KeyMapping, E> entry : this.keymappings.entrySet()) {
//			this.lastPoll.put(entry.getValue(), entry.getKey().isDown());
//		}
//	}
//
//	public Map<E, Boolean> poll() {
//		return this.lastPoll;
//	}
//
//
//	private final int capacity;
//	private final Map<@NotNull KeyMapping, @NotNull E> keymappings;
//	private Map<E, Boolean> lastPoll;
//}