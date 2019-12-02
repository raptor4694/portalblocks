package com.raptor.portalblocks.blocks;

import com.raptor.portalblocks.PortalBlocksMod;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockPanel extends Block {
	private final boolean portalable;
	
	public BlockPanel(Material material, boolean canPortalOn) {
		super(material, canPortalOn? MapColor.SNOW : MapColor.GRAY);
		portalable = canPortalOn;
		setHardness(5.0f);
		setResistance(10.0f);
		setSoundType(SoundType.METAL);
		setCreativeTab(PortalBlocksMod.PORTAL_TAB);
	}
	
	public BlockPanel(boolean canPortalOn){
		this(Material.IRON, canPortalOn);
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return isPortalable();
	}
	
	public boolean isPortalable() {
		return portalable;
	}
	
}
