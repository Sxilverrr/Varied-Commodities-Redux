package com.sxilverr.variedcommoditiesredux.block;

import com.sxilverr.variedcommoditiesredux.block.entity.StorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public final class DisplayBlock {
    public static final VoxelShape[] PEDESTAL = {
            FurnitureBlock.box(0.0, 0.0, 0.2, 1.0, 0.5, 0.8),
            FurnitureBlock.box(0.2, 0.0, 0.0, 0.8, 0.5, 1.0)
    };
    public static final VoxelShape[] WEAPON_RACK = {
            FurnitureBlock.box(0.0, 0.0, 0.7, 1.0, 1.0, 1.0),
            FurnitureBlock.box(0.0, 0.0, 0.0, 0.3, 1.0, 1.0),
            FurnitureBlock.box(0.0, 0.0, 0.0, 1.0, 1.0, 0.3),
            FurnitureBlock.box(0.7, 0.0, 0.0, 1.0, 1.0, 1.0)
    };
    private static final VoxelShape[] WEAPON_RACK_TOP = {
            FurnitureBlock.box(0.0, 0.0, 0.7, 1.0, 0.8, 1.0),
            FurnitureBlock.box(0.0, 0.0, 0.0, 0.3, 0.8, 1.0),
            FurnitureBlock.box(0.0, 0.0, 0.0, 1.0, 0.8, 0.3),
            FurnitureBlock.box(0.7, 0.0, 0.0, 1.0, 0.8, 1.0)
    };

    static InteractionResult swap(Level level, BlockPos pos, Player player, InteractionHand hand, int slot, boolean swordsOnly) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof StorageBlockEntity be)) return InteractionResult.CONSUME;
        ItemStack held = player.getMainHandItem();
        ItemStack stored = be.getItem(slot);
        if (held.isEmpty() && !stored.isEmpty()) {
            be.setItem(slot, ItemStack.EMPTY);
            player.setItemInHand(hand, stored);
        } else if (!held.isEmpty() && stored.isEmpty() && (swordsOnly ? held.getItem() instanceof SwordItem : !(held.getItem() instanceof BlockItem))) {
            be.setItem(slot, held);
            player.setItemInHand(hand, ItemStack.EMPTY);
        } else {
            return InteractionResult.CONSUME;
        }
        be.sync();
        updateAround(level, pos);
        return InteractionResult.CONSUME;
    }

    static int power(BlockGetter level, BlockPos pos, int perItem) {
        return level.getBlockEntity(pos) instanceof StorageBlockEntity be ? Math.min(15, be.filledSlots() * perItem) : 0;
    }

    static void drop(BlockState state, Level level, BlockPos pos, BlockState newState) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof StorageBlockEntity be) {
            Containers.dropContents(level, pos, be);
        }
    }

    public static class Pedestal extends FurnitureBlock {
        public Pedestal(VCVariant variant, Supplier<? extends BlockEntityType<?>> type, Properties properties) {
            super(variant, type, PEDESTAL, true, properties);
        }

        @Override
        protected InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            return swap(level, pos, player, hand, 0, true);
        }

        @Override
        public boolean isSignalSource(BlockState state) {
            return true;
        }

        @Override
        public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return power(level, pos, 15);
        }

        @Override
        public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return power(level, pos, 15);
        }

        @Override
        public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
            drop(state, level, pos, newState);
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    public static class WeaponRack extends TallBlock {
        public WeaponRack(VCVariant variant, Supplier<? extends BlockEntityType<?>> type, Properties properties) {
            super(variant, type, WEAPON_RACK, properties);
        }

        @Override
        protected VoxelShape topShape(BlockState state, VoxelShape bottom) {
            return WEAPON_RACK_TOP[rotation(state)];
        }

        @Override
        protected InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            Vec3 local = hit.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());
            int rotation = rotation(state);
            double h = rotation == 2 ? 1.0 - local.x : rotation == 3 ? 1.0 - local.z : rotation == 1 ? local.z : local.x;
            int slot = Math.max(0, Math.min(2, 2 - (int) (h / 0.34)));
            return swap(level, base(state, pos), player, hand, slot, false);
        }

        @Override
        public boolean isSignalSource(BlockState state) {
            return true;
        }

        @Override
        public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return power(level, base(state, pos), 5);
        }

        @Override
        public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return power(level, base(state, pos), 5);
        }

        @Override
        public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
            drop(state, level, pos, newState);
            super.onRemove(state, level, pos, newState, moved);
        }
    }

    private static void updateAround(Level level, BlockPos pos) {
        net.minecraft.world.level.block.Block block = level.getBlockState(pos).getBlock();
        level.updateNeighborsAt(pos, block);
        for (Direction direction : Direction.values()) {
            level.updateNeighborsAt(pos.relative(direction), block);
        }
    }

    private DisplayBlock() {
    }
}
