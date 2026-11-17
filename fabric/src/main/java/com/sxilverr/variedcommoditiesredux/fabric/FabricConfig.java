package com.sxilverr.variedcommoditiesredux.fabric;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.sxilverr.variedcommoditiesredux.VCConfig;
import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FabricConfig {
    public static final class Data {
        public int machine_gun_damage = VCConfig.DEFAULT_MACHINE_GUN_DAMAGE;
        public int musket_damage = VCConfig.DEFAULT_MUSKET_DAMAGE;
        public int musket_knockback = VCConfig.DEFAULT_MUSKET_KNOCKBACK;
        public int heavy_crossbow_damage = VCConfig.DEFAULT_HEAVY_CROSSBOW_DAMAGE;
        public double gun_damage_multiplier = VCConfig.DEFAULT_GUN_DAMAGE_MULTIPLIER;
        public double staff_damage_multiplier = VCConfig.DEFAULT_STAFF_DAMAGE_MULTIPLIER;
        public int slingshot_damage = VCConfig.DEFAULT_SLINGSHOT_DAMAGE;
        public int shuriken_damage = VCConfig.DEFAULT_SHURIKEN_DAMAGE;
        public int giant_shuriken_damage = VCConfig.DEFAULT_GIANT_SHURIKEN_DAMAGE;
        public boolean shurikens_break = VCConfig.DEFAULT_SHURIKENS_BREAK;
        public double holy_hand_grenade_strength = VCConfig.DEFAULT_HOLY_HAND_GRENADE_STRENGTH;
        public boolean holy_hand_grenade_breaks_blocks = VCConfig.DEFAULT_HOLY_HAND_GRENADE_BREAKS_BLOCKS;
        public int holy_hand_grenade_fuse = VCConfig.DEFAULT_HOLY_HAND_GRENADE_FUSE;
        public int machine_gun_clip_size = VCConfig.DEFAULT_MACHINE_GUN_CLIP_SIZE;
        public int machine_gun_fire_delay = VCConfig.DEFAULT_MACHINE_GUN_FIRE_DELAY;
        public int musket_reload_time = VCConfig.DEFAULT_MUSKET_RELOAD_TIME;
        public int heavy_crossbow_reload_time = VCConfig.DEFAULT_HEAVY_CROSSBOW_RELOAD_TIME;
        public boolean heavy_crossbow_bolt_pickup = VCConfig.DEFAULT_HEAVY_CROSSBOW_BOLT_PICKUP;
        public boolean guns_use_bullets = VCConfig.DEFAULT_GUNS_USE_BULLETS;
        public boolean staffs_use_mana = VCConfig.DEFAULT_STAFFS_USE_MANA;
        public double staff_charge_time_multiplier = VCConfig.DEFAULT_STAFF_CHARGE_TIME_MULTIPLIER;
        public boolean staff_spell_explosion = VCConfig.DEFAULT_STAFF_SPELL_EXPLOSION;
        public int slingshot_knockback = VCConfig.DEFAULT_SLINGSHOT_KNOCKBACK;
        public int thrown_kunai_damage = VCConfig.DEFAULT_THROWN_KUNAI_DAMAGE;
        public boolean thrown_weapons_despawn = VCConfig.DEFAULT_THROWN_WEAPONS_DESPAWN;
        public int enchant_effect_chance = VCConfig.DEFAULT_ENCHANT_EFFECT_CHANCE;
        public int enchant_effect_duration = VCConfig.DEFAULT_ENCHANT_EFFECT_DURATION;
        public boolean golden_armor_calms_piglins = VCConfig.DEFAULT_GOLDEN_ARMOR_CALMS_PIGLINS;
        public boolean seats = VCConfig.DEFAULT_SEATS;
        public boolean seats_need_empty_hand = VCConfig.DEFAULT_SEATS_NEED_EMPTY_HAND;
        public double instrument_volume = VCConfig.DEFAULT_INSTRUMENT_VOLUME;
        public boolean sign_editor_on_place = VCConfig.DEFAULT_SIGN_EDITOR_ON_PLACE;
        public boolean icon_reset = VCConfig.DEFAULT_ICON_RESET;
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Data data = new Data();

    public static Data data() {
        return data;
    }

    public static void load() {
        Path path = path();
        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                Data loaded = GSON.fromJson(reader, Data.class);
                if (loaded != null) {
                    data = loaded;
                }
            } catch (IOException | JsonParseException e) {
                apply();
                return;
            }
        }
        persist();
    }

    public static void persist() {
        apply();
        try (Writer writer = Files.newBufferedWriter(path())) {
            GSON.toJson(data, writer);
        } catch (IOException ignored) {
        }
    }

    private static void apply() {
        VCConfig.machineGunDamage = data.machine_gun_damage;
        VCConfig.musketDamage = data.musket_damage;
        VCConfig.musketKnockback = data.musket_knockback;
        VCConfig.heavyCrossbowDamage = data.heavy_crossbow_damage;
        VCConfig.gunDamageMultiplier = data.gun_damage_multiplier;
        VCConfig.staffDamageMultiplier = data.staff_damage_multiplier;
        VCConfig.slingshotDamage = data.slingshot_damage;
        VCConfig.shurikenDamage = data.shuriken_damage;
        VCConfig.giantShurikenDamage = data.giant_shuriken_damage;
        VCConfig.shurikensBreak = data.shurikens_break;
        VCConfig.holyHandGrenadeStrength = data.holy_hand_grenade_strength;
        VCConfig.holyHandGrenadeBreaksBlocks = data.holy_hand_grenade_breaks_blocks;
        VCConfig.holyHandGrenadeFuse = data.holy_hand_grenade_fuse;
        VCConfig.machineGunClipSize = data.machine_gun_clip_size;
        VCConfig.machineGunFireDelay = Math.max(1, data.machine_gun_fire_delay);
        VCConfig.musketReloadTime = data.musket_reload_time;
        VCConfig.heavyCrossbowReloadTime = data.heavy_crossbow_reload_time;
        VCConfig.heavyCrossbowBoltPickup = data.heavy_crossbow_bolt_pickup;
        VCConfig.gunsUseBullets = data.guns_use_bullets;
        VCConfig.staffsUseMana = data.staffs_use_mana;
        VCConfig.staffChargeTimeMultiplier = data.staff_charge_time_multiplier;
        VCConfig.staffSpellExplosion = data.staff_spell_explosion;
        VCConfig.slingshotKnockback = data.slingshot_knockback;
        VCConfig.thrownKunaiDamage = data.thrown_kunai_damage;
        VCConfig.thrownWeaponsDespawn = data.thrown_weapons_despawn;
        VCConfig.enchantEffectChance = data.enchant_effect_chance;
        VCConfig.enchantEffectDuration = data.enchant_effect_duration;
        VCConfig.goldenArmorCalmsPiglins = data.golden_armor_calms_piglins;
        VCConfig.seats = data.seats;
        VCConfig.seatsNeedEmptyHand = data.seats_need_empty_hand;
        VCConfig.instrumentVolume = data.instrument_volume;
        VCConfig.signEditorOnPlace = data.sign_editor_on_place;
        VCConfig.iconReset = data.icon_reset;
    }

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve(VariedCommodities.MODID + ".json");
    }

    private FabricConfig() {
    }
}
