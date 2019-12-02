package com.raptor.portalblocks.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;

/*public class ItemBlock extends net.minecraft.item.ItemBlock{
	String[] unlocalizedNamesByMeta;
	private String unlocalizedName;
	
	public ItemBlock(Block block){
		this(block, new String[0]);
	}
	
	public ItemBlock(Block block, String... unlocalizedNames){
		super(block);
		this.unlocalizedNamesByMeta = unlocalizedNames;
		this.unlocalizedName = block.getUnlocalizedName();
	}
	
	public net.minecraft.item.Item setUnlocalizedNames(String... unlocalizedNames){
		this.unlocalizedNamesByMeta = unlocalizedNames;
		return this;
	}
	
	public String[] getUnlocalizedNames(){
		return (String[])Arrays.copyOf(unlocalizedNamesByMeta, unlocalizedNamesByMeta.length);
	}
	
	@Override
	public net.minecraft.item.Item setUnlocalizedName(String unlocalizedName){
		this.unlocalizedName = "tile." + unlocalizedName;
		return this;
	}
	
	@Override
	public String getUnlocalizedName(){
		return unlocalizedName;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		int meta = stack.getMetadata();
		
		if(meta == 0) return getUnlocalizedName();
		if(meta - 1 < unlocalizedNamesByMeta.length){
			String key = "tile." + unlocalizedNamesByMeta[meta - 1];
			if(I18n.canTranslate(key)) return I18n.translateToLocal(key);
		}
		return getUnlocalizedName();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn,
			List<String> tooltip, ITooltipFlag flagIn){
		int meta = stack.getMetadata();
		
		String key;
		
		if(meta > 0 && meta - 1 < unlocalizedNamesByMeta.length)
			key = "tile." + unlocalizedNamesByMeta[meta - 1] + ".desc";
		else
			key = getUnlocalizedName() + ".desc";
		
		if(I18n.canTranslate(key) || meta != 0 && I18n
				.canTranslate(key = getUnlocalizedName() + ".desc")){
			String str = I18n.translateToLocal(key);
			int i = str.indexOf("\\u");
			while(i >= 0){
				if(i + 5 <= str.length()){
					str = str.substring(0,
							i) + (char)Integer.parseInt(str.substring(i + 2,
									i + 5)) + (i + 5 < str.length()? str
											.substring(i + 5) : "");
				}
				i = str.indexOf("\\u");
			}
			
			for(String msg : str.split("\\\\n")){
				tooltip.add(msg);
			}
		}
		
		this.block.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
*/