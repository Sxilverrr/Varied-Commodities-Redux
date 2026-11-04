package com.sxilverr.variedcommoditiesredux;

import com.sxilverr.variedcommoditiesredux.item.GunItem;
import com.sxilverr.variedcommoditiesredux.item.MiscItems;
import com.sxilverr.variedcommoditiesredux.item.ReloadItem;
import com.sxilverr.variedcommoditiesredux.item.StaffItem;
import com.sxilverr.variedcommoditiesredux.item.ThrowingItem;
import com.sxilverr.variedcommoditiesredux.item.VCMaterial;
import com.sxilverr.variedcommoditiesredux.item.VCWeaponItem;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.sxilverr.variedcommoditiesredux.VCItems.ITEMS;

public final class VCWeapons {
    public static final Map<VCMaterial, RegistrySupplier<Item>> GUNS = family(VCMaterial.GUNS, "gun", m -> new GunItem(m, props()));
    public static final RegistrySupplier<Item> MACHINE_GUN = ITEMS.register("machine_gun", () -> new ReloadItem(ReloadItem.Kind.MACHINE_GUN, props()));
    public static final Map<VCMaterial, RegistrySupplier<Item>> STAFFS = family(VCMaterial.ALL, "staff", m -> new StaffItem(m, spell(m), staffColors(m), props()));
    public static final Map<DyeColor, RegistrySupplier<Item>> ELEMENTAL_STAFFS = VCItems.colored("elemental_staff", c -> StaffItem.elemental(c, props()));
    public static final Map<VCMaterial, RegistrySupplier<Item>> TRIDENTS = melee("trident", VCMaterial.ALL, 1);
    public static final RegistrySupplier<Item> CURSED_TRIDENT = weapon("cursed_trident", VCMaterial.EMERALD.tier(), 1);
    public static final RegistrySupplier<Item> KUNAI = ITEMS.register("kunai", () -> new VCWeaponItem(Tiers.IRON, 1, true, props().stacksTo(1)));
    public static final RegistrySupplier<Item> REVERSE_KUNAI = ITEMS.register("reverse_kunai", () -> new VCWeaponItem(Tiers.IRON, 1, true, props().stacksTo(1)));
    public static final RegistrySupplier<Item> SHURIKEN = ITEMS.register("shuriken", () -> new ThrowingItem(ThrowingItem.Kind.SHURIKEN, props()));
    public static final RegistrySupplier<Item> GIANT_SHURIKEN = ITEMS.register("giant_shuriken", () -> new ThrowingItem(ThrowingItem.Kind.GIANT_SHURIKEN, props()));
    public static final RegistrySupplier<Item> KATANA = weapon("katana", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> KUKRI = weapon("kukri", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> NINJA_CLAW = weapon("ninja_claw", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> STEEL_CLAW = weapon("steel_claw", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> BEAR_CLAW = weapon("bear_claw", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> KATAR = weapon("katar", Tiers.IRON, 1);
    public static final Map<VCMaterial, RegistrySupplier<Item>> SWORDS = melee("sword", VCMaterial.SWORDS, 1);
    public static final RegistrySupplier<Item> LEAF_BLADE = weapon("leaf_blade", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> GOLF_CLUB = weapon("golf_club", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> HAMMER = weapon("hammer", Tiers.STONE, 1);
    public static final RegistrySupplier<Item> BASEBALL_BAT = weapon("baseball_bat", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> LEAD_PIPE = weapon("lead_pipe", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> CLEAVER = weapon("cleaver", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> SABER = weapon("saber", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> HOCKEY_STICK = weapon("hockey_stick", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> SLEDGEHAMMER = weapon("sledgehammer", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> BROKEN_BOTTLE = weapon("broken_bottle", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> COMBAT_KNIFE = weapon("combat_knife", Tiers.STONE, 1);
    public static final RegistrySupplier<Item> MACUAHUITL = weapon("macuahuitl", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> BO_STAFF = weapon("bo_staff", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> SAI = weapon("sai", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> SLINGSHOT = ITEMS.register("slingshot", () -> new ThrowingItem(ThrowingItem.Kind.SLINGSHOT, props()));
    public static final RegistrySupplier<Item> MUSKET = ITEMS.register("musket", () -> new ReloadItem(ReloadItem.Kind.MUSKET, props()));
    public static final RegistrySupplier<Item> CROSSBOW = ITEMS.register("crossbow", () -> new ReloadItem(ReloadItem.Kind.CROSSBOW, props()));
    public static final RegistrySupplier<Item> BATON = weapon("baton", Tiers.STONE, 1);
    public static final RegistrySupplier<Item> CHAINSAW_GUN = weapon("chainsaw_gun", Tiers.DIAMOND, 1);
    public static final RegistrySupplier<Item> RAPIER = weapon("rapier", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> CROWBAR = weapon("crowbar", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> PIPE_WRENCH = weapon("pipe_wrench", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> SWISS_ARMY_KNIFE = weapon("swiss_army_knife", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> WRENCH = weapon("wrench", Tiers.IRON, 1);
    public static final RegistrySupplier<Item> EXCALIBUR = weapon("excalibur", Tiers.DIAMOND, 1);
    public static final Map<VCMaterial, RegistrySupplier<Item>> ROUND_SHIELDS = family(VCMaterial.ROUND_SHIELDS, "round_shield", m -> new MiscItems.Shield(m, props()));
    public static final Map<VCMaterial, RegistrySupplier<Item>> SHIELDS = family(VCMaterial.SHIELDS, "shield", m -> new MiscItems.Shield(m, props()));
    public static final RegistrySupplier<Item> HEATER_SHIELD = ITEMS.register("heater_shield", () -> new MiscItems.Shield(VCMaterial.IRON, props()));
    public static final RegistrySupplier<Item> CRESCENT_SHIELD = ITEMS.register("crescent_shield", () -> new MiscItems.Shield(VCMaterial.IRON, props()));
    public static final RegistrySupplier<Item> SCUTUM_SHIELD = ITEMS.register("scutum_shield", () -> new MiscItems.Shield(VCMaterial.IRON, props()));
    public static final RegistrySupplier<Item> TOWER_SHIELD = ITEMS.register("tower_shield", () -> new MiscItems.Shield(VCMaterial.IRON, props()));
    public static final Map<VCMaterial, RegistrySupplier<Item>> SPEARS = melee("spear", VCMaterial.ALL, 2);
    public static final Map<VCMaterial, RegistrySupplier<Item>> HALBERDS = melee("halberd", VCMaterial.ALL, 1);
    public static final Map<VCMaterial, RegistrySupplier<Item>> BATTLEAXES = melee("battleaxe", VCMaterial.ALL, 1);
    public static final Map<VCMaterial, RegistrySupplier<Item>> WARHAMMERS = melee("warhammer", VCMaterial.ALL, 1);
    public static final Map<VCMaterial, RegistrySupplier<Item>> MACES = melee("mace", VCMaterial.ALL, 1);
    public static final Map<VCMaterial, RegistrySupplier<Item>> DAGGERS = melee("dagger", VCMaterial.ALL, 2);
    public static final Map<VCMaterial, RegistrySupplier<Item>> REVERSE_DAGGERS = melee("reverse_dagger", VCMaterial.ALL, 2);
    public static final Map<VCMaterial, RegistrySupplier<Item>> SCYTHES = melee("scythe", VCMaterial.ALL, 1);
    public static final Map<VCMaterial, RegistrySupplier<Item>> GLAIVES = melee("glaive", VCMaterial.ALL, 1);
    public static final Map<VCMaterial, RegistrySupplier<Item>> BROADSWORDS = melee("broadsword", VCMaterial.ALL, 1);
    public static final RegistrySupplier<Item> HOLY_HAND_GRENADE = ITEMS.register("holy_hand_grenade", () -> new ThrowingItem(ThrowingItem.Kind.HOLY_HAND_GRENADE, props()));

    public static Item.Properties props() {
        return new Item.Properties().arch$tab(VCTabs.WEAPONS);
    }

    private static Supplier<ItemStack> spell(VCMaterial material) {
        return () -> new ItemStack(switch (material) {
            case WOODEN -> VCItems.NATURE_SPELL.get();
            case STONE, DEMONIC -> VCItems.DARK_SPELL.get();
            case IRON, MITHRIL -> VCItems.HOLY_SPELL.get();
            case BRONZE -> VCItems.LIGHTNING_SPELL.get();
            case GOLDEN -> VCItems.FIRE_SPELL.get();
            case DIAMOND, FROST -> VCItems.ICE_SPELL.get();
            case EMERALD -> VCItems.ARCANE_SPELL.get();
        });
    }

    private static int[] staffColors(VCMaterial material) {
        return switch (material) {
            case WOODEN -> new int[]{5, 12};
            case STONE, DEMONIC -> new int[]{5649239, 4400964};
            case BRONZE -> new int[]{8648694, 6091007};
            case IRON, MITHRIL -> new int[]{16580553, 15728535};
            case GOLDEN -> new int[]{1, 14};
            case DIAMOND, FROST -> new int[]{9756653, 4503295};
            case EMERALD -> new int[]{16761831, 16487167};
        };
    }

    private static RegistrySupplier<Item> weapon(String name, Tier tier, int durabilityCost) {
        return ITEMS.register(name, () -> new VCWeaponItem(tier, durabilityCost, false, props()));
    }

    private static Map<VCMaterial, RegistrySupplier<Item>> melee(String name, VCMaterial[] materials, int durabilityCost) {
        return family(materials, name, m -> new VCWeaponItem(m.tier(), durabilityCost, false, props()));
    }

    private static Map<VCMaterial, RegistrySupplier<Item>> family(VCMaterial[] materials, String name, Function<VCMaterial, Item> factory) {
        Map<VCMaterial, RegistrySupplier<Item>> map = new EnumMap<>(VCMaterial.class);
        for (VCMaterial material : materials) {
            map.put(material, ITEMS.register(material.id() + "_" + name, () -> factory.apply(material)));
        }
        return map;
    }

    public static void init() {
    }

    private VCWeapons() {
    }
}
