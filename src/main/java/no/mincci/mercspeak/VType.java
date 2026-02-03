package no.mincci.mercspeak;

import java.util.Arrays;

public enum VType {
    MEDIC(VCmd.VCMD_A, VNum.VNUM_1),
    THANKS(VCmd.VCMD_A, VNum.VNUM_2),
    GO(VCmd.VCMD_A, VNum.VNUM_3),
    MOVE(VCmd.VCMD_A, VNum.VNUM_4),
    LEFT(VCmd.VCMD_A, VNum.VNUM_5),
    RIGHT(VCmd.VCMD_A, VNum.VNUM_6),
    YES(VCmd.VCMD_A, VNum.VNUM_7),
    NO(VCmd.VCMD_A, VNum.VNUM_8),

    INCOMING(VCmd.VCMD_B, VNum.VNUM_1),
    SPY(VCmd.VCMD_B, VNum.VNUM_2),
    SENTRY_AHEAD(VCmd.VCMD_B, VNum.VNUM_3),
    TELEPORTER(VCmd.VCMD_B, VNum.VNUM_4),
    DISPENSER(VCmd.VCMD_B, VNum.VNUM_5),
    SENTRY_HERE(VCmd.VCMD_B, VNum.VNUM_6),
    UBERCHARGE(VCmd.VCMD_B, VNum.VNUM_7),
    SCHADENFREUDE(VCmd.VCMD_B, VNum.VNUM_8),

    HELP(VCmd.VCMD_C, VNum.VNUM_1),
    CRY(VCmd.VCMD_C, VNum.VNUM_2),
    CHEER(VCmd.VCMD_C, VNum.VNUM_3),
    JEER(VCmd.VCMD_C, VNum.VNUM_4),
    POSITIVE(VCmd.VCMD_C, VNum.VNUM_5),
    NEGATIVE(VCmd.VCMD_C, VNum.VNUM_6),
    NICESHOT(VCmd.VCMD_C, VNum.VNUM_7),
    GOODJOB(VCmd.VCMD_C, VNum.VNUM_8);

    VType(VCmd vc, VNum vn) {
        this.vc = vc;
        this.vn = vn;
    }

    public static VType from(VCmd vc, VNum vn) {
        return Arrays.stream(VType.values())
                .filter(vt -> vt.vc == vc && vt.vn == vn)
                .findFirst()
                .get(); // SAFETY: every VCmd and VNum value always map to an existent VType.
    }

    public static VType[] category(VCmd vc) {
        return Arrays.stream(VType.values()) // note: while more efficient to pre-alloc 8 slots, don't assume it will always stay 8! for the modders!! :>
                .filter(vt -> vt.vc == vc)
                .toArray(VType[]::new);
    }

    final VCmd vc;
    final VNum vn;
}
