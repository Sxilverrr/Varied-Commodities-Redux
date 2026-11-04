package com.sxilverr.variedcommoditiesredux;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

//? if >=1.21 {
/*import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.component.DyedItemColor;
*///?} else {
import net.minecraft.world.item.DyeableLeatherItem;
//?}

public final class Compat {
    public static CompoundTag saveStack(ItemStack stack, HolderLookup.Provider registries) {
        //? if >=1.21 {
        /*return stack.isEmpty() ? new CompoundTag() : (CompoundTag) stack.save(registries);
        *///?} else {
        return stack.save(new CompoundTag());
        //?}
    }

    public static ItemStack loadStack(CompoundTag tag, HolderLookup.Provider registries) {
        //? if >=1.21 {
        /*return ItemStack.parseOptional(registries, tag);
        *///?} else {
        return ItemStack.of(tag);
        //?}
    }

    public static void damage(ItemStack stack, int amount, LivingEntity entity, InteractionHand hand) {
        //? if >=1.21 {
        /*stack.hurtAndBreak(amount, entity, LivingEntity.getSlotForHand(hand));
        *///?} else {
        stack.hurtAndBreak(amount, entity, e -> e.broadcastBreakEvent(hand));
        //?}
    }

    public static ItemStack withItem(ItemStack stack, Item item) {
        //? if >=1.21 {
        /*return new ItemStack(item.builtInRegistryHolder(), stack.getCount(), stack.getComponentsPatch());
        *///?} else {
        ItemStack copy = new ItemStack(item, stack.getCount());
        if (stack.getTag() != null) copy.setTag(stack.getTag().copy());
        return copy;
        //?}
    }

    public static boolean sameItemSameData(ItemStack a, ItemStack b) {
        //? if >=1.21 {
        /*return ItemStack.isSameItemSameComponents(a, b);
        *///?} else {
        return ItemStack.isSameItemSameTags(a, b);
        //?}
    }

    public static SoundEvent explodeSound() {
        //? if >=1.21 {
        /*return SoundEvents.GENERIC_EXPLODE.value();
        *///?} else {
        return SoundEvents.GENERIC_EXPLODE;
        //?}
    }

    public static int sheepRgb(DyeColor color) {
        //? if >=1.21 {
        /*return Sheep.getColor(color) & 0xFFFFFF;
        *///?} else {
        float[] c = Sheep.getColorArray(color);
        return ((int) (c[0] * 255.0F) << 16) | ((int) (c[1] * 255.0F) << 8) | (int) (c[2] * 255.0F);
        //?}
    }

    public static int armorColor(ItemStack stack) {
        //? if >=1.21 {
        /*return DyedItemColor.getOrDefault(stack, 10511680) & 0xFFFFFF;
        *///?} else {
        return stack.getItem() instanceof DyeableLeatherItem dyeable ? dyeable.getColor(stack) : 10511680;
        //?}
    }

    public static int dyeRgb(DyeColor color) {
        //? if >=1.21 {
        /*return color.getTextureDiffuseColor() & 0xFFFFFF;
        *///?} else {
        float[] c = color.getTextureDiffuseColors();
        return ((int) (c[0] * 255.0F) << 16) | ((int) (c[1] * 255.0F) << 8) | (int) (c[2] * 255.0F);
        //?}
    }

    //? if >=1.21 {
    /*public static ItemInteractionResult toItemResult(InteractionResult result) {
        return switch (result) {
            case SUCCESS, SUCCESS_NO_ITEM_USED -> ItemInteractionResult.SUCCESS;
            case CONSUME -> ItemInteractionResult.CONSUME;
            case CONSUME_PARTIAL -> ItemInteractionResult.CONSUME_PARTIAL;
            case FAIL -> ItemInteractionResult.FAIL;
            default -> ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        };
    }
    *///?}

    private Compat() {
    }
}
