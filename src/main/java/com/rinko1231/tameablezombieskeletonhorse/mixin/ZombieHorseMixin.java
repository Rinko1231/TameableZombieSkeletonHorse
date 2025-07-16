package com.rinko1231.tameablezombieskeletonhorse.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(value = ZombieHorse.class)
public abstract class ZombieHorseMixin extends AbstractHorse {

    protected ZombieHorseMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }
    @Unique
    private static final UUID ARMOR_MODIFIER_UUID =
            UUID.fromString("a2cfad3f-8792-4e10-a66d-7a55c4e0e6c7");

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (!this.inventory.getItem(1).isEmpty()) {
            tag.put("ArmorItem", this.inventory.getItem(1).save(new CompoundTag()));
        }
    }

    @Unique
    public ItemStack Uma$getArmor() {
        return this.getItemBySlot(EquipmentSlot.CHEST);
    }

    @Unique
    private void ba_painting$setArmor(ItemStack p_30733_) {
        this.setItemSlot(EquipmentSlot.CHEST, p_30733_);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ArmorItem", Tag.TAG_COMPOUND)) {
            ItemStack armor = ItemStack.of(tag.getCompound("ArmorItem"));
            if (!armor.isEmpty() && this.isArmor(armor)) {
                this.inventory.setItem(1, armor); // 索引1是马铠槽位
            }
        }
    }

    @Override
    protected void updateContainerEquipment() {
        if (!this.level().isClientSide) {
            super.updateContainerEquipment();
            this.ba_painting$setArmorEquipment(this.inventory.getItem(1));
            this.setDropChance(EquipmentSlot.CHEST, 0.0F);
        }
    }
    @Override
    public void containerChanged(@NotNull Container container) {
        ItemStack itemstack = this.Uma$getArmor();
        super.containerChanged(container);
        ItemStack itemstack1 = this.Uma$getArmor();
        if (this.tickCount > 20 && this.isArmor(itemstack1) && itemstack != itemstack1) {
            this.playSound(SoundEvents.HORSE_ARMOR, 0.5F, 1.0F);
        }
    }


    @Unique
    private void ba_painting$setArmorEquipment(ItemStack p_30735_) {
        this.ba_painting$setArmor(p_30735_);
        if (!this.level().isClientSide) {
            this.getAttribute(Attributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
            if (this.isArmor(p_30735_)) {
                int i = ((HorseArmorItem)p_30735_.getItem()).getProtection();
                if (i != 0) {
                    this.getAttribute(Attributes.ARMOR).addTransientModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", (double)i, AttributeModifier.Operation.ADDITION));
                }
            }
        }

    }


    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    public void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        boolean flag = this.isTamed() && player.isSecondaryUseActive();
        if (!this.isVehicle() && !flag) {
            ItemStack itemstack = player.getItemInHand(hand);
            if (!itemstack.isEmpty()) {
                if (this.isFood(itemstack)) {
                    cir.setReturnValue(this.fedFood(player, itemstack));
                    cir.cancel();
                    return;
                }
                if (!this.isTamed()) {
                    this.makeMad();
                    cir.setReturnValue(InteractionResult.SUCCESS);
                    cir.cancel();
                    return;
                }
            }
        }
        cir.setReturnValue(super.mobInteract(player, hand));
        cir.cancel();
    }

    @Override
    public boolean canWearArmor() {
        return true;
    }

    @Override
    public boolean isArmor(ItemStack p_30731_) {
        return p_30731_.getItem() instanceof HorseArmorItem;
    }
}