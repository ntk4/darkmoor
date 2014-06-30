package com.ntk.darkmoor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.ntk.darkmoor.config.Settings;
import com.ntk.darkmoor.engine.GameColors;
import com.ntk.darkmoor.engine.Hero;
import com.ntk.darkmoor.engine.Team;
import com.ntk.darkmoor.resource.GraphicAssets;

public class GameScreenObjects implements Disposable {

	public Stage stage;
	public Team team;
	public Skin uiSkin;

	// internal graphics cache

	/** default hero heads in normal state */
	private SpriteDrawable[] heroHeads;
	private SpriteDrawable unconsciousHero;
	private SpriteDrawable deadHero;

	// Scene2d Actors
	public Image[] heads;
	public Label[] heroNameLabels;
	public Label[] heroHitPointLabels;
	public ProgressBar[] heroHitPointProgressBars;

	// temporary objects
	private Hero heroToSwap;

	public void initializeHeads() {
		// Hero heads
		// don't initialize to current (e.g. 4 heroes) but to max in order to avoid resizing
		heads = new Image[Team.MAX_HEROES];
		heroHeads = new SpriteDrawable[Team.MAX_HEROES];
		heroNameLabels = new Label[Team.MAX_HEROES];
		heroHitPointLabels = new Label[Team.MAX_HEROES];
		heroHitPointProgressBars = new ProgressBar[Team.MAX_HEROES];

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 2; x++) {

				int index = y * 2 + x;
				if (index > team.getHeroCount())
					continue;
				Hero hero = team.getHeroes()[index];
				if (hero == null)
					continue;

				// Heads
				Sprite sprite = GraphicAssets.getDefault().getTextureSet("heads").getSprite(hero.getHead());
				heroHeads[index] = new SpriteDrawable(sprite);
				heads[index] = new Image(heroHeads[index]);

				// initialize the positions once, not on every frame
				heads[index].setPosition(736 + 288 * x, 1024 - (y * 334 + 18));
				heads[index].setSize(128, 202);

				stage.addActor(heads[index]);

				// Names
				heroNameLabels[index] = new Label(team.getHeroes()[index].getName(), uiSkin, "gameFont48",
						GameColors.Black);
				heroNameLabels[index].setPosition(740 + 288 * y, 1210 - (x * 334));
				stage.addActor(heroNameLabels[index]);

				// Hit points
				heroHitPointLabels[index] = new Label(team.getHeroes()[index].getName(), uiSkin, "gameFont48",
						GameColors.Black);
				heroHitPointLabels[index].setPosition(740 + 288 * y, 948 - (x * 334));
				stage.addActor(heroHitPointLabels[index]);

				heroHitPointProgressBars[index] = new ProgressBar(hero.getHitPoint().getCurrent(), hero.getHitPoint()
						.getMax(), 1, false, uiSkin); //TODO: ntk: regenerate progress bar when a hero gains additional HP!
				heroHitPointProgressBars[index].setPosition(740 + 288 * y, 948 - (x * 334));
				heroHitPointProgressBars[index].setHeight(60);
				heroHitPointProgressBars[index].setWidth(240);
				stage.addActor(heroHitPointProgressBars[index]);

			}
		}
		unconsciousHero = new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("heads").getSprite(2));
		deadHero = new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("heads").getSprite(4));
	}

	public void updateHeroes() {
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 2; x++) {

				int index = y * 2 + x;

				if (index > team.getHeroCount())
					continue;
				if (team.getHeroes()[index] == null)
					continue;
				heroHitPointLabels[index].setPosition(740 + 288 * y, 948 - (x * 334));

				updateHero(index);

			}
		}
	}

	private Hero updateHero(int index) {
		Hero hero = team.getHeroes()[index];
		if (hero == null)
			return hero;

		// Head
		if (hero.isDead())
			heads[index].setDrawable(deadHero);
		else if (hero.isUnconscious())
			heads[index].setDrawable(unconsciousHero);
		else
			heads[index].setDrawable(heroHeads[index]);

		// Name
		if (heroToSwap == hero) {
			heroNameLabels[index].setColor(GameColors.Red);
			heroNameLabels[index].setText(" Swapping");
		} else if (team.getSelectedHero() == hero) {
			heroNameLabels[index].setColor(GameColors.White);
			heroNameLabels[index].setText(hero.getName());
		} else {
			heroNameLabels[index].setColor(GameColors.Black);
			heroNameLabels[index].setText(hero.getName());
		}
		
		// HP
		if (Settings.getLastLoadedInstance().isHPAsBar()) {
			float percent = (float) hero.getHitPoint().getCurrent() / (float) hero.getHitPoint().getMax();
			Color color = GameColors.Green;
			if (percent < 0.15)
				color = GameColors.Red;
			else if (percent < 0.4)
				color = GameColors.Yellow;

			heroHitPointProgressBars[index].setColor(color);
			heroHitPointProgressBars[index].setVisible(true);
			heroHitPointLabels[index].setVisible(false);
			heroHitPointProgressBars[index].setRange(0, hero.getHitPoint().getMax());
			heroHitPointProgressBars[index].setValue(hero.getHitPoint().getCurrent());
		} else {
			heroHitPointLabels[index].setText(hero.getHitPoint().getCurrent() + " of " + hero.getHitPoint().getMax());
			heroHitPointProgressBars[index].setVisible(false);
			heroHitPointLabels[index].setVisible(true);
		}

		return hero;
	}

	@Override
	public void dispose() {
		heads = null;
	}
}
