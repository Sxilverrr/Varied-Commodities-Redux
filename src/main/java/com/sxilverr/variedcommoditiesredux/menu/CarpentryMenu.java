package com.sxilverr.variedcommoditiesredux.menu;

import com.sxilverr.variedcommoditiesredux.Compat;
import com.sxilverr.variedcommoditiesredux.VCMenus;
import com.sxilverr.variedcommoditiesredux.VCRecipes;
import com.sxilverr.variedcommoditiesredux.block.StationBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Optional;

//? if >=1.21 {
/*import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
*///?}

public class CarpentryMenu extends AbstractContainerMenu {
    private final TransientCraftingContainer grid = new TransientCraftingContainer(this, 4, 4);
    private final ResultContainer result = new ResultContainer();
    private final Player player;
    private final Level level;
    private final BlockPos pos;
    private NonNullList<ItemStack> remaining = NonNullList.withSize(16, ItemStack.EMPTY);

    public CarpentryMenu(int id, Inventory inventory, BlockPos pos) {
        super(VCMenus.CARPENTRY.get(), id);
        this.player = inventory.player;
        this.level = inventory.player.level();
        this.pos = pos;
        addSlot(new ResultSlot(result, 0, 133, 41));
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                addSlot(new Slot(grid, x + y * 4, 17 + x * 18, 14 + y * 18));
            }
        }
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 98 + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            addSlot(new Slot(inventory, x, 8 + x * 18, 156));
        }
        slotsChanged(grid);
    }

    public boolean isAnvil() {
        return level.getBlockState(pos).getBlock() instanceof StationBlock.Carpentry bench && bench.isAnvil();
    }

    @Override
    public void slotsChanged(Container container) {
        if (container != grid || level.isClientSide) return;
        remaining = NonNullList.withSize(16, ItemStack.EMPTY);
        result.setItem(0, craft());
        broadcastChanges();
    }

    private ItemStack craft() {
        //? if >=1.21 {
        /*CraftingInput.Positioned positioned = CraftingInput.ofPositioned(4, 4, grid.getItems());
        CraftingInput input = positioned.input();
        if (input.isEmpty()) return ItemStack.EMPTY;
        Optional<Recipe<CraftingInput>> recipe = level.getRecipeManager().getRecipeFor(VCRecipes.CARPENTRY.get(), input, level).<Recipe<CraftingInput>>map(RecipeHolder::value);
        if (recipe.isEmpty() && input.width() <= 3 && input.height() <= 3) {
            recipe = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level).map(RecipeHolder::value);
        }
        return recipe.map(r -> {
            NonNullList<ItemStack> leftovers = r.getRemainingItems(input);
            for (int y = 0; y < input.height(); y++) {
                for (int x = 0; x < input.width(); x++) {
                    remaining.set(positioned.left() + x + (positioned.top() + y) * 4, leftovers.get(x + y * input.width()));
                }
            }
            return r.assemble(input, level.registryAccess());
        }).orElse(ItemStack.EMPTY);
        *///?} else {
        if (grid.isEmpty()) return ItemStack.EMPTY;
        Optional<VCRecipes.Carpentry> carpentry = level.getRecipeManager().getRecipeFor(VCRecipes.CARPENTRY.get(), grid, level);
        if (carpentry.isPresent()) {
            remaining = carpentry.get().getRemainingItems(grid);
            return carpentry.get().assemble(grid, level.registryAccess());
        }
        int minX = 4, minY = 4, maxX = -1, maxY = -1;
        for (int i = 0; i < 16; i++) {
            if (grid.getItem(i).isEmpty()) continue;
            minX = Math.min(minX, i % 4);
            maxX = Math.max(maxX, i % 4);
            minY = Math.min(minY, i / 4);
            maxY = Math.max(maxY, i / 4);
        }
        if (maxX < 0 || maxX - minX > 2 || maxY - minY > 2) return ItemStack.EMPTY;
        NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                int gx = minX + x;
                int gy = minY + y;
                if (gx < 4 && gy < 4) items.set(x + y * 3, grid.getItem(gx + gy * 4));
            }
        }
        TransientCraftingContainer container = new TransientCraftingContainer(this, 3, 3, items);
        int offsetX = minX;
        int offsetY = minY;
        return level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, container, level).map(recipe -> {
            NonNullList<ItemStack> leftovers = recipe.getRemainingItems(container);
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    if (offsetX + x < 4 && offsetY + y < 4) remaining.set(offsetX + x + (offsetY + y) * 4, leftovers.get(x + y * 3));
                }
            }
            return recipe.assemble(container, level.registryAccess());
        }).orElse(ItemStack.EMPTY);
        //?}
    }

    private void consume() {
        NonNullList<ItemStack> leftovers = remaining;
        for (int i = 0; i < 16; i++) {
            ItemStack stack = grid.getItem(i);
            ItemStack leftover = leftovers.get(i);
            if (!stack.isEmpty()) {
                grid.removeItem(i, 1);
                stack = grid.getItem(i);
            }
            if (leftover.isEmpty()) continue;
            if (stack.isEmpty()) {
                grid.setItem(i, leftover.copy());
            } else if (Compat.sameItemSameData(stack, leftover)) {
                leftover.grow(stack.getCount());
                grid.setItem(i, leftover.copy());
            } else if (!player.getInventory().add(leftover.copy())) {
                player.drop(leftover.copy(), false);
            }
        }
        slotsChanged(grid);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!level.isClientSide) clearContainer(player, grid);
    }

    @Override
    public boolean stillValid(Player player) {
        return level.getBlockState(pos).getBlock() instanceof StationBlock.Carpentry
                && player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        if (index == 0) {
            if (!moveItemStackTo(stack, 17, 53, true)) return ItemStack.EMPTY;
            slot.onQuickCraft(stack, copy);
        } else if (index >= 17 && index < 44) {
            if (!moveItemStackTo(stack, 44, 53, false)) return ItemStack.EMPTY;
        } else if (index >= 44 && index < 53) {
            if (!moveItemStackTo(stack, 17, 44, false)) return ItemStack.EMPTY;
        } else if (!moveItemStackTo(stack, 17, 53, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();
        if (stack.getCount() == copy.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, stack);
        return copy;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != result && super.canTakeItemForPickAll(stack, slot);
    }

    private class ResultSlot extends Slot {
        ResultSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            stack.onCraftedBy(player.level(), player, stack.getCount());
            consume();
        }
    }
}
