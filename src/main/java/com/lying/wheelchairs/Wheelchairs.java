package com.lying.wheelchairs;

import com.lying.wheelchairs.data.WDataGenerators;
import com.lying.wheelchairs.init.WItems;
import com.lying.wheelchairs.init.WRecipeTypes;
import com.lying.wheelchairs.proxy.ClientProxy;
import com.lying.wheelchairs.proxy.IProxy;
import com.lying.wheelchairs.proxy.ServerProxy;
import com.lying.wheelchairs.reference.Reference;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.ModInfo.MOD_ID)
public class Wheelchairs
{
	public static IProxy proxy = new ServerProxy();
	
	@SuppressWarnings("deprecation")
	public Wheelchairs()
	{
		DistExecutor.callWhenOn(Dist.CLIENT, () -> () -> proxy = new ClientProxy());
		proxy.registerHandlers();
		
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::doCommonSetup);
        bus.addListener(this::doClientSetup);
        bus.addListener(this::doLoadComplete);
        bus.addListener(WDataGenerators::onGatherData);
        
        bus.addGenericListener(Item.class, WItems::onItemsRegistry);
        bus.addGenericListener(IRecipeSerializer.class, WRecipeTypes::onRecipeSerializerRegistry);
        
        MinecraftForge.EVENT_BUS.register(this);
	}
	
    private void doCommonSetup(final FMLCommonSetupEvent event)
    {
    	
    }
    
    private void doClientSetup(final FMLClientSetupEvent event)
    {
    	
    }
    
    private void doLoadComplete(final FMLLoadCompleteEvent event)
    {
    	proxy.onLoadComplete(event);
    }
    
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event)
    {
    	
    }
}
