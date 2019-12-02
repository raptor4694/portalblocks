package com.raptor.portalblocks.blocks;

import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockSquarePanel extends BlockPanel implements IVariantHolder {

	public static final PropertyEnum<EnumSquarePanelType> VARIANT = PropertyEnum.create("variant", EnumSquarePanelType.class);
	
	public BlockSquarePanel(boolean canPortalOn) {
		super(canPortalOn);
	}
	
	public BlockSquarePanel(Material material, boolean canPortalOn) {
		super(material, canPortalOn);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return super.getItemDropped(state, rand, fortune);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return super.getPickBlock(state, target, world, pos, player);
	}	

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(tab == this.getCreativeTabToDisplayOn()) {
			for(EnumSquarePanelType type : EnumSquarePanelType.VALUES) {
				items.add(new ItemStack(this, 1, type.ordinal()));
			}
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, EnumSquarePanelType.fromIndex(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}
	
	@Override
	public String getVariantName(int meta) {
		EnumSquarePanelType type = EnumSquarePanelType.fromIndex(meta);
		return type == EnumSquarePanelType.NORMAL? "" : type.getName();
	}
	
	@Override
	public int variantCount() {
		return EnumSquarePanelType.VALUES.length;
	}

	public static enum EnumSquarePanelType implements IStringSerializable {
		NORMAL, 
		GRID;
		
		private static final Map<String, EnumSquarePanelType> NAME_LOOKUP = Maps.newHashMap();
		private static final EnumSquarePanelType[] VALUES = values();
		
		private final String name;
		
		private EnumSquarePanelType() {
			this.name = name().toLowerCase(Locale.ROOT);
		}

		@Override
		public String getName() {
			return name;
		}
		
		public static EnumSquarePanelType fromIndex(int index) {
			return VALUES[MathHelper.abs(index) % VALUES.length];
		}
		
		@Nullable
		public static EnumSquarePanelType byName(String name) {
			return name == null? null : NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
		}
		
		static {
			for(EnumSquarePanelType type : VALUES) {
				NAME_LOOKUP.put(type.getName(), type);
			}
		}
		
	}

}
