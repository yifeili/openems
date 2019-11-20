package io.openems.edge.airconditioner.envicool;

import io.openems.common.types.OptionsEnum;

public enum WorkState implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	Standby(1, "Standby"), //
	Running(2, "Running"), //
	FAULT(3, "Fault");

	private final int value;
	private final String name;

	private WorkState(int value, String name) {
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
		return FAULT;
	}

}
