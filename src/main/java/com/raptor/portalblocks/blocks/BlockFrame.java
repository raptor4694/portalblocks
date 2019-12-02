package com.raptor.portalblocks.blocks;

import static com.raptor.portalblocks.PortalBlocks.createBoundingBox;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFrame extends Block {

	public static final PropertyEnum<EnumFacing.Axis> AXIS = BlockLog.AXIS;

	public static final AxisAlignedBB AABB_AXIS_X = createBoundingBox(2, 0, 0, 14, 16, 16);
	public static final AxisAlignedBB AABB_AXIS_Y = createBoundingBox(0, 2, 0, 16, 14, 16);
	public static final AxisAlignedBB AABB_AXIS_Z = createBoundingBox(0, 0, 2, 16, 16, 14);

	public BlockFrame() {
		super(Material.IRON);
		setHardness(5.0F);
		setResistance(10.0F);
		setSoundType(SoundType.METAL);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, AXIS);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(AXIS).ordinal();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing.Axis axis;
		switch(MathHelper.abs(meta) % 3) {
			case 0: axis = EnumFacing.Axis.X; break;
			case 1: axis = EnumFacing.Axis.Y; break;
			case 2: axis = EnumFacing.Axis.Z; break;
			default: throw new AssertionError();
		}
		return getDefaultState().withProperty(AXIS, axis);
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return true;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch(state.getValue(AXIS)) {
		case X:
			return AABB_AXIS_X;
		case Y:
			return AABB_AXIS_Y;
		case Z:
			return AABB_AXIS_Z;
		default:
			throw new AssertionError();
		}
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(AXIS, EnumFacing.getDirectionFromEntityLiving(pos, placer).getAxis());
	}

}
