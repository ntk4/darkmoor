package com.ntk.darkmoor;

import java.io.InputStream;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.ntk.darkmoor.config.LanguagesManager;
import com.ntk.darkmoor.config.Log;
import com.ntk.darkmoor.config.SaveGame;
import com.ntk.darkmoor.engine.Entity.EntityAlignment;
import com.ntk.darkmoor.engine.GameColors;
import com.ntk.darkmoor.engine.Hero;
import com.ntk.darkmoor.engine.Hero.HeroClass;
import com.ntk.darkmoor.engine.Hero.HeroGender;
import com.ntk.darkmoor.engine.Hero.HeroRace;
import com.ntk.darkmoor.engine.Hero.InventoryPosition;
import com.ntk.darkmoor.engine.HeroAllowedClasses;
import com.ntk.darkmoor.engine.Profession;
import com.ntk.darkmoor.engine.Team;
import com.ntk.darkmoor.engine.Team.HeroPosition;
import com.ntk.darkmoor.engine.gui.ScreenButton;
import com.ntk.darkmoor.resource.ItemAssets;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.resource.TextureSet;
import com.ntk.darkmoor.stub.Display;

public class CharGen extends GameScreenBase {
	enum CharGenStates {
		SelectRace, SelectHero, SelectClass, SelectAlignment, SelectFace, Confirm, SelectName, Delete,
	}

	private Hero[] heroes;
	private Rectangle[] heroBoxes;
	private Vector2[] nameLocations;
	private int heroID;
	private Rectangle backButton;
	private Map<HeroRace, int[]> allowedClass;
	private SpriteBatch batch;
	private TextureSet textureSet;
	private TextureSet heads;
	private BitmapFont font;
	private BitmapFont nameFont;
	private ScreenButton playButton;
	private Animation anims;
	private CharGenStates currentState;
	private int faceOffset;

	public CharGen(Game game) {
		super(game);
		
		heroes = new Hero[4];

		heroBoxes = new Rectangle[] {
			new Rectangle(32, 128, 64, 64),
			new Rectangle(160, 128, 64, 64),
			new Rectangle(32, 256, 64, 64),
			new Rectangle(160, 256, 64, 64),
		};
		nameLocations = new Vector2[] {
			new Vector2(4, 212), new Vector2(132, 212), new Vector2(4, 340), new Vector2(132, 340),
		};
		heroID = -1;

		backButton = new Rectangle(528, 344, 76, 32);

		// Allowed classes

		allowedClass = HeroAllowedClasses.allowedClass;

	}

	@Override
	public void loadContent() {
		Log.debug("[CharGen] LoadContent()");

		// TODO: ntk: the batch should be given and be already opened
		batch = new SpriteBatch();

		textureSet = Resources.createTextureSetAsset("chargen");

		heads = Resources.createTextureSetAsset("heads");

		font = Resources.createFontAsset("intro");

		nameFont = Resources.createFontAsset("name");

		playButton = new ScreenButton("", new Rectangle(48, 362, 166, 32));
		// playButton.Selected += new EventHandler(PlayButton_Selected);

		// stringTable = LanguagesManager.getInstance();// ."Chargen");
		// stringTable.LanguageName = Game.LanguageName;

		anims = Resources.createAsset(Animation.class, "Animations");
		// anims.play();

		currentState = CharGenStates.SelectHero;

		// TODO: ntk: Load name list
		InputStream stream = Resources.load("names.xml");
		if (stream != null) {
			XmlReader xr = new XmlReader();
			// xr.parse(stream);
			// names = XDocument.Load(xr);
		}

	}

	String getRandomName(HeroRace race, HeroGender gender) {
		// TODO: ntk: load the Names.xml and return a random one uncommenting the next section
		return "";

		/*
		 * var res = (from name in Names.Descendants("race") where name.Attribute("name").Value == race.ToString()
		 * select new { Entries = name.Element(gender.ToString()).Elements("name") }).FirstOrDefault();
		 * 
		 * if (res == null || res.Entries.Count() == 0) return string.Empty;
		 * 
		 * return res.Entries.ElementAt(GameBase.Random.Next(res.Entries.Count() - 1)).Value;
		 */
	}

