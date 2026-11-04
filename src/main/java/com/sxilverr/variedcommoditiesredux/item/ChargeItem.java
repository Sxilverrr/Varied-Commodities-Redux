package com.sxilverr.variedcommoditiesredux.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class ChargeItem extends Item {
    public ChargeItem(Properties properties) {
        super(properties);
    }

    public static int usedTicks(ItemStack stack, LivingEntity entity, int remaining) {
        return 72000 - remaining;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    //? if >=1.21 {
    /*@Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }
    *///?} else {
    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
    //?}
}
