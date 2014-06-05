package com.ntk.darkmoor.stub;

import java.util.HashSet;
import java.util.Set;

import org.ntk.commons.StringUtils;

import com.ntk.darkmoor.engine.Attack;
import com.ntk.darkmoor.engine.Entity;
import com.ntk.darkmoor.engine.HitPoint;
import com.ntk.darkmoor.engine.Item;


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

		public static Set<HeroClass> parse(String classList) {
			Set<HeroClass> result = new HashSet<Hero.HeroClass>(6);
			
			if (StringUtils.isEmpty(classList)) {
				return result;
			}
			
			for (HeroClass heroClass: values()) {
				if (classList.contains(heroClass.toString())) {
					result.add(heroClass);
				}
			}
			return result;
		}

	}
	
	public enum HeroHand
	{
		/// Right hand
		Primary(0),

		/// Left hand
		Secondary(1);
		
		private int value;
		
		private HeroHand(int value) {
			this.value = value;
		}
		
		public int value() {
			return value;
		}

		public static Set<HeroHand> parse(String handList) {
			Set<HeroHand> result = new HashSet<HeroHand>(6);
			
			if (StringUtils.isEmpty(handList)) {
				return result;
			}
			
			for (HeroHand hand: values()) {
				if (handList.contains(hand.toString())) {
					result.add(hand);
				}
			}
			return result;
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

	public int getBaseAttackBonus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void hit(Attack attack) {
		// TODO Auto-generated method stub
		
	}



}
