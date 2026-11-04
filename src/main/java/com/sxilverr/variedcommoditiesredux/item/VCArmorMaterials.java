package com.sxilverr.variedcommoditiesredux.item;

import com.sxilverr.variedcommoditiesredux.VCItems;
import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

//? if >=1.21 {
/*import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorMaterial;
import java.util.List;
*///?} else {
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorMaterial;
//?}

public final class VCArmorMaterials {
    public enum Base {
        LEATHER(5), CHAIN(15), IRON(15), GOLD(7), DIAMOND(33), MITHRIL(40), BRONZE(7), EMERALD(35);

        public final int durability;

        Base(int durability) {
            this.durability = durability;
        }
    }

    //? if >=1.21 {
    /*public static final DeferredRegister<ArmorMaterial> MATERIALS = DeferredRegister.create(VariedCommodities.MODID, Registries.ARMOR_MATERIAL);
    private static final Map<String, RegistrySupplier<ArmorMaterial>> CACHE = new HashMap<>();

    public static Holder<ArmorMaterial> get(String namespace, String texture, Base base, boolean dyeable) {
        String key = namespace + "_" + texture;
        return CACHE.computeIfAbsent(key, k -> MATERIALS.register(k, () -> create(VariedCommodities.rl(namespace, texture), base, dyeable)));
    }

    private static ArmorMaterial create(ResourceLocation texture, Base base, boolean dyeable) {
        List<ArmorMaterial.Layer> layers = dyeable
                ? List.of(new ArmorMaterial.Layer(texture, "", true), new ArmorMaterial.Layer(texture, "_overlay", false))
                : List.of(new ArmorMaterial.Layer(texture));
        ArmorMaterial vanilla = switch (base) {
            case LEATHER -> ArmorMaterials.LEATHER.value();
            case CHAIN -> ArmorMaterials.CHAIN.value();
            case IRON, BRONZE -> ArmorMaterials.IRON.value();
            case GOLD -> ArmorMaterials.GOLD.value();
            default -> ArmorMaterials.DIAMOND.value();
        };
        Map<ArmorItem.Type, Integer> defense = new EnumMap<>(vanilla.defense());
        int enchantment = vanilla.enchantmentValue();
        Holder<SoundEvent> sound = vanilla.equipSound();
        Supplier<Ingredient> repair = vanilla.repairIngredient();
        float toughness = vanilla.toughness();
        if (base == Base.MITHRIL) {
            enchantment = 20;
            sound = SoundEvents.ARMOR_EQUIP_IRON;
            repair = () -> Ingredient.of(VCItems.MITHRIL_INGOT.get());
        } else if (base == Base.BRONZE) {
            enchantment = 20;
            repair = () -> Ingredient.EMPTY;
            toughness = 0.0F;
        } else if (base == Base.EMERALD) {
            enchantment = 5;
            repair = () -> Ingredient.EMPTY;
        }
        return new ArmorMaterial(defense, enchantment, sound, repair, layers, toughness, 0.0F);
    }
    *///?} else {
    private static final Map<String, ArmorMaterial> CACHE = new HashMap<>();

    public static ArmorMaterial get(String namespace, String texture, Base base, boolean dyeable) {
        String name = namespace.equals("minecraft") ? texture : namespace + ":" + texture;
        return CACHE.computeIfAbsent(name, n -> new Named(n, base));
    }

    private record Named(String name, Base base) implements ArmorMaterial {
        private ArmorMaterial vanilla() {
            return switch (base) {
                case LEATHER -> ArmorMaterials.LEATHER;
                case CHAIN -> ArmorMaterials.CHAIN;
                case IRON, BRONZE -> ArmorMaterials.IRON;
                case GOLD -> ArmorMaterials.GOLD;
                default -> ArmorMaterials.DIAMOND;
            };
        }

        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            int health = switch (type) {
                case HELMET -> 11;
                case CHESTPLATE -> 16;
                case LEGGINGS -> 15;
                case BOOTS -> 13;
            };
            return health * base.durability;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return vanilla().getDefenseForType(type);
        }

        @Override
        public int getEnchantmentValue() {
            return switch (base) {
                case MITHRIL, BRONZE -> 20;
                case EMERALD -> 5;
                default -> vanilla().getEnchantmentValue();
            };
        }

        @Override
        public SoundEvent getEquipSound() {
            return base == Base.MITHRIL ? SoundEvents.ARMOR_EQUIP_IRON : vanilla().getEquipSound();
        }

        @Override
        public Ingredient getRepairIngredient() {
            return switch (base) {
                case MITHRIL -> Ingredient.of(VCItems.MITHRIL_INGOT.get());
                case BRONZE, EMERALD -> Ingredient.EMPTY;
                default -> vanilla().getRepairIngredient();
            };
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public float getToughness() {
            return base == Base.BRONZE ? 0.0F : vanilla().getToughness();
        }

        @Override
        public float getKnockbackResistance() {
            return 0.0F;
        }
    }
    //?}

    public static ResourceLocation texture(String namespace, String texture, boolean legs) {
        return VariedCommodities.rl(namespace, "textures/models/armor/" + texture + "_layer_" + (legs ? 2 : 1) + ".png");
    }

    private VCArmorMaterials() {
    }
}
