package com.lying.wheelchairs.data;

import com.lying.wheelchairs.data.recipes.WRecipeProvider;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class WDataGenerators
{
	public static void onGatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		if(event.includeServer())
		{
			generator.addProvider(new WItemTags(generator, existingFileHelper));
			generator.addProvider(new WRecipeProvider(generator));
		}
		if(event.includeClient())
		{
			;
		}
	}
}
