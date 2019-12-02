package com.raptor.portalblocks.blocks;

import com.raptor.portalblocks.blocks.BlockPanel.EnumPanelFace;

class FaceInfo {
	
	public static FaceInfo faceInfo(EnumPanelFace white, EnumPanelFace black) {
		return new FaceInfo(white, black);
	}
	
	public final EnumPanelFace white, black;
	
	public FaceInfo(EnumPanelFace white, EnumPanelFace black) {
		this.white = white;
		this.black = black;
	}
	
	public EnumPanelFace getFaceType(boolean isPortalable) {
		return isPortalable? white : black;
	}
	
}
