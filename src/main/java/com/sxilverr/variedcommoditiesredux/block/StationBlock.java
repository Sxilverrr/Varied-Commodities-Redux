package com.sxilverr.variedcommoditiesredux.block;

import com.sxilverr.variedcommoditiesredux.block.entity.TradingBlockEntity;
import com.sxilverr.variedcommoditiesredux.menu.CarpentryMenu;
import com.sxilverr.variedcommoditiesredux.menu.TradingMenu;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public final class StationBlock {
    public static class Carpentry extends FurnitureBlock {
        private final boolean anvil;

        public Carpentry(boolean anvil, Supplier<? extends BlockEntityType<?>> type, Properties properties) {
            super(null, type, full(), true, properties);
            this.anvil = anvil;
        }

        public boolean isAnvil() {
            return anvil;
        }

        @Override
        protected InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            if (player instanceof ServerPlayer serverPlayer) {
                MenuRegistry.openExtendedMenu(serverPlayer, provider(getName(), pos, (id, inv) -> new CarpentryMenu(id, inv, pos)));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    public static class Trading extends FurnitureBlock {
        public Trading(Supplier<? extends BlockEntityType<?>> type, Properties properties) {
            super(null, type, one(box(0.0, 0.0, 0.0, 1.0, 0.4, 1.0)), true, properties);
        }

        @Override
        protected InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            if (player instanceof ServerPlayer serverPlayer && level.getBlockEntity(pos) instanceof TradingBlockEntity be) {
                if (be.isFull()) {
                    player.displayClientMessage(Component.translatable("trader.variedcommoditiesredux.busy"), true);
                } else {
                    MenuRegistry.openExtendedMenu(serverPlayer, provider(getName(), pos, (id, inv) -> new TradingMenu(id, inv, pos)));
                    be.addTrader(serverPlayer);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    interface MenuFactory {
        AbstractContainerMenu create(int id, Inventory inventory);
    }

    static ExtendedMenuProvider provider(Component title, BlockPos pos, MenuFactory factory) {
        return new ExtendedMenuProvider() {
            @Override
            public void saveExtraData(FriendlyByteBuf buf) {
                buf.writeBlockPos(pos);
            }

            @Override
            public Component getDisplayName() {
                return title;
            }

            @Override
            public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                return factory.create(id, inventory);
            }
        };
    }

    private StationBlock() {
    }
}
