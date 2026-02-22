package no.mincci.mercspeak;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.Optional;

public enum Mercenary {
    SCOUT("scout"),
    SOLDIER("soldier"),
    PYRO("pyro"),
    DEMOMAN("demo"),
    HEAVY("heavy"),
    ENGINEER("engie"),
    MEDIC("medic"),
    SNIPER("sniper"),
    SPY("spy"),
    //MERCENARY,
    CIVILIAN("civvie");

    Mercenary(String abbrev) {
        this.abbrev = abbrev;
    }

    public static Optional<Mercenary> from(String abbrev) {
        return Optional.ofNullable(switch (abbrev) {
            case "scout" -> SCOUT;
            case "soldier" -> SOLDIER;
            case "pyro" -> PYRO;
            case "demo" -> DEMOMAN;
            case "heavy" -> HEAVY;
            case "engie" -> ENGINEER;
            case "medic" -> MEDIC;
            case "sniper" -> SNIPER;
            case "spy" -> SPY;
            case "civvie" -> CIVILIAN;
            default -> null;
        });
    }

    public static Mercenary from(VNum vn) {
        return switch (vn) {
            case VNUM_1 -> SCOUT;
            case VNUM_2 -> SOLDIER;
            case VNUM_3 -> PYRO;
            case VNUM_4 -> DEMOMAN;
            case VNUM_5 -> HEAVY;
            case VNUM_6 -> ENGINEER;
            case VNUM_7 -> MEDIC;
            case VNUM_8 -> SNIPER;
            case VNUM_9 -> SPY;
            case VNUM_0 -> CIVILIAN;
        };
    }

    public static Optional<Mercenary> from(int i) {
        return Optional.ofNullable(switch (i) {
            case 0 -> SCOUT;
            case 1 -> SOLDIER;
            case 2 -> PYRO;
            case 3 -> DEMOMAN;
            case 4 -> HEAVY;
            case 5 -> ENGINEER;
            case 6 -> MEDIC;
            case 7 -> SNIPER;
            case 8 -> SPY;
            case 9 -> CIVILIAN;
            default -> null;
        });
    }

    @Override
    public String toString() {
        return this.abbrev;
    }

    private String abbrev;
}

class MercenaryArgumentType implements ArgumentType<Mercenary> {
    @Override
    public Mercenary parse(StringReader reader) throws CommandSyntaxException {
        try {
            String s = reader.readString();
            return Mercenary.valueOf(s.toUpperCase());
        } catch (Exception e) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create("expected valid mercenary name");
        }
    }
}
