package com.lying.wheelchairs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lying.wheelchairs.item.bauble.ItemWheelchair;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

@OnlyIn(Dist.CLIENT)
@Mixin(BipedModel.class)
public class BipedModelMixin extends EntityModelMixin
{
	@Shadow
	public ModelRenderer leftLeg;
	@Shadow
	public ModelRenderer rightLeg;
	@Shadow
	public float swimAmount;
	
	@Inject(method = "prepareMobModel(Lnet/minecraft/entity/LivingEntity;FFF)V", at = @At("HEAD"))
	public void setLivingAnimations(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick, final CallbackInfo ci)
	{
		riding = riding || shouldForceSit(entityIn);
	}
	
	@Inject(method = "setupAnim(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("RETURN"))
	public void setRotationAngles(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, final CallbackInfo ci)
	{
		// Poses can overrule the sitting pose, so we put it back here
		if(shouldForceSit(entityIn) && swimAmount > 0F)
		{
			this.rightLeg.xRot = -1.4137167F;
			this.rightLeg.yRot = ((float)Math.PI / 10F);
			this.rightLeg.zRot = 0.07853982F;
			this.leftLeg.xRot = -1.4137167F;
			this.leftLeg.yRot = (-(float)Math.PI / 10F);
			this.leftLeg.zRot = -0.07853982F;
		}
	}
	
	private static boolean shouldForceSit(LivingEntity entityIn)
	{
		if(entityIn.getType() == EntityType.PLAYER)
		{
			PlayerEntity player = (PlayerEntity)entityIn;
			ICuriosHelper helper = CuriosApi.getCuriosHelper();
			if(helper.getCuriosHandler(player).isPresent())
			{
				ICuriosItemHandler handler = helper.getCuriosHandler(player).orElse(null);
				for(ICurioStacksHandler stacks : handler.getCurios().values())
					for(int i=0; i<stacks.getSlots(); i++)
						if(stacks.getRenders().get(i))
						{
							ItemStack stack = stacks.getStacks().getStackInSlot(i);
							if(!stack.isEmpty() && stack.getItem() instanceof ItemWheelchair)
								return true;
						}
			}
		}
		return false;
	}
}
