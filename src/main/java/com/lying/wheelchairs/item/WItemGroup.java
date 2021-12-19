package com.lying.wheelchairs.item;

import com.lying.wheelchairs.init.WItems;
import com.lying.wheelchairs.reference.Reference;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public abstract class WItemGroup extends ItemGroup
{
	public static final ItemGroup WHEELCHAIRS = new WItemGroup("default")
	{
		public ItemStack makeIcon(){ return WItems.OAK_WHEELCHAIR.getDefaultInstance(); }
	};
	
	public WItemGroup(String labelIn)
	{
		super(Reference.ModInfo.MOD_ID+"."+labelIn);
	}
	
	public abstract ItemStack makeIcon();
}
