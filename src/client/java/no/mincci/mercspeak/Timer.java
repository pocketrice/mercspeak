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

    ///  Steps timer counter down by given interval, stopping at 0.
    public long step() {
        if (isActive) {
            this.counter = Math.max(0, this.counter - this.step);
        }
        return this.counter;
    }

    /// Lerps independent of active state since inactive freezes time.
    @Override
    public float lerp() {
        return ((float)this.counter / this.start) / this.step;
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
            if (this.counter == 0) {
                this.isActive = false;
                return TimerState.DONE;
            } else {
                return TimerState.RUNNING;
            }
        }
    }

    @Override
    public boolean isRunning() {
        return isActive && (this.counter > 0);
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

    /// Lerps dependent of active state (0 for inactive) since inactive does not freeze timer.
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
            if (Util.getMillis() > this.start_ms + this.duration) { // equivalent to prior `isMsExceeded`
                this.isActive = false;
                return TimerState.DONE;
            } else {
                return TimerState.RUNNING;
            }
        }
    }

    @Override
    public boolean isRunning() {
        return isActive && (Util.getMillis() <= this.start_ms + this.duration);
    }

    private final long duration;
    private long start_ms;
    private boolean isActive;
}

public interface Timer {
    /// Provides 0-1f depending on timer progress to end value.
    /// Pipe into an [EasingFunction] to apply an easing function.
    float lerp();
    /// Stops timer to flush and starts.
    void reset();
    ///  Deactivates timer irrespective of state.
    /// This is required for starting new timer.
    void stop();
    ///  Starts timer if inactive, else no-op.
    void start();
    ///  Polls current timer state. Polling for done will set it to inactive.
    TimerState poll();
    /// Shorthand check for if timer is running.
    /// Note this is slightly more performant than {@link MsTimer#poll()}
    /// due to branchless, and also will not trip the done poll.
    boolean isRunning();
}

enum TimerState {
    RUNNING,
    DONE,
    INACTIVE
}