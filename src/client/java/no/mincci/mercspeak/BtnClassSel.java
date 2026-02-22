package no.mincci.mercspeak;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class BtnClassSel extends AbstractWidget {
    private final int index;
    private int tint;
    private boolean isFocused; // procs only once.

    public BtnClassSel(int x, int y, int width, int height, int index) {
        super(x, y, width, height, Component.empty());

        assert index < ModSounds.SOUNDPACK_CLASSNOTE.length && index >= 0
                : "Violation of: BtnClassSel index must map to existent classnote";
        this.tint = 0xFF9999FF;
        this.index = index;
    }

    @Override
    public void onClick(@NonNull MouseButtonEvent event, boolean isDoubleClick) {
    }

    @Override
    public void onRelease(@NonNull MouseButtonEvent event) {
        if (true) { // TODO: check for left-click (binded)
            VPanel.currentMerc = Mercenary.from(index).get(); // SAFETY: `index` always within bounds
            Minecraft.getInstance().setScreen(null);
        }
    }

    @Override
    protected void renderWidget(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (this.isHovered()) {
            graphics.requestCursor(this.isActive() ? CursorTypes.POINTING_HAND : CursorTypes.NOT_ALLOWED);
            tint = 0xFFFFFFFF;
            LocalPlayer player = Minecraft.getInstance().player;

            if (!isFocused && player != null) {
                player.playSound(ModSounds.SOUNDPACK_CLASSNOTE[this.index]);
                ((SoundManagerExt) Minecraft.getInstance().getSoundManager()).mercspeak$stopPrefix(Mercspeak.resolveIdFrag("misc.classnote"), null);
            }
            isFocused = true;
        } else {
            tint = 0xFF9999FF;
            isFocused = false;
        }

        graphics.fill(getX(), getY(), getX() + this.width, getY() + this.height, tint);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {
        return;
    }
}
