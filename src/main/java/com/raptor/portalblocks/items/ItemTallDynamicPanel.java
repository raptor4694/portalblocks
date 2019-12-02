package com.raptor.portalblocks.items;

import com.raptor.portalblocks.blocks.BlockDynamicPanel.EnumPanelFace;
import com.raptor.portalblocks.blocks.BlockPanel;
import com.raptor.portalblocks.tileentities.TileEntityDynamicPanel;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Deprecated
public class ItemTallDynamicPanel extends ItemDynamicPanel {

	public ItemTallDynamicPanel(Block block, boolean isPortalable) {
		super(block, isPortalable);
	}
	
	protected EnumPanelFace getTopPanelType() {
		return portalable? EnumPanelFace.WHITE_TALL_TOP : EnumPanelFace.BLACK_TALL_TOP;
	}
	
	protected EnumPanelFace getBottomPanelType() {
		return portalable? EnumPanelFace.WHITE_TALL_BOTTOM : EnumPanelFace.BLACK_TALL_BOTTOM;
	}
	
	protected EnumPanelFace getLeftPanelType() {
		return portalable? EnumPanelFace.WHITE_TALL_LEFT : EnumPanelFace.BLACK_TALL_LEFT;
	}
	
	protected EnumPanelFace getRightPanelType() {
		return portalable? EnumPanelFace.WHITE_TALL_RIGHT : EnumPanelFace.BLACK_TALL_RIGHT;
	}
	
	protected EnumPanelFace getTopPanelTypeBasedOnHorizontalFacing(EnumFacing horizFacing) {
		switch(horizFacing) {
		case NORTH:
			return getTopPanelType();
		case SOUTH:
			return getBottomPanelType();
		case EAST:
			return getRightPanelType();
		case WEST:
			return getLeftPanelType();
		default:
			throw new IllegalArgumentException("not a horizontal face: " + horizFacing);
		}
	}
	
	protected EnumPanelFace getBottomPanelTypeBasedOnHorizontalFacing(EnumFacing horizFacing) {
		switch(horizFacing) {
		case NORTH:
			return getBottomPanelType();
		case SOUTH:
			return getTopPanelType();
		case EAST:
			return getLeftPanelType();
		case WEST:
			return getRightPanelType();
		default:
			throw new IllegalArgumentException("not a horizontal face: " + horizFacing);
		}
	}