	@Override
	public void unloadContent() {
		Log.debug("[CharGen] UnloadContent()");

		if (textureSet != null)
			textureSet.dispose();
		textureSet = null;

		if (heads != null)
			heads.dispose();
		heads = null;

		if (font != null)
			font.dispose();
		font = null;

		if (nameFont != null)
			nameFont.dispose();
		nameFont = null;

		//
		// if (anims != null)
		// anims.dispose();
		anims = null;

		if (batch != null)
			batch.dispose();
		batch = null;

	}

	/**
	 * Equipe all heroes in the team
	 * 
	 */
	void PrepareTeam() {
		for (Hero hero : heroes) {
			if (hero == null)
				continue;

			hero.addToInventory(ItemAssets.getItem("iron ration"));
			hero.addToInventory(ItemAssets.getItem("iron ration"));
			hero.addToInventory(ItemAssets.getItem("potion of extra healing"));

			if (hero.getClasses().contains(HeroClass.Fighter)) {
				hero.setInventoryItem(InventoryPosition.Armor, ItemAssets.getItem("scalemail +1"));
				hero.setInventoryItem(InventoryPosition.Primary, ItemAssets.getItem("long sword"));
				hero.setInventoryItem(InventoryPosition.Secondary, ItemAssets.getItem("shield"));
			} else if (hero.getClasses().contains(HeroClass.Ranger)) {
				hero.setInventoryItem(InventoryPosition.Armor, ItemAssets.getItem("scalemail +1"));
				hero.setInventoryItem(InventoryPosition.Primary, ItemAssets.getItem("long sword"));
				hero.setInventoryItem(InventoryPosition.Secondary, ItemAssets.getItem("shield +1"));
			} else if (hero.getClasses().contains(HeroClass.Mage)) {
				hero.setInventoryItem(InventoryPosition.Armor, ItemAssets.getItem("chainmail +1"));
				hero.setInventoryItem(InventoryPosition.Primary, ItemAssets.getItem("long sword"));
				hero.setInventoryItem(InventoryPosition.Secondary, ItemAssets.getItem("shield +1"));

				hero.addToInventory(ItemAssets.getItem("mage scroll of magic missile"));
				hero.addToInventory(ItemAssets.getItem("mage scroll of fireball"));
			} else if (hero.getClasses().contains(HeroClass.Paladin)) {
				hero.setInventoryItem(InventoryPosition.Armor, ItemAssets.getItem("scalemail +1"));
				hero.setInventoryItem(InventoryPosition.Primary, ItemAssets.getItem("short sword +1"));
				hero.setInventoryItem(InventoryPosition.Secondary, ItemAssets.getItem("shield +1"));

				hero.addToInventory(ItemAssets.getItem("paladin holy symbol"));
				hero.addToInventory(ItemAssets.getItem("scroll of cure serious wounds"));
			} else if (hero.getClasses().contains(HeroClass.Cleric)) {
				hero.setInventoryItem(InventoryPosition.Armor, ItemAssets.getItem("chainmail +1"));
				hero.setInventoryItem(InventoryPosition.Primary, ItemAssets.getItem("mace +1"));
				hero.setInventoryItem(InventoryPosition.Secondary, ItemAssets.getItem("shield"));

				hero.addToInventory(ItemAssets.getItem("cleric holy symbol"));
				hero.addToInventory(ItemAssets.getItem("cleric scroll of cure serious wounds"));
			} else if (hero.getClasses().contains(HeroClass.Thief)) {
				hero.setInventoryItem(InventoryPosition.Armor, ItemAssets.getItem("leather armor +1"));
				hero.setInventoryItem(InventoryPosition.Primary, ItemAssets.getItem("short sword +1"));
				hero.setInventoryItem(InventoryPosition.Secondary, ItemAssets.getItem("shield +1"));

				hero.addToInventory(ItemAssets.getItem("lock picks"));
			}
		}
	}

	void playButton_Selected() {
		// if (Heroes.Any(hero => hero == null)) return;
		GameScreen screen = new GameScreen(game);

		// Generate the team
		game.setSavedGame(new SaveGame(Resources.getSavegameFilename()));

		Team gsteam = new Team();
		for (int i = 0; i < 4; i++)
			gsteam.addHero(heroes[i], HeroPosition.valueOf(i));

		screen.newGame(gsteam);

		ScreenManager.addScreen(screen);

		exitScreen();
	}

