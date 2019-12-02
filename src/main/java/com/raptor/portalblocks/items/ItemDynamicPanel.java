package com.raptor.portalblocks.items;

import com.raptor.portalblocks.blocks.BlockDynamicPanel;
import com.raptor.portalblocks.tileentities.TileEntityDynamicPanel;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Deprecated
public abstract class ItemDynamicPanel extends Item {
	protected final BlockDynamicPanel panel;
	protected final boolean portalable;
	
	public ItemDynamicPanel(Block block, boolean isPortalable) {
		this.panel = (BlockDynamicPanel)block;
		this.portalable = isPortalable;
	}
	
	public final boolean isPortalable() {
		return portalable;
	}
	
	@Override
	public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, final BlockPos pos,
			final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		IBlockState state = worldIn.getBlockState(pos);
		
		if(state.getBlock() != this.panel)
			return EnumActionResult.FAIL;
		
		state = this.panel.getActualState(state, worldIn, pos);
		
		return modifyPanels(player, worldIn, (TileEntityDynamicPanel)worldIn.getTileEntity(pos), state, pos, hand, facing, hitX, hitY, hitZ);
	}

	protected abstract EnumActionResult modifyPanels(final EntityPlayer player, final World worldIn, final TileEntityDynamicPanel te, IBlockState state,
			final BlockPos pos, final EnumHand hand, final EnumFacing face, final float hitX, final float hitY, final float hitZ);
	
}

/*
public class ItemDynamicPanel extends ItemBlock {
	
	public ItemDynamicPanel(Block block) {
		super(block);
		BlockDynamicPanel.class.cast(block);
		setHasSubtypes(true);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(this.isInCreativeTab(tab)) {
			for(int i = 0; i < 2; i++) {
				items.add(new ItemStack(this, 1, i));
			}
		}
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	*//**
	 * Called to actually place the block, after the location is determined
	 * and all permission checks have been made.
	 *
	 * @param stack
	 *            The item stack that was used to place the block. This can be
	 *            changed inside the method.
	 * @param player
	 *            The player who is placing the block. Can be null if the block
	 *            is not being placed by a player.
	 * @param side
	 *            The side the player (or machine) right-clicked on.
	 *//*
	@SuppressWarnings("deprecation")
	@Override
	public boolean placeBlockAt(final ItemStack stack, final EntityPlayer player, final World world, final BlockPos pos,
			final EnumFacing side, final float hitX, final float hitY, final float hitZ, final IBlockState newState) {		
		if(super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState) && !stack.getTagCompound().hasKey("BlockEntityTag", 10)) {
			boolean isPortalableDefault = (stack.getMetadata() == 0);
			TileEntityDynamicPanel te = (TileEntityDynamicPanel)world.getTileEntity(pos);
			
			for(final EnumFacing face : EnumFacing.Plane.HORIZONTAL) {
				boolean setFace = false;
				
				BlockPos    posRight   = pos.offset(face.rotateY());
				IBlockState stateRight = world.getBlockState(posRight);
				Block       blockRight = stateRight.getBlock();
				stateRight = blockRight.getActualState(stateRight, world, posRight);
				
				if(blockRight instanceof BlockPanel) {
					EnumPanelFace faceTypeRight = ((BlockPanel)blockRight).getFaceType(face, stateRight, world, posRight);
					if(faceTypeRight != EnumPanelFace.NONE) {
						
					}
				}
				
				BlockPos    posLeft   = pos.offset(face.rotateYCCW());
				IBlockState stateLeft = world.getBlockState(posLeft);
				Block       blockLeft = stateLeft.getBlock();
				stateLeft = blockLeft.getActualState(stateLeft, world, posLeft);
				
				
				if(!setFace)
					te.setFaceType(face, isPortalableDefault? EnumPanelFace.WHITE_SQUARE : EnumPanelFace.BLACK_SQUARE);
			}
			
			return true;
		}
		return false;
	}
	
}
*/