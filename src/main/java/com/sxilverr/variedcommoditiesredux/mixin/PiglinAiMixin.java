package com.sxilverr.variedcommoditiesredux.mixin;

import com.sxilverr.variedcommoditiesredux.VCArmors;
import com.sxilverr.variedcommoditiesredux.VCConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinAi.class)
public abstract class PiglinAiMixin {
    @Inject(method = "isWearingSafeArmor", at = @At("RETURN"), cancellable = true)
    private static void variedcommoditiesredux$piglinSafeArmor(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() || !VCConfig.goldenArmorCalmsPiglins) return;
        for (EquipmentSlot slot : EquipmentSlot.VALUES) {
            if (slot.isArmor() && entity.getItemBySlot(slot).is(VCArmors.PIGLIN_SAFE)) {
                cir.setReturnValue(true);
                return;
            }
        }
    }
}
