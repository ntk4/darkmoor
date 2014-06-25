package com.ntk.darkmoor;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.ntk.darkmoor.engine.Hero;
import com.ntk.darkmoor.engine.Team;
import com.ntk.darkmoor.resource.GraphicAssets;

public class GameScreenObjects implements Disposable {

	public Stage stage;
	public Team team;

	// internal graphics cache

	/** default hero heads in normal state */
	private SpriteDrawable[] heroHeads;
	private SpriteDrawable unconsciousHero;
	private SpriteDrawable deadHero;

	// Scene2d Actors
	public Image[] heads;

	public void initializeHeads() {
		// Hero heads
		// don't initialize to current (e.g. 4 heroes) but to max in order to avoid resizing
		heads = new Image[Team.MAX_HEROES];
		heroHeads = new SpriteDrawable[Team.MAX_HEROES];

		int i = 0;
		for (Hero hero : team.getHeroes()) {
			if (hero == null)
				continue;
			Sprite sprite = GraphicAssets.getDefault().getTextureSet("heads").getSprite(hero.getHead());
			// sprite.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			heroHeads[i] = new SpriteDrawable(sprite);
			heads[i] = new Image(heroHeads[i]);
			stage.addActor(heads[i++]);
		}
		unconsciousHero = new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("heads").getSprite(2));
		deadHero = new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("heads").getSprite(4));
	}

	public void updateHeads() {
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 2; x++) {
				int index = y * 2 + x;
				if (index > team.getHeroCount())
					continue;

				Hero hero = team.getHeroes()[y * 2 + x];
				if (hero == null)
					continue;
				// old coordinates: (0,0=>top left)
				// 366+144*x+2, y*104+22
				// 370, 22 // 515, 22
				// 370, 126 // 515, 126

				heads[index].setDrawable(heroHeads[index]);
				heads[index].setPosition(736 + 288 * x, 1024 - (y * 334 + 18));
				heads[index].setSize(128, 202);

			}
		}
	}

	@Override
	public void dispose() {
		heads = null;
	}
}
