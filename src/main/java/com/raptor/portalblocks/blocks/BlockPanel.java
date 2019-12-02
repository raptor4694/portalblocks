package com.raptor.portalblocks.blocks;

import static com.google.common.collect.Maps.newHashMap;
import static net.minecraft.util.math.MathHelper.abs;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.raptor.portalblocks.PortalBlocksMod;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class BlockPanel extends Block {
	protected final boolean portalable;
	
	public BlockPanel(Material material, boolean canPortalOn) {
		super(material, canPortalOn? MapColor.SNOW : MapColor.GRAY);
		portalable = canPortalOn;
		setHardness(5.0f);
		setResistance(10.0f);
		setSoundType(SoundType.METAL);
		setCreativeTab(PortalBlocksMod.PORTAL_TAB);
	}
	
	public BlockPanel(boolean canPortalOn){
		this(Material.IRON, canPortalOn);
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return portalable;
	}
	
	public abstract EnumPanelFace getFaceType(final EnumFacing face, final IBlockState state, final IBlockAccess world, final BlockPos pos);
	
	/*public void replaceWithDynamicPanel(BlockDynamicPanel dynamicPanel, IBlockState state, World world, BlockPos pos) {
		EnumPanelFace upFace    = getFaceType(EnumFacing.UP, state, world, pos);
		EnumPanelFace downFace  = getFaceType(EnumFacing.DOWN, state, world, pos);
		EnumPanelFace northFace = getFaceType(EnumFacing.NORTH, state, world, pos);
		EnumPanelFace southFace = getFaceType(EnumFacing.SOUTH, state, world, pos);
		EnumPanelFace eastFace  = getFaceType(EnumFacing.EAST, state, world, pos);
		EnumPanelFace westFace  = getFaceType(EnumFacing.WEST, state, world, pos);
		
		world.setBlockState(pos, dynamicPanel.getDefaultState());
		TileEntityDynamicPanel te = (TileEntityDynamicPanel)world.getTileEntity(pos);
		te.setUpFace(upFace);
		te.setDownFace(downFace);
		te.setNorthFace(northFace);
		te.setSouthFace(southFace);
		te.setEastFace(eastFace);
		te.setWestFace(westFace);
	}*/
	
	private static final int L_off = 0, T_off = 1, R_off = 2, B_off = 3;
	private static final int L     = 1<<L_off;
	private static final int T     = 1<<T_off;
	private static final int R     = 1<<R_off;
	private static final int B     = 1<<B_off;
	private static final int ALL   = L|R|T|B;
	private static final int LIGHT_HORIZ = 1<<4;
	private static final int LIGHT_VERT = 2<<4;
	private static final int SPECIAL = 3<<4;
	
	private static final boolean CLEAN = false, DIRTY = true;

	public static enum EnumPanelFace implements IStringSerializable {
		// @formatter:off
		NONE,
		
		WHITE_NONE				(CLEAN, Type.WHITE, Shape.NONE),
		WHITE_EDGE_LEFT			(CLEAN, Type.WHITE, Shape.LEFT),
		WHITE_EDGE_RIGHT		(CLEAN, Type.WHITE, Shape.RIGHT),
		WHITE_EDGE_TOP			(CLEAN, Type.WHITE, Shape.TOP),
		WHITE_EDGE_BOTTOM		(CLEAN, Type.WHITE, Shape.BOTTOM),
		WHITE_SQUARE			(CLEAN, Type.WHITE,	Shape.SQUARE),
		WHITE_GRID				(CLEAN, Type.WHITE,	Shape.GRID),
		WHITE_VERT				(CLEAN, Type.WHITE, Shape.VERT),
		WHITE_HORIZ				(CLEAN, Type.WHITE, Shape.HORIZ),
		WHITE_TALL_TOP			(CLEAN, Type.WHITE, Shape.TALL_TOP),
		WHITE_TALL_LEFT			(CLEAN, Type.WHITE, Shape.TALL_LEFT),
		WHITE_TALL_RIGHT		(CLEAN, Type.WHITE, Shape.TALL_RIGHT),
		WHITE_TALL_BOTTOM		(CLEAN, Type.WHITE, Shape.TALL_BOTTOM),
		WHITE_LARGE_LT			(CLEAN, Type.WHITE, Shape.TOP_LEFT),
		WHITE_LARGE_RT			(CLEAN, Type.WHITE, Shape.TOP_RIGHT),
		WHITE_LARGE_LB			(CLEAN, Type.WHITE, Shape.BOTTOM_LEFT),
		WHITE_LARGE_RB			(CLEAN, Type.WHITE, Shape.BOTTOM_RIGHT),
		WHITE_LIGHT_LT			(CLEAN, Type.WHITE, Shape.LIGHT_VERT_LEFT_TOP),
		WHITE_LIGHT_LB			(CLEAN, Type.WHITE, Shape.LIGHT_VERT_LEFT_BOTTOM),
		WHITE_LIGHT_RT			(CLEAN, Type.WHITE, Shape.LIGHT_VERT_RIGHT_TOP),
		WHITE_LIGHT_RB			(CLEAN, Type.WHITE, Shape.LIGHT_VERT_RIGHT_BOTTOM),
		WHITE_LIGHT_TL			(CLEAN, Type.WHITE, Shape.LIGHT_HORIZ_TOP_LEFT),
		WHITE_LIGHT_TR			(CLEAN, Type.WHITE, Shape.LIGHT_HORIZ_TOP_RIGHT),
		WHITE_LIGHT_BL			(CLEAN, Type.WHITE, Shape.LIGHT_HORIZ_BOTTOM_LEFT),
		WHITE_LIGHT_BR			(CLEAN, Type.WHITE, Shape.LIGHT_HORIZ_BOTTOM_RIGHT),
		
		STAINED_WHITE_TALL_1_TOP		(DIRTY, Type.WHITE, Shape.TALL_TOP),
		STAINED_WHITE_TALL_1_LEFT		(DIRTY, Type.WHITE, Shape.TALL_LEFT),
		STAINED_WHITE_TALL_1_RIGHT		(DIRTY, Type.WHITE, Shape.TALL_RIGHT),
		STAINED_WHITE_TALL_1_BOTTOM		(DIRTY, Type.WHITE, Shape.TALL_BOTTOM),
		
		BLACK_NONE				(CLEAN, Type.BLACK, Shape.NONE),
		BLACK_EDGE_LEFT			(CLEAN, Type.BLACK, Shape.LEFT),
		BLACK_EDGE_RIGHT		(CLEAN, Type.BLACK, Shape.RIGHT),
		BLACK_EDGE_TOP			(CLEAN, Type.BLACK, Shape.TOP),
		BLACK_EDGE_BOTTOM		(CLEAN, Type.BLACK, Shape.BOTTOM),
		BLACK_SQUARE			(CLEAN, Type.BLACK, Shape.SQUARE),
		BLACK_GRID				(CLEAN, Type.BLACK, Shape.GRID),
		BLACK_VERT				(CLEAN, Type.BLACK, Shape.VERT),
		BLACK_HORIZ				(CLEAN, Type.BLACK, Shape.HORIZ),
		BLACK_TALL_TOP			(CLEAN, Type.BLACK, Shape.TALL_TOP),
		BLACK_TALL_LEFT			(CLEAN, Type.BLACK, Shape.TALL_LEFT),
		BLACK_TALL_RIGHT		(CLEAN, Type.BLACK, Shape.TALL_RIGHT),
		BLACK_TALL_BOTTOM		(CLEAN, Type.BLACK, Shape.TALL_BOTTOM),
		BLACK_LARGE_LT			(CLEAN, Type.BLACK, Shape.TOP_LEFT),
		BLACK_LARGE_RT			(CLEAN, Type.BLACK, Shape.TOP_RIGHT),
		BLACK_LARGE_LB			(CLEAN, Type.BLACK, Shape.BOTTOM_LEFT),
		BLACK_LARGE_RB			(CLEAN, Type.BLACK, Shape.BOTTOM_RIGHT),
		BLACK_LIGHT_LT			(CLEAN, Type.BLACK, Shape.LIGHT_VERT_LEFT_TOP),
		BLACK_LIGHT_LB			(CLEAN, Type.BLACK, Shape.LIGHT_VERT_LEFT_BOTTOM),
		BLACK_LIGHT_RT			(CLEAN, Type.BLACK, Shape.LIGHT_VERT_RIGHT_TOP),
		BLACK_LIGHT_RB			(CLEAN, Type.BLACK, Shape.LIGHT_VERT_RIGHT_BOTTOM),
		BLACK_LIGHT_TL			(CLEAN, Type.BLACK, Shape.LIGHT_HORIZ_TOP_LEFT),
		BLACK_LIGHT_TR			(CLEAN, Type.BLACK, Shape.LIGHT_HORIZ_TOP_RIGHT),
		BLACK_LIGHT_BL			(CLEAN, Type.BLACK, Shape.LIGHT_HORIZ_BOTTOM_LEFT),
		BLACK_LIGHT_BR			(CLEAN, Type.BLACK, Shape.LIGHT_HORIZ_BOTTOM_RIGHT),
		
		LIGHT_BOTH_TOP			(CLEAN, Type.OTHER, Shape.LIGHT_VERT_BOTH_TOP),
		LIGHT_BOTH_BOTTOM		(CLEAN, Type.OTHER, Shape.LIGHT_VERT_BOTH_BOTTOM),
		LIGHT_BOTH_LEFT			(CLEAN, Type.OTHER, Shape.LIGHT_HORIZ_BOTH_LEFT),
		LIGHT_BOTH_RIGHT		(CLEAN, Type.OTHER, Shape.LIGHT_HORIZ_BOTH_RIGHT),
		;
		
		// @formatter:off
		
		private static final Map<String, EnumPanelFace> NAME_LOOKUP = newHashMap();
		private static final EnumPanelFace[] VALUES = values();
		
		public final boolean isPortalable, isLight, isDirty;
		public final EnumPanelFace.Type type;
		public final EnumPanelFace.Shape shape;
		private final String name;
		private EnumPanelFace mirrorHoriz, mirrorVert; 
		private EnumPanelFace rotateCW, rotateCCW;
		private EnumPanelFace blackVariant, whiteVariant;
		
		private EnumPanelFace() {
			this.name = "none";
			this.isPortalable = false;
			this.isLight = false;
			this.isDirty = false;
			this.type = Type.OTHER;
			this.shape = EnumPanelFace.Shape.NONE;
		}
		
		private EnumPanelFace(boolean isDirty, EnumPanelFace.Type type, EnumPanelFace.Shape shape) {
			this.name = name().toLowerCase(Locale.ROOT);
			this.isPortalable = type.isPortalable;
			this.isLight = shape.isLight;
			this.isDirty = isDirty;
			this.type = type;
			this.shape = shape;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		@Nullable
		public EnumPanelFace getVariant(boolean portalable) {
			return portalable? whiteVariant : blackVariant;
		}
		
		public EnumPanelFace getBlackVariant() {
			return blackVariant;
		}
		
		public EnumPanelFace getWhiteVariant() {
			return whiteVariant;
		}
		
		public EnumPanelFace rotateCW() {
			return rotateCW;
		}
		
		public EnumPanelFace rotateCCW() {
			return rotateCCW;
		}
		
		public EnumPanelFace mirrorAlong(EnumFacing.Plane plane) {
			switch(plane) {
			case HORIZONTAL: return mirrorHoriz;
			case VERTICAL:   return mirrorVert;
			default:         throw new AssertionError();
			}
		}
		
		public EnumPanelFace mirrorHorizontal() {
			return mirrorHoriz;
		}
		
		public EnumPanelFace mirrorVertical() {
			return mirrorVert;
		}
		
		public static EnumPanelFace byName(String name) {
			return name == null? null : NAME_LOOKUP.get(name);
		}
		
		public static EnumPanelFace fromIndex(int index) {
			return VALUES[abs(index) % VALUES.length];
		}
		
		static {
			for(EnumPanelFace face : VALUES) {
				NAME_LOOKUP.put(face.name, face);
				switch(face.type) {
				case WHITE:
					face.shape.whiteFace = face;
					break;
				case BLACK:
					face.shape.blackFace = face;
					break;
				default:
				}
			}
			for(EnumPanelFace face : VALUES) {
				switch(face.type) {
				case WHITE:
					face.rotateCW = face.shape.rotateCW.whiteFace;
					face.rotateCCW = face.shape.rotateCCW.whiteFace;
					face.mirrorHoriz = face.shape.mirrorHoriz.whiteFace;
					face.mirrorVert = face.shape.mirrorVert.whiteFace;
					break;
				case BLACK:
					face.rotateCW = face.shape.rotateCW.blackFace;
					face.rotateCCW = face.shape.rotateCCW.blackFace;
					face.mirrorHoriz = face.shape.mirrorHoriz.blackFace;
					face.mirrorVert = face.shape.mirrorVert.blackFace;
					break;
				default:
				}
			}
		}
		
		public static enum Type implements IStringSerializable {
			OTHER(false),
			WHITE(true),
			BLACK(false);
			
			public final boolean isPortalable;
			private final String name;
			
			private Type(boolean isPortalable) {
				this.name = name().toLowerCase(Locale.ROOT);
				this.isPortalable = isPortalable;
			}
			
			@Override
			public String getName() {
				return name;
			}
			
			public EnumPanelFace.Type getOpposite() {
				switch(this) {
				case WHITE: return BLACK;
				case BLACK: return WHITE;
				default:    return this;
				}
			}
			
			public static EnumPanelFace.Type byName(String name) {
				if(name == null) return null;
				switch(name) {
				case "white": return WHITE;
				case "black": return BLACK;
				case "other": return OTHER;
				default:      return null;
				}
			}
			
			public static EnumPanelFace.Type fromIndex(int index) {
				switch(abs(index) % 3) {
				case 0: return OTHER;
				case 1: return WHITE;
				case 2: return BLACK;
				default: throw new AssertionError();
				}
			}
			
		}
		
		public static enum Shape implements IStringSerializable {
			NONE(0),
			LEFT(L),
			RIGHT(R),
			TOP(T),
			BOTTOM(B),
			TOP_LEFT(L|T),
			TOP_RIGHT(R|T),
			BOTTOM_LEFT(L|B),
			BOTTOM_RIGHT(R|B),
			VERT(L|R),
			HORIZ(T|B),
			TALL_TOP(T|L|R),
			TALL_BOTTOM(B|L|R),
			TALL_LEFT(L|T|B),
			TALL_RIGHT(R|T|B),
			SQUARE(ALL),
			GRID(ALL|SPECIAL),
			LIGHT_VERT_LEFT_TOP(L|T|LIGHT_VERT),
			LIGHT_VERT_LEFT_BOTTOM(L|B|LIGHT_VERT),
			LIGHT_VERT_RIGHT_TOP(R|T|LIGHT_VERT),
			LIGHT_VERT_RIGHT_BOTTOM(R|B|LIGHT_VERT),
			LIGHT_VERT_BOTH_TOP(L|R|T|LIGHT_VERT),
			LIGHT_VERT_BOTH_BOTTOM(L|R|B|LIGHT_VERT),
			LIGHT_HORIZ_TOP_LEFT(T|L|LIGHT_HORIZ),
			LIGHT_HORIZ_TOP_RIGHT(T|R|LIGHT_HORIZ),
			LIGHT_HORIZ_BOTTOM_LEFT(B|L|LIGHT_HORIZ),
			LIGHT_HORIZ_BOTTOM_RIGHT(B|R|LIGHT_HORIZ),
			LIGHT_HORIZ_BOTH_LEFT(T|B|L|LIGHT_HORIZ),
			LIGHT_HORIZ_BOTH_RIGHT(T|B|R|LIGHT_HORIZ),
			;
			
			private static final Map<String, Shape> NAME_LOOKUP = newHashMap();
			private static final Shape[] VALUES = values();
			private static final Shape[] FLAG_LOOKUP = new Shape[(ALL|SPECIAL) + 1];
			
			
			public final boolean hasLeftEdge, hasRightEdge, hasTopEdge, hasBottomEdge;
			public final boolean isLight;
			private final String name;
			private final int flags;
			private Shape mirrorHoriz = this, mirrorVert = this;
			private Shape rotateCW = this, rotateCCW = this;
			private EnumPanelFace whiteFace, blackFace; 
			
			private Shape(int flags) {
				this.name = name().toLowerCase(Locale.ROOT);
				this.flags = flags;
				this.hasLeftEdge = (flags & L) != 0;
				this.hasRightEdge = (flags & R) != 0;
				this.hasTopEdge = (flags & T) != 0;
				this.hasBottomEdge = (flags & B) != 0;
				this.isLight = (flags & SPECIAL ^ SPECIAL) != 0;
			}
			
			@Override
			public String getName() {
				return name;
			}
			
			public boolean isHorizontalLight() {
				return isLight && (flags & LIGHT_HORIZ) != 0;
			}
			
			public boolean isVerticalLight() {
				return isLight && (flags & LIGHT_VERT) != 0;
			}
			
			public EnumPanelFace.Shape mirrorAlong(EnumFacing.Plane plane) {
				switch(plane) {
				case HORIZONTAL: return mirrorHoriz;
				case VERTICAL:   return mirrorVert;
				default: throw new AssertionError();
				}
			}
			
			public EnumPanelFace.Shape mirrorHorizontal() {
				return mirrorHoriz;
			}
			
			public EnumPanelFace.Shape mirrorVertical() {
				return mirrorVert;
			}
			
			public EnumPanelFace.Shape rotateCW() {
				return rotateCW;
			}
			
			public EnumPanelFace.Shape rotateCCW() {
				return rotateCCW;
			}
			
			public EnumPanelFace getPanelFace(boolean isPortalable) {
				return isPortalable? whiteFace : blackFace;
			}
			
			public EnumPanelFace getWhitePanelFace() {
				return whiteFace;
			}
			
			public EnumPanelFace getBlackPanelFace() {
				return blackFace;
			}
			
			public static EnumPanelFace.Shape byName(String name) {
				return name == null? null : NAME_LOOKUP.get(name);
			}
			
			public static EnumPanelFace.Shape fromIndex(int index) {
				return VALUES[abs(index) % VALUES.length];
			}
			
			public static ShapeSelector select() {
				return new ShapeSelector();
			}
			
			static {
				for(EnumPanelFace.Shape shape : VALUES) {
					NAME_LOOKUP.put(shape.name, shape);
					FLAG_LOOKUP[shape.flags] = shape;
					if((shape.flags & ALL) == ALL || (shape.flags & ALL) == 0)
						shape.rotateCW = shape.rotateCCW = shape.mirrorHoriz = shape.mirrorVert = shape;
				}
				for(EnumPanelFace.Shape shape : VALUES) {
					if((shape.flags & ALL) != ALL && (shape.flags & ALL) != 0) {
						int shapeflagsNotDir = shape.flags & ~ALL;
						if(shape.isLight)
							shapeflagsNotDir ^= LIGHT_HORIZ | LIGHT_VERT; 
						int newflags;
						// rotate clockwise
						newflags = shapeflagsNotDir | (shape.flags & B) >> B_off | (shape.flags & ALL) << 1;
						shape.rotateCW = FLAG_LOOKUP[newflags];
						// rotate counter-clockwise
						newflags = (shape.flags & L) << B_off | (shape.flags & ALL) >> 1;
						shape.rotateCCW = FLAG_LOOKUP[shapeflagsNotDir | newflags];
						// mirror horizontally
						newflags = shape.flags & (T|B);
						if(shape.hasLeftEdge)
							newflags |= R;
						if(shape.hasRightEdge)
							newflags |= L;
						shape.mirrorHoriz = FLAG_LOOKUP[shapeflagsNotDir | newflags];
						// mirror vertically
						newflags = shape.flags & (L|R);
						if(shape.hasTopEdge)
							newflags |= B;
						if(shape.hasBottomEdge)
							newflags |= T;
						shape.mirrorVert = FLAG_LOOKUP[shapeflagsNotDir | newflags];
					}
				}
				
				
			}
			
			private static int makeFlags(boolean left, boolean right, boolean top, boolean bottom) {
				int flags = 0;
				if(left)
					flags |= L;
				if(right)
					flags |= R;
				if(top)
					flags |= T;
				if(bottom)
					flags |= B;
				return flags;
			}
			
			public static class ShapeSelector {
				private boolean left = false, right = false, top = false, bottom = false;
				
				private ShapeSelector() {}
				
				public ShapeSelector left() {
					left = true;
					return this;
				}
				
				public ShapeSelector right() {
					right = true;
					return this;
				}
				
				public ShapeSelector top() {
					top = true;
					return this;
				}
				
				public ShapeSelector bottom() {
					bottom = true;
					return this;
				}
				
				public Shape light_horiz() {
					if(left && right && top && bottom)
						throw new IllegalStateException("Cannot build horizontal light from left=1 right=1 top=1 bottom=1");
					Shape res = FLAG_LOOKUP[LIGHT_HORIZ | makeFlags(left, right, top, bottom)];
					if(res == null)
						throw new IllegalStateException("Cannot build horizontal light from " + this);
					return res;
				}
				
				public Shape light_vert() {
					if(left && right && top && bottom)
						throw new IllegalStateException("Cannot build vertical light from left=1 right=1 top=1 bottom=1");
					Shape res = FLAG_LOOKUP[LIGHT_HORIZ | makeFlags(left, right, top, bottom)];
					if(res == null)
						throw new IllegalStateException("Cannot build vertical light from " + this);
					return res;
				}
				
				public Shape shape() {
					return FLAG_LOOKUP[makeFlags(left, right, top, bottom)];
				}
				
				@Override
				public int hashCode() {
					return makeFlags(left, right, top, bottom);
				}
				
				@Override
				public String toString() {
					return "left=" + (left? '1' : '0') + " right=" + (right? '1' : '0') + " top=" + (top? '1' : '0') + " bottom=" + (bottom? '1' : '0');
				}
				
				@Override
				public boolean equals(Object obj) {
					return this == obj || obj instanceof ShapeSelector && this.hashCode() == obj.hashCode();
				}
				
			}
			
		}
		
		public static PanelFaceSelector select() {
			return new PanelFaceSelector();
		}
		
		public static class PanelFaceSelector {
			private final EnumSet<EnumPanelFace> options = EnumSet.allOf(EnumPanelFace.class);
			
			private PanelFaceSelector() {}
			
			private boolean left = true;
			
			public PanelFaceSelector left() {
				if(left) {
					left = false;
					options.removeIf(face -> !face.shape.hasLeftEdge);
					if(options.isEmpty())
						throw new IllegalStateException("the result of this operation eliminated all possibilites");
				}
				return this;
			}
			
			private boolean right = true;
			
			public PanelFaceSelector right() {
				if(right) {
					right = false;
					options.removeIf(face -> !face.shape.hasRightEdge);
					if(options.isEmpty())
						throw new IllegalStateException("the result of this operation eliminated all possibilites");
				}
				return this;
			}
			
			private boolean top = true;
			
			public PanelFaceSelector top() {
				if(top) {
					top = false;
					options.removeIf(face -> !face.shape.hasTopEdge);
					if(options.isEmpty())
						throw new IllegalStateException("the result of this operation eliminated all possibilites");
				}
				return this;
			}
			
			private boolean bottom = true;
			
			public PanelFaceSelector bottom() {
				if(bottom) {
					bottom = false;
					options.removeIf(face -> !face.shape.hasBottomEdge);
					if(options.isEmpty())
						throw new IllegalStateException("the result of this operation eliminated all possibilites");
				}
				return this;
			}
			
			private boolean grid = true;
			
			public PanelFaceSelector grid() {
				if(grid) {
					grid = false;
					options.removeIf(face -> face.shape != Shape.GRID);
					if(options.isEmpty())
						throw new IllegalStateException("the result of this operation eliminated all possibilites");
				}
				return this;
			}
			
			private boolean light = true;
			
			public PanelFaceSelector light() {
				if(light) {
					light = false;
					options.removeIf(face -> !face.isLight);
					if(options.isEmpty())
						throw new IllegalStateException("the result of this operation eliminated all possibilites");
				}
				return this;
			}
			
			private boolean none = true;
			
			public PanelFaceSelector none() {
				if(none) {
					none = false;
					options.removeIf(face -> face.shape.hasBottomEdge || face.shape.hasTopEdge || face.shape.hasRightEdge || face.shape.hasLeftEdge);
					if(options.isEmpty())
						throw new IllegalStateException("the result of this operation eliminated all possibilites");
				}
				return this;
			}
			
			private boolean tall = true;
			
			public PanelFaceSelector tall() {
				if(tall) {
					tall = false;
					options.removeIf(face -> {
						Shape shape = face.shape;
						int sum = 0;
						if(shape.hasLeftEdge)
							sum++;
						if(shape.hasRightEdge)
							sum++;
						if(shape.hasTopEdge)
							sum++;
						if(shape.hasBottomEdge)
							sum++;
						return sum == 1 || sum == 4;
					});
					if(options.isEmpty())
						throw new IllegalStateException("the result of this operation eliminated all possibilites");
				}
				return this;
			}
			
			private boolean tall_edge = true;
			
			public PanelFaceSelector tall_edge() {
				if(tall_edge) {
					tall_edge = false;
					tall = false;
					options.removeIf(face -> {
						Shape shape = face.shape;
						int sum = 0;
						if(shape.hasLeftEdge)
							sum++;
						if(shape.hasRightEdge)
							sum++;
						if(shape.hasTopEdge)
							sum++;
						if(shape.hasBottomEdge)
							sum++;
						return sum != 3;
					});
					if(options.isEmpty())
						throw new IllegalStateException("the result of this operation eliminated all possibilites");
				}
				return this;
			}
			
			private boolean dirty = true;
			
			public PanelFaceSelector dirty() {
				if(dirty) {
					if(!clean)
						throw new IllegalStateException("the result of this operation eliminated all possibilites");
					dirty = false;
					options.removeIf(face -> !face.isDirty);
					if(options.isEmpty())
						throw new IllegalStateException("the result of this operation eliminated all possibilites");
				}
				return this;
			}
			
			private boolean clean = true;
			
			public PanelFaceSelector clean() {
				if(clean) {
					if(!dirty)
						throw new IllegalStateException("the result of this operation eliminated all possibilites");
					clean = false;
					options.removeIf(face -> face.isDirty);
					if(options.isEmpty())
						throw new IllegalStateException("the result of this operation eliminated all possibilites");
				}
				return this;
			}
			
			private boolean color = true;
			
			public PanelFaceSelector color(Type type) {
				if(color) {
					color = false;
					options.removeIf(face -> face.type != type);
					if(options.isEmpty())
						throw new IllegalStateException("the result of this operation eliminated all possibilites");
				}
				return this;
			}
			
			public EnumPanelFace shape() {
				if(options.size() != 1)
					throw new IllegalStateException("there are more than 1 possibilities remaining");
				return options.iterator().next();
			}
			
			public EnumSet<EnumPanelFace> shapes() {
				return options.clone();
			}
			
			public Stream<EnumPanelFace> stream() {
				return options.stream();
			}
			
		}
		
	}
	
}
