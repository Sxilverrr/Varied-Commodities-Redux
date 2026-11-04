package com.sxilverr.variedcommoditiesredux.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import java.util.function.Supplier;

public class TableBlock extends FurnitureBlock {
    public TableBlock(VCVariant variant, Supplier<? extends BlockEntityType<?>> type, Properties properties) {
        super(variant, type, full(), true, properties);
        BlockState state = defaultBlockState();
        for (Direction direction : Direction.Plane.HORIZONTAL) state = state.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), false);
        registerDefaultState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        for (Direction direction : Direction.Plane.HORIZONTAL) builder.add(PipeBlock.PROPERTY_BY_DIRECTION.get(direction));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) return null;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            boolean table = context.getLevel().getBlockState(context.getClickedPos().relative(direction)).getBlock() instanceof TableBlock;
            state = state.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), table);
        }
        return state;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!direction.getAxis().isHorizontal()) return state;
        return state.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), neighbor.getBlock() instanceof TableBlock);
    }
}
