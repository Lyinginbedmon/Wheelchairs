package com.lying.wheelchairs.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;

public class ItemWheel extends Item
{
	public ItemWheel(ItemTier tier, Properties properties)
	{
		super(properties.tab(WItemGroup.WHEELCHAIRS));
	}
	
	public int getItemEnchantability(){ return 0; }
}
