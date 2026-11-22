package com.sxilverr.variedcommoditiesredux;

import com.sxilverr.variedcommoditiesredux.item.VCArmorMaterials;
import com.sxilverr.variedcommoditiesredux.item.VCArmorMaterials.Base;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.ArrayList;
import java.util.List;

public final class VCArmors {
    private static final String VC = VariedCommodities.MODID;
    public static final List<RegistrySupplier<Item>> SKIRTS = new ArrayList<>();
    public static final TagKey<Item> PIGLIN_SAFE = TagKey.create(Registries.ITEM, VariedCommodities.id("piglin_safe_armor"));

    public static final RegistrySupplier<Item> COW_LEATHER_HELMET = armor("cow_leather_helmet", VC, "cow_leather", Base.LEATHER, ArmorType.HELMET);

    static {
        set("cow_leather", "cow_leather", Base.LEATHER, "chestplate", "leggings", "boots");
        set("nanorum", "nanorum", Base.IRON, "helmet", "chestplate", "leggings", "boots");
        set("tactical", "tactical", Base.IRON, "helmet", "chestplate");
        set("full_leather", "full_cloth", Base.LEATHER, "helmet", "chestplate");
        set("full_iron", "full_iron", Base.IRON, "helmet", "chestplate");
        set("full_golden", "full_golden", Base.GOLD, "helmet", "chestplate");
        set("full_diamond", "full_diamond", Base.DIAMOND, "helmet", "chestplate");
        set("full_bronze", "full_bronze", Base.BRONZE, "helmet", "chestplate", "leggings", "boots");
        set("full_emerald", "full_emerald", Base.EMERALD, "helmet", "chestplate", "leggings", "boots");
        set("full_wooden", "full_wooden", Base.LEATHER, "helmet", "chestplate", "leggings", "boots");
        set("tuxedo", "tuxedo", Base.CHAIN, "chestplate", "leggings", "trenchcoat");
        set("wizard", "wizard", Base.CHAIN, "helmet", "chestplate", "leggings");
        set("assassin", "assassin", Base.IRON, "helmet", "chestplate", "leggings", "boots");
        set("soldier", "soldier", Base.IRON, "helmet", "chestplate", "leggings", "trenchcoat");
        set("x407", "x407", Base.DIAMOND, "helmet", "chestplate", "leggings", "boots");
        set("mithril", "mithril", Base.MITHRIL, "helmet", "chestplate", "leggings", "boots");
        set("demonic", "demonic", Base.DIAMOND, "helmet", "chestplate", "leggings", "boots");
        set("commissar", "commissar", Base.GOLD, "helmet", "chestplate", "leggings", "trenchcoat");
        armor("infantry_helmet", VC, "infantry", Base.IRON, ArmorType.HELMET);
        armor("officer_chestplate", VC, "officer", Base.DIAMOND, ArmorType.CHESTPLATE);
        armor("silver_crown", VC, "crown2", Base.DIAMOND, ArmorType.HELMET);
        armor("golden_crown", VC, "crown1", Base.DIAMOND, ArmorType.HELMET);
        set("ninja", "ninja", Base.IRON, "helmet", "chestplate", "leggings");
        armor("bandit_mask", VC, "bandit_mask", Base.IRON, ArmorType.HELMET);
        armor("paper_crown", VC, "paper_crown", Base.LEATHER, ArmorType.HELMET);
        skirt("chainmail_skirt", "minecraft", "chainmail", Base.CHAIN);
        skirt("leather_skirt", "minecraft", "leather", Base.LEATHER);
        skirt("golden_skirt", "minecraft", "gold", Base.GOLD);
        skirt("iron_skirt", "minecraft", "iron", Base.IRON);
        skirt("diamond_skirt", "minecraft", "diamond", Base.DIAMOND);
        skirt("emerald_skirt", VC, "full_emerald", Base.EMERALD);
        skirt("bronze_skirt", VC, "full_bronze", Base.BRONZE);
        skirt("demonic_skirt", VC, "demonic", Base.DIAMOND);
        skirt("mithril_skirt", VC, "mithril", Base.MITHRIL);
    }

    private static void set(String prefix, String texture, Base base, String... pieces) {
        for (String piece : pieces) {
            ArmorType type = switch (piece) {
                case "helmet" -> ArmorType.HELMET;
                case "chestplate" -> ArmorType.CHESTPLATE;
                case "leggings" -> ArmorType.LEGGINGS;
                default -> ArmorType.BOOTS;
            };
            armor(prefix + "_" + piece, VC, texture, base, type);
        }
    }

    private static RegistrySupplier<Item> armor(String name, String namespace, String texture, Base base, ArmorType type) {
        return VCItems.register(name, VCTabs.ARMORS, p -> new Item(p.humanoidArmor(VCArmorMaterials.create(namespace, texture, base), type)));
    }

    private static void skirt(String name, String namespace, String texture, Base base) {
        SKIRTS.add(armor(name, namespace, texture, base, ArmorType.LEGGINGS));
    }

    public static void init() {
    }

    private VCArmors() {
    }
}
