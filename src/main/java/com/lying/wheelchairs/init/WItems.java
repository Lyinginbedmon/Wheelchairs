package com.lying.wheelchairs.init;

import java.util.List;

import com.google.common.collect.Lists;
import com.lying.wheelchairs.item.IEventListenerItem;
import com.lying.wheelchairs.item.ItemWheel;
import com.lying.wheelchairs.item.bauble.ItemWheelchair;
import com.lying.wheelchairs.reference.Reference;

import net.minecraft.block.WoodType;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class WItems
{
	private static final List<Item> ITEMS = Lists.newArrayList();
	
	public static final List<Item> DYEABLES = Lists.newArrayList();
	public static final List<Item> WHEELCHAIRS = Lists.newArrayList();
	
	// Items
	public static final Item OAK_WHEELCHAIR		= register("oak_wheelchair", new ItemWheelchair(new Item.Properties(), WoodType.OAK));
	public static final Item SPRUCE_WHEELCHAIR	= register("spruce_wheelchair", new ItemWheelchair(new Item.Properties(), WoodType.SPRUCE));
	public static final Item BIRCH_WHEELCHAIR	= register("birch_wheelchair", new ItemWheelchair(new Item.Properties(), WoodType.BIRCH));
	public static final Item ACACIA_WHEELCHAIR	= register("acacia_wheelchair", new ItemWheelchair(new Item.Properties(), WoodType.ACACIA));
	public static final Item JUNGLE_WHEELCHAIR	= register("jungle_wheelchair", new ItemWheelchair(new Item.Properties(), WoodType.JUNGLE));
	public static final Item DARK_OAK_WHEELCHAIR	= register("dark_oak_wheelchair", new ItemWheelchair(new Item.Properties(), WoodType.DARK_OAK));
	public static final Item CRIMSON_WHEELCHAIR	= register("crimson_wheelchair", new ItemWheelchair(new Item.Properties(), WoodType.CRIMSON));
	public static final Item WARPED_WHEELCHAIR	= register("warped_wheelchair", new ItemWheelchair(new Item.Properties(), WoodType.WARPED));
	public static final Item WHEEL_OAK		= register("oak_wheel", new ItemWheel(ItemTier.WOOD, new Item.Properties()));
	public static final Item WHEEL_SPRUCE	= register("spruce_wheel", new ItemWheel(ItemTier.WOOD, new Item.Properties()));
	public static final Item WHEEL_BIRCH	= register("birch_wheel", new ItemWheel(ItemTier.WOOD, new Item.Properties()));
	public static final Item WHEEL_ACACIA	= register("acacia_wheel", new ItemWheel(ItemTier.WOOD, new Item.Properties()));
	public static final Item WHEEL_JUNGLE	= register("jungle_wheel", new ItemWheel(ItemTier.WOOD, new Item.Properties()));
	public static final Item WHEEL_DARK_OAK	= register("dark_oak_wheel", new ItemWheel(ItemTier.WOOD, new Item.Properties()));
	public static final Item WHEEL_CRIMSON	= register("crimson_wheel", new ItemWheel(ItemTier.WOOD, new Item.Properties()));
	public static final Item WHEEL_WARPED	= register("warped_wheel", new ItemWheel(ItemTier.WOOD, new Item.Properties()));
	
	public static Item register(String nameIn, Item itemIn)
	{
		itemIn.setRegistryName(new ResourceLocation(Reference.ModInfo.MOD_ID, nameIn));
		ITEMS.add(itemIn);
		
		if(itemIn instanceof IDyeableArmorItem)
			DYEABLES.add(itemIn);
		if(itemIn instanceof ItemWheelchair)
			WHEELCHAIRS.add(itemIn);
		
		return itemIn;
	}
	
    public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent)
    {
    	IForgeRegistry<Item> registry = itemRegistryEvent.getRegistry();
    	registry.registerAll(ITEMS.toArray(new Item[0]));
    	
    	ITEMS.forEach((item) -> 
    	{
    		if(item instanceof IEventListenerItem)
    			((IEventListenerItem)item).addListeners(MinecraftForge.EVENT_BUS);
    	});
    }
}
