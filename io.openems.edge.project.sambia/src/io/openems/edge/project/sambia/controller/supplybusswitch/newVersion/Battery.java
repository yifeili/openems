package io.openems.edge.project.sambia.controller.supplybusswitch.newVersion;

import io.openems.common.types.OptionsEnum;

enum Battery implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	ESS1(0, "Ess1"), //
	ESS2(1, "Ess2"), //
	ESS3(2, "Ess3"), //
	ESS4(3, "Ess4");

	private final int value;
	private final String name;

	private Battery(int value, String name) {
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