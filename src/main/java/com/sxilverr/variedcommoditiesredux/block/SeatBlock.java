package com.sxilverr.variedcommoditiesredux.block;

import com.sxilverr.variedcommoditiesredux.entity.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class SeatBlock extends FurnitureBlock {
    public SeatBlock(VCVariant variant, Supplier<? extends BlockEntityType<?>> type, VoxelShape shape, Properties properties) {
        super(variant, type, one(shape), true, properties);
    }

    @Override
    protected InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return SeatEntity.mount(level, pos, player, 0.58, facingYaw(state) + 180.0F);
    }
}
