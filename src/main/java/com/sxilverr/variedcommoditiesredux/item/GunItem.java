package com.sxilverr.variedcommoditiesredux.item;

import com.sxilverr.variedcommoditiesredux.VCConfig;
import com.sxilverr.variedcommoditiesredux.Compat;
import com.sxilverr.variedcommoditiesredux.VCEnchants;
import com.sxilverr.variedcommoditiesredux.VCItems;
import com.sxilverr.variedcommoditiesredux.VCSounds;
import com.sxilverr.variedcommoditiesredux.entity.VCProjectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GunItem extends ChargeItem {
    private final VCMaterial material;

    public GunItem(VCMaterial material, Properties properties) {
        super(properties.durability(material.maxUses()));
        this.material = material;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int remaining) {
        if (level.isClientSide) return;
        if (entity instanceof Player player) {
            boolean free = player.getAbilities().instabuild || VCEnchants.infinity(level, stack);
            if (!free && !ItemUtil.has(player, VCItems.BULLET.get())) {
                ItemUtil.playSound(player, VCSounds.GUN_EMPTY.get(), 1.0F, 1.0F);
                return;
            }
            if (usedTicks(stack, entity, remaining) < 10) return;
            Compat.damage(stack, 1, player, player.getUsedItemHand());
            if (!free) ItemUtil.consume(player, VCItems.BULLET.get());
        }
        int damage = (material.damage() + 1) / 2 + 5;
        damage = (int) (damage + damage * VCEnchants.damage(level, stack) * 0.5F);
        VCProjectile projectile = new VCProjectile(level, entity, new ItemStack(VCItems.BULLET.get()));
        projectile.damage = (float) (damage * VCConfig.gunDamageMultiplier);
        ItemStack gun = stack.copy();
        projectile.callback = (p, pos, hit) -> {
            if (hit instanceof LivingEntity living) VCEnchants.applyEffects(level, gun, living);
            return false;
        };
        projectile.setSpeed(40);
        projectile.shoot(material.damage() + 1);
        ItemUtil.playSound(entity, VCSounds.GUN_SHOT.get(), 1.0F, level.random.nextFloat() * 0.3F + 0.8F);
        level.addFreshEntity(projectile);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remaining) {
        if (!level.isClientSide && usedTicks(stack, entity, remaining) == 8) {
            ItemUtil.playSound(entity, VCSounds.GUN_TRIGGER.get(), 1.0F, 1.0F / (level.random.nextFloat() * 0.4F + 0.8F));
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return material.enchantability();
    }
}
