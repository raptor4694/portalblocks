package com.raptor.portalblocks.items;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

@Deprecated
public class MultiItemBlock extends ItemBlock{
	private final String[] unlocalizedNames;
	
	public MultiItemBlock(Block block, String... unlocalizedNames) {
		super(block);
		this.unlocalizedNames = unlocalizedNames;
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		int meta = stack.getMetadata();
		if(meta == 0) return this.block.getUnlocalizedName();
		if(meta-1 < unlocalizedNames.length /*&& I18n.canTranslate(unlocalizedNames[meta-1])*/) return unlocalizedNames[meta-1];
		return this.block.getUnlocalizedName();
	}
	
	@Override
	public int getMetadata(int damage){
		return damage;
	}

}
