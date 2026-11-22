package com.sxilverr.variedcommoditiesredux.item;

import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

public enum VCMaterial {
    WOODEN("wooden", 0, 59, 0, 15, ToolMaterial.WOOD),
    STONE("stone", 1, 131, 1, 5, ToolMaterial.STONE),
    IRON("iron", 2, 250, 2, 14, ToolMaterial.IRON),
    GOLDEN("golden", 0, 32, 1, 22, ToolMaterial.GOLD),
    DIAMOND("diamond", 3, 1561, 3, 10, ToolMaterial.DIAMOND),
    BRONZE("bronze", 2, 170, 2, 15, custom(2, 170, 5.0F, 2.0F, 15, "c", "ingots/bronze")),
    EMERALD("emerald", 3, 1000, 4, 10, custom(3, 1000, 8.0F, 4.0F, 10, "c", "gems/emerald")),
    DEMONIC("demonic", 3, 100, 6, 10, custom(3, 100, 8.0F, 6.0F, 22, VariedCommodities.MODID, "demonic_tool_materials")),
    FROST("frost", 2, 59, 3, 5, custom(2, 59, 6.0F, 3.0F, 5, VariedCommodities.MODID, "frost_tool_materials")),
    MITHRIL("mithril", 3, 3000, 3, 10, custom(3, 3000, 8.0F, 3.0F, 10, "c", "ingots/mithril"));

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
    private final ToolMaterial material;

    VCMaterial(String id, int harvestLevel, int maxUses, int damage, int enchantability, ToolMaterial material) {
        this.id = id;
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.damage = damage;
        this.enchantability = enchantability;
        this.material = material;
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

    public ToolMaterial material() {
        return material;
    }

    private static ToolMaterial custom(int level, int uses, float speed, float damage, int enchantment, String namespace, String repair) {
        TagKey<Block> incorrect = level >= 3 ? BlockTags.INCORRECT_FOR_DIAMOND_TOOL
                : level == 2 ? BlockTags.INCORRECT_FOR_IRON_TOOL
                : level == 1 ? BlockTags.INCORRECT_FOR_STONE_TOOL : BlockTags.INCORRECT_FOR_WOODEN_TOOL;
        return new ToolMaterial(incorrect, uses, speed, damage, enchantment, TagKey.create(Registries.ITEM, VariedCommodities.rl(namespace, repair)));
    }

    public static TagKey<Item> tag(String namespace, String path) {
        return TagKey.create(Registries.ITEM, VariedCommodities.rl(namespace, path));
    }
}
