package no.mincci.mercspeak;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.client.sounds.Weighted;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

import static org.apache.commons.lang3.ArrayUtils.swap;

@Environment(EnvType.CLIENT)
public class RndWaveSoundEvents extends WeighedSoundEvents {
    private int ptr;
    private int @Nullable [] inds;

    // trick is to shuffle the sounds list (clobbers order) and use a pointer to
    // iterate through, avoiding "guess and check" and the like.

    // TODO: figure out if can get `list` by reference and then shuffle it using `RandomSource`.
    //       ...or is it better to call `RandomSource` once every time and defer picked items to back? (it is!)

    public RndWaveSoundEvents(Identifier location, @Nullable String subtitleKey) {
        super(location, subtitleKey);
        this.ptr = 0;
        // `this.sounds` begins as empty, then is filled incrementally once! we can precache* based on what SoundEventsRegistration has instead!
        // *note: please see `SoundManager.handleRegistration()V`, SoundsEventRegistration has max theoretical count but if invalid file it will be omitted from `this.sounds`.
        //        ...hence we make valid RndWaveSoundEvents by injecting `validate()V` after all sounds are added.
        //        ...YOU MUST VALIDATE THIS BEFORE CALLING ANYTHING ON IT!!! :p
    }

    /// `this` should only be validated once, and prior to any usage of `ptr`.
    public void validate() {
        this.inds = IntStream.range(0, ((WeighedSoundEventsExt) this).mercspeak$lenSounds()).toArray();
    }


    /// In-place shuffle, guaranteeing first slot will not match previous permutation's last slot (repeated sound).<br>
    /// Adapted verbatim from {@link Collections#shuffle(List, RandomGenerator)}.
    public void shuffle(RandomSource randomSource) {
        if (this.inds.length <= 2) { // 0 and 1 make sense, but 2 is b/c shuffling guarantees [0,1] <-> [1,0] so no need to shuffle (that even breaks order too!)
            return;
        }

        // Shuffle [0, max-1] items
        for (int i = this.inds.length - 1; i > 1; i--) {
            swap(this.inds, i-1, randomSource.nextInt(i)); // ...exclusive
        }

        // Shuffle max item (last played sound)
        swap(this.inds, this.inds.length - 1, randomSource.nextIntBetweenInclusive(0, this.inds.length - 2)); // ...inclusive
    }

    /// This call only works if `this` has been {@link RndWaveSoundEvents#validate() validated}.
    @Override
    public @NonNull Sound getSound(@NonNull RandomSource randomSource) { // <-- note: when playing a SoundEvent using RndWaveSoundEvents
        if (this.ptr == 0) {
            this.shuffle(randomSource);
        }

        Weighted<Sound> choice = ((WeighedSoundEventsExt) this).mercspeak$getSounds(this.inds[this.ptr]);
        this.ptr = (this.ptr + 1) % this.inds.length;

        return choice.getSound(randomSource); // note: pedantic but this call just extracts the sound from wrapper, not randomly picking from set.
    }
}
