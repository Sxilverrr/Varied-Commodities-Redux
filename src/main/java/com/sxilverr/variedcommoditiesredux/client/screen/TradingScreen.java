package com.sxilverr.variedcommoditiesredux.client.screen;

import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import com.sxilverr.variedcommoditiesredux.menu.TradingMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class TradingScreen extends AbstractContainerScreen<TradingMenu> {
    private static final ResourceLocation TEXTURE = VariedCommodities.id("textures/gui/tradingblock.png");
    private Button trade;

    public TradingScreen(TradingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageHeight = 230;
    }

    @Override
    protected void init() {
        super.init();
        trade = addRenderableWidget(Button.builder(Component.translatable("trader.variedcommoditiesredux.trade"),
                b -> minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0)).bounds(leftPos + 53, topPos + 102, 70, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int state = menu.state();
        trade.visible = menu.hasPartner() && (state == TradingMenu.WAITING || state == TradingMenu.PARTNER_ACCEPTED);
        //? if <1.21 {
        renderBackground(graphics);
        //?}
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        entity(graphics, leftPos + 34, topPos + 94, mouseX, mouseY, minecraft.player);
        int state = menu.state();
        String label = !menu.hasPartner() ? "trader.variedcommoditiesredux.waiting"
                : state == TradingMenu.COMPLETE ? "trader.variedcommoditiesredux.complete"
                : state == TradingMenu.CANCELLED ? "trader.variedcommoditiesredux.cancelled" : null;
        if (label != null) {
            Component text = Component.translatable(label);
            graphics.drawString(font, text, leftPos + 53 + (70 - font.width(text)) / 2, topPos + 107, 0x404040, false);
        }
        if (state == TradingMenu.COMPLETE || state == TradingMenu.CANCELLED) return;
        Player partner = menu.partner(minecraft.level);
        if (partner != null) entity(graphics, leftPos + 142, topPos + 94, mouseX, mouseY, partner);
        if (state == TradingMenu.PARTNER_ACCEPTED) square(graphics, leftPos + 6, topPos + 6, 164, 20);
        if (state == TradingMenu.ACCEPTED) square(graphics, leftPos + 6, topPos + 123, 164, 20);
    }

    private static void square(GuiGraphics graphics, int x, int y, int width, int height) {
        int color = 0xFF00FF00;
        graphics.fill(x, y, x + width, y + 2, color);
        graphics.fill(x, y, x + 2, y + height, color);
        graphics.fill(x, y + height - 2, x + width, y + height, color);
        graphics.fill(x + width - 2, y, x + width, y + height, color);
    }

    private static void entity(GuiGraphics graphics, int x, int y, int mouseX, int mouseY, LivingEntity entity) {
        //? if >=1.21 {
        /*InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, x - 25, y - 70, x + 25, y, 30, 0.0625F, mouseX, mouseY, entity);
        *///?} else {
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, x, y, 30, x - 1 - mouseX, y - 42 - mouseY, entity);
        //?}
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    }
}
