package com.sxilverr.variedcommoditiesredux;

import com.sxilverr.variedcommoditiesredux.item.GunItem;
import com.sxilverr.variedcommoditiesredux.item.StaffItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import net.minecraft.resources.ResourceKey;

public final class VCEnchants {
    private static final ResourceKey<Enchantment> DAMAGE = key("damage");
    private static final ResourceKey<Enchantment> POISON = key("poison");
    private static final ResourceKey<Enchantment> CONFUSION = key("confusion");

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, VariedCommodities.id(name));
    }

    private static int level(Level level, ResourceKey<Enchantment> key, ItemStack stack) {
        return level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(key)
                .map(holder -> EnchantmentHelper.getItemEnchantmentLevel(holder, stack)).orElse(0);
    }

    public static void register() {
    }

    public static boolean isApplicable(ItemStack stack) {
        return stack.getItem() instanceof GunItem || stack.getItem() instanceof StaffItem;
    }

    public static int damage(Level level, ItemStack stack) {
        return level(level, DAMAGE, stack);
    }

    public static boolean infinity(Level level, ItemStack stack) {
        return level(level, Enchantments.INFINITY, stack) > 0;
    }

    public static void applyEffects(Level level, ItemStack stack, LivingEntity target) {
        if (target.getRandom().nextInt(100) < level(level, CONFUSION, stack) * VCConfig.enchantEffectChance) {
            target.addEffect(new MobEffectInstance(MobEffects.NAUSEA, VCConfig.enchantEffectDuration));
        }
        if (target.getRandom().nextInt(100) < level(level, POISON, stack) * VCConfig.enchantEffectChance) {
            target.addEffect(new MobEffectInstance(MobEffects.POISON, VCConfig.enchantEffectDuration));
        }
    }

    private VCEnchants() {
    }
}
