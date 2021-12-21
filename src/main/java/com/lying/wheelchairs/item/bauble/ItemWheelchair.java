package com.lying.wheelchairs.item.bauble;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.lying.wheelchairs.client.model.bauble.ModelWheelchair;
import com.lying.wheelchairs.init.WBlocks;
import com.lying.wheelchairs.init.WItems;
import com.lying.wheelchairs.item.WItemGroup;
import com.lying.wheelchairs.reference.Reference;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.WoodType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

public class ItemWheelchair extends ItemBaublePersistent implements IDyeableArmorItem
{
	public static List<Item> WHEELCHAIRS = Lists.newArrayList();
	
	public static final UUID MODIFIER_UUID = UUID.fromString("d26831e4-f088-435f-b23b-e43525f5e0f2");
	private final WoodType woodType;
	
	public ItemWheelchair(Properties properties, WoodType wheelIn)
	{
		super(properties.stacksTo(1).tab(WItemGroup.WHEELCHAIRS));
		this.woodType = wheelIn;
		
		MinecraftForge.EVENT_BUS.addListener(ItemWheelchair::wheelchairStepEvent);
		WHEELCHAIRS.add(this);
	}
	
	public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack){ return true; }
	
	public boolean canRender(String identifier, int index, LivingEntity living, ItemStack stack){ return !living.isInvisible(); }
	
	public boolean hasColor(ItemStack stack)
	{
		return stack.getTagElement("display") != null && stack.getTagElement("display").contains("color", 99);
	}
	
	public int getColor(ItemStack stack)
	{
		return hasColor(stack) ? stack.getTagElement("display").getInt("color") : getDefaultColor();
	}
	
	public int getDefaultColor(){ return DyeColor.RED.getColorValue(); }
	
	public ActionResultType useOn(ItemUseContext context)
	{
		if(context.getPlayer().isCrouching())
		{
			ActionResultType placeResult = this.tryPlace(new BlockItemUseContext(context));
			return !placeResult.consumesAction() && this.getFoodProperties() != null ? this.useOn(context) : placeResult;
		}
		return super.useOn(context);
	}
	
	public ActionResultType tryPlace(BlockItemUseContext context)
	{
		BlockState blockstate = WBlocks.WHEELCHAIR.defaultBlockState();
		if(context == null || !context.canPlace() || !this.canPlace(context, blockstate))
			return ActionResultType.FAIL;
		else
		{
			if(blockstate == null)
				return ActionResultType.FAIL;
			else if (!context.getLevel().setBlock(context.getClickedPos(), blockstate, 11))
				return ActionResultType.FAIL;
			else
			{
				BlockPos blockpos = context.getClickedPos();
				World world = context.getLevel();
				PlayerEntity playerentity = context.getPlayer();
				ItemStack itemstack = context.getItemInHand();
				BlockState blockstate1 = world.getBlockState(blockpos);
				Block block = blockstate1.getBlock();
				if(block == blockstate.getBlock())
				{
					block.setPlacedBy(world, blockpos, blockstate1, playerentity, itemstack);
					if(playerentity instanceof ServerPlayerEntity)
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)playerentity, blockpos, itemstack);
				}
				SoundType soundtype = blockstate1.getSoundType(world, blockpos, context.getPlayer());
				
				SoundEvent placeSound = blockstate1.getSoundType(world, blockpos, playerentity).getPlaceSound();
				world.playSound(playerentity, blockpos, placeSound, SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				if(playerentity == null || !playerentity.isCreative())
					itemstack.shrink(1);
				return ActionResultType.sidedSuccess(world.isClientSide);
			}
		}
	}
	
	protected boolean canPlace(BlockItemUseContext context, BlockState blockState)
	{
		PlayerEntity playerentity = context.getPlayer();
		ISelectionContext iselectioncontext = playerentity == null ? ISelectionContext.empty() : ISelectionContext.of(playerentity);
		return (blockState.canSurvive(context.getLevel(), context.getClickedPos())) && context.getLevel().isUnobstructed(blockState, context.getClickedPos(), iselectioncontext);
	}
	
	public ResourceLocation getTexture(ItemStack stack)
	{
		return new ResourceLocation(Reference.ModInfo.MOD_ID, "textures/models/bauble/"+stack.getItem().getRegistryName().getPath()+".png");
	}
	
	public ResourceLocation getOverlayTexture(ItemStack stack)
	{
		return new ResourceLocation(Reference.ModInfo.MOD_ID, "textures/models/bauble/"+stack.getItem().getRegistryName().getPath()+"_overlay.png");
	}
	
	public static ItemStack setWheels(ItemStack wheelchair, Pair<ItemStack, ItemStack> wheels)
	{
		CompoundNBT stackData = wheelchair.getOrCreateTag();
		ListNBT wheelSet = new ListNBT();
		wheelSet.add(wheels.getFirst().save(new CompoundNBT()));
		wheelSet.add(wheels.getSecond().save(new CompoundNBT()));
		stackData.put("Wheels", wheelSet);
		wheelchair.setTag(stackData);
		return wheelchair;
	}
	
	public static Pair<ItemStack, ItemStack> getWheelsFromItem(ItemStack stack)
	{
		Item defaultWheel = getDefaultWheelFromItem(stack);
		if(stack.getItem() instanceof ItemWheelchair && stack.hasTag())
		{
			CompoundNBT stackData = stack.getTag();
			if(stackData.contains("Wheels", 9))
			{
				ListNBT wheelSet = stackData.getList("Wheels", 10);
				ItemStack wheelR = wheelSet.isEmpty() ? new ItemStack(defaultWheel) : ItemStack.of(wheelSet.getCompound(0));
				ItemStack wheelL = wheelSet.size() < 2 ? new ItemStack(defaultWheel) : ItemStack.of(wheelSet.getCompound(1));
				return Pair.of(wheelR, wheelL);
			}
		}
		return Pair.of(new ItemStack(defaultWheel), new ItemStack(defaultWheel));
	}
	
	@OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flags)
	{
		Pair<ItemStack, ItemStack> wheels = getWheelsFromItem(stack);
		tooltip.add(new TranslationTextComponent("gui."+Reference.ModInfo.MOD_ID+".wheelchair.wheel_right", wheels.getFirst().getDisplayName()).withStyle(TextFormatting.GRAY));
		tooltip.add(new TranslationTextComponent("gui."+Reference.ModInfo.MOD_ID+".wheelchair.wheel_left", wheels.getSecond().getDisplayName()).withStyle(TextFormatting.GRAY));
	}
	
	public static Item getDefaultWheelFromItem(ItemStack stack)
	{
		if(stack.getItem() instanceof ItemWheelchair)
		{
			WoodType type = ((ItemWheelchair)stack.getItem()).woodType;
			if(type == WoodType.OAK)
				return WItems.WHEEL_OAK;
			else if(type == WoodType.SPRUCE)
				return WItems.WHEEL_SPRUCE;
			else if(type == WoodType.BIRCH)
				return WItems.WHEEL_BIRCH;
			else if(type == WoodType.ACACIA)
				return WItems.WHEEL_ACACIA;
			else if(type == WoodType.JUNGLE)
				return WItems.WHEEL_JUNGLE;
			else if(type == WoodType.DARK_OAK)
				return WItems.WHEEL_DARK_OAK;
			else if(type == WoodType.CRIMSON)
				return WItems.WHEEL_CRIMSON;
			else if(type == WoodType.WARPED)
				return WItems.WHEEL_WARPED;
		}
		return WItems.WHEEL_OAK;
	}
	
	@OnlyIn(Dist.CLIENT)
	public void render(String identifier, int index, MatrixStack matrixStackIn, IRenderTypeBuffer renderTypeBuffer,
			int light, LivingEntity living, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, ItemStack stack)
	{
		if(living.isPassenger())
			return;
		
		ModelWheelchair model = new ModelWheelchair();
		IVertexBuilder vertexBuilder = ItemRenderer.getFoilBuffer(renderTypeBuffer, model.renderType(getTexture(stack)), false, stack.isEnchanted());
		model.setupAnim(living, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		model.renderToBuffer(matrixStackIn, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
		
        int i = getColor(stack);
        float r = (float)(i >> 16 & 255) / 255.0F;
        float g = (float)(i >> 8 & 255) / 255.0F;
        float b = (float)(i & 255) / 255.0F;
        
		vertexBuilder = ItemRenderer.getFoilBuffer(renderTypeBuffer, model.renderType(getOverlayTexture(stack)), false, stack.isEnchanted());
		model.setupAnim(living, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		model.renderToBuffer(matrixStackIn, vertexBuilder, light, OverlayTexture.NO_OVERLAY, r, g, b, 1F);
		
		renderWheels(matrixStackIn, renderTypeBuffer, light, living.getPose() == Pose.CROUCHING, limbSwing * 45F, getWheelsFromItem(stack));
	}
	
	public void renderStaticItem(MatrixStack matrixStackIn, IRenderTypeBuffer renderTypeBuffer, int light, ItemStack stack)
	{
		ModelWheelchair model = new ModelWheelchair();
		IVertexBuilder vertexBuilder = ItemRenderer.getFoilBuffer(renderTypeBuffer, model.renderType(getTexture(stack)), false, stack.isEnchanted());
		model.renderToBuffer(matrixStackIn, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
		
        int i = getColor(stack);
        float r = (float)(i >> 16 & 255) / 255.0F;
        float g = (float)(i >> 8 & 255) / 255.0F;
        float b = (float)(i & 255) / 255.0F;
        
		vertexBuilder = ItemRenderer.getFoilBuffer(renderTypeBuffer, model.renderType(getOverlayTexture(stack)), false, stack.isEnchanted());
		model.renderToBuffer(matrixStackIn, vertexBuilder, light, OverlayTexture.NO_OVERLAY, r, g, b, 1F);
		
		renderWheels(matrixStackIn, renderTypeBuffer, light, false, 0F, getWheelsFromItem(stack));
	}
	
	private void renderWheels(MatrixStack matrixStackIn, IRenderTypeBuffer renderTypeBuffer, int light, boolean isSneaking, float spin, Pair<ItemStack, ItemStack> wheels)
	{
		ItemRenderer itemRender = Minecraft.getInstance().getItemRenderer();
		// Right wheel
		matrixStackIn.pushPose();
			matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(180F));
			matrixStackIn.translate(-0.4D, -1D, isSneaking ? -0.375D : 0D);
			matrixStackIn.pushPose();
				matrixStackIn.scale(1F, 1F, 1F);
				matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90F));
				matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(10F));
				matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(spin));
				itemRender.renderStatic(wheels.getFirst(), TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, matrixStackIn, renderTypeBuffer);
			matrixStackIn.popPose();
		matrixStackIn.popPose();
		
		// Left wheel
		matrixStackIn.pushPose();
			matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(180F));
			matrixStackIn.translate(0.4D, -1D, isSneaking ? -0.375D : 0D);
			matrixStackIn.pushPose();
				matrixStackIn.scale(1F, 1F, 1F);
				matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90F));
				matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(-10F));
				matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(spin));
				itemRender.renderStatic(wheels.getSecond(), TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, matrixStackIn, renderTypeBuffer);
			matrixStackIn.popPose();
		matrixStackIn.popPose();
	}
	
	public void onEquip(String identifier, int index, LivingEntity livingEntity, ItemStack stack)
	{
		if(livingEntity.isPassenger())
			livingEntity.stopRiding();
	}
	
	public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack)
	{
		if(livingEntity.isPassenger() && livingEntity.getRandom().nextInt(20) == 0)
		{
			if(livingEntity.getType() == EntityType.PLAYER)
				((PlayerEntity)livingEntity).addItem(stack.copy());
			else
				livingEntity.spawnAtLocation(stack.copy());
			
			if(livingEntity.getType() == EntityType.PLAYER)
			{
				PlayerEntity player = (PlayerEntity)livingEntity;
				ICuriosHelper helper = CuriosApi.getCuriosHelper();
				if(helper.getCuriosHandler(player).isPresent())
				{
					ICuriosItemHandler handler = helper.getCuriosHandler(player).orElse(null);
					Optional<ICurioStacksHandler> stackHandler = handler.getStacksHandler(identifier);
					if(stackHandler.isPresent())
						stackHandler.get().getStacks().extractItem(index, stack.getCount(), false);
				}
			}
		}
	}
	
	public static void wheelchairStepEvent(TickEvent.PlayerTickEvent event)
	{
		PlayerEntity player = event.player;
		boolean hasWheelchair = hasVisibleWheelchair(player);
		
		ModifiableAttributeInstance moveSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);
		boolean hasModifier = moveSpeed.getModifier(MODIFIER_UUID) != null;
		
		/**
		 * Check if player has wheelchair modifier
		 * If check does not match wheelchair presence, rectify
		 */
		if(hasWheelchair != hasModifier)
			if(hasWheelchair)
			{
				// Apply modifier
				moveSpeed.addTransientModifier(new AttributeModifier(MODIFIER_UUID, "wheelchair_step", 0D, Operation.MULTIPLY_TOTAL));
				player.maxUpStep += 0.5F;
			}
			else
			{
				// Remove modifier
				moveSpeed.removeModifier(MODIFIER_UUID);
				player.maxUpStep -= 0.5F;
			}
	}
	
	private static boolean hasVisibleWheelchair(PlayerEntity player)
	{
		ICuriosHelper helper = CuriosApi.getCuriosHelper();
		if(helper.getCuriosHandler(player).isPresent())
		{
			ICuriosItemHandler handler = helper.getCuriosHandler(player).orElse(null);
			for(ICurioStacksHandler curios : handler.getCurios().values())
				for(int slot=0; slot<curios.getStacks().getSlots(); slot++)
				{
					ItemStack visible = curios.getRenders().get(slot) ? curios.getStacks().getStackInSlot(slot) : ItemStack.EMPTY;
					if(!visible.isEmpty() && visible.getItem() instanceof ItemWheelchair)
						return true;
				}
		}
		return false;
	}
}
