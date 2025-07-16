package com.rinko1231.tameablezombieskeletonhorse;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("tameablezombieskeletonhorse")
public class TameableZombieSkeletonHorse {
    public TameableZombieSkeletonHorse()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
