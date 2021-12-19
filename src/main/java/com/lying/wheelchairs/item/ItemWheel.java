package com.lying.wheelchairs.item;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;

public class ItemWheel extends Item
{
	private final ItemTier tier;
	
	public ItemWheel(ItemTier tier, Properties properties)
	{
		super(properties.group(VEItemGroup.GEAR).maxDamage(tier.getMaxUses()));
		this.tier = tier;
	}
	
	public int getItemEnchantability(){ return 0; }
}
