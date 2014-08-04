package com.ntk.darkmoor.engine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.engine.ViewField.ViewFieldPosition;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.resource.TextureSet;

public class DecorationSet {

	private static final String TAG = "decorationset";
	
	private Map<Integer, Decoration> decorations;
	private boolean disposed;
	private String textureSetName;
	private TextureSet textureSet;
	private String backgroundTextureSet;

	private Object name;

	public DecorationSet() {
		decorations = new HashMap<Integer, Decoration>();
		disposed = false;
	}

	public boolean init() {
		loadTextureSet(textureSetName);

		return true;
	}

	public void dispose() {
		if (textureSet != null)
			textureSet.dispose();
		textureSet = null;

		decorations = null;
		disposed = true;
	}

	public boolean IsPointInside(int id, Vector2 location) {
		Decoration deco = getDecoration(id);
		if (deco == null)
			return false;

		Sprite sprite = textureSet.getSprite(deco.getTextureId(ViewFieldPosition.L));
		if (sprite == null)
			return false;

		Vector2 rectLocation = deco.getLocation(ViewFieldPosition.L);
		Rectangle zone = new Rectangle(rectLocation.x, rectLocation.y, sprite.getWidth(), sprite.getHeight());

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

	public boolean loadTextureSet(String name)
	{
		textureSetName = name;

		if (StringUtils.isEmpty(name))
			return false;

		textureSet = Resources.createTextureSetAsset(textureSetName);

		return textureSet != null;
	}
	
	public boolean isPointInside(int decoration, Vector2 location) {
		// TODO Auto-generated method stub
		return false;
	}

	public void draw(int id, ViewFieldPosition position) {
		if (id == -1)
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
				loadTextureSet(child.getAttribute("name"));
				
			} else if ("decoration".equals(child.getName())) {
				Decoration deco = new Decoration();
				deco.load(child);
				int id = Integer.parseInt(child.getAttribute("id"));
				decorations.put(Integer.valueOf(id), deco);
				
			} else if ("editor".equals(child.getName())) {
				backgroundTextureSet = child.getAttribute("tileset");
				
			}
		}
		return true;
	}

	public boolean save(XmlWriter writer, int id) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG).attribute("name", name);
		
		writer.element("tileset").attribute("name", textureSetName).pop();
		
		writer.element("editor").attribute("tileset", backgroundTextureSet).pop();
		
		for (Map.Entry<Integer, Decoration> decoration : decorations.entrySet()) {
			decoration.getValue().save(writer, decoration.getKey());
		}

		writer.pop();

		return true;
	}

	public String getTextureSetName() {
		return textureSetName;
	}

	public void setTextureSetName(String textureSetName) {
		this.textureSetName = textureSetName;
	}

	public String getBackgroundTextureSet() {
		return backgroundTextureSet;
	}

	public void setBackgroundTextureSet(String backgroundTextureSet) {
		this.backgroundTextureSet = backgroundTextureSet;
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

	public TextureSet getTextureSet() {
		return textureSet;
	}
	
}
