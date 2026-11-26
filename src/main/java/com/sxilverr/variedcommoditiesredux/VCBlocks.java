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
import com.sxilverr.variedcommoditiesredux.block.ShelfBlock;
import com.sxilverr.variedcommoditiesredux.block.StationBlock;
import com.sxilverr.variedcommoditiesredux.block.StorageBlock;
import com.sxilverr.variedcommoditiesredux.block.TableBlock;
import com.sxilverr.variedcommoditiesredux.block.VCVariant;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class VCBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(VariedCommodities.MODID, Registries.BLOCK);
    public static final List<RegistrySupplier<Block>> FURNITURE = new ArrayList<>();

    public static final Map<VCVariant, RegistrySupplier<Block>> CHAIRS = woods("chair", (v, p) -> new SeatBlock(v, null, FurnitureBlock.box(0.1, 0.0, 0.1, 0.9, 0.5, 0.9), p));
    public static final Map<VCVariant, RegistrySupplier<Block>> STOOLS = woods("stool", (v, p) -> new SeatBlock(v, null, FurnitureBlock.box(0.1, 0.0, 0.1, 0.9, 0.6, 0.9), p));
    public static final Map<VCVariant, RegistrySupplier<Block>> COUCHES = woods("couch", (v, p) -> new CouchBlock(v, false, null, p));
    public static final Map<VCVariant, RegistrySupplier<Block>> WOOL_COUCHES = woods("wool_couch", CouchBlock.Wool::new);
    public static final Map<VCVariant, RegistrySupplier<Block>> TABLES = woods("table", (v, p) -> new TableBlock(v, null, p));
    public static final Map<VCVariant, RegistrySupplier<Block>> SHELVES = woods("shelf", (v, p) -> new ShelfBlock(v, null, new VoxelShape[]{
            FurnitureBlock.box(0.0, 0.44, 0.3, 1.0, 1.0, 1.0),
            FurnitureBlock.box(0.0, 0.44, 0.0, 0.7, 1.0, 1.0),
            FurnitureBlock.box(0.0, 0.44, 0.0, 1.0, 1.0, 0.7),
            FurnitureBlock.box(0.3, 0.44, 0.0, 1.0, 1.0, 1.0)}, p));
    public static final Map<VCVariant, RegistrySupplier<Block>> BEAMS = woods("beam", (v, p) -> new FurnitureBlock(v, null, new VoxelShape[]{
            FurnitureBlock.box(0.33, 0.33, 0.25, 0.67, 0.67, 1.0),
            FurnitureBlock.box(0.0, 0.33, 0.33, 0.75, 0.67, 0.67),
            FurnitureBlock.box(0.33, 0.33, 0.0, 0.67, 0.67, 0.75),
            FurnitureBlock.box(0.25, 0.33, 0.33, 1.0, 0.67, 0.67)}, true, p));
    public static final Map<VCVariant, RegistrySupplier<Block>> CRATES = woods("crate", (v, p) -> new StorageBlock(v, VCBlockEntities.CRATE, p));
    public static final Map<VCVariant, RegistrySupplier<Block>> BARRELS = woods("barrel", (v, p) -> new StorageBlock.Barrel(v, VCBlockEntities.BARREL, p));
    public static final Map<VCVariant, RegistrySupplier<Block>> WEAPON_RACKS = woods("weapon_rack", (v, p) -> new DisplayBlock.WeaponRack(v, VCBlockEntities.WEAPON_RACK, p));
    public static final Map<VCVariant, RegistrySupplier<Block>> SHOP_SIGNS = woods("shop_sign", (v, p) -> new IconBlocks.ShopSign(v, VCBlockEntities.SHOP_SIGN, p));
    public static final Map<VCVariant, RegistrySupplier<Block>> BANNERS = materials("banner", (v, p) -> new IconBlocks.Banner(v, VCBlockEntities.BANNER, p));
    public static final Map<VCVariant, RegistrySupplier<Block>> WALL_BANNERS = materials("wall_banner", (v, p) -> new IconBlocks.WallBanner(v, VCBlockEntities.WALL_BANNER, p));
    public static final Map<VCVariant, RegistrySupplier<Block>> TALL_LAMPS = materials("tall_lamp", (v, p) -> new IconBlocks.TallLamp(v, null, p.sound(SoundType.STONE).lightLevel(s -> 15)));
    public static final Map<VCVariant, RegistrySupplier<Block>> PEDESTALS = materials("pedestal", (v, p) -> new DisplayBlock.Pedestal(v, VCBlockEntities.PEDESTAL, p));

    public static final RegistrySupplier<Block> CANDLE = furniture("candle", p -> new LightableBlock(LightableBlock.Kind.CANDLE, null, p.lightLevel(s -> LightableBlock.lightLevel(s, 15))), false);
    public static final RegistrySupplier<Block> LAMP = furniture("lamp", p -> new LightableBlock(LightableBlock.Kind.LAMP, null, p.lightLevel(s -> LightableBlock.lightLevel(s, 15))), false);
    public static final RegistrySupplier<Block> CAMPFIRE = furniture("campfire", p -> new LightableBlock(LightableBlock.Kind.CAMPFIRE, null, p.lightLevel(s -> LightableBlock.lightLevel(s, 14))), true);
    public static final RegistrySupplier<Block> TRADING_BLOCK = furniture("trading_block", p -> new StationBlock.Trading(VCBlockEntities.TRADING_BLOCK, p.sound(SoundType.STONE)), false);
    public static final RegistrySupplier<Block> CARPENTRY_BENCH = furniture("carpentry_bench", p -> new StationBlock.Carpentry(false, null, p), false);
    public static final RegistrySupplier<Block> BLACKSMITH_ANVIL = furniture("blacksmith_anvil", p -> new StationBlock.Carpentry(true, null, p), false);
    public static final RegistrySupplier<Block> BIG_SIGN = furniture("big_sign", p -> new BigSignBlock(-1, VCBlockEntities.BIG_SIGN, p), false);
    public static final RegistrySupplier<Block> BOOK = furniture("book", p -> new BookBlock(VCBlockEntities.BOOK, p), false);
    public static final RegistrySupplier<Block> SQUARE_TOMBSTONE = furniture("square_tombstone", p -> new BigSignBlock(0, VCBlockEntities.TOMBSTONE, p), true);
    public static final RegistrySupplier<Block> TOMBSTONE = furniture("tombstone", p -> new BigSignBlock(1, VCBlockEntities.TOMBSTONE, p), true);
    public static final RegistrySupplier<Block> BROKEN_TOMBSTONE = furniture("broken_tombstone", p -> new BigSignBlock(2, VCBlockEntities.TOMBSTONE, p), true);

    public static final List<RegistrySupplier<Block>> BLOOD = new ArrayList<>();
    public static final Map<DyeColor, RegistrySupplier<Block>> CRYSTALS = new EnumMap<>(DyeColor.class);
    public static final List<RegistrySupplier<Block>> PLACEHOLDERS = new ArrayList<>();

    static {
        for (int i = 0; i < 3; i++) {
            BLOOD.add(plain("blood_splat_" + (i + 1), p -> new BloodBlock(p
                    .mapColor(MapColor.COLOR_RED).replaceable().noCollision().strength(0.2F).noLootTable()
                    .lightLevel(s -> 1).sound(SoundType.SLIME_BLOCK).pushReaction(PushReaction.DESTROY))));
        }
        for (DyeColor color : DyeColor.values()) {
            CRYSTALS.put(color, plain(color.getName() + "_crystal_block", p -> new CrystalBlock(color, p
                    .mapColor(color).strength(5.0F, 6.0F).sound(SoundType.GLASS).noOcclusion().lightLevel(s -> 12)
                    .isValidSpawn((s, l, pos, e) -> false).isRedstoneConductor((s, l, pos) -> false)
                    //? if >=26.3 {
                    /*.isSuffocating((s, l, pos) -> false).isViewBlocking((s, l, pos, box) -> false))));
                    *///?} else {
                    .isSuffocating((s, l, pos) -> false).isViewBlocking((s, l, pos) -> false))));
                    //?}
        }
        for (int i = 0; i < 16; i++) {
            PLACEHOLDERS.add(plain("placeholder_" + i, p -> new Block(p.mapColor(MapColor.STONE).sound(SoundType.WOOD))));
        }
    }

    private static BlockBehaviour.Properties wood() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(5.0F, 6.0F).sound(SoundType.WOOD).ignitedByLava();
    }

    private static BlockBehaviour.Properties rock() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(5.0F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops();
    }

    private static Map<VCVariant, RegistrySupplier<Block>> woods(String name, BiFunction<VCVariant, BlockBehaviour.Properties, Block> factory) {
        return variants(VCVariant.WOODS, name, factory, false);
    }

    private static Map<VCVariant, RegistrySupplier<Block>> materials(String name, BiFunction<VCVariant, BlockBehaviour.Properties, Block> factory) {
        return variants(VCVariant.MATERIALS, name, factory, !name.equals("tall_lamp"));
    }

    private static Map<VCVariant, RegistrySupplier<Block>> variants(VCVariant[] variants, String name, BiFunction<VCVariant, BlockBehaviour.Properties, Block> factory, boolean rock) {
        Map<VCVariant, RegistrySupplier<Block>> map = new EnumMap<>(VCVariant.class);
        for (VCVariant variant : variants) {
            map.put(variant, furniture(variant.id() + "_" + name, p -> factory.apply(variant, p), rock));
        }
        return map;
    }

    private static RegistrySupplier<Block> furniture(String name, Function<BlockBehaviour.Properties, Block> factory, boolean rock) {
        RegistrySupplier<Block> block = register(name, rock ? VCBlocks::rock : VCBlocks::wood, factory);
        FURNITURE.add(block);
        return block;
    }

    private static RegistrySupplier<Block> plain(String name, Function<BlockBehaviour.Properties, Block> factory) {
        return register(name, BlockBehaviour.Properties::of, factory);
    }

    private static RegistrySupplier<Block> register(String name, Supplier<BlockBehaviour.Properties> base, Function<BlockBehaviour.Properties, Block> factory) {
        RegistrySupplier<Block> block = BLOCKS.register(name, () -> factory.apply(base.get().setId(ResourceKey.create(Registries.BLOCK, VariedCommodities.id(name)))));
        VCItems.register(name, VCTabs.BLOCKS, p -> new BlockItem(block.get(), p.useBlockDescriptionPrefix()));
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
