package io.openems.edge.battery.renaultzoe;

import io.openems.common.types.OptionsEnum;

public enum OperatingType implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	NO_REQUEST(0, "No Request"), //
	SLOW_CHARGING(1, "Slow Charging"), //
	FAST_CHARGING(2, "Fast Charging"), //
	NORMAL(3, "Normal"),
	QUICK_DROP(4, "Quick Drop"),
	NOT_USED_0(5, "Not used"),
	NOT_USED_1(6, "Not used"),
	UNAVAILABLE_VALUE(7, "Unavailable Value");
	
	
	private int value;
	private String name;

	private OperatingType(int value, String name) {
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
