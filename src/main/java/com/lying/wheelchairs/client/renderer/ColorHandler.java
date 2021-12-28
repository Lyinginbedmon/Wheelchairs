package com.lying.wheelchairs.client.renderer;

import com.lying.wheelchairs.init.WItems;
import com.lying.wheelchairs.item.bauble.ItemWheelchair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;

public class ColorHandler
{
	public static void registerColorHandlers()
	{
		ItemColors registryItems = Minecraft.getInstance().getItemColors();
		
		IItemColor wheelchairColor = (stack, val) -> 
		{
			ItemWheelchair wheelchair = (ItemWheelchair)stack.getItem();
			return val == 0 ? (wheelchair.hasCustomColor(stack) ? wheelchair.getColor(stack) : ItemWheelchair.DEFAULT_COLOR) : -1;
		};
		for(Item wheelchair : WItems.WHEELCHAIRS)
			registryItems.register(wheelchairColor, wheelchair);
	}
}
