package com.ntk.darkmoor.engine;

import com.badlogic.gdx.math.Rectangle;

public class InterfaceCoordinates {

	// Main window

	static public Rectangle TurnLeft = new Rectangle(10, 256, 38, 34);

	static public Rectangle MoveForward = new Rectangle(48, 256, 40, 34);

	static public Rectangle TurnRight = new Rectangle(88, 256, 40, 34);

	static public Rectangle MoveLeft = new Rectangle(10, 290, 38, 34);

	static public Rectangle MoveBackward = new Rectangle(48, 290, 40, 34);

	static public Rectangle MoveRight = new Rectangle(88, 290, 40, 34);

	//@formatter:off
	static public Rectangle[] SelectHero = new Rectangle[]
	{
		new Rectangle(368      ,       2, 126, 20),		// Hero 1
		new Rectangle(368 + 144,       2, 126, 20),		// Hero 2

		new Rectangle(368      , 104 + 2, 126, 20),		// Hero 3
		new Rectangle(368 + 144, 104 + 2, 126, 20),		// Hero 4

		new Rectangle(368      , 208 + 2, 126, 20),		// Hero 5
		new Rectangle(368 + 144, 208 + 2, 126, 20),		// Hero 6
	};


	static public Rectangle[] HeroFace = new Rectangle[]
	{
		new Rectangle(368      ,       22, 64, 64),		// Hero 1
		new Rectangle(368 + 144,       22, 64, 64),		// Hero 2

		new Rectangle(368      , 104 + 22, 64, 64),		// Hero 3
		new Rectangle(368 + 144, 104 + 22, 64, 64),		// Hero 4
		
		new Rectangle(368      , 208 + 22, 64, 64),		// Hero 5
		new Rectangle(368 + 144, 208 + 22, 64, 64),		// Hero 6
	};


	static public Rectangle[] PrimaryHand = new Rectangle[]
	{
		new Rectangle(432      ,       22, 62, 32),		// Hero 1
		new Rectangle(432 + 144,       22, 62, 32),		// Hero 2

		new Rectangle(432      , 104 + 22, 62, 32),		// Hero 3
		new Rectangle(432 + 144, 104 + 22, 62, 32),		// Hero 4

		new Rectangle(432      , 208 + 22, 62, 32),		// Hero 5
		new Rectangle(432 + 144, 208 + 22, 60, 32),		// Hero 6
	};


	static public Rectangle[] SecondaryHand = new Rectangle[]
	{
		new Rectangle(432      ,       54, 62, 32),		// Hero 1
		new Rectangle(432 + 144,       54, 62, 32),		// Hero 2

		new Rectangle(432      , 104 + 54, 62, 32),		// Hero 3
		new Rectangle(432 + 144, 104 + 54, 62, 32),		// Hero 4

		new Rectangle(432      , 208 + 54, 62, 32),		// Hero 5
		new Rectangle(432 + 144, 208 + 54, 62, 32),		// Hero 6
	};



	// Inventory screen

	static public Rectangle[] BackPack = new Rectangle[]
	{
		new Rectangle(358     , 76     ,  36, 36),
		new Rectangle(358 + 36, 76     ,  36, 36),
		new Rectangle(358     , 76 + 36,  36, 36),
		new Rectangle(358 + 36, 76 + 36,  36, 36),
		new Rectangle(358     , 76 + 72,  36, 36),
		new Rectangle(358 + 36, 76 + 72,  36, 36),
		new Rectangle(358     , 76 + 108, 36, 36),
		new Rectangle(358 + 36, 76 + 108, 36, 36),
		new Rectangle(358     , 76 + 144, 36, 36),
		new Rectangle(358 + 36, 76 + 144, 36, 36),
		new Rectangle(358     , 76 + 180, 36, 36),
		new Rectangle(358 + 36, 76 + 180, 36, 36),
		new Rectangle(358     , 76 + 216, 36, 36),
		new Rectangle(358 + 36, 76 + 216, 36, 36),
	};

	static public Rectangle[] Rings = new Rectangle[]
	{
		new Rectangle(452     , 268, 20, 20),
		new Rectangle(452 + 24, 268, 20, 20),
	};

	static public Rectangle[] Waist = new Rectangle[]
	{
		new Rectangle(596, 184     , 36, 36),
		new Rectangle(596, 184 + 36, 36, 36),
		new Rectangle(596, 184 + 72, 36, 36),
	};
	//@formatter:on

	static public Rectangle CloseInventory = new Rectangle(360, 4, 64, 64);

	static public Rectangle ShowStatistics = new Rectangle(602, 296, 36, 36);

	static public Rectangle PreviousHero = new Rectangle(546, 68, 40, 30);

	static public Rectangle NextHero = new Rectangle(592, 68, 40, 30);

	static public Rectangle Quiver = new Rectangle(446, 108, 36, 36);

	static public Rectangle Armor = new Rectangle(444, 148, 36, 36);

	static public Rectangle Food = new Rectangle(470, 72, 62, 30);

	static public Rectangle Wrist = new Rectangle(446, 188, 36, 36);

	static public Rectangle PrimaryHandInventory = new Rectangle(456, 228, 36, 36);

	static public Rectangle Feet = new Rectangle(550, 270, 36, 36);

	static public Rectangle SecondaryHandInventory = new Rectangle(552, 228, 36, 36);

	static public Rectangle Neck = new Rectangle(570, 146, 36, 36);

	static public Rectangle Head = new Rectangle(592, 106, 36, 36);

}
