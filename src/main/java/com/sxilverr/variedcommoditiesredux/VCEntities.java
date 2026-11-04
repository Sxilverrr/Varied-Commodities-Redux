package com.sxilverr.variedcommoditiesredux;

import com.sxilverr.variedcommoditiesredux.entity.HolyHandGrenade;
import com.sxilverr.variedcommoditiesredux.entity.MagicProjectile;
import com.sxilverr.variedcommoditiesredux.entity.SeatEntity;
import com.sxilverr.variedcommoditiesredux.entity.ThrownWeapon;
import com.sxilverr.variedcommoditiesredux.entity.VCProjectile;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class VCEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(VariedCommodities.MODID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<SeatEntity>> SEAT = ENTITIES.register("seat", () ->
            EntityType.Builder.<SeatEntity>of(SeatEntity::new, MobCategory.MISC).sized(0.001F, 0.001F).clientTrackingRange(4).updateInterval(10).noSummon().build("seat"));
    public static final RegistrySupplier<EntityType<VCProjectile>> PROJECTILE = ENTITIES.register("projectile", () ->
            EntityType.Builder.<VCProjectile>of(VCProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(3).build("projectile"));
    public static final RegistrySupplier<EntityType<ThrownWeapon>> THROWN_WEAPON = ENTITIES.register("thrown_weapon", () ->
            EntityType.Builder.<ThrownWeapon>of(ThrownWeapon::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("thrown_weapon"));
    public static final RegistrySupplier<EntityType<MagicProjectile>> MAGIC_PROJECTILE = ENTITIES.register("magic_projectile", () ->
            EntityType.Builder.<MagicProjectile>of(MagicProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(3).build("magic_projectile"));
    public static final RegistrySupplier<EntityType<HolyHandGrenade>> HOLY_HAND_GRENADE = ENTITIES.register("holy_hand_grenade", () ->
            EntityType.Builder.<HolyHandGrenade>of(HolyHandGrenade::new, MobCategory.MISC).sized(0.2F, 0.2F).clientTrackingRange(4).updateInterval(1).build("holy_hand_grenade"));

    private VCEntities() {
    }
}
