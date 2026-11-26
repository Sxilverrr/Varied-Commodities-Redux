package com.sxilverr.variedcommoditiesredux.client.screen;

import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import com.sxilverr.variedcommoditiesredux.menu.CarpentryMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class CarpentryScreen extends AbstractContainerScreen<CarpentryMenu> {
    private static final Identifier TEXTURE = VariedCommodities.id("textures/gui/carpentry.png");

    public CarpentryScreen(CarpentryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 180);
        titleLabelX = 4;
        titleLabelY = 4;
        inventoryLabelX = 4;
        inventoryLabelY = 87;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0.0F, 0.0F, imageWidth, imageHeight, 256, 256);
    }
}
