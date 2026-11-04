package com.sxilverr.variedcommoditiesredux.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

//? if >=1.21 {
/*import net.minecraft.core.HolderLookup;
*///?}

public class StorageBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items;

    public StorageBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int size) {
        super(type, pos, state);
        items = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    public static StorageBlockEntity chest(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        return new StorageBlockEntity(type, pos, state, 54);
    }

    public static StorageBlockEntity pedestal(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        return new StorageBlockEntity(type, pos, state, 1);
    }

    public static StorageBlockEntity weaponRack(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        return new StorageBlockEntity(type, pos, state, 3);
    }

    private boolean isChest() {
        return items.size() == 54;
    }

    public int filledSlots() {
        int count = 0;
        for (ItemStack stack : getItems()) if (!stack.isEmpty()) count++;
        return count;
    }

    public void sync() {
        setChanged();
        if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    protected Component getDefaultName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return isChest() ? ChestMenu.sixRows(id, inventory, this) : null;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void startOpen(Player player) {
        if (isChest() && !player.isSpectator() && level != null) {
            level.playSound(null, worldPosition, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (isChest() && !player.isSpectator() && level != null) {
            level.playSound(null, worldPosition, SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return isChest() ? null : ClientboundBlockEntityDataPacket.create(this);
    }

    //? if >=1.21 {
    /*@Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!trySaveLootTable(tag)) ContainerHelper.saveAllItems(tag, items, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        if (!tryLoadLootTable(tag)) ContainerHelper.loadAllItems(tag, items, registries);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return isChest() ? super.getUpdateTag(registries) : saveWithoutMetadata(registries);
    }
    *///?} else {
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!trySaveLootTable(tag)) ContainerHelper.saveAllItems(tag, items);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        if (!tryLoadLootTable(tag)) ContainerHelper.loadAllItems(tag, items);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return isChest() ? super.getUpdateTag() : saveWithoutMetadata();
    }
    //?}
}
