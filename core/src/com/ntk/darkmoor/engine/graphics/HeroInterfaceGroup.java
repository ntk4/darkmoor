package com.ntk.darkmoor.engine.graphics;


import java.util.Date;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.ntk.darkmoor.config.Settings;
import com.ntk.darkmoor.engine.Attack;
import com.ntk.darkmoor.engine.GameColors;
import com.ntk.darkmoor.engine.HandAction;
import com.ntk.darkmoor.engine.HandAction.ActionResult;
import com.ntk.darkmoor.engine.Hero;
import com.ntk.darkmoor.engine.Hero.HeroHand;
import com.ntk.darkmoor.engine.Hero.InventoryPosition;
import com.ntk.darkmoor.engine.Item;
import com.ntk.darkmoor.engine.Team;
import com.ntk.darkmoor.resource.GraphicAssets;
import com.ntk.darkmoor.resource.ItemAssets;

public class HeroInterfaceGroup extends GameScreenGroup {

	protected static final int HERO_WINDOW_X = 736;
	protected static final int HERO_WINDOW_HEIGHT = 334;
	protected static final int HERO_WINDOW_WIDTH = 288;
	
	private static final String ATTACK_LABEL_MISS = "MISS";
	private static final int HERO_FACE_HEIGHT = 202;
	private static final int HERO_FACE_WIDTH = 128;
	private static final int HAND_X = HERO_WINDOW_X + HERO_FACE_WIDTH + 24;
	

	// internal graphics cache

	/** default hero heads in normal state */
	SpriteDrawable[] heroHeads;
	SpriteDrawable unconsciousHero;
	SpriteDrawable deadHero;
	SpriteDrawable emptyHandLeft;
	SpriteDrawable emptyHandRight;
	SpriteDrawable handShadow;
	SpriteDrawable attackHitSprite;
	private SpriteDrawable heroHitSprite;

	// Scene2d Actors
	public Image[] heads, heroHitImages;
	public Label[] heroNameLabels;
	public Label[] heroHitPointLabels;
	public ProgressBar[] heroHitPointProgressBars;
	public Image[] rightHands;
	public Image[] rightHandShadows;
	public Image[] leftHands;
	public Image[] leftHandShadows;
	public Label[] rightHandAttackLabels, leftHandAttackLabels, heroHitLabels;

	// temporary objects
	private Hero heroToSwap;
	
