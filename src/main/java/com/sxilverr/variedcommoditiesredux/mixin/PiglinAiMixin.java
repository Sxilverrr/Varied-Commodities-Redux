package com.sxilverr.variedcommoditiesredux.mixin;

import com.sxilverr.variedcommoditiesredux.VCConfig;
import com.sxilverr.variedcommoditiesredux.VCArmors;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinAi.class)
public abstract class PiglinAiMixin {
    @Inject(method = "isWearingGold", at = @At("RETURN"), cancellable = true)
    private static void variedcommoditiesredux$piglinSafeArmor(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() || !VCConfig.goldenArmorCalmsPiglins) return;
        for (ItemStack stack : entity.getArmorSlots()) {
            if (stack.is(VCArmors.PIGLIN_SAFE)) {
                cir.setReturnValue(true);
                return;
            }
        }
    }
}