	@Override
	public void update(float delta, boolean hasFocus, boolean isCovered) {
		// Go back to the main menu
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			ScreenManager.addScreen(new MainMenu(game));
			exitScreen();
		}

		switch (currentState) {
		// section Select hero
		case SelectHero:
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				for (int id = 0; id < 4; id++) {
					if (heroBoxes[id].contains(new Vector2(Gdx.input.getX(), Gdx.input.getY()))) {
						heroID = id;

						// Create a new hero or remove it
						if (heroes[id] == null) {
							heroes[id] = new Hero();

							currentState = CharGenStates.SelectRace;
						} else
							currentState = CharGenStates.Delete;
					}
				}
			}
			break;

		// section Select race
		case SelectRace: {
			Vector2 point = new Vector2(302, 160);
			for (int i = 0; i < 12; i++) {
				if (new Rectangle(point.x, point.y, 324, 16).contains(new Vector2(Gdx.input.getX(), Gdx.input.getY()))
						&& Gdx.input.isButtonPressed(Buttons.LEFT)) {
					getCurrentHero().setRace(HeroRace.valueOf(i / 2));
					getCurrentHero().setGender(HeroGender.valueOf(i % 2));
					currentState = CharGenStates.SelectClass;
				}

				point.y += 18;
			}
		}
			break;

		// section Select class
		case SelectClass: {
			Vector2 point = new Vector2(304, 0);
			for (int i = 0; i < 9; i++) {
				point.y = 160 + i * 18;
				if (new Rectangle(point.x, point.y, 324, 16).contains(new Vector2(Gdx.input.getX(), Gdx.input.getY()))
						&& Gdx.input.isButtonPressed(Buttons.LEFT)) {
					getCurrentHero().getProfessions().clear();

					switch (i) {
					case 0:
						getCurrentHero().getProfessions().add(new Profession(0, HeroClass.Fighter));
						break;
					case 1:
						getCurrentHero().getProfessions().add(new Profession(0, HeroClass.Ranger));
						break;
					case 2:
						getCurrentHero().getProfessions().add(new Profession(0, HeroClass.Mage));
						break;
					case 3:
						getCurrentHero().getProfessions().add(new Profession(0, HeroClass.Cleric));
						break;
					case 4:
						getCurrentHero().getProfessions().add(new Profession(0, HeroClass.Thief));
						break;
					case 5:
						getCurrentHero().getProfessions().add(new Profession(0, HeroClass.Fighter));
						getCurrentHero().getProfessions().add(new Profession(0, HeroClass.Thief));
						break;
					case 6:
						getCurrentHero().getProfessions().add(new Profession(0, HeroClass.Fighter));
						getCurrentHero().getProfessions().add(new Profession(0, HeroClass.Mage));
						break;
					case 7:
						getCurrentHero().getProfessions().add(new Profession(0, HeroClass.Fighter));
						getCurrentHero().getProfessions().add(new Profession(0, HeroClass.Mage));
						getCurrentHero().getProfessions().add(new Profession(0, HeroClass.Thief));
						break;
					case 8:
						getCurrentHero().getProfessions().add(new Profession(0, HeroClass.Thief));
						getCurrentHero().getProfessions().add(new Profession(0, HeroClass.Mage));
						break;
					}

					currentState = CharGenStates.SelectAlignment;
				}

				// Back
				if (backButton.contains(new Vector2(Gdx.input.getX(), Gdx.input.getY()))
						&& Gdx.input.isButtonPressed(Buttons.LEFT))
					currentState = CharGenStates.SelectRace;
			}

		}
			break;

