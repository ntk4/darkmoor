package com.ntk.darkmoor.engine;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.DarkmoorGame;
import com.ntk.darkmoor.config.Log;
import com.ntk.darkmoor.engine.Compass.CardinalPoint;
import com.ntk.darkmoor.engine.Square.SquarePosition;
import com.ntk.darkmoor.engine.ViewField.ViewFieldPosition;
import com.ntk.darkmoor.engine.graphics.SpriteEffects;
import com.ntk.darkmoor.engine.graphics.TileDrawing;
import com.ntk.darkmoor.resource.ResourceUtility;
import com.ntk.darkmoor.resource.Resources;

/**
 * http://dmweb.free.fr/?q=node/1394
 * 
 * Type of coordinates, lighting bolts , clouds , creatures , items , stack of items ThrownItems , Doors , Floor
 * decorations (foot steps) , Wall decorations , Unreadable texts , Wall texts , Door buttons , Door decorations
 * 
 * @author Nick
 * 
 */

public class DisplayCoordinates {

	public static final int TOP_LEFT_X = 0;
	public static final int TOP_LEFT_Y = DarkmoorGame.DISPLAY_HEIGHT - 215;
	public static final int PLAYABLE_WINDOW_WIDTH = 712;
	public static final int PLAYABLE_WINDOW_HEIGHT = 775;

	static private CardinalPoint[][] drawingWallSides = {//@formatter:off
			new CardinalPoint[] {CardinalPoint.East, CardinalPoint.South},			// A
			new CardinalPoint[] {CardinalPoint.East, CardinalPoint.South},			// B
			new CardinalPoint[] {CardinalPoint.South},								// C
			new CardinalPoint[] {CardinalPoint.West, CardinalPoint.South},			// D
			new CardinalPoint[] {CardinalPoint.West, CardinalPoint.South},			// E

			new CardinalPoint[] {CardinalPoint.East},								// F
			new CardinalPoint[] {CardinalPoint.East, CardinalPoint.South},			// G
			new CardinalPoint[] {CardinalPoint.South},								// H
			new CardinalPoint[] {CardinalPoint.West, CardinalPoint.South},			// I
			new CardinalPoint[] {CardinalPoint.West},								// J

			new CardinalPoint[] {CardinalPoint.East, CardinalPoint.South},			// K
			new CardinalPoint[] {CardinalPoint.South},								// L
			new CardinalPoint[] {CardinalPoint.West, CardinalPoint.South},			// M

			new CardinalPoint[] {CardinalPoint.East},								// N
			new CardinalPoint[] {CardinalPoint.North, CardinalPoint.South, 
								 CardinalPoint.West, CardinalPoint.East},			// Team
			new CardinalPoint[] {CardinalPoint.West},								// O
		};
	
	static public Vector2[] scaleFactor = new Vector2[]
			{
				// EOB 2 scaling : 1.0, 0.66, 0.44

				new Vector2(1.0f, 1.0f),
				new Vector2(0.66f, 0.66f),
				new Vector2(0.5f, 0.5f),
				new Vector2(0.33f, 0.33f)
			};

	static int[] itemScaleOffset = new int[]
		{
			3, 3, 3, 3, 3,
			2, 2, 2, 2, 2,
			   1, 1, 1,
			   0, 0, 0,
		};
	
