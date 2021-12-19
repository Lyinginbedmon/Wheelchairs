package com.lying.wheelchairs.client.model;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.vector.Vector3d;

public class ModelUtils
{
	/** 180 degrees expressed as radians */
	public static final float degree180 = (float)(Math.toRadians(180D));
	/** 90 degrees expressed as radians */
	public static final float degree90 = (float)(Math.toRadians(90D));
	/** 10 degrees expressed as radians */
	public static final float degree10 = (float)(Math.toRadians(10D));
	/** 5 degrees expressed as radians */
	public static final float degree5 = (float)(Math.toRadians(5D));
	
	/** Converts a given double from degrees to radians as a float */
	public static float toRadians(double par1Double){ return (float)(Math.toRadians(par1Double)); }
	
	public static ModelRenderer freshRenderer(Model par1ModelBase){ return new ModelRenderer(par1ModelBase).setTexSize(par1ModelBase.texWidth,par1ModelBase.texHeight); }
	
	public static ModelRenderer clonePosition(ModelRenderer fromModel, ModelRenderer toModel)
	{
		toModel.setPos(fromModel.x, fromModel.y, fromModel.z);
		return toModel;
	}
	
	public static ModelRenderer cloneRotation(ModelRenderer fromModel, ModelRenderer toModel)
	{
		toModel.xRot = fromModel.xRot;
		toModel.yRot = fromModel.yRot;
		toModel.zRot = fromModel.zRot;
		return toModel;
	}
	
	public static ModelRenderer shiftWithRotation(ModelRenderer par1ModelRenderer, Vector3d angle, Vector3d shift)
	{
//		Vec3d newVec = shift.rotatePitch((float)angle.x).rotateYaw((float)angle.y);
		Vector3d newVec = new Vector3d(0,0,0);
		
		par1ModelRenderer.x += newVec.x;
		par1ModelRenderer.y += newVec.y;
		par1ModelRenderer.z += newVec.z;
		
		return par1ModelRenderer;
	}
	
	public static Vector3d getAngles(ModelRenderer par1ModelRenderer)
	{
		return new Vector3d(par1ModelRenderer.xRot, par1ModelRenderer.yRot, par1ModelRenderer.zRot);
	}
	
	public static Vector3d getPosition(ModelRenderer par1ModelRenderer)
	{
		return new Vector3d(par1ModelRenderer.x, par1ModelRenderer.y, par1ModelRenderer.z);
	}
}

