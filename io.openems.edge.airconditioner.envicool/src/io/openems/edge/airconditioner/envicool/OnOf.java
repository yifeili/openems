package io.openems.edge.airconditioner.envicool;

import io.openems.common.types.OptionsEnum;

public enum OnOf implements OptionsEnum {
	UNDEFINED(-1, "Undefined"),
	ON(1, "On"),
	OFF(2, "Off");

	private final int value;
	private final String name;
	
	private OnOf(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	@Override
	public int getValue() {
		return this.value;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public OptionsEnum getUndefined() {
		return this.getUndefined();
	}
}
