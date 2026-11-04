package com.sxilverr.variedcommoditiesredux.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class ShelfBlock extends FurnitureBlock {
    private static final Direction[] LEFT_SIDE = {Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH};

    public ShelfBlock(VCVariant variant, Supplier<? extends BlockEntityType<?>> type, VoxelShape[] shapes, Properties properties) {
        super(variant, type, shapes, true, properties);
        registerDefaultState(defaultBlockState().setValue(LEFT, false).setValue(RIGHT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LEFT, RIGHT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : connect(context.getLevel(), context.getClickedPos(), state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return direction.getAxis().isHorizontal() ? connect(level, pos, state) : state;
    }

    private static BlockState connect(BlockGetter level, BlockPos pos, BlockState state) {
        int rotation = rotation(state);
        Direction left = LEFT_SIDE[rotation];
        return state.setValue(LEFT, sameShelf(level, pos.relative(left), rotation))
                .setValue(RIGHT, sameShelf(level, pos.relative(left.getOpposite()), rotation));
    }

    private static boolean sameShelf(BlockGetter level, BlockPos pos, int rotation) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof ShelfBlock && rotation(state) == rotation;
    }
}