	@Override
	protected EnumActionResult modifyPanels(final EntityPlayer player, final World worldIn,
			final TileEntityDynamicPanel te, IBlockState state, final BlockPos pos, final EnumHand hand,
			final EnumFacing face, final float hitX, final float hitY, final float hitZ) {
		if(face.getAxis() == EnumFacing.Axis.Y) {
			EnumFacing horizFacing = player.getHorizontalFacing();
			if(player.isSneaking())
				horizFacing = horizFacing.rotateY();
			
			boolean flag;
			
			switch(horizFacing) {
			case NORTH:
				flag = (hitZ >= 0.5f);
				break;
			case SOUTH:
				flag = (hitZ < 0.5f);
				break;
			case EAST:
				flag = (hitX < 0.5f);
				break;
			case WEST:
				flag = (hitX >= 0.5f);
				break;
			default:
				throw new AssertionError();
			}
			
			setFrontward:
			if(flag) {
				BlockPos posFront = pos.offset(horizFacing);
				IBlockState stateFront = worldIn.getBlockState(posFront);
				if(stateFront.getBlock() != this.panel) {
					Block blockFront = stateFront.getBlock();
					if(blockFront instanceof BlockPanel)
						((BlockPanel)blockFront).replaceWithDynamicPanel(this.panel, stateFront, worldIn, posFront);
					else break setFrontward;
				}
				
				te.setFaceType(face, getBottomPanelTypeBasedOnHorizontalFacing(horizFacing));
				((TileEntityDynamicPanel)worldIn.getTileEntity(posFront)).setFaceType(face, getTopPanelTypeBasedOnHorizontalFacing(horizFacing));
				
				return EnumActionResult.SUCCESS;
			}
		
			BlockPos posBack = pos.offset(horizFacing.getOpposite());
			IBlockState stateBack = worldIn.getBlockState(posBack);
			if(stateBack.getBlock() != this.panel) {
				Block blockBack = stateBack.getBlock();
				if(blockBack instanceof BlockPanel)
					((BlockPanel)blockBack).replaceWithDynamicPanel(this.panel, stateBack, worldIn, posBack);
				else return EnumActionResult.FAIL;
			}
			
			te.setFaceType(face, getTopPanelTypeBasedOnHorizontalFacing(horizFacing));
			((TileEntityDynamicPanel)worldIn.getTileEntity(posBack)).setFaceType(face, getBottomPanelTypeBasedOnHorizontalFacing(horizFacing));
			
			return EnumActionResult.SUCCESS;
		}
		else {
			if(player.isSneaking()) {
				float hit;
				switch(face.getAxis()) {
				case X:
					hit = hitZ;
					break;
				case Z:
					hit = hitX;
					break;
				default:
					throw new AssertionError();
				}
				
				setRightward:
				if(hit < 0.5f) {
					BlockPos posRight = pos.offset(face.rotateYCCW());
					IBlockState stateRight = worldIn.getBlockState(posRight);
					if(stateRight.getBlock() != this.panel) {
						Block blockRight = stateRight.getBlock();
						if(blockRight instanceof BlockPanel)
							((BlockPanel)blockRight).replaceWithDynamicPanel(this.panel, stateRight, worldIn, posRight);
						else break setRightward;
					}
					
					te.setFaceType(face, getLeftPanelType());
					((TileEntityDynamicPanel)worldIn.getTileEntity(posRight)).setFaceType(face, getRightPanelType());
					
					return EnumActionResult.SUCCESS;
				}
				
				BlockPos posLeft = pos.offset(face.rotateYCCW());
				IBlockState stateLeft = worldIn.getBlockState(posLeft);
				if(stateLeft.getBlock() != this.panel) {
					Block blockLeft = stateLeft.getBlock();
					if(blockLeft instanceof BlockPanel)
						((BlockPanel)blockLeft).replaceWithDynamicPanel(this.panel, stateLeft, worldIn, posLeft);
					else return EnumActionResult.FAIL;
				}
				
				te.setFaceType(face, getRightPanelType());
				((TileEntityDynamicPanel)worldIn.getTileEntity(posLeft)).setFaceType(face, getLeftPanelType());
				
				return EnumActionResult.SUCCESS;
			}
			else {
				setUpward:
				if(hitY < 0.5f) {
					BlockPos posUp = pos.up();
					IBlockState stateUp = worldIn.getBlockState(posUp);
					if(stateUp.getBlock() != this.panel) {
						Block blockUp = stateUp.getBlock();
						if(blockUp instanceof BlockPanel)
							((BlockPanel)blockUp).replaceWithDynamicPanel(this.panel, stateUp, worldIn, posUp);
						else break setUpward;
					}
					
					te.setFaceType(face, getBottomPanelType());
					((TileEntityDynamicPanel)worldIn.getTileEntity(posUp)).setFaceType(face, getTopPanelType());
					
					return EnumActionResult.SUCCESS;
				}
			
				BlockPos posDown = pos.down();
				IBlockState stateDown = worldIn.getBlockState(posDown);
				if(stateDown.getBlock() != this.panel) {
					Block blockDown = stateDown.getBlock();
					if(blockDown instanceof BlockPanel)
						((BlockPanel)blockDown).replaceWithDynamicPanel(this.panel, stateDown, worldIn, posDown);
					else return EnumActionResult.FAIL;
				}
				
				te.setFaceType(face, getTopPanelType());
				((TileEntityDynamicPanel)worldIn.getTileEntity(posDown)).setFaceType(face, getBottomPanelType());
				
				return EnumActionResult.SUCCESS;
			}
		}
		
	}

}
