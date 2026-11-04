package com.sxilverr.variedcommoditiesredux.item;

import com.sxilverr.variedcommoditiesredux.platform.VCPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;

import java.util.function.Consumer;

//? if >=1.21 {
/*import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
*///?} else {
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableLeatherItem;
//?}

public class VCArmorItem extends ArmorItem {
    private final String namespace;
    private final String texture;
    private final boolean skirt;

    //? if >=1.21 {
    /*public VCArmorItem(String namespace, String texture, VCArmorMaterials.Base base, Type type, boolean skirt, boolean dyeable, Properties properties) {
        super(VCArmorMaterials.get(namespace, texture, base, dyeable), type, properties.durability(type.getDurability(base.durability)));
        this.namespace = namespace;
        this.texture = texture;
        this.skirt = skirt;
    }
    *///?} else {
    public VCArmorItem(String namespace, String texture, VCArmorMaterials.Base base, Type type, boolean skirt, boolean dyeable, Properties properties) {
        super(VCArmorMaterials.get(namespace, texture, base, dyeable), type, properties);
        this.namespace = namespace;
        this.texture = texture;
        this.skirt = skirt;
    }
    //?}

    public ResourceLocation layerTexture() {
        return VCArmorMaterials.texture(namespace, texture, getType() == Type.LEGGINGS);
    }

    public boolean isSkirt() {
        return skirt;
    }

    public void initializeClient(Consumer<Object> consumer) {
        Object extensions = VCPlatform.clientItemExtensions(this);
        if (extensions != null) consumer.accept(extensions);
    }

    //? if >=1.21 {
    /*public static class Dyeable extends VCArmorItem {
        public Dyeable(String namespace, String texture, VCArmorMaterials.Base base, Type type, boolean skirt, Properties properties) {
            super(namespace, texture, base, type, skirt, true, properties);
        }
    }
    *///?} else {
    public static class Dyeable extends VCArmorItem implements DyeableLeatherItem {
        public Dyeable(String namespace, String texture, VCArmorMaterials.Base base, Type type, boolean skirt, Properties properties) {
            super(namespace, texture, base, type, skirt, true, properties);
        }
    }
    //?}
}
