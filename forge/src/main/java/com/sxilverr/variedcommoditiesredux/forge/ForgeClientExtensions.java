package com.sxilverr.variedcommoditiesredux.forge;

import com.sxilverr.variedcommoditiesredux.client.FurnitureItemRenderer;
import com.sxilverr.variedcommoditiesredux.client.SkirtModel;
import com.sxilverr.variedcommoditiesredux.item.VCArmorItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public final class ForgeClientExtensions {
    public static IClientItemExtensions create(Item item) {
        if (item instanceof VCArmorItem) {
            return new IClientItemExtensions() {
                @Override
                public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
                    return stack.getItem() instanceof VCArmorItem armor && armor.isSkirt() ? SkirtModel.get() : original;
                }
            };
        }
        return new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) renderer = new FurnitureItemRenderer();
                return renderer;
            }
        };
    }

    private ForgeClientExtensions() {
    }
}
