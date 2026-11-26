package com.sxilverr.variedcommoditiesredux.mixin;

import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemStackRenderState.LayerRenderState.class)
public interface LayerRenderStateAccessor {
    @Accessor("itemTransform")
    ItemTransform variedcommoditiesredux$transform();

    @Accessor("specialRenderer")
    SpecialModelRenderer<Object> variedcommoditiesredux$specialRenderer();
}
