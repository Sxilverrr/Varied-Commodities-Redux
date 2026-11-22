package com.sxilverr.variedcommoditiesredux.block.entity;

import com.sxilverr.variedcommoditiesredux.Compat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class IconBlockEntity extends VCBlockEntity {
    public ItemStack icon = ItemStack.EMPTY;

    public IconBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        Compat.saveStack(output, "Icon", icon);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        icon = Compat.loadStack(input, "Icon");
    }
}
