package com.sxilverr.variedcommoditiesredux;

import com.sxilverr.variedcommoditiesredux.client.VCClient;
import com.sxilverr.variedcommoditiesredux.network.VCNetwork;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraft.resources.ResourceLocation;

public final class VariedCommodities {
    public static final String MODID = "variedcommoditiesredux";

    public static ResourceLocation id(String path) {
        return rl(MODID, path);
    }

    public static ResourceLocation rl(String namespace, String path) {
        //? if >=1.21 {
        /*return ResourceLocation.fromNamespaceAndPath(namespace, path);
        *///?} else {
        return new ResourceLocation(namespace, path);
        //?}
    }

    public static void init() {
        VCSounds.SOUNDS.register();
        VCTabs.TABS.register();
        VCBlocks.BLOCKS.register();
        VCBlockEntities.BLOCK_ENTITIES.register();
        VCEntities.ENTITIES.register();
        VCWeapons.init();
        VCArmors.init();
        //? if >=1.21 {
        /*com.sxilverr.variedcommoditiesredux.item.VCArmorMaterials.MATERIALS.register();
        com.sxilverr.variedcommoditiesredux.item.GunState.COMPONENTS.register();
        *///?}
        VCItems.ITEMS.register();
        VCMenus.MENUS.register();
        VCRecipes.TYPES.register();
        VCRecipes.SERIALIZERS.register();
        VCEnchants.register();
        VCNetwork.init();
        EnvExecutor.runInEnv(Env.CLIENT, () -> VCClient::init);
    }

    private VariedCommodities() {
    }
}
