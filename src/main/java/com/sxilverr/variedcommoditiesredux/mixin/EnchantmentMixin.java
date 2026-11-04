package com.sxilverr.variedcommoditiesredux.mixin;

import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;

//? if <1.21 {
import com.sxilverr.variedcommoditiesredux.VCEnchants;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//?}

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    //? if <1.21 {
    @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
    private void variedcommoditiesredux$infinityOnGuns(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this == Enchantments.INFINITY_ARROWS && VCEnchants.isApplicable(stack)) cir.setReturnValue(true);
    }
    //?}
}
