package com.sxilverr.variedcommoditiesredux.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class VCBlock extends Block {
    public VCBlock(Properties properties) {
        super(properties);
    }

    protected InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return InteractionResult.PASS;
    }

    protected void onPlayerDestroy(Level level, BlockPos pos, BlockState state, Player player) {
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult result = onUse(state, level, pos, player, hand, hit);
        return result == InteractionResult.PASS ? InteractionResult.TRY_WITH_EMPTY_HAND : result;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        onPlayerDestroy(level, pos, state, player);
        return super.playerWillDestroy(level, pos, state, player);
    }
}
