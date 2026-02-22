package no.mincci.mercspeak;

import net.minecraft.client.Minecraft;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.security.InvalidParameterException;
import java.util.*;

public class HUDClamp {
    private final int lenX, lenY, padX, padY, offX, offY;
    private int x, y;
    // lenX/lenY = length of HUD element
    // padX/padY = padding applied to both sides
    // offX/offY = offset of top-left corner (no padding) from magnet point
    // x/y = cached location of top-left corner (no padding, includes offset)

    private @Nullable final HUDMagnet magnetX, magnetY; // invariant: 0-2 items, no contradictions (top & bottom, right & left), no CENTER
    private final @NonNull List<HUDClamp> neighbours;

    HUDClamp(HUDClampBuilder builder) {
        this.lenX = builder.lenX;
        this.lenY = builder.lenY;
        this.padX = builder.padX;
        this.padY = builder.padY;
        this.offX = builder.offX;
        this.offY = builder.offY;
        this.x = this.y = 0;

        this.magnetX = builder.magnetX;
        this.magnetY = builder.magnetY;
        this.neighbours = builder.neighbours;
    }

    private boolean isOverlapping(HUDClamp other) {
        return this.x - this.padX > other.x - other.padX ||
                this.y - this.padY > other.y - other.padY ||
                this.x + this.lenX + this.padX < other.x + other.lenX + other.padX ||
                this.y + this.padY + this.padY < other.y + other.lenY + other.padY;
    }

    /// Returns the raw "magnet point" of top-left corner of HUD element, ignoring offset and padding.
    private int[] magnetise() {
        int winX = Minecraft.getInstance().getWindow().getWidth();
        int winY = Minecraft.getInstance().getWindow().getHeight();

        Optional<HUDMagnet> ratioMag = Optional.ofNullable(this.magnets.poll());
        Optional<HUDMagnet> modMag = Optional.ofNullable(this.magnets.poll());

        int magX = (int) (winX * ratioMag.map(m -> m.gRatioX).orElse(0f) + modMag.map(m -> m.modX).orElse(0f));
        int magY = (int) (winY * ratioMag.map(m -> m.gRatioY).orElse(0f) + modMag.map(m -> m.modY).orElse(0f));

        return new int[]{magX, magY};
    }

    // respect neighbour overlap + padding; updates cached position based on magnetisation, offset, padding.
    public int[] position() {

        return new int[] {this.x, this.y}; // update own state
    }

    // separate X and Y movement and only allow local movement to promote more efficient repositioning of neighbours!
    public void moveX(int deltaX) {
        this.offX += deltaX;
    }

    // see `moveX`
    public void moveY(int deltaY) {
        this.offY += deltaY;
    }

    public void reset() {
        this.moveX(-this.offX);
        this.moveY(-this.offY);
    }



    static class HUDClampBuilder {
        private int lenX, lenY, padX, padY, offX, offY;
        private @Nullable HUDMagnet magnetX, magnetY;
        private final List<HUDClamp> neighbours;

        private HUDClampBuilder() {
            this.lenX = this.lenY = this.padX = this.padY = this.offX = this.offY = 0;
            this.magnetX = this.magnetY = null;
            this.neighbours = new ArrayList<>();
        }

        public HUDClampBuilder len(int x, int y) {
            this.lenX = x;
            this.lenY = y;

            return this;
        }

        public HUDClampBuilder pad(int x, int y) {
            this.padX = x;
            this.padY = y;

            return this;
        }

        public HUDClampBuilder offset(int x, int y) {
            this.offX = x;
            this.offY = y;

            return this;
        }

        public HUDClampBuilder magnetX(@NonNull HUDMagnet magnet) throws InvalidParameterException {
            if (this.magnetX != null || (magnet != HUDMagnet.LEFT && magnet != HUDMagnet.RIGHT)) {
                throw new InvalidParameterException();
            }

            this.magnetX = magnet;

            return this;
        }

        public HUDClampBuilder magnetY(@NonNull HUDMagnet magnet) throws InvalidParameterException {
            if (this.magnetY != null || (magnet != HUDMagnet.TOP && magnet != HUDMagnet.BOTTOM)) {
                throw new InvalidParameterException();
            }

            this.magnetX = magnet;

            return this;
        }

        public HUDClampBuilder neighbour(HUDClamp neighbour) {
            this.neighbours.add(neighbour);

            return this;
        }

        public HUDClamp build() {
            return new HUDClamp(this);
        }
    }

    enum HUDMagnet {
        TOP(0.5f, 0f,
                0f, 0.5f),

        BOTTOM(0.5f, 1f,
                0f, -0.5f),

        LEFT(0f, 0.5f,
                -0.5f, 0f),

        RIGHT(1f, 0.5f,
                0.5f, 0f);

        HUDMagnet(float ratioX, float ratioY, float modX, float modY) {
            this.ratioX = ratioX;
            this.ratioY = ratioY;
            this.modX = modX;
            this.modY = modY;
        }

        // global ratio = +ratioX, +ratioY
        // local ratio = -ratioX, -ratioY
        private final float ratioX, ratioY, modX, modY;
    }
}
