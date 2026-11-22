package com.sxilverr.variedcommoditiesredux.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.sxilverr.variedcommoditiesredux.block.BigSignBlock;
import com.sxilverr.variedcommoditiesredux.block.RotatedBlock;
import com.sxilverr.variedcommoditiesredux.block.entity.BigSignBlockEntity;
import com.sxilverr.variedcommoditiesredux.block.entity.IconBlockEntity;
import com.sxilverr.variedcommoditiesredux.block.entity.StorageBlockEntity;
import com.sxilverr.variedcommoditiesredux.block.entity.BookBlockEntity;
import com.sxilverr.variedcommoditiesredux.client.VCRender;
import com.sxilverr.variedcommoditiesredux.item.VCWeaponItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public final class BlockRenderers {
    public interface TextOut {
        void draw(String text, float x, float y, int color, PoseStack pose);
    }

    public static class State extends BlockEntityRenderState {
        public int rotation;
        public final ItemStackRenderState[] items = {new ItemStackRenderState(), new ItemStackRenderState(), new ItemStackRenderState()};
        public final float[] flip = new float[3];
        public final float[][] scale = new float[3][3];
        public boolean sword;
        public String[] lines = new String[0];
        public int style;
        public boolean text;
    }

    private abstract static class Renderer<T extends BlockEntity> implements BlockEntityRenderer<T, State> {
        protected final ItemModelResolver items;
        protected final Font font;
        private final ItemStackRenderState probe = new ItemStackRenderState();
        private final int distance;

        Renderer(BlockEntityRendererProvider.Context context, int distance) {
            this.items = context.itemModelResolver();
            this.font = context.font();
            this.distance = distance;
        }

        @Override
        public State createRenderState() {
            return new State();
        }

        @Override
        public void extractRenderState(T be, State state, float partialTick, Vec3 camera, ModelFeatureRenderer.@Nullable CrumblingOverlay crumbling) {
            BlockEntityRenderer.super.extractRenderState(be, state, partialTick, camera, crumbling);
            state.rotation = RotatedBlock.rotation(be.getBlockState());
            extract(be, state);
        }

        protected abstract void extract(T be, State state);

        protected void displayed(State state, int slot, ItemStack stack, Level level, int seed) {
            items.updateForTopItem(probe, stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, level, null, seed);
            ItemTransform transform = VCRender.transform(probe);
            state.flip[slot] = Math.round(transform.rotation().x() / 180.0F) * 180.0F;
            state.scale[slot][0] = transform.scale().x();
            state.scale[slot][1] = transform.scale().y();
            state.scale[slot][2] = transform.scale().z();
            items.updateForTopItem(state.items[slot], stack, VCRender.special(probe) ? ItemDisplayContext.FIXED : ItemDisplayContext.NONE, level, null, seed);
        }

        protected void icon(State state, ItemStack stack, Level level, int seed) {
            items.updateForTopItem(state.items[0], stack, ItemDisplayContext.GUI, level, null, seed);
        }

        @Override
        public boolean shouldRenderOffScreen() {
            return true;
        }

        @Override
        public int getViewDistance() {
            return distance;
        }

        protected void submitItem(State state, int slot, PoseStack pose, SubmitNodeCollector collector) {
            state.items[slot].submit(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        }

        protected void submitIcon(State state, PoseStack pose, SubmitNodeCollector collector, float scaleX, float scaleY, float scaleZ) {
            pose.pushPose();
            pose.scale(scaleX, scaleY, scaleZ);
            pose.scale(16.0F, -16.0F, 16.0F);
            submitItem(state, 0, pose, collector);
            pose.popPose();
        }

        protected TextOut text(SubmitNodeCollector collector, int light) {
            return (line, x, y, color, pose) -> collector.submitText(pose, x, y, FormattedCharSequence.forward(line, Style.EMPTY), false, Font.DisplayMode.POLYGON_OFFSET, light, color, 0, 0);
        }
    }

    public static class WeaponRack extends Renderer<StorageBlockEntity> {
        public WeaponRack(BlockEntityRendererProvider.Context context) {
            super(context, 26);
        }

        @Override
        protected void extract(StorageBlockEntity be, State state) {
            int seed = (int) be.getBlockPos().asLong();
            for (int i = 0; i < 3; i++) displayed(state, i, be.getItem(i), be.getLevel(), seed + i);
        }

        @Override
        public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
            pose.pushPose();
            pose.translate(0.5, 1.34, 0.5);
            pose.scale(0.9F, 0.9F, 0.9F);
            VCRender.rotate(pose, 90 * state.rotation);
            for (int i = 0; i < 3; i++) {
                if (state.items[i].isEmpty()) continue;
                pose.pushPose();
                pose.translate(-0.37F + i * 0.37F, 0.6F, 0.33F);
                pose.mulPose(Axis.YP.rotationDegrees(state.flip[i]));
                pose.mulPose(Axis.XP.rotationDegrees(180.0F));
                pose.scale(state.scale[i][0], state.scale[i][1], state.scale[i][2]);
                pose.mulPose(Axis.YP.rotationDegrees(90.0F));
                pose.mulPose(Axis.ZP.rotationDegrees(45.0F));
                submitItem(state, i, pose, collector);
                pose.popPose();
            }
            pose.popPose();
        }
    }

    public static class Pedestal extends Renderer<StorageBlockEntity> {
        public Pedestal(BlockEntityRendererProvider.Context context) {
            super(context, 40);
        }

        @Override
        protected void extract(StorageBlockEntity be, State state) {
            ItemStack stack = be.getItem(0);
            state.sword = VCWeaponItem.isSword(stack);
            displayed(state, 0, stack, be.getLevel(), (int) be.getBlockPos().asLong());
        }

        @Override
        public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
            if (state.items[0].isEmpty()) return;
            pose.pushPose();
            pose.translate(0.5, 1.5, 0.5);
            VCRender.rotate(pose, 90 * state.rotation);
            pose.scale(1.0F, 0.99F, 1.0F);
            pose.translate(0.0, 0.6, 0.0);
            if (!state.sword) pose.mulPose(Axis.XP.rotationDegrees(180.0F));
            pose.scale(state.scale[0][0], state.scale[0][1], state.scale[0][2]);
            pose.mulPose(Axis.ZP.rotationDegrees(45.0F));
            submitItem(state, 0, pose, collector);
            pose.popPose();
        }
    }

    public static class ShopSign extends Renderer<IconBlockEntity> {
        public ShopSign(BlockEntityRendererProvider.Context context) {
            super(context, 20);
        }

        @Override
        protected void extract(IconBlockEntity be, State state) {
            icon(state, be.icon, be.getLevel(), (int) be.getBlockPos().asLong());
        }

        @Override
        public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
            if (state.items[0].isEmpty()) return;
            pose.pushPose();
            pose.translate(0.5, 1.62, 0.5);
            VCRender.rotate(pose, 90 * state.rotation);
            pose.translate(0.0, 1.02, 0.03);
            submitIcon(state, pose, collector, -0.024F, 0.024F, 0.001F);
            pose.translate(0.0, 0.0, -0.06);
            pose.mulPose(Axis.YP.rotationDegrees(180.0F));
            submitIcon(state, pose, collector, -0.024F, 0.024F, 0.001F);
            pose.popPose();
        }
    }

    public static class Banner extends Renderer<IconBlockEntity> {
        private final double y;
        private final double z;

        private Banner(BlockEntityRendererProvider.Context context, double y, double z) {
            super(context, 26);
            this.y = y;
            this.z = z;
        }

        public static Banner standing(BlockEntityRendererProvider.Context context) {
            return new Banner(context, 1.3, -0.14);
        }

        public static Banner wall(BlockEntityRendererProvider.Context context) {
            return new Banner(context, 0.2, 0.26);
        }

        @Override
        protected void extract(IconBlockEntity be, State state) {
            icon(state, be.icon, be.getLevel(), (int) be.getBlockPos().asLong());
        }

        @Override
        public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
            if (state.items[0].isEmpty()) return;
            pose.pushPose();
            pose.translate(0.5, y, 0.5);
            VCRender.rotate(pose, 90 * state.rotation);
            pose.translate(0.0, 0.0, z);
            submitIcon(state, pose, collector, 0.05F, 0.05F, -0.001F);
            pose.popPose();
        }
    }

    public static class Sign extends Renderer<BigSignBlockEntity> {
        public Sign(BlockEntityRendererProvider.Context context) {
            super(context, 20);
        }

        @Override
        protected void extract(BigSignBlockEntity be, State state) {
            state.lines = be.lines().clone();
            if (be.getBlockState().getBlock() instanceof BigSignBlock sign) {
                state.style = sign.style();
                state.text = sign.hasText();
            }
        }

        @Override
        public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
            if (!state.text) return;
            pose.pushPose();
            TextOut out = text(collector, state.lightCoords);
            if (state.style < 0) {
                int rotation = state.rotation;
                double x = rotation == 1 ? -0.44 : rotation == 3 ? 0.44 : 0.0;
                double z = rotation == 2 ? -0.44 : rotation == 0 ? 0.44 : 0.0;
                pose.translate(0.5 + x, 0.5, 0.5 + z);
                pose.mulPose(Axis.YP.rotationDegrees(90 * rotation));
                if (rotation % 2 == 0) pose.mulPose(Axis.YP.rotationDegrees(180.0F));
                bigSignText(font, out, pose, state.lines, -1, 0);
            } else {
                pose.translate(0.5, 1.5, 0.5);
                pose.mulPose(Axis.YP.rotationDegrees(-90 * state.rotation));
                tombstoneText(font, out, state.style, pose, state.lines, -1, 0);
            }
            pose.popPose();
        }
    }

    public static class Book implements BlockEntityRenderer<BookBlockEntity, State> {
        private final BookRenderer book;

        public Book(BlockEntityRendererProvider.Context context) {
            book = new BookRenderer(context.entityModelSet());
        }

        @Override
        public State createRenderState() {
            return new State();
        }

        @Override
        public void extractRenderState(BookBlockEntity be, State state, float partialTick, Vec3 camera, ModelFeatureRenderer.@Nullable CrumblingOverlay crumbling) {
            BlockEntityRenderer.super.extractRenderState(be, state, partialTick, camera, crumbling);
            state.rotation = RotatedBlock.rotation(be.getBlockState());
        }

        @Override
        public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
            book.submit(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 90 * state.rotation - 90);
        }

        @Override
        public boolean shouldRenderOffScreen() {
            return true;
        }
    }

    public static void bigSignText(Font font, TextOut out, PoseStack pose, String[] lines, int cursorLine, int cursor) {
        pose.translate(0.0, 0.5, 0.065);
        float scale = 0.0133F * 0.6666667F;
        pose.scale(scale, -scale, scale);
        text(font, out, pose, lines, 0.5F, 0xFF000000, cursorLine, cursor);
    }

    public static void tombstoneText(Font font, TextOut out, int style, PoseStack pose, String[] lines, int cursorLine, int cursor) {
        VCRender.rotate(pose, 0.0F);
        pose.mulPose(Axis.XP.rotationDegrees(180.0F));
        pose.translate(0.0, -0.64, style == 0 ? 0.095 : 0.126);
        float scale = 0.00665F;
        pose.scale(scale, -scale, scale);
        text(font, out, pose, lines, 0.0F, 0xFFFFFFFF, cursorLine, cursor);
    }

    private static void text(Font font, TextOut out, PoseStack pose, String[] lines, float top, int color, int cursorLine, int cursor) {
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            float x = -font.width(line) / 2.0F;
            int y = (int) ((top + i) * (font.lineHeight - 0.3F));
            if (!line.isEmpty()) out.draw(line, x, y, color, pose);
            if (i != cursorLine) continue;
            if (cursor >= line.length()) out.draw("_", x + font.width(line), y, color, pose);
            else out.draw("|", x + font.width(line.substring(0, cursor)) - 1.0F, y, color, pose);
        }
    }

    private BlockRenderers() {
    }
}
