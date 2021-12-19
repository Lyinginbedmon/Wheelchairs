package com.lying.wheelchairs.init;

import com.lying.wheelchairs.reference.Reference;
import com.lying.wheelchairs.tileentity.*;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod.EventBusSubscriber(modid = Reference.ModInfo.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class VETileEntities
{
	public static final TileEntityType<TileEntityWheelchair> WHEELCHAIR	= TileEntityType.Builder.create(TileEntityWheelchair::new, VEBlocks.WHEELCHAIR).build(null);
	
    @SubscribeEvent
	public static void registerTiles(final RegistryEvent.Register<TileEntityType<?>> tileRegistryevent)
	{
    	IForgeRegistry<TileEntityType<?>> registry = tileRegistryevent.getRegistry();
		register(registry, new ResourceLocation(Reference.ModInfo.MOD_ID, "wheelchair_block"), WHEELCHAIR);
	}
    
    private static void register(IForgeRegistry<TileEntityType<?>> registry, ResourceLocation name, IForgeRegistryEntry<TileEntityType<?>> tile)
    {
    	registry.register(tile.setRegistryName(name));
    }
}
