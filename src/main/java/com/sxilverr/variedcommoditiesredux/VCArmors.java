package com.sxilverr.variedcommoditiesredux;

import com.sxilverr.variedcommoditiesredux.item.VCArmorItem;
import com.sxilverr.variedcommoditiesredux.item.VCArmorMaterials;
import com.sxilverr.variedcommoditiesredux.item.VCArmorMaterials.Base;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

import static com.sxilverr.variedcommoditiesredux.VCItems.ITEMS;

public final class VCArmors {
    private static final String VC = VariedCommodities.MODID;
    public static final List<RegistrySupplier<Item>> SKIRTS = new ArrayList<>();
    public static final List<RegistrySupplier<Item>> DYEABLE = new ArrayList<>();
    public static final TagKey<Item> PIGLIN_SAFE = TagKey.create(Registries.ITEM, VariedCommodities.id("piglin_safe_armor"));

    public static final RegistrySupplier<Item> COW_LEATHER_HELMET = armor("cow_leather_helmet", "cow_leather", Base.LEATHER, Type.HELMET);

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
        armor("infantry_helmet", "infantry", Base.IRON, Type.HELMET);
        armor("officer_chestplate", "officer", Base.DIAMOND, Type.CHESTPLATE);
        armor("silver_crown", "crown2", Base.DIAMOND, Type.HELMET);
        armor("golden_crown", "crown1", Base.DIAMOND, Type.HELMET);
        set("ninja", "ninja", Base.IRON, "helmet", "chestplate", "leggings");
        armor("bandit_mask", "bandit_mask", Base.IRON, Type.HELMET);
        VCArmorMaterials.get(VC, "paper_crown", Base.LEATHER, true);
        DYEABLE.add(ITEMS.register("paper_crown", () -> new VCArmorItem.Dyeable(VC, "paper_crown", Base.LEATHER, Type.HELMET, false, props())));
        skirt("chainmail_skirt", "minecraft", "chainmail", Base.CHAIN);
        DYEABLE.add(skirt("leather_skirt", "minecraft", "leather", Base.LEATHER));
        skirt("golden_skirt", "minecraft", "gold", Base.GOLD);
        skirt("iron_skirt", "minecraft", "iron", Base.IRON);
        skirt("diamond_skirt", "minecraft", "diamond", Base.DIAMOND);
        skirt("emerald_skirt", VC, "full_emerald", Base.EMERALD);
        skirt("bronze_skirt", VC, "full_bronze", Base.BRONZE);
        skirt("demonic_skirt", VC, "demonic", Base.DIAMOND);
        skirt("mithril_skirt", VC, "mithril", Base.MITHRIL);
    }

    public static Item.Properties props() {
        return new Item.Properties().arch$tab(VCTabs.ARMORS);
    }

    private static void set(String prefix, String texture, Base base, String... pieces) {
        for (String piece : pieces) {
            Type type = switch (piece) {
                case "helmet" -> Type.HELMET;
                case "chestplate" -> Type.CHESTPLATE;
                case "leggings" -> Type.LEGGINGS;
                default -> Type.BOOTS;
            };
            armor(prefix + "_" + piece, texture, base, type);
        }
    }

    private static RegistrySupplier<Item> armor(String name, String texture, Base base, Type type) {
        VCArmorMaterials.get(VC, texture, base, false);
        return ITEMS.register(name, () -> new VCArmorItem(VC, texture, base, type, false, false, props()));
    }

    private static RegistrySupplier<Item> skirt(String name, String namespace, String texture, Base base) {
        boolean dyeable = texture.equals("leather");
        VCArmorMaterials.get(namespace, texture, base, dyeable);
        RegistrySupplier<Item> item = ITEMS.register(name, () -> dyeable
                ? new VCArmorItem.Dyeable(namespace, texture, base, Type.LEGGINGS, true, props())
                : new VCArmorItem(namespace, texture, base, Type.LEGGINGS, true, false, props()));
        SKIRTS.add(item);
        return item;
    }

    public static void init() {
    }

    private VCArmors() {
    }
}
