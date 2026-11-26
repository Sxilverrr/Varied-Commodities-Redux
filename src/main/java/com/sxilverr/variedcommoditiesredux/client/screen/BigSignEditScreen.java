package com.sxilverr.variedcommoditiesredux.client.screen;

import com.sxilverr.variedcommoditiesredux.block.BigSignBlock;
import com.sxilverr.variedcommoditiesredux.block.entity.BigSignBlockEntity;
import com.sxilverr.variedcommoditiesredux.mixin.GuiGraphicsAccessor;
import com.sxilverr.variedcommoditiesredux.network.VCNetwork;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

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
        //? if >=26.3 {
        /*minecraft.textInputManager().startTextInput(this);
        *///?} else {
        minecraft.textInputManager().startTextInput();
        //?}
    }

    @Override
    public void tick() {
        frame++;
        if (sign.isRemoved() || minecraft.player == null || minecraft.player.distanceToSqr(Vec3.atCenterOf(sign.getBlockPos())) > 64.0) onClose();
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int key = event.key();
        if (key == InputConstants.KEY_UP) {
            move(-1);
            return true;
        }
        if (key == InputConstants.KEY_DOWN || key == InputConstants.KEY_RETURN || key == InputConstants.KEY_NUMPADENTER) {
            move(1);
            return true;
        }
        return field.keyPressed(event) || super.keyPressed(event);
    }

    private void move(int direction) {
        line = Math.floorMod(line + direction, lines.length);
        field.setCursorToEnd();
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        field.charTyped(event);
        return true;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        float size = size();
        graphics.centeredText(font, title, width / 2, center() - (int) (size / 2) - 16, 0xFFFFFFFF);
        int half = (int) size;
        ((GuiGraphicsAccessor) graphics).variedcommoditiesredux$guiRenderState().addPicturesInPictureState(new SignPreviewRenderer.State(
                block, lines.clone(), frame / 6 % 2 == 0 ? line : -1, field.getCursorPos(),
                width / 2 - half, center() - half, width / 2 + half, center() + half, size));
    }

    @Override
    public void removed() {
        if (minecraft.level != null) VCNetwork.sendToServer(new VCNetwork.SaveSign(sign.getBlockPos(), List.of(lines)));
        //? if >=26.3 {
        /*minecraft.textInputManager().stopTextInput(this);
        *///?} else {
        minecraft.textInputManager().stopTextInput();
        //?}
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
