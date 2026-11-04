package com.sxilverr.variedcommoditiesredux.block;

public enum VCVariant {
    OAK("oak"),
    SPRUCE("spruce"),
    BIRCH("birch"),
    JUNGLE("jungle"),
    ACACIA("acacia"),
    DARK_OAK("dark_oak"),
    WOODEN("wooden"),
    STONE("stone"),
    IRON("iron"),
    GOLDEN("golden"),
    DIAMOND("diamond");

    public static final VCVariant[] WOODS = {OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK};
    public static final VCVariant[] MATERIALS = {WOODEN, STONE, IRON, GOLDEN, DIAMOND};

    private final String id;

    VCVariant(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public int index() {
        return this.ordinal() >= WOODEN.ordinal() ? this.ordinal() - WOODEN.ordinal() : this.ordinal();
    }

}
