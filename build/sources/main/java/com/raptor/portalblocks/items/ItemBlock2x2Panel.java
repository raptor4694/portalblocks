package com.raptor.portalblocks.items;

import static com.raptor.portalblocks.blocks.BlockLargePanelCorner.TYPE;

import com.raptor.portalblocks.blocks.BlockLargePanelCorner;
import com.raptor.portalblocks.blocks.BlockLargePanelCorner.EnumLargePanelCorner;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ItemBlock2x2Panel extends ItemBlock {
	protected final BlockLargePanelCorner panelCorner;

	public ItemBlock2x2Panel(Block panelCorner) {
		super(panelCorner);
		this.panelCorner = (BlockLargePanelCorner)panelCorner;
	}

	@Override
	public EnumActionResult onItemUse(final EntityPlayer player, final World world, BlockPos pos, final EnumHand hand,
			final EnumFacing face, final float hitX, final float hitY, final float hitZ) {
		IBlockState iblockstate = world.getBlockState(pos);
		Block block = iblockstate.getBlock();

		if(!block.isReplaceable(world, pos))
			pos = pos.offset(face);

		ItemStack itemstack = player.getHeldItem(hand);
		if(itemstack.isEmpty())
			return EnumActionResult.FAIL;

		EnumLargePanelCorner type;
		BlockPos bottomNorthWestCorner;

		BlockPos pos2 = pos.offset(face.getOpposite());
		IBlockState state = world.getBlockState(pos2);
		if(!player.isSneaking() && state.getBlock() instanceof BlockLargePanelCorner) {
			type = state.getValue(TYPE).flipAlongAxis(face.getAxis());
		}
		else {
			switch(face) {
			case UP:
			case DOWN:
				if(hitX < 0.5f) {
					if(hitZ < 0.5f)
						type = EnumLargePanelCorner.TOP_SOUTH_EAST;
					else
						type = EnumLargePanelCorner.TOP_NORTH_EAST;
				}
				else {
					if(hitZ < 0.5f)
						type = EnumLargePanelCorner.TOP_SOUTH_WEST;
					else
						type = EnumLargePanelCorner.TOP_NORTH_WEST;
				}

				if(face == EnumFacing.DOWN)
					type = type.flipAlongAxis(EnumFacing.Axis.Y);

				EnumFacing facing = player.getHorizontalFacing();
				while(facing != EnumFacing.SOUTH) {
					facing = facing.rotateY();
					type = type.rotateCW();
				}

				break;
			case NORTH:
				if(hitY < 0.5f) {
					if(hitX < 0.5f)
						type = EnumLargePanelCorner.BOTTOM_NORTH_WEST;
					else
						type = EnumLargePanelCorner.BOTTOM_NORTH_EAST;
				}
				else {
					if(hitX < 0.5f)
						type = EnumLargePanelCorner.TOP_NORTH_WEST;
					else
						type = EnumLargePanelCorner.TOP_NORTH_EAST;
				}
				break;
			case SOUTH:
				if(hitY < 0.5f) {
					if(hitX < 0.5f)
						type = EnumLargePanelCorner.BOTTOM_SOUTH_WEST;
					else
						type = EnumLargePanelCorner.BOTTOM_SOUTH_EAST;
				}
				else {
					if(hitX < 0.5f)
						type = EnumLargePanelCorner.TOP_SOUTH_WEST;
					else
						type = EnumLargePanelCorner.TOP_SOUTH_EAST;
				}
				break;
			case EAST:
				if(hitY < 0.5f) {
					if(hitZ < 0.5f)
						type = EnumLargePanelCorner.BOTTOM_NORTH_EAST;
					else
						type = EnumLargePanelCorner.BOTTOM_SOUTH_EAST;
				}
				else {
					if(hitZ < 0.5f)
						type = EnumLargePanelCorner.TOP_NORTH_EAST;
					else
						type = EnumLargePanelCorner.TOP_SOUTH_EAST;
				}
				break;
			case WEST:
				if(hitY < 0.5f) {
					if(hitZ < 0.5f)
						type = EnumLargePanelCorner.BOTTOM_NORTH_WEST;
					else
						type = EnumLargePanelCorner.BOTTOM_SOUTH_WEST;
				}
				else {
					if(hitZ < 0.5f)
						type = EnumLargePanelCorner.TOP_NORTH_WEST;
					else
						type = EnumLargePanelCorner.TOP_SOUTH_WEST;
				}
				break;
			default:
				throw new AssertionError();
			}
		}

		bottomNorthWestCorner = pos.add(type.getOffsetToBottomNorthWest());
		if(!has2x2ReplaceableCube(world, player, face, itemstack, bottomNorthWestCorner)) {
			boolean flag = false;
			for(int i = 0; i < 3 && !flag; i++) {
				type = type.rotateAround(face);
				bottomNorthWestCorner = pos.add(type.getOffsetToBottomNorthWest());
				flag = has2x2ReplaceableCube(world, player, face, itemstack, bottomNorthWestCorner);
			}
			if(!flag)
				return EnumActionResult.FAIL;
		}

		BlockPos[] positions = new BlockPos[8];
		IBlockState[] states = new IBlockState[8], oldstates = new IBlockState[8];
		boolean placed = false;
		int i = 0;
		for(EnumLargePanelCorner theType : EnumLargePanelCorner.values()) {
			positions[i] = pos2 = bottomNorthWestCorner.add(theType.getOffsetFromBottomNorthWest());
			states[i] = state = this.panelCorner.getDefaultState().withProperty(TYPE, theType);
			oldstates[i] = world.getBlockState(pos2);
			placed |= world.setBlockState(pos2, state, 8|2);
			i++;
		}
		
		if(placed) {
			SoundType soundtype = this.panelCorner.getSoundType(state, world, pos2, player);
            world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            itemstack.shrink(1);
		}
		else return EnumActionResult.FAIL;
		
		// send the block updates
		for(i = 0; i < 8; i++) {
			pos2 = positions[i];
			world.markAndNotifyBlock(pos2, world.getChunkFromBlockCoords(pos2), oldstates[i], states[i], 1);
		}
		
		return EnumActionResult.SUCCESS;
	}

	private static final Vec3i[] HORIZ_FACES_OFFSETS = {
			Vec3i.NULL_VECTOR, 
			EnumFacing.EAST.getDirectionVec(), 
			EnumFacing.SOUTH.getDirectionVec(), 
			EnumFacing.WEST.getDirectionVec()
		};

	/**
	 * @param world
	 * @param pos
	 *            the bottom northwest corner pos
	 * @return
	 */
	protected boolean has2x2ReplaceableCube(IBlockAccess worldIn, EntityPlayer player, EnumFacing face,
			ItemStack itemstack, BlockPos pos) {
		for(Vec3i offset : HORIZ_FACES_OFFSETS) {
			BlockPos pos2 = pos.add(offset);
			IBlockState state = worldIn.getBlockState(pos2);
			if(!state.getBlock().isReplaceable(worldIn, pos2) || !player.canPlayerEdit(pos2, face, itemstack))
				return false;
			state = worldIn.getBlockState(pos2 = pos2.up());
			if(!state.getBlock().isReplaceable(worldIn, pos2) || !player.canPlayerEdit(pos2, face, itemstack))
				return false;
		}
		return true;
	}

	/*public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ, IBlockState newState) {
		if(!world.setBlockState(pos, newState, 11))
			return false;
		world.updateObservingBlocksAt(pos, blockType);

		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() == this.panelCorner) {
			ItemBlock.setTileEntityNBT(world, player, pos, stack);
			this.panelCorner.onBlockPlacedBy(world, pos, state, player, stack);

			if(player instanceof EntityPlayerMP)
				CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
		}

		return true;
	}*/

}
