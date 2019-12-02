package com.raptor.portalblocks.blocks;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Deprecated
public class QuadruplePanel extends BlockPanel{

	public static final PropertyInteger FACE = PropertyInteger.create("face", 0, 7);
	
	public QuadruplePanel(boolean canPortalOn){
		super(canPortalOn);
	}
	
	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, FACE);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos,
			EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand){
		int face;
		
		EnumFacing horiz = placer.getAdjustedHorizontalFacing();
		
		switch(facing){
		case UP:
			switch(horiz){
			case WEST:
				face = 0;
				break;
			case NORTH:
				face = 1;
				break;
			case EAST:
				face = 2;
				break;
			case SOUTH:
				face = 3;
				break;
			default:
				face = 0;
			}
			break;
		case DOWN:
			switch(horiz){
			case WEST:
				face = 4;
				break;
			case NORTH:
				face = 5;
				break;
			case EAST:
				face = 6;
				break;
			case SOUTH:
				face = 7;
				break;
			default:
				face = 0;
			}
			break;
		case NORTH:
			face = 0;
			break;
		case SOUTH:
			face = 2;
			break;
		case EAST:
			face = 3;
			break;
		case WEST:
			face = 1;
			break;
		default:
			face = 0;
		}
		
		IBlockState wantedState = getDefaultState().withProperty(FACE, face);
		
		BlockPos[] positions = getAllPositions(pos, wantedState);
		
		if(areAllBlocksReplaceable(world, positions))
			return wantedState;
		return Blocks.AIR.getDefaultState();
	}
	
	public BlockPos[] getAllPositions(BlockPos pos, IBlockState state){
		switch(state.getValue(FACE)){
		case 1:
			pos = pos.east();
			break;
		case 2:
			pos = pos.south().east();
			break;
		case 3:
			pos = pos.south();
			break;
		case 4:
			pos = pos.down();
			break;
		case 5:
			pos = pos.down().east();
			break;
		case 6:
			pos = pos.down().south().east();
			break;
		case 7:
			pos = pos.down().south();
			break;
		}
		
		return new BlockPos[]{pos, pos.west(), pos.north().west(), pos.north(), pos.up(), pos.up().west(), pos.up().north().west(), pos.up().north()};
	}
	
	public IBlockState[] getAllStates(World world, BlockPos[] pos){
		IBlockState[] result = new IBlockState[pos.length];
		for(int i = 0; i < pos.length; ++i){
			result[i] = world.getBlockState(pos[i]);
		}
		return result;
	}
	
	public boolean areAllBlocksReplaceable(World world, BlockPos[] pos){
		IBlockState[] states = getAllStates(world, pos);
		for(int i = 0; i < pos.length; ++i){
			if(!states[i].getBlock().isReplaceable(world, pos[i]))
				return false;
		}
		return true;
	}
	
	public IBlockState[] getExpectedStates(){
		IBlockState[] result = new IBlockState[8];
		for(int i = 0; i < 8; ++i){
			result[i] = getDefaultState().withProperty(FACE, i);
		}
		return result;
	}
	
	public boolean doStatesMatchExpected(World world, BlockPos[] pos){
		IBlockState[] actualStates = getAllStates(world, pos);
		IBlockState[] expectedStates = getExpectedStates();
		
		for(int i = 0; i < 8; ++i){
			if(!expectedStates[i].equals(actualStates[i]))
				return false;
		}
		return true;
	}
	
	public void setToExpectedStates(World world, BlockPos[] pos){
		for(int i = 0; i < 8; ++i){
			world.setBlockState(pos[i], getDefaultState().withProperty(FACE, i), world.isRemote? 10 : 2);
		}
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state){
		BlockPos[] positions = getAllPositions(pos, state);
		setToExpectedStates(worldIn, positions);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(FACE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		if(meta > 7)
			meta = 0;
		return getDefaultState().withProperty(FACE, meta);
	}
}
