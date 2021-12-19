package com.lying.wheelchairs.data;

import javax.annotation.Nullable;

import com.lying.wheelchairs.init.VEItems;
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
public class VEItemTags extends ItemTagsProvider
{
    public static final IOptionalNamedTag<Item> COSMETIC = ItemTags.createOptional(new ResourceLocation(CuriosApi.MODID, "cosmetic"));
    
	public VEItemTags(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(dataGenerator, new VEBlockTags(dataGenerator, existingFileHelper), Reference.ModInfo.MOD_ID, existingFileHelper);
	}
	
	@Override
	public String getName()
	{
		return "Various Equipment item tags";
	}
	
	protected void registerTags()
	{
		getOrCreateBuilder(COSMETIC).add(
				VEItems.OAK_WHEELCHAIR,
				VEItems.SPRUCE_WHEELCHAIR,
				VEItems.BIRCH_WHEELCHAIR,
				VEItems.ACACIA_WHEELCHAIR,
				VEItems.JUNGLE_WHEELCHAIR,
				VEItems.DARK_OAK_WHEELCHAIR,
				VEItems.CRIMSON_WHEELCHAIR,
				VEItems.WARPED_WHEELCHAIR
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
	
	private static class VEBlockTags extends BlockTagsProvider
	{
		public VEBlockTags(DataGenerator generatorIn, ExistingFileHelper existingFileHelper)
		{
			super(generatorIn, Reference.ModInfo.MOD_ID, existingFileHelper);
		}
		
		protected void registerTags(){ }
	}
}
