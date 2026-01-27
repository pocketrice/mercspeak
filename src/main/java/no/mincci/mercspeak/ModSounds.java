package no.mincci.mercspeak;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    private ModSounds() {}

    private static final SoundEvent DEMO_MEDIC = registerSound("demo.medic");
    private static final SoundEvent DEMO_THANKS = registerSound("demo.thanks");
    private static final SoundEvent DEMO_GO = registerSound("demo.go");
    private static final SoundEvent DEMO_MOVE = registerSound("demo.move");
    private static final SoundEvent DEMO_LEFT =  registerSound("demo.left");
    private static final SoundEvent DEMO_RIGHT = registerSound("demo.right");
    private static final SoundEvent DEMO_YES = registerSound("demo.yes");
    private static final SoundEvent DEMO_NO = registerSound("demo.no");
    private static final SoundEvent DEMO_INCOMING = registerSound("demo.incoming");
    private static final SoundEvent DEMO_SPY = registerSound("demo.spy");
    private static final SoundEvent DEMO_SENTRY_AHEAD = registerSound("demo.sentry_ahead");
    private static final SoundEvent DEMO_TELEPORTER = registerSound("demo.teleporter");
    private static final SoundEvent DEMO_DISPENSER =  registerSound("demo.dispenser");
    private static final SoundEvent DEMO_SENTRY_HERE = registerSound("demo.sentry_here");
    private static final SoundEvent DEMO_UBERCHARGE =  registerSound("demo.ubercharge");
    private static final SoundEvent DEMO_HELP = registerSound("demo.help");
    private static final SoundEvent DEMO_CRY = registerSound("demo.cry");
    private static final SoundEvent DEMO_CHEER = registerSound("demo.cheer");
    private static final SoundEvent DEMO_JEER = registerSound("demo.jeer");
    private static final SoundEvent DEMO_POSITIVE = registerSound("demo.positive");
    private static final SoundEvent DEMO_NEGATIVE = registerSound("demo.negative");
    private static final SoundEvent DEMO_NICESHOT = registerSound("demo.niceshot");
    private static final SoundEvent DEMO_GOODJOB = registerSound("demo.goodjob");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Mercspeak.resolveIdPath(id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

    public static void initialize() {
        Mercspeak.LOGGER.info("Registering {} sounds!", Mercspeak.MOD_ID);
    }
}
