package com.sxilverr.variedcommoditiesredux.entity;

import com.sxilverr.variedcommoditiesredux.VCEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MagicProjectile extends VCProjectile {
    private LivingEntity caster;
    private ItemStack equipped = ItemStack.EMPTY;

    public MagicProjectile(EntityType<? extends MagicProjectile> type, Level level) {
        super(type, level);
    }

    public MagicProjectile(Level level, LivingEntity caster, ItemStack item) {
        super(VCEntities.MAGIC_PROJECTILE.get(), level, caster, item);
        this.caster = caster;
        this.equipped = caster.getMainHandItem();
    }

    @Override
    public void tick() {
        if (!level().isClientSide && (caster == null || caster.getMainHandItem() != equipped)) {
            discard();
            return;
        }
        super.tick();
    }
}
