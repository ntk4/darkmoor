package com.ntk.darkmoor.resource;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ntk.darkmoor.engine.Item;
import com.ntk.darkmoor.exception.LoadException;

public class ItemAssets implements Disposable {

	public static final String ITEMS_FILE = Resources.getResourcePath() + "Item.xml";

	private static ItemAssets instance;
	private Map<String, Item> itemCache;
	private Element rootElement;

//	/**
//	 * Used only for the JUnit tests that can't access Gdx.files.*
//	 * @param itemsXmlFile
//	 * @return
//	 */
//	public static ItemAssets getInstance(String itemsXmlFile) {
//		if (instance != null)
//			return instance;
//		instance = new ItemAssets(itemsXmlFile);
//		return instance;
//	}
	
	/**
	 * Singleton access method to be used internally. For any reference to items see getItem
	 * @return
	 */
	private static ItemAssets getInstance() {
		if (instance != null)
			return instance;
		instance = new ItemAssets(ITEMS_FILE);
		return instance;
	}
	
	public static Item getItem(String itemName) {
		return getInstance().load(itemName);
	}

	private ItemAssets(String fileName) {
		this.rootElement = ResourceUtility.extractRootElement(fileName);
		itemCache = new HashMap<String, Item>();
	}

	private Item load(String itemName) throws LoadException {

		Item item = itemCache.get(itemName);
		if (item != null)
			return item;
		try {

			if (rootElement == null) {
				throw new LoadException("Item resources have not been initialized, cannot load " + itemName);
			}

			Array<Element> children = rootElement.getChildrenByName("item");

			for (Element child : children) {
				if (itemName.equalsIgnoreCase(child.getAttribute("name"))) {
					item = new Item();
					item.load(child);
					itemCache.put(itemName, item);
					break;
				}
			}
		} catch (Exception e) {
			throw new LoadException(e);
		}
		return item;
	}

	@Override
	public void dispose() {
		for (Item item: itemCache.values()) {
			item.dispose();
		}
		itemCache.clear();
		itemCache = null;
		rootElement = null;
	}
}
