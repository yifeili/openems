package io.openems.edge.battery.renaultzoe;

import io.openems.common.types.OptionsEnum;

public enum ETS_SleepMode implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	NORMAL_SLEEPING(0, "Normal Sleeping"), //
	BATTERY_MANAGEMENT_14V(1, "14V Battery Management"), //
	QUICK_DROP(2, "Quick Drop"), //
	UNAVAILABLE_VALUE(3, "Unavailable Value");
	
	
	private int value;
	private String name;

	private ETS_SleepMode(int value, String name) {
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
