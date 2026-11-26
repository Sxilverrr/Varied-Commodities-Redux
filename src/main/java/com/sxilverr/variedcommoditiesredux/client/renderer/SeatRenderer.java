package com.sxilverr.variedcommoditiesredux.client.renderer;

import com.sxilverr.variedcommoditiesredux.entity.SeatEntity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class SeatRenderer extends EntityRenderer<SeatEntity, EntityRenderState> {
    public SeatRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    //? if >=26.3 {
    /*public boolean shouldRender(SeatEntity entity, Frustum frustum, double x, double y, double z, float partialTicks) {
    *///?} else {
    public boolean shouldRender(SeatEntity entity, Frustum frustum, double x, double y, double z) {
    //?}
        return false;
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
