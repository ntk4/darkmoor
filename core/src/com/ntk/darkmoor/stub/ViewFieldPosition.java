package com.ntk.darkmoor.stub;

public enum ViewFieldPosition {
	A(0), B(1), C(2), D(3), E(4),

	F(5), G(6), H(7), I(8), J(9),

	K(10), L(11), M(12),

	N(13), Team(14), O(15);

	
	private ViewFieldPosition(int value) {
		this.value = value;
	}
	
	private int value;
	
	public int getValue() {
		return value;
	}
}
