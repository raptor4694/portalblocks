package com.raptor.portalblocks;

import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.raptor.portalblocks.blocks.BlockPedestalButton;
import com.raptor.portalblocks.blocks.IVariantHolder;
import com.raptor.portalblocks.items.ItemBlock2x2Panel;
import com.raptor.portalblocks.items.ItemBlockWithVariants;
import com.raptor.portalblocks.items.ItemPedestalButton;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(PortalBlocksMod.MODID)
public class PortalItems{
	/* Misc */
	public static final Item pedestalbutton = null;
	public static final Item floorbutton = null;
	public static final Item portal_door = null;
	//public static final Item indicator_wire = null;
	
	static List<Item> items = Lists.newArrayList();
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(new Item[] {
				createItem("pedestalbutton", new ItemPedestalButton((BlockPedestalButton.Bottom)PortalBlocks.pedestalbutton_bottom)),
				createItem(PortalBlocks.floorbutton),
				createItem(PortalBlocks.portal_door, ItemDoor::new),
				createItem(PortalBlocks.white_square_panel, ItemBlockWithVariants::new),
				createItem(PortalBlocks.white_tall_panel),
				createItem(PortalBlocks.white_large_panel_corner, ItemBlock2x2Panel::new),
				createItem(PortalBlocks.stained_white_tall_panel),
				createItem(PortalBlocks.black_square_panel, ItemBlockWithVariants::new),
				createItem(PortalBlocks.black_tall_panel),
				createItem(PortalBlocks.black_large_panel_corner, ItemBlock2x2Panel::new),
				createItem(PortalBlocks.yellow_frame),
				createItem(PortalBlocks.black_frame),
				createItem(PortalBlocks.red_frame),
				createItem(PortalBlocks.empty_frame),
				createItem(PortalBlocks.white_light_panel_left),
				createItem(PortalBlocks.white_light_panel_right),
				createItem(PortalBlocks.black_light_panel_left),
				createItem(PortalBlocks.black_light_panel_right),
				createItem(PortalBlocks.light_panel_both),
		});
	}
	
	private static Item createItem(Block block) {
		return createItem(block.getRegistryName().getResourcePath(), new ItemBlock(block));
	}
	
	private static Item createItem(Block block, Function<? super Block, ? extends Item> itemCreator) {
		return createItem(block.getRegistryName().getResourcePath(), itemCreator.apply(block));
	}
	
	private static Item createItem(String name, Item item) {
		return createItem(name, item, true);
	}
	
	private static Item createItem(String name, Item item, boolean addToItemsList) {
		item.setUnlocalizedName(PortalBlocksMod.MODID + '.' + name)
			.setRegistryName(PortalBlocksMod.MODID + ':' + name)
			.setCreativeTab(PortalBlocksMod.PORTAL_TAB);
		
		if(item instanceof IVariantHolder) {
			IVariantHolder ivariantholder = (IVariantHolder)item;
			for(int i = 0; i < ivariantholder.variantCount(); i++) {
				String variantName = ivariantholder.getVariantName(i);
				String id = StringUtils.isEmpty(variantName)
								? PortalBlocksMod.MODID + ':' + name
								: PortalBlocksMod.MODID + ':' + name + '_' + variantName;
				ModelLoader.setCustomModelResourceLocation(item, 0,
						new ModelResourceLocation(id, "inventory"));
			}
		}
		else if(item.getHasSubtypes()) {
			NonNullList<ItemStack> items = NonNullList.create();
			for(CreativeTabs tab : item.getCreativeTabs()) {
				items.clear();
				item.getSubItems(tab, items);
				for(ItemStack stack : items) {
					ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(),
							new ModelResourceLocation(PortalBlocksMod.MODID + ':' + name, "inventory"));
				}
			}
		}
		else {
			ModelLoader.setCustomModelResourceLocation(item, 0,
					new ModelResourceLocation(PortalBlocksMod.MODID + ':' + name, "inventory"));
		}
		
		if(addToItemsList)
			items.add(item);
		else
			item.setCreativeTab(null);
		
		return item;
	}
	
	/*
	
	public static void register(Item item, String name, String... variantNames){
		item.setUnlocalizedName(PortalBlocksMod.MODID + '.' + name)
			.setRegistryName(PortalBlocksMod.MODID + ':' + name);
		
		if(item instanceof ItemBlock){
			ItemBlock block = (ItemBlock)item;
			String[] names = new String[variantNames.length];
			for(int i = 0; i < names.length; ++i)
				names[i] = PortalBlocksMod.MODID + '.' + variantNames[i];
			block.setUnlocalizedNames(names);
		}
		
		ForgeRegistries.ITEMS.register(item);
		
		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(PortalBlocksMod.MODID + ':' + name,
						"inventory"));
		
		if(variantNames.length > 0){
			item.setHasSubtypes(true);
			for(int i = 0; i < variantNames.length; ++i){
				ModelLoader.setCustomModelResourceLocation(item, i + 1,
						new ModelResourceLocation(
								PortalBlocksMod.MODID + ':' + variantNames[i]));
			}
		}
	}
	*/
}
