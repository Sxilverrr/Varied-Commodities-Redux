package com.sxilverr.variedcommoditiesredux;

import com.sxilverr.variedcommoditiesredux.block.VCVariant;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public final class VCTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(VariedCommodities.MODID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> BLOCKS = tab("blocks", () -> VCBlocks.WOOL_COUCHES.get(VCVariant.SPRUCE).get());
    public static final RegistrySupplier<CreativeModeTab> ITEMS = tab("items", () -> VCItems.GOLDEN_COIN.get());
    public static final RegistrySupplier<CreativeModeTab> WEAPONS = tab("weapons", () -> VCWeapons.SHURIKEN.get());
    public static final RegistrySupplier<CreativeModeTab> ARMORS = tab("armors", () -> VCArmors.COW_LEATHER_HELMET.get());

    private static RegistrySupplier<CreativeModeTab> tab(String name, Supplier<ItemLike> icon) {
        return TABS.register(name, () -> CreativeTabRegistry.create(
                Component.translatable("itemGroup." + VariedCommodities.MODID + "." + name),
                () -> new ItemStack(icon.get())));
    }

    private VCTabs() {
    }
}
