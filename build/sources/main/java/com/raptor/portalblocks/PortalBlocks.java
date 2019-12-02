package com.raptor.portalblocks;

import com.raptor.portalblocks.blocks.BlockDoublePanel;
import com.raptor.portalblocks.blocks.BlockEmptyFrame;
import com.raptor.portalblocks.blocks.BlockFloorButton;
import com.raptor.portalblocks.blocks.BlockFrame;
import com.raptor.portalblocks.blocks.BlockLargePanelCorner;
import com.raptor.portalblocks.blocks.BlockLightPanel;
import com.raptor.portalblocks.blocks.BlockPedestalButton;
import com.raptor.portalblocks.blocks.BlockPortalDoor;
import com.raptor.portalblocks.blocks.BlockSquarePanel;

import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(PortalBlocksMod.MODID)
public class PortalBlocks {
	/* Misc */
	public static final Block pedestalbutton_top = null;
	public static final Block pedestalbutton_bottom = null;
	public static final Block floorbutton = null;
	public static final Block portal_door = null;
	//public static final Block indicator_wire = null;
	
	/* White panels */
	public static final Block white_square_panel = null;
	public static final Block white_tall_panel = null;
	public static final Block white_large_panel_corner = null;
	public static final Block stained_white_tall_panel = null;
	
	/* Black panels */
	public static final Block black_square_panel = null;
	public static final Block black_tall_panel = null;
	public static final Block black_large_panel_corner = null;

	/* Broken Panels */
	public static final Block yellow_frame = null;
	public static final Block black_frame = null;
	public static final Block red_frame = null;
	public static final Block empty_frame = null;

	/* Lights */
	public static final Block white_light_panel_left = null;
	public static final Block white_light_panel_right = null;
	public static final Block black_light_panel_left = null;
	public static final Block black_light_panel_right = null;
	public static final Block light_panel_both = null;
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		String str0;
		event.getRegistry().registerAll(new Block[] {
				/* Misc */
				createBlock("pedestalbutton_top", new BlockPedestalButton.Top() { @Override public BlockPedestalButton.Bottom getBottomBlock() { return (BlockPedestalButton.Bottom)PortalBlocks.pedestalbutton_bottom; } }).setUnlocalizedName(str0 = PortalBlocksMod.MODID + '.' + "pedestalbutton"),
				createBlock("pedestalbutton_bottom", new BlockPedestalButton.Bottom() { @Override public BlockPedestalButton.Top getTopBlock() { return (BlockPedestalButton.Top)PortalBlocks.pedestalbutton_top; } }).setUnlocalizedName(str0),
				createBlock("floorbutton", new BlockFloorButton()),
				createBlock("portal_door", new BlockPortalDoor()),
				/* White panels */
				createBlock("white_square_panel", new BlockSquarePanel(true)),
				createBlock("white_tall_panel", new BlockDoublePanel(true)),
				createBlock("white_large_panel_corner", new BlockLargePanelCorner(true)),
				createBlock("stained_white_tall_panel", new BlockDoublePanel(true)),
				/* Black panels */
				createBlock("black_square_panel", new BlockSquarePanel(false)),
				createBlock("black_tall_panel", new BlockDoublePanel(false)),
				createBlock("black_large_panel_corner", new BlockLargePanelCorner(false)),
				/* Broken panels */
				createBlock("yellow_frame", new BlockFrame()),
				createBlock("black_frame", new BlockFrame()),
				createBlock("red_frame", new BlockFrame()),
				createBlock("empty_frame", new BlockEmptyFrame()),
				/* Lights */
				createBlock("white_light_panel_left", new BlockLightPanel()),
				createBlock("white_light_panel_right", new BlockLightPanel()),
				createBlock("black_light_panel_left", new BlockLightPanel()),
				createBlock("black_light_panel_right", new BlockLightPanel()),
				createBlock("light_panel_both", new BlockLightPanel()),
		});
	}

	private static Block createBlock(String name, Block block) {
		return block.setUnlocalizedName(PortalBlocksMod.MODID + '.' + name)
					.setRegistryName(PortalBlocksMod.MODID + ':' + name)
					.setCreativeTab(PortalBlocksMod.PORTAL_TAB);
	}
	
	/*public static void init() {
		register(white_square_panel, "panel_white");
		register(white_grid_panel, "panel_white_grid");
		register(white_tall_panel, "double_panel_white");
		register(stained_white_tall_panel, "double_panel_stained");
		register(black_square_panel, "panel_black");
		register(black_grid_panel, "panel_black_grid");
		register(black_tall_panel, "double_panel_black");
		register(black_large_panel, "quad_panel_black");
		register(yellow_frame, "frame_yellow");
		register(black_frame, "frame_black");
		//register(light_left_top, "light_lt");
		//register(light_left_bottom, "light_lb");
		//register(LIGHT_RIGHT_TOP, "light_rt");
		//register(LIGHT_RIGHT_BOTTOM, "light_rb");
		//register(LIGHT_ALL_TOP, "light_lrt");
		//register(LIGHT_ALL_BOTTOM, "light_lrb");
		register(floor_button, "floorbutton");
		register(door, "portal_door");
		register(button, "pedestalbutton");
		// register(INDICATOR_WIRE, "indicator_wire");
	}*/

	public static AxisAlignedBB createBoundingBox(int x1, int y1, int z1, int x2, int y2, int z2) {
		return new AxisAlignedBB(x1 / 16.0, y1 / 16.0, z1 / 16.0, x2 / 16.0, y2 / 16.0, z2 / 16.0);
	}

}
