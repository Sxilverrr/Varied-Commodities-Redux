package com.sxilverr.variedcommoditiesredux;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public final class Compat {
    public static void saveStack(ValueOutput output, String key, ItemStack stack) {
        if (!stack.isEmpty()) output.store(key, ItemStack.CODEC, stack);
    }

    public static ItemStack loadStack(ValueInput input, String key) {
        return input.read(key, ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }

    public static void damage(ItemStack stack, int amount, LivingEntity entity, InteractionHand hand) {
        stack.hurtAndBreak(amount, entity, hand);
    }

    public static SoundEvent explodeSound() {
        return SoundEvents.GENERIC_EXPLODE.value();
    }

    public static int dyeRgb(DyeColor color) {
        return color.getTextureDiffuseColor() & 0xFFFFFF;
    }

    private Compat() {
    }
}
