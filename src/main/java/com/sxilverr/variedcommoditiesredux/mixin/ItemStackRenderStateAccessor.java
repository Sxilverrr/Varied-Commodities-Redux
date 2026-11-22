package com.sxilverr.variedcommoditiesredux.mixin;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemStackRenderState.class)
public interface ItemStackRenderStateAccessor {
    @Accessor("layers")
    ItemStackRenderState.LayerRenderState[] variedcommoditiesredux$layers();

    @Accessor("activeLayerCount")
    int variedcommoditiesredux$activeLayerCount();
}
