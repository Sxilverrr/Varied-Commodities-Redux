package com.sxilverr.variedcommoditiesredux.block.entity;

import com.sxilverr.variedcommoditiesredux.Compat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BookBlockEntity extends VCBlockEntity {
    public ItemStack book = new ItemStack(Items.WRITABLE_BOOK);

    public BookBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries) {
        super.write(tag, registries);
        tag.put("Book", Compat.saveStack(book, registries));
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries) {
        super.read(tag, registries);
        book = tag.contains("Book") ? Compat.loadStack(tag.getCompound("Book"), registries) : ItemStack.EMPTY;
        if (book.isEmpty()) book = new ItemStack(Items.WRITABLE_BOOK);
    }
}