	static Vector2[][] monsterLocations = 
		{
			//			North West				North East					South West			South East				Middle
			new Vector2[]{new Vector2(20, 132),		new Vector2(48, 132),		new Vector2(-999, -999),	new Vector2(20, 140),		new Vector2(36, 156)},		// A
			new Vector2[]{new Vector2(84, 132),		new Vector2(116, 132),	new Vector2(64, 140),		new Vector2(108, 140),	new Vector2(88, 136)},		// B
			new Vector2[]{new Vector2(160, 132),	new Vector2(184, 132),	new Vector2(152, 140),	new Vector2(192, 140),	new Vector2(192, 136)},		// C
			new Vector2[]{new Vector2(224, 128),	new Vector2(256, 128),	new Vector2(236, 140),	new Vector2(276, 140),	new Vector2(256, 136)},		// D
			new Vector2[]{new Vector2(292, 128),	new Vector2(328, 128),	new Vector2(320, 140),	new Vector2(-999, -999),	new Vector2(312, 136)},		// E

			new Vector2[]{new Vector2(-999, -999),	new Vector2(-999, -999),	new Vector2(-999, -999),	new Vector2(-999, -999),	new Vector2(-999, -999)},		// F
			new Vector2[]{new Vector2(44, 152),		new Vector2(100, 152),	new Vector2(12, 164),		new Vector2(80, 164),		new Vector2(48, 156)},		// G
			new Vector2[]{new Vector2(148, 152),	new Vector2(200, 152),	new Vector2(144, 164),	new Vector2(208, 164),	new Vector2(196, 156)},		// H
			new Vector2[]{new Vector2(252, 152),	new Vector2(304, 152),	new Vector2(280, 164),	new Vector2(344, 164),	new Vector2(312, 156)},		// I
			new Vector2[]{new Vector2(-999, -999),	new Vector2(-999, -999),	new Vector2(-999, -999),	new Vector2(-999, -999),	new Vector2(-999, -999)},		// J

			new Vector2[]{new Vector2(-999, -999),	new Vector2(44, 184),		new Vector2(-999, -999),	new Vector2(12, 212),		new Vector2(28, 200)},		// K
			new Vector2[]{new Vector2(132, 184),	new Vector2(224, 184),	new Vector2(124, 212),	new Vector2(232, 212),	new Vector2(195, 200)},		// L
			new Vector2[]{new Vector2(312, 184),	new Vector2(-999, -999),	new Vector2(344, 212),	new Vector2(-999, -999),	new Vector2(360, 200)},		// M

			new Vector2[]{new Vector2(-999, -999),	new Vector2(-999, -999),	new Vector2(-999, -999),	new Vector2(-999, -999),	new Vector2(-999, -999)},		// N
			new Vector2[]{new Vector2(-999, -999),	new Vector2(-999, -999),	new Vector2(-999, -999),	new Vector2(-999, -999),	new Vector2(-999, -999)},		// Team
			new Vector2[]{new Vector2(-999, -999),	new Vector2(-999, -999),	new Vector2(-999, -999),	new Vector2(-999, -999),	new Vector2(-999, -999)},		// O
		};
	
	static int[] monsterScaleOffset = new int[]
			{
				2, 2, 2, 2, 2,
				1, 1, 1, 1, 1,
				   0, 0, 0,
				   0, 0, 0,
			};
	
