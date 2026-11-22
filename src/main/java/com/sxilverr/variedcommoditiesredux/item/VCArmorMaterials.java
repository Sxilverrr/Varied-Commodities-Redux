package com.sxilverr.variedcommoditiesredux.item;

import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public final class VCArmorMaterials {
    public enum Base {
        LEATHER(5), CHAIN(15), IRON(15), GOLD(7), DIAMOND(33), MITHRIL(40), BRONZE(7), EMERALD(35);

        public final int durability;

        Base(int durability) {
            this.durability = durability;
        }
    }

    private static final TagKey<Item> UNREPAIRABLE = VCMaterial.tag(VariedCommodities.MODID, "unrepairable");

    public static ArmorMaterial create(String namespace, String texture, Base base) {
        ArmorMaterial vanilla = switch (base) {
            case LEATHER -> ArmorMaterials.LEATHER;
            case CHAIN -> ArmorMaterials.CHAINMAIL;
            case IRON, BRONZE -> ArmorMaterials.IRON;
            case GOLD -> ArmorMaterials.GOLD;
            default -> ArmorMaterials.DIAMOND;
        };
        int enchantment = vanilla.enchantmentValue();
        Holder<SoundEvent> sound = vanilla.equipSound();
        TagKey<Item> repair = vanilla.repairIngredient();
        float toughness = vanilla.toughness();
        if (base == Base.MITHRIL) {
            enchantment = 20;
            sound = SoundEvents.ARMOR_EQUIP_IRON;
            repair = VCMaterial.tag("c", "ingots/mithril");
        } else if (base == Base.BRONZE) {
            enchantment = 20;
            repair = UNREPAIRABLE;
            toughness = 0.0F;
        } else if (base == Base.EMERALD) {
            enchantment = 5;
            repair = UNREPAIRABLE;
        }
        ResourceKey<EquipmentAsset> asset = ResourceKey.create(EquipmentAssets.ROOT_ID, VariedCommodities.rl(namespace, texture));
        return new ArmorMaterial(base.durability, vanilla.defense(), enchantment, sound, toughness, 0.0F, repair, asset);
    }

    private VCArmorMaterials() {
    }
}
