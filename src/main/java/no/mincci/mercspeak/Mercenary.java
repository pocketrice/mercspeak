package no.mincci.mercspeak;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public enum Mercenary {
    SCOUT,
    SOLDIER,
    PYRO,
    DEMOMAN,
    HEAVY,
    ENGINEER,
    MEDIC,
    SNIPER,
    SPY,
    //MERCENARY,
    CIVILIAN;

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

    public static Mercenary from(int i) {
        return switch (i) {
            case 0 -> SCOUT;
            case 1 -> SOLDIER;
            case 2 -> PYRO;
            case 3 -> DEMOMAN;
            case 4 -> HEAVY;
            case 5 -> ENGINEER;
            case 6 -> MEDIC;
            case 7 -> SNIPER;
            case 8 -> SPY;
            default -> CIVILIAN;
        };
    }
}

class MercenaryArgumentType implements ArgumentType<Mercenary> {
    @Override
    public Mercenary parse(StringReader reader) throws CommandSyntaxException {
        try {
            String s = reader.readString();
            return Mercenary.valueOf(s.toUpperCase());
        } catch (Exception e) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create("Invalid Mercenary format. Expected valid name.");
        }
    }
}
