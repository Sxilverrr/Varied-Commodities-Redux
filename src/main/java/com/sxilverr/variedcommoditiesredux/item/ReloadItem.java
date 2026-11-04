package com.sxilverr.variedcommoditiesredux.item;

import com.sxilverr.variedcommoditiesredux.Compat;
import com.sxilverr.variedcommoditiesredux.VCConfig;
import com.sxilverr.variedcommoditiesredux.VCItems;
import com.sxilverr.variedcommoditiesredux.VCSounds;
import com.sxilverr.variedcommoditiesredux.entity.VCProjectile;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
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

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.getAbilities().instabuild) {
            if (kind == Kind.MACHINE_GUN) {
                if (!ItemUtil.has(player, ammo())) GunState.update(stack, s -> s.withReloading(true));
            } else if (ItemUtil.has(player, ammo()) && !GunState.get(stack).loaded()) {
                GunState.update(stack, s -> s.withReloading(true));
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return GunState.get(stack).reloading() ? UseAnim.NONE : UseAnim.BOW;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remaining) {
        if (level.isClientSide) return;
        int ticks = usedTicks(stack, entity, remaining);
        if (kind == Kind.MACHINE_GUN) {
            machineGunTick(level, entity, stack, ticks);
            return;
        }
        if (!(entity instanceof Player player) || player.getAbilities().instabuild) return;
        if (GunState.get(stack).reloading() && ItemUtil.has(player, ammo()) && ticks == (kind == Kind.MUSKET ? 60 : 20)) {
            ItemUtil.playSound(player, kind == Kind.MUSKET ? VCSounds.GUN_RELOAD.get() : SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);
            GunState.update(stack, s -> s.withLoaded(true));
        }
    }

    private void machineGunTick(Level level, LivingEntity entity, ItemStack stack, int ticks) {
        if (ticks % 6 != 0) return;
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            int shotsLeft = GunState.get(stack).shots() - ticks / 6;
            if (GunState.get(stack).reloading() && ItemUtil.has(player, ammo())) {
                if (ticks > 0 && ticks <= 24) ItemUtil.playSound(player, VCSounds.GUN_RELOAD.get(), 1.0F, 1.0F);
                return;
            }
            if (shotsLeft <= 0 || !ItemUtil.has(player, ammo())) {
                ItemUtil.playSound(player, VCSounds.GUN_EMPTY.get(), 1.0F, 1.0F);
                return;
            }
            ItemUtil.consume(player, ammo());
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
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int remaining) {
        if (kind == Kind.MACHINE_GUN) {
            machineGunRelease(stack, entity, usedTicks(stack, entity, remaining));
            return;
        }
        if (level.isClientSide) return;
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
            if (!creative) ItemUtil.consume(player, ammo());
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
            //? if >=1.21 {
            /*Arrow arrow = new Arrow(level, entity, new ItemStack(Items.ARROW), stack);
            *///?} else {
            Arrow arrow = new Arrow(level, entity);
            //?}
            arrow.setBaseDamage(VCConfig.heavyCrossbowDamage / 2.0);
            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
            arrow.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, 2.0F, 2.0F);
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 1.0F, level.random.nextFloat() * 0.3F + 0.8F);
            level.addFreshEntity(arrow);
        }
    }

    private void machineGunRelease(ItemStack stack, LivingEntity entity, int ticks) {
        if (!(entity instanceof Player player) || player.getAbilities().instabuild) return;
        int shotsLeft = GunState.get(stack).shots() - ticks / 6;
        if (GunState.get(stack).reloading()) {
            int loaded = ticks > 40 ? 8 : ticks / 5;
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
