package com.sxilverr.variedcommoditiesredux;

import com.sxilverr.variedcommoditiesredux.block.BigSignBlock;
import com.sxilverr.variedcommoditiesredux.block.BloodBlock;
import com.sxilverr.variedcommoditiesredux.block.BookBlock;
import com.sxilverr.variedcommoditiesredux.block.CouchBlock;
import com.sxilverr.variedcommoditiesredux.block.CrystalBlock;
import com.sxilverr.variedcommoditiesredux.block.DisplayBlock;
import com.sxilverr.variedcommoditiesredux.block.FurnitureBlock;
import com.sxilverr.variedcommoditiesredux.block.IconBlocks;
import com.sxilverr.variedcommoditiesredux.block.LightableBlock;
import com.sxilverr.variedcommoditiesredux.block.SeatBlock;
import com.sxilverr.variedcommoditiesredux.block.StationBlock;
import com.sxilverr.variedcommoditiesredux.block.StorageBlock;
import com.sxilverr.variedcommoditiesredux.block.ShelfBlock;
import com.sxilverr.variedcommoditiesredux.block.TableBlock;
import com.sxilverr.variedcommoditiesredux.block.VCVariant;
import com.sxilverr.variedcommoditiesredux.item.FurnitureItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public final class VCBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(VariedCommodities.MODID, Registries.BLOCK);
    public static final List<RegistrySupplier<Block>> FURNITURE = new ArrayList<>();
    public static final List<RegistrySupplier<Block>> ROCK = new ArrayList<>();
    public static final List<RegistrySupplier<Block>> WOOD = new ArrayList<>();

    public static final Map<VCVariant, RegistrySupplier<Block>> CHAIRS = woods("chair", v -> new SeatBlock(v, null, FurnitureBlock.box(0.1, 0.0, 0.1, 0.9, 0.5, 0.9), wood()));
    public static final Map<VCVariant, RegistrySupplier<Block>> STOOLS = woods("stool", v -> new SeatBlock(v, null, FurnitureBlock.box(0.1, 0.0, 0.1, 0.9, 0.6, 0.9), wood()));
    public static final Map<VCVariant, RegistrySupplier<Block>> COUCHES = woods("couch", v -> new CouchBlock(v, false, null, wood()));
    public static final Map<VCVariant, RegistrySupplier<Block>> WOOL_COUCHES = woods("wool_couch", v -> new CouchBlock.Wool(v, wood()));
    public static final Map<VCVariant, RegistrySupplier<Block>> TABLES = woods("table", v -> new TableBlock(v, null, wood()));
    public static final Map<VCVariant, RegistrySupplier<Block>> SHELVES = woods("shelf", v -> new ShelfBlock(v, null, new net.minecraft.world.phys.shapes.VoxelShape[]{
            FurnitureBlock.box(0.0, 0.44, 0.3, 1.0, 1.0, 1.0),
            FurnitureBlock.box(0.0, 0.44, 0.0, 0.7, 1.0, 1.0),
            FurnitureBlock.box(0.0, 0.44, 0.0, 1.0, 1.0, 0.7),
            FurnitureBlock.box(0.3, 0.44, 0.0, 1.0, 1.0, 1.0)}, wood()));
    public static final Map<VCVariant, RegistrySupplier<Block>> BEAMS = woods("beam", v -> new FurnitureBlock(v, null, new net.minecraft.world.phys.shapes.VoxelShape[]{
            FurnitureBlock.box(0.33, 0.33, 0.25, 0.67, 0.67, 1.0),
            FurnitureBlock.box(0.0, 0.33, 0.33, 0.75, 0.67, 0.67),
            FurnitureBlock.box(0.33, 0.33, 0.0, 0.67, 0.67, 0.75),
            FurnitureBlock.box(0.25, 0.33, 0.33, 1.0, 0.67, 0.67)}, true, wood()));
    public static final Map<VCVariant, RegistrySupplier<Block>> CRATES = woods("crate", v -> new StorageBlock(v, VCBlockEntities.CRATE, wood()));
    public static final Map<VCVariant, RegistrySupplier<Block>> BARRELS = woods("barrel", v -> new StorageBlock.Barrel(v, VCBlockEntities.BARREL, wood()));
    public static final Map<VCVariant, RegistrySupplier<Block>> WEAPON_RACKS = woods("weapon_rack", v -> new DisplayBlock.WeaponRack(v, VCBlockEntities.WEAPON_RACK, wood()));
    public static final Map<VCVariant, RegistrySupplier<Block>> SHOP_SIGNS = woods("shop_sign", v -> new IconBlocks.ShopSign(v, VCBlockEntities.SHOP_SIGN, wood()));
    public static final Map<VCVariant, RegistrySupplier<Block>> BANNERS = materials("banner", v -> new IconBlocks.Banner(v, VCBlockEntities.BANNER, rock()));
    public static final Map<VCVariant, RegistrySupplier<Block>> WALL_BANNERS = materials("wall_banner", v -> new IconBlocks.WallBanner(v, VCBlockEntities.WALL_BANNER, rock()));
    public static final Map<VCVariant, RegistrySupplier<Block>> TALL_LAMPS = materials("tall_lamp", v -> new IconBlocks.TallLamp(v, null, wood().sound(SoundType.STONE).lightLevel(s -> 15)));
    public static final Map<VCVariant, RegistrySupplier<Block>> PEDESTALS = materials("pedestal", v -> new DisplayBlock.Pedestal(v, VCBlockEntities.PEDESTAL, rock()));

    public static final RegistrySupplier<Block> CANDLE = furniture("candle", () -> new LightableBlock(LightableBlock.Kind.CANDLE, null, wood().lightLevel(s -> LightableBlock.lightLevel(s, 15))), false);
    public static final RegistrySupplier<Block> LAMP = furniture("lamp", () -> new LightableBlock(LightableBlock.Kind.LAMP, null, wood().lightLevel(s -> LightableBlock.lightLevel(s, 15))), false);
    public static final RegistrySupplier<Block> CAMPFIRE = furniture("campfire", () -> new LightableBlock(LightableBlock.Kind.CAMPFIRE, null, rock().lightLevel(s -> LightableBlock.lightLevel(s, 14))), true);
    public static final RegistrySupplier<Block> TRADING_BLOCK = furniture("trading_block", () -> new StationBlock.Trading(VCBlockEntities.TRADING_BLOCK, wood().sound(SoundType.STONE)), false);
    public static final RegistrySupplier<Block> CARPENTRY_BENCH = furniture("carpentry_bench", () -> new StationBlock.Carpentry(false, null, wood()), false);
    public static final RegistrySupplier<Block> BLACKSMITH_ANVIL = furniture("blacksmith_anvil", () -> new StationBlock.Carpentry(true, null, wood()), false);
    public static final RegistrySupplier<Block> BIG_SIGN = furniture("big_sign", () -> new BigSignBlock(-1, VCBlockEntities.BIG_SIGN, wood()), false);
    public static final RegistrySupplier<Block> BOOK = furniture("book", () -> new BookBlock(VCBlockEntities.BOOK, wood()), false);
    public static final RegistrySupplier<Block> SQUARE_TOMBSTONE = furniture("square_tombstone", () -> new BigSignBlock(0, VCBlockEntities.TOMBSTONE, rock()), true);
    public static final RegistrySupplier<Block> TOMBSTONE = furniture("tombstone", () -> new BigSignBlock(1, VCBlockEntities.TOMBSTONE, rock()), true);
    public static final RegistrySupplier<Block> BROKEN_TOMBSTONE = furniture("broken_tombstone", () -> new BigSignBlock(2, VCBlockEntities.TOMBSTONE, rock()), true);

    public static final List<RegistrySupplier<Block>> BLOOD = new ArrayList<>();
    public static final Map<DyeColor, RegistrySupplier<Block>> CRYSTALS = new EnumMap<>(DyeColor.class);
    public static final List<RegistrySupplier<Block>> PLACEHOLDERS = new ArrayList<>();

    static {
        for (int i = 0; i < 3; i++) {
            BLOOD.add(plain("blood_splat_" + (i + 1), () -> new BloodBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED).replaceable().noCollission().strength(0.2F).noLootTable()
                    .lightLevel(s -> 1).sound(SoundType.SLIME_BLOCK).pushReaction(PushReaction.DESTROY))));
        }
        for (DyeColor color : DyeColor.values()) {
            CRYSTALS.put(color, plain(color.getName() + "_crystal_block", () -> new CrystalBlock(color, BlockBehaviour.Properties.of()
                    .mapColor(color).strength(5.0F, 6.0F).sound(SoundType.GLASS).noOcclusion().lightLevel(s -> 12)
                    .isValidSpawn((s, l, p, e) -> false).isRedstoneConductor((s, l, p) -> false)
                    .isSuffocating((s, l, p) -> false).isViewBlocking((s, l, p) -> false))));
        }
        for (int i = 0; i < 16; i++) {
            PLACEHOLDERS.add(plain("placeholder_" + i, () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.WOOD))));
        }
    }

    public static BlockBehaviour.Properties wood() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(5.0F, 6.0F).sound(SoundType.WOOD).ignitedByLava();
    }

    public static BlockBehaviour.Properties rock() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(5.0F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops();
    }

    private static Map<VCVariant, RegistrySupplier<Block>> woods(String name, Function<VCVariant, Block> factory) {
        return variants(VCVariant.WOODS, name, factory, false);
    }

    private static Map<VCVariant, RegistrySupplier<Block>> materials(String name, Function<VCVariant, Block> factory) {
        return variants(VCVariant.MATERIALS, name, factory, !name.equals("tall_lamp"));
    }

    private static Map<VCVariant, RegistrySupplier<Block>> variants(VCVariant[] variants, String name, Function<VCVariant, Block> factory, boolean rock) {
        Map<VCVariant, RegistrySupplier<Block>> map = new EnumMap<>(VCVariant.class);
        for (VCVariant variant : variants) {
            map.put(variant, furniture(variant.id() + "_" + name, () -> factory.apply(variant), rock));
        }
        return map;
    }

    private static RegistrySupplier<Block> furniture(String name, Supplier<Block> factory, boolean rock) {
        RegistrySupplier<Block> block = BLOCKS.register(name, factory);
        FURNITURE.add(block);
        (rock ? ROCK : WOOD).add(block);
        VCItems.ITEMS.register(name, () -> name.equals("book")
                ? new FurnitureItem(block.get(), new Item.Properties().arch$tab(VCTabs.BLOCKS))
                : new BlockItem(block.get(), new Item.Properties().arch$tab(VCTabs.BLOCKS)));
        return block;
    }

    private static RegistrySupplier<Block> plain(String name, Supplier<Block> factory) {
        RegistrySupplier<Block> block = BLOCKS.register(name, factory);
        VCItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().arch$tab(VCTabs.BLOCKS)));
        return block;
    }

    public static Block[] all(Iterable<RegistrySupplier<Block>> blocks) {
        List<Block> list = new ArrayList<>();
        blocks.forEach(b -> list.add(b.get()));
        return list.toArray(Block[]::new);
    }

    private VCBlocks() {
    }
}
