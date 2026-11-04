package com.sxilverr.variedcommoditiesredux.fabric;

import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import net.fabricmc.api.ModInitializer;

public final class VariedCommoditiesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FabricConfig.load();
        VariedCommodities.init();
    }
}
