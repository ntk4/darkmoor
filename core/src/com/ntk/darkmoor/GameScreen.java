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
import com.ntk.darkmoor.engine.GameColors;
import com.ntk.darkmoor.engine.GameMessage;
import com.ntk.darkmoor.engine.ScriptedDialog;
import com.ntk.darkmoor.engine.SpellBook;
import com.ntk.darkmoor.engine.Team;
import com.ntk.darkmoor.engine.graphics.GameScreenGroup;
import com.ntk.darkmoor.engine.graphics.HeroInterfaceGroup;
import com.ntk.darkmoor.engine.graphics.InventoryGroup;
import com.ntk.darkmoor.engine.graphics.MazeGroup;
import com.ntk.darkmoor.engine.graphics.StatisticsGroup;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.resource.TextureSet;

public class GameScreen extends GameScreenBase {

	public enum TeamInterface {
		Main, Inventory, Statistic
	}

	private static Team team;

	private SpellBook spellBook;
	private TextureSet textureSet;
	private BitmapFont inventoryFont;
	private BitmapFont outlinedFont;
	private ScriptedDialog dialog;

	private TeamInterface interfaceType;

	// parts of the screen as scene2d actors
	private GameScreenGroup heroInterfaceGroup;
	private InventoryGroup inventoryGroup;
	private MazeGroup mazeGroup;
	private StatisticsGroup statisticsGroup;

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

		// Fonts
		inventoryFont = Resources.createSharedFontAsset("inventory");
		outlinedFont = Resources.createSharedFontAsset("outline");

		// Misc init
		spellBook = new SpellBook();
		spellBook.loadContent();
		GameMessage.init();

		super.loadContent();

		Log.debug("[GameScreen] loadContent() - finished ! (%d ms)", new java.util.Date().getTime() - startTime);
	}

	@Override
	protected Image setupBackground() {
		Image image = new Image(textureSet.getSprite(0));
		image.setBounds(0, 0, DarkmoorGame.DISPLAY_WIDTH, DarkmoorGame.DISPLAY_WIDTH);
		return image;
	}

	@Override
	public void unloadContent() {
		Log.debug("[team] : UnloadContent");

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

		if (dialog != null)
			dialog.dispose();
		dialog = null;

		// if (inputScheme != null)
		// inputScheme.dispose();
		// inputScheme = null;
	}

	private void initScreenObjects() {

		interfaceType = TeamInterface.Main;

		mazeGroup = new MazeGroup();
		mazeGroup.team = GameScreen.team;
		mazeGroup.uiSkin = uiSkin;
		mazeGroup.initialize();
		stage.addActor(mazeGroup);

		heroInterfaceGroup = new HeroInterfaceGroup();
		heroInterfaceGroup.team = GameScreen.team;
		heroInterfaceGroup.uiSkin = uiSkin;
		heroInterfaceGroup.initialize();
		stage.addActor(heroInterfaceGroup);

		inventoryGroup = new InventoryGroup();
		inventoryGroup.team = GameScreen.team;
		inventoryGroup.uiSkin = uiSkin;
		inventoryGroup.initialize();
		stage.addActor(inventoryGroup);

		statisticsGroup = new StatisticsGroup();
		statisticsGroup.team = GameScreen.team;
		statisticsGroup.uiSkin = uiSkin;
		statisticsGroup.initialize();
		stage.addActor(statisticsGroup);

	}

	public boolean loadGameSlot(int slotid) {
		SaveGameSlot slot = game.getSavedGame().getSlot(slotid);
		if (slot == null) {
			Log.debug("[GameScreen]LoadGameSlot() : Slot " + slotid + " empty !");
			return false;
		}

		while (Resources.getDungeon() == null)
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		
		// Load team
		if (team != null)
			team.dispose();
		else
			team = new Team();
		team.load(slot.getTeam());

		team.init();

		newGame(team);

		GameMessage.addMessage("Party loaded...", GameColors.Yellow);
		return true;
	}

	public boolean saveGameSlot(int slotid) throws IOException {
		if (slotid < 0 || slotid >= Settings.getLastLoadedInstance().getSaveSlots())
			return false;

		SaveGameSlot slot = game.getSavedGame().getSlot(slotid);
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
		if (game.getSavedGame().save())
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
		initScreenObjects();
	}

	@Override
	public void update(float delta, boolean hasFocus, boolean isCovered) {
		super.update(delta, hasFocus, isCovered);

		if (team.getMaze() != null)
			mazeGroup.update();

		if (interfaceType == TeamInterface.Main) {

			heroInterfaceGroup.setVisible(true);
			inventoryGroup.setVisible(false);
			statisticsGroup.setVisible(false);
			heroInterfaceGroup.update();

		} else if (interfaceType == TeamInterface.Inventory) {
			heroInterfaceGroup.setVisible(false);
			inventoryGroup.setVisible(true);
			statisticsGroup.setVisible(false);
			inventoryGroup.update();

		} else if (interfaceType == TeamInterface.Statistic) {

			heroInterfaceGroup.setVisible(false);
			inventoryGroup.setVisible(false);
			statisticsGroup.setVisible(true);
			statisticsGroup.update();
		}
	}

	public static Team getTeam() {
		return team;
	}
}
