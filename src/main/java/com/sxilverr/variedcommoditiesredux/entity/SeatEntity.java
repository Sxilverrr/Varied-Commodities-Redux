package com.sxilverr.variedcommoditiesredux.entity;

import com.sxilverr.variedcommoditiesredux.VCConfig;
import com.sxilverr.variedcommoditiesredux.VCEntities;
import com.sxilverr.variedcommoditiesredux.block.CouchBlock;
import com.sxilverr.variedcommoditiesredux.block.SeatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityDimensions;

public class SeatEntity extends Entity {
    private static final double CHAIR_HEIGHT = 0.58;
    private BlockPos seatPos;
    private boolean aligned;

    public SeatEntity(EntityType<? extends SeatEntity> type, Level level) {
        super(type, level);
        noPhysics = true;
    }

    public static InteractionResult mount(Level level, BlockPos pos, Player player, double height, float yaw) {
        if (!VCConfig.seats || VCConfig.seatsNeedEmptyHand && !player.getMainHandItem().isEmpty()) return InteractionResult.PASS;
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (player.isPassenger() || !level.getEntitiesOfClass(SeatEntity.class, new AABB(pos).inflate(0.0, 1.0, 0.0), seat -> pos.equals(seat.seatPos)).isEmpty()) {
            return InteractionResult.CONSUME;
        }
        SeatEntity seat = new SeatEntity(VCEntities.SEAT.get(), level);
        seat.seatPos = pos.immutable();
        seat.snapTo(pos.getX() + 0.5, pos.getY() + height - CHAIR_HEIGHT, pos.getZ() + 0.5, yaw, 0.0F);
        level.addFreshEntity(seat);
        player.startRiding(seat, true, true);
        return InteractionResult.CONSUME;
    }

    private BlockPos seatPos() {
        if (seatPos == null) seatPos = BlockPos.containing(getX(), getY() + 0.3, getZ());
        return seatPos;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) return;
        Block block = level().getBlockState(seatPos()).getBlock();
        if (getPassengers().isEmpty() || !(block instanceof SeatBlock || block instanceof CouchBlock)) {
            ejectPassengers();
            discard();
        }
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction move) {
        super.positionRider(passenger, move);
        if (!aligned) {
            passenger.setYRot(getYRot());
            passenger.yRotO = getYRot();
            aligned = true;
        }
        clampRotation(passenger);
    }

    @Override
    public void onPassengerTurned(Entity passenger) {
        clampRotation(passenger);
    }

    private void clampRotation(Entity passenger) {
        passenger.setYBodyRot(getYRot());
        float offset = Mth.wrapDegrees(passenger.getYRot() - getYRot());
        float clamped = Mth.clamp(offset, -105.0F, 105.0F);
        passenger.yRotO += clamped - offset;
        passenger.setYRot(passenger.getYRot() + clamped - offset);
        passenger.setYHeadRot(passenger.getYRot());
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        BlockPos origin = seatPos();
        for (int[] offset : DismountHelper.offsetsForDirection(passenger.getDirection())) {
            Vec3 spot = DismountHelper.findSafeDismountLocation(passenger.getType(), level(), origin.offset(offset[0], 0, offset[1]), true);
            if (spot != null) return spot;
        }
        Vec3 above = DismountHelper.findSafeDismountLocation(passenger.getType(), level(), origin.above(), true);
        return above != null ? above : super.getDismountLocationForPassenger(passenger);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
        return new Vec3(0.0, CHAIR_HEIGHT, 0.0);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        input.getLong("SeatPos").ifPresent(pos -> seatPos = BlockPos.of(pos));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putLong("SeatPos", seatPos().asLong());
    }
}
