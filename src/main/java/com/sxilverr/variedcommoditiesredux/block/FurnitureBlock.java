package com.sxilverr.variedcommoditiesredux.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class FurnitureBlock extends RotatedBlock {
    public static final BooleanProperty LEFT = BooleanProperty.create("left");
    public static final BooleanProperty RIGHT = BooleanProperty.create("right");
    private final VoxelShape[] shapes;
    private final boolean solid;

    public FurnitureBlock(VCVariant variant, Supplier<? extends BlockEntityType<?>> type, VoxelShape[] shapes, boolean solid, Properties properties) {
        super(variant, type, properties);
        this.shapes = shapes;
        this.solid = solid;
    }

    public static VoxelShape box(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(x1 * 16, y1 * 16, z1 * 16, x2 * 16, y2 * 16, z2 * 16);
    }

    public static VoxelShape[] one(VoxelShape shape) {
        return new VoxelShape[]{shape};
    }

    public static VoxelShape[] full() {
        return one(Shapes.block());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapes[rotation(state) % shapes.length];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return solid ? getShape(state, level, pos, context) : Shapes.empty();
    }
}
