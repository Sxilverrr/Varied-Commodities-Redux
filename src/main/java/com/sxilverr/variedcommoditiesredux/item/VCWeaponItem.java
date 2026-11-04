package com.sxilverr.variedcommoditiesredux.item;

import com.sxilverr.variedcommoditiesredux.Compat;
import com.sxilverr.variedcommoditiesredux.VCSounds;
import com.sxilverr.variedcommoditiesredux.entity.ThrownWeapon;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class VCWeaponItem extends SwordItem {
    private final int durabilityCost;
    private final boolean throwable;

    public VCWeaponItem(Tier tier, int durabilityCost, boolean throwable, Properties properties) {
        //? if >=1.21 {
        /*super(tier, properties.attributes(SwordItem.createAttributes(tier, 3, -2.4F)));
        *///?} else {
        super(tier, 3, -2.4F, properties);
        //?}
        this.durabilityCost = durabilityCost;
        this.throwable = throwable;
    }

    //? if >=1.21 {
    /*@Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Compat.damage(stack, durabilityCost, attacker, InteractionHand.MAIN_HAND);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return throwable ? 72000 : 0;
    }
    *///?} else {
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Compat.damage(stack, durabilityCost, attacker, InteractionHand.MAIN_HAND);
        return true;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return throwable ? 72000 : 0;
    }
    //?}

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return throwable ? UseAnim.SPEAR : super.getUseAnimation(stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!throwable) return super.use(level, player, hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int remaining) {
        if (!throwable || level.isClientSide) return;
        boolean creative = entity instanceof Player player && player.getAbilities().instabuild;
        if (entity instanceof Player player && !creative) {
            Compat.damage(stack, 1, player, entity.getUsedItemHand());
            if (stack.isEmpty()) return;
        }
        ThrownWeapon weapon = new ThrownWeapon(level, entity, stack, getTier().getAttackDamageBonus(), true);
        weapon.pickup = creative ? AbstractArrow.Pickup.CREATIVE_ONLY : AbstractArrow.Pickup.ALLOWED;
        weapon.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, 1.2F, 1.0F);
        if (entity instanceof Player player && !creative) player.setItemInHand(entity.getUsedItemHand(), ItemStack.EMPTY);
        ItemUtil.playSound(entity, VCSounds.SWOOSH.get(), 1.0F, 1.0F);
        level.addFreshEntity(weapon);
    }
}
