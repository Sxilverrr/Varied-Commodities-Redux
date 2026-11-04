package com.sxilverr.variedcommoditiesredux.mixin;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;

//? if <1.21 {
import com.sxilverr.variedcommoditiesredux.VCEnchants;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;
//?}

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    //? if <1.21 {
    @Inject(method = "getAvailableEnchantmentResults", at = @At("RETURN"))
    private static void variedcommoditiesredux$addEnchantments(int power, ItemStack stack, boolean treasure, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        VCEnchants.adjustTableResults(power, stack, cir.getReturnValue());
    }
    //?}
}
