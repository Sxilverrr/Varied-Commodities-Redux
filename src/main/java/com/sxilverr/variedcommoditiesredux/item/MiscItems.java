package com.sxilverr.variedcommoditiesredux.item;

import com.sxilverr.variedcommoditiesredux.client.VCClientHooks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public final class MiscItems {
    public static class Music extends ChargeItem {
        private final boolean bowed;

        public Music(boolean bowed, Properties properties) {
            super(properties.stacksTo(1));
            this.bowed = bowed;
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
            if (bowed) player.startUsingItem(hand);
            ItemStack stack = player.getItemInHand(hand);
            if (level instanceof ServerLevel server) {
                int note = level.random.nextInt(24);
                float pitch = (float) Math.pow(2.0, (note - 12) / 12.0);
                ItemUtil.playSound(player, SoundEvents.NOTE_BLOCK_HARP.value(), 3.0F, pitch);
                server.sendParticles(ParticleTypes.NOTE, player.getX(), player.getY() + 1.2, player.getZ(), 0, note / 24.0, 0.0, 0.0, 1.0);
            }
            return bowed ? InteractionResultHolder.consume(stack) : InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        @Override
        public UseAnim getUseAnimation(ItemStack stack) {
            return bowed ? UseAnim.BOW : UseAnim.NONE;
        }
    }

    public static class Colored extends Item {
        private final DyeColor color;

        public Colored(DyeColor color, Properties properties) {
            super(properties);
            this.color = color;
        }

        public DyeColor color() {
            return color;
        }
    }

    public static class Wand extends Item {
        public Wand(Properties properties) {
            super(properties.stacksTo(1));
        }

        @Override
        public boolean isFoil(ItemStack stack) {
            return true;
        }
    }

    public static class RecipeBook extends Item {
        public RecipeBook(Properties properties) {
            super(properties);
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
            if (level.isClientSide) VCClientHooks.openRecipeBook();
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
        }
    }

    public static class Shield extends ShieldItem {
        public Shield(VCMaterial material, Properties properties) {
            super(properties.durability(material.maxUses() * 5));
        }
    }

    private MiscItems() {
    }
}
