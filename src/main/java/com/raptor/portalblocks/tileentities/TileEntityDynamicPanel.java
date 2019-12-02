package com.raptor.portalblocks.tileentities;

import com.raptor.portalblocks.blocks.BlockDynamicPanel;
import com.raptor.portalblocks.blocks.BlockPanel.EnumPanelFace;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

@Deprecated
public class TileEntityDynamicPanel extends TileEntity {
	private EnumPanelFace northFace = EnumPanelFace.NONE, 
						  southFace = EnumPanelFace.NONE, 
						  eastFace  = EnumPanelFace.NONE, 
						  westFace  = EnumPanelFace.NONE, 
						  upFace    = EnumPanelFace.NONE, 
						  downFace  = EnumPanelFace.NONE;
	private boolean locked = false;
	
	private boolean emitsLight = false;
	private final BlockDynamicPanel block;
	
	public TileEntityDynamicPanel(BlockDynamicPanel panelIn) {
		this.block = panelIn;
	}
	
	public IBlockState getBlockState() {
		return this.block.getDefaultState().withProperty(BlockDynamicPanel.NORTH, northFace)
											.withProperty(BlockDynamicPanel.SOUTH, southFace)
											.withProperty(BlockDynamicPanel.EAST, eastFace)
											.withProperty(BlockDynamicPanel.WEST, westFace)
											.withProperty(BlockDynamicPanel.UP, upFace)
											.withProperty(BlockDynamicPanel.DOWN, downFace);
	}
	
	public boolean isLightPanel() {
		return emitsLight;
	}
	
	public EnumPanelFace getNorthFace() {
		return northFace;
	}
	
	public EnumPanelFace getSouthFace() {
		return southFace;
	}
	
	public EnumPanelFace getEastFace() {
		return eastFace;
	}
	
	public EnumPanelFace getWestFace() {
		return westFace;
	}
	
	public EnumPanelFace getUpFace() {
		return upFace;
	}
	
	public EnumPanelFace getDownFace() {
		return downFace;
	}
	
	public EnumPanelFace getFaceType(EnumFacing face) {
		switch(face) {
		case DOWN:
			return downFace;
		case EAST:
			return eastFace;
		case NORTH:
			return northFace;
		case SOUTH:
			return southFace;
		case UP:
			return upFace;
		case WEST:
			return westFace;
		default:
			throw new AssertionError();
		}
	}
	
	public boolean isPortalable(EnumFacing face) {
		switch(face) {
		case DOWN:
			return downFace.isPortalable;
		case EAST:
			return eastFace.isPortalable;
		case NORTH:
			return northFace.isPortalable;
		case SOUTH:
			return southFace.isPortalable;
		case UP:
			return upFace.isPortalable;
		case WEST:
			return westFace.isPortalable;
		default:
			throw new AssertionError();
		}
	}
	
	private void recalcEmitsLight() {
		//boolean oldEmitsLight = emitsLight;
		emitsLight = northFace.isLight || southFace.isLight || eastFace.isLight
				|| westFace.isLight || upFace.isLight || downFace.isLight;
		/*if(oldEmitsLight != emitsLight) {
			
		}*/
	}
	
	public TileEntityDynamicPanel setNorthFace(EnumPanelFace type) {
		if(northFace != type) {
			boolean recalcLight = northFace.isLight;
			northFace = type;
			if(recalcLight)
				recalcEmitsLight();
			else
				emitsLight |= type.isLight;
			updateBlock();
		}
		return this;
	}

	public TileEntityDynamicPanel setSouthFace(EnumPanelFace type) {
		if(southFace != type) {
			boolean recalcLight = southFace.isLight;
			southFace = type;
			if(recalcLight)
				recalcEmitsLight();
			else
				emitsLight |= type.isLight;
			updateBlock();
		}
		return this;
	}

	public TileEntityDynamicPanel setEastFace(EnumPanelFace type) {
		if(eastFace != type) {
			boolean recalcLight = eastFace.isLight;
			eastFace = type;
			if(recalcLight)
				recalcEmitsLight();
			else
				emitsLight |= type.isLight;
			updateBlock();
		}
		return this;
	}

	public TileEntityDynamicPanel setWestFace(EnumPanelFace type) {
		if(westFace != type) {
			boolean recalcLight = westFace.isLight;
			westFace = type;
			if(recalcLight)
				recalcEmitsLight();
			else
				emitsLight |= type.isLight;
			updateBlock();
		}
		return this;
	}

	public TileEntityDynamicPanel setUpFace(EnumPanelFace type) {
		if(upFace != type) {
			boolean recalcLight = upFace.isLight;
			upFace = type;
			if(recalcLight)
				recalcEmitsLight();
			else
				emitsLight |= type.isLight;
			updateBlock();
		}
		return this;
	}

	public TileEntityDynamicPanel setDownFace(EnumPanelFace type) {
		if(downFace != type) {
			boolean recalcLight = downFace.isLight;
			downFace = type;
			if(recalcLight)
				recalcEmitsLight();
			else
				emitsLight |= type.isLight;
			updateBlock();
		}
		return this;
	}

	public TileEntityDynamicPanel setFaceType(EnumFacing face, EnumPanelFace type) {
		boolean recalcLight;
		switch(face) {
		case DOWN:
			if(downFace == type)
				return this;
			recalcLight = downFace.isLight;
			downFace = type;
			break;
		case EAST:
			if(eastFace == type)
				return this;
			recalcLight = eastFace.isLight;
			eastFace = type;
			break;
		case NORTH:
			if(northFace == type)
				return this;
			recalcLight = northFace.isLight;
			northFace = type;
			break;
		case SOUTH:
			if(southFace == type)
				return this;
			recalcLight = southFace.isLight;
			southFace = type;
			break;
		case UP:
			if(upFace == type)
				return this;
			recalcLight = upFace.isLight;
			upFace = type;
			break;
		case WEST:
			if(westFace == type)
				return this;
			recalcLight = westFace.isLight;
			westFace = type;
			break;
		default:
			throw new AssertionError();
		}
		if(recalcLight)
			recalcEmitsLight();
		else
			emitsLight |= type.isLight;
		updateBlock();
		return this;
	}
	
	protected void updateBlock() {
		this.world.scheduleUpdate(this.pos, this.block, 0);
		this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		NBTTagCompound faces = compound.getCompoundTag("Faces");
		northFace = EnumPanelFace.fromIndex(faces.getByte("north"));
		southFace = EnumPanelFace.fromIndex(faces.getByte("south"));
		eastFace = EnumPanelFace.fromIndex(faces.getByte("east"));
		westFace = EnumPanelFace.fromIndex(faces.getByte("west"));
		upFace = EnumPanelFace.fromIndex(faces.getByte("up"));
		downFace = EnumPanelFace.fromIndex(faces.getByte("down"));
		
		locked = compound.getBoolean("Locked");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		NBTTagCompound faces = new NBTTagCompound();
		faces.setByte("north", (byte)northFace.ordinal());
		faces.setByte("south", (byte)southFace.ordinal());
		faces.setByte("east", (byte)eastFace.ordinal());
		faces.setByte("west", (byte)westFace.ordinal());
		faces.setByte("up", (byte)upFace.ordinal());
		faces.setByte("down", (byte)downFace.ordinal());
		
		compound.setTag("Faces", faces);
		
		compound.setBoolean("Locked", locked);
		
		return compound;
	}
	
}
