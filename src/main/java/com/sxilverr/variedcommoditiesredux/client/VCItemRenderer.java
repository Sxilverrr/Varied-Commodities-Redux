package com.sxilverr.variedcommoditiesredux.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.Map;

public final class VCItemRenderer {
    private static final Map<Block, BlockEntity> CACHE = new HashMap<>();

    public static void render(ItemStack stack, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        if (!(stack.getItem() instanceof BlockItem item) || !(item.getBlock() instanceof EntityBlock entityBlock)) return;
        BlockEntity be = CACHE.computeIfAbsent(item.getBlock(), block -> entityBlock.newBlockEntity(BlockPos.ZERO, block.defaultBlockState()));
        if (be != null) {
            Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(be, pose, buffers, light, overlay);
        }
    }

    private VCItemRenderer() {
    }
}
