package com.lying.wheelchairs.data.recipes;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.lying.wheelchairs.init.WItems;
import com.lying.wheelchairs.item.bauble.ItemWheelchair;
import com.lying.wheelchairs.reference.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.CustomRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.Tags;

public class WRecipeProvider extends RecipeProvider
{
	private static final Map<Item, Block> WHEEL_TO_MATS = new HashMap<>();
	
	public WRecipeProvider(DataGenerator generatorIn)
	{
		super(generatorIn);
	}
	
	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer)
	{
		registerSpecialRecipe(consumer, WheelchairRecipe.SERIALIZER);
		
		// Wheels
		for(Item wheel : WHEEL_TO_MATS.keySet())
		{
			Block slab = WHEEL_TO_MATS.get(wheel);
			ShapedRecipeBuilder.shaped(wheel).group("wheels")
				.pattern(" S ")
				.pattern("SsS")
				.pattern(" S ")
				.define('S', slab)
				.define('s', Tags.Items.RODS_WOODEN)
				.unlockedBy("has_"+slab.getRegistryName().getPath(), has(slab)).save(consumer);
		}
		
		// Wheelchairs (non-custom recipe)
		for(Item chair : ItemWheelchair.WHEELCHAIRS)
		{
			Item wheel = ItemWheelchair.getDefaultWheelFromItem(new ItemStack(chair));
			Block slab = WHEEL_TO_MATS.get(wheel);
			ShapedRecipeBuilder.shaped(chair).group("wheelchairs")
				.pattern(" W ")
				.pattern("EsE")
				.define('W', Items.RED_WOOL)
				.define('E', wheel)
				.define('s', slab)
				.unlockedBy("has_"+wheel.getRegistryName().getPath(), has(wheel)).save(consumer);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void registerSpecialRecipe(Consumer<IFinishedRecipe> consumer, SpecialRecipeSerializer<?> serializer)
	{
		ResourceLocation name = Registry.RECIPE_SERIALIZER.getKey(serializer);
		CustomRecipeBuilder.special(serializer).save(consumer, new ResourceLocation(Reference.ModInfo.MOD_ID,"dynamic/"+name.getPath()).toString());
	}
	
	@Override
	public String getName()
	{
		return Reference.ModInfo.MOD_NAME+" crafting recipes";
	}
	
	static
	{
		WHEEL_TO_MATS.put(WItems.WHEEL_ACACIA, Blocks.ACACIA_SLAB);
		WHEEL_TO_MATS.put(WItems.WHEEL_BIRCH, Blocks.BIRCH_SLAB);
		WHEEL_TO_MATS.put(WItems.WHEEL_CRIMSON, Blocks.CRIMSON_SLAB);
		WHEEL_TO_MATS.put(WItems.WHEEL_DARK_OAK, Blocks.DARK_OAK_SLAB);
		WHEEL_TO_MATS.put(WItems.WHEEL_JUNGLE, Blocks.JUNGLE_SLAB);
		WHEEL_TO_MATS.put(WItems.WHEEL_OAK, Blocks.OAK_SLAB);
		WHEEL_TO_MATS.put(WItems.WHEEL_SPRUCE, Blocks.SPRUCE_SLAB);
		WHEEL_TO_MATS.put(WItems.WHEEL_WARPED, Blocks.WARPED_SLAB);
	}
}
