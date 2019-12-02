package com.raptor.portalblocks.blocks;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.common.collect.Maps;
import com.raptor.portalblocks.tileentities.TileEntityDynamicPanel;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Deprecated
public class BlockDynamicPanel extends BlockPanel {
	
	public static final PropertyEnum<EnumPanelFace> NORTH = PropertyEnum.create("north", EnumPanelFace.class),
													SOUTH = PropertyEnum.create("south", EnumPanelFace.class),
													EAST  = PropertyEnum.create("east", EnumPanelFace.class),
													WEST  = PropertyEnum.create("west", EnumPanelFace.class),
													UP    = PropertyEnum.create("up", EnumPanelFace.class),
													DOWN  = PropertyEnum.create("down", EnumPanelFace.class);

	public BlockDynamicPanel() {
		super(true);
		setDefaultState(getDefaultState().withProperty(NORTH, EnumPanelFace.NONE)
										 .withProperty(SOUTH, EnumPanelFace.NONE)
										 .withProperty(EAST, EnumPanelFace.NONE)
										 .withProperty(WEST, EnumPanelFace.NONE)
										 .withProperty(UP, EnumPanelFace.NONE)
										 .withProperty(DOWN, EnumPanelFace.NONE));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, NORTH, SOUTH, EAST, WEST, UP, DOWN);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return ((TileEntityDynamicPanel)worldIn.getTileEntity(pos)).getBlockState();
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntityDynamicPanel createTileEntity(World world, IBlockState state) {
		return new TileEntityDynamicPanel(this);
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return ((TileEntityDynamicPanel)world.getTileEntity(pos)).isPortalable(side);
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return ((TileEntityDynamicPanel)world.getTileEntity(pos)).isLightPanel()? 15 : 0;
	}
	
	@Override
	public EnumPanelFace getFaceType(EnumFacing face, IBlockState state, IBlockAccess world, BlockPos pos) {
		return ((TileEntityDynamicPanel)world.getTileEntity(pos)).getFaceType(face);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		super.addInformation(stack, player, tooltip, advanced);
		switch(stack.getMetadata()) {
		case 0:
			tooltip.add(TextFormatting.GRAY + I18n.format(getUnlocalizedName() + ".desc.white"));
			break;
		case 1:
			tooltip.add(TextFormatting.GRAY + I18n.format(getUnlocalizedName() + ".desc.black"));
			break;
		}
	}
	
	//@Override
	//public void replaceWithDynamicPanel(BlockDynamicPanel dynamicPanel, IBlockState state, World world, BlockPos pos) {}

}
