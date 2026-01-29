package no.mincci.mercspeak;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

public class ModSounds {
    private ModSounds() {}

    public static final SoundEvent DEMO_MEDIC = registerSound("demo.medic");
    public static final SoundEvent DEMO_THANKS = registerSound("demo.thanks");
    public static final SoundEvent DEMO_GO = registerSound("demo.go");
    public static final SoundEvent DEMO_MOVE = registerSound("demo.move");
    public static final SoundEvent DEMO_LEFT =  registerSound("demo.left");
    public static final SoundEvent DEMO_RIGHT = registerSound("demo.right");
    public static final SoundEvent DEMO_YES = registerSound("demo.yes");
    public static final SoundEvent DEMO_NO = registerSound("demo.no");
    public static final SoundEvent DEMO_INCOMING = registerSound("demo.incoming");
    public static final SoundEvent DEMO_SPY = registerSound("demo.spy");
    public static final SoundEvent DEMO_SENTRY_AHEAD = registerSound("demo.sentry_ahead");
    public static final SoundEvent DEMO_TELEPORTER = registerSound("demo.teleporter");
    public static final SoundEvent DEMO_DISPENSER =  registerSound("demo.dispenser");
    public static final SoundEvent DEMO_SENTRY_HERE = registerSound("demo.sentry_here");
    public static final SoundEvent DEMO_UBERCHARGE =  registerSound("demo.ubercharge");
    public static final SoundEvent DEMO_HELP = registerSound("demo.help");
    public static final SoundEvent DEMO_CRY = registerSound("demo.cry");
    public static final SoundEvent DEMO_CHEER = registerSound("demo.cheer");
    public static final SoundEvent DEMO_JEER = registerSound("demo.jeer");
    public static final SoundEvent DEMO_POSITIVE = registerSound("demo.positive");
    public static final SoundEvent DEMO_NEGATIVE = registerSound("demo.negative");
    public static final SoundEvent DEMO_NICESHOT = registerSound("demo.niceshot");
    public static final SoundEvent DEMO_GOODJOB = registerSound("demo.goodjob");

    public static final Map<VType, SoundEvent> DEMO_EVENTS = Map.ofEntries(
            entry(VType.MEDIC, DEMO_MEDIC),
            entry(VType.THANKS, DEMO_THANKS),
            entry(VType.GO, DEMO_GO),
            entry(VType.MOVE, DEMO_MOVE),
            entry(VType.LEFT, DEMO_LEFT),
            entry(VType.RIGHT, DEMO_RIGHT),
            entry(VType.YES, DEMO_YES),
            entry(VType.NO, DEMO_NO),

            entry(VType.INCOMING, DEMO_INCOMING),
            entry(VType.SPY, DEMO_SPY), // TODO: context-aware spy calls
            entry(VType.SENTRY_AHEAD, DEMO_SENTRY_AHEAD),
            entry(VType.TELEPORTER, DEMO_TELEPORTER),
            entry(VType.DISPENSER, DEMO_DISPENSER),
            entry(VType.SENTRY_HERE, DEMO_SENTRY_HERE),
            entry(VType.UBERCHARGE, DEMO_UBERCHARGE),
            //entry(VType.UBERCHARGE_READY, null),

            entry(VType.HELP, DEMO_HELP),
            entry(VType.CRY, DEMO_CRY),
            entry(VType.CHEER, DEMO_CHEER),
            entry(VType.JEER, DEMO_JEER),
            entry(VType.POSITIVE, DEMO_POSITIVE),
            entry(VType.NEGATIVE, DEMO_NEGATIVE),
            entry(VType.NICESHOT, DEMO_NICESHOT),
            entry(VType.GOODJOB, DEMO_GOODJOB)
    );

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Mercspeak.resolveIdPath(id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

    public static void initialize() {
        Mercspeak.LOGGER.info("Registering {} sounds!", Mercspeak.MOD_ID);
    }
}
