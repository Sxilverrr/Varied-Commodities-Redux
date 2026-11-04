package com.sxilverr.variedcommoditiesredux.forge;

import com.sxilverr.variedcommoditiesredux.VCConfig;
import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class ForgeConfig {
    private static final ForgeConfigSpec.IntValue MACHINE_GUN_DAMAGE;
    private static final ForgeConfigSpec.IntValue MUSKET_DAMAGE;
    private static final ForgeConfigSpec.IntValue MUSKET_KNOCKBACK;
    private static final ForgeConfigSpec.IntValue HEAVY_CROSSBOW_DAMAGE;
    private static final ForgeConfigSpec.DoubleValue GUN_DAMAGE_MULTIPLIER;
    private static final ForgeConfigSpec.DoubleValue STAFF_DAMAGE_MULTIPLIER;
    private static final ForgeConfigSpec.IntValue SLINGSHOT_DAMAGE;
    private static final ForgeConfigSpec.IntValue SHURIKEN_DAMAGE;
    private static final ForgeConfigSpec.IntValue GIANT_SHURIKEN_DAMAGE;
    private static final ForgeConfigSpec.BooleanValue SHURIKENS_BREAK;
    private static final ForgeConfigSpec.DoubleValue HOLY_HAND_GRENADE_STRENGTH;
    private static final ForgeConfigSpec.BooleanValue HOLY_HAND_GRENADE_BREAKS_BLOCKS;
    private static final ForgeConfigSpec.BooleanValue GOLDEN_ARMOR_CALMS_PIGLINS;
    private static final ForgeConfigSpec.BooleanValue SEATS;
    private static final ForgeConfigSpec.BooleanValue SIGN_EDITOR_ON_PLACE;
    private static final ForgeConfigSpec.BooleanValue ICON_RESET;
    private static final String KEY = VariedCommodities.MODID + ".configuration.";
    private static final ForgeConfigSpec SPEC;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
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
        GOLDEN_ARMOR_CALMS_PIGLINS = builder.comment("Golden armor calms piglins")
                .translation(KEY + "golden_armor_calms_piglins")
                .define("golden_armor_calms_piglins", VCConfig.DEFAULT_GOLDEN_ARMOR_CALMS_PIGLINS);
        SEATS = builder.comment("Players can sit on chairs, stools and couches")
                .translation(KEY + "seats")
                .define("seats", VCConfig.DEFAULT_SEATS);
        SIGN_EDITOR_ON_PLACE = builder.comment("Open the editor when placing a Big Sign or tombstone")
                .translation(KEY + "sign_editor_on_place")
                .define("sign_editor_on_place", VCConfig.DEFAULT_SIGN_EDITOR_ON_PLACE);
        ICON_RESET = builder.comment("Shift right-click a banner or shop sign with an empty hand to remove its icon")
                .translation(KEY + "icon_reset")
                .define("icon_reset", VCConfig.DEFAULT_ICON_RESET);
        SPEC = builder.build();
    }

    static void register(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, SPEC);
        context.getModEventBus().addListener((ModConfigEvent.Loading e) -> sync());
        context.getModEventBus().addListener((ModConfigEvent.Reloading e) -> sync());
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
        VCConfig.goldenArmorCalmsPiglins = GOLDEN_ARMOR_CALMS_PIGLINS.get();
        VCConfig.seats = SEATS.get();
        VCConfig.signEditorOnPlace = SIGN_EDITOR_ON_PLACE.get();
        VCConfig.iconReset = ICON_RESET.get();
    }

    private ForgeConfig() {
    }
}
