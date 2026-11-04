package com.sxilverr.variedcommoditiesredux.neoforge;

import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(VariedCommodities.MODID)
public final class VariedCommoditiesNeoForge {
    public VariedCommoditiesNeoForge(IEventBus modBus, ModContainer container) {
        NeoForgeConfig.register(modBus, container);
        VariedCommodities.init();
        if (FMLEnvironment.dist.isClient()) NeoForgeClient.init(modBus, container);
    }
}
