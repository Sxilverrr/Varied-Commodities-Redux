package com.sxilverr.variedcommoditiesredux.mixin;

import com.sxilverr.variedcommoditiesredux.client.screen.OpenBookScreen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BookEditScreen.class)
public abstract class BookEditScreenMixin {
    @Shadow
    @Final
    private List<String> pages;

    @Inject(method = "saveChanges", at = @At("HEAD"), cancellable = true)
    private void variedcommoditiesredux$saveToBlock(CallbackInfo ci) {
        if ((Object) this instanceof OpenBookScreen screen) {
            screen.save(pages, "", false);
            ci.cancel();
        }
    }
}
