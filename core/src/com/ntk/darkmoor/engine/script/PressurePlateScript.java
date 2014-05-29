package com.ntk.darkmoor.engine.script;

import java.io.IOException;

import org.ntk.commons.StringUtils;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

public class PressurePlateScript extends ScriptBase {

	private static final String TAG = "PressurePlateScript";

	public enum PressurcePlateCondition {

		/** On team stepping on the switch will activate it */
		OnTeamEnter(0x01),

		/** On team stepping off the switch will activate it */
		OnTeamLeave(0x02),

		/** On monsters stepping on the switch will activate it */
		OnMonsterEnter(0x04),

		/** On monsters stepping off the switch will activate it */
		OnMonsterLeave(0x08),

		/** On item adding, the switch will activate it */
		OnItemAdded(0x10),

		/** On item removing, the switch will activate it */
		OnItemRemoved(0x20),

		/** On item adding or removing, the switch will activate it */
		OnItem(OnItemAdded.weight | OnItemRemoved.weight),

		/** On monsters stepping on or off the switch will activate it */
		OnMonster(OnMonsterEnter.weight | OnMonsterLeave.weight),

		/** On team stepping on or off the switch will activate it */
		OnTeam(OnTeamEnter.weight | OnTeamLeave.weight),

		/** Everything triggers the switch. */
		Always(OnTeam.weight | OnMonster.weight | OnItem.weight),

		/** On team, monsters or items stepping on the switch will activate it */
		OnEnter(OnTeamEnter.weight | OnMonsterEnter.weight | OnItemAdded.weight),

		/** On team, monsters or items stepping off the switch will activate it */
		OnLeave(OnTeamLeave.weight | OnMonsterLeave.weight | OnItemRemoved.weight),

		/** On team or monsters stepping on or off the switch will activate it */
		OnEntity(OnTeam.weight | OnMonster.weight),

		/** On team or monsters stepping on the switch will activate it */
		OnEntityEnter(OnTeamEnter.weight | OnMonsterEnter.weight),

		/** On team or monsters stepping off the switch will activate it */
		OnEntityLeave(OnTeamLeave.weight | OnMonsterLeave.weight);

		public final int weight;

		PressurcePlateCondition(Integer weight) {
			this.weight = weight;
		}

	}

	private PressurcePlateCondition condition;

	public PressurePlateScript() {
		condition = PressurcePlateCondition.Always;
	}

	public boolean load(XmlReader.Element xml) {
		if (xml == null)
			return false;

		if (StringUtils.equals("condition", xml.getName())) {
			condition = PressurcePlateCondition.valueOf(xml.getText());
		} else {
			super.load(xml);
		}

		return true;
	}

	public boolean save(XmlWriter writer) throws IOException {
		if (writer == null)
			return false;

		writer.element(TAG);

		writer.element("condition", condition.toString()).pop();

		super.save(writer);

		writer.pop();

		return true;
	}

	public PressurcePlateCondition getCondition() {
		return condition;
	}

	public void setCondition(PressurcePlateCondition condition) {
		this.condition = condition;
	}

}
