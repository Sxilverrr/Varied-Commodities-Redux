package com.sxilverr.variedcommoditiesredux.entity;

import com.sxilverr.variedcommoditiesredux.Compat;
import com.sxilverr.variedcommoditiesredux.VCEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownWeapon extends AbstractArrow {
    private static final EntityDataAccessor<ItemStack> ITEM = SynchedEntityData.defineId(ThrownWeapon.class, EntityDataSerializers.ITEM_STACK);
    private float damage;
    private boolean returns;
    private boolean dealtDamage;

    public ThrownWeapon(EntityType<? extends ThrownWeapon> type, Level level) {
        super(type, level);
    }

    //? if >=1.21 {
    /*public ThrownWeapon(Level level, LivingEntity owner, ItemStack stack, float damage, boolean returns) {
        super(VCEntities.THROWN_WEAPON.get(), owner, level, stack.copy(), null);
        setup(stack, damage, returns);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ITEM, ItemStack.EMPTY);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return getItem().copy();
    }
    *///?} else {
    public ThrownWeapon(Level level, LivingEntity owner, ItemStack stack, float damage, boolean returns) {
        super(VCEntities.THROWN_WEAPON.get(), owner, level);
        setup(stack, damage, returns);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ITEM, ItemStack.EMPTY);
    }

    @Override
    protected ItemStack getPickupItem() {
        return getItem().copy();
    }
    //?}

    private void setup(ItemStack stack, float damage, boolean returns) {
        entityData.set(ITEM, stack.copy());
        this.damage = damage;
        this.returns = returns;
    }

    public ItemStack getItem() {
        return entityData.get(ITEM);
    }

    public boolean isStuck() {
        return inGround;
    }

    @Override
    protected EntityHitResult findHitEntity(Vec3 start, Vec3 end) {
        return dealtDamage ? null : super.findHitEntity(start, end);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        Entity owner = getOwner();
        if (target.hurt(damageSources().thrown(this, owner == null ? this : owner), damage)) {
            if (target instanceof EnderMan) return;
            if (target instanceof LivingEntity living && !level().isClientSide) living.setArrowCount(living.getArrowCount() + 1);
            playSound(SoundEvents.ARROW_HIT, 1.0F, 1.2F / (random.nextFloat() * 0.2F + 0.9F));
            if (!returns) {
                discard();
                return;
            }
            dealtDamage = true;
            setDeltaMovement(getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        } else {
            setDeltaMovement(getDeltaMovement().scale(-0.1));
            setYRot(getYRot() + 180.0F);
            yRotO += 180.0F;
        }
    }

    @Override
    protected void tickDespawn() {
        if (!returns || pickup != Pickup.ALLOWED) super.tickDespawn();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Weapon", Compat.saveStack(getItem(), level().registryAccess()));
        tag.putFloat("WeaponDamage", damage);
        tag.putBoolean("Returns", returns);
        tag.putBoolean("DealtDamage", dealtDamage);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(ITEM, Compat.loadStack(tag.getCompound("Weapon"), level().registryAccess()));
        damage = tag.getFloat("WeaponDamage");
        returns = tag.getBoolean("Returns");
        dealtDamage = tag.getBoolean("DealtDamage");
    }
}
