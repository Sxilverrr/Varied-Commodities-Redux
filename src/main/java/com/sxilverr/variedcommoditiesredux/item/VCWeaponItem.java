package com.sxilverr.variedcommoditiesredux.item;

import com.sxilverr.variedcommoditiesredux.Compat;
import com.sxilverr.variedcommoditiesredux.VCConfig;
import com.sxilverr.variedcommoditiesredux.VCSounds;
import com.sxilverr.variedcommoditiesredux.entity.ThrownWeapon;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.level.Level;

public class VCWeaponItem extends Item {
    private final boolean throwable;

    public VCWeaponItem(ToolMaterial material, int durabilityCost, boolean throwable, Properties properties) {
        super(properties.sword(material, 3.0F, -2.4F).component(DataComponents.WEAPON, new Weapon(durabilityCost)));
        this.throwable = throwable;
    }

    public static boolean isSword(ItemStack stack) {
        return stack.is(ItemTags.SWORDS) || stack.getItem() instanceof VCWeaponItem;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return throwable ? 72000 : 0;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return throwable ? ItemUseAnimation.TRIDENT : super.getUseAnimation(stack);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!throwable) return super.use(level, player, hand);
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int remaining) {
        if (!throwable || level.isClientSide()) return false;
        boolean creative = entity instanceof Player player && player.getAbilities().instabuild;
        if (entity instanceof Player player && !creative) {
            Compat.damage(stack, 1, player, entity.getUsedItemHand());
            if (stack.isEmpty()) return false;
        }
        ThrownWeapon weapon = new ThrownWeapon(level, entity, stack, VCConfig.thrownKunaiDamage, true);
        weapon.pickup = creative ? AbstractArrow.Pickup.CREATIVE_ONLY : AbstractArrow.Pickup.ALLOWED;
        weapon.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, 1.2F, 1.0F);
        if (entity instanceof Player player && !creative) player.setItemInHand(entity.getUsedItemHand(), ItemStack.EMPTY);
        ItemUtil.playSound(entity, VCSounds.SWOOSH.get(), 1.0F, 1.0F);
        level.addFreshEntity(weapon);
        return true;
    }
}
