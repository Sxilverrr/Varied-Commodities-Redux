package com.sxilverr.variedcommoditiesredux.neoforge;

import com.sxilverr.variedcommoditiesredux.VCArmors;
import com.sxilverr.variedcommoditiesredux.client.SkirtModel;
import com.sxilverr.variedcommoditiesredux.client.renderer.BookRenderer;
import com.sxilverr.variedcommoditiesredux.client.screen.SignPreviewRenderer;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.client.model.Model;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.event.RegisterPictureInPictureRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public final class NeoForgeClient {
    static void init(IEventBus modBus, ModContainer container) {
        modBus.addListener(NeoForgeClient::registerExtensions);
        modBus.addListener((RegisterSpecialModelRendererEvent event) -> event.register(BookRenderer.ID, BookRenderer.Unbaked.CODEC));
        modBus.addListener((RegisterPictureInPictureRenderersEvent event) -> event.register(SignPreviewRenderer.State.class, SignPreviewRenderer::new));
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private static void registerExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public Model getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType type, Model original) {
                return SkirtModel.get();
            }
        }, VCArmors.SKIRTS.stream().map(RegistrySupplier::get).toArray(Item[]::new));
    }

    private NeoForgeClient() {
    }
}
