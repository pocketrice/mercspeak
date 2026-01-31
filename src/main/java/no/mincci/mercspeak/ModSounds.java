package no.mincci.mercspeak;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import org.apache.commons.lang3.tuple.Pair;

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

    // ▼ pls give me Rust macros :<
    // ▼ ...a simple `merc_map!("DEMO"); merc_map!("MEDIC")..` would have been so easy lol
    public static final Map<Pair<Mercenary, VType>, SoundEvent> MERC_EVENTS = Map.ofEntries(
            entry(Pair.of(Mercenary.DEMOMAN, VType.MEDIC), DEMO_MEDIC),
            entry(Pair.of(Mercenary.DEMOMAN, VType.THANKS), DEMO_THANKS),
            entry(Pair.of(Mercenary.DEMOMAN, VType.GO), DEMO_GO),
            entry(Pair.of(Mercenary.DEMOMAN, VType.MOVE), DEMO_MOVE),
            entry(Pair.of(Mercenary.DEMOMAN, VType.LEFT), DEMO_LEFT),
            entry(Pair.of(Mercenary.DEMOMAN, VType.RIGHT), DEMO_RIGHT),
            entry(Pair.of(Mercenary.DEMOMAN, VType.YES), DEMO_YES),
            entry(Pair.of(Mercenary.DEMOMAN, VType.NO), DEMO_NO),

            entry(Pair.of(Mercenary.DEMOMAN, VType.INCOMING), DEMO_INCOMING),
            entry(Pair.of(Mercenary.DEMOMAN, VType.SPY), DEMO_SPY), // TODO: context-aware spy calls
            entry(Pair.of(Mercenary.DEMOMAN, VType.SENTRY_AHEAD), DEMO_SENTRY_AHEAD),
            entry(Pair.of(Mercenary.DEMOMAN, VType.TELEPORTER), DEMO_TELEPORTER),
            entry(Pair.of(Mercenary.DEMOMAN, VType.DISPENSER), DEMO_DISPENSER),
            entry(Pair.of(Mercenary.DEMOMAN, VType.SENTRY_HERE), DEMO_SENTRY_HERE),
            entry(Pair.of(Mercenary.DEMOMAN, VType.UBERCHARGE), DEMO_UBERCHARGE),
            //entry(VType.UBERCHARGE_READY, null),

            entry(Pair.of(Mercenary.DEMOMAN, VType.HELP), DEMO_HELP),
            entry(Pair.of(Mercenary.DEMOMAN, VType.CRY), DEMO_CRY),
            entry(Pair.of(Mercenary.DEMOMAN, VType.CHEER), DEMO_CHEER),
            entry(Pair.of(Mercenary.DEMOMAN, VType.JEER), DEMO_JEER),
            entry(Pair.of(Mercenary.DEMOMAN, VType.POSITIVE), DEMO_POSITIVE),
            entry(Pair.of(Mercenary.DEMOMAN, VType.NEGATIVE), DEMO_NEGATIVE),
            entry(Pair.of(Mercenary.DEMOMAN, VType.NICESHOT), DEMO_NICESHOT),
            entry(Pair.of(Mercenary.DEMOMAN, VType.GOODJOB), DEMO_GOODJOB)
    );

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Mercspeak.resolveIdPath(id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

    public static void initialize() {
        Mercspeak.LOGGER.info("Registering {} sounds!", Mercspeak.MOD_ID);
    }
}
