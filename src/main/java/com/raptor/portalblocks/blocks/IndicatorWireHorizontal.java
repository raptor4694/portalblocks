package com.raptor.portalblocks.blocks;

@Deprecated
class IndicatorWireHorizontal {}

/*public class IndicatorWireHorizontal extends BlockPanel{
	public static final PropertyBool POWERED = PropertyBool.create("powered");

	private boolean canProvidePower = true;

	public IndicatorWireHorizontal(boolean canPortalOn){
		super(canPortalOn);
	}

	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, POWERED);
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side){
		return !this.canProvidePower? 0 : blockState.getWeakPower(blockAccess, pos, side);
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side){
		if(!this.canProvidePower){
			return 0;
		}else{
			boolean isPowered = (blockState.getValue(POWERED));

			if(!isPowered){
				return 0;
			}else{
				//EnumSet<EnumFacing> enumset = EnumSet.<EnumFacing>noneOf(EnumFacing.class);

				
				
				for(EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL){
					if(this.isBlockPoweredAt(blockAccess, pos, enumfacing)){
						return 15;
					}
				}
				
				return 0;
			}
		}
	}
	
	private boolean isBlockPoweredAt(IBlockAccess worldIn, BlockPos pos, EnumFacing facing){
		return worldIn.getStrongPower(pos.offset(facing), facing.getOpposite()) > 0;
	}

	private boolean isPowerSourceAt(IBlockAccess worldIn, BlockPos pos, EnumFacing side){
		BlockPos blockpos = pos.offset(side);
		IBlockState sideblockstate = worldIn.getBlockState(blockpos);
		boolean sideblock_isNormalCube = sideblockstate.isNormalCube();
		IBlockState upblockstate = worldIn.getBlockState(pos.up());
		boolean upblock_isNormalCube = upblockstate.isNormalCube();

		if(!upblock_isNormalCube && sideblock_isNormalCube && canConnectUpwardsTo(worldIn, blockpos.up())){
			return true;
		}else if(canConnectTo(sideblockstate, side, worldIn, pos)){
			return true;
		}else if(sideblockstate.getBlock() == Blocks.POWERED_REPEATER
				&& sideblockstate.getValue(BlockRedstoneDiode.FACING) == side){
			return true;
		}else{
			return !sideblock_isNormalCube && canConnectUpwardsTo(worldIn, blockpos.down());
		}
	}

	protected static boolean canConnectUpwardsTo(IBlockAccess worldIn, BlockPos pos){
		return canConnectTo(worldIn.getBlockState(pos), null, worldIn, pos);
	}

	protected static boolean canConnectTo(IBlockState blockState, @Nullable EnumFacing side, IBlockAccess world,
			BlockPos pos){
		Block block = blockState.getBlock();

		if(block instanceof IndicatorWireHorizontal){
			return true;
		}else if(Blocks.UNPOWERED_REPEATER.isSameDiode(blockState)){
			EnumFacing enumfacing = blockState.getValue(BlockRedstoneRepeater.FACING);
			return enumfacing == side || enumfacing.getOpposite() == side;
		}else if(Blocks.OBSERVER == blockState.getBlock()){
			return side == blockState.getValue(BlockObserver.FACING);
		}else{
			return blockState.getBlock().canConnectRedstone(blockState, world, pos, side);
		}
	}

}
*/