package com.sxilverr.variedcommoditiesredux.fabric;

import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import com.sxilverr.variedcommoditiesredux.network.VCNetwork;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public final class VariedCommoditiesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FabricConfig.load();
        VariedCommodities.init();
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> VCNetwork.syncRecipes(player));
    }
}
