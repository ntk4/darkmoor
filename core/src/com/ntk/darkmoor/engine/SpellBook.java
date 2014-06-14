package com.ntk.darkmoor.engine;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ntk.darkmoor.engine.Hero.HeroClass;
import com.ntk.darkmoor.engine.Item.ItemType;
import com.ntk.darkmoor.engine.gui.GUI;
import com.ntk.darkmoor.resource.Resources;
import com.ntk.darkmoor.resource.TextureSet;
import com.ntk.darkmoor.stub.GameTime;

public class SpellBook {
	private int spellLevel;
	private TextureSet textureSet;
	private Hero hero;
	private HeroClass heroClass;
	private boolean visible;
	private Rectangle mainRectangle = new Rectangle(142, 262, 212, 90);

	public SpellBook() {
		spellLevel = 1;
	}

	public void loadContent() {
		textureSet = Resources.lockSharedTextureSetAsset("Interface");
	}

	public void Dispose() {
		Resources.unlockSharedTextureSetAsset(textureSet);
		textureSet = null;
	}

	/**
	 * Open the Spell Window
	 * 
	 * @param hero
	 *            Hero handle
	 * @param item
	 *            Item used to open the spell book
	 */
	public void open(Hero hero, Item item) {
		if (hero == null)
			return;

		this.hero = hero;

		if (item.getType() == ItemType.Book)
			heroClass = HeroClass.Mage;
		else if (item.getType() == ItemType.HolySymbol)
			heroClass = HeroClass.Cleric;
		else
			return;

		visible = true;
	}

	public void close() {
		visible = false;
	}

	public void draw(SpriteBatch batch) {
		if (!visible)
			return;

		Color color;

		// Main window
		// TODO: ntk: draw the tile
		// batch.drawTile(textureSet, 23, new Vector2(mainRectangle.x, mainRectangle.Y - 2));

		// Levels
		for (int level = 1; level <= 6; level++) {
			int id = spellLevel == level ? 24 : 25;
			// TODO: ntk: draw the tile
			// batch.drawTile(textureSet, id, new Vector2(mainRectangle.x + level * 36 - 36, mainRectangle.Top - 20));
			GUI.getDialogFont().setColor(Color.BLACK);
			GUI.getDialogFont().draw(batch, String.valueOf(level), mainRectangle.x + level * 36 + 12 - 36,
					mainRectangle.y - 20 + 4);
		}

		// Get a list of available spells for this level
		List<Spell> spells = hero.getSpells(heroClass, spellLevel);

		// Display at max 6 spells
		Vector2 pos = new Vector2(146, 264);
		for (int id = 0; id < Math.min(spells.size(), 6); id++) {
			color = GameColors.White;

			if (new Rectangle(pos.x, pos.y, 212, 12).contains(Gdx.input.getX(), Gdx.input.getY()))
				color = GameColors.Black;

			GUI.getDialogFont().setColor(color);
			GUI.getDialogFont().draw(batch, spells.get(id).getName(), pos.x, pos.y);
			pos.add(0, 12);
		}

		// Abort spell
		// TODO: ntk: draw the tile
		// batch.drawTile(textureSet, 30, new Vector2(142, 336));

		// Abort spell
		if (new Rectangle(mainRectangle.x + 2, mainRectangle.y + mainRectangle.height - 14, mainRectangle.width - 56,
				18).contains(Gdx.input.getX(), Gdx.input.getY()))
			color = GameColors.Red;
		else
			color = GameColors.White;
		GUI.getDialogFont().setColor(color);
		GUI.getDialogFont().draw(batch, "Abort spell", 146, 340);

		// Next & previous buttons
		// TODO: ntk: draw the buttons
		// batch.draw(textureSet, 28, new Vector2(298, 336));
		// batch.draw(textureSet, 29, new Vector2(326, 336));
	}

	public void update(GameTime time) {
		if (!visible)
			return;

		hero.getMaxSpellCount(HeroClass.Cleric, 3);

		// Left mouse button
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			// Change spell level
			for (int level = 0; level < 6; level++) {
				if (new Rectangle(mainRectangle.x + level * 36, mainRectangle.y - 18, 36, 18).contains(
						Gdx.input.getX(), Gdx.input.getY())) {
					spellLevel = level + 1;
				}
			}

			// Cast a spell
			Rectangle line = new Rectangle(144, 262, 212, 12);
			for (int i = 0; i < 6; i++) {
				// Cast a spell
				if (line.contains(Gdx.input.getX(), Gdx.input.getY())) {

					Spell spell = hero.popSpell(heroClass, spellLevel, i + 1);
					if (spell != null && spell.getScript().getInstance() != null)
						spell.getScript().getInstance().onCast(spell, hero);
				}

				// Next spell line
				line.setY(line.getY() + 12);
			}

			// Abort spell
			if (new Rectangle(mainRectangle.x + 2, mainRectangle.y + mainRectangle.height - 14,
					mainRectangle.width - 56, 18).contains(Gdx.input.getX(), Gdx.input.getY()))
				visible = false;

			// Previous line
			if (new Rectangle(298, 336, 30, 18).contains(Gdx.input.getX(), Gdx.input.getY())) {
				// TODO: ntk: nothing here?
			}

			// Next line
			if (new Rectangle(328, 336, 30, 18).contains(Gdx.input.getX(), Gdx.input.getY())) {
				// TODO: ntk: nothing here?
			}
		}

	}

	public int getSpellLevel() {
		return spellLevel;
	}

	public TextureSet getTextureSet() {
		return textureSet;
	}

	public Hero getHero() {
		return hero;
	}

	public HeroClass getHeroClass() {
		return heroClass;
	}

	public boolean isVisible() {
		return visible;
	}

	public Rectangle getMainRectangle() {
		return mainRectangle;
	}
	
	
}
