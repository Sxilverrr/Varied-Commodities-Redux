package com.sxilverr.variedcommoditiesredux.mixin;

import com.sxilverr.variedcommoditiesredux.client.screen.OpenBookScreen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.gui.screens.inventory.BookSignScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BookSignScreen.class)
public abstract class BookSignScreenMixin {
    @Shadow
    @Final
    private BookEditScreen bookEditScreen;

    @Shadow
    @Final
    private List<String> pages;

    @Shadow
    private EditBox titleBox;

    @Inject(method = "saveChanges", at = @At("HEAD"), cancellable = true)
    private void variedcommoditiesredux$signOnBlock(CallbackInfo ci) {
        if (bookEditScreen instanceof OpenBookScreen screen) {
            screen.save(pages, titleBox.getValue().trim(), true);
            ci.cancel();
        }
    }
}
