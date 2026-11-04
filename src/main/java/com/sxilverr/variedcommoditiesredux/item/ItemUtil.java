package com.sxilverr.variedcommoditiesredux.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class ItemUtil {
    public static ItemStack find(Player player, Item item) {
        if (player.getItemInHand(InteractionHand.OFF_HAND).is(item)) return player.getItemInHand(InteractionHand.OFF_HAND);
        if (player.getItemInHand(InteractionHand.MAIN_HAND).is(item)) return player.getItemInHand(InteractionHand.MAIN_HAND);
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(item)) return stack;
        }
        return ItemStack.EMPTY;
    }

    public static boolean has(Player player, Item item) {
        return !find(player, item).isEmpty();
    }

    public static boolean consume(Player player, Item item) {
        ItemStack stack = find(player, item);
        if (stack.isEmpty()) return false;
        stack.shrink(1);
        return true;
    }

    public static void playSound(Entity entity, SoundEvent sound, float volume, float pitch) {
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.NEUTRAL, volume, pitch);
    }

    private ItemUtil() {
    }
}
