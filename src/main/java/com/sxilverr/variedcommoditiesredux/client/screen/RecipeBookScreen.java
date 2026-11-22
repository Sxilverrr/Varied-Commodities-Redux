package com.sxilverr.variedcommoditiesredux.client.screen;

import com.sxilverr.variedcommoditiesredux.VCRecipes;
import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Optional;

public class RecipeBookScreen extends Screen {
    private static final Identifier BACKGROUND = VariedCommodities.id("textures/gui/recipes.png");
    private static final Identifier SLOT = VariedCommodities.id("textures/gui/slot.png");
    private final List<VCRecipes.Entry> recipes = VCRecipes.synced;
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
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawString(font, title, left() + 5, top() + 5, 0xFF404040, false);
        String number = (page + 1) + "/" + pages();
        graphics.drawString(font, number, left() + (256 - font.width(number)) / 2, top() + 168, 0xFF404040, false);
        ItemStack hovered = ItemStack.EMPTY;
        for (int i = 0; i < 4; i++) {
            int index = i + page * 4;
            if (index >= recipes.size()) break;
            VCRecipes.Entry recipe = recipes.get(index);
            int x = left() + 5 + i / 2 * 126;
            int y = top() + 15 + i % 2 * 76;
            hovered = item(graphics, recipe.result(), x + 98, y + 28, mouseX, mouseY, hovered);
            x += (72 - recipe.width() * 18) / 2;
            y += (72 - recipe.height() * 18) / 2;
            for (int j = 0; j < recipe.inputs().size(); j++) {
                int x2 = x + j % recipe.width() * 18;
                int y2 = y + j / recipe.width() * 18;
                graphics.blit(RenderPipelines.GUI_TEXTURED, SLOT, x2, y2, 0.0F, 0.0F, 18, 18, 256, 256);
                hovered = item(graphics, cycle(recipe.inputs().get(j)), x2 + 1, y2 + 1, mouseX, mouseY, hovered);
            }
        }
        if (!hovered.isEmpty()) graphics.setTooltipForNextFrame(font, hovered, mouseX, mouseY);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, left(), top(), 0.0F, 0.0F, 256, 182, 256, 256);
    }

    private static ItemStack cycle(Optional<Ingredient> ingredient) {
        if (ingredient.isEmpty()) return ItemStack.EMPTY;
        List<Holder<Item>> items = ingredient.get().items().toList();
        if (items.isEmpty()) return ItemStack.EMPTY;
        return new ItemStack(items.get((int) (System.currentTimeMillis() / 1000L % items.size())));
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
