package com.sxilverr.variedcommoditiesredux.block;

import com.sxilverr.variedcommoditiesredux.block.entity.StorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;


public class StorageBlock extends FurnitureBlock {
    public StorageBlock(VCVariant variant, Supplier<? extends BlockEntityType<?>> type, Properties properties) {
        super(variant, type, full(), true, properties);
    }

    @Override
    protected InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (level.getBlockEntity(pos) instanceof StorageBlockEntity be) player.openMenu(be);
        return InteractionResult.CONSUME;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean moved) {
        Containers.updateNeighboursAfterDestroy(state, level, pos);
    }

    public static class Barrel extends StorageBlock {
        public Barrel(VCVariant variant, Supplier<? extends BlockEntityType<?>> type, Properties properties) {
            super(variant, type, properties);
        }

        @Override
        protected int maxRotation() {
            return 8;
        }
    }
}
