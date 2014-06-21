package com.ntk.darkmoor.test.general;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	SettingsTest.class,
	HeroTest.class,
	MonsterTest.class,
	ItemTest.class,
	LanguagesManagerTest.class,
	GraphicAssetsTest.class,
	AnimationTest.class,
	SaveLoadGameTest.class,
	MazeElementsTest.class
})
public class DarkmoorTestSuite {

}
