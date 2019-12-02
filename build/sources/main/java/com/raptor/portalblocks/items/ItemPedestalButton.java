package com.raptor.portalblocks.items;

import com.raptor.portalblocks.blocks.BlockPedestalButton;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemPedestalButton extends ItemBlock {
	private String unlocalizedName;
	
	public ItemPedestalButton(BlockPedestalButton.Bottom block) {
		super(block);
	}
	
	public Item setUnlocaizedName(String str) {
		this.unlocalizedName = str;
		return this;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "block." + unlocalizedName;
	}
	
	@Override
	public String getUnlocalizedName() {
		return "block." + unlocalizedName;
	}

}
