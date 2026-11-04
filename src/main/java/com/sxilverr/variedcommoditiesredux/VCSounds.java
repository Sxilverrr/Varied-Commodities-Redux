package com.sxilverr.variedcommoditiesredux;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public final class VCSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(VariedCommodities.MODID, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> GUN_EMPTY = register("gun.empty");
    public static final RegistrySupplier<SoundEvent> GUN_RELOAD = register("gun.reload");
    public static final RegistrySupplier<SoundEvent> GUN_SHOT = register("gun.shot");
    public static final RegistrySupplier<SoundEvent> GUN_TRIGGER = register("gun.trigger");
    public static final RegistrySupplier<SoundEvent> MAGIC_CHARGE = register("magic.charge");
    public static final RegistrySupplier<SoundEvent> MAGIC_SHOT = register("magic.shot");
    public static final RegistrySupplier<SoundEvent> OLD_EXPLODE = register("misc.old_explode");
    public static final RegistrySupplier<SoundEvent> SWOOSH = register("misc.swoosh");
    public static final RegistrySupplier<SoundEvent> HALLELUJAH = register("misc.hallelujah");

    private static RegistrySupplier<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(VariedCommodities.id(name)));
    }

    private VCSounds() {
    }
}
