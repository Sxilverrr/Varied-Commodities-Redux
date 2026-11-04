package com.sxilverr.variedcommoditiesredux.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class VCRender {
    public static final float[][] COLORS = {
            {1.0F, 1.0F, 1.0F}, {0.95F, 0.7F, 0.2F}, {0.9F, 0.5F, 0.85F}, {0.6F, 0.7F, 0.95F},
            {0.9F, 0.9F, 0.2F}, {0.5F, 0.8F, 0.1F}, {0.95F, 0.7F, 0.8F}, {0.3F, 0.3F, 0.3F},
            {0.6F, 0.6F, 0.6F}, {0.3F, 0.6F, 0.7F}, {0.7F, 0.4F, 0.9F}, {0.2F, 0.4F, 0.8F},
            {0.5F, 0.4F, 0.3F}, {0.4F, 0.5F, 0.2F}, {0.8F, 0.3F, 0.3F}, {0.1F, 0.1F, 0.1F}
    };

    public static VertexConsumer culled(MultiBufferSource buffers, ResourceLocation texture) {
        return buffers.getBuffer(RenderType.entityCutout(texture));
    }

    public static void part(ModelPart part, PoseStack pose, VertexConsumer consumer, int light, int overlay, float r, float g, float b) {
        //? if >=1.21 {
        /*part.render(pose, consumer, light, overlay, 0xFF000000 | ((int) (r * 255.0F) << 16) | ((int) (g * 255.0F) << 8) | (int) (b * 255.0F));
        *///?} else {
        part.render(pose, consumer, light, overlay, r, g, b, 1.0F);
        //?}
    }

    public static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, float u, float v, int light, float nx, float ny, float nz) {
        //? if >=1.21 {
        /*consumer.addVertex(pose, x, y, z).setColor(255, 255, 255, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, nx, ny, nz);
        *///?} else {
        consumer.vertex(pose.pose(), x, y, z).color(255, 255, 255, 255).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(pose.normal(), nx, ny, nz).endVertex();
        //?}
    }

    public static float[] color(int index) {
        return COLORS[Math.floorMod(index, 16)];
    }

    public static void rotate(PoseStack pose, float degrees) {
        pose.mulPose(Axis.ZP.rotationDegrees(180.0F));
        pose.mulPose(Axis.YP.rotationDegrees(degrees));
    }

    public static void icon(PoseStack pose, MultiBufferSource buffers, int light, ItemStack stack, Level level, float scaleX, float scaleY, float scaleZ) {
        pose.pushPose();
        pose.scale(scaleX, scaleY, scaleZ);
        pose.scale(16.0F, -16.0F, 16.0F);
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GUI, light, OverlayTexture.NO_OVERLAY, pose, buffers, level, 0);
        pose.popPose();
    }

    public static ItemTransform thirdPerson(ItemStack stack, Level level) {
        return Minecraft.getInstance().getItemRenderer().getModel(stack, level, null, 0).getTransforms().thirdPersonRightHand;
    }

    public static void item(PoseStack pose, MultiBufferSource buffers, int light, int overlay, ItemStack stack, ItemDisplayContext context, Level level) {
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, context, light, overlay, pose, buffers, level, 0);
    }

    private VCRender() {
    }
}
