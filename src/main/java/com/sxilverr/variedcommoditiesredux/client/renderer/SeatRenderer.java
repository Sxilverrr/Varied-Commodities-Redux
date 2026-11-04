package com.sxilverr.variedcommoditiesredux.client.renderer;

import com.sxilverr.variedcommoditiesredux.entity.SeatEntity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

public class SeatRenderer extends EntityRenderer<SeatEntity> {
    public SeatRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(SeatEntity entity, Frustum frustum, double x, double y, double z) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public ResourceLocation getTextureLocation(SeatEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