		// section Select alignment
		case SelectAlignment: {
			Vector2 point = new Vector2(304, 0);
			for (int i = 0; i < 9; i++) {
				point.y = 176 + i * 18;
				if (new Rectangle(point.x, point.y, 324, 16).contains(new Vector2(Gdx.input.getX(), Gdx.input.getY()))
						&& Gdx.input.isButtonPressed(Buttons.LEFT)) {
					EntityAlignment[] alignments = new EntityAlignment[] {
						EntityAlignment.LawfulGood,
						EntityAlignment.NeutralGood,
						EntityAlignment.ChaoticGood,
						EntityAlignment.LawfulNeutral,
						EntityAlignment.TrueNeutral,
						EntityAlignment.ChaoticNeutral,
						EntityAlignment.LawfulEvil,
						EntityAlignment.NeutralEvil,
						EntityAlignment.ChaoticEvil,
					};

					getCurrentHero().setAlignment(alignments[i]);
					getCurrentHero().rollAbilities();
					currentState = CharGenStates.SelectFace;
				}
			}

			// Back
			if (backButton.contains(new Vector2(Gdx.input.getX(), Gdx.input.getY()))
					&& Gdx.input.isButtonPressed(Buttons.LEFT))
				currentState = CharGenStates.SelectClass;
		}
			break;

		// section Select face
		case SelectFace: {

			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				if (new Rectangle(288, 132, 64, 32).contains(new Vector2(Gdx.input.getX(), Gdx.input.getY())))
					faceOffset--;

				if (new Rectangle(288, 164, 64, 32).contains(new Vector2(Gdx.input.getX(), Gdx.input.getY())))
					faceOffset++;

				// Select a face
				for (int x = 0; x < 4; x++) {
					if (new Rectangle(352 + x * 64, 132, 64, 64).contains(new Vector2(Gdx.input.getX(), Gdx.input
							.getY()))) {
						getCurrentHero().setHead(faceOffset + x);
						currentState = CharGenStates.Confirm;
						break;
					}
				}
			}

			// Limit value
			if (getCurrentHero().getGender() == HeroGender.Male) {
				if (faceOffset < 0)
					faceOffset = 0;

				if (faceOffset > 25)
					faceOffset = 25;
			} else {
				if (faceOffset < 29)
					faceOffset = 29;

				if (faceOffset > 40)
					faceOffset = 40;
			}

