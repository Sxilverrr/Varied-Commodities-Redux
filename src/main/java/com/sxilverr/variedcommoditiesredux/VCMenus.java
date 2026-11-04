package com.sxilverr.variedcommoditiesredux;

import com.sxilverr.variedcommoditiesredux.menu.CarpentryMenu;
import com.sxilverr.variedcommoditiesredux.menu.TradingMenu;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public final class VCMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(VariedCommodities.MODID, Registries.MENU);

    public static final RegistrySupplier<MenuType<CarpentryMenu>> CARPENTRY = MENUS.register("carpentry",
            () -> MenuRegistry.ofExtended((id, inventory, buf) -> new CarpentryMenu(id, inventory, buf.readBlockPos())));
    public static final RegistrySupplier<MenuType<TradingMenu>> TRADING = MENUS.register("trading",
            () -> MenuRegistry.ofExtended((id, inventory, buf) -> new TradingMenu(id, inventory, buf.readBlockPos())));

    private VCMenus() {
    }
}
