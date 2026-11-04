package com.sxilverr.variedcommoditiesredux.client;

import com.sxilverr.variedcommoditiesredux.Compat;
import com.sxilverr.variedcommoditiesredux.client.screen.RecipeBookScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;

public final class VCClientHooks {
    public static void spellParticles(LivingEntity entity, int color, int count) {
        int rgb = color <= 15 ? Compat.dyeRgb(DyeColor.byId(15 - color)) : color;
        float r = (rgb >> 16 & 255) / 255.0F;
        float g = (rgb >> 8 & 255) / 255.0F;
        float b = (rgb & 255) / 255.0F;
        RandomSource random = entity.getRandom();
        float yaw = entity.getYRot() * Mth.DEG_TO_RAD;
        float pitch = entity.getXRot() * Mth.DEG_TO_RAD;
        double dx = -Mth.sin(yaw) * Mth.cos(pitch);
        double dz = Mth.cos(yaw) * Mth.cos(pitch);
        for (int i = 0; i < count; i++) {
            double x = entity.getX() + (random.nextDouble() - 0.5) * entity.getBbWidth() + dx;
            double y = entity.getY() + entity.getEyeHeight() - entity.getXRot() / 40.0F;
            double z = entity.getZ() + (random.nextDouble() - 0.5) * entity.getBbWidth() + dz;
            Particle particle = Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.PORTAL, x, y, z,
                    (random.nextDouble() - 0.5) * 2.0, -random.nextDouble(), (random.nextDouble() - 0.5) * 2.0);
            if (particle != null) particle.setColor(r, g, b);
        }
    }

    public static void openRecipeBook() {
        Minecraft.getInstance().setScreen(new RecipeBookScreen());
    }

    private VCClientHooks() {
    }
}
