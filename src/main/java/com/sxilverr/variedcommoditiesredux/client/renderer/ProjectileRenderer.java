package com.sxilverr.variedcommoditiesredux.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.sxilverr.variedcommoditiesredux.client.VCRender;
import com.sxilverr.variedcommoditiesredux.entity.HolyHandGrenade;
import com.sxilverr.variedcommoditiesredux.entity.VCProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RenderShape;

public class ProjectileRenderer<T extends VCProjectile> extends EntityRenderer<T> {

    public ProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(T entity, float yaw, float partialTicks, PoseStack pose, MultiBufferSource buffers, int light) {
        ItemStack item = entity.getItem();
        float entityYaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float entityPitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        pose.pushPose();
        if (entity instanceof HolyHandGrenade) {
            pose.mulPose(Axis.YP.rotationDegrees(entityYaw - 180.0F));
            pose.mulPose(Axis.XP.rotationDegrees(entityPitch));
            pose.translate(0.0, -0.125, 0.0);
            VCRender.item(pose, buffers, light, OverlayTexture.NO_OVERLAY, item, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity.level());
        } else if (entity.is3D()) {
            pose.mulPose(Axis.YP.rotationDegrees(entityYaw - 180.0F));
            pose.mulPose(Axis.XP.rotationDegrees(entityPitch));
            pose.translate(0.0, -0.125, 0.25);
            if (item.getItem() instanceof BlockItem block && block.getBlock().defaultBlockState().getRenderShape() == RenderShape.ENTITYBLOCK_ANIMATED) {
                pose.translate(0.0F, 0.1875F, -0.3125F);
                pose.mulPose(Axis.XP.rotationDegrees(20.0F));
                pose.mulPose(Axis.YP.rotationDegrees(45.0F));
                pose.scale(-0.375F, -0.375F, 0.375F);
            }
            VCRender.item(pose, buffers, light, OverlayTexture.NO_OVERLAY, item, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity.level());
        } else {
            pose.scale(0.5F, 0.5F, 0.5F);
            pose.mulPose(entityRenderDispatcher.cameraOrientation());
            pose.mulPose(Axis.YP.rotationDegrees(180.0F));
            VCRender.item(pose, buffers, light, OverlayTexture.NO_OVERLAY, item, ItemDisplayContext.NONE, entity.level());
        }
        pose.popPose();
        super.render(entity, yaw, partialTicks, pose, buffers, light);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ResourceLocation getTextureLocation(T entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
