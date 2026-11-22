package com.sxilverr.variedcommoditiesredux.client.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.sxilverr.variedcommoditiesredux.block.BigSignBlock;
import com.sxilverr.variedcommoditiesredux.client.renderer.BlockRenderers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jspecify.annotations.Nullable;

public class SignPreviewRenderer extends PictureInPictureRenderer<SignPreviewRenderer.State> {
    public record State(BigSignBlock block, String[] lines, int cursorLine, int cursor, int x0, int y0, int x1, int y1, float scale,
                        @Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds) implements PictureInPictureRenderState {
        public State(BigSignBlock block, String[] lines, int cursorLine, int cursor, int x0, int y0, int x1, int y1, float scale) {
            this(block, lines, cursorLine, cursor, x0, y0, x1, y1, scale, null, PictureInPictureRenderState.getBounds(x0, y0, x1, y1, null));
        }
    }

    public SignPreviewRenderer(MultiBufferSource.BufferSource buffers) {
        super(buffers);
    }

    @Override
    public Class<State> getRenderStateClass() {
        return State.class;
    }

    @Override
    protected float getTranslateY(int height, int scale) {
        return height / 2.0F;
    }

    @Override
    protected void renderToTexture(State state, PoseStack pose) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_FLAT);
        pose.mulPose(Axis.XP.rotationDegrees(180.0F));
        int light = LightTexture.FULL_BRIGHT;
        Font font = minecraft.font;
        BlockRenderers.TextOut out = (text, x, y, color, p) -> font.drawInBatch(text, x, y, color, false, p.last().pose(), bufferSource, Font.DisplayMode.POLYGON_OFFSET, 0, light);
        BigSignBlock block = state.block();
        if (block.style() < 0) {
            pose.pushPose();
            pose.mulPose(Axis.YP.rotationDegrees(180.0F));
            pose.translate(-0.5, -0.5, -0.94);
            minecraft.getBlockRenderer().renderSingleBlock(block.defaultBlockState(), pose, bufferSource, light, OverlayTexture.NO_OVERLAY);
            pose.popPose();
            BlockRenderers.bigSignText(font, out, pose, state.lines(), state.cursorLine(), state.cursor());
        } else {
            pose.mulPose(Axis.YP.rotationDegrees(180.0F));
            pose.translate(0.0, 1.0, 0.0);
            pose.pushPose();
            pose.translate(-0.5, -1.5, -0.5);
            minecraft.getBlockRenderer().renderSingleBlock(block.defaultBlockState(), pose, bufferSource, light, OverlayTexture.NO_OVERLAY);
            pose.popPose();
            BlockRenderers.tombstoneText(font, out, block.style(), pose, state.lines(), state.cursorLine(), state.cursor());
        }
    }

    @Override
    protected String getTextureLabel() {
        return "variedcommoditiesredux_sign";
    }
}
