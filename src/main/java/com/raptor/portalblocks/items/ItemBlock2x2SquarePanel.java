package com.raptor.portalblocks.items;

import com.raptor.portalblocks.blocks.BlockLargeSquarePanel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant.EnumBlockHalf;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlock2x2SquarePanel extends ItemBlock {

	public ItemBlock2x2SquarePanel(Block block) {
		super(block);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();

		if(!block.isReplaceable(worldIn, pos)) {
			pos = pos.offset(side);
		}

		ItemStack itemstack = player.getHeldItem(hand);

		if(!itemstack.isEmpty() && player.canPlayerEdit(pos, side, itemstack)
				&& worldIn.mayPlace(this.block, pos, false, side, (Entity)null)) {
			int i = this.getMetadata(itemstack.getMetadata());
			state = this.block.getStateForPlacement(worldIn, pos, side, hitX, hitY, hitZ, i, player, hand);
			
			if(placeBlockAt(itemstack, player, worldIn, pos, side, hitX, hitY, hitZ, state)) {
				state = worldIn.getBlockState(pos);
				SoundType soundtype = state.getBlock().getSoundType(state, worldIn, pos, player);
				worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS,
						(soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				itemstack.shrink(1);
			}

			return EnumActionResult.SUCCESS;
		}
		else {
			return EnumActionResult.FAIL;
		}
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ, IBlockState state) {
		EnumFacing facing = state.getValue(BlockLargeSquarePanel.FACING);
		boolean isUpper = state.getValue(BlockLargeSquarePanel.HALF) == EnumBlockHalf.UPPER;
		
	}
	
}
