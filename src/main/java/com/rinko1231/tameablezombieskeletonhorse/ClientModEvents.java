package com.rinko1231.tameablezombieskeletonhorse;

import com.rinko1231.tameablezombieskeletonhorse.render.ModSkeletonHorseRenderer;
import com.rinko1231.tameablezombieskeletonhorse.render.ModZombieHorseRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "tameablezombieskeletonhorse", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {

    @SubscribeEvent
    public static void onEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
       event.registerEntityRenderer(EntityType.SKELETON_HORSE, ModSkeletonHorseRenderer::new);
        event.registerEntityRenderer(EntityType.ZOMBIE_HORSE, ModZombieHorseRenderer::new);
    }
}