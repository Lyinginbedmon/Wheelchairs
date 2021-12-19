package com.lying.wheelchairs.client;

import com.lying.wheelchairs.Wheelchairs;
import com.lying.wheelchairs.config.ConfigVE;
import com.lying.wheelchairs.client.renderer.tileentity.TileEntityWheelchairRenderer;
import com.lying.wheelchairs.init.VETileEntities;

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
		if(ConfigVE.GENERAL.verboseLogs())
			VariousEquipment.log.info("Registering tile entity renderers");
		
		if(!registered)
			registered = true;
		
		ClientRegistry.bindTileEntityRenderer(VETileEntities.WHEELCHAIR, TileEntityWheelchairRenderer::new);
	}
}