	static TileDrawing[][] walls = new TileDrawing[][]
	{
		// A
		new TileDrawing[]
		{
			new TileDrawing(7, new Vector2(62, DisplayCoordinates.TOP_LEFT_Y + 360), 
					CardinalPoint.East, SpriteEffects.NONE,1.4f, 3.55f),
			new TileDrawing(3, new Vector2(-128, DisplayCoordinates.TOP_LEFT_Y + 360), 
					CardinalPoint.South, SpriteEffects.NONE, 2f, 3.40f),
//			new TileDrawing(7, new Vector2(32, 56), CardinalPoint.East, SpriteEffects.NONE),
//			new TileDrawing(3, new Vector2(-64, 54), CardinalPoint.South, SpriteEffects.NONE),
		},
		// B
		new TileDrawing[]
		{
			new TileDrawing(3, new Vector2(64, DisplayCoordinates.TOP_LEFT_Y + 360), 
					CardinalPoint.South, SpriteEffects.NONE, 2f, 3.40f),
			new TileDrawing(7, new Vector2(256, DisplayCoordinates.TOP_LEFT_Y + 360), 
					CardinalPoint.East, SpriteEffects.NONE,1.4f, 3.55f),
		},
		// C
		new TileDrawing[]
		{
			new TileDrawing(3, new Vector2(256, DisplayCoordinates.TOP_LEFT_Y + 360), 
					CardinalPoint.South, SpriteEffects.NONE, 2f, 3.40f),
//			new TileDrawing(3, new Vector2(128, 54), CardinalPoint.South, SpriteEffects.NONE),
		},
		// D
		new TileDrawing[]
		{
			new TileDrawing(3, new Vector2(448, DisplayCoordinates.TOP_LEFT_Y + 360),//108), 
					CardinalPoint.South, SpriteEffects.NONE, 2f, 3.40f),
			new TileDrawing(7, new Vector2(426, DisplayCoordinates.TOP_LEFT_Y + 360),//112), 
					CardinalPoint.East, SpriteEffects.FLIP_HORIZONTALLY,1.4f, 3.55f),
					
//			new TileDrawing(3, new Vector2(224, 54), CardinalPoint.South, SpriteEffects.NONE),
//			new TileDrawing(7, new Vector2(208, 56), CardinalPoint.West, SpriteEffects.FLIP_HORIZONTALLY),
		},
		// E
		new TileDrawing[]
		{
			new TileDrawing(3, new Vector2(640, DisplayCoordinates.TOP_LEFT_Y + 360), 
					CardinalPoint.South, SpriteEffects.NONE, 2f, 3.40f),
			new TileDrawing(7, new Vector2(619, DisplayCoordinates.TOP_LEFT_Y + 360), 
					CardinalPoint.West, SpriteEffects.FLIP_HORIZONTALLY,1.4f, 3.55f),
//			new TileDrawing(3, new Vector2(320, 54), CardinalPoint.South, SpriteEffects.NONE),
//			new TileDrawing(7, new Vector2(304, 56), CardinalPoint.West, SpriteEffects.FLIP_HORIZONTALLY),
		},

		// F
		new TileDrawing[]
		{
			new TileDrawing(6, new Vector2(0, 40), CardinalPoint.East, SpriteEffects.NONE),
		},
		// G
		new TileDrawing[]
		{
			new TileDrawing(2, new Vector2(-128, DisplayCoordinates.TOP_LEFT_Y + 270), 
					CardinalPoint.South, SpriteEffects.NONE, 2f, 3.15f),
			new TileDrawing(6, new Vector2(192, DisplayCoordinates.TOP_LEFT_Y + 272), 
					CardinalPoint.East, SpriteEffects.NONE,1.4f, 3.14f),
//			new TileDrawing(2, new Vector2(-64, 40), CardinalPoint.South, SpriteEffects.NONE),
//			new TileDrawing(6, new Vector2(96, 38), CardinalPoint.East, SpriteEffects.NONE),
		},
		// H
		new TileDrawing[]
		{
			new TileDrawing(2, new Vector2(191,  DisplayCoordinates.TOP_LEFT_Y + 270), 
					CardinalPoint.South, SpriteEffects.NONE, 2.025f, 3.15f),
//			new TileDrawing(2, new Vector2(96, 40), CardinalPoint.South, SpriteEffects.NONE),
		},
		// I
		new TileDrawing[]
		{
			new TileDrawing(2, new Vector2(512, DisplayCoordinates.TOP_LEFT_Y + 270), 
					CardinalPoint.South, SpriteEffects.NONE, 2f, 3.15f),
			new TileDrawing(6, new Vector2(470, DisplayCoordinates.TOP_LEFT_Y + 272), 
					CardinalPoint.West, SpriteEffects.FLIP_HORIZONTALLY,1.4f, 3.14f),
//			new TileDrawing(2, new Vector2(256, 40), CardinalPoint.South, SpriteEffects.NONE),
//			new TileDrawing(6, new Vector2(224, 40), CardinalPoint.West, SpriteEffects.FLIP_HORIZONTALLY),
		},
		// J
		new TileDrawing[]
		{
			new TileDrawing(6, new Vector2(320, 40), CardinalPoint.West, SpriteEffects.FLIP_HORIZONTALLY),
		},

		// K
		new TileDrawing[]
		{
			new TileDrawing(5, new Vector2(96, DisplayCoordinates.TOP_LEFT_Y+105), 
					CardinalPoint.East, SpriteEffects.NONE, 2.0f,3.24f),
			new TileDrawing(1, new Vector2(-415, DisplayCoordinates.TOP_LEFT_Y+105), 
					CardinalPoint.South, SpriteEffects.NONE,2f, 3.24f)
//			new TileDrawing(1, new Vector2(-208, 14), CardinalPoint.South, SpriteEffects.NONE),
//			new TileDrawing(5, new Vector2(48, 14), CardinalPoint.East, SpriteEffects.NONE),
		},
		// L
		new TileDrawing[]
		{
			new TileDrawing(1, new Vector2(96, DisplayCoordinates.TOP_LEFT_Y+105), 
					CardinalPoint.South, SpriteEffects.NONE,2.06f, 3.24f)
//			new TileDrawing(1, new Vector2(48, 14), CardinalPoint.South, SpriteEffects.NONE),
		},
		// M
		new TileDrawing[]
		{
			new TileDrawing(5, new Vector2(525, DisplayCoordinates.TOP_LEFT_Y+105), 
					CardinalPoint.West, SpriteEffects.FLIP_HORIZONTALLY, 2.0f,3.24f),
			new TileDrawing(1, new Vector2(620,DisplayCoordinates.TOP_LEFT_Y+105), 
					CardinalPoint.South, SpriteEffects.NONE,2f, 3.24f),
//			new TileDrawing(1, new Vector2(608, DisplayCoordinates.TOP_LEFT_Y + 28), CardinalPoint.South, SpriteEffects.NONE),
//			new TileDrawing(5, new Vector2(512, DisplayCoordinates.TOP_LEFT_Y + 28), CardinalPoint.West, SpriteEffects.FLIP_HORIZONTALLY),
		},

		// N
		new TileDrawing[]
		{
			new TileDrawing(4, new Vector2(0, TOP_LEFT_Y), CardinalPoint.East, SpriteEffects.NONE, 2.2f, 3.23f),
		},

		// Team
		new TileDrawing[] {},

		// O
		new TileDrawing[]
		{
			new TileDrawing(4, new Vector2(610, DisplayCoordinates.TOP_LEFT_Y - 2 * 0), 
					CardinalPoint.West, SpriteEffects.FLIP_HORIZONTALLY, 2.15f, 3.23f),
//				new TileDrawing(4, new Vector2(304, 0), CardinalPoint.West, SpriteEffects.FLIP_HORIZONTALLY),
		},
	};
	//@formatter:on

