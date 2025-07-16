package com.rinko1231.tameablezombieskeletonhorse.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.DyeableHorseArmorItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class GenericHorseArmorLayer<T extends AbstractHorse, M extends HorseModel<T>> extends RenderLayer<T, M> {
    private final M model;

    public GenericHorseArmorLayer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet) {
        super(renderer);
        // 使用原版马铠模型层
        this.model = (M) new HorseModel<>(modelSet.bakeLayer(ModelLayers.HORSE_ARMOR));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTicks,
                       float ageInTicks, float netHeadYaw, float headPitch) {

        // 使用通用装备槽位获取马铠
        ItemStack armorStack = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (!(armorStack.getItem() instanceof HorseArmorItem)) return;

        HorseArmorItem armorItem = (HorseArmorItem) armorStack.getItem();

        // 同步模型状态
        this.getParentModel().copyPropertiesTo(this.model);
        this.model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
        this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        // 处理染色马铠
        float r = 1.0F, g = 1.0F, b = 1.0F;
        if (armorItem instanceof DyeableHorseArmorItem dyeable) {
            int color = dyeable.getColor(armorStack);
            r = (color >> 16 & 0xFF) / 255.0F;
            g = (color >> 8 & 0xFF) / 255.0F;
            b = (color & 0xFF) / 255.0F;
        }

        // 渲染马铠
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(armorItem.getTexture()));
        this.model.renderToBuffer(
                poseStack, vertexConsumer, packedLight,
                OverlayTexture.NO_OVERLAY, r, g, b, 1.0F
        );
    }
}