package com.sxilverr.variedcommoditiesredux.client.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.sxilverr.variedcommoditiesredux.block.BigSignBlock;
import com.sxilverr.variedcommoditiesredux.client.renderer.BlockRenderers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.LightCoordsUtil;
import org.jspecify.annotations.Nullable;
//? if <26.2 {
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
//?}

public class SignPreviewRenderer extends PictureInPictureRenderer<SignPreviewRenderer.State> {
    private static final BlockDisplayContext DISPLAY_CONTEXT = BlockDisplayContext.create();
    private final BlockModelRenderState blockState = new BlockModelRenderState();

    public record State(BigSignBlock block, String[] lines, int cursorLine, int cursor, int x0, int y0, int x1, int y1, float scale,
                        @Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds) implements PictureInPictureRenderState {
        public State(BigSignBlock block, String[] lines, int cursorLine, int cursor, int x0, int y0, int x1, int y1, float scale) {
            this(block, lines, cursorLine, cursor, x0, y0, x1, y1, scale, null, PictureInPictureRenderState.getBounds(x0, y0, x1, y1, null));
        }
    }

    //? if <26.2 {
    public SignPreviewRenderer(MultiBufferSource.BufferSource buffers) {
        super(buffers);
    }

    @Override
    protected void renderToTexture(State state, PoseStack pose) {
        FeatureRenderDispatcher features = Minecraft.getInstance().gameRenderer.getFeatureRenderDispatcher();
        Minecraft.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_FLAT);
        draw(state, pose, features.getSubmitNodeStorage());
        features.renderAllFeatures();
    }
    //?} else {
    /*@Override
    protected void renderToTexture(State state, PoseStack pose, SubmitNodeCollector collector) {
        Minecraft.getInstance().gameRenderer.lighting().setupFor(Lighting.Entry.ITEMS_FLAT);
        draw(state, pose, collector);
    }
    *///?}

    @Override
    public Class<State> getRenderStateClass() {
        return State.class;
    }

    @Override
    protected float getTranslateY(int height, int scale) {
        return height / 2.0F;
    }

    private void draw(State state, PoseStack pose, SubmitNodeCollector collector) {
        Minecraft minecraft = Minecraft.getInstance();
        pose.mulPose(Axis.XP.rotationDegrees(180.0F));
        int light = LightCoordsUtil.FULL_BRIGHT;
        BlockRenderers.TextOut out = BlockRenderers.text(collector, light);
        BigSignBlock block = state.block();
        new BlockModelResolver(minecraft.getModelManager()).update(blockState, block.defaultBlockState(), DISPLAY_CONTEXT);
        if (block.style() < 0) {
            pose.pushPose();
            pose.mulPose(Axis.YP.rotationDegrees(180.0F));
            pose.translate(-0.5, -0.5, -0.94);
            blockState.submit(pose, collector, light, OverlayTexture.NO_OVERLAY, 0);
            pose.popPose();
            BlockRenderers.bigSignText(minecraft.font, out, pose, state.lines(), state.cursorLine(), state.cursor());
        } else {
            pose.mulPose(Axis.YP.rotationDegrees(180.0F));
            pose.translate(0.0, 1.0, 0.0);
            pose.pushPose();
            pose.translate(-0.5, -1.5, -0.5);
            blockState.submit(pose, collector, light, OverlayTexture.NO_OVERLAY, 0);
            pose.popPose();
            BlockRenderers.tombstoneText(minecraft.font, out, block.style(), pose, state.lines(), state.cursorLine(), state.cursor());
        }
    }

    @Override
    protected String getTextureLabel() {
        return "variedcommoditiesredux_sign";
    }
}
