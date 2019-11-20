package io.openems.edge.battery.renaultzoe;

import io.openems.common.types.OptionsEnum;

public enum HvBatLevel2Failure implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	NOT_USED(0, "Not used"), //
	NO_DEFAULT(1, "No default"), //
	FAILURE_LEVEL_2_DEFAULT(2, "Failure: level 2 default"), //
	UNAVAILABLE_VALUE(3, "Unavailable Value");
	
	
	private int value;
	private String name;

	private HvBatLevel2Failure(int value, String name) {
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
