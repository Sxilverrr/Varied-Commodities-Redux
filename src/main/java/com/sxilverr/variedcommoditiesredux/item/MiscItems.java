package com.sxilverr.variedcommoditiesredux.item;

import com.sxilverr.variedcommoditiesredux.VCConfig;
import com.sxilverr.variedcommoditiesredux.client.VCClientHooks;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public final class MiscItems {
    public static class Music extends ChargeItem {
        private final boolean bowed;

        public Music(boolean bowed, Properties properties) {
            super(properties.stacksTo(1));
            this.bowed = bowed;
        }

        @Override
        public InteractionResult use(Level level, Player player, InteractionHand hand) {
            if (bowed) player.startUsingItem(hand);
            ItemStack stack = player.getItemInHand(hand);
            if (level instanceof ServerLevel server) {
                int note = level.getRandom().nextInt(24);
                float pitch = (float) Math.pow(2.0, (note - 12) / 12.0);
                ItemUtil.playSound(player, SoundEvents.NOTE_BLOCK_HARP.value(), (float) VCConfig.instrumentVolume, pitch);
                server.sendParticles(ParticleTypes.NOTE, player.getX(), player.getY() + 1.2, player.getZ(), 0, note / 24.0, 0.0, 0.0, 1.0);
            }
            return bowed ? InteractionResult.CONSUME : InteractionResult.SUCCESS;
        }

        @Override
        public ItemUseAnimation getUseAnimation(ItemStack stack) {
            return bowed ? ItemUseAnimation.BOW : ItemUseAnimation.NONE;
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
        public InteractionResult use(Level level, Player player, InteractionHand hand) {
            if (level.isClientSide()) VCClientHooks.openRecipeBook();
            return InteractionResult.SUCCESS;
        }
    }

    public static class Shield extends ShieldItem {
        public Shield(VCMaterial material, Properties properties) {
            super(properties.durability(material.maxUses() * 5)
                    .repairable(ItemTags.WOODEN_TOOL_MATERIALS)
                    .equippableUnswappable(EquipmentSlot.OFFHAND)
                    .delayedComponent(DataComponents.BLOCKS_ATTACKS, context -> new BlocksAttacks(0.25F, 1.0F,
                            List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                            new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                            Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                            Optional.of(SoundEvents.SHIELD_BLOCK),
                            Optional.of(SoundEvents.SHIELD_BREAK)))
                    .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK));
        }
    }

    private MiscItems() {
    }
}
