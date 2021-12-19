package com.lying.wheelchairs.proxy;

import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

public interface IProxy 
{
	public default void registerHandlers(){ }
	
	public default void onLoadComplete(FMLLoadCompleteEvent event){ }
}