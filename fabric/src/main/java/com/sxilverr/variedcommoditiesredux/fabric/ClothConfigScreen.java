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
        general.addEntry(eb.startBooleanToggle(Component.translatable(KEY + "golden_armor_calms_piglins"), data.golden_armor_calms_piglins)
                .setDefaultValue(VCConfig.DEFAULT_GOLDEN_ARMOR_CALMS_PIGLINS)
                .setTooltip(Component.translatable(KEY + "golden_armor_calms_piglins.tooltip"))
                .setSaveConsumer(v -> data.golden_armor_calms_piglins = v).build());
        general.addEntry(eb.startBooleanToggle(Component.translatable(KEY + "seats"), data.seats)
                .setDefaultValue(VCConfig.DEFAULT_SEATS)
                .setTooltip(Component.translatable(KEY + "seats.tooltip"))
                .setSaveConsumer(v -> data.seats = v).build());
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
