package no.mincci.mercspeak;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class VPanel {
    private static final int VMENU_COOLDOWN_TICKS = 30;
    private static final int VMENU_ANIM_MS = 400;
    private static final int
            VMENU_X = 3, VMENU_Y = 55,
            VMENU_U = 0, VMENU_V = 0,
            VMENU_R_WIDTH = 40 * 2, VMENU_R_HEIGHT = 64 * 2,
            VMENU_T_WIDTH = 40 * 2, VMENU_T_HEIGHT = 64 * 2;
    private static final int COLOR_CHAT = 0x256D8D;

    private static final Identifier texVMenu = Mercspeak.resolveId("textures/vmenu.png");

    private static final MsTimer timerVMenuFade = new MsTimer(VMENU_ANIM_MS); // for both fade-in and fade-out; fade-out == !isFadeIn
    private static final AgnosticTimer timerVMenuCooldown = new AgnosticTimer(VMENU_COOLDOWN_TICKS);
    private static @Nullable VCmd activeVMenu;
    private static boolean isFadeIn = true;

    private static String[] activeVNums;
    protected static Mercenary currentMerc = Mercenary.SPY;



    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (ModBinds.BIND_CLASS_SEL.consumeClick()) {
                Minecraft.getInstance().setScreen(new ClassScreen(
                        Component.empty(),
                        Minecraft.getInstance().screen));
            }



            timerVMenuCooldown.step(); // note that AgnosticTimers do not step if not vc!

            ModBinds.VC_LATCH.update_depress();
            ModBinds.VN_LATCH.update_depress();

            Optional<VCmd> vcCand = ModBinds.VC_LATCH.poll_depress();
            Optional<VNum> vnCand = ModBinds.VN_LATCH.poll_depress();

            vcCand.ifPresent(vc -> {
                if (isFadeIn || !timerVMenuFade.isRunning()) { // close if same keyed, otherwise set to new key. Lock if animation is running (only fade out).

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
                        activeVNums = ModBinds.VL_MAP.get(vc);
                    }
                }
            });

            if (activeVMenu != null && vnCand.isPresent() && !timerVMenuCooldown.isRunning() ) { // if not locked fading out and not on cooldown, proc vcmd.
                VNum vn = vnCand.get();
                client.player.displayClientMessage(
                        Component.translatable("text.mercspeak.chat_prefix", client.player.getDisplayName()).withColor(COLOR_CHAT)
                                .append(Component.translatable("text.mercspeak.chat_sep")).withStyle(ChatFormatting.WHITE)
                                .append(Component.translatable(activeVNums[vn.index()])), false);

                MSoundPlayC2SPayload payload = new MSoundPlayC2SPayload(Mercspeak.resolveId(
                        String.format("%s.%s", currentMerc.toString(), VType.from(activeVMenu, vn))),
                        client.player.getUUID());
                ClientPlayNetworking.send(payload);

                //client.player.playSound(ModSounds.SOUNDPACK_MERC.get(Pair.of(currentMerc, VType.from(activeVMenu, vn))), 1f, 1f);

                timerVMenuCooldown.reset();
                timerVMenuFade.reset();
                activeVMenu = null;
                isFadeIn = false;
                //activeVMenu = null; // if call is done, close the menu!
            }
        });
    }

    protected static void render(GuiGraphics context, DeltaTracker tickCounter) {
        if (activeVMenu != null || timerVMenuFade.isRunning()) {
            timerVMenuFade.start();
            double lerpFade = (isFadeIn)
                    ? EasingFunction.EASE_OUT_EXPONENTIAL.apply(1f - timerVMenuFade.lerp())
                    : EasingFunction.EASE_IN_CIRCULAR.apply(timerVMenuFade.lerp());
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
