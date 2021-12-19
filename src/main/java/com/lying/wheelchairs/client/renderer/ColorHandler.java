package com.lying.wheelchairs.client.renderer;

import com.lying.wheelchairs.init.VEItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;

public class ColorHandler
{
	public static void registerColorHandlers()
	{
		ItemColors registryItems = Minecraft.getInstance().getItemColors();
		
		for(Item item : VEItems.DYEABLES)
			registryItems.register((stack, val) -> { return val == 0 ? ((IDyeableArmorItem)stack.getItem()).getColor(stack) : -1; }, item);
	}
}
