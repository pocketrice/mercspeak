package no.mincci.mercspeak;

import net.minecraft.util.Util;

/// Simple decrement timer that may be used agnostically; best used
/// with intervalic consistent source.
class AgnosticTimer implements Timer {
    public AgnosticTimer(long count) { // TODO: accept any (non)mutable callback, akin to C++ fn obj?
        this(count, 1);
    }

    public AgnosticTimer(long count, long interval) {
        this.start = count;
        this.counter = this.start;
        this.step = interval;
        this.isActive = false;
    }

    public long step() {
        if (isActive) {
            this.counter = Math.max(0, this.counter - this.step);
        }
        return this.counter;
    }

    @Override
    public float lerp() {
        return (float)this.counter / this.start;
    }

    @Override
    public void reset() {
        this.counter = this.start;
        this.start();
    }

    @Override
    public void start() {
        this.isActive = true;
    }

    @Override
    public void stop() {
        this.isActive = false;
    }

    @Override
    public TimerState poll() {
        // RUNNING = !isFinished && isActive
        // DONE = isFinished && !isActive
        // INACTIVE = !isActive
        if (!isActive) {
            return TimerState.INACTIVE;
        } else {
            boolean isFinished = this.counter == 0;
            return (isFinished) ? TimerState.DONE : TimerState.RUNNING;
        }
    }

    private final long start, step;
    private long counter;
    private boolean isActive;
}

/// Analogous to [AgnosticTimer] but uses {@link Util#getMillis()}
/// instead.
class MsTimer implements Timer {
    public MsTimer(long duration_ms) {
        this.start_ms = -1;
        this.duration = duration_ms;
        this.isActive = false;
    }

    @Override
    public float lerp() {
        return (this.isActive)
                ? (float)(Math.max(0, start_ms + duration - Util.getMillis())) / duration
                : 0; // <-- technically isActive may be out-of-date, but Math.max(0) handles. Optims!
    }

    @Override
    public void reset() {
        this.stop();
        this.start();
    }

    @Override
    public void stop() {
        this.isActive = false;
    }

    @Override
    public void start() {
        if (!this.isActive) {
            this.start_ms = Util.getMillis();
            this.isActive = true;
        }
    }

    @Override
    public TimerState poll() {
        // RUNNING = !isFinished && isActive
        // DONE = isFinished && isActive
        // INACTIVE = !isActive

        if (!isActive) {
            return TimerState.INACTIVE;
        } else {
            boolean isMsExceeded = Util.getMillis() > this.start_ms + this.duration;
            return (isMsExceeded) ? TimerState.DONE : TimerState.RUNNING;
        }
    }

    private final long duration;
    private long start_ms;
    private boolean isActive;
}

public interface Timer {
    float lerp();
    void reset();
    void stop();
    void start();
    TimerState poll();
}

enum TimerState {
    RUNNING,
    DONE,
    INACTIVE
}