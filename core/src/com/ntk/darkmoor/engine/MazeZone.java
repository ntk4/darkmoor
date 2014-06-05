package com.ntk.darkmoor.engine;

import java.io.IOException;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

public class MazeZone {

	public static final String TAG = "zone";
	private Rectangle rectangle;
	private String scriptName;
	private String onTeamLeaveScript;
	private String onTeamEnterScript;
	private String onMonsterLeaveScript;
	private String onMonsterEnterScript;
	private String onUpdateScript;
	private String onDrawScript;
	private String name;
	private boolean hide;

	public MazeZone() {
		rectangle = new Rectangle();
	}

	public boolean load(Element xml) {
		if (xml == null || !TAG.equalsIgnoreCase(xml.getName()))
			return false;

		Element node = null;
		String name = null;
		for (int i = 0; i < xml.getChildCount(); i++) {
			node = xml.getChild(i);
			name = node.getName();
			
			if ("script".equalsIgnoreCase(name)) {
				scriptName = node.getAttribute("name");

			} else if ("onteamleave".equalsIgnoreCase(name)) {
				onTeamLeaveScript = node.getAttribute("name");
			} else if ("onteamenter".equalsIgnoreCase(name)) {
				onTeamEnterScript = node.getAttribute("name");
			} else if ("onmonsterleave".equalsIgnoreCase(name)) {
				onMonsterLeaveScript = node.getAttribute("name");
			} else if ("onmonsterenter".equalsIgnoreCase(name)) {
				onMonsterEnterScript = node.getAttribute("name");
			} else if ("onupdate".equalsIgnoreCase(name)) {
				onUpdateScript = node.getAttribute("name");
			} else if ("ondraw".equalsIgnoreCase(name)) {
				onDrawScript = node.getAttribute("name");
			}

		}

		return true;
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG).attribute("name", name);

		writer.element("rectangle").attribute("x", rectangle.x).attribute("y", rectangle.y)
				.attribute("width", rectangle.width).attribute("height", rectangle.height).pop();

		writer.element("script").attribute("name", scriptName).pop();

		writer.element("onteamenter").attribute("name", onTeamEnterScript).pop();

		writer.element("onteamleave").attribute("name", onTeamLeaveScript).pop();

		writer.element("onmonsterenter").attribute("name", onMonsterEnterScript).pop();

		writer.element("onmonsterleave").attribute("name", onMonsterLeaveScript).pop();

		writer.element("onupdate").attribute("name", onUpdateScript).pop();

		writer.element("ondraw").attribute("name", onDrawScript).pop();

		writer.pop();

		return true;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public String getOnTeamLeaveScript() {
		return onTeamLeaveScript;
	}

	public void setOnTeamLeaveScript(String onTeamLeaveScript) {
		this.onTeamLeaveScript = onTeamLeaveScript;
	}

	public String getOnTeamEnterScript() {
		return onTeamEnterScript;
	}

	public void setOnTeamEnterScript(String onTeamEnterScript) {
		this.onTeamEnterScript = onTeamEnterScript;
	}

	public String getOnMonsterLeaveScript() {
		return onMonsterLeaveScript;
	}

	public void setOnMonsterLeaveScript(String onMonsterLeaveScript) {
		this.onMonsterLeaveScript = onMonsterLeaveScript;
	}

	public String getOnMonsterEnterScript() {
		return onMonsterEnterScript;
	}

	public void setOnMonsterEnterScript(String onMonsterEnterScript) {
		this.onMonsterEnterScript = onMonsterEnterScript;
	}

	public String getOnUpdateScript() {
		return onUpdateScript;
	}

	public void setOnUpdateScript(String onUpdateScript) {
		this.onUpdateScript = onUpdateScript;
	}

	public String getOnDrawScript() {
		return onDrawScript;
	}

	public void setOnDrawScript(String onDrawScript) {
		this.onDrawScript = onDrawScript;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}

}
