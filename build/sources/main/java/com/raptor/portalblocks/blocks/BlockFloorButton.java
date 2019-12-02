package com.raptor.portalblocks.blocks;

import com.raptor.portalblocks.PortalSounds;

import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFloorButton extends BlockPressurePlate {

	public BlockFloorButton(Material materialIn, Sensitivity sensitivityIn) {
		super(materialIn, sensitivityIn);
	}
	
	public BlockFloorButton() {
		this(Material.IRON, Sensitivity.MOBS);
	}

	private static final AxisAlignedBB BOUNDING_BOX_PRESSED = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.25, 1.0),
			BOUNDING_BOX_UNPRESSED = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.3125, 1.0);

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(POWERED)? BOUNDING_BOX_PRESSED : BOUNDING_BOX_UNPRESSED;
	}

	@Override
	protected void playClickOnSound(World worldIn, BlockPos color) {
		worldIn.playSound(null, color, PortalSounds.button_down, SoundCategory.BLOCKS, 0.1F, 0.8F);
	}

	@Override
	protected void playClickOffSound(World worldIn, BlockPos pos) {
		worldIn.playSound(null, pos, PortalSounds.button_up, SoundCategory.BLOCKS, 0.1F, 0.7F);
	}
}
