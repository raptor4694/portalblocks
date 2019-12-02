package com.raptor.portalblocks.items;

import com.raptor.portalblocks.PortalBlocks;
import com.raptor.portalblocks.blocks.BlockPanel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
/*
public class ItemLight extends ItemBlock{
	private Light light_left_top, light_right_top;
	private String unlocalizedName;
	
	public ItemLight(Light left_top, Light right_top){
		super(left_top);
		light_left_top = left_top;
		light_right_top = right_top;
	}
	
	@Override
	public Item setUnlocalizedName(String unlocalizedName){
		this.unlocalizedName = unlocalizedName;
		return this;
	}
	
	@Override
	public String getUnlocalizedName(){
		return "item." + this.unlocalizedName;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		return getUnlocalizedName();
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world,
			BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY,
			float hitZ){
		ItemStack itemstack = player.getHeldItem(hand);
		
		if(itemstack.isEmpty()) return EnumActionResult.FAIL;
		
		IBlockState iblockstate = world.getBlockState(pos);
		// Block block = iblockstate.getBlock();
		
		boolean isLeftSide = true;
		
		EnumFacing horizontalFacing = player.getAdjustedHorizontalFacing();
		
		if(horizontalFacing == EnumFacing.NORTH)
			isLeftSide = hitX <= 0.5f;
		else if(horizontalFacing == EnumFacing.EAST)
			isLeftSide = hitZ <= 0.5f;
		else if(horizontalFacing == EnumFacing.SOUTH)
			isLeftSide = hitX >= 0.5f;
		else if(horizontalFacing == EnumFacing.WEST) isLeftSide = hitZ >= 0.5f;
		
		Light light = isLeftSide? light_left_top : light_right_top;
		
		// System.out.println("\nlight placed: hitX = " + hitX + ", hitY = " + hitY + ", hitZ = " + hitZ);
		
		if(!iblockstate.getBlock().isReplaceable(world, pos)){
			Block block = iblockstate.getBlock();
			boolean isOtherLeft = block == PortalBlocks.light_left_bottom || block == PortalBlocks.light_left_top;
			if(block instanceof Light
					&& block != PortalBlocks.LIGHT_ALL_BOTTOM 
					&& block != PortalBlocks.LIGHT_ALL_TOP 
					&& isLeftSide != isOtherLeft){
				IBlockState iblockstate1 = light.getStateForPlacement(world, pos,
						facing, hitX, hitY, hitZ, 0, player, hand);
				
				EnumFacing facing1 = iblockstate1.getValue(Light.FACING);
				EnumOrientation orientation1 = iblockstate1
						.getValue(Light.ORIENTATION);
				
				if(orientation1 == iblockstate
						.getValue(Light.ORIENTATION) && facing1 == iblockstate
								.getValue(Light.FACING)){
					Light lightBlock = ((Light)block).isTopHalf? PortalBlocks.LIGHT_ALL_TOP : PortalBlocks.LIGHT_ALL_BOTTOM;
					world.setBlockState(pos,
							lightBlock.getDefaultState()
									.withProperty(Light.ORIENTATION, orientation1)
									.withProperty(Light.FACING, facing1), world.isRemote? 10 : 2);
					world.setBlockState(((Light)block).getOtherHalfPos(iblockstate, pos),
							lightBlock.getOtherHalf().getDefaultState()
							.withProperty(Light.ORIENTATION, orientation1)
							.withProperty(Light.FACING, facing1), world.isRemote? 11 : 3);
					itemstack.shrink(1);
					return EnumActionResult.SUCCESS;
				}
			}
			pos = pos.offset(facing);
		}
		
		if(!itemstack.isEmpty() && player.canPlayerEdit(pos, facing,
				itemstack) && world.mayPlace(light, pos, false, facing,
						(Entity)null)){
			int i = this.getMetadata(itemstack.getMetadata());
			IBlockState iblockstate1 = this.block.getStateForPlacement(worldIn, pos,
					facing, hitX, hitY, hitZ, i, player, hand);
			
			
			IBlockState iblockstate1 = light.getStateForPlacement(world, pos,
					facing, hitX, hitY, hitZ, 0, player, hand);
			
			horizontalFacing = iblockstate1.getValue(Light.FACING);
			EnumOrientation orientation = iblockstate1.getValue(Light.ORIENTATION);
			
			BlockPos otherPos;
			
			if(orientation == EnumOrientation.SIDE){
				BlockPos posBehind = pos.offset(horizontalFacing.getOpposite());
				IBlockState behindState = world.getBlockState(posBehind);
				
				if(!behindState.isSideSolid(world, posBehind, horizontalFacing) && !(behindState.getBlock() instanceof BlockPanel)){
					System.out.println("\nside behind was not solid");
					return EnumActionResult.FAIL;
				}
				
				BlockPos posDown = pos.down();
				IBlockState downState = world.getBlockState(posDown);
				
				if(!downState.getBlock().isReplaceable(world, posDown)){
					System.out.println("\nblock down was not replaceable");
					return EnumActionResult.FAIL;
				}
				
				BlockPos posDownBehind = posBehind.down();
				IBlockState downBehindState = world.getBlockState(posDownBehind);
				
				if(!downBehindState.isSideSolid(world, posDownBehind,
						horizontalFacing) && !(downBehindState.getBlock() instanceof BlockPanel)){
					System.out.println("\nside down behind was not solid");
					return EnumActionResult.FAIL;
				}
				
				otherPos = posDown;
			}else{
				EnumFacing verticalOpposite = orientation.getOpposite()
						.toEnumFacing();
				
				BlockPos posBehind = pos.offset(verticalOpposite);
				IBlockState behindState = world.getBlockState(posBehind);
				
				if(!behindState.isSideSolid(world, posBehind,
						orientation.toEnumFacing()) && !(behindState.getBlock() instanceof BlockPanel)){
					System.out.println("\nside above/below was not solid");
					return EnumActionResult.FAIL;
				}
				
				horizontalFacing = horizontalFacing.getOpposite();
				
				BlockPos posAside = pos.offset(horizontalFacing);
				IBlockState asideState = world.getBlockState(posAside);
				
				if(!asideState.getBlock().isReplaceable(world, posAside)){
					System.out.println("\nblock aside was not replaceable");
					return EnumActionResult.FAIL;
				}
				
				BlockPos posAsideBehind = posBehind.offset(horizontalFacing);
				IBlockState asideBehindState = world.getBlockState(posAsideBehind);
				
				if(!asideBehindState.isSideSolid(world, posAsideBehind,
						orientation.toEnumFacing()) && !(asideBehindState.getBlock() instanceof BlockPanel)){
					System.out.println("\nside aside above/below was not solid");
					return EnumActionResult.FAIL;
				}
				
				otherPos = posAside;
			}
			
			boolean b1 = world.setBlockState(otherPos,
					light.getLowerHalfStateForPlacement(iblockstate1), 2);
			boolean b2 = placeBlockAt(itemstack, player, world, pos, facing, hitX,
					hitY,
					hitZ, iblockstate1);
			System.out.println(
					"\nplaced lower half successfully: " + b1 + "\nplaced upper half successfully: " + b2);
			
			if(b1 && b2){
				
				iblockstate1 = world.getBlockState(pos);
				SoundType soundtype = iblockstate1.getBlock()
						.getSoundType(iblockstate1, world, pos, player);
				world.playSound(player, pos, soundtype.getPlaceSound(),
						SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F,
						soundtype.getPitch() * 0.8F);
				itemstack.shrink(1);
			}
			
			return EnumActionResult.SUCCESS;
		}else{
			return EnumActionResult.FAIL;
		}
	}
	
}
*/