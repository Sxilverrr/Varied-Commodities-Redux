package com.sxilverr.variedcommoditiesredux.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.sxilverr.variedcommoditiesredux.mixin.ItemStackRenderStateAccessor;
import com.sxilverr.variedcommoditiesredux.mixin.LayerRenderStateAccessor;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public final class VCRender {
    public static final float[][] COLORS = {
            {1.0F, 1.0F, 1.0F}, {0.95F, 0.7F, 0.2F}, {0.9F, 0.5F, 0.85F}, {0.6F, 0.7F, 0.95F},
            {0.9F, 0.9F, 0.2F}, {0.5F, 0.8F, 0.1F}, {0.95F, 0.7F, 0.8F}, {0.3F, 0.3F, 0.3F},
            {0.6F, 0.6F, 0.6F}, {0.3F, 0.6F, 0.7F}, {0.7F, 0.4F, 0.9F}, {0.2F, 0.4F, 0.8F},
            {0.5F, 0.4F, 0.3F}, {0.4F, 0.5F, 0.2F}, {0.8F, 0.3F, 0.3F}, {0.1F, 0.1F, 0.1F}
    };

    public static float[] color(int index) {
        return COLORS[Math.floorMod(index, 16)];
    }

    public static void rotate(PoseStack pose, float degrees) {
        pose.mulPose(Axis.ZP.rotationDegrees(180.0F));
        pose.mulPose(Axis.YP.rotationDegrees(degrees));
    }

    public static ItemStackRenderState.LayerRenderState firstLayer(ItemStackRenderState state) {
        ItemStackRenderStateAccessor accessor = (ItemStackRenderStateAccessor) state;
        return accessor.variedcommoditiesredux$activeLayerCount() > 0 ? accessor.variedcommoditiesredux$layers()[0] : null;
    }

    public static ItemTransform transform(ItemStackRenderState state) {
        ItemStackRenderState.LayerRenderState layer = firstLayer(state);
        return layer == null ? ItemTransform.NO_TRANSFORM : ((LayerRenderStateAccessor) layer).variedcommoditiesredux$transform();
    }

    public static boolean special(ItemStackRenderState state) {
        ItemStackRenderState.LayerRenderState layer = firstLayer(state);
        return layer != null && ((LayerRenderStateAccessor) layer).variedcommoditiesredux$specialRenderer() != null;
    }

    private VCRender() {
    }
}
