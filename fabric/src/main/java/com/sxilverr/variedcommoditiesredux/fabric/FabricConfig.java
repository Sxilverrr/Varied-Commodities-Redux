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
        public boolean golden_armor_calms_piglins = VCConfig.DEFAULT_GOLDEN_ARMOR_CALMS_PIGLINS;
        public boolean seats = VCConfig.DEFAULT_SEATS;
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
        VCConfig.goldenArmorCalmsPiglins = data.golden_armor_calms_piglins;
        VCConfig.seats = data.seats;
        VCConfig.signEditorOnPlace = data.sign_editor_on_place;
        VCConfig.iconReset = data.icon_reset;
    }

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve(VariedCommodities.MODID + ".json");
    }

    private FabricConfig() {
    }
}
