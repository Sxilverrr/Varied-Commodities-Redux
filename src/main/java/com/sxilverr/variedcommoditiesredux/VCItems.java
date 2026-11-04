package com.sxilverr.variedcommoditiesredux;

import com.sxilverr.variedcommoditiesredux.item.MiscItems;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public final class VCItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(VariedCommodities.MODID, Registries.ITEM);

    public static final RegistrySupplier<Item> RECIPE_BOOK = ITEMS.register("recipe_book", () -> new MiscItems.RecipeBook(props()));
    public static final RegistrySupplier<Item> WOODEN_COIN = basic("wooden_coin");
    public static final RegistrySupplier<Item> STONE_COIN = basic("stone_coin");
    public static final RegistrySupplier<Item> BRONZE_COIN = basic("bronze_coin");
    public static final RegistrySupplier<Item> IRON_COIN = basic("iron_coin");
    public static final RegistrySupplier<Item> GOLDEN_COIN = basic("golden_coin");
    public static final RegistrySupplier<Item> DIAMOND_COIN = basic("diamond_coin");
    public static final RegistrySupplier<Item> EMERALD_COIN = basic("emerald_coin");
    public static final RegistrySupplier<Item> SAPPHIRE = basic("sapphire");
    public static final RegistrySupplier<Item> RUBY = basic("ruby");
    public static final RegistrySupplier<Item> AMETHYST = basic("amethyst");
    public static final RegistrySupplier<Item> BRONZE_INGOT = basic("bronze_ingot");
    public static final RegistrySupplier<Item> STEEL_INGOT = basic("steel_ingot");
    public static final RegistrySupplier<Item> DEMONIC_INGOT = basic("demonic_ingot");
    public static final RegistrySupplier<Item> MITHRIL_INGOT = basic("mithril_ingot");
    public static final RegistrySupplier<Item> BANJO = ITEMS.register("banjo", () -> new MiscItems.Music(false, props()));
    public static final RegistrySupplier<Item> VIOLIN = ITEMS.register("violin", () -> new MiscItems.Music(true, props()));
    public static final RegistrySupplier<Item> VIOLIN_BOW = ITEMS.register("violin_bow", () -> new Item(props().stacksTo(1)));
    public static final RegistrySupplier<Item> HARP = ITEMS.register("harp", () -> new MiscItems.Music(true, props()));
    public static final RegistrySupplier<Item> GUITAR = ITEMS.register("guitar", () -> new MiscItems.Music(false, props()));
    public static final RegistrySupplier<Item> FRENCH_HORN = ITEMS.register("french_horn", () -> new MiscItems.Music(false, props()));
    public static final RegistrySupplier<Item> OCARINA = ITEMS.register("ocarina", () -> new MiscItems.Music(true, props()));
    public static final RegistrySupplier<Item> CLARINET = ITEMS.register("clarinet", () -> new MiscItems.Music(true, props()));
    public static final RegistrySupplier<Item> EARTH_ELEMENT = basic("earth_element");
    public static final RegistrySupplier<Item> WATER_ELEMENT = basic("water_element");
    public static final RegistrySupplier<Item> FIRE_ELEMENT = basic("fire_element");
    public static final RegistrySupplier<Item> AIR_ELEMENT = basic("air_element");
    public static final RegistrySupplier<Item> NATURE_SPELL = basic("nature_spell");
    public static final RegistrySupplier<Item> ARCANE_SPELL = basic("arcane_spell");
    public static final RegistrySupplier<Item> LIGHTNING_SPELL = basic("lightning_spell");
    public static final RegistrySupplier<Item> ICE_SPELL = basic("ice_spell");
    public static final RegistrySupplier<Item> FIRE_SPELL = basic("fire_spell");
    public static final RegistrySupplier<Item> DARK_SPELL = basic("dark_spell");
    public static final RegistrySupplier<Item> HOLY_SPELL = basic("holy_spell");
    public static final Map<DyeColor, RegistrySupplier<Item>> ORBS = colored("orb");
    public static final Map<DyeColor, RegistrySupplier<Item>> BROKEN_ORBS = colored("broken_orb");
    public static final RegistrySupplier<Item> ANCIENT_SCROLL = basic("ancient_scroll");
    public static final RegistrySupplier<Item> ARTIFACT = basic("artifact");
    public static final RegistrySupplier<Item> LOCKET = basic("locket");
    public static final RegistrySupplier<Item> SILK = basic("silk");
    public static final RegistrySupplier<Item> STATUETTE = basic("statuette");
    public static final RegistrySupplier<Item> TABLET = basic("tablet");
    public static final RegistrySupplier<Item> HEART = basic("heart");
    public static final RegistrySupplier<Item> MONEY = basic("money");
    public static final RegistrySupplier<Item> NECKLACE = basic("necklace");
    public static final RegistrySupplier<Item> USB_STICK = basic("usb_stick");
    public static final RegistrySupplier<Item> ANCIENT_COIN = basic("ancient_coin");
    public static final RegistrySupplier<Item> LETTER = basic("letter");
    public static final RegistrySupplier<Item> PLANS = basic("plans");
    public static final RegistrySupplier<Item> SATCHEL = basic("satchel");
    public static final RegistrySupplier<Item> BAG = basic("bag");
    public static final RegistrySupplier<Item> CRYSTAL = basic("crystal");
    public static final RegistrySupplier<Item> SEVERED_EAR = basic("severed_ear");
    public static final RegistrySupplier<Item> PHONE = basic("phone");
    public static final RegistrySupplier<Item> BROKEN_ARROW = basic("broken_arrow");
    public static final RegistrySupplier<Item> CAR_KEY = basic("car_key");
    public static final RegistrySupplier<Item> KEY = basic("key");
    public static final RegistrySupplier<Item> OLD_KEY = basic("old_key");
    public static final RegistrySupplier<Item> PENDANT = basic("pendant");
    public static final RegistrySupplier<Item> BLUEPRINT = basic("blueprint");
    public static final RegistrySupplier<Item> RING = basic("ring");
    public static final RegistrySupplier<Item> SKULL = basic("skull");
    public static final RegistrySupplier<Item> LIGHTER = ITEMS.register("lighter", () -> new FlintAndSteelItem(props().durability(64)));
    public static final RegistrySupplier<Item> CHICKEN_SWORD = ITEMS.register("chicken_sword", () -> new Item(props().stacksTo(1)));
    public static final RegistrySupplier<Item> HANDCUFFS = ITEMS.register("handcuffs", () -> new Item(props().stacksTo(1)));
    public static final RegistrySupplier<Item> MAGIC_WAND = ITEMS.register("magic_wand", () -> new MiscItems.Wand(props()));

    public static final RegistrySupplier<Item> BULLET = ITEMS.register("bullet", () -> new Item(VCWeapons.props()));
    public static final RegistrySupplier<Item> MANA = ITEMS.register("mana", () -> new Item(VCWeapons.props()));
    public static final RegistrySupplier<Item> CROSSBOW_BOLT = ITEMS.register("crossbow_bolt", () -> new Item(VCWeapons.props()));

    public static Item.Properties props() {
        return new Item.Properties().arch$tab(VCTabs.ITEMS);
    }

    private static RegistrySupplier<Item> basic(String name) {
        return ITEMS.register(name, () -> new Item(props()));
    }

    private static Map<DyeColor, RegistrySupplier<Item>> colored(String name) {
        return colored(name, color -> new MiscItems.Colored(color, props()));
    }

    static Map<DyeColor, RegistrySupplier<Item>> colored(String name, Function<DyeColor, Item> factory) {
        Map<DyeColor, RegistrySupplier<Item>> map = new EnumMap<>(DyeColor.class);
        for (DyeColor color : DyeColor.values()) {
            map.put(color, ITEMS.register(color.getName() + "_" + name, () -> factory.apply(color)));
        }
        return map;
    }

    private VCItems() {
    }
}
