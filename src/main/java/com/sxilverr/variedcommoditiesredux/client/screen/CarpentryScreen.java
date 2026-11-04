package com.sxilverr.variedcommoditiesredux.client.screen;

import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import com.sxilverr.variedcommoditiesredux.menu.CarpentryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CarpentryScreen extends AbstractContainerScreen<CarpentryMenu> {
    private static final ResourceLocation TEXTURE = VariedCommodities.id("textures/gui/carpentry.png");

    public CarpentryScreen(CarpentryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageHeight = 180;
        titleLabelX = 4;
        titleLabelY = 4;
        inventoryLabelX = 4;
        inventoryLabelY = 87;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        //? if <1.21 {
        renderBackground(graphics);
        //?}
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }
}
