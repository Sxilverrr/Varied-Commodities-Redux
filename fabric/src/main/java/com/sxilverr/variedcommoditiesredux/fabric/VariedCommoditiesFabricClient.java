package com.sxilverr.variedcommoditiesredux.fabric;

import com.sxilverr.variedcommoditiesredux.Compat;
import com.sxilverr.variedcommoditiesredux.VCArmors;
import com.sxilverr.variedcommoditiesredux.VCBlocks;
import com.sxilverr.variedcommoditiesredux.client.SkirtModel;
import com.sxilverr.variedcommoditiesredux.client.VCItemRenderer;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.world.item.Item;

public final class VariedCommoditiesFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BuiltinItemRendererRegistry.INSTANCE.register(VCBlocks.BOOK.get(), (stack, context, pose, buffers, light, overlay) ->
                VCItemRenderer.render(stack, pose, buffers, light, overlay));
        ArmorRenderer.register((pose, buffers, stack, entity, slot, light, model) ->
                        SkirtModel.render(pose, buffers, light, stack, model, VCArmors.DYEABLE.stream().anyMatch(d -> d.get() == stack.getItem()) ? Compat.armorColor(stack) : 0xFFFFFF),
                VCArmors.SKIRTS.stream().map(RegistrySupplier::get).toArray(Item[]::new));
    }
}
