package no.mincci.mercspeak;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

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
    public void onClick(MouseButtonEvent event, boolean isDoubleClick) {
    }

    @Override
    public void onRelease(MouseButtonEvent event) {
        if (true) { // TODO: check for left-click (binded)
            MercspeakClient.currentMerc = Mercenary.from(index);
            Minecraft.getInstance().setScreen(null);
        }
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (this.isHovered()) {
            graphics.requestCursor(this.isActive() ? CursorTypes.POINTING_HAND : CursorTypes.NOT_ALLOWED);
            tint = 0xFFFFFFFF;

            if (!isFocused) {
                Minecraft.getInstance().player.playSound(ModSounds.SOUNDPACK_CLASSNOTE[this.index]);
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
