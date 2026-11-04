package com.sxilverr.variedcommoditiesredux.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sxilverr.variedcommoditiesredux.client.VCRender;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;

public abstract class VCModel {
    protected final ModelPart root;
    private final String[] main;

    protected VCModel(LayerDefinition layer) {
        this(layer, null);
    }

    protected VCModel(LayerDefinition layer, String[] main) {
        this.root = layer.bakeRoot();
        this.main = main;
    }

    public ModelPart part(String name) {
        return root.getChild(name);
    }

    public void render(PoseStack pose, VertexConsumer consumer, int light, int overlay, float r, float g, float b) {
        if (main == null) VCRender.part(root, pose, consumer, light, overlay, r, g, b);
        else render(pose, consumer, light, overlay, r, g, b, main);
    }

    public void render(PoseStack pose, VertexConsumer consumer, int light, int overlay) {
        render(pose, consumer, light, overlay, 1.0F, 1.0F, 1.0F);
    }

    public void render(PoseStack pose, VertexConsumer consumer, int light, int overlay, float r, float g, float b, String... parts) {
        for (String name : parts) VCRender.part(root.getChild(name), pose, consumer, light, overlay, r, g, b);
    }
}
