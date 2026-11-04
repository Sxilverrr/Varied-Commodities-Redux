package com.sxilverr.variedcommoditiesredux.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.HalfTransparentBlock;

public class CrystalBlock extends HalfTransparentBlock {
    private final DyeColor color;

    public CrystalBlock(DyeColor color, Properties properties) {
        super(properties);
        this.color = color;
    }

    public DyeColor color() {
        return color;
    }
}
