package com.sxilverr.variedcommoditiesredux.block.entity;

import com.sxilverr.variedcommoditiesredux.menu.TradingMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TradingBlockEntity extends VCBlockEntity {
    public ServerPlayer trader1;
    public ServerPlayer trader2;

    public TradingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    private ServerPlayer check(ServerPlayer trader) {
        return trader != null && trader.containerMenu instanceof TradingMenu ? trader : null;
    }

    private static boolean done(ServerPlayer trader) {
        return trader != null && ((TradingMenu) trader.containerMenu).state() >= TradingMenu.COMPLETE;
    }

    public boolean isFull() {
        trader1 = check(trader1);
        trader2 = check(trader2);
        if (done(trader1) || done(trader2)) return true;
        return trader1 != null && trader2 != null;
    }

    public void addTrader(ServerPlayer player) {
        if (isFull()) return;
        if (trader1 != player && trader2 != player) {
            if (trader1 == null) trader1 = player;
            else if (trader2 == null) trader2 = player;
        }
        if (trader1 != null && trader2 != null) {
            ((TradingMenu) trader2.containerMenu).setPartner(trader1);
            ((TradingMenu) trader1.containerMenu).setPartner(trader2);
        }
    }

    public ServerPlayer other(ServerPlayer player) {
        return player == trader1 ? trader2 : trader1;
    }

    public void removeTrader(ServerPlayer player) {
        if (trader1 == player) trader1 = null;
        else if (trader2 == player) trader2 = null;
    }
}
