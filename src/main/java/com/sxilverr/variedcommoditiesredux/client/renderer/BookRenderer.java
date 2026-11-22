package com.sxilverr.variedcommoditiesredux.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import com.sxilverr.variedcommoditiesredux.client.VCRender;
import com.sxilverr.variedcommoditiesredux.client.model.InkModel;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.object.book.BookModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3fc;

import java.util.function.Consumer;

public final class BookRenderer {
    public static final Identifier ID = VariedCommodities.id("book");
    private static final Identifier INK = VariedCommodities.id("textures/models/block/ink.png");
    private static final Identifier ENCHANTING_BOOK = Identifier.withDefaultNamespace("textures/entity/enchanting_table_book.png");
    private static final BookModel.State OPEN = new BookModel.State(0.0F, 0.0F, 1.0F, 1.24F);
    private final ModelPart ink = InkModel.create();
    private final BookModel book;

    public BookRenderer(EntityModelSet models) {
        book = new BookModel(models.bakeLayer(ModelLayers.BOOK));
    }

    public void submit(PoseStack pose, SubmitNodeCollector collector, int light, int overlay, float yaw) {
        pose.pushPose();
        transformInk(pose, yaw);
        collector.submitModelPart(ink, pose, RenderTypes.entityCutout(INK), light, overlay, null);
        transformBook(pose);
        collector.submitModel(book, OPEN, pose, RenderTypes.entityCutout(ENCHANTING_BOOK), light, overlay, -1, null, 0, null);
        pose.popPose();
    }

    private static void transformInk(PoseStack pose, float yaw) {
        pose.translate(0.5, 1.5, 0.5);
        VCRender.rotate(pose, yaw);
    }

    private static void transformBook(PoseStack pose) {
        pose.mulPose(Axis.ZP.rotationDegrees(-90.0F));
        pose.translate(-1.49, -0.18, 0.0);
    }

    public record Item(BookRenderer renderer) implements NoDataSpecialModelRenderer {
        @Override
        public void submit(ItemDisplayContext context, PoseStack pose, SubmitNodeCollector collector, int light, int overlay, boolean foil, int outline) {
            renderer.submit(pose, collector, light, overlay, 0.0F);
        }

        @Override
        public void getExtents(Consumer<Vector3fc> consumer) {
            PoseStack pose = new PoseStack();
            transformInk(pose, 0.0F);
            renderer.ink.getExtentsForGui(pose, consumer);
            transformBook(pose);
            renderer.book.root().getExtentsForGui(pose, consumer);
        }
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
            return new Item(new BookRenderer(context.entityModelSet()));
        }

        @Override
        public MapCodec<Unbaked> type() {
            return CODEC;
        }
    }
}