	private static TileDrawing[] doors;
	private static TileDrawing[] teleporters;
	private static TileDrawing[] floorPlates;
	private static Vector2[][] ground;
	private static Vector2[][] flyingItems;
	private static TileDrawing[] pits;
	private static TileDrawing[] ceilingPits;
	private static TileDrawing[][] stairs;
	private static Rectangle throwRight;
	private static Rectangle trowLeft;
	private static Rectangle campButton;
	private static Rectangle frontSquare;
	private static Rectangle leftFeetTeam;
	private static Rectangle leftFrontTeamground;
	private static Rectangle rightFeetTeam;
	private static Rectangle rightFrontTeamground;
	private static Rectangle scriptedDialog;
	private static Rectangle[] scriptedDialogChoices;
	private static Rectangle scroll;
	private static Rectangle scrollOk;

	private static boolean loaded;

	static {
		int viewcount = ViewFieldPosition.values().length;

		doors = new TileDrawing[viewcount];
		teleporters = new TileDrawing[viewcount];
		floorPlates = new TileDrawing[viewcount];

		ground = new Vector2[viewcount][5];
		for (int i = 0; i < viewcount; i++)
			for (int j = 0; j < 5; j++)
				ground[i][j] = new Vector2(-999, -999);

		flyingItems = new Vector2[viewcount][5];

		pits = new TileDrawing[viewcount];
		ceilingPits = new TileDrawing[viewcount];
		stairs = new TileDrawing[viewcount][viewcount << 1]; // (viewcount);
		for (int i = 0; i < viewcount; i++)
			stairs[i] = new TileDrawing[viewcount << 1];

		throwRight = new Rectangle(176, 0, 176, 144);
		trowLeft = new Rectangle(0, 0, 176, 144);
		campButton = new Rectangle(578, 354, 62, 42);
		frontSquare = new Rectangle(48, 14, 256, 192);
		leftFeetTeam = new Rectangle(0, 202, 176, 38);
		leftFrontTeamground = new Rectangle(0, 144, 176, 58);
		rightFeetTeam = new Rectangle(176, 202, 176, 38);
		rightFrontTeamground = new Rectangle(176, 144, 176, 58);

		scriptedDialog = new Rectangle(0, 242, 640, 158);
		scriptedDialogChoices = new Rectangle[] {
			// 1 choice
			new Rectangle(442, 378, 190, 18),
			new Rectangle(),
			new Rectangle(),

			// 2 choices
			new Rectangle(118, 378, 190, 18),
			new Rectangle(332, 378, 190, 18),
			new Rectangle(),

			// 3 choices
			new Rectangle(8, 378, 190, 18),
			new Rectangle(224, 378, 190, 18),
			new Rectangle(440, 378, 190, 18),
		};

		scroll = new Rectangle(0, 0, 352, 350);
		scrollOk = new Rectangle(152, 324, 190, 18);
	}

