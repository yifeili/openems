package io.openems.edge.controller.renault.tmh;

import io.openems.common.types.OptionsEnum;

public enum ErrorReset implements OptionsEnum {
	UNDEFINED(-1, "Undefined"),
	DISABLE(0, "Disable"),
	ENABLE(1, "Enable"),
	;

	private final int value;
	private final String name;

	private ErrorReset(int value, String name) {
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

