package com.sxilverr.variedcommoditiesredux.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

import java.util.EnumSet;

public class SkirtModel extends HumanoidModel<HumanoidRenderState> {
    private static final int PANELS = 10;
    private static SkirtModel instance;
    private final ModelPart skirt;
    private final ModelPart[] panels = new ModelPart[PANELS];

    private SkirtModel(ModelPart root) {
        super(root, RenderTypes::armorCutoutNoCull);
        skirt = root.getChild("skirt");
        ModelPart scaled = skirt.getChild("scaled");
        for (int i = 0; i < PANELS; i++) panels[i] = scaled.getChild("turn" + i).getChild("panel");
    }

    public static SkirtModel get() {
        if (instance == null) instance = new SkirtModel(create().bakeRoot());
        return instance;
    }

    private static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO).addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(5.0F, 2.0F, 0.0F));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9F, 12.0F, 0.0F));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.9F, 12.0F, 0.0F));
        PartDefinition scaled = root.addOrReplaceChild("skirt", CubeListBuilder.create(), PartPose.ZERO)
                .addOrReplaceChild("scaled", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 0.0F).scaled(2.1F, 1.04F, 2.1F));
        for (int i = 0; i < PANELS; i++) {
            scaled.addOrReplaceChild("turn" + i, CubeListBuilder.create(), PartPose.rotation(0.0F, (i + 1) * 36.0F * Mth.DEG_TO_RAD, 0.0F))
                    .addOrReplaceChild("panel", plane(-2.6F, -2.6F, 4), PartPose.offset(2.4F, 0.0F, 0.0F))
                    .addOrReplaceChild("side", plane(-0.6F, 2.6F, 6), PartPose.rotation(0.0F, -Mth.HALF_PI, 0.0F));
        }
        return LayerDefinition.create(mesh, 64, 32);
    }

    private static CubeListBuilder plane(float x, float z, int u) {
        return CubeListBuilder.create().texOffs(u - 2, 18).addBox(x, 9.0F, z, 0.0F, 9.0F, 2.0F, EnumSet.of(Direction.EAST));
    }

    @Override
    public void setupAnim(HumanoidRenderState state) {
        super.setupAnim(state);
        skirt.x = (leftLeg.x + rightLeg.x) / 2.0F;
        skirt.y = (leftLeg.y + rightLeg.y) / 2.0F;
        skirt.z = (leftLeg.z + rightLeg.z) / 2.0F;
        skirt.xRot = body.xRot * 0.42F;
        float rotX = 0.3F + leftArm.xRot * 0.01F;
        float rotZ = -0.23F + leftArm.xRot * 0.03F;
        for (ModelPart panel : panels) {
            panel.xRot = rotX;
            panel.yRot = -0.2F;
            panel.zRot = rotZ;
        }
    }
}
