package com.sxilverr.variedcommoditiesredux.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.function.Supplier;

public class RotatedBlock extends VCBlock implements EntityBlock {
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 3);
    public static final IntegerProperty ROTATION8 = IntegerProperty.create("rotation", 0, 7);
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    private final VCVariant variant;
    private final Supplier<? extends BlockEntityType<?>> type;

    public RotatedBlock(VCVariant variant, Supplier<? extends BlockEntityType<?>> type, Properties properties) {
        super(properties.noOcclusion());
        this.variant = variant;
        this.type = type;
    }

    public VCVariant variant() {
        return variant;
    }

    protected int maxRotation() {
        return 4;
    }

    public IntegerProperty rotationProperty() {
        return maxRotation() == 8 ? ROTATION8 : ROTATION;
    }

    public float facingYaw(BlockState state) {
        return 360.0F / maxRotation() * rotation(state);
    }

    public static int rotation(BlockState state) {
        if (state.hasProperty(ROTATION8)) return state.getValue(ROTATION8);
        if (state.hasProperty(ROTATION)) return state.getValue(ROTATION);
        return 0;
    }

    public int defaultColor() {
        return 15 - (variant == null ? 0 : variant.index());
    }

    protected boolean dyeable() {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(rotationProperty());
        if (dyeable()) builder.add(COLOR);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        int max = maxRotation();
        int rotation = Mth.floor(context.getRotation() * max / 360.0F + 0.5D) & (max - 1);
        BlockState state = defaultBlockState().setValue(rotationProperty(), rotation);
        return dyeable() ? state.setValue(COLOR, DyeColor.byId(defaultColor())) : state;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return type == null ? null : type.get().create(pos, state);
    }

    protected static boolean dye(Level level, BlockPos pos, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof DyeItem dye)) return false;
        BlockState state = level.getBlockState(pos);
        if (!state.hasProperty(COLOR) || state.getValue(COLOR) == dye.getDyeColor()) return true;
        level.setBlock(pos, state.setValue(COLOR, dye.getDyeColor()), 3);
        if (state.hasProperty(TallBlock.TOP)) {
            BlockPos other = state.getValue(TallBlock.TOP) ? pos.below() : pos.above();
            BlockState otherState = level.getBlockState(other);
            if (otherState.is(state.getBlock())) level.setBlock(other, otherState.setValue(COLOR, dye.getDyeColor()), 3);
        }
        if (!player.getAbilities().instabuild) stack.shrink(1);
        return true;
    }
}
