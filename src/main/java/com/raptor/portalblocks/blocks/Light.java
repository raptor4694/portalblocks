package com.raptor.portalblocks.blocks;
/*
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.raptor.portalblocks.PortalItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Deprecated
public class Light extends Block{
	
	public static final PropertyEnum<EnumFacing> FACING = PropertyDirection
			.create("facing", Lists.newArrayList(EnumFacing.HORIZONTALS));
	public static final PropertyEnum<EnumOrientation> ORIENTATION = PropertyEnum
			.create("orientation", EnumOrientation.class);
	
	private static final AxisAlignedBB[] BOUNDING_BOXES = {new AxisAlignedBB(0.0,
			0.0, 0.8125, 1.0, 1.0, 1.0), // NORTH
			new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.0625), // SOUTH
			new AxisAlignedBB(0.9375, 0.0, 0.0, 1.0, 1.0, 1.0), // WEST
			new AxisAlignedBB(0.0, 0.0, 0.0, 0.0625, 1.0, 1.0), // EAST
			new AxisAlignedBB(0.0, 0.9375, 0.0, 1.0, 1.0, 1.0), // DOWN
			new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0), // UP
	};
	
	public final boolean isTopHalf;
	private Light otherHalf;
	
	public Light(boolean isTopHalf){
		super(Material.GLASS);
		this.isTopHalf = isTopHalf;
		setLightLevel(1.0f);
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH)
				.withProperty(ORIENTATION, EnumOrientation.SIDE));
	}
	
	public Light setOtherHalf(Light light){
		if(light.isTopHalf == isTopHalf) throw new RuntimeException(
				"Cannot set other half of " + this + " to " + light);
		this.otherHalf = light;
		light.otherHalf = this;
		return this;
	}
	
	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, ORIENTATION, FACING);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state){
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state){
		return false;
	}
	
	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState,
			IBlockAccess worldIn, BlockPos pos){
		return NULL_AABB;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn,
			BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn,
			boolean p_185477_7_){
		
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source,
			BlockPos pos){
		EnumOrientation orientation = state.getValue(ORIENTATION);
		if(orientation == EnumOrientation.SIDE)
			return BOUNDING_BOXES[state.getValue(FACING).ordinal() - 2];
		else if(orientation == EnumOrientation.BOTTOM)
			return BOUNDING_BOXES[4];
		else
			return BOUNDING_BOXES[5];
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos,
			EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand){
		int i = facing.getFrontOffsetX();
		int j = facing.getFrontOffsetZ();
		boolean flag = i < 0 && hitZ < 0.5F || i > 0 && hitZ > 0.5F || j < 0 && hitX > 0.5F || j > 0 && hitX < 0.5F;
		System.out.println("side flag: " + flag);
		EnumOrientation orientation;
		if(facing == EnumFacing.UP)
			orientation = EnumOrientation.TOP;
		else if(facing == EnumFacing.DOWN)
			orientation = EnumOrientation.BOTTOM;
		else
			orientation = EnumOrientation.SIDE;
		System.out.println(
				"\norientation = " + orientation + "\nfacing = " + facing + "\nplayer facing = " + placer
						.getAdjustedHorizontalFacing().getOpposite());
		return getDefaultState()
				.withProperty(FACING, placer
						.getAdjustedHorizontalFacing().getOpposite())
				.withProperty(ORIENTATION, orientation);
	}
	
	public BlockPos getOtherHalfPos(IBlockState state, BlockPos pos){
		EnumFacing facing = state.getValue(FACING);
		EnumOrientation orientation = state.getValue(ORIENTATION);
		
		if(orientation == EnumOrientation.SIDE){
			if(isTopHalf) return pos.down();
			else return pos.up();
		}else{
			if(isTopHalf) return pos.offset(facing.getOpposite());
			else return pos.offset(facing);
		}
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state){
		EnumFacing facing = state.getValue(FACING);
		EnumOrientation orientation = state.getValue(ORIENTATION);
		// EnumBlockHalf half = state.getValue(HALF);
		// System.out.println("\nONBLOCKADDED:\nfacing = " + facing + "\norientation = " + orientation + "\nhalf = " + half);
		if(isTopHalf){
			if(orientation == EnumOrientation.SIDE){
				IBlockState stateBelow = worldIn.getBlockState(pos.down());
				if(stateBelow.getBlock().isReplaceable(worldIn, pos.down())){
					worldIn.setBlockState(pos.down(), otherHalf.getDefaultState()
							.withProperty(FACING, facing)
							.withProperty(ORIENTATION, orientation));
				}else
					this.dropBlockAsItem(worldIn, pos, state, 0);
			}else{
				BlockPos pos2 = pos.offset(facing);
				IBlockState stateAside = worldIn.getBlockState(pos2);
				if(stateAside.getBlock().isReplaceable(worldIn, pos2)){
					worldIn.setBlockState(pos2, otherHalf.getDefaultState()
							.withProperty(FACING, facing)
							.withProperty(ORIENTATION, orientation));
				}else
					this.dropBlockAsItem(worldIn, pos, state, 0);
			}
		}
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_,
			IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_){
		return BlockFaceShape.UNDEFINED;
	}
	
	public IBlockState getLowerHalfStateForPlacement(IBlockState state){
		if(!isTopHalf) throw new RuntimeException(
				"Cannot get lower half state of lower half block");
		EnumFacing facing = state.getValue(FACING);
		EnumOrientation orientation = state.getValue(ORIENTATION);
		
		return getOtherHalf().getDefaultState().withProperty(FACING, facing)
				.withProperty(ORIENTATION, orientation);
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos){
		return super.canPlaceBlockAt(worldIn, pos);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos,
			Block blockIn, BlockPos fromPos){
		EnumFacing facing = state.getValue(FACING);
		EnumOrientation orientation = state.getValue(ORIENTATION);
		// EnumBlockHalf half = state.getValue(HALF);
		if(isTopHalf){
			if(orientation == EnumOrientation.SIDE){
				BlockPos down = pos.down();
				IBlockState stateDown = worldIn.getBlockState(down);
				if(stateDown.getBlock() != getOtherHalf() || stateDown
						.getValue(FACING) != facing || stateDown.getValue(
								ORIENTATION) != orientation || stateDown.getValue(HALF) != EnumBlockHalf.BOTTOM){
					worldIn.setBlockToAir(pos);
					this.dropBlockAsItem(worldIn, pos, state, 0);
				}else{
					down = pos.offset(facing, -1);
					stateDown = worldIn.getBlockState(down);
					if(!stateDown.isSideSolid(worldIn, down, facing) && !(stateDown.getBlock() instanceof BlockPanel)){
						worldIn.setBlockToAir(pos);
						this.dropBlockAsItem(worldIn, pos, state, 0);
						worldIn.setBlockToAir(pos.down());
					}else{
						down = down.down();
						stateDown = worldIn.getBlockState(down);
						if(!stateDown.isSideSolid(worldIn, down, facing) && !(stateDown.getBlock() instanceof BlockPanel)){
							worldIn.setBlockToAir(pos);
							this.dropBlockAsItem(worldIn, pos, state, 0);
							worldIn.setBlockToAir(pos.down());
						}
					}
				}
			}else{
				BlockPos down = pos.offset(facing.getOpposite());
				IBlockState stateDown = worldIn.getBlockState(down);
				if(stateDown.getBlock() != getOtherHalf() || stateDown
						.getValue(FACING) != facing || stateDown.getValue(
								ORIENTATION) != orientation || stateDown.getValue(HALF) != EnumBlockHalf.BOTTOM){
					//System.out.println("\n[top] top block was not correct (my pos = " + pos + ", top block pos = " + down + ", my state = " + state + ", top block state = " + stateDown);
					worldIn.setBlockToAir(pos);
					this.dropBlockAsItem(worldIn, pos, state, 0);
				}else{
					down = pos.offset(orientation.getOpposite().toEnumFacing());
					stateDown = worldIn.getBlockState(down);
					if(!stateDown.isSideSolid(worldIn, down, orientation.toEnumFacing()) && !(stateDown.getBlock() instanceof BlockPanel)){
						//System.out.println("\n[top] block face was not solid");
						worldIn.setBlockToAir(pos);
						this.dropBlockAsItem(worldIn, pos, state, 0);
						worldIn.setBlockToAir(pos.offset(facing));
					}else{
						down = down.offset(facing.getOpposite());
						stateDown = worldIn.getBlockState(down);
						if(!stateDown.isSideSolid(worldIn, down, orientation.toEnumFacing()) && !(stateDown.getBlock() instanceof BlockPanel)){
							//System.out.println("\n[top] bottom block face was not solid");
							worldIn.setBlockToAir(pos);
							this.dropBlockAsItem(worldIn, pos, state, 0);
							worldIn.setBlockToAir(pos.offset(facing));
						}
					}
				}
			}
		}else{
			if(orientation == EnumOrientation.SIDE){
				BlockPos down = pos.up();
				IBlockState stateDown = worldIn.getBlockState(down);
				if(stateDown.getBlock() != getOtherHalf() || stateDown
						.getValue(FACING) != facing || stateDown.getValue(
								ORIENTATION) != orientation || stateDown.getValue(HALF) != EnumBlockHalf.TOP){
					worldIn.setBlockToAir(pos);
					//this.dropBlockAsItem(worldIn, pos, state, 0);
				}else{
					down = pos.offset(facing, -1);
					stateDown = worldIn.getBlockState(down);
					if(!stateDown.isSideSolid(worldIn, down, facing) && !(stateDown.getBlock() instanceof BlockPanel)){
						worldIn.setBlockToAir(pos);
						this.dropBlockAsItem(worldIn, pos, state, 0);
						worldIn.setBlockToAir(pos.up());
					}else{
						down = down.up();
						stateDown = worldIn.getBlockState(down);
						if(!stateDown.isSideSolid(worldIn, down, facing) && !(stateDown.getBlock() instanceof BlockPanel)){
							worldIn.setBlockToAir(pos);
							//this.dropBlockAsItem(worldIn, pos, state, 0);
							worldIn.setBlockToAir(pos.up());
						}
					}
				}
			}else{
				BlockPos down = pos.offset(facing);
				IBlockState stateDown = worldIn.getBlockState(down);
				if(stateDown.getBlock() != getOtherHalf() || stateDown
						.getValue(FACING) != facing || stateDown
								.getValue(ORIENTATION) != orientation){
				//	System.out.println("\n[btm] top block was not correct (my pos = " + pos + ", top block pos = " + down + ", my state = " + state + ", top block state = " + stateDown);
					worldIn.setBlockToAir(pos);
					//this.dropBlockAsItem(worldIn, pos, state, 0);
				}else{
					down = pos.offset(orientation.getOpposite().toEnumFacing());
					stateDown = worldIn.getBlockState(down);
					if(!stateDown.isSideSolid(worldIn, down,
							orientation.toEnumFacing()) && !(stateDown.getBlock() instanceof BlockPanel)){
						//System.out.println("\n[btm] block face was not solid");
						worldIn.setBlockToAir(pos);
						//this.dropBlockAsItem(worldIn, pos, state, 0);
						worldIn.setBlockToAir(pos.offset(facing, -1));
					}else{
						down = down.offset(facing);
						stateDown = worldIn.getBlockState(down);
						if(!stateDown.isSideSolid(worldIn, down, orientation.toEnumFacing()) && !(stateDown.getBlock() instanceof BlockPanel)){
							//System.out.println("\n[btm] top block face was not solid");
							worldIn.setBlockToAir(pos);
							//this.dropBlockAsItem(worldIn, pos, state, 0);
							worldIn.setBlockToAir(pos.offset(facing, -1));
						}
					}
				}
			}
		}
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos,
			IBlockState state){
		// EnumOrientation orientation = state.getValue(ORIENTATION);
		// EnumFacing facing = state.getValue(FACING);
		//if(isTopHalf){
			super.onBlockDestroyedByPlayer(worldIn, pos, state);
		//}else{
		if(!isTopHalf){
			//worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			
			//System.out.println("Set block to air");
			
			EnumFacing facing = state.getValue(FACING);
			EnumOrientation orientation = state.getValue(ORIENTATION);
			
			if(orientation == EnumOrientation.SIDE)
				pos = pos.up();
			else
				pos = pos.offset(facing);
			
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), worldIn.isRemote? 10 : 2);
			
			//otherHalf.onBlockDestroyedByPlayer(worldIn, pos, worldIn.getBlockState(pos));
		}
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos,
			Explosion explosionIn){
		onBlockDestroyedByPlayer(worldIn, pos, worldIn.getBlockState(pos));
	}
	
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state){
		return new ItemStack(PortalItems.LIGHT);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune){
		return isTopHalf? PortalItems.LIGHT : Items.AIR;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos){
		if(state.getValue(HALF) == EnumBlockHalf.TOP)
	}
	
	private static final int FACING_MASK = 0b0011,
			ORIENTATION_MASK = 0b1100;
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		int facing_value = meta & FACING_MASK;
		if(facing_value >= EnumFacing.HORIZONTALS.length) facing_value = 0;
		int orientation_value = meta & ORIENTATION_MASK;
		
		//System.out.println("\nfacing part = " + Integer.toBinaryString(facing_value) + "\norientation part = " + Integer.toBinaryString(orientation_value));
		
		System.out.println("meta & orientation mask = " + Integer.toBinaryString(
				meta & ORIENTATION_MASK) + '(' + (meta & ORIENTATION_MASK) + ')');
		System.out.println("meta & orientation mas << 3 = " + Integer
				.toBinaryString(orientation_value) + '(' + orientation_value + ')');
		
		// boolean is_top = (meta & HALF_MASK) == HALF_MASK;
		EnumFacing facing = EnumFacing.VALUES[facing_value + 2]; // +2 because VALUES starts with UP, DOWN
		EnumOrientation orientation = EnumOrientation.VALUES[orientation_value >> 2];
		
		return getDefaultState().withProperty(FACING, facing)
				.withProperty(ORIENTATION, orientation);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		EnumFacing facing = state.getValue(FACING);
		EnumOrientation orientation = state.getValue(ORIENTATION);
		System.out.println("\nfacing.ordinal()-2 = " + (facing
				.ordinal() - 2) + "\norientation.ordinal() >> 2 = " + (orientation
						.ordinal() >> 2));
		//System.out.println("\nstate = " + state + "\nfacing part = " + Integer.toBinaryString(facing.ordinal()-2) + "\norientation part = " + Integer.toBinaryString(orientation.ordinal() << 2));
		
		int meta = facing.ordinal() - 2 | orientation.ordinal() << 2;
		
		//System.out.println("\nMETA = " + meta + "\nSTATE = " + state);
		
		return meta;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer(){
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	public Light getOtherHalf(){
		return otherHalf;
	}

	public static enum EnumOrientation implements IStringSerializable{
		SIDE, TOP{
			@Override
			public EnumOrientation getOpposite(){
				return BOTTOM;
			}
			@Override
			public EnumFacing toEnumFacing(){
				return EnumFacing.UP;
			}
		}, BOTTOM{
			@Override
			public EnumOrientation getOpposite(){
				return TOP;
			}
			@Override
			public EnumFacing toEnumFacing(){
				return EnumFacing.DOWN;
			}
		};
		public static final EnumOrientation[] VALUES = {SIDE, TOP, BOTTOM};
		
		@Override
		public String getName(){
			return toString().toLowerCase();
		}
		
		public EnumOrientation getOpposite(){
			throw new UnsupportedOperationException();
		}
		
		public EnumFacing toEnumFacing(){
			throw new UnsupportedOperationException();
		}
	}
	
}
*/