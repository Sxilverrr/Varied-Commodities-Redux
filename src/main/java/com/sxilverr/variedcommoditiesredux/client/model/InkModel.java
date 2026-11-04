package com.sxilverr.variedcommoditiesredux.client.model;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class InkModel extends VCModel {
    public InkModel() {
        super(create());
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("InkMid", CubeListBuilder.create().texOffs(0, 25).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F), PartPose.offset(5.0F, 21.0F, 3.5F));
        root.addOrReplaceChild("InkTop", CubeListBuilder.create().texOffs(0, 22).addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 2.0F), PartPose.offset(4.5F, 20.0F, 3.0F));
        root.addOrReplaceChild("InkBottom", CubeListBuilder.create().texOffs(3, 16).addBox(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 3.0F), PartPose.offset(4.0F, 22.0F, 2.5F));
        root.addOrReplaceChild("Shape1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 13.0F, 3.0F), PartPose.offset(5.5F, 10.0F, 2.5F));
        root.addOrReplaceChild("InkBottom2", CubeListBuilder.create().texOffs(0, 27).addBox(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 3.0F), PartPose.offset(4.0F, 23.0F, 2.5F));
        return LayerDefinition.create(mesh, 64, 32);
    }
}
