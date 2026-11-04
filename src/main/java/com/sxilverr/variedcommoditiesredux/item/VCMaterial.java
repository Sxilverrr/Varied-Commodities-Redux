package com.sxilverr.variedcommoditiesredux.item;

import com.sxilverr.variedcommoditiesredux.VCItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

//? if >=1.21 {
/*import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
*///?}

public enum VCMaterial {
    WOODEN("wooden", 0, 59, 0, 15, Tiers.WOOD),
    STONE("stone", 1, 131, 1, 5, Tiers.STONE),
    IRON("iron", 2, 250, 2, 14, Tiers.IRON),
    GOLDEN("golden", 0, 32, 1, 22, Tiers.GOLD),
    DIAMOND("diamond", 3, 1561, 3, 10, Tiers.DIAMOND),
    BRONZE("bronze", 2, 170, 2, 15, null),
    EMERALD("emerald", 3, 1000, 4, 10, null),
    DEMONIC("demonic", 3, 100, 6, 10, null),
    FROST("frost", 2, 59, 3, 5, null),
    MITHRIL("mithril", 3, 3000, 3, 10, null);

    public static final VCMaterial[] BASIC = {WOODEN, STONE, IRON, GOLDEN, DIAMOND};
    public static final VCMaterial[] ALL = values();
    public static final VCMaterial[] GUNS = {WOODEN, STONE, IRON, GOLDEN, DIAMOND, BRONZE, EMERALD};
    public static final VCMaterial[] ROUND_SHIELDS = {WOODEN, STONE, IRON, GOLDEN, DIAMOND, BRONZE, EMERALD, DEMONIC, MITHRIL};
    public static final VCMaterial[] SHIELDS = {WOODEN, STONE, IRON, GOLDEN, DIAMOND, BRONZE, EMERALD, FROST};
    public static final VCMaterial[] SWORDS = {BRONZE, EMERALD, DEMONIC, FROST, MITHRIL};

    private final String id;
    private final int harvestLevel;
    private final int maxUses;
    private final int damage;
    private final int enchantability;
    private final Tier vanillaTier;

    VCMaterial(String id, int harvestLevel, int maxUses, int damage, int enchantability, Tier vanillaTier) {
        this.id = id;
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.damage = damage;
        this.enchantability = enchantability;
        this.vanillaTier = vanillaTier;
    }

    public String id() {
        return id;
    }

    public int harvestLevel() {
        return harvestLevel;
    }

    public int maxUses() {
        return maxUses;
    }

    public int damage() {
        return damage;
    }

    public int enchantability() {
        return enchantability;
    }

    public Tier tier() {
        return vanillaTier != null ? vanillaTier : CUSTOM_TIERS[ordinal()];
    }

    private static final Tier[] CUSTOM_TIERS = new Tier[values().length];

    static {
        CUSTOM_TIERS[BRONZE.ordinal()] = new CustomTier(2, 170, 5.0F, 2.0F, 15, () -> Ingredient.of(VCItems.BRONZE_INGOT.get()));
        CUSTOM_TIERS[EMERALD.ordinal()] = new CustomTier(3, 1000, 8.0F, 4.0F, 10, () -> Ingredient.of(Items.EMERALD));
        CUSTOM_TIERS[DEMONIC.ordinal()] = new CustomTier(3, 100, 8.0F, 6.0F, 22, () -> Ingredient.of(VCItems.DEMONIC_INGOT.get()));
        CUSTOM_TIERS[FROST.ordinal()] = new CustomTier(2, 59, 6.0F, 3.0F, 5, () -> Ingredient.of(Blocks.ICE));
        CUSTOM_TIERS[MITHRIL.ordinal()] = new CustomTier(3, 3000, 8.0F, 3.0F, 10, () -> Ingredient.of(VCItems.MITHRIL_INGOT.get()));
    }

    private record CustomTier(int level, int uses, float speed, float damage, int enchantment, Supplier<Ingredient> repair) implements Tier {
        @Override
        public int getUses() {
            return uses;
        }

        @Override
        public float getSpeed() {
            return speed;
        }

        @Override
        public float getAttackDamageBonus() {
            return damage;
        }

        @Override
        public int getEnchantmentValue() {
            return enchantment;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return repair.get();
        }

        //? if >=1.21 {
        /*@Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return level >= 3 ? BlockTags.INCORRECT_FOR_DIAMOND_TOOL : level == 2 ? BlockTags.INCORRECT_FOR_IRON_TOOL : level == 1 ? BlockTags.INCORRECT_FOR_STONE_TOOL : BlockTags.INCORRECT_FOR_WOODEN_TOOL;
        }
        *///?} else {
        @Override
        public int getLevel() {
            return level;
        }
        //?}
    }
}
