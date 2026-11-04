package com.sxilverr.variedcommoditiesredux.client.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.sxilverr.variedcommoditiesredux.block.BigSignBlock;
import com.sxilverr.variedcommoditiesredux.block.entity.BigSignBlockEntity;
import com.sxilverr.variedcommoditiesredux.client.renderer.BlockRenderers;
import com.sxilverr.variedcommoditiesredux.network.VCNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class BigSignEditScreen extends Screen {
    private final BigSignBlockEntity sign;
    private final BigSignBlock block;
    private final String[] lines;
    private TextFieldHelper field;
    private int line;
    private int frame;

    public BigSignEditScreen(BigSignBlockEntity sign, BigSignBlock block) {
        super(Component.translatable("sign.edit"));
        this.sign = sign;
        this.block = block;
        this.lines = sign.lines().clone();
    }

    private float size() {
        return Math.min(150.0F, height * 0.55F);
    }

    private int center() {
        return height / 2 - 12;
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> onClose())
                .bounds(width / 2 - 100, center() + (int) (size() / 2) + 10, 200, 20).build());
        field = new TextFieldHelper(() -> lines[line], text -> lines[line] = text,
                TextFieldHelper.createClipboardGetter(minecraft), TextFieldHelper.createClipboardSetter(minecraft),
                text -> font.width(text) <= block.lineWidth());
        field.setCursorToEnd();
    }

    @Override
    public void tick() {
        frame++;
        if (sign.isRemoved() || minecraft.player == null || minecraft.player.distanceToSqr(sign.getBlockPos().getCenter()) > 64.0) onClose();
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        if (key == GLFW.GLFW_KEY_UP) {
            move(-1);
            return true;
        }
        if (key == GLFW.GLFW_KEY_DOWN || key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
            move(1);
            return true;
        }
        return field.keyPressed(key) || super.keyPressed(key, scanCode, modifiers);
    }

    private void move(int direction) {
        line = Math.floorMod(line + direction, lines.length);
        field.setCursorToEnd();
    }

    @Override
    public boolean charTyped(char c, int modifiers) {
        field.charTyped(c);
        return true;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        //? if <1.21 {
        renderBackground(graphics);
        //?}
        super.render(graphics, mouseX, mouseY, partialTick);
        float size = size();
        graphics.drawCenteredString(font, title, width / 2, center() - (int) (size / 2) - 16, 0xFFFFFF);
        Lighting.setupForFlatItems();
        graphics.pose().pushPose();
        graphics.pose().translate(width / 2.0F, center(), 50.0F);
        graphics.pose().scale(size, -size, size);
        BlockRenderers.signPreview(block, graphics.pose(), graphics.bufferSource(), lines, frame / 6 % 2 == 0 ? line : -1, field.getCursorPos());
        graphics.flush();
        graphics.pose().popPose();
        Lighting.setupFor3DItems();
    }

    @Override
    public void removed() {
        if (minecraft.level != null) VCNetwork.sendToServer(new VCNetwork.SaveSign(sign.getBlockPos(), List.of(lines)));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
