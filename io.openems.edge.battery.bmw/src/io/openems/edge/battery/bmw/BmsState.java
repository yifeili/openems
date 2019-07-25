package io.openems.edge.battery.bmw;

import io.openems.common.types.OptionsEnum;

public enum BmsState implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	OFF(0, "Off"), //
	INIT(1, "Init"), //
	STANDBY(2, "Standby"),
	READY(3, "Ready"),
	OPERATION(4, "Operation"),
	ERROR(5, "Error"),
	PRE_HEAT(6, "Pre-Heat"),
	PRE_HEAT_COMPLETED(7, "Pre-Heat completed"),
	PRE_CHARGE(8, "Precharge"),
	PRE_CHARGE_COMPLETED(9, "Precharge completed"),
	STATE_UNKNOWN(15, "Unknown undefined")
	;

	private final int value;
	private final String name;

	private BmsState(int value, String name) {
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
