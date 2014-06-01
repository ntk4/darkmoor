package com.ntk.darkmoor.engine.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MessageBox2 extends Window {
	private MessageBoxStyle style;
	private Label labelQuestion;
	private TextButton buttonYes;
	private TextButton buttonNo;

	public MessageBox2(String title, String question, BitmapFont textFont, Skin skin) {
		super(title, skin);

		labelQuestion = new Label(question, new Label.LabelStyle(textFont, Color.WHITE));

		buttonYes = new TextButton("yes", skin);
		buttonYes.setWidth(buttonYes.getWidth() * 2.5f);
		buttonYes.setHeight(buttonYes.getHeight() * 1.5f);

		buttonNo = new TextButton("no", skin);
		buttonNo.setX(2 * buttonYes.getWidth());
		buttonNo.setWidth(buttonYes.getWidth());
		buttonNo.setHeight(buttonYes.getHeight());

		buttonNo.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				setVisible(false);
			}
		});

		buttonYes.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				setVisible(false);
			}
		});

//		labelQuestion.setX(getPadLeft().width(this));
//		labelQuestion.setY(1.3f * buttonYes.getHeight());
//
//		setWidth(labelQuestion.getWidth() + getPadLeft().width(this) + getPadRight().width(this));
//		setHeight(labelQuestion.getHeight() + buttonNo.getHeight() + getPadTop().height(this)
//				+ getPadBottom().height(this));
//
//		buttonYes.translate(getWidth() / 2 - 1.5f * buttonYes.getWidth(), 0);
//		buttonNo.translate(getWidth() / 2 - 1.5f * buttonYes.getWidth(), 0);
//
//		buttonYes.translate(0, getPadBottom().height(this));
//		buttonNo.translate(0, getPadBottom().height(this));

		addActor(labelQuestion);
		addActor(buttonYes);
		addActor(buttonNo);

		setVisible(false);
	}

	public void setStyle(MessageBoxStyle style) {
		if (style == null)
			throw new IllegalArgumentException("style cannot be null.");
		this.style = style;
		// this.getStyle().titleFont = style.font;
		// this.getStyle().titleFontColor = style.fontColor;
		labelQuestion.getStyle().font = style.font;
		labelQuestion.setColor(style.fontColor);
		invalidateHierarchy();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (isVisible()) {
			super.draw(batch, parentAlpha);
		}
	}

	public void show() {
		setVisible(true);
	}

	public void hide() {
		setVisible(false);
	}

	public void addPositiveListener(EventListener listener) {
		buttonYes.addListener(listener);
	}

	public void addNegativeListener(EventListener listener) {
		buttonNo.addListener(listener);
	}

	static public class MessageBoxStyle {
		public BitmapFont font;

		/** Optional. */
		public Color fontColor;

		public MessageBoxStyle() {
		}

		public MessageBoxStyle(BitmapFont font, Color fontColor) {
			this.font = font;
			this.fontColor = fontColor;
		}

		public MessageBoxStyle(MessageBoxStyle style) {
			this.font = style.font;
			if (style.fontColor != null)
				this.fontColor = new Color(style.fontColor);
		}
	}
}