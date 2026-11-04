package com.sxilverr.variedcommoditiesredux.entity;

import com.sxilverr.variedcommoditiesredux.VCConfig;
import com.sxilverr.variedcommoditiesredux.VCEntities;
import com.sxilverr.variedcommoditiesredux.VCSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class HolyHandGrenade extends VCProjectile {
    public HolyHandGrenade(EntityType<? extends HolyHandGrenade> type, Level level) {
        super(type, level);
    }

    public HolyHandGrenade(Level level, LivingEntity thrower, ItemStack item) {
        super(VCEntities.HOLY_HAND_GRENADE.get(), level, thrower, item);
    }

    @Override
    protected boolean detectsEntities() {
        return false;
    }

    @Override
    protected float spin(float pitch) {
        return isRotating() ? getXRot() + getSpeed() * -60.0F : pitch;
    }

    @Override
    protected void impact(HitResult hit) {
        if (hit instanceof EntityHitResult) {
            setDeltaMovement(getDeltaMovement().scale(-0.1));
            setYRot(getYRot() + 180.0F);
            yRotO += 180.0F;
            ticksInAir = 0;
        } else if (hit instanceof BlockHitResult blockHit) {
            if (!level().isClientSide) {
                level().playSound(null, getX(), getY(), getZ(), VCSounds.HALLELUJAH.get(), SoundSource.NEUTRAL, 4.0F, 1.0F);
            }
            stick(blockHit);
        }
    }

    @Override
    protected void onGroundTimeout() {
        level().explode(this, getX(), getY(), getZ(), (float) VCConfig.holyHandGrenadeStrength, VCConfig.holyHandGrenadeBreaksBlocks ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);
        discard();
    }
}
