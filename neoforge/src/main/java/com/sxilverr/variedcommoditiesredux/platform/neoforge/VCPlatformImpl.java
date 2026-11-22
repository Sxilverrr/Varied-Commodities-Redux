package com.sxilverr.variedcommoditiesredux.platform.neoforge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public final class VCPlatformImpl {
    public static <T extends BlockEntity> BlockEntityType<T> blockEntityType(BiFunction<BlockPos, BlockState, T> factory, Block... blocks) {
        return new BlockEntityType<>(factory::apply, blocks);
    }

    private VCPlatformImpl() {
    }
}
