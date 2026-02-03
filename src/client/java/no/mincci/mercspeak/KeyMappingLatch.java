package no.mincci.mercspeak;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.input.KeyEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/// [KeyMapping] "mux" with lazy polling and deterministic (natural order) conflict priority.
/// Best practice is using an enum for [E].
public class KeyMappingLatch<E> {
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

    public Optional<E> match(KeyEvent event) {
        return this.keymappings.entrySet().stream()
                .filter((e) -> e.getValue().matches(event))
                .findFirst()
                .map(Map.Entry::getKey);
    }

    public void copy(@NonNull KeyMappingLatch<E> other) {
        this.keymappings.putAll(other.keymappings);
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