	public static Vector2 getItemScaleFactor(ViewFieldPosition position) {
		return scaleFactor[itemScaleOffset[position.value()]];
	}

	public static Vector2 getScaleFactor(ViewFieldPosition position, Vector2 point) {
		Vector2 vect = scaleFactor[itemScaleOffset[position.value()]];

		return new Vector2((int) (point.x * vect.x), (int) (point.y * vect.y));
	}

	public static Vector2 getMonsterScaleFactor(ViewFieldPosition position) {
		return scaleFactor[monsterScaleOffset[position.value()]];
	}

	public static Vector2 getMonsterLocation(ViewFieldPosition position, SquarePosition sub) {
		return monsterLocations[position.value()][sub.value()];
	}

	public static Color getDistantColor(ViewFieldPosition position) {
		Color color1 = Color.WHITE;
		Color.rgb888ToColor(color1, Color.rgba8888(128, 128, 128, 255));

		Color color2 = Color.WHITE;
		Color.rgb888ToColor(color2, Color.rgba8888(96, 96, 96, 220));

		Color[] colors = new Color[] {
			Color.WHITE, Color.WHITE, color1, color2,
		};
		return colors[itemScaleOffset[position.value()]];
	}

	public static TileDrawing[] getWalls(ViewFieldPosition position) {
		return walls[position.value()];
	}

	public static Vector2 getGroundPosition(ViewFieldPosition view, SquarePosition position) {
		return ground[view.value()][position.value()];
	}

	public static Vector2 getFlyingItem(ViewFieldPosition view, SquarePosition ground) {
		return flyingItems[view.value()][ground.value()];
	}

	public static TileDrawing getDoor(ViewFieldPosition view) {
		return doors[view.value()];
	}

	public static TileDrawing getPit(ViewFieldPosition view) {
		return pits[view.value()];
	}

	public static TileDrawing getCeilingPit(ViewFieldPosition view) {
		return ceilingPits[view.value()];
	}

	public static TileDrawing getFloorPlate(ViewFieldPosition view) {
		return floorPlates[view.value()];
	}

	public static TileDrawing[] getStairs(ViewFieldPosition view) {
		return stairs[view.value()];
	}

	public static TileDrawing getTeleporter(ViewFieldPosition view) {
		return teleporters[view.value()];
	}

	public static boolean load() throws IOException {
		if (loaded)
			return true;

		Element mazeRoot = null;
		mazeRoot = ResourceUtility.extractRootElement(Resources.getResourcePath() + "MazeElements.xml");

		if (mazeRoot == null || !"displaycoordinate".equals(mazeRoot.getName())) {
			Log.error(String.format("Wrong structure for MazeElements.xml file, root is '%s'", mazeRoot.getName()));
			return false;
		}

		int stairIndex = 0; // ntk: check that the conversion of List to array works

		for (int i = 0; i < mazeRoot.getChildCount(); i++) {
			Element node = mazeRoot.getChild(i);

			if ("stair".equalsIgnoreCase(node.getName())) {
				ViewFieldPosition view = ViewFieldPosition.valueOfIgnoreCase(node.getAttribute("position")
						.toUpperCase());

				if (stairs[view.value()].length >= stairIndex - 1) {
					stairs[view.value()][stairIndex++] = getTileDrawing(node);
				} else {
					Log.error("[DisplayCoordinates] load() : Found more stair instances than can possibly load");
				}

			} else if ("ground".equalsIgnoreCase(node.getName())) {
				ViewFieldPosition view = ViewFieldPosition.valueOfIgnoreCase(node.getAttribute("position"));
				SquarePosition squareground = SquarePosition.valueOf(node.getAttribute("coordinate"));

				ground[view.value()][squareground.value()] = new Vector2(Integer.parseInt(node.getAttribute("x")),
						Integer.parseInt(node.getAttribute("y")));

			} else if ("flyingitem".equalsIgnoreCase(node.getName())) {
				ViewFieldPosition view = ViewFieldPosition.valueOfIgnoreCase(node.getAttribute("position"));
				SquarePosition squareground = SquarePosition.valueOf(node.getAttribute("coordinate"));

				flyingItems[view.value()][squareground.value()] = new Vector2(Integer.parseInt(node.getAttribute("x")),
						Integer.parseInt(node.getAttribute("y")));

			} else if ("pit".equalsIgnoreCase(node.getName())) {
				ViewFieldPosition view = ViewFieldPosition.valueOfIgnoreCase(node.getAttribute("position"));

				pits[view.value()] = getTileDrawing(node);

			} else if ("teleporter".equalsIgnoreCase(node.getName())) {
				ViewFieldPosition view = ViewFieldPosition.valueOfIgnoreCase(node.getAttribute("position"));

				teleporters[view.value()] = getTileDrawing(node);

			} else if ("ceilingpit".equalsIgnoreCase(node.getName())) {
				ViewFieldPosition view = ViewFieldPosition.valueOfIgnoreCase(node.getAttribute("position"));

				ceilingPits[view.value()] = getTileDrawing(node);

			} else if ("floorplate".equalsIgnoreCase(node.getName())) {
				ViewFieldPosition view = ViewFieldPosition.valueOfIgnoreCase(node.getAttribute("position"));

				floorPlates[view.value()] = getTileDrawing(node);

			} else if ("door".equalsIgnoreCase(node.getName())) {
				ViewFieldPosition view = ViewFieldPosition.valueOfIgnoreCase(node.getAttribute("position"));

				doors[view.value()] = getTileDrawing(node);

			} else {
				Log.debug("[DisplayCoordinates] load() : Unknown element \"" + node.getName() + "\".");
			}
		}
		loaded = true;
		return true;
	}

