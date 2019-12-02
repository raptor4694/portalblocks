package com.raptor.portalblocks.items;

import org.apache.commons.lang3.StringUtils;

import com.raptor.portalblocks.blocks.IVariantHolder;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockWithVariants extends ItemBlock implements IVariantHolder {
	private final IVariantHolder variantHolderBlock;
	
	public ItemBlockWithVariants(Block block) {
		super(block);
		this.variantHolderBlock = (IVariantHolder)block;
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String variantName = variantHolderBlock.getVariantName(stack.getMetadata());
		return StringUtils.isEmpty(variantName)? block.getUnlocalizedName() : block.getUnlocalizedName() + "." + variantName;
	}
	
	@Override
	public int getMetadata(int damage){
		return damage;
	}

	@Override
	public String getVariantName(int meta) {
		return variantHolderBlock.getVariantName(meta);
	}
	
	@Override
	public int variantCount() {
		return variantHolderBlock.variantCount();
	}

}
