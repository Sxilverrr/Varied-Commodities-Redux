package com.sxilverr.variedcommoditiesredux.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.item.Item;

public final class VCPlatform {
    @ExpectPlatform
    public static Object clientItemExtensions(Item item) {
        throw new AssertionError();
    }

    private VCPlatform() {
    }
}
