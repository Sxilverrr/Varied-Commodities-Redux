package com.sxilverr.variedcommoditiesredux.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.sxilverr.variedcommoditiesredux.item.VCArmorItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SkirtModel extends HumanoidModel<LivingEntity> {
    private static SkirtModel instance;

    public SkirtModel() {
        super(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
    }

    public static SkirtModel get() {
        if (instance == null) instance = new SkirtModel();
        return instance;
    }

    private void draw(PoseStack pose, VertexConsumer consumer, int light, int overlay, float r, float g, float b) {
        float rotX = 0.3F + leftArm.xRot * 0.01F;
        float rotZ = -0.23F + leftArm.xRot * 0.03F;
        pose.pushPose();
        pose.translate((leftLeg.x + rightLeg.x) / 32.0F, (leftLeg.y + rightLeg.y) / 32.0F, (leftLeg.z + rightLeg.z) / 32.0F);
        pose.mulPose(Axis.XP.rotation(body.xRot * 0.42F));
        pose.translate(0.0F, -0.75F, 0.0F);
        pose.scale(2.1F, 1.04F, 2.1F);
        for (int i = 0; i < 10; i++) {
            pose.mulPose(Axis.YP.rotationDegrees(36.0F));
            pose.pushPose();
            pose.translate(2.4F / 16.0F, 0.0, 0.0);
            pose.mulPose(Axis.ZP.rotation(rotZ));
            pose.mulPose(Axis.YP.rotation(-0.2F));
            pose.mulPose(Axis.XP.rotation(rotX));
            plane(pose, consumer, light, overlay, r, g, b, -2.6F, -2.6F, 4);
            pose.pushPose();
            pose.mulPose(Axis.YP.rotation((float) (-Math.PI / 2)));
            plane(pose, consumer, light, overlay, r, g, b, -0.6F, 2.6F, 6);
            pose.popPose();
            pose.popPose();
        }
        pose.popPose();
    }

    private static void plane(PoseStack pose, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float x, float z, int u) {
        PoseStack.Pose last = pose.last();
        float px = x / 16.0F;
        float y1 = 9.0F / 16.0F;
        float y2 = 18.0F / 16.0F;
        float z1 = z / 16.0F;
        float z2 = (z + 2.0F) / 16.0F;
        float u1 = u / 64.0F;
        float u2 = (u + 2) / 64.0F;
        float v1 = 20.0F / 32.0F;
        float v2 = 29.0F / 32.0F;
        vertex(consumer, last, px, y1, z2, u2, v1, light, overlay, r, g, b);
        vertex(consumer, last, px, y1, z1, u1, v1, light, overlay, r, g, b);
        vertex(consumer, last, px, y2, z1, u1, v2, light, overlay, r, g, b);
        vertex(consumer, last, px, y2, z2, u2, v2, light, overlay, r, g, b);
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, float u, float v, int light, int overlay, float r, float g, float b) {
        //? if >=1.21 {
        /*consumer.addVertex(pose, x, y, z).setColor(r, g, b, 1.0F).setUv(u, v).setOverlay(overlay).setLight(light).setNormal(pose, 1.0F, 0.0F, 0.0F);
        *///?} else {
        consumer.vertex(pose.pose(), x, y, z).color(r, g, b, 1.0F).uv(u, v).overlayCoords(overlay).uv2(light).normal(pose.normal(), 1.0F, 0.0F, 0.0F).endVertex();
        //?}
    }

    //? if >=1.21 {
    /*@Override
    public void renderToBuffer(PoseStack pose, VertexConsumer consumer, int light, int overlay, int color) {
        draw(pose, consumer, light, overlay, (color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F);
    }
    *///?} else {
    @Override
    public void renderToBuffer(PoseStack pose, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float a) {
        draw(pose, consumer, light, overlay, r, g, b);
    }
    //?}

    public static void render(PoseStack pose, MultiBufferSource buffers, int light, ItemStack stack, HumanoidModel<?> parent, int color) {
        if (!(stack.getItem() instanceof VCArmorItem armor)) return;
        SkirtModel model = get();
        @SuppressWarnings("unchecked")
        HumanoidModel<LivingEntity> source = (HumanoidModel<LivingEntity>) parent;
        source.copyPropertiesTo(model);
        //? if >=1.21 {
        /*VertexConsumer consumer = ItemRenderer.getArmorFoilBuffer(buffers, RenderType.armorCutoutNoCull(armor.layerTexture()), stack.hasFoil());
        *///?} else {
        VertexConsumer consumer = ItemRenderer.getArmorFoilBuffer(buffers, RenderType.armorCutoutNoCull(armor.layerTexture()), false, stack.hasFoil());
        //?}
        model.draw(pose, consumer, light, OverlayTexture.NO_OVERLAY, (color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F);
    }
}
