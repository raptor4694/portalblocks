package com.raptor.portalblocks.blocks;

import static com.raptor.portalblocks.PortalBlocks.createBoundingBox;

import java.util.List;

import com.raptor.portalblocks.PortalSounds;

import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFloorButton extends BlockPressurePlate {

	public BlockFloorButton(Material materialIn, Sensitivity sensitivityIn) {
		super(materialIn, sensitivityIn);
	}
	
	public BlockFloorButton() {
		this(Material.IRON, Sensitivity.MOBS);
	}

	private static final AxisAlignedBB PRESSED_AABB   = createBoundingBox(0, 0, 0, 16, 4, 16);
	private static final AxisAlignedBB UNPRESSED_AABB = createBoundingBox(0, 0, 0, 16, 5, 16);
	private static final AxisAlignedBB PRESSURE_AABB  = createBoundingBox(0, 0, 0, 16, 8, 16);
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(POWERED)? PRESSED_AABB : UNPRESSED_AABB;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
//		return /*state.getValue(POWERED)? BOUNDING_BOX_PRESSED :*/ NULL_AABB;
//		return state.getValue(POWERED)? createBoundingBox(0, 0, 0, 16, 7, 16) : createBoundingBox(0, 0, 0, 16, 6, 16);
		return state.getValue(POWERED)? PRESSED_AABB : UNPRESSED_AABB;
	}
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		if(!worldIn.isRemote) {
			IBlockState state = worldIn.getBlockState(pos);
			int i = this.getRedstoneStrength(state);
			if(i == 0) {
				this.updateState(worldIn, pos, state, i);
			}
		}
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if(!worldIn.isRemote) {
			int i = this.getRedstoneStrength(state);

			if(i == 0) {
				this.updateState(worldIn, pos, state, i);
			}
		}
	}

	@Override
	protected void playClickOnSound(World worldIn, BlockPos color) {
		worldIn.playSound(null, color, PortalSounds.button_down, SoundCategory.BLOCKS, 0.1F, 0.8F);
	}

	@Override
	protected void playClickOffSound(World worldIn, BlockPos pos) {
		worldIn.playSound(null, pos, PortalSounds.button_up, SoundCategory.BLOCKS, 0.1F, 0.7F);
	}
	
	@Override
	protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
		AxisAlignedBB axisalignedbb = PRESSURE_AABB.offset(pos);
		List<? extends Entity> list = worldIn.<Entity>getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

		if(!list.isEmpty()) {
			for(Entity entity : list) {
				if(!entity.doesEntityNotTriggerPressurePlate()) {
					return 15;
				}
			}
		}

		return 0;
	}
	
}
