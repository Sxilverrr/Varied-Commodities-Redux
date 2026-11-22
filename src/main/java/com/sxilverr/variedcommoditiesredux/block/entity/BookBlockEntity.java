package com.sxilverr.variedcommoditiesredux.block.entity;

import com.sxilverr.variedcommoditiesredux.Compat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class BookBlockEntity extends VCBlockEntity {
    public ItemStack book = new ItemStack(Items.WRITABLE_BOOK);

    public BookBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        Compat.saveStack(output, "Book", book);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        book = Compat.loadStack(input, "Book");
        if (book.isEmpty()) book = new ItemStack(Items.WRITABLE_BOOK);
    }
}
