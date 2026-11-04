package com.sxilverr.variedcommoditiesredux.block;

import com.sxilverr.variedcommoditiesredux.VCConfig;
import com.sxilverr.variedcommoditiesredux.block.entity.IconBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public final class IconBlocks {
    private static final VoxelShape[] WALL_BANNER = {
            FurnitureBlock.box(0.0, 0.0, 0.7, 1.0, 1.0, 1.0),
            FurnitureBlock.box(0.0, 0.0, 0.0, 0.3, 1.0, 1.0),
            FurnitureBlock.box(0.0, 0.0, 0.0, 1.0, 1.0, 0.3),
            FurnitureBlock.box(0.7, 0.0, 0.0, 1.0, 1.0, 1.0)
    };
    private static final VoxelShape[] SHOP_SIGN = {
            FurnitureBlock.box(0.0, 0.3, 0.3, 1.0, 1.0, 0.7),
            FurnitureBlock.box(0.3, 0.3, 0.0, 0.7, 1.0, 1.0)
    };

    static InteractionResult interact(Level level, BlockPos pos, Player player, InteractionHand hand, boolean dyeable) {
        if (hand != InteractionHand.MAIN_HAND || !(level.getBlockEntity(pos) instanceof IconBlockEntity be)) return InteractionResult.PASS;
        ItemStack held = player.getItemInHand(hand);
        if (held.isEmpty()) {
            if (!VCConfig.iconReset || !player.isShiftKeyDown() || be.icon.isEmpty()) return InteractionResult.PASS;
            if (!level.isClientSide) {
                be.icon = ItemStack.EMPTY;
                be.sync();
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (dyeable && held.getItem() instanceof DyeItem) {
            if (!level.isClientSide) RotatedBlock.dye(level, pos, player, hand);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (!level.isClientSide) {
            be.icon = held.copyWithCount(1);
            be.sync();
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static class Banner extends TallBlock {
        public Banner(VCVariant variant, Supplier<? extends BlockEntityType<?>> type, Properties properties) {
            super(variant, type, FurnitureBlock.full(), properties);
        }

        @Override
        protected boolean dyeable() {
            return true;
        }

        @Override
        protected InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            return interact(level, base(state, pos), player, hand, true);
        }
    }

    public static class WallBanner extends FurnitureBlock {
        public WallBanner(VCVariant variant, Supplier<? extends BlockEntityType<?>> type, Properties properties) {
            super(variant, type, WALL_BANNER, true, properties);
        }

        @Override
        protected boolean dyeable() {
            return true;
        }

        @Override
        protected InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            return interact(level, pos, player, hand, true);
        }
    }

    public static class ShopSign extends FurnitureBlock {
        public ShopSign(VCVariant variant, Supplier<? extends BlockEntityType<?>> type, Properties properties) {
            super(variant, type, SHOP_SIGN, true, properties);
        }

        @Override
        protected InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            return interact(level, pos, player, hand, false);
        }
    }

    public static class TallLamp extends TallBlock {
        public TallLamp(VCVariant variant, Supplier<? extends BlockEntityType<?>> type, Properties properties) {
            super(variant, type, new VoxelShape[]{Shapes.block()}, properties);
        }

        @Override
        protected boolean dyeable() {
            return true;
        }

        @Override
        protected InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            if (hand != InteractionHand.MAIN_HAND || !(player.getItemInHand(hand).getItem() instanceof DyeItem)) return InteractionResult.PASS;
            if (!level.isClientSide) RotatedBlock.dye(level, base(state, pos), player, hand);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    private IconBlocks() {
    }
}
