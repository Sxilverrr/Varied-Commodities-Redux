package com.sxilverr.variedcommoditiesredux.fabric;

import com.sxilverr.variedcommoditiesredux.VCArmors;
import com.sxilverr.variedcommoditiesredux.client.SkirtModel;
import com.sxilverr.variedcommoditiesredux.client.renderer.BookRenderer;
import com.sxilverr.variedcommoditiesredux.client.screen.SignPreviewRenderer;
import com.sxilverr.variedcommoditiesredux.mixin.SpecialModelRenderersAccessor;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.SpecialGuiElementRegistry;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.Equippable;

public final class VariedCommoditiesFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SpecialModelRenderersAccessor.variedcommoditiesredux$idMapper().put(BookRenderer.ID, BookRenderer.Unbaked.CODEC);
        SpecialGuiElementRegistry.register(context -> new SignPreviewRenderer(context.vertexConsumers()));
        ArmorRenderer.register(context -> {
            EquipmentLayerRenderer equipment = context.getEquipmentRenderer();
            return (pose, collector, stack, state, slot, light, model) -> {
                Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
                if (equippable == null || equippable.assetId().isEmpty()) return;
                equipment.renderLayers(EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS, equippable.assetId().get(), SkirtModel.get(), state, stack, pose, collector, light, state.outlineColor);
            };
        }, VCArmors.SKIRTS.stream().map(RegistrySupplier::get).toArray(Item[]::new));
    }
}
