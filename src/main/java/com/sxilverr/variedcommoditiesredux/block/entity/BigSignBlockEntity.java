package com.sxilverr.variedcommoditiesredux.block.entity;

import com.sxilverr.variedcommoditiesredux.block.BigSignBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BigSignBlockEntity extends VCBlockEntity {
    public boolean waxed;
    private String text = "";
    private String[] lines;

    public BigSignBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public String[] lines() {
        if (lines == null) {
            int count = getBlockState().getBlock() instanceof BigSignBlock sign ? sign.lineCount() : 0;
            String[] split = text.split("\n", -1);
            lines = new String[count];
            for (int i = 0; i < count; i++) lines[i] = i < split.length ? split[i] : "";
        }
        return lines;
    }

    public void setLines(String[] lines) {
        this.text = String.join("\n", lines).replaceAll("\n+$", "");
        this.lines = null;
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries) {
        super.write(tag, registries);
        tag.putString("SignText", text);
        tag.putBoolean("Waxed", waxed);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries) {
        super.read(tag, registries);
        text = tag.getString("SignText");
        waxed = tag.getBoolean("Waxed");
        lines = null;
    }
}
