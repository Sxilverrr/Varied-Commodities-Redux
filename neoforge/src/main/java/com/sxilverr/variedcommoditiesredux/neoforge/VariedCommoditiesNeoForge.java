package com.sxilverr.variedcommoditiesredux.neoforge;

import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import com.sxilverr.variedcommoditiesredux.network.VCNetwork;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

@Mod(VariedCommodities.MODID)
public final class VariedCommoditiesNeoForge {
    public VariedCommoditiesNeoForge(IEventBus modBus, ModContainer container) {
        NeoForgeConfig.register(modBus, container);
        VariedCommodities.init();
        NeoForge.EVENT_BUS.addListener((OnDatapackSyncEvent event) -> event.getRelevantPlayers().forEach(VCNetwork::syncRecipes));
        if (FMLEnvironment.getDist().isClient()) NeoForgeClient.init(modBus, container);
    }
}
