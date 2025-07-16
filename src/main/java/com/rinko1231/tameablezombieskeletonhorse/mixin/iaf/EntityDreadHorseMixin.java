package com.rinko1231.tameablezombieskeletonhorse.mixin.iaf;

import com.github.alexthe666.iceandfire.entity.EntityDreadHorse;
import com.github.alexthe666.iceandfire.entity.util.IDreadMob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(value = EntityDreadHorse.class)
public abstract class EntityDreadHorseMixin extends SkeletonHorse implements IDreadMob {

    public EntityDreadHorseMixin(EntityType<? extends SkeletonHorse> p_30894_, Level p_30895_) {
        super(p_30894_, p_30895_);
    }


    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void onAddAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (!this.inventory.getItem(1).isEmpty()) {
            tag.put("ArmorItem", this.inventory.getItem(1).save(new CompoundTag()));
        }
    }
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void onReadAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("ArmorItem", Tag.TAG_COMPOUND)) {
            ItemStack armor = ItemStack.of(tag.getCompound("ArmorItem"));
            if (!armor.isEmpty() && this.isArmor(armor)) {
                this.inventory.setItem(1, armor);
            }
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        boolean flag = !this.isBaby() && this.isTamed() && player.isSecondaryUseActive();

        if (!this.isVehicle() && !flag) {
            ItemStack itemstack = player.getItemInHand(hand);
            if (!itemstack.isEmpty()) {
                if (this.isFood(itemstack)) {
                    return this.fedFood(player, itemstack);
                }

                if (!this.isTamed()) {
                    this.makeMad();
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }
            }
        }

        return this._AbstractHorse$mobInteract(player, hand);
    }

    @Unique
    public InteractionResult _AbstractHorse$mobInteract(Player p_252289_, InteractionHand p_248927_) {
        if (!this.isVehicle() && !this.isBaby()) {
            if (this.isTamed() && p_252289_.isSecondaryUseActive()) {
                this.openCustomInventoryScreen(p_252289_);
            } else {
                ItemStack itemstack = p_252289_.getItemInHand(p_248927_);
                if (!itemstack.isEmpty()) {
                    InteractionResult interactionresult = itemstack.interactLivingEntity(p_252289_, this, p_248927_);
                    if (interactionresult.consumesAction()) {
                        return interactionresult;
                    }

                    if (this.canWearArmor() && this.isArmor(itemstack) && !this.isWearingArmor()) {
                        this.equipArmor(p_252289_, itemstack);
                        return InteractionResult.sidedSuccess(this.level().isClientSide);
                    }
                }

                this.doPlayerRide(p_252289_);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return _Animal$mobInteract(p_252289_, p_248927_);
        }
    }


    @Unique
    public InteractionResult _Animal$mobInteract(Player p_27584_, InteractionHand p_27585_) {
        ItemStack itemstack = p_27584_.getItemInHand(p_27585_);
        if (this.isFood(itemstack)) {
            int i = this.getAge();
            if (!this.level().isClientSide && i == 0 && this.canFallInLove()) {
                this.usePlayerItem(p_27584_, p_27585_, itemstack);
                this.setInLove(p_27584_);
                return InteractionResult.SUCCESS;
            }

            if (this.isBaby()) {
                this.usePlayerItem(p_27584_, p_27585_, itemstack);
                this.ageUp(getSpeedUpSecondsWhenFeeding(-i), true);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            if (this.level().isClientSide) {
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }



}