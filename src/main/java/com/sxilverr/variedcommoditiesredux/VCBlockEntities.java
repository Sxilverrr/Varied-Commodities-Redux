package com.sxilverr.variedcommoditiesredux;

import com.sxilverr.variedcommoditiesredux.block.entity.BigSignBlockEntity;
import com.sxilverr.variedcommoditiesredux.block.entity.BookBlockEntity;
import com.sxilverr.variedcommoditiesredux.block.entity.IconBlockEntity;
import com.sxilverr.variedcommoditiesredux.block.entity.StorageBlockEntity;
import com.sxilverr.variedcommoditiesredux.block.entity.TradingBlockEntity;
import com.sxilverr.variedcommoditiesredux.block.entity.VCBlockEntity;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public final class VCBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(VariedCommodities.MODID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<StorageBlockEntity>> CRATE = register("crate", StorageBlockEntity::chest, () -> VCBlocks.CRATES.values());
    public static final RegistrySupplier<BlockEntityType<StorageBlockEntity>> BARREL = register("barrel", StorageBlockEntity::chest, () -> VCBlocks.BARRELS.values());
    public static final RegistrySupplier<BlockEntityType<StorageBlockEntity>> WEAPON_RACK = register("weapon_rack", StorageBlockEntity::weaponRack, () -> VCBlocks.WEAPON_RACKS.values());
    public static final RegistrySupplier<BlockEntityType<StorageBlockEntity>> PEDESTAL = register("pedestal", StorageBlockEntity::pedestal, () -> VCBlocks.PEDESTALS.values());
    public static final RegistrySupplier<BlockEntityType<IconBlockEntity>> SHOP_SIGN = register("shop_sign", IconBlockEntity::new, () -> VCBlocks.SHOP_SIGNS.values());
    public static final RegistrySupplier<BlockEntityType<IconBlockEntity>> BANNER = register("banner", IconBlockEntity::new, () -> VCBlocks.BANNERS.values());
    public static final RegistrySupplier<BlockEntityType<IconBlockEntity>> WALL_BANNER = register("wall_banner", IconBlockEntity::new, () -> VCBlocks.WALL_BANNERS.values());
    public static final RegistrySupplier<BlockEntityType<TradingBlockEntity>> TRADING_BLOCK = register("trading_block", TradingBlockEntity::new, () -> List.of(VCBlocks.TRADING_BLOCK));
    public static final RegistrySupplier<BlockEntityType<BigSignBlockEntity>> BIG_SIGN = register("big_sign", BigSignBlockEntity::new, () -> List.of(VCBlocks.BIG_SIGN));
    public static final RegistrySupplier<BlockEntityType<BigSignBlockEntity>> TOMBSTONE = register("tombstone", BigSignBlockEntity::new, () -> List.of(VCBlocks.SQUARE_TOMBSTONE, VCBlocks.TOMBSTONE, VCBlocks.BROKEN_TOMBSTONE));
    public static final RegistrySupplier<BlockEntityType<BookBlockEntity>> BOOK = register("book", BookBlockEntity::new, () -> List.of(VCBlocks.BOOK));

    public interface Factory<T extends BlockEntity> {
        T create(BlockEntityType<?> type, BlockPos pos, BlockState state);
    }

    private static <T extends BlockEntity> RegistrySupplier<BlockEntityType<T>> register(String name, Factory<T> factory, Supplier<Iterable<RegistrySupplier<Block>>> blocks) {
        return BLOCK_ENTITIES.register(name, () -> {
            AtomicReference<BlockEntityType<T>> self = new AtomicReference<>();
            BlockEntityType<T> type = BlockEntityType.Builder.of((pos, state) -> factory.create(self.get(), pos, state), VCBlocks.all(blocks.get())).build(null);
            self.set(type);
            return type;
        });
    }

    private VCBlockEntities() {
    }
}
