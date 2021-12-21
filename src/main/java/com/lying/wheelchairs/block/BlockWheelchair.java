package com.lying.wheelchairs.block;

import javax.annotation.Nullable;

import com.lying.wheelchairs.data.WItemTags;
import com.lying.wheelchairs.init.WTileEntities;
import com.lying.wheelchairs.item.bauble.ItemWheelchair;
import com.lying.wheelchairs.tileentity.TileEntityWheelchair;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

public class BlockWheelchair extends ContainerBlock
{
	private static final VoxelShape SHAPE = box(1D, 0D, 1D, 15D, 15D, 15D);
	
	public BlockWheelchair(AbstractBlock.Properties builder)
	{
		super(builder.noCollission().noOcclusion().noDrops());
	}
	
	public TileEntity newBlockEntity(IBlockReader p_196283_1_)
	{
		return new TileEntityWheelchair();
	}
	
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}
	
	public boolean isTransparent(BlockState state){ return true; }
	
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		TileEntityWheelchair tile = (TileEntityWheelchair)worldIn.getBlockEntity(pos);
		if(tile == null || tile.getStack().isEmpty() || !(tile.getStack().getItem() instanceof ItemWheelchair))
			return ActionResultType.PASS;
		
		int targetSlot = -1;
		ICuriosHelper helper = CuriosApi.getCuriosHelper();
		if(!helper.getCuriosHandler(player).isPresent())
			return ActionResultType.PASS;
		
		ICuriosItemHandler handler = helper.getCuriosHandler(player).orElse(null);
		ICurioStacksHandler stacks = handler.getCurios().get(WItemTags.COSMETIC.getName().getPath());
		for(int i=0; i<stacks.getSlots(); i++)
			if(stacks.getStacks().getStackInSlot(i).isEmpty())
			{
				targetSlot = i;
				break;
			}
		
		if(targetSlot >= 0)
		{
			player.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
			player.setYHeadRot(tile.getYaw());
			
			stacks.getRenders().set(targetSlot, true);
			
			if(worldIn.isClientSide)
				return ActionResultType.SUCCESS;
			else
			{
				stacks.getStacks().setStackInSlot(targetSlot, tile.getStack());
				
				tile.setItem(ItemStack.EMPTY);
				worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				return ActionResultType.CONSUME;
			}
		}
		else
			return ActionResultType.PASS;
	}
	
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
	{
		TileEntityWheelchair tile = (TileEntityWheelchair)worldIn.getBlockEntity(pos);
		tile.setItemAndYaw(stack.copy(), placer.yRot + 180F);
	}
	
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		TileEntity tile = worldIn.getBlockEntity(pos);
		if(tile != null && tile.getType() == WTileEntities.WHEELCHAIR)
		{
			TileEntityWheelchair wheelchair = (TileEntityWheelchair)tile;
			ItemStack stack = wheelchair.getStack();
			if(!stack.isEmpty())
			{
				ItemEntity itemDrop = new ItemEntity(worldIn, (double)pos.getX() + 0.5D, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5D, stack);
				itemDrop.setDefaultPickUpDelay();
				worldIn.addFreshEntity(itemDrop);
				wheelchair.setItemAndYaw(ItemStack.EMPTY, wheelchair.getYaw());
			}
		}
	}
	
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
		return Block.canSupportCenter(worldIn, pos.offset(Direction.DOWN.getNormal()), Direction.UP);
	}
	
	public PushReaction getPushReaction(BlockState state)
	{
		return PushReaction.DESTROY;
	}
}
