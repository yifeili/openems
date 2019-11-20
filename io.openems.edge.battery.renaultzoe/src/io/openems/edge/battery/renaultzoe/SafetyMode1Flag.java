package io.openems.edge.battery.renaultzoe;

import io.openems.common.types.OptionsEnum;

public enum SafetyMode1Flag implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	NOT_USED(0, "Not used"), //
	NO_SAFETY_MODE(1, "No Safety Mode"), //
	SAFETY_MODE_1_REQUEST(2, "Safety Mode 1 Request"), //
	UNAVAILABLE_VALUE(3, "Unavailable Value");
	
	
	private int value;
	private String name;

	private SafetyMode1Flag(int value, String name) {
		this.value = value;
		this.name = name;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public OptionsEnum getUndefined() {
		return UNDEFINED;
	}
}
