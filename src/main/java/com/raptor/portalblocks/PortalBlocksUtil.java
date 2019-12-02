package com.raptor.portalblocks;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import net.minecraft.util.EnumFacing;

public final class PortalBlocksUtil {

	public static final Set<EnumFacing> AXIS_Y = Collections.unmodifiableSet(EnumSet.of(EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST));
	public static final Set<EnumFacing> AXIS_X = Collections.unmodifiableSet(EnumSet.of(EnumFacing.NORTH, EnumFacing.UP, EnumFacing.SOUTH, EnumFacing.DOWN));
	public static final Set<EnumFacing> AXIS_Z = Collections.unmodifiableSet(EnumSet.of(EnumFacing.WEST, EnumFacing.UP, EnumFacing.EAST, EnumFacing.DOWN));
	
	private PortalBlocksUtil() {}
	
}
