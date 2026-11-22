package com.sxilverr.variedcommoditiesredux.block.entity;

import com.sxilverr.variedcommoditiesredux.block.BigSignBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putString("SignText", text);
        output.putBoolean("Waxed", waxed);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        text = input.getStringOr("SignText", "");
        waxed = input.getBooleanOr("Waxed", false);
        lines = null;
    }
}
