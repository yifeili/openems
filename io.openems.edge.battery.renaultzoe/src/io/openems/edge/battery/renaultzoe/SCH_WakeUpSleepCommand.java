package io.openems.edge.battery.renaultzoe;

import io.openems.common.types.OptionsEnum;

public enum SCH_WakeUpSleepCommand implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	GO_TO_SLEEP(0, "Go to Sleep"), //
	RESERVED_0(1, "reserved"), //
	RESERVED_1(2, "reserved"), //
	WAKE_UP(3, "Wake Up");
	
	
	private int value;
	private String name;

	private SCH_WakeUpSleepCommand(int value, String name) {
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
