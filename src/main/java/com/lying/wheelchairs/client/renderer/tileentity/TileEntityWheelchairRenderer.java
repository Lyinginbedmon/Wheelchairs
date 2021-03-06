package com.lying.wheelchairs.client.renderer.tileentity;

import com.lying.wheelchairs.init.WItems;
import com.lying.wheelchairs.item.bauble.ItemWheelchair;
import com.lying.wheelchairs.tileentity.TileEntityWheelchair;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class TileEntityWheelchairRenderer extends TileEntityRenderer<TileEntityWheelchair>
{
	public TileEntityWheelchairRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}
	
	public void render(TileEntityWheelchair tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		ItemStack stack = tileEntityIn.getStack();
		if(stack.isEmpty())
			stack = new ItemStack(WItems.OAK_WHEELCHAIR);
		
		matrixStackIn.pushPose();
			matrixStackIn.translate(0.5D, 1.5D, 0.5D);
			matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(180F));
			matrixStackIn.pushPose();
				matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(tileEntityIn.getYaw()));
				if(stack.getItem() instanceof ItemWheelchair)
					((ItemWheelchair)stack.getItem()).renderStaticItem(matrixStackIn, bufferIn, combinedLightIn, stack);
				else
					Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
			matrixStackIn.popPose();
		matrixStackIn.popPose();
	}
}
