package com.lying.wheelchairs.init;

import java.util.ArrayList;
import java.util.List;

import com.lying.wheelchairs.block.BlockWheelchair;
import com.lying.wheelchairs.reference.Reference;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod.EventBusSubscriber(modid = Reference.ModInfo.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class VEBlocks
{
	private static final List<Block> BLOCKS = new ArrayList<>();
	
	/* Blocks */
	public static final Block WHEELCHAIR		= register("wheelchair_block", new BlockWheelchair(AbstractBlock.Properties.create(Material.WOOD)));
	
	public static Block register(String nameIn, Block blockIn)
	{
		blockIn.setRegistryName(Reference.ModInfo.MOD_PREFIX+nameIn);
		BLOCKS.add(blockIn);
		return blockIn;
	}
	
    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
    {
    	blockRegistryEvent.getRegistry().registerAll(BLOCKS.toArray(new Block[0]));
    }
}
