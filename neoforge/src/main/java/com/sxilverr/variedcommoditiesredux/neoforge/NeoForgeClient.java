package com.sxilverr.variedcommoditiesredux.neoforge;

import com.sxilverr.variedcommoditiesredux.VCArmors;
import com.sxilverr.variedcommoditiesredux.VCBlocks;
import com.sxilverr.variedcommoditiesredux.client.FurnitureItemRenderer;
import com.sxilverr.variedcommoditiesredux.client.SkirtModel;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

public final class NeoForgeClient {
    static void init(IEventBus modBus, ModContainer container) {
        modBus.addListener(NeoForgeClient::registerExtensions);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private static void registerExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) renderer = new FurnitureItemRenderer();
                return renderer;
            }
        }, VCBlocks.BOOK.get().asItem());
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
                return SkirtModel.get();
            }
        }, VCArmors.SKIRTS.stream().map(RegistrySupplier::get).toArray(Item[]::new));
    }

    private NeoForgeClient() {
    }
}
