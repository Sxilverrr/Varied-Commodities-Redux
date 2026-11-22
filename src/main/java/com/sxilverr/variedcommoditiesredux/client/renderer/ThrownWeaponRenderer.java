package com.sxilverr.variedcommoditiesredux.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.sxilverr.variedcommoditiesredux.entity.ThrownWeapon;
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

public class ThrownWeaponRenderer extends EntityRenderer<ThrownWeapon, ThrownWeaponRenderer.State> {
    private final ItemModelResolver items;

    public static class State extends EntityRenderState {
        public final ItemStackRenderState item = new ItemStackRenderState();
        public float yaw;
        public float pitch;
        public boolean stuck;
    }

    public ThrownWeaponRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.items = context.getItemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(ThrownWeapon entity, State state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.yaw = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        state.pitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        state.stuck = entity.isStuck();
        items.updateForNonLiving(state.item, entity.getItem(), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
    }

    @Override
    public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        pose.pushPose();
        pose.mulPose(Axis.YP.rotationDegrees(state.yaw - 180.0F));
        pose.mulPose(Axis.XP.rotationDegrees(state.pitch));
        if (!state.stuck) pose.mulPose(Axis.XP.rotationDegrees(-state.ageInTicks * 24.0F));
        pose.translate(0.0, -0.125, 0.25);
        state.item.submit(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        pose.popPose();
        super.submit(state, pose, collector, camera);
    }
}
