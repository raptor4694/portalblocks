package com.raptor.portalblocks.blocks;

import static com.raptor.portalblocks.PortalBlocks.createBoundingBox;
import static com.raptor.portalblocks.blocks.BlockFrame.*;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEmptyFrame extends Block {
	
	public static final PropertyEnum<EnumFacing.Axis> AXIS = BlockLog.AXIS;
	public static final PropertyBool FRONT = PropertyBool.create("front");
	public static final PropertyBool BACK = PropertyBool.create("back");

	public BlockEmptyFrame(Material materialIn) {
		super(materialIn);
		setDefaultState(getDefaultState().withProperty(AXIS, EnumFacing.Axis.X));
	}

	public BlockEmptyFrame() {
		this(Material.IRON);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing face, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		EnumFacing facing = EnumFacing.getDirectionFromEntityLiving(pos, placer);
		boolean front, back;
		if(placer.isSneaking()) {
			front = back = true;
		}
		else {
			switch(facing) {
			case SOUTH:
			case EAST:
			case UP:
				front = true;
				back  = false;
				break;
			case NORTH:
			case WEST:
			case DOWN:
				front = false;
				back  = true;
				break;
			default:
				throw new AssertionError();
			}
		}
		
		return getDefaultState().withProperty(AXIS, facing.getAxis()).withProperty(FRONT, front).withProperty(BACK, back);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, AXIS, FRONT, BACK);
	}
	
	private static final AxisAlignedBB AABB_AXIS_X_WEST = createBoundingBox(2, 0, 0, 4, 16, 16),
										AABB_AXIS_X_WEST_BAR_NORTH = createBoundingBox(2, 1, 0, 4, 15, 1),
										AABB_AXIS_X_WEST_BAR_SOUTH = createBoundingBox(2, 1, 15, 4, 15, 16),
										AABB_AXIS_X_WEST_BAR_DOWN  = createBoundingBox(2, 0, 0, 4, 1, 16),
										AABB_AXIS_X_WEST_BAR_UP    = createBoundingBox(2, 15, 0, 4, 16, 16);
	
	private static final AxisAlignedBB AABB_AXIS_X_EAST = createBoundingBox(12, 0, 0, 14, 16, 16),
										AABB_AXIS_X_EAST_BAR_NORTH = createBoundingBox(12, 1, 0, 14, 15, 1),
										AABB_AXIS_X_EAST_BAR_SOUTH = createBoundingBox(12, 1, 15, 14, 15, 16),
										AABB_AXIS_X_EAST_BAR_DOWN  = createBoundingBox(12, 0, 0, 14, 1, 16),
										AABB_AXIS_X_EAST_BAR_UP    = createBoundingBox(12, 15, 0, 14, 16, 16);
	
	private static final AxisAlignedBB AABB_AXIS_Y_UP = createBoundingBox(0, 12, 0, 16, 14, 16),
										AABB_AXIS_Y_UP_BAR_NORTH = createBoundingBox(0, 12, 0, 16, 14, 1),
										AABB_AXIS_Y_UP_BAR_SOUTH = createBoundingBox(0, 12, 15, 16, 14, 16),
										AABB_AXIS_Y_UP_BAR_WEST  = createBoundingBox(0, 12, 1, 1, 14, 15),
										AABB_AXIS_Y_UP_BAR_EAST  = createBoundingBox(15, 12, 1, 16, 14, 15);
	
	private static final AxisAlignedBB AABB_AXIS_Y_DOWN = createBoundingBox(0, 2, 0, 16, 4, 16),
										AABB_AXIS_Y_DOWN_BAR_NORTH = createBoundingBox(0, 2, 0, 16, 4, 1),
										AABB_AXIS_Y_DOWN_BAR_SOUTH = createBoundingBox(0, 2, 15, 16, 4, 16),
										AABB_AXIS_Y_DOWN_BAR_WEST  = createBoundingBox(0, 2, 1, 1, 4, 15),
										AABB_AXIS_Y_DOWN_BAR_EAST  = createBoundingBox(15, 2, 1, 16, 4, 15);
	
	private static final AxisAlignedBB AABB_AXIS_Z_NORTH = createBoundingBox(0, 0, 2, 16, 16, 4),
										AABB_AXIS_Z_NORTH_BAR_WEST = createBoundingBox(0, 1, 2, 1, 15, 4),
										AABB_AXIS_Z_NORTH_BAR_EAST = createBoundingBox(15, 1, 2, 16, 15, 4),
										AABB_AXIS_Z_NORTH_BAR_DOWN = createBoundingBox(0, 0, 2, 16, 1, 4),
										AABB_AXIS_Z_NORTH_BAR_UP   = createBoundingBox(0, 15, 2, 16, 16, 4);
	
	private static final AxisAlignedBB AABB_AXIS_Z_SOUTH = createBoundingBox(0, 0, 12, 16, 16, 14),
										AABB_AXIS_Z_SOUTH_BAR_WEST = createBoundingBox(0, 1, 12, 1, 15, 14),
										AABB_AXIS_Z_SOUTH_BAR_EAST = createBoundingBox(15, 1, 12, 16, 15, 14),
										AABB_AXIS_Z_SOUTH_BAR_DOWN = createBoundingBox(0, 0, 12, 16, 1, 14),
										AABB_AXIS_Z_SOUTH_BAR_UP   = createBoundingBox(0, 15, 12, 16, 16, 14);

	private static final List<AxisAlignedBB> AABBs_AXIS_X_WEST  = Lists.newArrayList(AABB_AXIS_X_WEST_BAR_NORTH, AABB_AXIS_X_WEST_BAR_SOUTH, AABB_AXIS_X_WEST_BAR_DOWN, AABB_AXIS_X_WEST_BAR_UP),
										 	 AABBs_AXIS_X_EAST  = Lists.newArrayList(AABB_AXIS_X_EAST_BAR_NORTH, AABB_AXIS_X_EAST_BAR_SOUTH, AABB_AXIS_X_EAST_BAR_DOWN, AABB_AXIS_X_EAST_BAR_UP),
										 	 AABBs_AXIS_Y_UP    = Lists.newArrayList(AABB_AXIS_Y_UP_BAR_NORTH, AABB_AXIS_Y_UP_BAR_SOUTH, AABB_AXIS_Y_UP_BAR_WEST, AABB_AXIS_Y_UP_BAR_EAST),
										 	 AABBs_AXIS_Y_DOWN  = Lists.newArrayList(AABB_AXIS_Y_DOWN_BAR_NORTH, AABB_AXIS_Y_DOWN_BAR_SOUTH, AABB_AXIS_Y_DOWN_BAR_WEST, AABB_AXIS_Y_DOWN_BAR_EAST),
										 	 AABBs_AXIS_Z_NORTH = Lists.newArrayList(AABB_AXIS_Z_NORTH_BAR_WEST, AABB_AXIS_Z_NORTH_BAR_EAST, AABB_AXIS_Z_NORTH_BAR_DOWN, AABB_AXIS_Z_NORTH_BAR_UP),
										 	 AABBs_AXIS_Z_SOUTH = Lists.newArrayList(AABB_AXIS_Z_SOUTH_BAR_WEST, AABB_AXIS_Z_SOUTH_BAR_EAST, AABB_AXIS_Z_SOUTH_BAR_DOWN, AABB_AXIS_Z_SOUTH_BAR_UP);
	
	public List<AxisAlignedBB> getCollisionBoxList(IBlockState state) {
		List<AxisAlignedBB> list = new ArrayList<>(8);
		boolean front = state.getValue(FRONT);
		boolean back  = state.getValue(BACK);
		switch(state.getValue(AXIS)) {
		case X:
			if(front)
				list.addAll(AABBs_AXIS_X_EAST);
			if(back)
				list.addAll(AABBs_AXIS_X_WEST);
			break;
		case Y:
			if(front)
				list.addAll(AABBs_AXIS_Y_UP);
			if(back)
				list.addAll(AABBs_AXIS_Y_DOWN);
			break;
		case Z:
			if(front)
				list.addAll(AABBs_AXIS_Z_SOUTH);
			if(back) 
				list.addAll(AABBs_AXIS_Z_NORTH);
			break;
		}
		if(list.isEmpty())
			list.add(FULL_BLOCK_AABB);
		return list;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		for(AxisAlignedBB axisalignedbb : getCollisionBoxList(state)) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, axisalignedbb);
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		boolean front = state.getValue(FRONT);
		boolean back  = state.getValue(BACK);
		switch(state.getValue(AXIS)) {
		case X:
			if(front && back)
				return AABB_AXIS_X;
			if(front)
				return AABB_AXIS_X_EAST;
			if(back)
				return AABB_AXIS_X_WEST;
			return AABB_AXIS_X;
		case Y:
			if(front && back)
				return AABB_AXIS_Y;
			if(front)
				return AABB_AXIS_Y_UP;
			if(back)
				return AABB_AXIS_Y_DOWN;
			return AABB_AXIS_Y;
		case Z:
			if(front && back)
				return AABB_AXIS_Z;
			if(front)
				return AABB_AXIS_Z_SOUTH;
			if(back)
				return AABB_AXIS_Z_NORTH;
			return AABB_AXIS_Z;
		default:
			throw new AssertionError();
		}
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@Nullable
	public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
		List<RayTraceResult> list = Lists.<RayTraceResult>newArrayList();

		for(AxisAlignedBB axisalignedbb : getCollisionBoxList(blockState)) {
			list.add(this.rayTrace(pos, start, end, axisalignedbb));
		}

		RayTraceResult raytraceresult1 = null;
		double d1 = 0.0D;

		for(RayTraceResult raytraceresult : list) {
			if(raytraceresult != null) {
				double d0 = raytraceresult.hitVec.squareDistanceTo(end);

				if(d0 > d1) {
					raytraceresult1 = raytraceresult;
					d1 = d0;
				}
			}
		}

		return raytraceresult1;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing.Axis axis;
		switch(meta & 0b11) {
		case 0:
			axis = EnumFacing.Axis.X;
			break;
		case 1:
			axis = EnumFacing.Axis.Y;
			break;
		case 2:
		case 3:
			axis = EnumFacing.Axis.Z;
			break;
		default:
			throw new AssertionError();
		}
		boolean front = (meta & (1 << 2)) != 0;
		boolean back  = (meta & (1 << 3)) != 0;
		return getDefaultState().withProperty(AXIS, axis).withProperty(FRONT, front).withProperty(BACK, back);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = state.getValue(AXIS).ordinal();
		if(state.getValue(FRONT))
			meta |= 1 << 2;
		if(state.getValue(BACK))
			meta |= 1 << 3;
		return meta;
	}

}
