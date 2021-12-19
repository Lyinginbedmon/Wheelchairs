package com.lying.wheelchairs.init;

import com.lying.wheelchairs.data.recipes.WheelchairRecipe;
import com.lying.wheelchairs.reference.Reference;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;

public class WRecipeTypes
{
	public static void onRecipeSerializerRegistry(RegistryEvent.Register<IRecipeSerializer<?>> event)
	{
		event.getRegistry().register(WheelchairRecipe.SERIALIZER.setRegistryName(new ResourceLocation(Reference.ModInfo.MOD_ID, "wheelchair")));
	}
	
	public static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T>
	{
		public String toString(){ return Registry.RECIPE_TYPE.getKey(this).toString(); }
	}
}
