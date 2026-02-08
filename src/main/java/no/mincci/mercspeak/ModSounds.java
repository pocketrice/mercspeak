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

    public static final SoundEvent MISC_CLASSNOTE_1 = registerSound("misc.classnote_1");
    public static final SoundEvent MISC_CLASSNOTE_2 = registerSound("misc.classnote_2");
    public static final SoundEvent MISC_CLASSNOTE_3 = registerSound("misc.classnote_3");
    public static final SoundEvent MISC_CLASSNOTE_4 = registerSound("misc.classnote_4");
    public static final SoundEvent MISC_CLASSNOTE_5 = registerSound("misc.classnote_5");
    public static final SoundEvent MISC_CLASSNOTE_6 = registerSound("misc.classnote_6");
    public static final SoundEvent MISC_CLASSNOTE_7 = registerSound("misc.classnote_7");
    public static final SoundEvent MISC_CLASSNOTE_7b = registerSound("misc.classnote_7b");
    public static final SoundEvent MISC_CLASSNOTE_8 = registerSound("misc.classnote_8");
    public static final SoundEvent MISC_CLASSNOTE_9 = registerSound("misc.classnote_9");

    public static final SoundEvent[] SOUNDPACK_CLASSNOTE = {
            MISC_CLASSNOTE_1, MISC_CLASSNOTE_2, MISC_CLASSNOTE_3, MISC_CLASSNOTE_4, MISC_CLASSNOTE_5, MISC_CLASSNOTE_6, MISC_CLASSNOTE_7b, MISC_CLASSNOTE_7, MISC_CLASSNOTE_8, MISC_CLASSNOTE_9
    };

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
    public static final SoundEvent DEMO_SCHADENFREUDE = registerSound("demo.schadenfreude");
    public static final SoundEvent DEMO_HELP = registerSound("demo.help");
    public static final SoundEvent DEMO_CRY = registerSound("demo.cry");
    public static final SoundEvent DEMO_CHEER = registerSound("demo.cheer");
    public static final SoundEvent DEMO_JEER = registerSound("demo.jeer");
    public static final SoundEvent DEMO_POSITIVE = registerSound("demo.positive");
    public static final SoundEvent DEMO_NEGATIVE = registerSound("demo.negative");
    public static final SoundEvent DEMO_NICESHOT = registerSound("demo.niceshot");
    public static final SoundEvent DEMO_GOODJOB = registerSound("demo.goodjob");
    public static final SoundEvent DEMO_PAIN_SHARP = registerSound("demo.pain.sharp");

    public static final SoundEvent ENGIE_MEDIC = registerSound("engie.medic");
    public static final SoundEvent ENGIE_THANKS = registerSound("engie.thanks");
    public static final SoundEvent ENGIE_GO = registerSound("engie.go");
    public static final SoundEvent ENGIE_MOVE = registerSound("engie.move");
    public static final SoundEvent ENGIE_LEFT =  registerSound("engie.left");
    public static final SoundEvent ENGIE_RIGHT = registerSound("engie.right");
    public static final SoundEvent ENGIE_YES = registerSound("engie.yes");
    public static final SoundEvent ENGIE_NO = registerSound("engie.no");
    public static final SoundEvent ENGIE_INCOMING = registerSound("engie.incoming");
    public static final SoundEvent ENGIE_SPY = registerSound("engie.spy");
    public static final SoundEvent ENGIE_SENTRY_AHEAD = registerSound("engie.sentry_ahead");
    public static final SoundEvent ENGIE_TELEPORTER = registerSound("engie.teleporter");
    public static final SoundEvent ENGIE_DISPENSER =  registerSound("engie.dispenser");
    public static final SoundEvent ENGIE_SENTRY_HERE = registerSound("engie.sentry_here");
    public static final SoundEvent ENGIE_UBERCHARGE =  registerSound("engie.ubercharge");
    public static final SoundEvent ENGIE_SCHADENFREUDE = registerSound("engie.schadenfreude");
    public static final SoundEvent ENGIE_HELP = registerSound("engie.help");
    public static final SoundEvent ENGIE_CRY = registerSound("engie.cry");
    public static final SoundEvent ENGIE_CHEER = registerSound("engie.cheer");
    public static final SoundEvent ENGIE_JEER = registerSound("engie.jeer");
    public static final SoundEvent ENGIE_POSITIVE = registerSound("engie.positive");
    public static final SoundEvent ENGIE_NEGATIVE = registerSound("engie.negative");
    public static final SoundEvent ENGIE_NICESHOT = registerSound("engie.niceshot");
    public static final SoundEvent ENGIE_GOODJOB = registerSound("engie.goodjob");

    public static final SoundEvent SPY_MEDIC = registerSound("spy.medic");
    public static final SoundEvent SPY_THANKS = registerSound("spy.thanks");
    public static final SoundEvent SPY_GO = registerSound("spy.go");
    public static final SoundEvent SPY_MOVE = registerSound("spy.move");
    public static final SoundEvent SPY_LEFT =  registerSound("spy.left");
    public static final SoundEvent SPY_RIGHT = registerSound("spy.right");
    public static final SoundEvent SPY_YES = registerSound("spy.yes");
    public static final SoundEvent SPY_NO = registerSound("spy.no");
    public static final SoundEvent SPY_INCOMING = registerSound("spy.incoming");
    public static final SoundEvent SPY_SPY = registerSound("spy.spy");
    public static final SoundEvent SPY_SENTRY_AHEAD = registerSound("spy.sentry_ahead");
    public static final SoundEvent SPY_TELEPORTER = registerSound("spy.teleporter");
    public static final SoundEvent SPY_DISPENSER =  registerSound("spy.dispenser");
    public static final SoundEvent SPY_SENTRY_HERE = registerSound("spy.sentry_here");
    public static final SoundEvent SPY_UBERCHARGE =  registerSound("spy.ubercharge");
    public static final SoundEvent SPY_SCHADENFREUDE = registerSound("spy.schadenfreude");
    public static final SoundEvent SPY_HELP = registerSound("spy.help");
    public static final SoundEvent SPY_CRY = registerSound("spy.cry");
    public static final SoundEvent SPY_CHEER = registerSound("spy.cheer");
    public static final SoundEvent SPY_JEER = registerSound("spy.jeer");
    public static final SoundEvent SPY_POSITIVE = registerSound("spy.positive");
    public static final SoundEvent SPY_NEGATIVE = registerSound("spy.negative");
    public static final SoundEvent SPY_NICESHOT = registerSound("spy.niceshot");
    public static final SoundEvent SPY_GOODJOB = registerSound("spy.goodjob");

    // ▼ pls give me Rust macros :<
    // ▼ ...a simple `merc_map!("DEMO"); merc_map!("MEDIC")..` would have been so easy lol
    public static final Map<Pair<Mercenary, VType>, SoundEvent> SOUNDPACK_MERC = Map.<Pair<Mercenary, VType>, SoundEvent>ofEntries(
            // ╔═════════════╗
            // |    DEMO     |
            // ╚═════════════╝
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
            entry(Pair.of(Mercenary.DEMOMAN, VType.SCHADENFREUDE), DEMO_SCHADENFREUDE),

            entry(Pair.of(Mercenary.DEMOMAN, VType.HELP), DEMO_HELP),
            entry(Pair.of(Mercenary.DEMOMAN, VType.CRY), DEMO_CRY),
            entry(Pair.of(Mercenary.DEMOMAN, VType.CHEER), DEMO_CHEER),
            entry(Pair.of(Mercenary.DEMOMAN, VType.JEER), DEMO_JEER),
            entry(Pair.of(Mercenary.DEMOMAN, VType.POSITIVE), DEMO_POSITIVE),
            entry(Pair.of(Mercenary.DEMOMAN, VType.NEGATIVE), DEMO_NEGATIVE),
            entry(Pair.of(Mercenary.DEMOMAN, VType.NICESHOT), DEMO_NICESHOT),
            entry(Pair.of(Mercenary.DEMOMAN, VType.GOODJOB), DEMO_GOODJOB),


            // ╔═════════════╗
            // |    ENGIE    |
            // ╚═════════════╝
            entry(Pair.of(Mercenary.ENGINEER, VType.MEDIC), ENGIE_MEDIC),
            entry(Pair.of(Mercenary.ENGINEER, VType.THANKS), ENGIE_THANKS),
            entry(Pair.of(Mercenary.ENGINEER, VType.GO), ENGIE_GO),
            entry(Pair.of(Mercenary.ENGINEER, VType.MOVE), ENGIE_MOVE),
            entry(Pair.of(Mercenary.ENGINEER, VType.LEFT), ENGIE_LEFT),
            entry(Pair.of(Mercenary.ENGINEER, VType.RIGHT), ENGIE_RIGHT),
            entry(Pair.of(Mercenary.ENGINEER, VType.YES), ENGIE_YES),
            entry(Pair.of(Mercenary.ENGINEER, VType.NO), ENGIE_NO),

            entry(Pair.of(Mercenary.ENGINEER, VType.INCOMING), ENGIE_INCOMING),
            entry(Pair.of(Mercenary.ENGINEER, VType.SPY), ENGIE_SPY), // TODO: context-aware spy calls
            entry(Pair.of(Mercenary.ENGINEER, VType.SENTRY_AHEAD), ENGIE_SENTRY_AHEAD),
            entry(Pair.of(Mercenary.ENGINEER, VType.TELEPORTER), ENGIE_TELEPORTER),
            entry(Pair.of(Mercenary.ENGINEER, VType.DISPENSER), ENGIE_DISPENSER),
            entry(Pair.of(Mercenary.ENGINEER, VType.SENTRY_HERE), ENGIE_SENTRY_HERE),
            entry(Pair.of(Mercenary.ENGINEER, VType.UBERCHARGE), ENGIE_UBERCHARGE),
            entry(Pair.of(Mercenary.ENGINEER, VType.SCHADENFREUDE), ENGIE_SCHADENFREUDE),

            entry(Pair.of(Mercenary.ENGINEER, VType.HELP), ENGIE_HELP),
            entry(Pair.of(Mercenary.ENGINEER, VType.CRY), ENGIE_CRY),
            entry(Pair.of(Mercenary.ENGINEER, VType.CHEER), ENGIE_CHEER),
            entry(Pair.of(Mercenary.ENGINEER, VType.JEER), ENGIE_JEER),
            entry(Pair.of(Mercenary.ENGINEER, VType.POSITIVE), ENGIE_POSITIVE),
            entry(Pair.of(Mercenary.ENGINEER, VType.NEGATIVE), ENGIE_NEGATIVE),
            entry(Pair.of(Mercenary.ENGINEER, VType.NICESHOT), ENGIE_NICESHOT),
            entry(Pair.of(Mercenary.ENGINEER, VType.GOODJOB), ENGIE_GOODJOB),


            // ╔═════════════╗
            // |     SPY     |
            // ╚═════════════╝
            entry(Pair.of(Mercenary.SPY, VType.MEDIC), SPY_MEDIC),
            entry(Pair.of(Mercenary.SPY, VType.THANKS), SPY_THANKS),
            entry(Pair.of(Mercenary.SPY, VType.GO), SPY_GO),
            entry(Pair.of(Mercenary.SPY, VType.MOVE), SPY_MOVE),
            entry(Pair.of(Mercenary.SPY, VType.LEFT), SPY_LEFT),
            entry(Pair.of(Mercenary.SPY, VType.RIGHT), SPY_RIGHT),
            entry(Pair.of(Mercenary.SPY, VType.YES), SPY_YES),
            entry(Pair.of(Mercenary.SPY, VType.NO), SPY_NO),

            entry(Pair.of(Mercenary.SPY, VType.INCOMING), SPY_INCOMING),
            entry(Pair.of(Mercenary.SPY, VType.SPY), SPY_SPY), // TODO: context-aware spy calls
            entry(Pair.of(Mercenary.SPY, VType.SENTRY_AHEAD), SPY_SENTRY_AHEAD),
            entry(Pair.of(Mercenary.SPY, VType.TELEPORTER), SPY_TELEPORTER),
            entry(Pair.of(Mercenary.SPY, VType.DISPENSER), SPY_DISPENSER),
            entry(Pair.of(Mercenary.SPY, VType.SENTRY_HERE), SPY_SENTRY_HERE),
            entry(Pair.of(Mercenary.SPY, VType.UBERCHARGE), SPY_UBERCHARGE),
            entry(Pair.of(Mercenary.SPY, VType.SCHADENFREUDE), SPY_SCHADENFREUDE),

            entry(Pair.of(Mercenary.SPY, VType.HELP), SPY_HELP),
            entry(Pair.of(Mercenary.SPY, VType.CRY), SPY_CRY),
            entry(Pair.of(Mercenary.SPY, VType.CHEER), SPY_CHEER),
            entry(Pair.of(Mercenary.SPY, VType.JEER), SPY_JEER),
            entry(Pair.of(Mercenary.SPY, VType.POSITIVE), SPY_POSITIVE),
            entry(Pair.of(Mercenary.SPY, VType.NEGATIVE), SPY_NEGATIVE),
            entry(Pair.of(Mercenary.SPY, VType.NICESHOT), SPY_NICESHOT),
            entry(Pair.of(Mercenary.SPY, VType.GOODJOB), SPY_GOODJOB)
    );

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Mercspeak.resolveIdPath(id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

    public static void initialize() {
        Mercspeak.LOGGER.info("Registering {} sounds!", Mercspeak.MOD_ID);
    }
}
