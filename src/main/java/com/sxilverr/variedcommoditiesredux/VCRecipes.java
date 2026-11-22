package com.sxilverr.variedcommoditiesredux;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.IntFunction;

public final class VCRecipes {
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(VariedCommodities.MODID, Registries.RECIPE_TYPE);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(VariedCommodities.MODID, Registries.RECIPE_SERIALIZER);
    public static final RegistrySupplier<RecipeType<CraftingRecipe>> CARPENTRY = TYPES.register("carpentry", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return VariedCommodities.MODID + ":carpentry";
        }
    });
    public static final RegistrySupplier<RecipeSerializer<Carpentry>> CARPENTRY_SERIALIZER = SERIALIZERS.register("carpentry", Carpentry.Serializer::new);
    public static final RegistrySupplier<RecipeSerializer<FlipDagger>> FLIP_DAGGER = SERIALIZERS.register("flip_dagger", () -> new CustomRecipe.Serializer<>(FlipDagger::new));

    public static List<Entry> synced = List.of();
    public static Runnable onSync = () -> {
    };

    private static Map<Item, Item> flips;

    public record Entry(boolean carpentry, int width, int height, List<Optional<Ingredient>> inputs, ItemStack result) {
        public static final StreamCodec<RegistryFriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL, Entry::carpentry,
                ByteBufCodecs.VAR_INT, Entry::width,
                ByteBufCodecs.VAR_INT, Entry::height,
                Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), Entry::inputs,
                ItemStack.STREAM_CODEC, Entry::result,
                Entry::new);
    }

    public static List<Entry> entries(RecipeManager manager, HolderLookup.Provider registries) {
        List<Entry> list = new ArrayList<>();
        for (RecipeHolder<?> holder : manager.getRecipes()) {
            if (!holder.id().identifier().getNamespace().equals(VariedCommodities.MODID)) continue;
            if (holder.value() instanceof ShapedRecipe shaped) {
                list.add(new Entry(false, shaped.getWidth(), shaped.getHeight(), shaped.getIngredients(), shaped.assemble(CraftingInput.EMPTY, registries)));
            } else if (holder.value() instanceof Carpentry carpentry) {
                list.add(new Entry(true, carpentry.width(), carpentry.height(), carpentry.inputs(), carpentry.result()));
            }
        }
        list.sort(Comparator.comparingInt(entry -> Item.getId(entry.result().getItem())));
        return list;
    }

    public static class Carpentry implements CraftingRecipe {
        private final List<String> pattern;
        private final Map<String, Ingredient> key;
        private final ItemStack result;
        private final int width;
        private final List<Optional<Ingredient>> inputs;
        private PlacementInfo placement;

        public Carpentry(List<String> pattern, Map<String, Ingredient> key, ItemStack result) {
            this.pattern = List.copyOf(pattern);
            this.key = Map.copyOf(key);
            this.result = result;
            this.width = pattern.stream().mapToInt(String::length).max().orElse(0);
            if (pattern.size() > 4 || width > 4) throw new IllegalArgumentException("Carpentry patterns can be at most 4x4");
            List<Optional<Ingredient>> cells = new ArrayList<>(Collections.nCopies(width * pattern.size(), Optional.empty()));
            for (int y = 0; y < pattern.size(); y++) {
                String row = pattern.get(y);
                for (int x = 0; x < row.length(); x++) {
                    char c = row.charAt(x);
                    if (c == ' ') continue;
                    Ingredient ingredient = key.get(String.valueOf(c));
                    if (ingredient == null) throw new IllegalArgumentException("Pattern references undefined symbol '" + c + "'");
                    cells.set(x + y * width, Optional.of(ingredient));
                }
            }
            if (cells.stream().allMatch(Optional::isEmpty)) throw new IllegalArgumentException("Carpentry pattern is empty");
            this.inputs = List.copyOf(cells);
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

        public List<Optional<Ingredient>> inputs() {
            return inputs;
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
                    Optional<Ingredient> target = Optional.empty();
                    if (subX >= 0 && subY >= 0 && subX < width && subY < height()) {
                        target = inputs.get(mirror ? width - subX - 1 + subY * width : subX + subY * width);
                    }
                    if (!Ingredient.testOptionalIngredient(target, grid.apply(x + y * gridWidth))) return false;
                }
            }
            return true;
        }

        @Override
        public boolean matches(CraftingInput input, Level level) {
            return matches(input.width(), input.height(), input::getItem);
        }

        @Override
        public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
            return result.copy();
        }

        @Override
        public PlacementInfo placementInfo() {
            if (placement == null) placement = PlacementInfo.createFromOptionals(inputs);
            return placement;
        }

        @Override
        public CraftingBookCategory category() {
            return CraftingBookCategory.MISC;
        }

        @Override
        public RecipeSerializer<Carpentry> getSerializer() {
            return CARPENTRY_SERIALIZER.get();
        }

        @Override
        public RecipeType<CraftingRecipe> getType() {
            return CARPENTRY.get();
        }

        public static class Serializer implements RecipeSerializer<Carpentry> {
            private static final MapCodec<Carpentry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.listOf().fieldOf("pattern").forGetter(r -> r.pattern),
                    Codec.unboundedMap(Codec.STRING, Ingredient.CODEC).fieldOf("key").forGetter(r -> r.key),
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
        }
    }

    public static class FlipDagger extends CustomRecipe {
        public FlipDagger(CraftingBookCategory category) {
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

        @Override
        public RecipeSerializer<FlipDagger> getSerializer() {
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
        return flipped == null ? ItemStack.EMPTY : single.transmuteCopy(flipped);
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
