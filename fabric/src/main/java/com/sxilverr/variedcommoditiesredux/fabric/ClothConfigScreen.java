package com.sxilverr.variedcommoditiesredux.fabric;

import com.sxilverr.variedcommoditiesredux.VCConfig;
import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class ClothConfigScreen {
    private static final String KEY = VariedCommodities.MODID + ".configuration.";

    public static Screen create(Screen parent) {
        FabricConfig.Data data = FabricConfig.data();
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable(KEY + "title"))
                .setSavingRunnable(FabricConfig::persist);
        ConfigEntryBuilder eb = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(Component.translatable(KEY + "title"));
        general.addEntry(eb.startIntField(Component.translatable(KEY + "machine_gun_damage"), data.machine_gun_damage)
                .setMin(0)
                .setDefaultValue(VCConfig.DEFAULT_MACHINE_GUN_DAMAGE)
                .setTooltip(Component.translatable(KEY + "machine_gun_damage.tooltip"))
                .setSaveConsumer(v -> data.machine_gun_damage = v).build());
        general.addEntry(eb.startIntField(Component.translatable(KEY + "musket_damage"), data.musket_damage)
                .setMin(0)
                .setDefaultValue(VCConfig.DEFAULT_MUSKET_DAMAGE)
                .setTooltip(Component.translatable(KEY + "musket_damage.tooltip"))
                .setSaveConsumer(v -> data.musket_damage = v).build());
        general.addEntry(eb.startIntField(Component.translatable(KEY + "musket_knockback"), data.musket_knockback)
                .setMin(0)
                .setDefaultValue(VCConfig.DEFAULT_MUSKET_KNOCKBACK)
                .setTooltip(Component.translatable(KEY + "musket_knockback.tooltip"))
                .setSaveConsumer(v -> data.musket_knockback = v).build());
        general.addEntry(eb.startIntField(Component.translatable(KEY + "heavy_crossbow_damage"), data.heavy_crossbow_damage)
                .setMin(0)
                .setDefaultValue(VCConfig.DEFAULT_HEAVY_CROSSBOW_DAMAGE)
                .setTooltip(Component.translatable(KEY + "heavy_crossbow_damage.tooltip"))
                .setSaveConsumer(v -> data.heavy_crossbow_damage = v).build());
        general.addEntry(eb.startDoubleField(Component.translatable(KEY + "gun_damage_multiplier"), data.gun_damage_multiplier)
                .setMin(0.0D)
                .setDefaultValue(VCConfig.DEFAULT_GUN_DAMAGE_MULTIPLIER)
                .setTooltip(Component.translatable(KEY + "gun_damage_multiplier.tooltip"))
                .setSaveConsumer(v -> data.gun_damage_multiplier = v).build());
        general.addEntry(eb.startDoubleField(Component.translatable(KEY + "staff_damage_multiplier"), data.staff_damage_multiplier)
                .setMin(0.0D)
                .setDefaultValue(VCConfig.DEFAULT_STAFF_DAMAGE_MULTIPLIER)
                .setTooltip(Component.translatable(KEY + "staff_damage_multiplier.tooltip"))
                .setSaveConsumer(v -> data.staff_damage_multiplier = v).build());
        general.addEntry(eb.startIntField(Component.translatable(KEY + "slingshot_damage"), data.slingshot_damage)
                .setMin(0)
                .setDefaultValue(VCConfig.DEFAULT_SLINGSHOT_DAMAGE)
                .setTooltip(Component.translatable(KEY + "slingshot_damage.tooltip"))
                .setSaveConsumer(v -> data.slingshot_damage = v).build());
        general.addEntry(eb.startIntField(Component.translatable(KEY + "shuriken_damage"), data.shuriken_damage)
                .setMin(0)
                .setDefaultValue(VCConfig.DEFAULT_SHURIKEN_DAMAGE)
                .setTooltip(Component.translatable(KEY + "shuriken_damage.tooltip"))
                .setSaveConsumer(v -> data.shuriken_damage = v).build());
        general.addEntry(eb.startIntField(Component.translatable(KEY + "giant_shuriken_damage"), data.giant_shuriken_damage)
                .setMin(0)
                .setDefaultValue(VCConfig.DEFAULT_GIANT_SHURIKEN_DAMAGE)
                .setTooltip(Component.translatable(KEY + "giant_shuriken_damage.tooltip"))
                .setSaveConsumer(v -> data.giant_shuriken_damage = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable(KEY + "shurikens_break"), data.shurikens_break)
                .setDefaultValue(VCConfig.DEFAULT_SHURIKENS_BREAK)
                .setTooltip(Component.translatable(KEY + "shurikens_break.tooltip"))
                .setSaveConsumer(v -> data.shurikens_break = v).build());
        general.addEntry(eb.startDoubleField(Component.translatable(KEY + "holy_hand_grenade_strength"), data.holy_hand_grenade_strength)
                .setMin(0.0D)
                .setDefaultValue(VCConfig.DEFAULT_HOLY_HAND_GRENADE_STRENGTH)
                .setTooltip(Component.translatable(KEY + "holy_hand_grenade_strength.tooltip"))
                .setSaveConsumer(v -> data.holy_hand_grenade_strength = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable(KEY + "holy_hand_grenade_breaks_blocks"), data.holy_hand_grenade_breaks_blocks)
                .setDefaultValue(VCConfig.DEFAULT_HOLY_HAND_GRENADE_BREAKS_BLOCKS)
                .setTooltip(Component.translatable(KEY + "holy_hand_grenade_breaks_blocks.tooltip"))
                .setSaveConsumer(v -> data.holy_hand_grenade_breaks_blocks = v).build());
        general.addEntry(eb.startIntField(Component.translatable(KEY + "holy_hand_grenade_fuse"), data.holy_hand_grenade_fuse)
                .setMin(1)
                .setDefaultValue(VCConfig.DEFAULT_HOLY_HAND_GRENADE_FUSE)
                .setTooltip(Component.translatable(KEY + "holy_hand_grenade_fuse.tooltip"))
                .setSaveConsumer(v -> data.holy_hand_grenade_fuse = v).build());
        general.addEntry(eb.startIntField(Component.translatable(KEY + "machine_gun_clip_size"), data.machine_gun_clip_size)
                .setMin(2)
                .setDefaultValue(VCConfig.DEFAULT_MACHINE_GUN_CLIP_SIZE)
                .setTooltip(Component.translatable(KEY + "machine_gun_clip_size.tooltip"))
                .setSaveConsumer(v -> data.machine_gun_clip_size = v).build());
        general.addEntry(eb.startIntField(Component.translatable(KEY + "machine_gun_fire_delay"), data.machine_gun_fire_delay)
                .setMin(1)
                .setDefaultValue(VCConfig.DEFAULT_MACHINE_GUN_FIRE_DELAY)
                .setTooltip(Component.translatable(KEY + "machine_gun_fire_delay.tooltip"))
                .setSaveConsumer(v -> data.machine_gun_fire_delay = v).build());
        general.addEntry(eb.startIntField(Component.translatable(KEY + "musket_reload_time"), data.musket_reload_time)
                .setMin(1)
                .setDefaultValue(VCConfig.DEFAULT_MUSKET_RELOAD_TIME)
                .setTooltip(Component.translatable(KEY + "musket_reload_time.tooltip"))
                .setSaveConsumer(v -> data.musket_reload_time = v).build());
        general.addEntry(eb.startIntField(Component.translatable(KEY + "heavy_crossbow_reload_time"), data.heavy_crossbow_reload_time)
                .setMin(1)
                .setDefaultValue(VCConfig.DEFAULT_HEAVY_CROSSBOW_RELOAD_TIME)
                .setTooltip(Component.translatable(KEY + "heavy_crossbow_reload_time.tooltip"))
                .setSaveConsumer(v -> data.heavy_crossbow_reload_time = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable(KEY + "heavy_crossbow_bolt_pickup"), data.heavy_crossbow_bolt_pickup)
                .setDefaultValue(VCConfig.DEFAULT_HEAVY_CROSSBOW_BOLT_PICKUP)
                .setTooltip(Component.translatable(KEY + "heavy_crossbow_bolt_pickup.tooltip"))
                .setSaveConsumer(v -> data.heavy_crossbow_bolt_pickup = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable(KEY + "guns_use_bullets"), data.guns_use_bullets)
                .setDefaultValue(VCConfig.DEFAULT_GUNS_USE_BULLETS)
                .setTooltip(Component.translatable(KEY + "guns_use_bullets.tooltip"))
                .setSaveConsumer(v -> data.guns_use_bullets = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable(KEY + "staffs_use_mana"), data.staffs_use_mana)
                .setDefaultValue(VCConfig.DEFAULT_STAFFS_USE_MANA)
                .setTooltip(Component.translatable(KEY + "staffs_use_mana.tooltip"))
                .setSaveConsumer(v -> data.staffs_use_mana = v).build());
        general.addEntry(eb.startDoubleField(Component.translatable(KEY + "staff_charge_time_multiplier"), data.staff_charge_time_multiplier)
                .setMin(0.0D)
                .setDefaultValue(VCConfig.DEFAULT_STAFF_CHARGE_TIME_MULTIPLIER)
                .setTooltip(Component.translatable(KEY + "staff_charge_time_multiplier.tooltip"))
                .setSaveConsumer(v -> data.staff_charge_time_multiplier = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable(KEY + "staff_spell_explosion"), data.staff_spell_explosion)
                .setDefaultValue(VCConfig.DEFAULT_STAFF_SPELL_EXPLOSION)
                .setTooltip(Component.translatable(KEY + "staff_spell_explosion.tooltip"))
                .setSaveConsumer(v -> data.staff_spell_explosion = v).build());
        general.addEntry(eb.startIntField(Component.translatable(KEY + "slingshot_knockback"), data.slingshot_knockback)
                .setMin(0)
                .setDefaultValue(VCConfig.DEFAULT_SLINGSHOT_KNOCKBACK)
                .setTooltip(Component.translatable(KEY + "slingshot_knockback.tooltip"))
                .setSaveConsumer(v -> data.slingshot_knockback = v).build());
        general.addEntry(eb.startIntField(Component.translatable(KEY + "thrown_kunai_damage"), data.thrown_kunai_damage)
                .setMin(0)
                .setDefaultValue(VCConfig.DEFAULT_THROWN_KUNAI_DAMAGE)
                .setTooltip(Component.translatable(KEY + "thrown_kunai_damage.tooltip"))
                .setSaveConsumer(v -> data.thrown_kunai_damage = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable(KEY + "thrown_weapons_despawn"), data.thrown_weapons_despawn)
                .setDefaultValue(VCConfig.DEFAULT_THROWN_WEAPONS_DESPAWN)
                .setTooltip(Component.translatable(KEY + "thrown_weapons_despawn.tooltip"))
                .setSaveConsumer(v -> data.thrown_weapons_despawn = v).build());
        general.addEntry(eb.startIntField(Component.translatable(KEY + "enchant_effect_chance"), data.enchant_effect_chance)
                .setMin(0)
                .setMax(100)
                .setDefaultValue(VCConfig.DEFAULT_ENCHANT_EFFECT_CHANCE)
                .setTooltip(Component.translatable(KEY + "enchant_effect_chance.tooltip"))
                .setSaveConsumer(v -> data.enchant_effect_chance = v).build());
        general.addEntry(eb.startIntField(Component.translatable(KEY + "enchant_effect_duration"), data.enchant_effect_duration)
                .setMin(1)
                .setDefaultValue(VCConfig.DEFAULT_ENCHANT_EFFECT_DURATION)
                .setTooltip(Component.translatable(KEY + "enchant_effect_duration.tooltip"))
                .setSaveConsumer(v -> data.enchant_effect_duration = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable(KEY + "golden_armor_calms_piglins"), data.golden_armor_calms_piglins)
                .setDefaultValue(VCConfig.DEFAULT_GOLDEN_ARMOR_CALMS_PIGLINS)
                .setTooltip(Component.translatable(KEY + "golden_armor_calms_piglins.tooltip"))
                .setSaveConsumer(v -> data.golden_armor_calms_piglins = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable(KEY + "seats"), data.seats)
                .setDefaultValue(VCConfig.DEFAULT_SEATS)
                .setTooltip(Component.translatable(KEY + "seats.tooltip"))
                .setSaveConsumer(v -> data.seats = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable(KEY + "seats_need_empty_hand"), data.seats_need_empty_hand)
                .setDefaultValue(VCConfig.DEFAULT_SEATS_NEED_EMPTY_HAND)
                .setTooltip(Component.translatable(KEY + "seats_need_empty_hand.tooltip"))
                .setSaveConsumer(v -> data.seats_need_empty_hand = v).build());
        general.addEntry(eb.startDoubleField(Component.translatable(KEY + "instrument_volume"), data.instrument_volume)
                .setMin(0.0D)
                .setDefaultValue(VCConfig.DEFAULT_INSTRUMENT_VOLUME)
                .setTooltip(Component.translatable(KEY + "instrument_volume.tooltip"))
                .setSaveConsumer(v -> data.instrument_volume = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable(KEY + "sign_editor_on_place"), data.sign_editor_on_place)
                .setDefaultValue(VCConfig.DEFAULT_SIGN_EDITOR_ON_PLACE)
                .setTooltip(Component.translatable(KEY + "sign_editor_on_place.tooltip"))
                .setSaveConsumer(v -> data.sign_editor_on_place = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable(KEY + "icon_reset"), data.icon_reset)
                .setDefaultValue(VCConfig.DEFAULT_ICON_RESET)
                .setTooltip(Component.translatable(KEY + "icon_reset.tooltip"))
                .setSaveConsumer(v -> data.icon_reset = v).build());
        return builder.build();
    }

    private ClothConfigScreen() {
    }
}
