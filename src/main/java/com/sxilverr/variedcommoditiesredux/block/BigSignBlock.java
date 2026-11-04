package com.sxilverr.variedcommoditiesredux.block;

import com.sxilverr.variedcommoditiesredux.VCConfig;
import com.sxilverr.variedcommoditiesredux.block.entity.BigSignBlockEntity;
import com.sxilverr.variedcommoditiesredux.network.VCNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class BigSignBlock extends FurnitureBlock {
    public static final VoxelShape[] SIGN = {
            box(0.0, 0.0, 0.87, 1.0, 1.0, 1.0),
            box(0.0, 0.0, 0.0, 0.13, 1.0, 1.0),
            box(0.0, 0.0, 0.0, 1.0, 1.0, 0.13),
            box(0.87, 0.0, 0.0, 1.0, 1.0, 1.0)
    };
    public static final VoxelShape[] TOMBSTONE = {
            box(0.0, 0.0, 0.3, 1.0, 1.0, 0.7),
            box(0.3, 0.0, 0.0, 0.7, 1.0, 1.0)
    };

    private final int style;

    public BigSignBlock(int style, Supplier<? extends BlockEntityType<?>> type, Properties properties) {
        super(null, type, style < 0 ? SIGN : TOMBSTONE, true, properties);
        this.style = style;
    }

    public int style() {
        return style;
    }

    public boolean hasText() {
        return style < 2;
    }

    public int lineCount() {
        return style < 0 ? 13 : 11;
    }

    public int lineWidth() {
        return style < 0 ? 112 : 94;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (VCConfig.signEditorOnPlace && hasText() && placer instanceof ServerPlayer player) {
            VCNetwork.openSignEditor(player, pos);
        }
    }

    @Override
    protected InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!hasText() || hand != InteractionHand.MAIN_HAND || !(level.getBlockEntity(pos) instanceof BigSignBlockEntity be)) return InteractionResult.PASS;
        ItemStack held = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (be.waxed) {
                level.playSound(null, pos, SoundEvents.WAXED_SIGN_INTERACT_FAIL, SoundSource.BLOCKS);
            } else if (held.is(Items.HONEYCOMB)) {
                be.waxed = true;
                be.sync();
                if (!player.getAbilities().instabuild) held.shrink(1);
                level.playSound(null, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.levelEvent(null, 3003, pos, 0);
            } else if (player instanceof ServerPlayer serverPlayer) {
                VCNetwork.openSignEditor(serverPlayer, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
