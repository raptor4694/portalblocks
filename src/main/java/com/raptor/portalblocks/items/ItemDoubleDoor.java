package com.raptor.portalblocks.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDoubleDoor extends Item {
	protected final Block block;
	
	public ItemDoubleDoor(Block block) {
		this.block = block;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if(side != EnumFacing.UP) {
			return EnumActionResult.FAIL;
		}
		else {
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();

			if(!block.isReplaceable(worldIn, pos)) {
				pos = pos.offset(side);
			}

			ItemStack itemstack = player.getHeldItem(hand);

			if(player.canPlayerEdit(pos, side, itemstack) && this.block.canPlaceBlockAt(worldIn, pos)) {
				EnumFacing enumfacing = EnumFacing.fromAngle(player.rotationYaw);
				int i = enumfacing.getFrontOffsetX();
				int j = enumfacing.getFrontOffsetZ();
				boolean isRightHinge = i < 0 && hitZ < 0.5F || i > 0 && hitZ > 0.5F || j < 0 && hitX > 0.5F || j > 0 && hitX < 0.5F;
				if(placeDoor(worldIn, pos, enumfacing, this.block, isRightHinge, player, side, itemstack)) {
					SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos),
							worldIn, pos, player);
					worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS,
							(soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					itemstack.shrink(1);
					return EnumActionResult.SUCCESS;
				}
			}
			
			return EnumActionResult.FAIL;
		}
	}

	public static boolean placeDoor(World worldIn, BlockPos pos, EnumFacing facing, Block door, boolean isRightHinge, EntityPlayer player, EnumFacing side, ItemStack itemstack) {
		BlockPos posCW  = pos.offset(facing.rotateY());
		BlockPos posCCW = pos.offset(facing.rotateYCCW());
		int i = (worldIn.getBlockState(posCCW).isNormalCube()? 1 : 0) +
				(worldIn.getBlockState(posCCW.up()).isNormalCube()? 1 : 0);
		int j = (worldIn.getBlockState(posCW).isNormalCube()? 1 : 0) +
				(worldIn.getBlockState(posCW.up()).isNormalCube()? 1 : 0);
		boolean flag = worldIn.getBlockState(posCCW).getBlock() == door ||
				worldIn.getBlockState(posCCW.up()).getBlock() == door;
		boolean flag1 = worldIn.getBlockState(posCW).getBlock() == door ||
				worldIn.getBlockState(posCW.up()).getBlock() == door;

		if((!flag || flag1) && j <= i) {
			if(flag1 && !flag || j < i) {
				isRightHinge = false;
			}
		}
		else {
			isRightHinge = true;
		}

		BlockPos posUp = pos.up();
		boolean isPowered = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(posUp);
		
		BlockPos posAdjacent = isRightHinge? posCW : posCCW;
		if(!player.canPlayerEdit(posAdjacent, side, itemstack) || !door.canPlaceBlockAt(worldIn, posAdjacent))
			return false;
		BlockPos posAdjacentUp = posAdjacent.up();
		
		IBlockState iblockstate = door.getDefaultState()
				.withProperty(BlockDoor.FACING, facing)
				.withProperty(BlockDoor.HINGE, isRightHinge? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT)
				.withProperty(BlockDoor.POWERED, isPowered)
				.withProperty(BlockDoor.OPEN, isPowered);
		
		worldIn.setBlockState(pos, iblockstate.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER), 2);
		worldIn.setBlockState(posUp, iblockstate.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 2);
		
		iblockstate = iblockstate.withProperty(BlockDoor.HINGE, isRightHinge? BlockDoor.EnumHingePosition.LEFT : BlockDoor.EnumHingePosition.RIGHT);
		worldIn.setBlockState(posAdjacent, iblockstate.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER), 2);
		worldIn.setBlockState(posAdjacentUp, iblockstate.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 2);
		
		worldIn.notifyNeighborsOfStateChange(pos, door, false);
		worldIn.notifyNeighborsOfStateChange(posUp, door, false);
		worldIn.notifyNeighborsOfStateChange(posAdjacent, door, false);
		worldIn.notifyNeighborsOfStateChange(posAdjacentUp, door, false);
		
		return true;
	}

}
