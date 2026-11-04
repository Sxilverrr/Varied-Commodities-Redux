package com.sxilverr.variedcommoditiesredux.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import com.sxilverr.variedcommoditiesredux.block.BigSignBlock;
import com.sxilverr.variedcommoditiesredux.block.RotatedBlock;
import com.sxilverr.variedcommoditiesredux.block.entity.BigSignBlockEntity;
import com.sxilverr.variedcommoditiesredux.block.entity.IconBlockEntity;
import com.sxilverr.variedcommoditiesredux.block.entity.StorageBlockEntity;
import com.sxilverr.variedcommoditiesredux.block.entity.VCBlockEntity;
import com.sxilverr.variedcommoditiesredux.client.VCRender;
import com.sxilverr.variedcommoditiesredux.client.model.InkModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public final class BlockRenderers {
    private static final ResourceLocation INK = VariedCommodities.id("textures/models/block/ink.png");
    private static final ResourceLocation ENCHANTING_BOOK = VariedCommodities.rl("minecraft", "textures/entity/enchanting_table_book.png");
    private static final InkModel INK_MODEL = new InkModel();
    private static BookModel book;

    public interface Drawer<T extends BlockEntity> {
        void draw(T be, PoseStack pose, MultiBufferSource buffers, int light, int overlay);
    }

    public static <T extends BlockEntity> BlockEntityRendererProvider<T> of(Drawer<T> drawer) {
        return context -> new Renderer<>(drawer);
    }

    private record Renderer<T extends BlockEntity>(Drawer<T> drawer) implements BlockEntityRenderer<T> {
        @Override
        public void render(T be, float partialTick, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
            pose.pushPose();
            drawer.draw(be, pose, buffers, light, overlay);
            pose.popPose();
        }

        @Override
        public boolean shouldRenderOffScreen(T be) {
            return true;
        }
    }

    private static boolean item(BlockEntity be) {
        return be.getLevel() == null;
    }

    private static int rotation(BlockEntity be) {
        return item(be) ? 0 : RotatedBlock.rotation(be.getBlockState());
    }

    private static boolean tooFar(BlockEntity be, double distance) {
        if (item(be)) return true;
        Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        return camera.distanceToSqr(Vec3.atCenterOf(be.getBlockPos())) > distance * distance;
    }

    public static void weaponRack(StorageBlockEntity be, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        if (tooFar(be, 26)) return;
        pose.translate(0.5, 1.34, 0.5);
        pose.scale(0.9F, 0.9F, 0.9F);
        VCRender.rotate(pose, 90 * rotation(be));
        for (int i = 0; i < 3; i++) {
            ItemStack stack = be.getItem(i);
            if (stack.isEmpty()) continue;
            pose.pushPose();
            pose.translate(-0.37F + i * 0.37F, 0.6F, 0.33F);
            ItemTransform transform = VCRender.thirdPerson(stack, be.getLevel());
            pose.mulPose(Axis.YP.rotationDegrees(Math.round(transform.rotation.x() / 180.0F) * 180.0F));
            pose.mulPose(Axis.XP.rotationDegrees(180.0F));
            pose.scale(transform.scale.x(), transform.scale.y(), transform.scale.z());
            pose.mulPose(Axis.YP.rotationDegrees(90.0F));
            pose.mulPose(Axis.ZP.rotationDegrees(45.0F));
            VCRender.item(pose, buffers, light, overlay, stack, displayContext(stack, be), be.getLevel());
            pose.popPose();
        }
    }

    public static void pedestal(StorageBlockEntity be, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        ItemStack stack = be.getItem(0);
        if (stack.isEmpty() || tooFar(be, 40)) return;
        pose.translate(0.5, 1.5, 0.5);
        VCRender.rotate(pose, 90 * rotation(be));
        pose.scale(1.0F, 0.99F, 1.0F);
        pose.translate(0.0, 0.6, 0.0);
        if (!(stack.getItem() instanceof SwordItem)) pose.mulPose(Axis.XP.rotationDegrees(180.0F));
        ItemTransform transform = VCRender.thirdPerson(stack, be.getLevel());
        pose.scale(transform.scale.x(), transform.scale.y(), transform.scale.z());
        pose.mulPose(Axis.ZP.rotationDegrees(45.0F));
        VCRender.item(pose, buffers, light, overlay, stack, displayContext(stack, be), be.getLevel());
    }

    private static ItemDisplayContext displayContext(ItemStack stack, BlockEntity be) {
        return Minecraft.getInstance().getItemRenderer().getModel(stack, be.getLevel(), null, 0).isCustomRenderer() ? ItemDisplayContext.FIXED : ItemDisplayContext.NONE;
    }

    public static void shopSign(IconBlockEntity be, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        if (be.icon.isEmpty() || tooFar(be, 20)) return;
        pose.translate(0.5, 1.62, 0.5);
        VCRender.rotate(pose, 90 * rotation(be));
        pose.translate(0.0, 1.02, 0.03);
        VCRender.icon(pose, buffers, light, be.icon, be.getLevel(), -0.024F, 0.024F, 0.001F);
        pose.translate(0.0, 0.0, -0.06);
        pose.mulPose(Axis.YP.rotationDegrees(180.0F));
        VCRender.icon(pose, buffers, light, be.icon, be.getLevel(), -0.024F, 0.024F, 0.001F);
    }

    public static void banner(IconBlockEntity be, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        icon(be, pose, buffers, light, 1.3, -0.14);
    }

    public static void wallBanner(IconBlockEntity be, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        icon(be, pose, buffers, light, 0.2, 0.26);
    }

    private static void icon(IconBlockEntity be, PoseStack pose, MultiBufferSource buffers, int light, double y, double z) {
        if (be.icon.isEmpty() || tooFar(be, 26)) return;
        pose.translate(0.5, y, 0.5);
        VCRender.rotate(pose, 90 * rotation(be));
        pose.translate(0.0, 0.0, z);
        VCRender.icon(pose, buffers, light, be.icon, be.getLevel(), 0.05F, 0.05F, -0.001F);
    }

    public static void bigSign(BigSignBlockEntity be, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        if (tooFar(be, 20)) return;
        int rotation = rotation(be);
        double x = rotation == 1 ? -0.44 : rotation == 3 ? 0.44 : 0.0;
        double z = rotation == 2 ? -0.44 : rotation == 0 ? 0.44 : 0.0;
        pose.translate(0.5 + x, 0.5, 0.5 + z);
        pose.mulPose(Axis.YP.rotationDegrees(90 * rotation));
        if (rotation % 2 == 0) pose.mulPose(Axis.YP.rotationDegrees(180.0F));
        bigSignText(pose, buffers, light, be.lines(), -1, 0);
    }

    public static void tombstone(BigSignBlockEntity be, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        if (!(be.getBlockState().getBlock() instanceof BigSignBlock sign) || !sign.hasText() || tooFar(be, 20)) return;
        pose.translate(0.5, 1.5, 0.5);
        pose.mulPose(Axis.YP.rotationDegrees(-90 * rotation(be)));
        tombstoneText(sign.style(), pose, buffers, light, be.lines(), -1, 0);
    }

    public static void signPreview(BigSignBlock block, PoseStack pose, MultiBufferSource buffers, String[] lines, int cursorLine, int cursor) {
        int light = LightTexture.FULL_BRIGHT;
        if (block.style() < 0) {
            pose.pushPose();
            pose.mulPose(Axis.YP.rotationDegrees(180.0F));
            pose.translate(-0.5, -0.5, -0.94);
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(block.defaultBlockState(), pose, buffers, light, OverlayTexture.NO_OVERLAY);
            pose.popPose();
            bigSignText(pose, buffers, light, lines, cursorLine, cursor);
        } else {
            pose.mulPose(Axis.YP.rotationDegrees(180.0F));
            pose.translate(0.0, 1.0, 0.0);
            pose.pushPose();
            pose.translate(-0.5, -1.5, -0.5);
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(block.defaultBlockState(), pose, buffers, light, OverlayTexture.NO_OVERLAY);
            pose.popPose();
            tombstoneText(block.style(), pose, buffers, light, lines, cursorLine, cursor);
        }
    }

    private static void bigSignText(PoseStack pose, MultiBufferSource buffers, int light, String[] lines, int cursorLine, int cursor) {
        pose.translate(0.0, 0.5, 0.065);
        float scale = 0.0133F * 0.6666667F;
        pose.scale(scale, -scale, scale);
        text(pose, buffers, light, lines, 0.5F, 0, cursorLine, cursor);
    }

    private static void tombstoneText(int style, PoseStack pose, MultiBufferSource buffers, int light, String[] lines, int cursorLine, int cursor) {
        VCRender.rotate(pose, 0.0F);
        pose.mulPose(Axis.XP.rotationDegrees(180.0F));
        pose.translate(0.0, -0.64, style == 0 ? 0.095 : 0.126);
        float scale = 0.00665F;
        pose.scale(scale, -scale, scale);
        text(pose, buffers, light, lines, 0.0F, 0xFFFFFF, cursorLine, cursor);
    }

    private static void text(PoseStack pose, MultiBufferSource buffers, int light, String[] lines, float top, int color, int cursorLine, int cursor) {
        Font font = Minecraft.getInstance().font;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            float x = -font.width(line) / 2.0F;
            int y = (int) ((top + i) * (font.lineHeight - 0.3F));
            if (!line.isEmpty()) draw(font, line, x, y, color, pose, buffers, light);
            if (i != cursorLine) continue;
            if (cursor >= line.length()) draw(font, "_", x + font.width(line), y, color, pose, buffers, light);
            else draw(font, "|", x + font.width(line.substring(0, cursor)) - 1.0F, y, color, pose, buffers, light);
        }
    }

    private static void draw(Font font, String text, float x, int y, int color, PoseStack pose, MultiBufferSource buffers, int light) {
        font.drawInBatch(text, x, y, color, false, pose.last().pose(), buffers, Font.DisplayMode.POLYGON_OFFSET, 0, light);
    }

    public static void book(VCBlockEntity be, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        if (book == null) book = new BookModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.BOOK));
        pose.translate(0.5, 1.5, 0.5);
        VCRender.rotate(pose, item(be) ? 0 : 90 * rotation(be) - 90);
        INK_MODEL.render(pose, VCRender.culled(buffers, INK), light, overlay);
        pose.mulPose(Axis.ZP.rotationDegrees(-90.0F));
        pose.translate(-1.49, -0.18, 0.0);
        book.setupAnim(0.0F, 0.0F, 1.0F, 1.24F);
        //? if >=1.21 {
        /*book.renderToBuffer(pose, VCRender.culled(buffers, ENCHANTING_BOOK), light, overlay, 0xFFFFFFFF);
        *///?} else {
        book.renderToBuffer(pose, VCRender.culled(buffers, ENCHANTING_BOOK), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        //?}
    }

    private BlockRenderers() {
    }
}
