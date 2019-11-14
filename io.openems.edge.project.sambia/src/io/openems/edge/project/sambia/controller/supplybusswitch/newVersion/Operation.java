package io.openems.edge.project.sambia.controller.supplybusswitch.newVersion;

import io.openems.common.types.OptionsEnum;

enum Operation implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	CLOSE(0, "Ess1"), //
	OPEN(1, "Ess2");

	private final int value;
	private final String name;

	private Operation(int value, String name) {
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