	private static TileDrawing getTileDrawing(Element node) {
		if (node == null)
			return null;

		// Tile id
		int id = Integer.parseInt(node.getAttribute("tile"));

		// Location
		Vector2 location = new Vector2(Integer.parseInt(node.getAttribute("x")), Integer.parseInt(node
				.getAttribute("y")));

		// effect
		SpriteEffects effect = SpriteEffects.NONE;
		try {
			effect = SpriteEffects.fromDescription(node.getAttribute("effect"));
		} catch (GdxRuntimeException e) {
			effect = SpriteEffects.NONE;
		}
		// if (node.getAttribute("effect") != null)
		// effect = SpriteEffects.valueOf(node.getAttribute("effect"));

		CardinalPoint side = CardinalPoint.North;
		try {
			side = CardinalPoint.valueOf(node.getAttribute("side"));
		} catch (GdxRuntimeException e) {
		}

		return new TileDrawing(id, location, side, effect);
	}

	public boolean save(String name, XmlWriter writer) throws IOException {
		if (writer == null || StringUtils.isEmpty(name))
			return false;

		return true;
	}

	public static TileDrawing[] getDoors() {
		return doors;
	}

	public static TileDrawing[] getTeleporters() {
		return teleporters;
	}

	public static TileDrawing[] getFloorPlates() {
		return floorPlates;
	}

	public static Vector2[][] getGround() {
		return ground;
	}

	public static Vector2[][] getFlyingItems() {
		return flyingItems;
	}

	public static TileDrawing[] getPits() {
		return pits;
	}

	public static TileDrawing[] getCeilingPits() {
		return ceilingPits;
	}

	public static TileDrawing[][] getStairs() {
		return stairs;
	}

	public static Rectangle getThrowRight() {
		return throwRight;
	}

	public static Rectangle getTrowLeft() {
		return trowLeft;
	}

	public static Rectangle getCampButton() {
		return campButton;
	}

	public static Rectangle getFrontSquare() {
		return frontSquare;
	}

	public static Rectangle getLeftFeetTeam() {
		return leftFeetTeam;
	}

	public static Rectangle getLeftFrontTeamground() {
		return leftFrontTeamground;
	}

	public static Rectangle getRightFeetTeam() {
		return rightFeetTeam;
	}

	public static Rectangle getRightFrontTeamground() {
		return rightFrontTeamground;
	}

	public static Rectangle getScriptedDialog() {
		return scriptedDialog;
	}

	public static Rectangle[] getScriptedDialogChoices() {
		return scriptedDialogChoices;
	}

	public static Rectangle getScroll() {
		return scroll;
	}

	public static Rectangle getScrollOk() {
		return scrollOk;
	}

	public static Vector2[] getScaleFactor() {
		return scaleFactor;
	}

	public static int[] getItemScaleOffset() {
		return itemScaleOffset;
	}

	public static int[] getMonsterScaleOffset() {
		return monsterScaleOffset;
	}

	public static TileDrawing[][] getWalls() {
		return walls;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public static CardinalPoint[][] getDrawingWallSides() {
		return drawingWallSides;
	}
}
