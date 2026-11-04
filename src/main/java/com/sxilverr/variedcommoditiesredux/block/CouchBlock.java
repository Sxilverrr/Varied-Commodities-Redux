package com.sxilverr.variedcommoditiesredux.block;

import com.sxilverr.variedcommoditiesredux.entity.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Locale;
import java.util.function.Supplier;

public class CouchBlock extends FurnitureBlock {
    public static final EnumProperty<Corner> CORNER = EnumProperty.create("corner", Corner.class);
    private static final Direction[] BACK = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    public enum Corner implements StringRepresentable {
        NONE, LEFT, RIGHT;

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    private final boolean wool;

    public CouchBlock(VCVariant variant, boolean wool, Supplier<? extends BlockEntityType<?>> type, Properties properties) {
        super(variant, type, one(box(0.0, 0.0, 0.0, 1.0, 0.5, 1.0)), true, properties);
        this.wool = wool;
        registerDefaultState(defaultBlockState().setValue(LEFT, false).setValue(RIGHT, false).setValue(CORNER, Corner.NONE));
    }

    public boolean isWool() {
        return wool;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LEFT, RIGHT, CORNER);
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

    private BlockState connect(BlockGetter level, BlockPos pos, BlockState state) {
        int rotation = rotation(state);
        Direction back = BACK[rotation];
        Corner corner = Corner.NONE;
        if (wool) {
            if (isCouch(level, pos.relative(back), (rotation + 3) % 4)) corner = Corner.LEFT;
            else if (isCouch(level, pos.relative(back), (rotation + 1) % 4)) corner = Corner.RIGHT;
        }
        return state.setValue(CORNER, corner)
                .setValue(LEFT, connects(level, pos.relative(back.getCounterClockWise()), rotation))
                .setValue(RIGHT, connects(level, pos.relative(back.getClockWise()), rotation));
    }

    private boolean isCouch(BlockGetter level, BlockPos pos, int rotation) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() == this && rotation(state) == rotation;
    }

    private boolean connects(BlockGetter level, BlockPos pos, int rotation) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() != this) return false;
        int other = rotation(state);
        if (other == rotation) return true;
        if (!wool) return false;
        Corner corner = state.getValue(CORNER);
        if (corner == Corner.LEFT) other += 3;
        else if (corner == Corner.RIGHT) other++;
        return other % 4 == rotation;
    }

    public static class Wool extends CouchBlock {
        public Wool(VCVariant variant, Properties properties) {
            super(variant, true, null, properties);
        }

        @Override
        protected boolean dyeable() {
            return true;
        }
    }

    @Override
    protected InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand == InteractionHand.MAIN_HAND && player.getMainHandItem().getItem() instanceof DyeItem) {
            if (!level.isClientSide) dye(level, pos, player, hand);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return SeatEntity.mount(level, pos, player, wool ? 0.5 : 0.31, facingYaw(state) + 180.0F);
    }
}
