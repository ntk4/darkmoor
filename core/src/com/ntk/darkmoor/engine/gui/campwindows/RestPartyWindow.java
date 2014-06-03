package com.ntk.darkmoor.engine.gui.campwindows;

import java.util.Date;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ntk.darkmoor.engine.CampDialog;
import com.ntk.darkmoor.engine.gui.BaseWindow;
import com.ntk.darkmoor.engine.gui.GUI;
import com.ntk.darkmoor.engine.gui.MessageBox;
import com.ntk.darkmoor.engine.gui.MessageBox.DialogResult;
import com.ntk.darkmoor.engine.gui.MessageBox.MessageBoxButtons;
import com.ntk.darkmoor.engine.gui.ScreenButton;
import com.ntk.darkmoor.stub.GameMessage;
import com.ntk.darkmoor.stub.GameScreen;
import com.ntk.darkmoor.stub.GameTime;
import com.ntk.darkmoor.stub.Hero;
import com.ntk.darkmoor.stub.Team;

public class RestPartyWindow extends BaseWindow {

	private static final int MILLIS_OF_REAL_TIME_FOR_EACH_HOUR_OF_PARTY_REST = 500;
	private Date start;
	private boolean healParty;

	public RestPartyWindow(CampDialog camp, Skin skin) {
		super(camp, "Rest Party :", skin);

		ScreenButton button;

		button = new ScreenButton("Exit", new Rectangle(256, 244, 80, 28));
		button.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return exitSelected(event);
			}

		});
		getButtons().add(button);

		setMessageBox(new MessageBox("Will your healers<br />heals the party ?", skin, MessageBoxButtons.YesNo));
		getMessageBox().addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return healAnswer(event);
			}

		});

	}

	protected boolean healAnswer(Event event) {
		if (((MessageBox) event.getTarget()).getDialogResult() == DialogResult.Yes)
			healParty = true;
		else
			healParty = false;

		start = new java.util.Date();

		return true;
	}

	@Override
	public void update(GameTime time) {
		super.update(time);

		// TODO: ntk: what's the point here? also is the condition correct?
		// No answer, waiting
		if (start == null || start.getTime() == 0)
			return;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		// No answer, waiting
		if (start == null || start.getTime() == 0)
			return;

		Team team = GameScreen.getTeam();

		// Number of hour sleeping
		int hours = (int)(new Date().getTime() - start.getTime())/MILLIS_OF_REAL_TIME_FOR_EACH_HOUR_OF_PARTY_REST;

		//TODO: ntk: check if this is correct drawing
		// Display
		GUI.getMenuFont().setColor(Color.WHITE);
		GUI.getMenuFont().draw(batch, "Hours rested : " + hours, 26, 58);

		//TODO: ntk: Should the next part move to update() method?
		for (Hero hero : team.getHeroes())
		{
			if (hero == null)
				continue;

			// Hero can heal someone ?
			if (hero.canHeal())
			{

				// Find the weakest hero and heal him
				Hero weakest = team.getHeroes().get(0);
				for (Hero h : team.getHeroes())
				{
					if (h == null)
						continue;

					if (h.getHitPoint().getRatio() < weakest.getHitPoint().getRatio())
						weakest = h;
				}

				if (weakest.getHitPoint().getRatio() < 1.0f)
				{
					GameMessage.addMessage(hero.getName() + " casts healing on " + weakest.getName());
					hero.heal(weakest);
				}
			}
		}
	}

	protected boolean exitSelected(Event event) {
		setClosing(true);
		return true;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public boolean isHealParty() {
		return healParty;
	}

	public void setHealParty(boolean healParty) {
		this.healParty = healParty;
	}

}
