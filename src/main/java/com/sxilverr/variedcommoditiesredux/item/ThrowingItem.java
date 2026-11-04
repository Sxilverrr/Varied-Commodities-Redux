package com.sxilverr.variedcommoditiesredux.item;

import com.sxilverr.variedcommoditiesredux.VCConfig;
import com.sxilverr.variedcommoditiesredux.Compat;
import com.sxilverr.variedcommoditiesredux.VCSounds;
import com.sxilverr.variedcommoditiesredux.entity.HolyHandGrenade;
import com.sxilverr.variedcommoditiesredux.entity.ThrownWeapon;
import com.sxilverr.variedcommoditiesredux.entity.VCProjectile;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class ThrowingItem extends ChargeItem {
    public enum Kind {
        SHURIKEN, GIANT_SHURIKEN, SLINGSHOT, HOLY_HAND_GRENADE
    }

    private final Kind kind;

    public ThrowingItem(Kind kind, Properties properties) {
        super(kind == Kind.SLINGSHOT ? properties.durability(384) : properties);
        this.kind = kind;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return kind == Kind.SLINGSHOT ? UseAnim.BOW : UseAnim.SPEAR;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int remaining) {
        if (level.isClientSide) return;
        int ticks = usedTicks(stack, entity, remaining);
        boolean creative = entity instanceof Player player && player.getAbilities().instabuild;
        switch (kind) {
            case SLINGSHOT -> {
                if (ticks < 6) return;
                if (entity instanceof Player player && !creative && !ItemUtil.consume(player, Items.COBBLESTONE)) return;
                Compat.damage(stack, 1, entity, entity.getUsedItemHand());
                VCProjectile projectile = new VCProjectile(level, entity, new ItemStack(Items.COBBLESTONE));
                projectile.damage = VCConfig.slingshotDamage;
                projectile.punch = ticks > 24 ? 2 : 1;
                projectile.setRotating(true);
                projectile.setHasGravity(true);
                projectile.setSpeed(14);
                projectile.shoot(1.0F);
                ItemUtil.playSound(entity, SoundEvents.ARROW_SHOOT, 1.0F, level.random.nextFloat() * 0.3F + 0.8F);
                level.addFreshEntity(projectile);
            }
            case HOLY_HAND_GRENADE -> {
                float charge = ticks / 20.0F;
                charge = Math.min(1.0F, (charge * charge + charge * 2.0F) / 3.0F);
                HolyHandGrenade grenade = new HolyHandGrenade(level, entity, new ItemStack(stack.getItem()));
                grenade.damage = 0.0F;
                grenade.setIs3D(true);
                grenade.setRotating(true);
                grenade.setHasGravity(true);
                grenade.setSpeed((int) (8.0F * charge) + 5);
                grenade.timeToLive = 34;
                if (entity instanceof Player player && !creative) ItemUtil.consume(player, this);
                grenade.shoot(1.0F);
                ItemUtil.playSound(entity, VCSounds.SWOOSH.get(), 1.0F, 1.0F);
                level.addFreshEntity(grenade);
            }
            default -> {
                ThrownWeapon weapon = new ThrownWeapon(level, entity, new ItemStack(stack.getItem()), kind == Kind.GIANT_SHURIKEN ? VCConfig.giantShurikenDamage : VCConfig.shurikenDamage, !VCConfig.shurikensBreak);
                weapon.pickup = creative ? AbstractArrow.Pickup.CREATIVE_ONLY : AbstractArrow.Pickup.ALLOWED;
                if (entity instanceof Player player && !creative) ItemUtil.consume(player, this);
                weapon.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, 1.2F, 1.0F);
                ItemUtil.playSound(entity, VCSounds.SWOOSH.get(), 1.0F, 1.0F);
                level.addFreshEntity(weapon);
            }
        }
    }
}
