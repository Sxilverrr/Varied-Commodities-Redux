package com.sxilverr.variedcommoditiesredux.client.screen;

import com.sxilverr.variedcommoditiesredux.VCRecipes;
import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//? if >=1.21 {
/*import net.minecraft.world.item.crafting.RecipeHolder;
*///?}

public class RecipeBookScreen extends Screen {
    private static final ResourceLocation BACKGROUND = VariedCommodities.id("textures/gui/recipes.png");
    private static final ResourceLocation SLOT = VariedCommodities.id("textures/gui/slot.png");
    private final List<Entry> recipes = load();
    private int page;
    private Button next;
    private Button previous;

    public RecipeBookScreen() {
        super(Component.translatable("gui.variedcommoditiesredux.recipe_list"));
    }

    private int left() {
        return (width - 256) / 2;
    }

    private int top() {
        return (height - 182) / 2;
    }

    private int pages() {
        return Mth.ceil(recipes.size() / 4.0F);
    }

    @Override
    protected void init() {
        next = addRenderableWidget(new PageButton(left() + 150, top() + 164, true, b -> turn(1), true));
        previous = addRenderableWidget(new PageButton(left() + 80, top() + 164, false, b -> turn(-1), true));
        updateButtons();
    }

    private void turn(int direction) {
        page = Mth.clamp(page + direction, 0, Math.max(0, pages() - 1));
        updateButtons();
    }

    private void updateButtons() {
        previous.visible = page > 0;
        next.visible = page + 1 < pages();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        //? if <1.21 {
        renderBackground(graphics);
        graphics.blit(BACKGROUND, left(), top(), 0, 0, 256, 182);
        //?}
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawString(font, title, left() + 5, top() + 5, 0x404040, false);
        String number = (page + 1) + "/" + pages();
        graphics.drawString(font, number, left() + (256 - font.width(number)) / 2, top() + 168, 0x404040, false);
        ItemStack hovered = ItemStack.EMPTY;
        for (int i = 0; i < 4; i++) {
            int index = i + page * 4;
            if (index >= recipes.size()) break;
            Entry recipe = recipes.get(index);
            int x = left() + 5 + i / 2 * 126;
            int y = top() + 15 + i % 2 * 76;
            hovered = item(graphics, recipe.result(), x + 98, y + 28, mouseX, mouseY, hovered);
            x += (72 - recipe.width() * 18) / 2;
            y += (72 - recipe.height() * 18) / 2;
            for (int j = 0; j < recipe.inputs().size(); j++) {
                int x2 = x + j % recipe.width() * 18;
                int y2 = y + j / recipe.width() * 18;
                graphics.blit(SLOT, x2, y2, 0, 0, 18, 18);
                hovered = item(graphics, cycle(recipe.inputs().get(j)), x2 + 1, y2 + 1, mouseX, mouseY, hovered);
            }
        }
        if (!hovered.isEmpty()) graphics.renderTooltip(font, hovered, mouseX, mouseY);
    }

    //? if >=1.21 {
    /*@Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(BACKGROUND, left(), top(), 0, 0, 256, 182);
    }
    *///?}

    private record Entry(int width, int height, List<Ingredient> inputs, ItemStack result) {
    }

    private static List<Entry> load() {
        Minecraft minecraft = Minecraft.getInstance();
        List<Entry> list = new ArrayList<>();
        if (minecraft.level == null) return list;
        RegistryAccess access = minecraft.level.registryAccess();
        //? if >=1.21 {
        /*for (RecipeHolder<?> holder : minecraft.level.getRecipeManager().getRecipes()) {
            if (!holder.id().getNamespace().equals(VariedCommodities.MODID)) continue;
            Recipe<?> recipe = holder.value();
        *///?} else {
        for (Recipe<?> recipe : minecraft.level.getRecipeManager().getRecipes()) {
            if (!recipe.getId().getNamespace().equals(VariedCommodities.MODID)) continue;
        //?}
            if (recipe instanceof ShapedRecipe shaped) {
                list.add(new Entry(shaped.getWidth(), shaped.getHeight(), shaped.getIngredients(), shaped.getResultItem(access)));
            } else if (recipe instanceof VCRecipes.Carpentry carpentry) {
                list.add(new Entry(carpentry.width(), carpentry.height(), carpentry.getIngredients(), carpentry.result()));
            }
        }
        list.sort(Comparator.comparingInt(entry -> Item.getId(entry.result().getItem())));
        return list;
    }

    private static ItemStack cycle(Ingredient ingredient) {
        ItemStack[] items = ingredient.getItems();
        if (items.length == 0) return ItemStack.EMPTY;
        return items[(int) (System.currentTimeMillis() / 1000L % items.length)];
    }

    private ItemStack item(GuiGraphics graphics, ItemStack stack, int x, int y, int mouseX, int mouseY, ItemStack hovered) {
        if (stack.isEmpty()) return hovered;
        graphics.renderItem(stack, x, y);
        graphics.renderItemDecorations(font, stack, x, y);
        return mouseX >= x - 1 && mouseX < x + 17 && mouseY >= y - 1 && mouseY < y + 17 ? stack : hovered;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
