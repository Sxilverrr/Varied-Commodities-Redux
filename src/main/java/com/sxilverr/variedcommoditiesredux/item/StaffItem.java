package com.sxilverr.variedcommoditiesredux.item;

import com.sxilverr.variedcommoditiesredux.VCConfig;
import com.sxilverr.variedcommoditiesredux.VCEnchants;
import com.sxilverr.variedcommoditiesredux.VCItems;
import com.sxilverr.variedcommoditiesredux.VCSounds;
import com.sxilverr.variedcommoditiesredux.client.VCClientHooks;
import com.sxilverr.variedcommoditiesredux.entity.MagicProjectile;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Supplier;

public class StaffItem extends ChargeItem {
    private static final Map<LivingEntity, MagicProjectile> CHARGED = new WeakHashMap<>();

    private final VCMaterial material;
    private final Supplier<ItemStack> spell;
    private final int[] colors;

    public StaffItem(VCMaterial material, Supplier<ItemStack> spell, int[] colors, Properties properties) {
        super(properties.stacksTo(1));
        this.material = material;
        this.spell = spell;
        this.colors = colors;
    }

    public static StaffItem elemental(DyeColor color, Properties properties) {
        return new StaffItem(VCMaterial.IRON, () -> new ItemStack(VCItems.ORBS.get(color).get()), new int[]{15 - color.getId()}, properties);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remaining) {
        int tick = usedTicks(stack, entity, remaining);
        if (level.isClientSide) {
            for (int color : colors) VCClientHooks.spellParticles(entity, color, colors.length == 1 ? 4 : 2);
            return;
        }
        int chargeTime = 20 + material.harvestLevel() * 8;
        if (tick == chargeTime) {
            if (entity instanceof Player player && !player.getAbilities().instabuild && !VCEnchants.infinity(level, stack)) {
                if (!ItemUtil.consume(player, VCItems.MANA.get())) return;
            }
            ItemUtil.playSound(entity, VCSounds.MAGIC_CHARGE.get(), 1.0F, 1.0F);
            int damage = 6 + material.damage() + level.random.nextInt(4);
            damage = (int) (damage + damage * VCEnchants.damage(level, stack) * 0.5F);
            MagicProjectile projectile = new MagicProjectile(level, entity, spell.get());
            projectile.damage = (float) (damage * VCConfig.staffDamageMultiplier);
            projectile.setSpeed(25);
            hold(projectile, entity);
            level.addFreshEntity(projectile);
            CHARGED.put(entity, projectile);
        } else if (tick > chargeTime) {
            MagicProjectile projectile = CHARGED.get(entity);
            if (projectile == null || projectile.isRemoved()) return;
            projectile.ticksInAir = 0;
            hold(projectile, entity);
        }
    }

    private static void hold(MagicProjectile projectile, LivingEntity entity) {
        float yaw = entity.getYRot() * Mth.DEG_TO_RAD;
        float pitch = entity.getXRot() * Mth.DEG_TO_RAD;
        double dx = -Mth.sin(yaw) * Mth.cos(pitch);
        double dz = Mth.cos(yaw) * Mth.cos(pitch);
        projectile.setPos(entity.getX() + dx * 0.8, entity.getY() + 1.5 - entity.getXRot() / 80.0F, entity.getZ() + dz * 0.8);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int remaining) {
        if (level.isClientSide) return;
        MagicProjectile projectile = CHARGED.remove(entity);
        if (projectile == null || projectile.isRemoved()) return;
        ItemStack staff = stack.copy();
        projectile.callback = (p, pos, hit) -> {
            if (hit instanceof LivingEntity living) VCEnchants.applyEffects(level, staff, living);
            return false;
        };
        projectile.explosiveDamage = false;
        projectile.explosiveRadius = 1;
        projectile.setYRot(entity.getYRot());
        projectile.setXRot(entity.getXRot());
        projectile.yRotO = entity.getYRot();
        projectile.xRotO = entity.getXRot();
        projectile.shoot(2.0F);
        ItemUtil.playSound(entity, VCSounds.MAGIC_SHOT.get(), 1.0F, 1.0F);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return material.enchantability();
    }
}
