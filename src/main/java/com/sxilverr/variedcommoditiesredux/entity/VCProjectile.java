package com.sxilverr.variedcommoditiesredux.entity;

import com.sxilverr.variedcommoditiesredux.Compat;
import com.sxilverr.variedcommoditiesredux.VCEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class VCProjectile extends ThrowableProjectile {
    private static final EntityDataAccessor<ItemStack> ITEM = SynchedEntityData.defineId(VCProjectile.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> VELOCITY = SynchedEntityData.defineId(VCProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> GRAVITY = SynchedEntityData.defineId(VCProjectile.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_3D = SynchedEntityData.defineId(VCProjectile.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ROTATING = SynchedEntityData.defineId(VCProjectile.class, EntityDataSerializers.BOOLEAN);

    public interface ImpactCallback {
        boolean onImpact(VCProjectile projectile, BlockPos pos, Entity entity);
    }

    public boolean inGround;
    protected int ticksInGround;
    public int ticksInAir;
    protected BlockPos stuckPos = BlockPos.ZERO;
    protected BlockState stuckState;
    public float damage = 5.0F;
    public int punch;
    public boolean accelerate;
    public boolean explosiveDamage = true;
    public int explosiveRadius;
    public int timeToLive = 1200;
    private Vec3 acceleration = Vec3.ZERO;
    public ImpactCallback callback;

    public VCProjectile(EntityType<? extends VCProjectile> type, Level level) {
        super(type, level);
    }

    public VCProjectile(Level level, LivingEntity shooter, ItemStack item) {
        this(VCEntities.PROJECTILE.get(), level, shooter, item);
    }

    protected VCProjectile(EntityType<? extends VCProjectile> type, Level level, LivingEntity shooter, ItemStack item) {
        super(type, level);
        setOwner(shooter);
        setItem(item);
        float yaw = shooter.getYRot();
        moveTo(shooter.getX() - Mth.cos(yaw * Mth.DEG_TO_RAD) * 0.1F, shooter.getEyeY() - 0.1F, shooter.getZ() - Mth.sin(yaw * Mth.DEG_TO_RAD) * 0.1F, yaw, shooter.getXRot());
    }

    //? if >=1.21 {
    /*@Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ITEM, ItemStack.EMPTY);
        builder.define(VELOCITY, 10);
        builder.define(GRAVITY, false);
        builder.define(IS_3D, false);
        builder.define(ROTATING, false);
    }
    *///?} else {
    @Override
    protected void defineSynchedData() {
        entityData.define(ITEM, ItemStack.EMPTY);
        entityData.define(VELOCITY, 10);
        entityData.define(GRAVITY, false);
        entityData.define(IS_3D, false);
        entityData.define(ROTATING, false);
    }
    //?}

    public void setItem(ItemStack item) {
        entityData.set(ITEM, item.copy());
    }

    public ItemStack getItem() {
        return entityData.get(ITEM);
    }

    public void setSpeed(int speed) {
        entityData.set(VELOCITY, speed);
    }

    public float getSpeed() {
        return entityData.get(VELOCITY) / 10.0F;
    }

    public void setHasGravity(boolean gravity) {
        entityData.set(GRAVITY, gravity);
    }

    public boolean hasGravity() {
        return entityData.get(GRAVITY);
    }

    public void setIs3D(boolean value) {
        entityData.set(IS_3D, value);
    }

    public boolean is3D() {
        return entityData.get(IS_3D) || isBlock();
    }

    public void setRotating(boolean value) {
        entityData.set(ROTATING, value);
    }

    public boolean isRotating() {
        return entityData.get(ROTATING);
    }

    public boolean isBlock() {
        return getItem().getItem() instanceof BlockItem;
    }

    public void shoot(float spread) {
        float yaw = getYRot() * Mth.DEG_TO_RAD;
        float pitch = getXRot() * Mth.DEG_TO_RAD;
        double x = -Mth.sin(yaw) * Mth.cos(pitch);
        double z = Mth.cos(yaw) * Mth.cos(pitch);
        double y = -Mth.sin(pitch);
        shootDirection(x, y, z, -getXRot(), spread);
    }

    private void shootDirection(double x, double y, double z, float gravityPitch, float spread) {
        double length = Math.sqrt(x * x + y * y + z * z);
        double horizontal = Math.sqrt(x * x + z * z);
        float yaw = (float) (Math.atan2(x, z) * Mth.RAD_TO_DEG);
        float pitch = hasGravity() ? gravityPitch : (float) (Math.atan2(y, horizontal) * Mth.RAD_TO_DEG);
        setYRot(yaw);
        setXRot(pitch);
        yRotO = yaw;
        xRotO = pitch;
        double mx = Mth.sin(yaw * Mth.DEG_TO_RAD) * Mth.cos(pitch * Mth.DEG_TO_RAD) + random.nextGaussian() * 0.0075F * spread;
        double mz = Mth.cos(yaw * Mth.DEG_TO_RAD) * Mth.cos(pitch * Mth.DEG_TO_RAD) + random.nextGaussian() * 0.0075F * spread;
        double my = Mth.sin((pitch + 1.0F) * Mth.DEG_TO_RAD) + random.nextGaussian() * 0.0075F * spread;
        float speed = getSpeed();
        setDeltaMovement(mx * speed, my * speed, mz * speed);
        acceleration = new Vec3(x / length * 0.1, y / length * 0.1, z / length * 0.1);
        ticksInGround = 0;
    }

    protected boolean detectsEntities() {
        return true;
    }

    protected void onGroundTimeout() {
        discard();
    }

    @Override
    public void tick() {
        baseTick();
        if (inGround) {
            if (stuckState != null && level().getBlockState(stuckPos) != stuckState) {
                inGround = false;
                setDeltaMovement(getDeltaMovement().multiply(random.nextFloat() * 0.2F, random.nextFloat() * 0.2F, random.nextFloat() * 0.2F));
                ticksInGround = 0;
                ticksInAir = 0;
            } else {
                ticksInGround++;
                if (ticksInGround >= timeToLive && !level().isClientSide) onGroundTimeout();
                return;
            }
        }
        ticksInAir++;
        if (ticksInAir >= timeToLive) {
            if (!level().isClientSide) discard();
            return;
        }
        Vec3 start = position();
        Vec3 motion = getDeltaMovement();
        Vec3 end = start.add(motion);
        HitResult hit = level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hit.getType() != HitResult.Type.MISS) end = hit.getLocation();
        if (!level().isClientSide && detectsEntities()) {
            EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(level(), this, start, end, getBoundingBox().expandTowards(motion).inflate(1.0), this::canHitEntity);
            if (entityHit != null) hit = entityHit;
        }
        if (hit.getType() != HitResult.Type.MISS) {
            entityData.set(ROTATING, false);
            impact(hit);
        }
        if (isRemoved()) return;
        motion = getDeltaMovement();
        double x = getX() + motion.x;
        double y = getY() + motion.y;
        double z = getZ() + motion.z;
        double horizontal = motion.horizontalDistance();
        float yaw = (float) (Mth.atan2(motion.x, motion.z) * Mth.RAD_TO_DEG);
        float pitch = (float) (Mth.atan2(motion.y, horizontal) * Mth.RAD_TO_DEG);
        while (pitch - xRotO < -180.0F) xRotO -= 360.0F;
        while (pitch - xRotO >= 180.0F) xRotO += 360.0F;
        while (yaw - yRotO < -180.0F) yRotO -= 360.0F;
        while (yaw - yRotO >= 180.0F) yRotO += 360.0F;
        setYRot(yaw);
        setXRot(spin(pitch));
        float factor = accelerate ? 0.95F : 1.0F;
        if (isInWater()) {
            if (level().isClientSide) {
                for (int i = 0; i < 4; i++) {
                    level().addParticle(ParticleTypes.BUBBLE, x - motion.x * 0.25, y - motion.y * 0.25, z - motion.z * 0.25, motion.x, motion.y, motion.z);
                }
            }
            factor = 0.8F;
        }
        motion = motion.scale(factor);
        if (hasGravity()) motion = motion.subtract(0.0, 0.03, 0.0);
        if (accelerate) motion = motion.add(acceleration);
        setDeltaMovement(motion);
        setPos(x, y, z);
        checkInsideBlocks();
    }

    protected float spin(float pitch) {
        if (!isRotating()) return pitch;
        int spin = isBlock() ? 10 : 20;
        return pitch - ticksInAir % 15 * spin * getSpeed();
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if (entity.isSpectator() || !entity.isPickable()) return false;
        Entity owner = getOwner();
        if (entity == owner && ticksInAir < 25) return false;
        if (entity instanceof Player target) {
            if (target.getAbilities().invulnerable) return false;
            if (owner instanceof Player shooter && !shooter.canHarmPlayer(target)) return false;
        }
        return true;
    }

    protected void stick(BlockHitResult hit) {
        stuckPos = hit.getBlockPos();
        stuckState = level().getBlockState(stuckPos);
        Vec3 motion = hit.getLocation().subtract(position());
        setDeltaMovement(motion);
        Vec3 offset = motion.normalize().scale(0.05);
        setPos(getX() - offset.x, getY() - offset.y, getZ() - offset.z);
        inGround = true;
        if (!hasGravity()) setHasGravity(true);
    }

    protected void impact(HitResult hit) {
        Entity hitEntity = hit instanceof EntityHitResult e ? e.getEntity() : null;
        if (callback != null) {
            BlockPos pos = hitEntity != null ? hitEntity.blockPosition() : BlockPos.containing(hit.getLocation());
            if (callback.onImpact(this, pos, hitEntity)) {
                inGround = true;
                setDeltaMovement(Vec3.ZERO);
                if (hit instanceof BlockHitResult blockHit) {
                    stuckPos = blockHit.getBlockPos();
                    stuckState = level().getBlockState(stuckPos);
                }
                return;
            }
        }
        if (hitEntity != null) {
            float amount = damage == 0.0F ? 0.001F : damage;
            if (hitEntity.hurt(damageSources().thrown(this, getOwner()), amount)) {
                if (!(hitEntity instanceof EnderMan)) discard();
                breakParticles(hitEntity.blockPosition());
                if (punch > 0) {
                    Vec3 motion = getDeltaMovement();
                    double horizontal = motion.horizontalDistance();
                    if (horizontal > 0.0) {
                        hitEntity.push(motion.x * punch * 0.6 / horizontal, 0.1, motion.z * punch * 0.6 / horizontal);
                    }
                }
            }
        } else {
            breakParticles(blockPosition());
        }
        if (explosiveRadius > 0 && !level().isClientSide) {
            Entity owner = getOwner();
            level().explode(owner == null ? this : owner, getX(), getY(), getZ(), explosiveRadius,
                    explosiveDamage ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);
            discard();
        }
        if (!level().isClientSide) discard();
    }

    private void breakParticles(BlockPos pos) {
        if (!(level() instanceof ServerLevel server)) return;
        ItemStack item = getItem();
        if (item.isEmpty()) return;
        if (item.getItem() instanceof BlockItem block) {
            server.levelEvent(2001, pos, Block.getId(block.getBlock().defaultBlockState()));
        } else {
            server.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, item), getX(), getY(), getZ(), 8, 0.15, 0.2, 0.15, 0.05);
        }
    }

    @Override
    public Component getName() {
        ItemStack item = getItem();
        return item.isEmpty() ? super.getName() : item.getHoverName();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Item", Compat.saveStack(getItem(), level().registryAccess()));
        tag.putBoolean("InGround", inGround);
        tag.putInt("StuckX", stuckPos.getX());
        tag.putInt("StuckY", stuckPos.getY());
        tag.putInt("StuckZ", stuckPos.getZ());
        tag.putFloat("Damage", damage);
        tag.putInt("Punch", punch);
        tag.putInt("ExplosiveRadius", explosiveRadius);
        tag.putBoolean("ExplosiveDamage", explosiveDamage);
        tag.putBoolean("Accelerate", accelerate);
        tag.putInt("TimeToLive", timeToLive);
        tag.putInt("Velocity", entityData.get(VELOCITY));
        tag.putBoolean("Gravity", hasGravity());
        tag.putBoolean("Render3D", entityData.get(IS_3D));
        tag.putBoolean("Spins", isRotating());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        ItemStack item = Compat.loadStack(tag.getCompound("Item"), level().registryAccess());
        if (item.isEmpty()) {
            discard();
            return;
        }
        setItem(item);
        inGround = tag.getBoolean("InGround");
        stuckPos = new BlockPos(tag.getInt("StuckX"), tag.getInt("StuckY"), tag.getInt("StuckZ"));
        if (inGround) stuckState = level().getBlockState(stuckPos);
        damage = tag.getFloat("Damage");
        punch = tag.getInt("Punch");
        explosiveRadius = tag.getInt("ExplosiveRadius");
        explosiveDamage = tag.getBoolean("ExplosiveDamage");
        accelerate = tag.getBoolean("Accelerate");
        timeToLive = tag.contains("TimeToLive") ? tag.getInt("TimeToLive") : 1200;
        entityData.set(VELOCITY, tag.getInt("Velocity"));
        setHasGravity(tag.getBoolean("Gravity"));
        setIs3D(tag.getBoolean("Render3D"));
        setRotating(tag.getBoolean("Spins"));
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double size = getBoundingBox().getSize() * 4.0 * 64.0;
        return distance < size * size;
    }
}
