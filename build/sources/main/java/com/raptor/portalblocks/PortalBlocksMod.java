package com.raptor.portalblocks;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = PortalBlocksMod.MODID, version = PortalBlocksMod.VERSION)
public class PortalBlocksMod {
	public static final String MODID = "portalblocks";
	public static final String VERSION = "2.1";

	public static final CreativeTabs PORTAL_TAB = new CreativeTabs("portalblocks") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(PortalItems.pedestalbutton);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void displayAllRelevantItems(NonNullList<ItemStack> list) {
			for(Field field : PortalItems.class.getDeclaredFields()) {
				if(Item.class.isAssignableFrom(field.getType()) && Modifier.isStatic(field.getModifiers())) {
					Item item;
					try {
						item = (Item)field.get(null);
					}
					catch(IllegalAccessException e) {
						throw new Error(e);
					}
					if(item != null)
						item.getSubItems(this, list);
				}
			}
		}

	};

	@EventHandler
	public static void preinit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(PortalBlocks.class);
		MinecraftForge.EVENT_BUS.register(PortalItems.class);
		MinecraftForge.EVENT_BUS.register(PortalSounds.class);
	}

	@EventHandler
	public static void init(FMLInitializationEvent event) {

	}
	
	public static ResourceLocation getResource(String name) {
		return new ResourceLocation(MODID, name);
	}

}
