package com.sxilverr.variedcommoditiesredux.item;

import com.sxilverr.variedcommoditiesredux.Compat;
import com.sxilverr.variedcommoditiesredux.VCConfig;
import com.sxilverr.variedcommoditiesredux.VCItems;
import com.sxilverr.variedcommoditiesredux.VCSounds;
import com.sxilverr.variedcommoditiesredux.entity.VCProjectile;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

public class ReloadItem extends ChargeItem {
    public enum Kind {
        MACHINE_GUN, MUSKET, CROSSBOW
    }


    private final Kind kind;

    public ReloadItem(Kind kind, Properties properties) {
        super(properties.durability(kind == Kind.MACHINE_GUN ? 80 : 129));
        this.kind = kind;
    }

    private Item ammo() {
        return kind == Kind.CROSSBOW ? VCItems.CROSSBOW_BOLT.get() : VCItems.BULLET.get();
    }

    private boolean needsAmmo() {
        return kind == Kind.CROSSBOW || VCConfig.gunsUseBullets;
    }

    private boolean hasAmmo(Player player) {
        return !needsAmmo() || ItemUtil.has(player, ammo());
    }

    private void useAmmo(Player player) {
        if (needsAmmo()) ItemUtil.consume(player, ammo());
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.getAbilities().instabuild) {
            if (kind == Kind.MACHINE_GUN) {
                if (!hasAmmo(player)) GunState.update(stack, s -> s.withReloading(true));
            } else if (hasAmmo(player) && !GunState.get(stack).loaded()) {
                GunState.update(stack, s -> s.withReloading(true));
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return GunState.get(stack).reloading() ? ItemUseAnimation.NONE : ItemUseAnimation.BOW;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remaining) {
        if (level.isClientSide()) return;
        int ticks = usedTicks(stack, entity, remaining);
        if (kind == Kind.MACHINE_GUN) {
            machineGunTick(level, entity, stack, ticks);
            return;
        }
        if (!(entity instanceof Player player) || player.getAbilities().instabuild) return;
        if (GunState.get(stack).reloading() && hasAmmo(player) && ticks == (kind == Kind.MUSKET ? VCConfig.musketReloadTime : VCConfig.heavyCrossbowReloadTime)) {
            ItemUtil.playSound(player, kind == Kind.MUSKET ? VCSounds.GUN_RELOAD.get() : SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);
            GunState.update(stack, s -> s.withLoaded(true));
        }
    }

    private void machineGunTick(Level level, LivingEntity entity, ItemStack stack, int ticks) {
        if (ticks % VCConfig.machineGunFireDelay != 0) return;
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            int shotsLeft = GunState.get(stack).shots() - ticks / VCConfig.machineGunFireDelay;
            if (GunState.get(stack).reloading() && hasAmmo(player)) {
                if (ticks > 0 && ticks <= 24) ItemUtil.playSound(player, VCSounds.GUN_RELOAD.get(), 1.0F, 1.0F);
                return;
            }
            if (shotsLeft <= 0 || !hasAmmo(player)) {
                ItemUtil.playSound(player, VCSounds.GUN_EMPTY.get(), 1.0F, 1.0F);
                return;
            }
            useAmmo(player);
            GunState.update(stack, s -> s.withReloading(false));
        }
        VCProjectile projectile = new VCProjectile(level, entity, new ItemStack(VCItems.BULLET.get()));
        projectile.damage = VCConfig.machineGunDamage;
        projectile.setSpeed(40);
        projectile.shoot(2.0F);
        ItemUtil.playSound(entity, VCSounds.GUN_SHOT.get(), 0.9F, level.random.nextFloat() * 0.3F + 0.8F);
        level.addFreshEntity(projectile);
    }

    @Override
    protected void release(ItemStack stack, Level level, LivingEntity entity, int remaining) {
        if (kind == Kind.MACHINE_GUN) {
            machineGunRelease(stack, entity, usedTicks(stack, entity, remaining));
            return;
        }
        if (level.isClientSide()) return;
        if (entity instanceof Player player) {
            boolean creative = player.getAbilities().instabuild;
            if (!GunState.get(stack).loaded() && !creative) {
                if (kind == Kind.MUSKET) ItemUtil.playSound(player, VCSounds.GUN_EMPTY.get(), 1.0F, 1.0F);
                return;
            }
            if (GunState.get(stack).reloading() && !creative) {
                GunState.update(stack, s -> s.withReloading(false));
                return;
            }
            if (!creative) useAmmo(player);
            GunState.update(stack, s -> s.withLoaded(false));
        }
        Compat.damage(stack, 1, entity, entity.getUsedItemHand());
        if (kind == Kind.MUSKET) {
            VCProjectile projectile = new VCProjectile(level, entity, new ItemStack(VCItems.BULLET.get()));
            projectile.damage = VCConfig.musketDamage;
            projectile.punch = VCConfig.musketKnockback;
            projectile.setSpeed(50);
            projectile.shoot(2.0F);
            level.addFreshEntity(projectile);
            ItemUtil.playSound(entity, Compat.explodeSound(), 0.9F, level.random.nextFloat() * 0.3F + 1.8F);
            ItemUtil.playSound(entity, SoundEvents.LIGHTNING_BOLT_THUNDER, 2.0F, level.random.nextFloat() * 0.3F + 1.8F);
        } else {
            Arrow arrow = new Arrow(level, entity, new ItemStack(VCItems.CROSSBOW_BOLT.get()), stack);
            arrow.setBaseDamage(VCConfig.heavyCrossbowDamage / 2.0);
            arrow.pickup = VCConfig.heavyCrossbowBoltPickup ? AbstractArrow.Pickup.ALLOWED : AbstractArrow.Pickup.DISALLOWED;
            arrow.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, 2.0F, 2.0F);
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 1.0F, level.random.nextFloat() * 0.3F + 0.8F);
            level.addFreshEntity(arrow);
        }
    }

    private void machineGunRelease(ItemStack stack, LivingEntity entity, int ticks) {
        if (!(entity instanceof Player player) || player.getAbilities().instabuild) return;
        int shotsLeft = GunState.get(stack).shots() - ticks / VCConfig.machineGunFireDelay;
        if (GunState.get(stack).reloading()) {
            int loaded = Math.min(VCConfig.machineGunClipSize, ticks / 5);
            if (loaded > 1) {
                GunState.update(stack, s -> s.withShots(loaded));
                GunState.update(stack, s -> s.withReloading(false));
            }
        } else if (shotsLeft <= 0) {
            GunState.update(stack, s -> s.withReloading(true));
            Compat.damage(stack, 1, player, player.getUsedItemHand());
        } else {
            GunState.update(stack, s -> s.withShots(shotsLeft));
        }
    }
}
