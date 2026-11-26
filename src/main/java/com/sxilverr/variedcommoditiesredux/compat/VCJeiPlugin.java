package com.sxilverr.variedcommoditiesredux.compat;

import com.sxilverr.variedcommoditiesredux.VCBlocks;
import com.sxilverr.variedcommoditiesredux.VCMenus;
import com.sxilverr.variedcommoditiesredux.VCRecipes;
import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import com.sxilverr.variedcommoditiesredux.menu.CarpentryMenu;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@JeiPlugin
public class VCJeiPlugin implements IModPlugin {
    private static final IRecipeType<VCRecipes.Entry> CARPENTRY = IRecipeType.create(VariedCommodities.MODID, "carpentry", VCRecipes.Entry.class);
    private static List<VCRecipes.Entry> shown = List.of();

    @Override
    public Identifier getPluginUid() {
        return VariedCommodities.id("jei");
    }

    private static List<VCRecipes.Entry> carpentry() {
        return VCRecipes.synced.stream().filter(VCRecipes.Entry::carpentry).toList();
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new Category(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        shown = carpentry();
        registration.addRecipes(CARPENTRY, shown);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        VCRecipes.onSync = () -> {
            IRecipeManager manager = runtime.getRecipeManager();
            if (!shown.isEmpty()) manager.hideRecipes(CARPENTRY, shown);
            shown = carpentry();
            if (!shown.isEmpty()) manager.addRecipes(CARPENTRY, shown);
        };
    }

    @Override
    public void onRuntimeUnavailable() {
        VCRecipes.onSync = () -> {
        };
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(CARPENTRY, VCBlocks.CARPENTRY_BENCH.get(), VCBlocks.BLACKSMITH_ANVIL.get());
        registration.addCraftingStation(RecipeTypes.CRAFTING, VCBlocks.CARPENTRY_BENCH.get(), VCBlocks.BLACKSMITH_ANVIL.get());
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new BenchTransfer<>(CARPENTRY, VCJeiPlugin::carpentryCells));
        registration.addRecipeTransferHandler(new BenchTransfer<>(RecipeTypes.CRAFTING, recipe -> List.of(0, 1, 2, 4, 5, 6, 8, 9, 10)));
    }

    private static List<Integer> carpentryCells(VCRecipes.Entry recipe) {
        List<Integer> cells = new ArrayList<>();
        List<Optional<Ingredient>> inputs = recipe.inputs();
        for (int i = 0; i < inputs.size(); i++) {
            if (inputs.get(i).isPresent()) cells.add(i / recipe.width() * 4 + i % recipe.width());
        }
        return cells;
    }

    private record BenchTransfer<R>(IRecipeType<R> type, Function<R, List<Integer>> cells) implements IRecipeTransferInfo<CarpentryMenu, R> {
        @Override
        public Class<CarpentryMenu> getContainerClass() {
            return CarpentryMenu.class;
        }

        @Override
        public Optional<MenuType<CarpentryMenu>> getMenuType() {
            return Optional.of(VCMenus.CARPENTRY.get());
        }

        @Override
        public IRecipeType<R> getRecipeType() {
            return type;
        }

        @Override
        public boolean canHandle(CarpentryMenu menu, R recipe) {
            return true;
        }

        @Override
        public List<Slot> getRecipeSlots(CarpentryMenu menu, R recipe) {
            List<Integer> order = new ArrayList<>(cells.apply(recipe));
            for (int cell = 0; cell < 16; cell++) {
                if (!order.contains(cell)) order.add(cell);
            }
            return order.stream().map(cell -> menu.getSlot(1 + cell)).toList();
        }

        @Override
        public List<Slot> getInventorySlots(CarpentryMenu menu, R recipe) {
            return menu.slots.subList(17, 53);
        }
    }

    private static class Category implements IRecipeCategory<VCRecipes.Entry> {
        private final IDrawable background;
        private final IDrawable icon;

        Category(IGuiHelper gui) {
            background = gui.createDrawable(VariedCommodities.id("textures/gui/carpentry.png"), 12, 10, 146, 78);
            icon = gui.createDrawableItemStack(new ItemStack(VCBlocks.CARPENTRY_BENCH.get()));
        }

        @Override
        public IRecipeType<VCRecipes.Entry> getRecipeType() {
            return CARPENTRY;
        }

        @Override
        public Component getTitle() {
            return VCBlocks.CARPENTRY_BENCH.get().getName();
        }

        @Override
        public int getWidth() {
            return 146;
        }

        @Override
        public int getHeight() {
            return 78;
        }

        @Override
        public IDrawable getIcon() {
            return icon;
        }

        @Override
        public void draw(VCRecipes.Entry recipe, IRecipeSlotsView slots, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
            background.draw(graphics);
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, VCRecipes.Entry recipe, IFocusGroup focuses) {
            List<Optional<Ingredient>> inputs = recipe.inputs();
            for (int i = 0; i < inputs.size(); i++) {
                Optional<Ingredient> input = inputs.get(i);
                if (input.isEmpty()) continue;
                builder.addSlot(RecipeIngredientRole.INPUT, 5 + i % recipe.width() * 18, 4 + i / recipe.width() * 18).add(input.get());
            }
            builder.addSlot(RecipeIngredientRole.OUTPUT, 121, 31).add(recipe.result());
        }
    }
}
