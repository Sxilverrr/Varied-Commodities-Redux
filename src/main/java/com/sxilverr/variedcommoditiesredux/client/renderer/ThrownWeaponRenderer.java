package com.sxilverr.variedcommoditiesredux.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.sxilverr.variedcommoditiesredux.client.VCRender;
import com.sxilverr.variedcommoditiesredux.entity.ThrownWeapon;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;

public class ThrownWeaponRenderer extends EntityRenderer<ThrownWeapon> {
    public ThrownWeaponRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ThrownWeapon entity, float yaw, float partialTicks, PoseStack pose, MultiBufferSource buffers, int light) {
        pose.pushPose();
        pose.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 180.0F));
        pose.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
        if (!entity.isStuck()) pose.mulPose(Axis.XP.rotationDegrees(-(entity.tickCount + partialTicks) * 24.0F));
        pose.translate(0.0, -0.125, 0.25);
        VCRender.item(pose, buffers, light, OverlayTexture.NO_OVERLAY, entity.getItem(), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity.level());
        pose.popPose();
        super.render(entity, yaw, partialTicks, pose, buffers, light);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ResourceLocation getTextureLocation(ThrownWeapon entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
