package com.sxilverr.variedcommoditiesredux;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

//? if >=1.21 {
/*import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingInput;
*///?} else {
import com.google.gson.JsonElement;
import java.util.ArrayList;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.ShapedRecipe;
//?}

public final class VCRecipes {
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(VariedCommodities.MODID, Registries.RECIPE_TYPE);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(VariedCommodities.MODID, Registries.RECIPE_SERIALIZER);
    public static final RegistrySupplier<RecipeType<Carpentry>> CARPENTRY = TYPES.register("carpentry", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return VariedCommodities.MODID + ":carpentry";
        }
    });
    public static final RegistrySupplier<RecipeSerializer<Carpentry>> CARPENTRY_SERIALIZER = SERIALIZERS.register("carpentry", Carpentry.Serializer::new);
    public static final RegistrySupplier<RecipeSerializer<FlipDagger>> FLIP_DAGGER = SERIALIZERS.register("flip_dagger", () -> new SimpleCraftingRecipeSerializer<>(FlipDagger::new));

    private static Map<Item, Item> flips;

    //? if >=1.21 {
    /*public static class Carpentry implements Recipe<CraftingInput> {
    *///?} else {
    public static class Carpentry implements Recipe<CraftingContainer> {
        private final ResourceLocation id;
    //?}
        private final List<String> pattern;
        private final Map<String, Ingredient> key;
        private final ItemStack result;
        private final int width;
        private final NonNullList<Ingredient> inputs;

        //? if >=1.21 {
        /*public Carpentry(List<String> pattern, Map<String, Ingredient> key, ItemStack result) {
        *///?} else {
        public Carpentry(ResourceLocation id, List<String> pattern, Map<String, Ingredient> key, ItemStack result) {
            this.id = id;
        //?}
            this.pattern = List.copyOf(pattern);
            this.key = Map.copyOf(key);
            this.result = result;
            this.width = pattern.stream().mapToInt(String::length).max().orElse(0);
            if (pattern.size() > 4 || width > 4) throw new IllegalArgumentException("Carpentry patterns can be at most 4x4");
            inputs = NonNullList.withSize(width * pattern.size(), Ingredient.EMPTY);
            for (int y = 0; y < pattern.size(); y++) {
                String row = pattern.get(y);
                for (int x = 0; x < row.length(); x++) {
                    char c = row.charAt(x);
                    if (c == ' ') continue;
                    Ingredient ingredient = key.get(String.valueOf(c));
                    if (ingredient == null) throw new IllegalArgumentException("Pattern references undefined symbol '" + c + "'");
                    inputs.set(x + y * width, ingredient);
                }
            }
            if (inputs.stream().allMatch(Ingredient::isEmpty)) throw new IllegalArgumentException("Carpentry pattern is empty");
        }

        public int width() {
            return width;
        }

        public int height() {
            return pattern.size();
        }

        public ItemStack result() {
            return result;
        }

        private boolean matches(int gridWidth, int gridHeight, IntFunction<ItemStack> grid) {
            for (int i = 0; i <= gridWidth - width; i++) {
                for (int j = 0; j <= gridHeight - height(); j++) {
                    if (matches(gridWidth, gridHeight, grid, i, j, true) || matches(gridWidth, gridHeight, grid, i, j, false)) return true;
                }
            }
            return false;
        }

        private boolean matches(int gridWidth, int gridHeight, IntFunction<ItemStack> grid, int startX, int startY, boolean mirror) {
            for (int x = 0; x < gridWidth; x++) {
                for (int y = 0; y < gridHeight; y++) {
                    int subX = x - startX;
                    int subY = y - startY;
                    Ingredient target = Ingredient.EMPTY;
                    if (subX >= 0 && subY >= 0 && subX < width && subY < height()) {
                        target = inputs.get(mirror ? width - subX - 1 + subY * width : subX + subY * width);
                    }
                    if (!target.test(grid.apply(x + y * gridWidth))) return false;
                }
            }
            return true;
        }

        //? if >=1.21 {
        /*@Override
        public boolean matches(CraftingInput input, Level level) {
            return matches(input.width(), input.height(), input::getItem);
        }

        @Override
        public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
            return result.copy();
        }

        @Override
        public ItemStack getResultItem(HolderLookup.Provider registries) {
            return result;
        }
        *///?} else {
        @Override
        public boolean matches(CraftingContainer container, Level level) {
            return matches(container.getWidth(), container.getHeight(), container::getItem);
        }

        @Override
        public ItemStack assemble(CraftingContainer container, RegistryAccess registries) {
            return result.copy();
        }

        @Override
        public ItemStack getResultItem(RegistryAccess registries) {
            return result;
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }
        //?}

        @Override
        public boolean canCraftInDimensions(int gridWidth, int gridHeight) {
            return gridWidth >= width && gridHeight >= height();
        }

        @Override
        public NonNullList<Ingredient> getIngredients() {
            return inputs;
        }

        @Override
        public boolean isSpecial() {
            return true;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return CARPENTRY_SERIALIZER.get();
        }

        @Override
        public RecipeType<?> getType() {
            return CARPENTRY.get();
        }

        public static class Serializer implements RecipeSerializer<Carpentry> {
            //? if >=1.21 {
            /*private static final MapCodec<Carpentry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.listOf().fieldOf("pattern").forGetter(r -> r.pattern),
                    Codec.unboundedMap(Codec.STRING, Ingredient.CODEC_NONEMPTY).fieldOf("key").forGetter(r -> r.key),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result)
            ).apply(instance, Carpentry::new));
            private static final StreamCodec<RegistryFriendlyByteBuf, Carpentry> STREAM_CODEC = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), r -> r.pattern,
                    ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, Ingredient.CONTENTS_STREAM_CODEC), r -> new HashMap<>(r.key),
                    ItemStack.STREAM_CODEC, r -> r.result,
                    Carpentry::new);

            @Override
            public MapCodec<Carpentry> codec() {
                return CODEC;
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, Carpentry> streamCodec() {
                return STREAM_CODEC;
            }
            *///?} else {
            @Override
            public Carpentry fromJson(ResourceLocation id, JsonObject json) {
                List<String> pattern = new ArrayList<>();
                for (JsonElement row : GsonHelper.getAsJsonArray(json, "pattern")) pattern.add(GsonHelper.convertToString(row, "pattern"));
                Map<String, Ingredient> key = new HashMap<>();
                for (Map.Entry<String, JsonElement> entry : GsonHelper.getAsJsonObject(json, "key").entrySet()) {
                    key.put(entry.getKey(), Ingredient.fromJson(entry.getValue(), false));
                }
                return new Carpentry(id, pattern, key, ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result")));
            }

            @Override
            public Carpentry fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
                List<String> pattern = buf.readList(FriendlyByteBuf::readUtf);
                Map<String, Ingredient> key = buf.readMap(FriendlyByteBuf::readUtf, Ingredient::fromNetwork);
                return new Carpentry(id, pattern, key, buf.readItem());
            }

            @Override
            public void toNetwork(FriendlyByteBuf buf, Carpentry recipe) {
                buf.writeCollection(recipe.pattern, FriendlyByteBuf::writeUtf);
                buf.writeMap(recipe.key, FriendlyByteBuf::writeUtf, (b, ingredient) -> ingredient.toNetwork(b));
                buf.writeItem(recipe.result);
            }
            //?}
        }
    }

    public static class FlipDagger extends CustomRecipe {
        //? if >=1.21 {
        /*public FlipDagger(CraftingBookCategory category) {
            super(category);
        }

        @Override
        public boolean matches(CraftingInput input, Level level) {
            return !flip(input.size(), input::getItem).isEmpty();
        }

        @Override
        public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
            return flip(input.size(), input::getItem);
        }
        *///?} else {
        public FlipDagger(ResourceLocation id, CraftingBookCategory category) {
            super(id, category);
        }

        @Override
        public boolean matches(CraftingContainer container, Level level) {
            return !flip(container.getContainerSize(), container::getItem).isEmpty();
        }

        @Override
        public ItemStack assemble(CraftingContainer container, RegistryAccess registries) {
            return flip(container.getContainerSize(), container::getItem);
        }
        //?}

        @Override
        public boolean canCraftInDimensions(int width, int height) {
            return true;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return FLIP_DAGGER.get();
        }
    }

    private static ItemStack flip(int size, IntFunction<ItemStack> grid) {
        ItemStack single = ItemStack.EMPTY;
        for (int i = 0; i < size; i++) {
            ItemStack stack = grid.apply(i);
            if (stack.isEmpty()) continue;
            if (!single.isEmpty()) return ItemStack.EMPTY;
            single = stack;
        }
        if (single.isEmpty()) return ItemStack.EMPTY;
        Item flipped = flips().get(single.getItem());
        return flipped == null ? ItemStack.EMPTY : Compat.withItem(single, flipped);
    }

    private static Map<Item, Item> flips() {
        if (flips == null) {
            Map<Item, Item> map = new HashMap<>();
            VCWeapons.DAGGERS.forEach((material, dagger) -> {
                Item reversed = VCWeapons.REVERSE_DAGGERS.get(material).get();
                map.put(dagger.get(), reversed);
                map.put(reversed, dagger.get());
            });
            map.put(VCWeapons.KUNAI.get(), VCWeapons.REVERSE_KUNAI.get());
            map.put(VCWeapons.REVERSE_KUNAI.get(), VCWeapons.KUNAI.get());
            flips = map;
        }
        return flips;
    }

    private VCRecipes() {
    }
}
