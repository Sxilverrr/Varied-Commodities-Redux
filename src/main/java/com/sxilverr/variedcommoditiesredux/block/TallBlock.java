package com.sxilverr.variedcommoditiesredux.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class TallBlock extends FurnitureBlock {
    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public TallBlock(VCVariant variant, Supplier<? extends BlockEntityType<?>> type, VoxelShape[] shapes, Properties properties) {
        super(variant, type, shapes, true, properties);
        registerDefaultState(defaultBlockState().setValue(TOP, false));
    }

    public static BlockPos base(BlockState state, BlockPos pos) {
        return state.getValue(TOP) ? pos.below() : pos;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TOP);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos above = context.getClickedPos().above();
        if (above.getY() >= context.getLevel().getMaxBuildHeight() || !context.getLevel().getBlockState(above).canBeReplaced(context)) {
            return null;
        }
        return super.getStateForPlacement(context);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        level.setBlock(pos.above(), state.setValue(TOP, true), 3);
    }

    @Override
    protected void onPlayerDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockPos other = state.getValue(TOP) ? pos.below() : pos.above();
        if (level.getBlockState(other).is(this)) {
            level.setBlock(other, Blocks.AIR.defaultBlockState(), 35);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockPos other = state.getValue(TOP) ? pos.below() : pos.above();
            if (level.getBlockState(other).is(this)) {
                level.removeBlock(other, false);
            }
        }
        super.onRemove(state, level, pos, newState, moved);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = super.getShape(state, level, pos, context);
        return state.getValue(TOP) ? topShape(state, shape) : shape;
    }

    protected VoxelShape topShape(BlockState state, VoxelShape bottom) {
        return bottom;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(TOP) ? null : super.newBlockEntity(pos, state);
    }
}
