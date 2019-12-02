package com.raptor.portalblocks.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/*
public class Item extends net.minecraft.item.Item{
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
		String unlocalizedTooltip = getUnlocalizedName() + ".desc";
		if(I18n.canTranslate(unlocalizedTooltip)){
			String str = I18n.translateToLocal(unlocalizedTooltip);
			int i = str.indexOf("\\u");
			while(i >= 0){
				if(i + 5 <= str.length()){
					str = str.substring(0, i) + (char)Integer.parseInt(str.substring(i+2, i+5)) + (i + 5 < str.length()? str.substring(i+5) : "");
				}
				i = str.indexOf("\\u");
			}
			
			for(String msg : str.split("\\\\n")){
				tooltip.add(msg);
			}
		}
	}
}
*/