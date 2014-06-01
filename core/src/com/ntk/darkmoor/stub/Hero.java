package com.ntk.darkmoor.stub;


public class Hero extends Entity {

	public enum HeroClass
	{
		Fighter(0x1),
		Ranger(0x2),
		Paladin(0x4),
		Mage(0x8),
		Cleric(0x10),
		Thief(0x20);
		
		private int value;
		
		private HeroClass(int value) {
			this.value = value;
		}
		
		public int value() {
			return value;
		}

	}
	
	public boolean addToInventory(Item collectItemFromSide) {
		// TODO Auto-generated method stub
		return true;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean canHeal() {
		// TODO Auto-generated method stub
		return false;
	}

	public HitPoint getHitPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	public void heal(Hero weakest) {
		// TODO Auto-generated method stub
		
	}

	public boolean checkClass(Object filter) {
		// TODO Auto-generated method stub
		return false;
	}



}
