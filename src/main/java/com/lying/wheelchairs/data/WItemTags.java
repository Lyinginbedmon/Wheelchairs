package com.lying.wheelchairs.data;

import javax.annotation.Nullable;

import com.lying.wheelchairs.init.WItems;
import com.lying.wheelchairs.reference.Reference;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod.EventBusSubscriber(modid = Reference.ModInfo.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WItemTags extends ItemTagsProvider
{
    public static final IOptionalNamedTag<Item> COSMETIC = ItemTags.createOptional(new ResourceLocation(CuriosApi.MODID, "cosmetic"));
    
	public WItemTags(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(dataGenerator, new WBlockTags(dataGenerator, existingFileHelper), Reference.ModInfo.MOD_ID, existingFileHelper);
		System.out.println("WItemTags instantiated");
	}
	
	@Override
	public String getName()
	{
		return Reference.ModInfo.MOD_NAME+" item tags";
	}
	
	protected void addTags()
	{
		System.out.println("Generating item tags");
		tag(COSMETIC).add(
				WItems.OAK_WHEELCHAIR,
				WItems.SPRUCE_WHEELCHAIR,
				WItems.BIRCH_WHEELCHAIR,
				WItems.ACACIA_WHEELCHAIR,
				WItems.JUNGLE_WHEELCHAIR,
				WItems.DARK_OAK_WHEELCHAIR,
				WItems.CRIMSON_WHEELCHAIR,
				WItems.WARPED_WHEELCHAIR
				);
	}
	
	@SubscribeEvent
    public static void enqueueIMC(final InterModEnqueueEvent event)
    {
        registerCustomCurio("cosmetic", 1, true, false, null);
    }
	
	private static void registerCustomCurio(final String identifier, final int slots, final boolean isEnabled, final boolean isHidden, @Nullable final ResourceLocation icon)
	{
		final SlotTypeMessage.Builder message = new SlotTypeMessage.Builder(identifier);
		message.size(slots);
		if(!isEnabled)
			message.lock();
		
		if(isHidden)
			message.hide();
		
		if (icon != null)
			message.icon(icon);
		
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> message.build());
	}
	
	private static class WBlockTags extends BlockTagsProvider
	{
		public WBlockTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper)
		{
			super(generatorIn, Reference.ModInfo.MOD_ID, existingFileHelper);
		}
		
		protected void addTags(){ }
	}
}
