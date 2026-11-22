package com.sxilverr.variedcommoditiesredux.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.sxilverr.variedcommoditiesredux.entity.HolyHandGrenade;
import com.sxilverr.variedcommoditiesredux.entity.VCProjectile;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;

public class ProjectileRenderer<T extends VCProjectile> extends EntityRenderer<T, ProjectileRenderer.State> {
    private final ItemModelResolver items;

    public static class State extends EntityRenderState {
        public final ItemStackRenderState item = new ItemStackRenderState();
        public float yaw;
        public float pitch;
        public int mode;
    }

    public ProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.items = context.getItemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(T entity, State state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.yaw = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        state.pitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        state.mode = entity instanceof HolyHandGrenade ? 0 : entity.is3D() ? 1 : 2;
        items.updateForNonLiving(state.item, entity.getItem(), state.mode == 2 ? ItemDisplayContext.NONE : ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
    }

    @Override
    public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        pose.pushPose();
        if (state.mode == 2) {
            pose.scale(0.5F, 0.5F, 0.5F);
            pose.mulPose(camera.orientation);
            pose.mulPose(Axis.YP.rotationDegrees(180.0F));
        } else {
            pose.mulPose(Axis.YP.rotationDegrees(state.yaw - 180.0F));
            pose.mulPose(Axis.XP.rotationDegrees(state.pitch));
            pose.translate(0.0, -0.125, state.mode == 0 ? 0.0 : 0.25);
        }
        state.item.submit(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        pose.popPose();
        super.submit(state, pose, collector, camera);
    }
}
