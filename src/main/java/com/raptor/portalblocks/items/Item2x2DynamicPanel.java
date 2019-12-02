package com.raptor.portalblocks.items;

import com.raptor.portalblocks.tileentities.TileEntityDynamicPanel;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Deprecated
public class Item2x2DynamicPanel extends ItemDynamicPanel {

	public Item2x2DynamicPanel(Block block, boolean isPortalable) {
		super(block, isPortalable);
	}

	@Override
	protected EnumActionResult modifyPanels(EntityPlayer player, World worldIn, TileEntityDynamicPanel te,
			IBlockState state, BlockPos pos, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ) {
		return null;
	}

}
