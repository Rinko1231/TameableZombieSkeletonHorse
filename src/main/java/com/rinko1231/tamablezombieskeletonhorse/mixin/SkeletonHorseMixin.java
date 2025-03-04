package com.rinko1231.tamablezombieskeletonhorse.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = SkeletonHorse.class)
public abstract class SkeletonHorseMixin extends AbstractHorse {

    protected SkeletonHorseMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    public void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        boolean flag = this.isTamed() && player.isSecondaryUseActive();
        if (!this.isVehicle() && !flag) {
            ItemStack itemstack = player.getItemInHand(hand);
            if (!itemstack.isEmpty()) {
                if (this.isFood(itemstack)) {
                    cir.setReturnValue(this.fedFood(player, itemstack));
                }

                if (!this.isTamed()) {
                    this.makeMad();
                    cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide));
                }
            }

            cir.setReturnValue(super.mobInteract(player, hand));
        } else {
            cir.setReturnValue(super.mobInteract(player, hand));
        }
        cir.cancel();
    }
}