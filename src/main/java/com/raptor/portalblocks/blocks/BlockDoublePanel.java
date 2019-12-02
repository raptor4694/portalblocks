package com.raptor.portalblocks.blocks;

import static com.google.common.collect.Maps.newEnumMap;
import static com.raptor.portalblocks.blocks.FaceInfo.faceInfo;

import java.util.EnumMap;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoublePanel extends BlockPanel {

	public static final PropertyEnum<EnumFacing> FACING = BlockDirectional.FACING;

	public BlockDoublePanel(boolean canPortalOn) {
		super(canPortalOn);
		this.setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.DOWN));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing face, final float hitX, final float hitY,
			final float hitZ, final int meta, final EntityLivingBase placer, final EnumHand hand) {
		EnumFacing facing, facing2;
		
		figureOutState:
		if(placer.isSneaking()) {
			facing = adjustFacingInWorld(world, pos, face.getOpposite());
			if(facing == null)
				return Blocks.AIR.getDefaultState();
		}
		else {
			facing = null;
			
			IBlockState state;
			if(face.getAxis() == EnumFacing.Axis.Y) {
				state = world.getBlockState(pos.offset(facing = face.getOpposite()));
				if(state.getBlock() instanceof BlockDoublePanel) {
					facing = adjustFacingInWorld(world, pos, state.getValue(FACING));
				}
				else {
					facing = adjustFacingInWorld(world, pos, facing /* has been set to face.getOpposite() */);
				}
			}
			
			if(facing == null) {
				for(EnumFacing dir : EnumFacing.Plane.HORIZONTAL) {
					state = world.getBlockState(pos.offset(dir));
					if(state.getBlock() instanceof BlockDoublePanel) {
						facing2 = state.getValue(FACING);
						if(facing2.getAxis() == EnumFacing.Axis.Y) {
							if(facing == null) {
								facing = adjustFacingInWorld(world, pos, facing2);
								if(facing == null)
									return Blocks.AIR.getDefaultState();
							}
							else {
								facing = adjustFacingInWorld(world, pos, hitY > 0.5f? EnumFacing.UP : EnumFacing.DOWN);
								if(facing == null)
									return Blocks.AIR.getDefaultState();
								else
									break figureOutState;
							}
						}
						else if(facing == null) {
							facing = adjustFacingInWorld(world, pos, facing2);
						}
					}
				}
				
				if(facing == null) {
					facing = adjustFacingInWorld(world, pos, face.getOpposite());
					if(facing == null)
						return Blocks.AIR.getDefaultState();
				}			
			}
		}
		
		return getDefaultState().withProperty(FACING, facing);
	}
	
	protected EnumFacing adjustFacingInWorld(World world, BlockPos pos, EnumFacing desiredFacing) {
		BlockPos pos2 = pos.offset(desiredFacing.getOpposite());
		IBlockState state = world.getBlockState(pos2);
		if(state.getBlock().isReplaceable(world, pos2)) {
			return desiredFacing;
		}
		else {
			pos2 = pos.offset(desiredFacing);
			state = world.getBlockState(pos2);
			return state.getBlock().isReplaceable(world, pos2)? desiredFacing.getOpposite() : null;
		}
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		EnumFacing facingOpposite = state.getValue(FACING).getOpposite();
		BlockPos pos2 = pos.offset(facingOpposite);
		IBlockState state2 = world.getBlockState(pos2);
		if(state2.getBlock() == this) {
			if(state2.getValue(FACING) != facingOpposite) {
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
		}
		else {
			if(state2.getBlock().isReplaceable(world, pos2)) {
				world.setBlockState(pos2, getDefaultState().withProperty(FACING, facingOpposite));
			}
			else {
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
		}
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(this);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return state.getValue(FACING).getAxisDirection() == AxisDirection.NEGATIVE? Items.AIR : Item.getItemFromBlock(this);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		EnumFacing facingOpposite = state.getValue(FACING).getOpposite();
		BlockPos pos2 = pos.offset(facingOpposite);
		IBlockState state2 = worldIn.getBlockState(pos2);
		
		if(state2.getBlock() != this || state2.getValue(FACING) != facingOpposite) {
			worldIn.setBlockToAir(pos);
			if(!worldIn.isRemote) {
				if(Minecraft.getMinecraft().player.capabilities.isCreativeMode)
					return;
			}
			blockIn.dropBlockAsItem(worldIn, pos, state, 0);	
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
	}
	
	private static final EnumMap<EnumFacing, EnumMap<EnumFacing, FaceInfo>> FACE_TYPE_LOOKUP = newEnumMap(EnumFacing.class);
	
	static {
		EnumMap<EnumFacing, FaceInfo> map;
		FaceInfo faceInfoNone   = faceInfo(EnumPanelFace.NONE, EnumPanelFace.NONE);
		FaceInfo faceInfoSquare = faceInfo(EnumPanelFace.WHITE_SQUARE, EnumPanelFace.BLACK_SQUARE);
		FaceInfo faceInfoLeft   = faceInfo(EnumPanelFace.WHITE_TALL_LEFT, EnumPanelFace.BLACK_TALL_LEFT);
		FaceInfo faceInfoRight  = faceInfo(EnumPanelFace.WHITE_TALL_RIGHT, EnumPanelFace.BLACK_TALL_RIGHT);
		FaceInfo faceInfoTop    = faceInfo(EnumPanelFace.WHITE_TALL_TOP, EnumPanelFace.BLACK_TALL_TOP);
		FaceInfo faceInfoBottom = faceInfo(EnumPanelFace.WHITE_TALL_BOTTOM, EnumPanelFace.BLACK_TALL_BOTTOM);

		FACE_TYPE_LOOKUP.put(EnumFacing.UP, map = newEnumMap(EnumFacing.class));
		map.put(EnumFacing.UP,    faceInfoSquare);
		map.put(EnumFacing.DOWN,  faceInfoNone);
		map.put(EnumFacing.NORTH, faceInfoTop);
		map.put(EnumFacing.SOUTH, faceInfoTop);
		map.put(EnumFacing.EAST,  faceInfoTop);
		map.put(EnumFacing.WEST,  faceInfoTop);
		FACE_TYPE_LOOKUP.put(EnumFacing.DOWN, map = newEnumMap(EnumFacing.class));
		map.put(EnumFacing.UP,    faceInfoNone);
		map.put(EnumFacing.DOWN,  faceInfoSquare);
		map.put(EnumFacing.NORTH, faceInfoBottom);
		map.put(EnumFacing.SOUTH, faceInfoBottom);
		map.put(EnumFacing.EAST,  faceInfoBottom);
		map.put(EnumFacing.WEST,  faceInfoBottom);
		FACE_TYPE_LOOKUP.put(EnumFacing.NORTH, map = newEnumMap(EnumFacing.class));
		map.put(EnumFacing.UP,    faceInfoTop);
		map.put(EnumFacing.DOWN,  faceInfoBottom);
		map.put(EnumFacing.NORTH, faceInfoSquare);
		map.put(EnumFacing.SOUTH, faceInfoNone);
		map.put(EnumFacing.EAST,  faceInfoRight);
		map.put(EnumFacing.WEST,  faceInfoLeft);
		FACE_TYPE_LOOKUP.put(EnumFacing.SOUTH, map = newEnumMap(EnumFacing.class));
		map.put(EnumFacing.UP,    faceInfoBottom);
		map.put(EnumFacing.DOWN,  faceInfoTop);
		map.put(EnumFacing.NORTH, faceInfoNone);
		map.put(EnumFacing.SOUTH, faceInfoSquare);
		map.put(EnumFacing.EAST,  faceInfoLeft);
		map.put(EnumFacing.WEST,  faceInfoRight);
		FACE_TYPE_LOOKUP.put(EnumFacing.EAST, map = newEnumMap(EnumFacing.class));
		map.put(EnumFacing.UP,    faceInfoRight);
		map.put(EnumFacing.DOWN,  faceInfoRight);
		map.put(EnumFacing.NORTH, faceInfoLeft);
		map.put(EnumFacing.SOUTH, faceInfoRight);
		map.put(EnumFacing.EAST,  faceInfoSquare);
		map.put(EnumFacing.WEST,  faceInfoNone);
		FACE_TYPE_LOOKUP.put(EnumFacing.WEST, map = newEnumMap(EnumFacing.class));
		map.put(EnumFacing.UP,    faceInfoLeft);
		map.put(EnumFacing.DOWN,  faceInfoLeft);
		map.put(EnumFacing.NORTH, faceInfoRight);
		map.put(EnumFacing.SOUTH, faceInfoLeft);
		map.put(EnumFacing.EAST,  faceInfoNone);
		map.put(EnumFacing.WEST,  faceInfoSquare);
	}
	
	@Override
	public EnumPanelFace getFaceType(EnumFacing face, IBlockState state, IBlockAccess world, BlockPos pos) {
		return FACE_TYPE_LOOKUP.get(state.getValue(FACING)).get(face).getFaceType(portalable);
	}

}
