package com.ntk.darkmoor;

import java.io.IOException;
import java.io.StringWriter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.XmlWriter;
import com.ntk.darkmoor.config.Log;
import com.ntk.darkmoor.config.SaveGameSlot;
import com.ntk.darkmoor.config.Settings;
import com.ntk.darkmoor.engine.Dungeon;
import com.ntk.darkmoor.engine.GameColors;
import com.ntk.darkmoor.engine.GameMessage;
import com.ntk.darkmoor.engine.Item;
import com.ntk.darkmoor.engine.ScriptedDialog;
import com.ntk.darkmoor.engine.SpellBook;
import com.ntk.darkmoor.engine.Team;
import com.ntk.darkmoor.engine.gui.BaseWindow;
import com.ntk.darkmoor.resource.ItemAssets;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.resource.TextureSet;

public class GameScreen extends GameScreenBase {

	private static Dungeon dungeon;
	private static Team team;

	private SpellBook spellBook;
	private TextureSet textureSet;
	private TextureSet heads;
	private Item[] hands;
	private BitmapFont inventoryFont;
	private BitmapFont outlinedFont;
	private ScriptedDialog dialog;

	public GameScreen(Game game) {
		super(game);
	}

	@Override
	public void loadContent() {

		long startTime = new java.util.Date().getTime();

		// Keyboard input scheme
		/*
		 * inputScheme = Resources.createAsset<InputScheme>(Game.InputSchemeName); if (inputScheme == null) {
		 * Log.error("ERROR !!! No InputSchema detected !!!"); inputScheme = new InputScheme();
		 * InputScheme["MoveForward"] = Keys.Z; InputScheme["MoveBackward"] = Keys.S; InputScheme["StrafeLeft"] =
		 * Keys.Q; InputScheme["StrafeRight"] = Keys.D; InputScheme["TurnLeft"] = Keys.A; InputScheme["TurnRight"] =
		 * Keys.E; InputScheme["Inventory"] = Keys.I; InputScheme["SelectHero1"] = Keys.D1; InputScheme["SelectHero2"] =
		 * Keys.D2; InputScheme["SelectHero3"] = Keys.D3; InputScheme["SelectHero4"] = Keys.D4;
		 * InputScheme["SelectHero5"] = Keys.D5; InputScheme["SelectHero6"] = Keys.D6; }
		 * Log.debug("InputScheme (%d ms)", new java.util.Date().getTime() - startTime);
		 */

		// Interface tileset
		textureSet = Resources.createSharedTextureSetAsset("Interface", "Interface");

		// Heroe's heads
		heads = Resources.createTextureSetAsset("Heads");

		// Fonts
		inventoryFont = Resources.createSharedFontAsset("inventory");
		outlinedFont = Resources.createSharedFontAsset("outline");

		// Misc init
		spellBook = new SpellBook();
		spellBook.loadContent();
		GameMessage.init();

		hands = new Item[] {
			ItemAssets.getItem("left hand"), ItemAssets.getItem("right hand")
		};

		super.loadContent();
		
		Log.debug("[GameScreen] loadContent() - finished ! (%d ms)", new java.util.Date().getTime() - startTime);
	}

	@Override
	protected Image setupBackground() {
		Image image = new Image(textureSet.getSprite(0));
		image.setBounds(0, 0, DarkmoorGame.DISPLAY_WIDTH, DarkmoorGame.DISPLAY_WIDTH);
		// mainMenuImage.addAction(Actions.fadeIn(2));
		// mainMenuImage.setZIndex(0);
		return image;
	}

	public static Team getTeam() {
		return team;
	}

	public static Dungeon getDungeon() {
		return dungeon;
	}

	protected static void setDungeon(Dungeon dungeon) {
		GameScreen.dungeon = dungeon;
	}

	@Override
	public void unloadContent() {
		Log.debug("[team] : UnloadContent");

		if (GameScreen.getDungeon() != null)
			GameScreen.getDungeon().dispose();
		GameScreen.setDungeon(null);

		if (team != null)
			team.dispose();
		team = null;

		if (spellBook != null)
			spellBook.dispose();
		spellBook = null;

		GameMessage.dispose();

		Resources.unlockSharedFontAsset(outlinedFont);
		Resources.unlockSharedFontAsset(inventoryFont);
		Resources.unlockSharedTextureSetAsset(textureSet);
		textureSet = null;
		inventoryFont = null;
		outlinedFont = null;

		if (heads != null)
			heads.dispose();
		heads = null;

		if (dialog != null)
			dialog.dispose();
		dialog = null;

		// if (inputScheme != null)
		// inputScheme.dispose();
		// inputScheme = null;
	}

	public boolean loadGameSlot(int slotid) {
		SaveGameSlot slot = Settings.getSavedGame().getSlot(slotid);
		if (slot == null) {
			Log.debug("[GameScreen]LoadGameSlot() : Slot " + slotid + " empty !");
			return false;
		}

		// Load dungeon
		if (dungeon != null)
			dungeon.dispose();

		// If Shift key is down, create a new dungeon (DEBUG)
		if (slot.getDungeon() == null || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT))
			dungeon = Resources.createDungeonResource(Settings.getSavedGame().getDungeonName());
		else {
			dungeon = new Dungeon();
			dungeon.load(slot.getDungeon());
		}
		dungeon.init();

		// Load team
		if (team != null)
			team.dispose();
		else
			team = new Team();
		team.load(slot.getTeam());
		team.init();

		GameMessage.addMessage("Party loaded...", GameColors.Yellow);
		return true;
	}

	public boolean saveGameSlot(int slotid) throws IOException {
		if (slotid < 0 || slotid >= Settings.getLastLoadedInstance().getSaveSlots())
			return false;

		SaveGameSlot slot = Settings.getSavedGame().getSlot(slotid);
		slot.setName("slot " + slotid);
		StringWriter tempStringWriter = new StringWriter();
		XmlWriter tempXmlWriter = new XmlWriter(tempStringWriter);
		slot.setTeam(team.save(tempXmlWriter));

		// Some debug, if shiftkey down, no dungeon save
		if (!Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {

			tempXmlWriter.flush();
			tempXmlWriter.close();
			tempXmlWriter = new XmlWriter(tempStringWriter);
			// dungeon.save(tempXmlWriter, 1);
			// slot.setDungeon(tempXmlWriter.);
		}
		// Save the game
		if (Settings.getSavedGame().save())
			GameMessage.addMessage("Game saved.");
		else
			GameMessage.addMessage("Game NOT saved !");

		return true;
	}

	public SpellBook getSpellBook() {
		return spellBook;
	}

	public void setDialog(ScriptedDialog scriptedDialog) {
		if (dialog != null)
			dialog.dispose();

		dialog = scriptedDialog;
	}

	public ScriptedDialog getDialog() {
		return dialog;
	}

	public void newGame(Team gsteam) {
		// TODO Auto-generated method stub
		
	}
}
