package com.sxilverr.variedcommoditiesredux.forge;

import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(VariedCommodities.MODID)
public final class VariedCommoditiesForge {
    public VariedCommoditiesForge(FMLJavaModLoadingContext context) {
        EventBuses.registerModEventBus(VariedCommodities.MODID, context.getModEventBus());
        ForgeConfig.register(context);
        VariedCommodities.init();
    }
}
