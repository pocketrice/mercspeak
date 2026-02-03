package no.mincci.mercspeak;

public enum VNum {
    VNUM_1(0),
    VNUM_2(1),
    VNUM_3(2),
    VNUM_4(3),
    VNUM_5(4),
    VNUM_6(5),
    VNUM_7(6),
    VNUM_8(7),
    VNUM_9(8),
    VNUM_0(9);

    VNum(int i) {
        this.index = i;
    }

    public int index() {
        return this.index;
    }

    private final int index;
}