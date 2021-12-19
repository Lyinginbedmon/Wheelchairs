package com.lying.wheelchairs.client.model.bauble;

import com.lying.wheelchairs.client.model.ModelUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;

public class ModelWheelchair extends EntityModel<LivingEntity>
{
	private ModelRenderer seat;
	
	public ModelWheelchair()
	{
		this.texHeight = 32;
		this.texWidth = 64;
		
		this.seat = ModelUtils.freshRenderer(this);
		this.seat.setPos(0F, 12F, 0F);
		this.seat.texOffs(0, 0).addBox(-5F, 1F, -6F, 10, 4, 8);	// seat
		this.seat.texOffs(0, 12).addBox(-4.5F, -5F, 2F, 9, 7, 1);	// backrest
		this.seat.texOffs(0, 21).addBox(-7F, 3.5F, -0.5F, 14, 1, 1);	// rear axle
	}
	
	public void setupAnim(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		if(entityIn.getPose() == Pose.CROUCHING)
			this.seat.z = 6F;
		else
			this.seat.z = 0F;
	}
	
	public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		this.seat.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
}
