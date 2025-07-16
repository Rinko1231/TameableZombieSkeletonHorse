package com.rinko1231.tameablezombieskeletonhorse.render;

import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ModZombieHorseRenderer extends MobRenderer<ZombieHorse, HorseModel<ZombieHorse>> {

    public ModZombieHorseRenderer(EntityRendererProvider.Context context) {

        super(context, new HorseModel<>(context.bakeLayer(ModelLayers.ZOMBIE_HORSE)), 1.1F);


        this.addLayer(new GenericHorseArmorLayer<>(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(@NotNull ZombieHorse entity) {

        return new ResourceLocation("textures/entity/horse/horse_zombie.png");
    }
}