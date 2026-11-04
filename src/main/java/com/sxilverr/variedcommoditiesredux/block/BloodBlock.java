package com.sxilverr.variedcommoditiesredux.block;

import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

//? if >=1.21 {
/*import com.mojang.serialization.MapCodec;
*///?}

public class BloodBlock extends MultifaceBlock {
    //? if >=1.21 {
    /*private static final MapCodec<BloodBlock> CODEC = simpleCodec(BloodBlock::new);
    *///?}
    private final MultifaceSpreader spreader = new MultifaceSpreader(this);

    public BloodBlock(Properties properties) {
        super(properties);
    }

    //? if >=1.21 {
    /*@Override
    protected MapCodec<BloodBlock> codec() {
        return CODEC;
    }
    *///?}

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(RotatedBlock.ROTATION);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null || context.getLevel().getBlockState(context.getClickedPos()).is(this)) return state;
        float yaw = context.getPlayer() == null ? 0.0F : context.getPlayer().getYRot();
        return state.setValue(RotatedBlock.ROTATION, Mth.floor(yaw / 90.0F + 0.5F) & 3);
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return spreader;
    }
}
