package com.sxilverr.variedcommoditiesredux.neoforge;

import com.sxilverr.variedcommoditiesredux.VCConfig;
import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class NeoForgeConfig {
    private static final ModConfigSpec.IntValue MACHINE_GUN_DAMAGE;
    private static final ModConfigSpec.IntValue MUSKET_DAMAGE;
    private static final ModConfigSpec.IntValue MUSKET_KNOCKBACK;
    private static final ModConfigSpec.IntValue HEAVY_CROSSBOW_DAMAGE;
    private static final ModConfigSpec.DoubleValue GUN_DAMAGE_MULTIPLIER;
    private static final ModConfigSpec.DoubleValue STAFF_DAMAGE_MULTIPLIER;
    private static final ModConfigSpec.IntValue SLINGSHOT_DAMAGE;
    private static final ModConfigSpec.IntValue SHURIKEN_DAMAGE;
    private static final ModConfigSpec.IntValue GIANT_SHURIKEN_DAMAGE;
    private static final ModConfigSpec.BooleanValue SHURIKENS_BREAK;
    private static final ModConfigSpec.DoubleValue HOLY_HAND_GRENADE_STRENGTH;
    private static final ModConfigSpec.BooleanValue HOLY_HAND_GRENADE_BREAKS_BLOCKS;
    private static final ModConfigSpec.IntValue HOLY_HAND_GRENADE_FUSE;
    private static final ModConfigSpec.IntValue MACHINE_GUN_CLIP_SIZE;
    private static final ModConfigSpec.IntValue MACHINE_GUN_FIRE_DELAY;
    private static final ModConfigSpec.IntValue MUSKET_RELOAD_TIME;
    private static final ModConfigSpec.IntValue HEAVY_CROSSBOW_RELOAD_TIME;
    private static final ModConfigSpec.BooleanValue HEAVY_CROSSBOW_BOLT_PICKUP;
    private static final ModConfigSpec.BooleanValue GUNS_USE_BULLETS;
    private static final ModConfigSpec.BooleanValue STAFFS_USE_MANA;
    private static final ModConfigSpec.DoubleValue STAFF_CHARGE_TIME_MULTIPLIER;
    private static final ModConfigSpec.BooleanValue STAFF_SPELL_EXPLOSION;
    private static final ModConfigSpec.IntValue SLINGSHOT_KNOCKBACK;
    private static final ModConfigSpec.IntValue THROWN_KUNAI_DAMAGE;
    private static final ModConfigSpec.BooleanValue THROWN_WEAPONS_DESPAWN;
    private static final ModConfigSpec.IntValue ENCHANT_EFFECT_CHANCE;
    private static final ModConfigSpec.IntValue ENCHANT_EFFECT_DURATION;
    private static final ModConfigSpec.BooleanValue GOLDEN_ARMOR_CALMS_PIGLINS;
    private static final ModConfigSpec.BooleanValue SEATS;
    private static final ModConfigSpec.BooleanValue SEATS_NEED_EMPTY_HAND;
    private static final ModConfigSpec.DoubleValue INSTRUMENT_VOLUME;
    private static final ModConfigSpec.BooleanValue SIGN_EDITOR_ON_PLACE;
    private static final ModConfigSpec.BooleanValue ICON_RESET;
    private static final String KEY = VariedCommodities.MODID + ".configuration.";
    private static final ModConfigSpec SPEC;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        MACHINE_GUN_DAMAGE = builder.comment("Damage from Machine Gun")
                .translation(KEY + "machine_gun_damage")
                .defineInRange("machine_gun_damage", VCConfig.DEFAULT_MACHINE_GUN_DAMAGE, 0, Integer.MAX_VALUE);
        MUSKET_DAMAGE = builder.comment("Damage from Musket")
                .translation(KEY + "musket_damage")
                .defineInRange("musket_damage", VCConfig.DEFAULT_MUSKET_DAMAGE, 0, Integer.MAX_VALUE);
        MUSKET_KNOCKBACK = builder.comment("Bonus knockback from Musket")
                .translation(KEY + "musket_knockback")
                .defineInRange("musket_knockback", VCConfig.DEFAULT_MUSKET_KNOCKBACK, 0, Integer.MAX_VALUE);
        HEAVY_CROSSBOW_DAMAGE = builder.comment("Damage from Heavy Crossbow")
                .translation(KEY + "heavy_crossbow_damage")
                .defineInRange("heavy_crossbow_damage", VCConfig.DEFAULT_HEAVY_CROSSBOW_DAMAGE, 0, Integer.MAX_VALUE);
        GUN_DAMAGE_MULTIPLIER = builder.comment("Damage multiplier for guns, except the Machine Gun and Musket")
                .translation(KEY + "gun_damage_multiplier")
                .defineInRange("gun_damage_multiplier", VCConfig.DEFAULT_GUN_DAMAGE_MULTIPLIER, 0.0D, Double.MAX_VALUE);
        STAFF_DAMAGE_MULTIPLIER = builder.comment("Damage multiplier for magic staffs")
                .translation(KEY + "staff_damage_multiplier")
                .defineInRange("staff_damage_multiplier", VCConfig.DEFAULT_STAFF_DAMAGE_MULTIPLIER, 0.0D, Double.MAX_VALUE);
        SLINGSHOT_DAMAGE = builder.comment("Damage from Slingshot")
                .translation(KEY + "slingshot_damage")
                .defineInRange("slingshot_damage", VCConfig.DEFAULT_SLINGSHOT_DAMAGE, 0, Integer.MAX_VALUE);
        SHURIKEN_DAMAGE = builder.comment("Damage from Shuriken")
                .translation(KEY + "shuriken_damage")
                .defineInRange("shuriken_damage", VCConfig.DEFAULT_SHURIKEN_DAMAGE, 0, Integer.MAX_VALUE);
        GIANT_SHURIKEN_DAMAGE = builder.comment("Damage from Giant Shuriken")
                .translation(KEY + "giant_shuriken_damage")
                .defineInRange("giant_shuriken_damage", VCConfig.DEFAULT_GIANT_SHURIKEN_DAMAGE, 0, Integer.MAX_VALUE);
        SHURIKENS_BREAK = builder.comment("Shurikens break when they hit a mob")
                .translation(KEY + "shurikens_break")
                .define("shurikens_break", VCConfig.DEFAULT_SHURIKENS_BREAK);
        HOLY_HAND_GRENADE_STRENGTH = builder.comment("Holy Hand Grenade Explosion Strength")
                .translation(KEY + "holy_hand_grenade_strength")
                .defineInRange("holy_hand_grenade_strength", VCConfig.DEFAULT_HOLY_HAND_GRENADE_STRENGTH, 0.0D, Double.MAX_VALUE);
        HOLY_HAND_GRENADE_BREAKS_BLOCKS = builder.comment("Holy Hand Grenade breaks blocks")
                .translation(KEY + "holy_hand_grenade_breaks_blocks")
                .define("holy_hand_grenade_breaks_blocks", VCConfig.DEFAULT_HOLY_HAND_GRENADE_BREAKS_BLOCKS);
        HOLY_HAND_GRENADE_FUSE = builder.comment("How long in ticks before a Holy Hand Grenade explodes when landing")
                .translation(KEY + "holy_hand_grenade_fuse")
                .defineInRange("holy_hand_grenade_fuse", VCConfig.DEFAULT_HOLY_HAND_GRENADE_FUSE, 1, Integer.MAX_VALUE);
        MACHINE_GUN_CLIP_SIZE = builder.comment("Shots in a full Machine Gun clip")
                .translation(KEY + "machine_gun_clip_size")
                .defineInRange("machine_gun_clip_size", VCConfig.DEFAULT_MACHINE_GUN_CLIP_SIZE, 2, Integer.MAX_VALUE);
        MACHINE_GUN_FIRE_DELAY = builder.comment("Ticks between Machine Gun shots")
                .translation(KEY + "machine_gun_fire_delay")
                .defineInRange("machine_gun_fire_delay", VCConfig.DEFAULT_MACHINE_GUN_FIRE_DELAY, 1, Integer.MAX_VALUE);
        MUSKET_RELOAD_TIME = builder.comment("Ticks it takes to reload the Musket")
                .translation(KEY + "musket_reload_time")
                .defineInRange("musket_reload_time", VCConfig.DEFAULT_MUSKET_RELOAD_TIME, 1, Integer.MAX_VALUE);
        HEAVY_CROSSBOW_RELOAD_TIME = builder.comment("Ticks it takes to reload the Heavy Crossbow")
                .translation(KEY + "heavy_crossbow_reload_time")
                .defineInRange("heavy_crossbow_reload_time", VCConfig.DEFAULT_HEAVY_CROSSBOW_RELOAD_TIME, 1, Integer.MAX_VALUE);
        HEAVY_CROSSBOW_BOLT_PICKUP = builder.comment("Bolts shot from the Heavy Crossbow can be picked up")
                .translation(KEY + "heavy_crossbow_bolt_pickup")
                .define("heavy_crossbow_bolt_pickup", VCConfig.DEFAULT_HEAVY_CROSSBOW_BOLT_PICKUP);
        GUNS_USE_BULLETS = builder.comment("Guns need Bullets to fire")
                .translation(KEY + "guns_use_bullets")
                .define("guns_use_bullets", VCConfig.DEFAULT_GUNS_USE_BULLETS);
        STAFFS_USE_MANA = builder.comment("Magic Staffs need Mana")
                .translation(KEY + "staffs_use_mana")
                .define("staffs_use_mana", VCConfig.DEFAULT_STAFFS_USE_MANA);
        STAFF_CHARGE_TIME_MULTIPLIER = builder.comment("Charge time multiplier for magic staffs")
                .translation(KEY + "staff_charge_time_multiplier")
                .defineInRange("staff_charge_time_multiplier", VCConfig.DEFAULT_STAFF_CHARGE_TIME_MULTIPLIER, 0.0D, Double.MAX_VALUE);
        STAFF_SPELL_EXPLOSION = builder.comment("Staff spells explode on impact")
                .translation(KEY + "staff_spell_explosion")
                .define("staff_spell_explosion", VCConfig.DEFAULT_STAFF_SPELL_EXPLOSION);
        SLINGSHOT_KNOCKBACK = builder.comment("Knockback from a Slingshot which is doubled at full charge")
                .translation(KEY + "slingshot_knockback")
                .defineInRange("slingshot_knockback", VCConfig.DEFAULT_SLINGSHOT_KNOCKBACK, 0, Integer.MAX_VALUE);
        THROWN_KUNAI_DAMAGE = builder.comment("Damage from a Kunai projectile")
                .translation(KEY + "thrown_kunai_damage")
                .defineInRange("thrown_kunai_damage", VCConfig.DEFAULT_THROWN_KUNAI_DAMAGE, 0, Integer.MAX_VALUE);
        THROWN_WEAPONS_DESPAWN = builder.comment("Thrown kunai and shurikens despawn after a minute on the ground")
                .translation(KEY + "thrown_weapons_despawn")
                .define("thrown_weapons_despawn", VCConfig.DEFAULT_THROWN_WEAPONS_DESPAWN);
        ENCHANT_EFFECT_CHANCE = builder.comment("Percent chance per level for the Poison and Confusion enchantments to apply")
                .translation(KEY + "enchant_effect_chance")
                .defineInRange("enchant_effect_chance", VCConfig.DEFAULT_ENCHANT_EFFECT_CHANCE, 0, 100);
        ENCHANT_EFFECT_DURATION = builder.comment("How long in ticks the Poison and Confusion enchantment effects last")
                .translation(KEY + "enchant_effect_duration")
                .defineInRange("enchant_effect_duration", VCConfig.DEFAULT_ENCHANT_EFFECT_DURATION, 1, Integer.MAX_VALUE);
        GOLDEN_ARMOR_CALMS_PIGLINS = builder.comment("Golden armor calms piglins")
                .translation(KEY + "golden_armor_calms_piglins")
                .define("golden_armor_calms_piglins", VCConfig.DEFAULT_GOLDEN_ARMOR_CALMS_PIGLINS);
        SEATS = builder.comment("Players can sit on chairs, stools and couches")
                .translation(KEY + "seats")
                .define("seats", VCConfig.DEFAULT_SEATS);
        SEATS_NEED_EMPTY_HAND = builder.comment("Players need an empty hand to sit")
                .translation(KEY + "seats_need_empty_hand")
                .define("seats_need_empty_hand", VCConfig.DEFAULT_SEATS_NEED_EMPTY_HAND);
        INSTRUMENT_VOLUME = builder.comment("Volume of musical instruments")
                .translation(KEY + "instrument_volume")
                .defineInRange("instrument_volume", VCConfig.DEFAULT_INSTRUMENT_VOLUME, 0.0D, Double.MAX_VALUE);
        SIGN_EDITOR_ON_PLACE = builder.comment("Open the editor when placing a Big Sign or tombstone")
                .translation(KEY + "sign_editor_on_place")
                .define("sign_editor_on_place", VCConfig.DEFAULT_SIGN_EDITOR_ON_PLACE);
        ICON_RESET = builder.comment("Shift right-click a banner or shop sign with an empty hand to remove its icon")
                .translation(KEY + "icon_reset")
                .define("icon_reset", VCConfig.DEFAULT_ICON_RESET);
        SPEC = builder.build();
    }

    static void register(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, SPEC);
        modBus.addListener((ModConfigEvent.Loading e) -> sync());
        modBus.addListener((ModConfigEvent.Reloading e) -> sync());
    }

    private static void sync() {
        if (!SPEC.isLoaded()) return;
        VCConfig.machineGunDamage = MACHINE_GUN_DAMAGE.get();
        VCConfig.musketDamage = MUSKET_DAMAGE.get();
        VCConfig.musketKnockback = MUSKET_KNOCKBACK.get();
        VCConfig.heavyCrossbowDamage = HEAVY_CROSSBOW_DAMAGE.get();
        VCConfig.gunDamageMultiplier = GUN_DAMAGE_MULTIPLIER.get();
        VCConfig.staffDamageMultiplier = STAFF_DAMAGE_MULTIPLIER.get();
        VCConfig.slingshotDamage = SLINGSHOT_DAMAGE.get();
        VCConfig.shurikenDamage = SHURIKEN_DAMAGE.get();
        VCConfig.giantShurikenDamage = GIANT_SHURIKEN_DAMAGE.get();
        VCConfig.shurikensBreak = SHURIKENS_BREAK.get();
        VCConfig.holyHandGrenadeStrength = HOLY_HAND_GRENADE_STRENGTH.get();
        VCConfig.holyHandGrenadeBreaksBlocks = HOLY_HAND_GRENADE_BREAKS_BLOCKS.get();
        VCConfig.holyHandGrenadeFuse = HOLY_HAND_GRENADE_FUSE.get();
        VCConfig.machineGunClipSize = MACHINE_GUN_CLIP_SIZE.get();
        VCConfig.machineGunFireDelay = MACHINE_GUN_FIRE_DELAY.get();
        VCConfig.musketReloadTime = MUSKET_RELOAD_TIME.get();
        VCConfig.heavyCrossbowReloadTime = HEAVY_CROSSBOW_RELOAD_TIME.get();
        VCConfig.heavyCrossbowBoltPickup = HEAVY_CROSSBOW_BOLT_PICKUP.get();
        VCConfig.gunsUseBullets = GUNS_USE_BULLETS.get();
        VCConfig.staffsUseMana = STAFFS_USE_MANA.get();
        VCConfig.staffChargeTimeMultiplier = STAFF_CHARGE_TIME_MULTIPLIER.get();
        VCConfig.staffSpellExplosion = STAFF_SPELL_EXPLOSION.get();
        VCConfig.slingshotKnockback = SLINGSHOT_KNOCKBACK.get();
        VCConfig.thrownKunaiDamage = THROWN_KUNAI_DAMAGE.get();
        VCConfig.thrownWeaponsDespawn = THROWN_WEAPONS_DESPAWN.get();
        VCConfig.enchantEffectChance = ENCHANT_EFFECT_CHANCE.get();
        VCConfig.enchantEffectDuration = ENCHANT_EFFECT_DURATION.get();
        VCConfig.goldenArmorCalmsPiglins = GOLDEN_ARMOR_CALMS_PIGLINS.get();
        VCConfig.seats = SEATS.get();
        VCConfig.seatsNeedEmptyHand = SEATS_NEED_EMPTY_HAND.get();
        VCConfig.instrumentVolume = INSTRUMENT_VOLUME.get();
        VCConfig.signEditorOnPlace = SIGN_EDITOR_ON_PLACE.get();
        VCConfig.iconReset = ICON_RESET.get();
    }

    private NeoForgeConfig() {
    }
}
