package io.openems.edge.controller.renault.tmh;

import io.openems.common.types.OptionsEnum;

public enum SystemStatus implements OptionsEnum {
	UNDEFINED(-1, "Undefined"),
	OFF(0, "Off"),
	ON(50, "On"),
	STANDBY(100, "Standby"),
	STARTUP(150, "Startup"),
	SLEEP(200, "Sleep"),
	FAULT(300, "Fault"),
	;

	private final int value;
	private final String name;

	private SystemStatus(int value, String name) {
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