	@Override
	public void initialize() {
		// Hero heads
		// don't initialize to current (e.g. 4 heroes) but to max in order to avoid resizing
		heads = new Image[Team.MAX_HEROES];
		heroHeads = new SpriteDrawable[Team.MAX_HEROES];
		heroNameLabels = new Label[Team.MAX_HEROES];
		heroHitPointLabels = new Label[Team.MAX_HEROES];
		heroHitPointProgressBars = new ProgressBar[Team.MAX_HEROES];
		rightHands = new Image[Team.MAX_HEROES];
		leftHands = new Image[Team.MAX_HEROES];
		rightHandShadows = new Image[Team.MAX_HEROES];
		leftHandShadows = new Image[Team.MAX_HEROES];
		rightHandAttackLabels = new Label[Team.MAX_HEROES];
		leftHandAttackLabels = new Label[Team.MAX_HEROES];
		heroHitImages = new Image[Team.MAX_HEROES];
		heroHitLabels = new Label[Team.MAX_HEROES];
	
		unconsciousHero = new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("heads").getSprite(2));
		deadHero = new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("heads").getSprite(4));
		emptyHandRight = new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("items")
				.getSprite(ItemAssets.getItem("right hand").getTextureID()));
		emptyHandLeft = new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("items")
				.getSprite(ItemAssets.getItem("left hand").getTextureID()));
		handShadow = new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("interface").getSprite(3));
		heroHitSprite = new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("interface").getSprite(20));
		attackHitSprite = new SpriteDrawable(GraphicAssets.getDefault().getTextureSet("interface").getSprite(21));
	
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 2; x++) {
	
				initializeHero(x, y);
	
			}
		}
	}
	
	void initializeHero(int x, int y) {
		int index = getHeroIndexInArrays(x, y);
		if (index > team.getHeroCount())
			return;
		Hero hero = team.getHeroes()[index];
		if (hero == null)
			return;

		initializeHeads(x, y, index, hero);

		initializeName(x, y, index);

		initializeHitPoints(x, y, index, hero);

		initializeHands(x, y, index, hero);

	}

	/**
	 * Updates the hero information/images on screen. This method is intended to be called on every frame. It presumes
	 * that all the structures have already been initialized with a call to initializeHeroRectangles, otherwise will
	 * throw exception.
	 */
	@Override
	public void update() {
		now = new Date(); // TODO: check for unnecessary memory consumption here!
	
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 2; x++) {
	
				int index = getHeroIndexInArrays(x, y);
	
				if (index > team.getHeroCount())
					continue;
				if (team.getHeroes()[index] == null)
					continue;
	
				updateHero(index);
	
			}
		}
	}
	
	private Hero updateHero(int index) {
		Hero hero = team.getHeroes()[index];
		if (hero == null)
			return hero;

		updateHeroHead(index, hero);

		updateHeroName(index, hero);

		updateHitPoints(index, hero);

		updateHeroHands(index, hero);

		return hero;
	}

	private void initializeHeads(int x, int y, int index, Hero hero) {
		// Heads
		Sprite sprite = GraphicAssets.getDefault().getTextureSet("heads").getSprite(hero.getHead());
		heroHeads[index] = new SpriteDrawable(sprite);
		heads[index] = new Image(heroHeads[index]);

		// initialize the positions once, not on every frame
		heads[index].setPosition(HERO_WINDOW_X + HERO_WINDOW_WIDTH * x, 1024 - (y * HERO_WINDOW_HEIGHT + 18));
		heads[index].setSize(HERO_FACE_WIDTH, HERO_FACE_HEIGHT);

		addActor(heads[index]);

		heroHitImages[index] = new Image(heroHitSprite);
		heroHitImages[index].setPosition(HERO_WINDOW_X + HERO_WINDOW_WIDTH * x, 1024 - (y * HERO_WINDOW_HEIGHT + 18));
		heroHitImages[index].setSize(HERO_FACE_WIDTH, HERO_FACE_HEIGHT);
		heroHitImages[index].setVisible(false);
		addActor(heroHitImages[index]);

		heroHitLabels[index] = new Label("", uiSkin, "gameFont48", GameColors.White);
		heroHitLabels[index].setPosition(HERO_WINDOW_X + HERO_WINDOW_WIDTH * x, 1024 - (y * HERO_WINDOW_HEIGHT + 18));
		heroHitLabels[index].setSize(HERO_FACE_WIDTH, HERO_FACE_HEIGHT);
		heroHitLabels[index].setVisible(false);
		addActor(heroHitLabels[index]);
	}

	private void initializeName(int x, int y, int index) {
		// Names
		heroNameLabels[index] = new Label(team.getHeroes()[index].getName(), uiSkin, "gameFont48", GameColors.Black);
		heroNameLabels[index].setPosition(HERO_WINDOW_X + 4 + HERO_WINDOW_WIDTH * y, 1210 - (x * HERO_WINDOW_HEIGHT));
		addActor(heroNameLabels[index]);
	}

	private void initializeHitPoints(int x, int y, int index, Hero hero) {
		// Hit points
		heroHitPointLabels[index] = new Label(team.getHeroes()[index].getName(), uiSkin, "gameFont48", GameColors.Black);
		heroHitPointLabels[index]
				.setPosition(HERO_WINDOW_X + 4 + HERO_WINDOW_WIDTH * y, 948 - (x * HERO_WINDOW_HEIGHT));
		addActor(heroHitPointLabels[index]);

		heroHitPointProgressBars[index] = new ProgressBar(hero.getHitPoint().getCurrent(), hero.getHitPoint().getMax(),
				1, false, uiSkin); // TODO: ntk: regenerate progress bar when a hero gains additional HP!
		heroHitPointProgressBars[index].setPosition(HERO_WINDOW_X + 4 + HERO_WINDOW_WIDTH * y,
				948 - (x * HERO_WINDOW_HEIGHT));
		heroHitPointProgressBars[index].setHeight(60);
		heroHitPointProgressBars[index].setWidth(240);
		addActor(heroHitPointProgressBars[index]);
	}

	private void initializeHands(int x, int y, int index, final Hero hero) {

		// Hands // TODO: ntk: change the image when a different item is selected
		final Item item1 = hero.getInventoryItem(InventoryPosition.Primary);
		rightHands[index] = new Image(item1 == null ? emptyHandRight : new SpriteDrawable(GraphicAssets.getDefault()
				.getTextureSet("items").getSprite(item1.getTextureID())));
		rightHands[index].setPosition(HAND_X + HERO_WINDOW_WIDTH * x, 1110 - (y * HERO_WINDOW_HEIGHT));
		rightHands[index].setSize(64, 96);
		rightHands[index].addListener(new InputListener() {

	        public boolean touchDown(InputEvent event, float x, float y,
	               int pointer, int button) {
	        	hero.useHand(HeroHand.Primary);
	            return true;
	        }

	    });
		addActor(rightHands[index]);

		final Item item2 = hero.getInventoryItem(InventoryPosition.Secondary);
		leftHands[index] = new Image(item2 == null ? emptyHandLeft : new SpriteDrawable(GraphicAssets.getDefault()
				.getTextureSet("items").getSprite(item2.getTextureID())));
		leftHands[index].setPosition(HAND_X + HERO_WINDOW_WIDTH * x, 1012 - (y * HERO_WINDOW_HEIGHT));
		leftHands[index].setSize(64, 96);
		leftHands[index].addListener(new InputListener() {

	        public boolean touchDown(InputEvent event, float x, float y,
	               int pointer, int button) {
	        	hero.useHand(HeroHand.Secondary);
	            return true;
	        }

	    });
		addActor(leftHands[index]);

		// Hand shadows
		rightHandShadows[index] = new Image(handShadow);
		rightHandShadows[index].setPosition(HAND_X + HERO_WINDOW_WIDTH * x, 1110 - (y * HERO_WINDOW_HEIGHT));
		rightHandShadows[index].setSize(64, 96);
		rightHandShadows[index].setVisible(false); // invisible by default
		addActor(rightHandShadows[index]);

		leftHandShadows[index] = new Image(handShadow);
		leftHandShadows[index].setPosition(HAND_X + HERO_WINDOW_WIDTH * x, 1012 - (y * HERO_WINDOW_HEIGHT));
		leftHandShadows[index].setSize(64, 96);
		leftHandShadows[index].setVisible(false); // invisible by default
		addActor(leftHandShadows[index]);

		// Attack labels
		rightHandAttackLabels[index] = new Label(ATTACK_LABEL_MISS, uiSkin, "gameFont48", GameColors.White);
		rightHandAttackLabels[index].setPosition(HAND_X + HERO_WINDOW_WIDTH * x, 1110 - (y * HERO_WINDOW_HEIGHT));
		rightHandAttackLabels[index].setSize(64, 96);
		rightHandAttackLabels[index].setVisible(false); // invisible by default
		addActor(rightHandAttackLabels[index]);

		leftHandAttackLabels[index] = new Label(ATTACK_LABEL_MISS, uiSkin, "gameFont48", GameColors.White);
		leftHandAttackLabels[index].setPosition(HAND_X + HERO_WINDOW_WIDTH * x, 1012 - (y * HERO_WINDOW_HEIGHT));
		leftHandAttackLabels[index].setSize(64, 96);
		leftHandAttackLabels[index].setVisible(false); // invisible by default
		addActor(leftHandAttackLabels[index]);
	}

	int getHeroIndexInArrays(int x, int y) {
		return y * 2 + x;
	}

	private void updateHeroHands(int index, Hero hero) {
		updateHeroHand(index, hero, HeroHand.Primary, rightHandShadows, rightHandAttackLabels);
		updateHeroHand(index, hero, HeroHand.Secondary, leftHandShadows, leftHandAttackLabels);
	}

	private void updateHeroHand(int index, Hero hero, HeroHand hand, Image[] shadows, Label[] labels) {

		// TODO: ntk: dead or unconscious can be a long state, devise an event mechanism
		// so as not to do it on each frame
		if (hero.isDead() || hero.isUnconscious()) {
			shadows[index].setDrawable(handShadow);
			shadows[index].setVisible(true);
			return;
		}

		if (!hero.canUseHand(hand)) {
			// reset the image and make it visible
			shadows[index].setDrawable(handShadow);
			shadows[index].setVisible(true);
		} else {
			shadows[index].setVisible(false);
		}

		// Attack //TODO: TEST!
		Attack attack = hero.getLastAttack(hand);
		if (attack != null && !hero.canUseHand(hand)) {
			if (attack.getTarget() != null && !attack.isOutdated(now, 1000)) {
				shadows[index].setDrawable(attackHitSprite);

				labels[index].setVisible(true);

				if (attack.isAHit())
					labels[index].setText(String.valueOf(attack.getHit()));
				else if (attack.isAMiss())
					labels[index].setText(ATTACK_LABEL_MISS);
			}
		} else {
			labels[index].setVisible(false);
		}

		// hand actions //TODO: TEST!
		HandAction action = hero.getLastActionResult(hand);
		if (action != null && action.getResult() != ActionResult.Ok && !action.isOutdated(now, 1000)) {
			shadows[index].setDrawable(attackHitSprite);

			switch (action.getResult()) {
			case NoAmmo:
				labels[index].setText("NO AMMO");
				break;
			case CantReach:
				labels[index].setText("CAN'T REACH");
				break;
			default:
			}
		}

		// hero was hit //TODO: TEST!
		if (hero.getLastAttack() != null && !hero.getLastAttack().isOutdated(now, 1000)) {
			heroHitLabels[index].setText(String.valueOf(hero.getLastAttack().getHit()));
			heroHitImages[index].setVisible(true);
			heroHitLabels[index].setVisible(true);
		} else {
			heroHitImages[index].setVisible(false);
			heroHitLabels[index].setVisible(false);
		}
	}

	private void updateHeroHead(int index, Hero hero) {
		// Head
		if (hero.isDead())
			heads[index].setDrawable(deadHero);
		else if (hero.isUnconscious())
			heads[index].setDrawable(unconsciousHero);
		else
			heads[index].setDrawable(heroHeads[index]);
	}

	private void updateHeroName(int index, Hero hero) {
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
	}

	private void updateHitPoints(int index, Hero hero) {
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
	}
	
	@Override
	public void dispose() {
		heads = null;
	}
}
