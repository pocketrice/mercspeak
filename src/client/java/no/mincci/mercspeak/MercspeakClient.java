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
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class MercspeakClient implements ClientModInitializer {
	private static final int VMENU_COOLDOWN_TICKS = 30;
	private static final int VMENU_ANIM_MS = 200;
	private static final int
			VMENU_X = 3, VMENU_Y = 55,
			VMENU_U = 0, VMENU_V = 0,
			VMENU_R_WIDTH = 40 * 2, VMENU_R_HEIGHT = 64 * 2,
			VMENU_T_WIDTH = 40 * 2, VMENU_T_HEIGHT = 64 * 2;
	private static final int COLOR_CHAT = 0x256D8D;

	private static final KeyMappingLatch<VCmd> VC_LATCH = new KeyMappingLatch<>();
	private static final KeyMappingLatch<VNum> VN_LATCH = new KeyMappingLatch<>();
	private static final Map<VCmd, String[]> VL_MAP = new HashMap<>(8 + 8 + 8);

	private static final Identifier texVMenu = Mercspeak.resolveIdPath("textures/vmenu.png");

	private static final MsTimer timerVMenuFade = new MsTimer(VMENU_ANIM_MS); // for both fade-in and fade-out; fade-out == !isFadeIn
	private static final AgnosticTimer timerVMenuCooldown = new AgnosticTimer(VMENU_COOLDOWN_TICKS);
	private static @Nullable VCmd activeVMenu;
	private static boolean isFadeIn = true;

	private static String[] activeVNums;
	private static Mercenary currentMerc = Mercenary.DEMOMAN;


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
		KeyMapping.Category categoryVMenu = KeyMapping.Category.register(Mercspeak.resolveIdPath("voice_menu"));
		KeyMapping.Category categoryVMisc = KeyMapping.Category.register(Mercspeak.resolveIdPath("voice_misc"));

		KeyMapping bindingVCmdA = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_cmd_a", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, categoryVMenu));
		KeyMapping bindingVCmdB = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_cmd_b", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X, categoryVMenu));
		KeyMapping bindingVCmdC = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_cmd_c", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, categoryVMenu));

		KeyMapping bindingVNum1 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_1", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_1, categoryVMenu));
		KeyMapping bindingVNum2 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_2", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_2, categoryVMenu));
		KeyMapping bindingVNum3 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_3", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_3, categoryVMenu));
		KeyMapping bindingVNum4 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_4", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_4, categoryVMenu));
		KeyMapping bindingVNum5 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_5", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_5, categoryVMenu));
		KeyMapping bindingVNum6 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_6", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_6, categoryVMenu));
		KeyMapping bindingVNum7 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_7", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_7, categoryVMenu));
		KeyMapping bindingVNum8 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_8", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_8, categoryVMenu));
//		KeyMapping bindingVNum9 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_9", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_9, categoryVNum));
//		KeyMapping bindingVNum0 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_num_0", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_0, categoryVNum));

		KeyMapping bindingKill = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.bind_kill", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_EQUAL, categoryVMisc));
		KeyMapping bindingExplode = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.bind_explode", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_MINUS, categoryVMisc));
		KeyMapping bindingTaunt = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.taunt", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, categoryVMisc));

		String[] vNumsetA = Arrays.stream(VType.category(VCmd.VCMD_A))
				.map(vt -> String.format("text.mercspeak.%s", vt.name().toLowerCase()))
				.toArray(String[]::new);
		String[] vNumsetB = Arrays.stream(VType.category(VCmd.VCMD_B))
				.map(vt -> String.format("text.mercspeak.%s", vt.name().toLowerCase()))
				.toArray(String[]::new);
		String[] vNumsetC = Arrays.stream(VType.category(VCmd.VCMD_C))
				.map(vt -> String.format("text.mercspeak.%s", vt.name().toLowerCase()))
				.toArray(String[]::new);

		VL_MAP.put(VCmd.VCMD_A, vNumsetA);
		VL_MAP.put(VCmd.VCMD_B, vNumsetB);
		VL_MAP.put(VCmd.VCMD_C, vNumsetC);

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

            vcCand.ifPresent(vc -> {
				if (!timerVMenuFade.isRunning()) { // close if same keyed, otherwise set to new key. Lock if animation is running.

					// activeVMenu == vc (FADE OUT)
					// activeVMenu == null (FADE IN)

					// this is compacted (more efficient?) form of the following:
					// ```
					// if (activeVMenu == vc) {
					//		timerVMenuFade.reset();
					//		isFadeIn = false;
					//		activeVMenu = null*;
					// } elif (activeVMenu == null) {
					//		timerVMenuFade.reset();
					//		isFadeIn = true;
					//		activeVMenu = vc;
					// } else {
					//		activeVMenu = vc;
 					// }

					// ...again simple non-const pattern matching from Rust would have been so nice :<
					boolean isMenuNegation = (activeVMenu == vc);
					boolean wasMenuClosed = (activeVMenu == null);

					if (isMenuNegation || wasMenuClosed) { // initiate fade anim?
						timerVMenuFade.reset();
						isFadeIn = wasMenuClosed;
					}

					if (isMenuNegation) { // handle vmenu switch/close
						activeVMenu = null;
					} else {
						activeVMenu = vc;
						activeVNums = VL_MAP.get(vc);
					}
				}
			});

			if (activeVMenu != null && vnCand.isPresent() && !timerVMenuCooldown.isRunning() ) { // if not locked fading out and not on cooldown, proc vcmd.
				VNum vn = vnCand.get();
				client.player.displayClientMessage(
						Component.translatable("text.mercspeak.chat_prefix", client.player.getDisplayName()).withColor(COLOR_CHAT)
								.append(Component.translatable("text.mercspeak.chat_sep")).withStyle(ChatFormatting.WHITE)
								.append(Component.translatable(activeVNums[vn.index()])), false);
				client.player.playSound(ModSounds.MERC_EVENTS.get(Pair.of(currentMerc, VType.from(activeVMenu, vn))), 1f, 1f);

				timerVMenuCooldown.reset();
				timerVMenuFade.reset();
				activeVMenu = null;
				isFadeIn = false;
				//activeVMenu = null; // if call is done, close the menu!
			}
		});

		ServerTickEvents.START_SERVER_TICK.register(server -> {

		});

	}

	private static void render_hud(GuiGraphics context, DeltaTracker tickCounter) {
		if (activeVMenu != null || timerVMenuFade.isRunning()) {
			timerVMenuFade.start();
			float lerpFade = (isFadeIn) ? 1f - timerVMenuFade.lerp() : timerVMenuFade.lerp();
			int opacityFade = ((int)(0xB0 * lerpFade) << 0x18) + 0x00EFEFEF;
			context.blit(RenderPipelines.GUI_TEXTURED, texVMenu, VMENU_X, VMENU_Y, VMENU_U, VMENU_V, VMENU_R_WIDTH, VMENU_R_HEIGHT, VMENU_T_WIDTH, VMENU_T_HEIGHT, opacityFade);

			Font fontMc = Minecraft.getInstance().font;
			int i = 1;
			for (String vn : activeVNums) {
				context.drawString(
						fontMc,
						String.format("%d. %s", i, Component.translatable(vn).getString()),
						10, 50 + 15 * i,
						opacityFade
						);
				i++;
			}
		}
	}
}

/// Latch mechanism for first of n [KeyMapping] polled guaranteed in natural ordering.
class KeyMappingLatch<E> {
	public KeyMappingLatch() {
		keymappings = new TreeMap<>();
		lastTogglePoll = lastDepressPoll = null;
	}

	public void register(@NonNull E enumVal, @NonNull KeyMapping mapping) {
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

	private final TreeMap<@NonNull E, @NonNull KeyMapping> keymappings;
	private @Nullable E lastTogglePoll, lastDepressPoll;
}