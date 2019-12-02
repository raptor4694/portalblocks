package com.raptor.portalblocks.blocks;

import com.raptor.portalblocks.PortalSounds;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPortalDoor extends BlockDoor {

	public BlockPortalDoor() {
		super(Material.IRON);
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return super.canPlaceBlockAt(worldIn, pos);
		/*if(pos.getY() >= worldIn.getHeight() - 1) {
			return false;
		}
		else {
			IBlockState state = worldIn.getBlockState(pos.down());
			return (state.isTopSolid() || state.getBlock() instanceof BlockPanel ||
					state.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID) &&
					super.canPlaceBlockAt(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos.up());
		}*/
	}
	
	public static BlockPos getNeighborPos(IBlockState state, BlockPos pos) {
		EnumFacing facing = state.getValue(BlockDoor.FACING);
		return pos.offset(state.getValue(BlockDoor.HINGE) == BlockDoor.EnumHingePosition.LEFT? facing.rotateY() : facing.rotateYCCW());
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, final BlockPos pos, Block blockIn, BlockPos fromPos) {
		if(state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
			BlockPos posDown = pos.down();
			IBlockState stateDown = worldIn.getBlockState(posDown);

			if(stateDown.getBlock() != this || worldIn.getBlockState(getNeighborPos(state, pos)).getBlock() != this
					|| worldIn.getBlockState(getNeighborPos(stateDown, posDown)).getBlock() != this) {
				worldIn.setBlockToAir(pos);
			}
			else if(blockIn != this) {
				stateDown.neighborChanged(worldIn, posDown, blockIn, fromPos);
			}
		}
		else {
			boolean flag1 = false;
			BlockPos posUp = pos.up();
			IBlockState stateUp = worldIn.getBlockState(posUp);

			if(stateUp.getBlock() != this || worldIn.getBlockState(getNeighborPos(state, pos)).getBlock() != this
					|| worldIn.getBlockState(getNeighborPos(stateUp, posUp)).getBlock() != this) {
				worldIn.setBlockToAir(pos);
				flag1 = true;
			}

			IBlockState stateDown = worldIn.getBlockState(pos.down());
			if(!stateDown.isSideSolid(worldIn, pos.down(), EnumFacing.UP) && !(stateDown.getBlock() instanceof BlockPanel)) {
				worldIn.setBlockToAir(pos);
				flag1 = true;

				if(stateUp.getBlock() == this) {
					worldIn.setBlockToAir(posUp);
				}
			}

			if(flag1) {
				BlockPos neighborPos = getNeighborPos(state, pos);
				IBlockState neighbor = worldIn.getBlockState(neighborPos);
				if(neighbor.getBlock() == this) {
					worldIn.setBlockToAir(neighborPos);
				}
				
				neighborPos = neighborPos.up();
				neighbor = worldIn.getBlockState(neighborPos);
				if(neighbor.getBlock() == this) {
					worldIn.setBlockToAir(neighborPos);
				}
				
				if(!worldIn.isRemote) {
					this.dropBlockAsItem(worldIn, pos, state, 0);
				}
			}
			else {
				EnumHingePosition hinge = state.getValue(HINGE);
				EnumFacing facing = state.getValue(FACING);
				boolean shouldBeOpen = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(posUp) ||
						hinge == EnumHingePosition.LEFT 
							&& (worldIn.isBlockPowered(pos.offset(facing.rotateY())) ||
								worldIn.isBlockPowered(pos.offset(facing.rotateY()).up())) ||
						hinge == EnumHingePosition.RIGHT 
							&& (worldIn.isBlockPowered(pos.offset(facing.rotateYCCW())) ||
								worldIn.isBlockPowered(pos.offset(facing.rotateYCCW()).up()));

				if(blockIn != this && (shouldBeOpen || blockIn.getDefaultState().canProvidePower()) && shouldBeOpen != stateUp.getValue(POWERED)) {
					worldIn.setBlockState(posUp, stateUp.withProperty(POWERED, shouldBeOpen), 2);

					if(shouldBeOpen != state.getValue(OPEN)) {
						worldIn.setBlockState(pos, state.withProperty(OPEN, shouldBeOpen), 2);
						worldIn.markBlockRangeForRenderUpdate(pos, pos);
						SoundEvent sound;
						if(shouldBeOpen) {
							// play open sound
							sound = PortalSounds.door_open;
						}
						else {
							// play close sound
							sound = PortalSounds.door_close;
						}
						worldIn.playSound((EntityPlayer)null, pos,
								sound, SoundCategory.BLOCKS,
								0.1F, 1F);
						
						BlockPos pos2;
						switch(hinge) {
						case LEFT:
							pos2 = pos.offset(facing.rotateY());
							break;
						case RIGHT:
							pos2 = pos.offset(facing.rotateYCCW());
							break;
						default:
							throw new AssertionError();
						}
						
						
						
						IBlockState state2 = worldIn.getBlockState(pos2);
						state2 = state2.getActualState(worldIn, pos2);
						
//						System.out.println("my state = " + state + "  side state = " + state2);
//						System.out.println("my pos = " + pos + "  side pos = " + pos2);
						
						if(state2.getBlock() == this && state2.getValue(HALF) == EnumDoorHalf.LOWER && state2.getValue(HINGE) != hinge && state2.getValue(OPEN) != shouldBeOpen) {
//							System.out.println("opening opening opening opening opening opening opening opening opening");
							worldIn.setBlockState(pos2, state2.withProperty(OPEN, shouldBeOpen), 2);
						} else {
							
						}
					}

				}
			}
		}
	}

}
