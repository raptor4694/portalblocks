package com.raptor.portalblocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPortalDoor extends BlockDoor {

	public BlockPortalDoor() {
		super(Material.IRON);
	}

	private static final SoundEvent OPEN_SOUND = new SoundEvent(new ResourceLocation("portalblocks:door_open")),
			CLOSE_SOUND = new SoundEvent(new ResourceLocation("portalblocks:door_close"));

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos,
			Block blockIn, BlockPos fromPos) {
		if(state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
			BlockPos blockpos = pos.down();
			IBlockState iblockstate = worldIn.getBlockState(blockpos);

			if(iblockstate.getBlock() != this) {
				worldIn.setBlockToAir(pos);
			}
			else if(blockIn != this) {
				iblockstate.neighborChanged(worldIn, blockpos, blockIn, fromPos);
			}
		}
		else {
			boolean flag1 = false;
			BlockPos posUp = pos.up();
			IBlockState stateUp = worldIn.getBlockState(posUp);

			if(stateUp.getBlock() != this) {
				worldIn.setBlockToAir(pos);
				flag1 = true;
			}

			if(!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(),
					EnumFacing.UP)) {
				worldIn.setBlockToAir(pos);
				flag1 = true;

				if(stateUp.getBlock() == this) {
					worldIn.setBlockToAir(posUp);
				}
			}

			if(flag1) {
				if(!worldIn.isRemote) {
					this.dropBlockAsItem(worldIn, pos, state, 0);
				}
			}
			else {
				EnumHingePosition hinge = state.getValue(HINGE);
				EnumFacing facing = state.getValue(FACING);
				boolean flag = worldIn.isBlockPowered(pos) || worldIn
						.isBlockPowered(posUp) ||
						hinge == EnumHingePosition.LEFT && (worldIn.isBlockPowered(pos.offset(facing.rotateY())) ||
								worldIn.isBlockPowered(pos.offset(facing.rotateY()).up())) ||
						hinge == EnumHingePosition.RIGHT && (worldIn.isBlockPowered(pos.offset(facing.rotateYCCW())) ||
								worldIn.isBlockPowered(pos.offset(facing.rotateYCCW()).up()));

				if(blockIn != this && (flag || blockIn.getDefaultState()
						.canProvidePower()) && flag != stateUp
								.getValue(POWERED).booleanValue()) {
					worldIn.setBlockState(posUp, stateUp
							.withProperty(POWERED, Boolean.valueOf(flag)), 2);

					if(flag != state.getValue(OPEN).booleanValue()) {
						worldIn.setBlockState(pos,
								state.withProperty(OPEN, Boolean.valueOf(flag)), 2);
						worldIn.markBlockRangeForRenderUpdate(pos, pos);
						SoundEvent sound;
						if(flag) {
							// play open sound
							sound = OPEN_SOUND;
						}
						else {
							sound = CLOSE_SOUND;
						}
						worldIn.playSound((EntityPlayer)null, pos,
								sound, SoundCategory.BLOCKS,
								0.1F, 1F);
					}

				}
			}
		}
	}

}
