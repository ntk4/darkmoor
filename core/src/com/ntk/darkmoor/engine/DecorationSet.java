package com.ntk.darkmoor.engine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.stub.Tile;
import com.ntk.darkmoor.stub.TileSet;
import com.ntk.darkmoor.stub.ViewFieldPosition;

public class DecorationSet {

	private static final String TAG = "decorationset";
	
	private Map<Integer, Decoration> decorations;
	private boolean disposed;
	private String tileSetName;
	private TileSet tileset;
	private String backgroundTileSet;

	private Object name;

	public DecorationSet() {
		decorations = new HashMap<Integer, Decoration>();
		disposed = false;
	}

	public boolean init() {
		loadTileSet(tileSetName);

		return true;
	}

	public void dispose() {
		if (tileset != null)
			tileset.dispose();
		tileset = null;

		decorations = null;
		disposed = true;
	}

	public boolean IsPointInside(int id, Vector2 location) {
		Decoration deco = getDecoration(id);
		if (deco == null)
			return false;

		Tile tile = tileset.getTile(deco.getTileId(ViewFieldPosition.L));
		if (tile == null)
			return false;

		Vector2 rectLocation = deco.getLocation(ViewFieldPosition.L);
		Rectangle zone = new Rectangle(rectLocation.x, rectLocation.y, tile.getWidth(), tile.getHeight());

		return zone.contains(location);
	}

	public Decoration getDecoration(int id) {
		if (decorations == null || id == -1)
			return null;

		if (!decorations.containsKey(id))
			return null;

		return decorations.get(Integer.valueOf(id));
	}

	public Decoration AddDecoration(int id) {
		Decoration deco = new Decoration();
		decorations.put(Integer.valueOf(id), deco);

		return deco;
	}

	public boolean loadTileSet(String name)
	{
		tileSetName = name;

		if (StringUtils.isEmpty(name))
			return false;

		tileset = Resources.createAsset(TileSet.class, tileSetName);

		return tileset != null;
	}
	
	public boolean isPointInside(int decoration, Vector2 location) {
		// TODO Auto-generated method stub
		return false;
	}

	public void draw(SpriteBatch batch, int id, ViewFieldPosition position) {
		if (batch == null || id == -1)
			return;

		Decoration deco =  decorations.get(Integer.valueOf(id));
		if (deco == null)
			return;

		//TODO: ntk: draw the tile
//		batch.drawTile(tileset, deco.getTileId(position), deco.getLocation(position),
//			Color.WHITE, 0.0f,
//			deco.getSwap(position) ? SpriteEffects.FLIP_HORIZONTALLY : SpriteEffects.NONE,
//			0.0f);
	}

	public boolean load(XmlReader.Element node) {
		if (node == null || !StringUtils.equals(node.getName(), TAG))
			return false;

		name = node.getName();

		for (int i = 0; i < node.getChildCount(); i++) {
			XmlReader.Element child = node.getChild(i);

			if ("tileset".equals(child.getName())) {
				loadTileSet(child.getAttribute("name"));
				
			} else if ("decoration".equals(child.getName())) {
				Decoration deco = new Decoration();
				deco.load(node);
				int id = Integer.parseInt(node.getAttribute("id"));
				decorations.put(Integer.valueOf(id), deco);
				
			} else if ("editor".equals(child.getName())) {
				backgroundTileSet = child.getAttribute("tileset");
				
			}
		}
		return true;
	}

	public boolean save(XmlWriter writer, int id) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG).attribute("name", name);
		
		writer.element("tileset").attribute("name", tileSetName).pop();
		
		writer.element("editor").attribute("tileset", backgroundTileSet).pop();
		
		for (Map.Entry<Integer, Decoration> decoration : decorations.entrySet()) {
			decoration.getValue().save(writer, decoration.getKey());
		}

		writer.pop();

		return true;
	}

	public String getTileSetName() {
		return tileSetName;
	}

	public void setTileSetName(String tileSetName) {
		this.tileSetName = tileSetName;
	}

	public String getBackgroundTileSet() {
		return backgroundTileSet;
	}

	public void setBackgroundTileSet(String backgroundTileSet) {
		this.backgroundTileSet = backgroundTileSet;
	}

	public Object getName() {
		return name;
	}

	public void setName(Object name) {
		this.name = name;
	}

	public static String getTag() {
		return TAG;
	}

	public Map<Integer, Decoration> getDecorations() {
		return decorations;
	}

	public boolean isDisposed() {
		return disposed;
	}

	public TileSet getTileset() {
		return tileset;
	}
	
}
