package com.raptor.portalblocks.blocks;

import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLargePanelCorner extends BlockPanel {
	
	public static final PropertyEnum<EnumLargePanelCorner> TYPE = PropertyEnum.create("type", EnumLargePanelCorner.class);
	
	public BlockLargePanelCorner(boolean canPortalOn) {
		super(canPortalOn);
		setDefaultState(getDefaultState().withProperty(TYPE, EnumLargePanelCorner.BOTTOM_NORTH_WEST));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE);
	}
	
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(this);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(TYPE) == EnumLargePanelCorner.BOTTOM_NORTH_WEST? Item.getItemFromBlock(this) : Items.AIR;
	}
	
	/*@Override
	public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing face, final float hitX, final float hitY,
			final float hitZ, final int meta, final EntityLivingBase placer, final EnumHand hand) {
		EnumLargePanelCorner type;
		
		BlockPos pos2 = pos.offset(face.getOpposite());
		IBlockState state = world.getBlockState(pos2);
		if(state.getBlock() instanceof BlockLargePanelCorner) {
			type = state.getValue(TYPE).flipAlongAxis(face.getAxis());
		} else {
			switch(face) {
			case DOWN:
				
				break;
			case EAST:
				break;
			case NORTH:
				break;
			case SOUTH:
				break;
			case UP:
				break;
			case WEST:
				break;
			default:
				throw new AssertionError();
			}
		}
		
		return getDefaultState().withProperty(TYPE, type);
	}*/
		
	/*@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		
	}*/
	
	
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		for(Neighbor neighbor : state.getValue(TYPE).neighbors()) {
			if(!neighbor.test(world, pos, this)) {
				world.setBlockToAir(pos);
				blockIn.dropBlockAsItem(world, pos, state, 0);
				return;
			}
		}
		/*IBlockState state2;
		
		boolean remove = false;
		
		switch(state.getValue(TYPE)) {
		case BOTTOM_NORTH:
			state2 = world.getBlockState(pos.east());
			if(state2.getBlock() == this) {
				if(state2.getValue(TYPE) != EnumLargePanelCorner.BOTTOM_EAST) {
					remove = true;
					break;
				}
			}
			else {
				// TODO
				remove = true;
				break;
			}
			
			state2 = world.getBlockState(pos.up());
			if(state2.getBlock() == this) {
				if(state2.getValue(TYPE) != EnumLargePanelCorner.TOP_NORTH) {
					remove = true;
					break;
				}
			}
			else {
				// TODO
				remove = true;
				break;
			}
			
			state2 = world.getBlockState(pos.south());
			if(state2.getBlock() == this) {
				if(state2.getValue(TYPE) != EnumLargePanelCorner.BOTTOM_WEST) {
					remove = true;
					break;
				}
			}
			else {
				// TODO
				remove = true;
				break;
			}
			
			break;
		case BOTTOM_EAST:
			
			break;
		case BOTTOM_SOUTH:
			break;
		case BOTTOM_WEST:
			break;
		case TOP_EAST:
			break;
		case TOP_NORTH:
			break;
		case TOP_SOUTH:
			break;
		case TOP_WEST:
			break;
		}
		
		if(remove) {
			world.setBlockToAir(pos);
			blockIn.dropBlockAsItem(world, pos, state, 0);
		}*/
	}
	
	/*protected void onBlockDestroyed(World worldIn, BlockPos pos, IBlockState state) {
		
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		onBlockDestroyed(worldIn, pos, state);
	}	

	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
		onBlockDestroyed(world, pos, world.getBlockState(pos));
		super.onBlockExploded(world, pos, explosion);
	}*/

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).ordinal();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, EnumLargePanelCorner.fromIndex(meta));
	}	
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() == this) {
			world.setBlockState(pos, state.withProperty(TYPE, state.getValue(TYPE).rotateAround(axis)));
			return true;
		}
		else
			return super.rotateBlock(world, pos, axis);
	}

	static class Neighbor {
		final EnumFacing offsetDirection;
		final EnumLargePanelCorner panelCornerType;
		
		Neighbor(EnumFacing offsetDirection, EnumLargePanelCorner panelCornerType) {
			this.offsetDirection = offsetDirection;
			this.panelCornerType = panelCornerType;
		}
		
		public boolean test(IBlockAccess worldIn, BlockPos pos, BlockLargePanelCorner self) {
			IBlockState state = worldIn.getBlockState(pos.offset(offsetDirection));
			if(state.getBlock() == self) {
				return state.getValue(TYPE) == panelCornerType;
			}
			else return false;
		}
		
	}

	public static enum EnumLargePanelCorner implements IStringSerializable {
		BOTTOM_NORTH_WEST	(/*0,*/ true, EnumFacing.NORTH,  1, 4, 3,	1, 3, 4, 4, 1, 3,	Vec3i.NULL_VECTOR),
		BOTTOM_NORTH_EAST	(/*1,*/ true, EnumFacing.EAST,   0, 5, 2,	5, 2, 0, 5, 2, 0,	new Vec3i(1, 0, 0)),
		BOTTOM_SOUTH_EAST	(/*2,*/ true, EnumFacing.SOUTH,  3, 6, 1,	6, 6, 3, 1, 3, 1,	new Vec3i(1, 0, 1)),
		BOTTOM_SOUTH_WEST	(/*3,*/ true, EnumFacing.WEST,   2, 7, 0,	2, 7, 7, 0, 0, 2,	new Vec3i(0, 0, 1)),
		TOP_NORTH_WEST		(/*4,*/ false, EnumFacing.NORTH, 5, 0, 7,	0, 0, 5, 7, 5, 7,	new Vec3i(0, 1, 0)),
		TOP_NORTH_EAST		(/*5,*/ false, EnumFacing.EAST,  4, 1, 6,	4, 1, 1, 6, 6, 4,	new Vec3i(1, 1, 0)),
		TOP_SOUTH_EAST		(/*6,*/ false, EnumFacing.SOUTH, 7, 2, 5,	7, 5, 2, 2, 7, 5,	new Vec3i(1, 1, 1)),
		TOP_SOUTH_WEST		(/*7,*/ false, EnumFacing.WEST,  6, 3, 4,	3, 4, 6, 3, 4, 6,	new Vec3i(0, 1, 1));
		
		private static final Map<String, EnumLargePanelCorner> NAME_LOOKUP = Maps.newHashMap();
		private static final EnumLargePanelCorner[] VALUES = values();
		
		private final String name;
		private final EnumFacing horizontalFacing;
		private final boolean bottom;
		private final int[] oppositeIndices, rotateNextIndices;
		private final Vec3i offsetToBottomNorth, offsetFromBottomNorth;
		private final Neighbor[] neighbors = new Neighbor[3];
		
		@SafeVarargs
		private EnumLargePanelCorner(boolean isBottom, EnumFacing horizontalFacing, int xOppositeIndex, int yOppositeIndex, int zOppositeIndex, int rotateN, int rotateE, int rotateS, int rotateW, int rotateU, int rotateD, Vec3i offsetFromBottomNorth, Pair<Integer, Vec3i>... neighbors) {
			this.name = name().toLowerCase(Locale.ROOT);
			this.bottom = isBottom;
			this.horizontalFacing = horizontalFacing;
			this.offsetFromBottomNorth = offsetFromBottomNorth;
			this.offsetToBottomNorth = new Vec3i(-offsetFromBottomNorth.getX(), -offsetFromBottomNorth.getY(), -offsetFromBottomNorth.getZ());
			this.oppositeIndices = new int[3];
			oppositeIndices[EnumAxis.X.ordinal()] = xOppositeIndex;
			oppositeIndices[EnumAxis.Y.ordinal()] = yOppositeIndex;
			oppositeIndices[EnumAxis.Z.ordinal()] = zOppositeIndex;
			this.rotateNextIndices = new int[6];
			rotateNextIndices[EnumFacing.NORTH.ordinal()] = rotateN;
			rotateNextIndices[EnumFacing.EAST.ordinal()]  = rotateE;
			rotateNextIndices[EnumFacing.SOUTH.ordinal()] = rotateS;
			rotateNextIndices[EnumFacing.WEST.ordinal()]  = rotateW;
			rotateNextIndices[EnumFacing.UP.ordinal()]    = rotateU;
			rotateNextIndices[EnumFacing.DOWN.ordinal()]  = rotateD;
		}
		
		Neighbor[] neighbors() {
			return neighbors;
		}
		
		public Vec3i getOffsetToBottomNorthWest() {
			return offsetToBottomNorth;
		}
		
		public Vec3i getOffsetFromBottomNorthWest() {
			return offsetFromBottomNorth;
		}

		@Override
		public String getName() {
			return name;
		}
		
		public boolean isBottom() {
			return bottom;
		}
		
		public boolean isTop() {
			return !bottom;
		}
		
		public EnumFacing getHorizontalFacing() {
			return horizontalFacing;
		}
		
		public EnumLargePanelCorner flipAlongAxis(EnumFacing.Axis axis) {
			return VALUES[oppositeIndices[axis.ordinal()]];
		}
		
		public EnumLargePanelCorner rotateAround(EnumFacing face) {
			return VALUES[rotateNextIndices[face.ordinal()]];
		}
		
		/**
		 * Rotate clockwise around Y axis
		 */
		public EnumLargePanelCorner rotateCW() {
			switch(this) {
			case BOTTOM_NORTH_WEST:
				return BOTTOM_NORTH_EAST;
			case BOTTOM_NORTH_EAST:
				return BOTTOM_SOUTH_EAST;
			case BOTTOM_SOUTH_EAST:
				return BOTTOM_SOUTH_WEST;
			case BOTTOM_SOUTH_WEST:
				return BOTTOM_NORTH_WEST;
			case TOP_NORTH_WEST:
				return TOP_NORTH_EAST;
			case TOP_NORTH_EAST:
				return TOP_SOUTH_EAST;
			case TOP_SOUTH_EAST:
				return TOP_SOUTH_WEST;
			case TOP_SOUTH_WEST:
				return TOP_NORTH_WEST;	
			default:
				throw new IllegalStateException("Unable to get Y-rotated corner of " + this);
			}
		}
		
		/**
		 * Rotate clockwise around Y axis
		 */
		public EnumLargePanelCorner rotateCW(int numTimes) {
			if(numTimes < 0)
				return rotateCCW(-numTimes);
			if(numTimes == 0)
				return this;
			EnumLargePanelCorner result = this;
			for(int i = 0; i < numTimes; i++)
				result = result.rotateCW();
			return result;
		}
		
		/**
		 * Rotate counter-clockwise around Y axis
		 */
		public EnumLargePanelCorner rotateCCW() {
			switch(this) {
			case BOTTOM_SOUTH_WEST:
				return BOTTOM_SOUTH_EAST;
			case BOTTOM_SOUTH_EAST:
				return BOTTOM_NORTH_EAST;
			case BOTTOM_NORTH_EAST:
				return BOTTOM_NORTH_WEST;
			case BOTTOM_NORTH_WEST:
				return BOTTOM_SOUTH_WEST;
			case TOP_SOUTH_WEST:
				return TOP_SOUTH_EAST;
			case TOP_SOUTH_EAST:
				return TOP_NORTH_EAST;
			case TOP_NORTH_EAST:
				return TOP_NORTH_WEST;
			case TOP_NORTH_WEST:
				return TOP_SOUTH_WEST;
			default:
				throw new IllegalStateException("Unable to get Y-rotated corner of " + this);
			}
		}
		
		/**
		 * Rotate counter-clockwise around Y axis
		 */
		public EnumLargePanelCorner rotateCCW(int numTimes) {
			if(numTimes < 0)
				return rotateCW(-numTimes);
			if(numTimes == 0)
				return this;
			EnumLargePanelCorner result = this;
			for(int i = 0; i < numTimes; i++)
				result = result.rotateCCW();
			return result;
		}
		
		@Nullable
		public static EnumLargePanelCorner byName(String name) {
			return name == null? null : NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
		}
		
		public static EnumLargePanelCorner fromIndex(int index) {
			return VALUES[MathHelper.abs(index) % VALUES.length];
		} 
		
		static {
			for(EnumLargePanelCorner value : VALUES) {
				NAME_LOOKUP.put(value.getName(), value);
			}
			
			Neighbor[] neighbors;
			
			neighbors = BOTTOM_NORTH_WEST.neighbors;
			neighbors[0] = new Neighbor(EnumFacing.SOUTH, BOTTOM_SOUTH_WEST);
			neighbors[1] = new Neighbor(EnumFacing.UP, TOP_NORTH_WEST);
			neighbors[2] = new Neighbor(EnumFacing.EAST, BOTTOM_NORTH_EAST);
			
			neighbors = BOTTOM_NORTH_EAST.neighbors;
			neighbors[0] = new Neighbor(EnumFacing.WEST, BOTTOM_NORTH_WEST);
			neighbors[1] = new Neighbor(EnumFacing.UP, TOP_NORTH_EAST);
			neighbors[2] = new Neighbor(EnumFacing.SOUTH, BOTTOM_SOUTH_EAST);
			
			neighbors = BOTTOM_SOUTH_EAST.neighbors;
			neighbors[0] = new Neighbor(EnumFacing.NORTH, BOTTOM_NORTH_EAST);
			neighbors[1] = new Neighbor(EnumFacing.UP, TOP_SOUTH_EAST);
			neighbors[2] = new Neighbor(EnumFacing.WEST, BOTTOM_SOUTH_WEST);
			
			neighbors = BOTTOM_SOUTH_WEST.neighbors;
			neighbors[0] = new Neighbor(EnumFacing.EAST, BOTTOM_SOUTH_EAST);
			neighbors[1] = new Neighbor(EnumFacing.UP, TOP_SOUTH_WEST);
			neighbors[2] = new Neighbor(EnumFacing.NORTH, BOTTOM_NORTH_WEST);
			
			neighbors = TOP_NORTH_WEST.neighbors;
			neighbors[0] = new Neighbor(EnumFacing.SOUTH, TOP_SOUTH_WEST);
			neighbors[1] = new Neighbor(EnumFacing.DOWN, BOTTOM_NORTH_WEST);
			neighbors[2] = new Neighbor(EnumFacing.EAST, TOP_NORTH_EAST);
			
			neighbors = TOP_NORTH_EAST.neighbors;
			neighbors[0] = new Neighbor(EnumFacing.WEST, TOP_NORTH_WEST);
			neighbors[1] = new Neighbor(EnumFacing.DOWN, BOTTOM_NORTH_EAST);
			neighbors[2] = new Neighbor(EnumFacing.SOUTH, TOP_SOUTH_EAST);
			
			neighbors = TOP_SOUTH_EAST.neighbors;
			neighbors[0] = new Neighbor(EnumFacing.NORTH, TOP_NORTH_EAST);
			neighbors[1] = new Neighbor(EnumFacing.DOWN, BOTTOM_SOUTH_EAST);
			neighbors[2] = new Neighbor(EnumFacing.WEST, TOP_SOUTH_WEST);
			
			neighbors = TOP_SOUTH_WEST.neighbors;
			neighbors[0] = new Neighbor(EnumFacing.EAST, TOP_SOUTH_EAST);
			neighbors[1] = new Neighbor(EnumFacing.DOWN, BOTTOM_SOUTH_WEST);
			neighbors[2] = new Neighbor(EnumFacing.NORTH, TOP_NORTH_WEST);
		}

	}

}
