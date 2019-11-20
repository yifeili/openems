package io.openems.edge.battery.renaultzoe;

import io.openems.common.types.OptionsEnum;

public enum HvBatLevel1Failure implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	NOT_USED(0, "Not used"), //
	NO_DEFAULT(1, "No default"), //
	CAUTION_LEVEL_1_DEFAULT(2, "Caution: level 1 default"), //
	UNAVAILABLE_VALUE(3, "Unavailable Value");
	
	
	private int value;
	private String name;

	private HvBatLevel1Failure(int value, String name) {
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
