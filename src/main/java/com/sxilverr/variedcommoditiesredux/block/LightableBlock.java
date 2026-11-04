package com.sxilverr.variedcommoditiesredux.block;

import com.sxilverr.variedcommoditiesredux.Compat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class LightableBlock extends RotatedBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<Attach> ATTACH = EnumProperty.create("attach", Attach.class);

    public enum Attach implements StringRepresentable {
        FLOOR, CEILING, WALL;

        @Override
        public String getSerializedName() {
            return name().toLowerCase(java.util.Locale.ROOT);
        }
    }

    public enum Kind {
        CANDLE, LAMP, CAMPFIRE
    }

    private static final VoxelShape CANDLE_FLOOR = FurnitureBlock.box(0.3, 0.0, 0.3, 0.7, 0.5, 0.7);
    private static final VoxelShape CANDLE_CEILING = FurnitureBlock.box(0.1, 0.1, 0.1, 0.9, 0.8, 0.9);
    private static final VoxelShape[] CANDLE_WALL = {
            FurnitureBlock.box(0.2, 0.4, 0.4, 0.8, 0.9, 1.0),
            FurnitureBlock.box(0.0, 0.4, 0.2, 0.6, 0.9, 0.8),
            FurnitureBlock.box(0.2, 0.4, 0.0, 0.8, 0.9, 0.6),
            FurnitureBlock.box(0.4, 0.4, 0.2, 1.0, 0.9, 0.8)
    };
    private static final VoxelShape LAMP_FLOOR = FurnitureBlock.box(0.3, 0.0, 0.3, 0.7, 0.6, 0.7);
    private static final VoxelShape[] LAMP_WALL = {
            FurnitureBlock.box(0.3, 0.2, 0.5, 0.7, 0.7, 0.9),
            FurnitureBlock.box(0.1, 0.2, 0.3, 0.5, 0.7, 0.7),
            FurnitureBlock.box(0.3, 0.2, 0.1, 0.7, 0.7, 0.5),
            FurnitureBlock.box(0.5, 0.2, 0.3, 0.9, 0.7, 0.7)
    };
    private static final VoxelShape CAMPFIRE = FurnitureBlock.box(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);

    private final Kind kind;

    public LightableBlock(Kind kind, Supplier<? extends BlockEntityType<?>> type, Properties properties) {
        super(null, type, properties);
        this.kind = kind;
        registerDefaultState(defaultBlockState().setValue(LIT, true).setValue(ATTACH, Attach.FLOOR));
    }

    public Kind kind() {
        return kind;
    }

    public static int lightLevel(BlockState state, int lit) {
        return state.getValue(LIT) ? lit : 0;
    }

    @Override
    protected int maxRotation() {
        return 8;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT, ATTACH);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (kind == Kind.CAMPFIRE) return state;
        Direction face = context.getClickedFace();
        if (face == Direction.UP) return state.setValue(ATTACH, Attach.FLOOR);
        if (face == Direction.DOWN) return state.setValue(ATTACH, Attach.CEILING);
        int rotation = switch (face) {
            case EAST -> 2;
            case SOUTH -> 4;
            case WEST -> 6;
            default -> 0;
        };
        return state.setValue(ATTACH, Attach.WALL).setValue(ROTATION8, rotation);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (kind == Kind.CAMPFIRE) return CAMPFIRE;
        Attach attach = state.getValue(ATTACH);
        if (attach == Attach.WALL) {
            int index = switch (state.getValue(ROTATION8)) {
                case 0 -> 0;
                case 2 -> 1;
                case 4 -> 2;
                case 6 -> 3;
                default -> -1;
            };
            if (index >= 0) return kind == Kind.CANDLE ? CANDLE_WALL[index] : LAMP_WALL[index];
            return kind == Kind.CANDLE ? FurnitureBlock.box(0.2, 0.4, 0.2, 0.8, 0.9, 0.8) : FurnitureBlock.box(0.3, 0.2, 0.3, 0.7, 0.7, 0.7);
        }
        if (attach == Attach.CEILING && kind == Kind.CANDLE) return CANDLE_CEILING;
        return kind == Kind.CANDLE ? CANDLE_FLOOR : LAMP_FLOOR;
    }

    @Override
    protected InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (kind != Kind.CAMPFIRE) {
            level.setBlock(pos, state.cycle(LIT), 3);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        ItemStack held = player.getMainHandItem();
        boolean lit = state.getValue(LIT);
        boolean creative = player.getAbilities().instabuild;
        if (!lit && (held.is(Items.FLINT_AND_STEEL) || held.is(Items.FIRE_CHARGE))) {
            if (!level.isClientSide) {
                boolean charge = held.is(Items.FIRE_CHARGE);
                level.setBlock(pos, state.setValue(LIT, true), 11);
                level.playSound(null, pos, charge ? SoundEvents.FIRECHARGE_USE : SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.4F + 0.8F);
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                if (!creative) {
                    if (charge) held.shrink(1);
                    else Compat.damage(held, 1, player, hand);
                }
            }
        } else if (lit && (held.getItem() instanceof ShovelItem || held.is(Items.SAND))) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(LIT, false), 11);
                level.levelEvent(null, 1009, pos, 0);
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                if (!creative && held.getItem() instanceof ShovelItem) Compat.damage(held, 1, player, hand);
            }
        } else {
            return InteractionResult.PASS;
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) return;
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        if (kind == Kind.CAMPFIRE) {
            if (random.nextInt(36) == 0) {
                level.playLocalSound(x + 0.5, y + 0.5, z + 0.5, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
            }
            level.addParticle(ParticleTypes.LARGE_SMOKE, x + 0.5, y + 0.7, z + 0.5, 0.0, 0.0, 0.0);
            level.addParticle(ParticleTypes.FLAME, x + 0.5, y + 0.7, z + 0.5, 0.0, 0.0, 0.0);
            return;
        }
        if (kind != Kind.CANDLE) return;
        Attach attach = state.getValue(ATTACH);
        int rotation = state.getValue(ROTATION8);
        if (attach == Attach.CEILING) {
            double[][] points = rotation % 2 == 0
                    ? new double[][]{{0.5, 0.13}, {0.5, 0.87}, {0.13, 0.5}, {0.87, 0.5}}
                    : new double[][]{{0.24, 0.24}, {0.76, 0.76}, {0.24, 0.76}, {0.76, 0.24}};
            for (double[] p : points) flame(level, x + p[0], y + 0.65, z + p[1]);
            return;
        }
        double ox = 0.5;
        double oy = 0.45;
        double oz = 0.5;
        if (attach == Attach.WALL) {
            oy = 1.05;
            if (rotation == 0) oz += 0.12;
            if (rotation == 4) oz -= 0.12;
            if (rotation == 6) ox += 0.12;
            if (rotation == 2) ox -= 0.12;
        }
        flame(level, x + ox, y + oy, z + oz);
    }

    private static void flame(Level level, double x, double y, double z) {
        level.addParticle(ParticleTypes.SMOKE, x, y + 0.01, z, 0.0, 0.0, 0.0);
        level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0, 0.0, 0.0);
    }
}
