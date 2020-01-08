package io.openems.edge.battery.renaultzoe;

import io.openems.common.types.OptionsEnum;

public enum PowerRelayState implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	PRECHARGE(0, "Precharge"), //
	CLOSED(1, "Closed"), //
	OPENED(2, "Opened"), //
	TRANSITORY_STATE(3, "Transitory State");
	
	
	private int value;
	private String name;

	private PowerRelayState(int value, String name) {
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
