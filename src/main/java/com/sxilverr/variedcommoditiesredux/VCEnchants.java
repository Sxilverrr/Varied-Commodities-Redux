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

//? if >=1.21 {
/*import net.minecraft.resources.ResourceKey;
*///?} else {
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.Items;
import java.util.List;
//?}

public final class VCEnchants {
    //? if >=1.21 {
    /*private static final ResourceKey<Enchantment> DAMAGE = key("damage");
    private static final ResourceKey<Enchantment> POISON = key("poison");
    private static final ResourceKey<Enchantment> CONFUSION = key("confusion");

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, VariedCommodities.id(name));
    }

    private static int level(Level level, ResourceKey<Enchantment> key, ItemStack stack) {
        return level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolder(key)
                .map(holder -> EnchantmentHelper.getItemEnchantmentLevel(holder, stack)).orElse(0);
    }

    public static void register() {
    }
    *///?} else {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(VariedCommodities.MODID, Registries.ENCHANTMENT);
    private static final RegistrySupplier<Enchantment> DAMAGE = ENCHANTMENTS.register("damage", () -> new VCEnchantment(Enchantment.Rarity.COMMON, 5, 1, 10, 15));
    private static final RegistrySupplier<Enchantment> POISON = ENCHANTMENTS.register("poison", () -> new VCEnchantment(Enchantment.Rarity.UNCOMMON, 2, 12, 20, 25));
    private static final RegistrySupplier<Enchantment> CONFUSION = ENCHANTMENTS.register("confusion", () -> new VCEnchantment(Enchantment.Rarity.RARE, 2, 12, 20, 25));

    private static int level(Level level, RegistrySupplier<Enchantment> enchantment, ItemStack stack) {
        return EnchantmentHelper.getItemEnchantmentLevel(enchantment.get(), stack);
    }

    public static void register() {
        ENCHANTMENTS.register();
    }

    public static void adjustTableResults(int power, ItemStack stack, List<EnchantmentInstance> results) {
        if (stack.is(Items.BOOK)) return;
        boolean applicable = isApplicable(stack);
        results.removeIf(e -> e.enchantment instanceof VCEnchantment && !applicable);
        if (!applicable) return;
        for (Enchantment enchantment : List.of(DAMAGE.get(), POISON.get(), CONFUSION.get(), Enchantments.INFINITY_ARROWS)) {
            if (results.stream().anyMatch(e -> e.enchantment == enchantment)) continue;
            for (int lvl = enchantment.getMaxLevel(); lvl >= enchantment.getMinLevel(); lvl--) {
                if (power >= enchantment.getMinCost(lvl) && power <= enchantment.getMaxCost(lvl)) {
                    results.add(new EnchantmentInstance(enchantment, lvl));
                    break;
                }
            }
        }
    }

    public static class VCEnchantment extends Enchantment {
        private final int maxLevel;
        private final int base;
        private final int perLevel;
        private final int span;

        VCEnchantment(Rarity rarity, int maxLevel, int base, int perLevel, int span) {
            super(rarity, EnchantmentCategory.BREAKABLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
            this.maxLevel = maxLevel;
            this.base = base;
            this.perLevel = perLevel;
            this.span = span;
        }

        @Override
        public int getMinCost(int level) {
            return base + (level - 1) * perLevel;
        }

        @Override
        public int getMaxCost(int level) {
            return getMinCost(level) + span;
        }

        @Override
        public int getMaxLevel() {
            return maxLevel;
        }

        @Override
        public boolean canEnchant(ItemStack stack) {
            return isApplicable(stack);
        }
    }
    //?}

    public static boolean isApplicable(ItemStack stack) {
        return stack.getItem() instanceof GunItem || stack.getItem() instanceof StaffItem;
    }

    public static int damage(Level level, ItemStack stack) {
        return level(level, DAMAGE, stack);
    }

    public static boolean infinity(Level level, ItemStack stack) {
        //? if >=1.21 {
        /*return level(level, Enchantments.INFINITY, stack) > 0;
        *///?} else {
        return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
        //?}
    }

    public static void applyEffects(Level level, ItemStack stack, LivingEntity target) {
        int confusion = level(level, CONFUSION, stack);
        if (confusion > 0 && target.getRandom().nextInt(4) < confusion) {
            target.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100));
        }
        int poison = level(level, POISON, stack);
        if (poison > 0 && target.getRandom().nextInt(4) < poison) {
            target.addEffect(new MobEffectInstance(MobEffects.POISON, 100));
        }
    }

    private VCEnchants() {
    }
}
