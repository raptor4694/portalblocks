package com.raptor.portalblocks.blocks;

import static com.raptor.portalblocks.PortalBlocks.createBoundingBox;

import java.util.Random;

import javax.annotation.Nullable;

import com.raptor.portalblocks.PortalSounds;

import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockPedestalButton extends BlockHorizontal {

	public static final PropertyBool POWERED = BlockButton.POWERED;
	
	private BlockPedestalButton() {
		super(Material.CIRCUITS);
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
	}
	
	private static final AxisAlignedBB AABB_BOTTOM = createBoundingBox(5, 0, 5, 11, 16, 11);
	private static final AxisAlignedBB AABB_TOP    = createBoundingBox(5, 0, 5, 11, 4, 11);
	
	private static final AxisAlignedBB HITBOX_BOTTOM = createBoundingBox(5, 0, 5, 11, 20, 11);
	private static final AxisAlignedBB HITBOX_TOP    = createBoundingBox(5, -16, 5, 11, 4, 11);
	
	public static abstract class Top extends BlockPedestalButton {
		
		public Top() {
			setTickRandomly(true);
		}
		
		public abstract BlockPedestalButton.Bottom getBottomBlock();
		
		public boolean isBottomBlock(Block block) {
			return block == this.getBottomBlock();
		}
		
		@Override
		public int tickRate(World worldIn) {
			return 20;
		}
		
		@Override
		public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
				float hitZ, int meta, EntityLivingBase placer) {
			return Blocks.AIR.getDefaultState();
		}
		
		@Override
		@Nullable
		public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
			return AABB_TOP;
		}
		
		@Override
		public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
			return HITBOX_TOP;
		}
		
		@Override
		public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
			IBlockState stateDown = worldIn.getBlockState(pos.down());
			return isBottomBlock(stateDown.getBlock());
		}
		
		protected void playClickSound(EntityPlayer player, World worldIn, BlockPos pos) {
			worldIn.playSound((EntityPlayer)null, pos, PortalSounds.button_press, SoundCategory.BLOCKS, 0.3F, 0.8F);
		}

		protected void playReleaseSound(World worldIn, BlockPos pos) {

		}
		
		@Override
		public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
				EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
			if(state.getValue(POWERED).booleanValue()) {
				return true;
			}
			else {
				worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)), 3);
				worldIn.markBlockRangeForRenderUpdate(pos, pos);
				this.playClickSound(playerIn, worldIn, pos);
				this.notifyNeighbors(worldIn, pos);
				worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
				return true;
			}
		}
		
		@Override
		public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
		
		@Override
		public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
			if(!worldIn.isRemote) {
				if(state.getValue(POWERED)) {
					worldIn.setBlockState(pos, state.withProperty(POWERED, false));
					this.notifyNeighbors(worldIn, pos);
					this.playReleaseSound(worldIn, pos);
					worldIn.markBlockRangeForRenderUpdate(pos, pos);
				}
			}
		}
		
		@Override
		public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
			return blockState.getValue(POWERED)? 15 : 0;
		}

		@Override
		public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
			if(!blockState.getValue(POWERED)) {
				return 0;
			}
			else {
				// TODO
				return blockState.getValue(FACING) == side? 15 : 0;
			}
		}
		
		@Override
		protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
			return !isBottomBlock(worldIn.getBlockState(pos.down()).getBlock());
		}
		
		@Override
		public Item getItemDropped(IBlockState state, Random rand, int fortune) {
			return Items.AIR;
		}
		
		@Override
		public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
			return getBottomBlock().getPickBlock(state, target, world, pos, player);
		}
		
	}
	
	public static abstract class Bottom extends BlockPedestalButton {
		
		public Bottom() {}
		
		public abstract BlockPedestalButton.Top getTopBlock();
		
		public boolean isTopBlock(Block block) {
			return block == this.getTopBlock();
		}
		
		@Override
		public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
				float hitZ, int meta, EntityLivingBase placer) {
			return getDefaultState().withProperty(FACING, placer.getAdjustedHorizontalFacing());
		}
		
		@Override
		@Nullable
		public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
			return AABB_BOTTOM;
		}
		
		@Override
		public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
			return HITBOX_BOTTOM;
		}

		@Override
		public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
			IBlockState state;
			BlockPos pos2;
			if(!worldIn.isAirBlock(pos2 = pos.up())) {
				state = worldIn.getBlockState(pos2);
				if(!isTopBlock(state.getBlock()))
					return false;
			}
			state = worldIn.getBlockState(pos.down());
			return state.isSideSolid(worldIn, pos.down(), EnumFacing.UP) || state.getBlock() instanceof BlockPanel;
		}
		
		protected boolean isPowered(IBlockAccess world, BlockPos pos) {
			BlockPos posUp = pos.up();
			IBlockState state2 = world.getBlockState(posUp);
			if(isTopBlock(state2.getBlock()))
				return state2.getValue(POWERED);
			return false;
		}
		
		@Override
		public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
			return isPowered(world, pos)? 15 : 0;
		}

		@Override
		public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
			if(!isPowered(world, pos) || side == EnumFacing.UP) {
				return 0;
			}
			else {
				return 15;
			}
		}
		
		@Override
		public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
			BlockPos posUp = pos.up();
			if(fromPos.equals(posUp)) {
				IBlockState state2 = worldIn.getBlockState(posUp);
				if(isTopBlock(state2.getBlock())) {
					boolean powered  = state.getValue(POWERED);
					boolean powered2 = state2.getValue(POWERED);
					if(powered != powered2) {
						worldIn.setBlockState(pos, state.withProperty(POWERED, powered2));
						this.notifyNeighbors(worldIn, pos);
						worldIn.markBlockRangeForRenderUpdate(pos, pos);
					}
				}
			}
			super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		}
		
		@Override
		protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
			return !isTopBlock(worldIn.getBlockState(pos.up()).getBlock());
		}
		
		@Override
		public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
			worldIn.setBlockState(pos.up(), getTopBlock().getDefaultState().withProperty(POWERED, state.getValue(POWERED)).withProperty(FACING, state.getValue(FACING)), 3);
		}
		
		@Override
		public Item getItemDropped(IBlockState state, Random rand, int fortune) {
			return Item.getItemFromBlock(this);
		}
		
		@Override
		public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
			return new ItemStack(this);
		}
		
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, POWERED);
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
	public int getMetaFromState(IBlockState state) {
		int meta = state.getValue(FACING).getHorizontalIndex();
		if(state.getValue(POWERED))
			meta |= 1 << 2;
		return meta;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getHorizontal(meta & 0b11);
		boolean powered = (meta >> 2 & 1) != 0;
		return getDefaultState().withProperty(FACING, facing).withProperty(POWERED, powered);
	}

	protected void notifyNeighbors(World worldIn, BlockPos pos) {
		worldIn.notifyNeighborsOfStateChange(pos, this, false);
		worldIn.notifyNeighborsOfStateChange(pos.down(), this, false);
		for(EnumFacing facing : EnumFacing.Plane.HORIZONTAL) {
			worldIn.notifyNeighborsOfStateChange(pos.offset(facing), this, false);
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if(state.getValue(POWERED)) {
			this.notifyNeighbors(worldIn, pos);
		}

		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}
	
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if(this.checkForDrop(worldIn, pos, state) || !canPlaceBlockAt(worldIn, pos)) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
	}

	protected abstract boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state);
	
}
