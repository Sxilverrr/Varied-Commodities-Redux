package com.sxilverr.variedcommoditiesredux.block;

import com.sxilverr.variedcommoditiesredux.block.entity.BookBlockEntity;
import com.sxilverr.variedcommoditiesredux.network.VCNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class BookBlock extends FurnitureBlock {
    public BookBlock(Supplier<? extends BlockEntityType<?>> type, Properties properties) {
        super(null, type, one(box(0.0, 0.0, 0.0, 1.0, 0.2, 1.0)), true, properties);
    }

    @Override
    protected InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        if (player instanceof ServerPlayer serverPlayer && level.getBlockEntity(pos) instanceof BookBlockEntity be) {
            if (isWand(player.getMainHandItem()) && be.book.is(Items.WRITTEN_BOOK)) {
                be.book = BookPages.writable(BookPages.pages(be.book));
                be.setChanged();
            }
            VCNetwork.openBook(serverPlayer, pos, be.book);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static boolean isWand(ItemStack stack) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id.getNamespace().equals("customnpcs") && (id.getPath().equals("wand") || id.getPath().equals("npcwand"));
    }
}
