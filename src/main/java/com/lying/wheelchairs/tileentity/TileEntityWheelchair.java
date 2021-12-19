package com.lying.wheelchairs.tileentity;

import javax.annotation.Nullable;

import com.lying.wheelchairs.init.WTileEntities;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;

public class TileEntityWheelchair extends TileEntity
{
	private ItemStack stack = ItemStack.EMPTY;
	private float yaw = 0F;
	
	public TileEntityWheelchair()
	{
		super(WTileEntities.WHEELCHAIR);
	}
	
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		compound.putFloat("Rotation", this.yaw);
		compound.put("Item", this.stack.save(new CompoundNBT()));
		return compound;
	}
	
	public void load(BlockState state, CompoundNBT nbt)
	{
		super.load(state, nbt);
		setItemAndYaw(ItemStack.of(nbt.getCompound("Item")), nbt.getFloat("Rotation"));
	}
	
	public void setItem(ItemStack stackIn)
	{
		this.stack = stackIn.copy();
		markDirty();
	}
	
	public void setItemAndYaw(ItemStack stackIn, float yawIn)
	{
		this.yaw = yawIn;
		this.stack = stackIn.copy();
		markDirty();
	}
	
	public float getYaw(){ return this.yaw; }
	
	public ItemStack getStack(){ return this.stack.copy(); }
	
	public void markDirty()
	{
		super.setChanged();
		if(getLevel() instanceof ServerWorld)
		{
			getLevel().updateNeighbourForOutputSignal(getBlockPos(), getBlockState().getBlock());
			SUpdateTileEntityPacket packet = getUpdatePacket();
			if(packet != null)
			{
				BlockPos pos = getBlockPos();
				((ServerChunkProvider)getLevel().getChunkSource()).chunkMap
						.getPlayers(new ChunkPos(pos), false)
						.forEach(e -> e.connection.send(packet));
			}
		}
	}
	
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.getBlockPos(), -999, this.getUpdateTag());
	}
	
	public CompoundNBT getUpdateTag()
	{
		return this.save(new CompoundNBT());
	}
	
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet)
	{
		super.onDataPacket(net, packet);
		this.load(getBlockState(), packet.getTag());
	}
}
