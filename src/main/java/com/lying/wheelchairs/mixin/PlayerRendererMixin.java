package com.lying.wheelchairs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lying.wheelchairs.item.bauble.ILimbCosmetic;
import com.lying.wheelchairs.item.bauble.ILimbCosmetic.LimbType;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin
{
	@Inject(method = "setModelVisibilities(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;)V", at = @At("RETURN"), cancellable = true)
	private void setModelVisibilities(AbstractClientPlayerEntity player, final CallbackInfo ci)
	{
		PlayerModel<AbstractClientPlayerEntity> model = ((PlayerRenderer)(Object)this).getModel();
		for(LimbType type : LimbType.values())
			if(ILimbCosmetic.isWearingLimbsOfType(player, type))
				switch(type)
				{
					case ARM_LEFT:
						model.leftArm.visible = false;
						model.leftSleeve.visible = false;
						break;
					case ARM_RIGHT:
						model.rightArm.visible = false;
						model.rightSleeve.visible = false;
						break;
					case HEAD:
						model.head.visible = false;
						model.hat.visible = false;
						break;
					case LEG_LEFT:
						model.leftLeg.visible = false;
						model.leftPants.visible = false;
						break;
					case LEG_RIGHT:
						model.rightLeg.visible = false;
						model.rightPants.visible = false;
						break;
					case TORSO:
						model.body.visible = false;
						model.jacket.visible = false;
						break;
				}
	}
}
