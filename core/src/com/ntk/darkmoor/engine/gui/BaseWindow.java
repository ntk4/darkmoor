package com.ntk.darkmoor.engine.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ntk.darkmoor.engine.CampDialog;

public class BaseWindow extends Dialog {

	private CampDialog camp;
	private boolean closing;
	private MessageBox messageBox;
	protected TextButton[] buttons;

	protected Stage stage;
	protected Skin uiSkin;

	public BaseWindow(CampDialog camp, String title, Skin skin, Stage stage) {
		super(title, skin);
		this.camp = camp;
		closing = false;
		uiSkin = skin;
		this.stage = stage;
	}

	public void close() {
		closing = true;
	}

	public void update(float delta) {
		if (messageBox != null && !messageBox.isClosing())
			messageBox = null;

		if (messageBox != null)
			messageBox.update(delta);
		else
			updateButtons(delta, buttons);
	}

	protected void setupButtons(Skin uiSkin) {

	}

	private void updateButtons(float delta, TextButton[] buttons2) {
		// update message box
		if (messageBox != null) {
			messageBox.update(delta);

			return;
		}
	}

	public CampDialog getCamp() {
		return camp;
	}

	public void loadContent() {
		setupButtons(uiSkin);
	}

	protected Image setupBackground() {
		return null;
	}

	public void unloadContent() {
		buttons = null;
	}


	protected void setCamp(CampDialog camp) {
		this.camp = camp;
	}

	public boolean isClosing() {
		return closing;
	}

	public void setClosing(boolean closing) {
		this.closing = closing;
	}

	public MessageBox getMessageBox() {
		return messageBox;
	}

	protected void setMessageBox(MessageBox messageBox) {
		this.messageBox = messageBox;
	}

}
