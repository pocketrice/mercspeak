package no.mincci.mercspeak;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

import static no.mincci.mercspeak.ModBinds.*;

public class ClassScreen extends Screen {
    public final Screen parent;
    private int currScrollClass;
    private final int circScrollClass; // @circnum 0-{Mercenary.len}

    public ClassScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
        this.currScrollClass = 0;
        this.circScrollClass = Mercenary.values().length - 1;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    protected void init() {
        for (int i = 0; i < circScrollClass; i++) {
            BtnClassSel classWidget = new BtnClassSel(10 + 70 * i, 150, 50, 50, i);
            this.addRenderableWidget(classWidget);
        }
    }

    // adapted from [CreativeModeInventoryScreen]
    @Override
    public boolean keyPressed(KeyEvent event) {
        if (BIND_CLASS_SEL.matches(event)) {
            this.minecraft.setScreen(null);
            return true;
        }

        Optional<VNum> vn_cand = VNA_LATCH.match(event);
        vn_cand.ifPresent(vn -> {
            Minecraft.getInstance().player.playSound(ModSounds.SOUNDPACK_CLASSNOTE[vn.index()]);
        });

        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY == 0) {
            return false;
        }

        currScrollClass = (scrollY < 0) // aside: do ternaries compile into CMOVs like C? or no difference??
                ? ++currScrollClass % circScrollClass
                : (--currScrollClass + circScrollClass) % circScrollClass;

       Minecraft.getInstance().player.playSound(ModSounds.SOUNDPACK_CLASSNOTE[currScrollClass]);

        return true;
    }

    @Override
    public void render(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        graphics.drawString(this.font, String.valueOf(currScrollClass), 40, 40 - this.font.lineHeight - 10, 0xFFFFFFFF, true);
    }
}