			// Back
			if (backButton.contains(new Vector2(Gdx.input.getX(), Gdx.input.getY()))
					&& Gdx.input.isButtonPressed(Buttons.LEFT))
				currentState = CharGenStates.SelectAlignment;
		}
			break;

		// section Confirm
		case Confirm: {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				// Reroll
				if (new Rectangle(448, 318, 76, 32).contains(new Vector2(Gdx.input.getX(), Gdx.input.getY()))) {
					getCurrentHero().rollAbilities();
				}

				// Faces
				if (new Rectangle(448, 350, 76, 32).contains(new Vector2(Gdx.input.getX(), Gdx.input.getY()))) {
					getCurrentHero().setHead(-1);
					currentState = CharGenStates.SelectFace;
				}

				// Modify
				if (new Rectangle(528, 316, 76, 32).contains(new Vector2(Gdx.input.getX(), Gdx.input.getY()))) {
				}

				// Keep
				if (new Rectangle(528, 350, 76, 32).contains(new Vector2(Gdx.input.getX(), Gdx.input.getY()))) {
					currentState = CharGenStates.SelectName;
					getCurrentHero().setName(getRandomName(getCurrentHero().getRace(), getCurrentHero().getGender()));
					// TODO: ntk: event handling
					// Keyboard.KeyDown += new EventHandler<PreviewKeyDownEventArgs>(Keyboard_OnKeyDown);
				}
			}
		}
			break;

		// section Select name
		case SelectName: {
			// Back
			if (backButton.contains(new Vector2(Gdx.input.getX(), Gdx.input.getY()))
					&& Gdx.input.isButtonPressed(Buttons.LEFT)) {
				currentState = CharGenStates.Confirm;
				// TODO: ntk: event handling
				// Keyboard.KeyDown -= Keyboard_OnKeyDown;
			}

		}
			break;

		// section Delete hero
		case Delete: {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				// Delete
				if (new Rectangle(448, 344, 76, 32).contains(new Vector2(Gdx.input.getX(), Gdx.input.getY()))) {
					heroes[heroID] = null;
					currentState = CharGenStates.SelectHero;
				}

				// Ok
				if (new Rectangle(528, 344, 76, 32).contains(new Vector2(Gdx.input.getX(), Gdx.input.getY()))) {
					currentState = CharGenStates.SelectHero;
				}
			}
		}
			break;
		}

		// TODO: ntk: Update anim
		// if (anims != null)
		// anims.update(time);

		// If the team is ready, let's go !
		if (playButton.getRectangle().contains(new Vector2(Gdx.input.getX(), Gdx.input.getY()))
				&& Gdx.input.isButtonPressed(Buttons.LEFT))// && isTeamReadyToPlay())
		{
			PrepareTeam();
			playButton.onSelectEntry();
		}
	}

	private Hero getCurrentHero() {
		if (heroID == -1)
			return null;

		return heroes[heroID];
	}

	/**
	 * Draws the scene
	 */
	public void draw() {
		// Clears the background
		Display.clearBuffers();

		batch.begin();

		// Background
		// TODO: ntk: draw the background
		// batch.drawTile(Tileset, 0, Vector2.Empty, Color.White);

		// Heroes faces and names
		for (int i = 0; i < 4; i++) {
			Hero hero = heroes[i];
			if (hero == null)
				continue;

			// TODO: ntk: draw the texture and font
			// Batch.DrawTile(Heads, hero.Head, heroBoxes[i].Location);

			// Batch.DrawString(NameFont, NameLocations[i], GameColors.Cyan, hero.Name);
		}

		switch (currentState) {
		// section Select hero
		case SelectHero: {
			font.setColor(Color.WHITE);
			font.draw(batch, LanguagesManager.getString("Chargen", 1), 304, 160, 300, 64);

			// Team is ready, game can begin...
			if (isTeamReadyToPlay())
				// TODO: ntk: draw the texture
				// Batch.DrawTile(Tileset, 1, new Vector2(48, 362));
				;
		}
			break;

		// section Select race
		case SelectRace: {
			// TODO: ntk: draw the anim & texture
			// Anims.Draw(Batch, heroBoxes[heroID].Location);
			// Batch.DrawString(Font, new Rectangle(294, 134, 300, 64), GameColors.Cyan, StringTable.GetString(34));

			Vector2 point = new Vector2(300, 140);
			Color color;
			for (int i = 0; i < 12; i++) {
				point.y += 18;
				if (new Rectangle(point.x, point.y, 324, 16).contains(new Vector2(Gdx.input.getX(), Gdx.input.getY())))
					color = GameColors.getColor(255, 85, 85);
				else
					color = Color.WHITE;
				// TODO: ntk: draw the anim & texture
				// Batch.DrawString(Font, point, color, StringTable.GetString(i + 22));
			}

		}
			break;

		// section Select class
		case SelectClass: {
			// TODO: ntk: draw the anim & texture
			// Animation
			// Anims.Draw(Batch, heroBoxes[heroID].Location);

			// "Select class :"
			// Batch.DrawString(Font, new Rectangle(294, 134, 300, 64), GameColors.Cyan, StringTable.GetString(2));

			Vector2 point = new Vector2(302, 0);
			Color color;
			for (int i = 0; i < 9; i++) {
				point.y = 160 + i * 18;

				// On mouse over, paint it in red
				if (new Rectangle(point.x, point.y, 324, 16).contains(new Vector2(Gdx.input.getX(), Gdx.input.getY())))
					color = GameColors.Red;
				else
					color = Color.WHITE;

				// TODO: ntk: draw the font
				// Batch.DrawString(Font, point, color, StringTable.GetString(i + 3));
			}

			// TODO: ntk: draw the texture
			// Back
			// Batch.DrawTile(Tileset, 3, BackButton.Location);
			// Batch.DrawTile(Tileset, 12, new Vector2(BackButton.Location.X + 12, BackButton.Location.Y + 12));
		}
			break;

		// section Select alignment
		case SelectAlignment: {
			// TODO: ntk: draw the anim & texture
			// Anims.Draw(Batch, heroBoxes[heroID].Location);
			// Batch.DrawString(Font, new Rectangle(304, 140, 300, 64), GameColors.Cyan, StringTable.GetString(12));

			Vector2 point = new Vector2(304, 0);
			Color color;
			for (int i = 0; i < 9; i++) {
				point.y = 176 + i * 18;
				if (new Rectangle(286, 176 + i * 18, 324, 16).contains(new Vector2(Gdx.input.getX(), Gdx.input.getY())))
					color = GameColors.getColor(255, 85, 85);
				else
					color = Color.WHITE;
				// TODO: ntk: draw the font
				// Batch.DrawString(Font, point, color, StringTable.GetString(i + 13));
			}

			// Back
			// TODO: ntk: draw the texture
			// Batch.DrawTile(Tileset, 3, BackButton.Location);
			// Batch.DrawTile(Tileset, 12, new Vector2(BackButton.Location.X + 12, BackButton.Location.Y + 12));
		}
			break;

		// section Select face
		case SelectFace: {
			// TODO: ntk: draw the anim & texture
			// Anims.Draw(Batch, heroBoxes[heroID].Location);

			// Class and professions
			// Batch.DrawString(Font, new Rectangle(300, 210, 300, 64), Color.White, CurrentHero.Race.ToString());
			String txt = "";
			for (Profession prof : getCurrentHero().getProfessions())
				txt += prof.getHeroClass().toString() + "/";
			txt = txt.substring(0, txt.length() - 1);
			// Batch.DrawString(Font, new Rectangle(300, 228, 300, 64), Color.White, txt);

			displayProperties();

			// TODO: ntk: draw the textures till the end of the block
			// Left/right box
			// Batch.DrawTile(Tileset, 3, new Vector2(288, 132));
			// Batch.DrawTile(Tileset, 18, new Vector2(300, 140));
			// Batch.DrawTile(Tileset, 3, new Vector2(288, 164));
			// Batch.DrawTile(Tileset, 19, new Vector2(300, 172));

			// Faces
			// for (int i = 0; i < 4; i++)
			// Batch.DrawTile(Heads, i + FaceOffset, new Vector2(354 + i * 64, 132));
			// Back
			// Batch.DrawTile(Tileset, 3, BackButton.Location);
			// Batch.DrawTile(Tileset, 12, new Vector2(BackButton.Location.X + 12, BackButton.Location.Y + 12));

		}
			break;

		// section Confirm
		case Confirm: {
			// TODO: ntk: draw the textures
			// Class and professions
			// Batch.DrawString(Font, new Rectangle(300, 210, 300, 64), Color.White, CurrentHero.Race.ToString());
			String txt = "";
			for (Profession prof : getCurrentHero().getProfessions())
				txt += prof.getHeroClass().toString() + "/";
			txt = txt.substring(0, txt.length() - 1);
			// Batch.DrawString(Font, new Rectangle(300, 228, 300, 64), Color.White, txt);

			displayProperties();

			// TODO: ntk: draw the texture till end of block
			// Batch.DrawTile(Heads, CurrentHero.Head, new Vector2(438, 132));

			// Reroll
			// Batch.DrawTile(Tileset, 5, new Vector2(448, 318));
			// Batch.DrawTile(Tileset, 11, new Vector2(462, 330));

			// Faces
			// Batch.DrawTile(Tileset, 5, new Vector2(448, 350));
			// Batch.DrawTile(Tileset, 20, new Vector2(466, 362));

			// Modify
			// Batch.DrawTile(Tileset, 5, new Vector2(528, 316));
			// Batch.DrawTile(Tileset, 14, new Vector2(540, 328));

			// Keep
			// Batch.DrawTile(Tileset, 5, new Vector2(528, 350));
			// Batch.DrawTile(Tileset, 13, new Vector2(550, 360));
		}
			break;

		// section Select name
		case SelectName: {
			// TODO: ntk: draw the textures
			// Batch.DrawString(Font, new Rectangle(296, 200, 300, 64), GameColors.Cyan, "Name: ");
			// Batch.DrawString(Font, new Rectangle(380, 200, 300, 64), Color.White, CurrentHero.Name);

			displayProperties();

			// Batch.DrawTile(Heads, CurrentHero.Head, new Vector2(438, 132));
			// Back
			// Batch.DrawTile(Tileset, 3, BackButton.Location);
			// Batch.DrawTile(Tileset, 12, new Vector2(BackButton.Location.X + 12, BackButton.Location.Y + 12));
		}
			break;

		// section Delete hero
		case Delete: {
			// TODO: ntk: draw the textures, font
			// Batch.DrawTile(Heads, CurrentHero.Head, new Vector2(438, 132));
			// Batch.DrawString(Font, new Rectangle(292, 190, 300, 64), Color.White, CurrentHero.Name);

			// Class and professions
			// Batch.DrawString(Font, new Rectangle(300, 214, 300, 64), Color.White, CurrentHero.Race.ToString());
			String txt = "";
			for (Profession prof : getCurrentHero().getProfessions())
				txt += prof.getHeroClass().toString() + "/";
			txt = txt.substring(0, txt.length() - 1);
			// Batch.DrawString(Font, new Rectangle(300, 232, 300, 64), Color.White, txt);

			displayProperties();

			// TODO: ntk: draw the textures
			// Delete
			// Batch.DrawTile(Tileset, 7, new Vector2(448, 350));

			// OK
			// Batch.DrawTile(Tileset, 5, new Vector2(528, 350));
			// Batch.DrawTile(Tileset, 15, new Vector2(558, 360));

		}
			break;
		}

		batch.end();
	}

	private boolean isTeamReadyToPlay() {
		for (int id = 0; id < 4; id++) {
			if (heroes[id] == null)
				return false;
		}

		return true;
	}

	// / <summary>
	// / Display hero properties
	// / </summary>
	void displayProperties() {
		// TODO: ntk: draw the fonts
		// Batch.DrawString(Font, new Rectangle(294, 256, 300, 64), Color.White, "STR " +
		// CurrentHero.Strength.Value.ToString());
		// Batch.DrawString(Font, new Rectangle(294, 276, 300, 64), Color.White, "INT " +
		// CurrentHero.Intelligence.Value.ToString());
		// Batch.DrawString(Font, new Rectangle(294, 296, 300, 64), Color.White, "WIS " +
		// CurrentHero.Wisdom.Value.ToString());
		// Batch.DrawString(Font, new Rectangle(294, 316, 300, 64), Color.White, "DEX " +
		// CurrentHero.Dexterity.Value.ToString());
		// Batch.DrawString(Font, new Rectangle(294, 336, 300, 64), Color.White, "CON " +
		// CurrentHero.Constitution.Value.ToString());
		// Batch.DrawString(Font, new Rectangle(294, 356, 300, 64), Color.White, "CHA " +
		// CurrentHero.Charisma.Value.ToString());
		// Batch.DrawString(Font, new Rectangle(462, 256, 300, 64), Color.White, "AC  " +
		// CurrentHero.ArmorClass.ToString());
		// Batch.DrawString(Font, new Rectangle(462, 276, 300, 64), Color.White, "HP  " +
		// CurrentHero.HitPoint.Max.ToString());

		String lvl = "";
		for (Profession prof : getCurrentHero().getProfessions())
			lvl += prof.getLevel() + "/";
		// TODO: ntk: draw the fonts
		// Batch.DrawString(Font, new Rectangle(462, 296, 300, 64), Color.White, "LVL " + lvl.Substring(0, lvl.Length -
		// 1));
	}

	void Keyboard_OnKeyDown(Keys key) {
		// TODO: ntk: handle all keyboard events for typing names etc.

	}

	public Hero[] getHeroes() {
		return heroes;
	}

	public Rectangle[] getHeroBoxes() {
		return heroBoxes;
	}

	public Vector2[] getNameLocations() {
		return nameLocations;
	}

	public int getHeroID() {
		return heroID;
	}

	public Rectangle getBackButton() {
		return backButton;
	}

	public Map<HeroRace, int[]> getAllowedClass() {
		return allowedClass;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public TextureSet getTextureSet() {
		return textureSet;
	}

	public TextureSet getHeads() {
		return heads;
	}

	public BitmapFont getFont() {
		return font;
	}

	public BitmapFont getNameFont() {
		return nameFont;
	}

	public ScreenButton getPlayButton() {
		return playButton;
	}

	public Animation getAnims() {
		return anims;
	}

	public CharGenStates getCurrentState() {
		return currentState;
	}

	public int getFaceOffset() {
		return faceOffset;
	}
}
