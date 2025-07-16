package com.rinko1231.tameablezombieskeletonhorse.render;

import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ModSkeletonHorseRenderer extends MobRenderer<SkeletonHorse, HorseModel<SkeletonHorse>> {

    public ModSkeletonHorseRenderer(EntityRendererProvider.Context context) {
        // 使用原版骷髅马模型层
        super(context, new HorseModel<>(context.bakeLayer(ModelLayers.SKELETON_HORSE)), 1.1F);

        // 添加通用马铠层
        this.addLayer(new GenericHorseArmorLayer<>(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(@NotNull SkeletonHorse entity) {
        // 使用原版骷髅马纹理
        return new ResourceLocation("textures/entity/horse/horse_skeleton.png");
    }
}