package com.sxilverr.variedcommoditiesredux.item;

import net.minecraft.world.item.ItemStack;

import java.util.function.UnaryOperator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record GunState(boolean loaded, boolean reloading, int shots) {
    public static final GunState EMPTY = new GunState(false, false, 0);

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(VariedCommodities.MODID, Registries.DATA_COMPONENT_TYPE);
    public static final RegistrySupplier<DataComponentType<GunState>> TYPE = COMPONENTS.register("gun_state", () -> DataComponentType.<GunState>builder()
            .persistent(RecordCodecBuilder.create(instance -> instance.group(
                    Codec.BOOL.optionalFieldOf("loaded", false).forGetter(GunState::loaded),
                    Codec.BOOL.optionalFieldOf("reloading", false).forGetter(GunState::reloading),
                    Codec.INT.optionalFieldOf("shots", 0).forGetter(GunState::shots)
            ).apply(instance, GunState::new)))
            .networkSynchronized(StreamCodec.composite(
                    ByteBufCodecs.BOOL, GunState::loaded,
                    ByteBufCodecs.BOOL, GunState::reloading,
                    ByteBufCodecs.VAR_INT, GunState::shots,
                    GunState::new))
            .build());

    public static GunState get(ItemStack stack) {
        return stack.getOrDefault(TYPE.get(), EMPTY);
    }

    public static void update(ItemStack stack, UnaryOperator<GunState> change) {
        GunState state = change.apply(get(stack));
        if (state.equals(EMPTY)) stack.remove(TYPE.get());
        else stack.set(TYPE.get(), state);
    }

    public GunState withLoaded(boolean value) {
        return new GunState(value, reloading, shots);
    }

    public GunState withReloading(boolean value) {
        return new GunState(loaded, value, shots);
    }

    public GunState withShots(int value) {
        return new GunState(loaded, reloading, value);
    }
}
