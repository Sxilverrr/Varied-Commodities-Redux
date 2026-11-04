package com.sxilverr.variedcommoditiesredux.block.entity;

import com.sxilverr.variedcommoditiesredux.Compat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class IconBlockEntity extends VCBlockEntity {
    public ItemStack icon = ItemStack.EMPTY;

    public IconBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries) {
        super.write(tag, registries);
        tag.put("Icon", Compat.saveStack(icon, registries));
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries) {
        super.read(tag, registries);
        icon = tag.contains("Icon") ? Compat.loadStack(tag.getCompound("Icon"), registries) : ItemStack.EMPTY;
    }
}
