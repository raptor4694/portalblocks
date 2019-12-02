package com.raptor.portalblocks.blocks;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDoublePlant.EnumBlockHalf;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLargeSquarePanel extends BlockHorizontalPanel {

	public static final PropertyEnum<EnumBlockHalf> HALF = BlockDoublePlant.HALF; 
	
	protected final boolean sidePortalable, topPortalable, bottomPortalable;
	private final EnumPanelFace topSideType, bottomSideType;
	private final EnumPanelFace topLeftFaceType, topRightFaceType, bottomLeftFaceType, bottomRightFaceType;
	private final EnumPanelFace[] topTypes = new EnumPanelFace[4], bottomTypes = new EnumPanelFace[4];
	
	public BlockLargeSquarePanel(boolean canPortalOnFace, EnumPanelFace topSideType, EnumPanelFace northTopType) {
		this(canPortalOnFace, topSideType, northTopType, northTopType.mirrorVertical());
	}
	
	public BlockLargeSquarePanel(boolean canPortalOnFace, EnumPanelFace topSideType, EnumPanelFace northTopType, EnumPanelFace northBottomType) {
		super(canPortalOnFace);
		this.sidePortalable = topSideType.isPortalable;
		this.topPortalable = northTopType.isPortalable;
		this.bottomPortalable = northBottomType.isPortalable;
		
		this.topLeftFaceType     = EnumPanelFace.Shape.TOP_LEFT.getPanelFace(canPortalOnFace);
		this.topRightFaceType    = topLeftFaceType.rotateCW();
		this.bottomRightFaceType = topRightFaceType.rotateCW();
		this.bottomLeftFaceType  = bottomRightFaceType.rotateCW();
		
		this.topSideType    = topSideType;
		this.bottomSideType = topSideType.mirrorVertical();
		
		EnumPanelFace north, east, south, west;
		
		north = northTopType;
		east  = north.rotateCW();
		south = east.rotateCW();
		west  = south.rotateCW();
		
		this.topTypes[EnumFacing.NORTH.getHorizontalIndex()] = north;
		this.topTypes[EnumFacing.EAST.getHorizontalIndex()]  = east;
		this.topTypes[EnumFacing.SOUTH.getHorizontalIndex()] = south;
		this.topTypes[EnumFacing.WEST.getHorizontalIndex()]  = west;
		
		north = northBottomType;
		east  = north.rotateCW();
		south = east.rotateCW();
		west  = south.rotateCW();
		
		this.bottomTypes[EnumFacing.NORTH.getHorizontalIndex()] = north;
		this.bottomTypes[EnumFacing.EAST.getHorizontalIndex()]  = east;
		this.bottomTypes[EnumFacing.SOUTH.getHorizontalIndex()] = south;
		this.bottomTypes[EnumFacing.WEST.getHorizontalIndex()]  = west;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, HALF);
	}
	
	public BlockPos[] getAllPositions(IBlockState state, BlockPos pos) {
		EnumFacing facing = state.getValue(FACING).rotateY();
		BlockPos posVert = state.getValue(HALF) == EnumBlockHalf.LOWER? pos.up() : pos.down();
		return new BlockPos[] {pos, pos.offset(facing), posVert, posVert.offset(facing)};
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		
	}
	
	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		EnumFacing facing = state.getValue(FACING);
		EnumBlockHalf half = state.getValue(HALF);
		
		if(facing.getAxis() == side.getAxis())
			return portalable;
		
		if(side == EnumFacing.UP)
			return half == EnumBlockHalf.UPPER && topPortalable;
		
		if(side == EnumFacing.DOWN)
			return half == EnumBlockHalf.LOWER && bottomPortalable;
		
		if(side == facing.rotateYCCW()) // side 
			return sidePortalable;
		
		return false; // interior
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = state.getValue(FACING).getHorizontalIndex();
		if(state.getValue(HALF) == EnumBlockHalf.UPPER)
			meta |= 1 << 3;
		return meta;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 0b11))
				.withProperty(HALF, (meta >> 3 & 1) == 0? EnumBlockHalf.LOWER : EnumBlockHalf.UPPER);
	}
	
	@Override
	public EnumPanelFace getFaceType(EnumFacing face, IBlockState state, IBlockAccess world, BlockPos pos) {
		
		EnumBlockHalf half = state.getValue(HALF);
		
	}
	
	public static enum EnumBlockSide implements IStringSerializable {
		
	}

}
