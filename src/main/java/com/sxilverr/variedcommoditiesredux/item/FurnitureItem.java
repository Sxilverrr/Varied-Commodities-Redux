package com.sxilverr.variedcommoditiesredux.item;

import com.sxilverr.variedcommoditiesredux.platform.VCPlatform;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class FurnitureItem extends BlockItem {
    public FurnitureItem(Block block, Properties properties) {
        super(block, properties);
    }

    public void initializeClient(Consumer<Object> consumer) {
        Object extensions = VCPlatform.clientItemExtensions(this);
        if (extensions != null) consumer.accept(extensions);
    }
}
