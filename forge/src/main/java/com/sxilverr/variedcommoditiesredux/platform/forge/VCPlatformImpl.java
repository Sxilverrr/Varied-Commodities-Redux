package com.sxilverr.variedcommoditiesredux.platform.forge;

import com.sxilverr.variedcommoditiesredux.forge.ForgeClientExtensions;
import net.minecraft.world.item.Item;

public final class VCPlatformImpl {
    public static Object clientItemExtensions(Item item) {
        return ForgeClientExtensions.create(item);
    }

    private VCPlatformImpl() {
    }
}
