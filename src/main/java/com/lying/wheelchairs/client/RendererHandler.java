package com.lying.wheelchairs.client;

import com.lying.wheelchairs.client.renderer.tileentity.TileEntityWheelchairRenderer;
import com.lying.wheelchairs.init.WTileEntities;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class RendererHandler
{
	private static boolean registered = false;
	
	public static void registerTileRenderers(ModelRegistryEvent event)
	{
		if(!registered)
			registered = true;
		
		ClientRegistry.bindTileEntityRenderer(WTileEntities.WHEELCHAIR, TileEntityWheelchairRenderer::new);
	}
}
