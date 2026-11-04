package com.sxilverr.variedcommoditiesredux.block;

import com.sxilverr.variedcommoditiesredux.block.entity.StorageBlockEntity;
import net.minecraft.core.BlockPos;
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

//? if <1.21 {
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
//?}

public class StorageBlock extends FurnitureBlock {
    public StorageBlock(VCVariant variant, Supplier<? extends BlockEntityType<?>> type, Properties properties) {
        super(variant, type, full(), true, properties);
    }

    @Override
    protected InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (level.getBlockEntity(pos) instanceof StorageBlockEntity be) player.openMenu(be);
        return InteractionResult.CONSUME;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    //? if <1.21 {
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (stack.hasCustomHoverName() && level.getBlockEntity(pos) instanceof StorageBlockEntity be) be.setCustomName(stack.getHoverName());
    }
    //?}

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof StorageBlockEntity be) {
            Containers.dropContents(level, pos, be);
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, level, pos, newState, moved);
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
