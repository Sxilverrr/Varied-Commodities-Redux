package com.sxilverr.variedcommoditiesredux.menu;

import com.sxilverr.variedcommoditiesredux.VCMenus;
import com.sxilverr.variedcommoditiesredux.block.entity.TradingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TradingMenu extends AbstractContainerMenu {
    public static final int WAITING = 0;
    public static final int PARTNER_ACCEPTED = 1;
    public static final int ACCEPTED = 2;
    public static final int COMPLETE = 3;
    public static final int CANCELLED = 4;
    private static final int OFFER_START = 36;
    private static final int PARTNER_START = 45;

    private final SimpleContainer offer = new SimpleContainer(9);
    private final SimpleContainer partnerOffer = new SimpleContainer(9);
    private final DataSlot state = DataSlot.standalone();
    private final DataSlot partnerLow = DataSlot.standalone();
    private final DataSlot partnerHigh = DataSlot.standalone();
    private final Player player;
    private final BlockPos pos;

    public TradingMenu(int id, Inventory inventory, BlockPos pos) {
        super(VCMenus.TRADING.get(), id);
        this.player = inventory.player;
        this.pos = pos;
        setPartnerId(-1);
        addDataSlot(state);
        addDataSlot(partnerLow);
        addDataSlot(partnerHigh);
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inventory, col + row * 9, 8 + col * 18, 150 + row * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            addSlot(new OfferSlot(i, 8 + i * 18, 125));
        }
        for (int i = 0; i < 9; i++) {
            addSlot(new PartnerSlot(i, 8 + i % 5 * 18, 8 + i / 5 * 18));
        }
    }

    public int state() {
        return state.get();
    }

    public Player partner(Level level) {
        int id = partnerId();
        return id == -1 ? null : level.getEntity(id) instanceof Player partner ? partner : null;
    }

    public boolean hasPartner() {
        return partnerId() != -1;
    }

    private int partnerId() {
        return (partnerHigh.get() & 0xFFFF) << 16 | partnerLow.get() & 0xFFFF;
    }

    private void setPartnerId(int id) {
        partnerLow.set(id & 0xFFFF);
        partnerHigh.set(id >>> 16 & 0xFFFF);
    }

    public void setPartner(Player partner) {
        setPartnerId(partner == null ? -1 : partner.getId());
        clearPartnerOffer();
    }

    private void clearPartnerOffer() {
        for (int i = 0; i < 9; i++) partnerOffer.setItem(i, ItemStack.EMPTY);
    }

    private void showPartnerOffer(SimpleContainer items) {
        for (int i = 0; i < 9; i++) partnerOffer.setItem(i, items.getItem(i).copy());
    }

    private TradingBlockEntity blockEntity() {
        return player.level().getBlockEntity(pos) instanceof TradingBlockEntity be ? be : null;
    }

    private void setState(int value) {
        state.set(value);
        if (value >= COMPLETE) clearPartnerOffer();
    }

    private TradingMenu partnerMenu() {
        if (!(player instanceof ServerPlayer serverPlayer)) return null;
        TradingBlockEntity be = blockEntity();
        if (be == null || !be.isFull()) return null;
        ServerPlayer other = be.other(serverPlayer);
        return other != null && other.containerMenu instanceof TradingMenu menu ? menu : null;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        TradingMenu other = partnerMenu();
        if (other == null) return false;
        if (state() == WAITING) {
            setState(ACCEPTED);
            other.setState(PARTNER_ACCEPTED);
        } else if (state() == PARTNER_ACCEPTED || state() == ACCEPTED) {
            for (int i = 0; i < 9; i++) {
                ItemStack mine = offer.getItem(i);
                offer.setItem(i, other.offer.getItem(i));
                other.offer.setItem(i, mine);
            }
            setState(COMPLETE);
            other.setState(COMPLETE);
        }
        return true;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        TradingBlockEntity be = blockEntity();
        if (be != null) {
            ServerPlayer other = be.other(serverPlayer);
            be.removeTrader(serverPlayer);
            if (state() != COMPLETE && other != null && other.containerMenu instanceof TradingMenu otherMenu) {
                otherMenu.setState(CANCELLED);
            }
        }
        clearContainer(player, offer);
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity() != null && player.isAlive() && player.blockPosition().distSqr(pos) < 16.0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (index >= PARTNER_START || !slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        if (index < OFFER_START) {
            if (!slots.get(OFFER_START).mayPlace(stack) || !moveItemStackTo(stack, OFFER_START, PARTNER_START, false)) return ItemStack.EMPTY;
        } else if (!moveItemStackTo(stack, 0, OFFER_START, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();
        return copy;
    }

    private class OfferSlot extends Slot {
        OfferSlot(int index, int x, int y) {
            super(offer, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return hasPartner() && state() < COMPLETE;
        }

        @Override
        public void setChanged() {
            super.setChanged();
            TradingMenu other = partnerMenu();
            if (other == null) return;
            if (state() == PARTNER_ACCEPTED || state() == ACCEPTED) {
                setState(WAITING);
                other.setState(WAITING);
            }
            other.showPartnerOffer(offer);
        }
    }

    private class PartnerSlot extends Slot {
        PartnerSlot(int index, int x, int y) {
            super(partnerOffer, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(Player player) {
            return false;
        }
    }
}
