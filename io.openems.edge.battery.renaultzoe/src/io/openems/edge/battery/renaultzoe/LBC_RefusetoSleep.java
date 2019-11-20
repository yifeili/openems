package io.openems.edge.battery.renaultzoe;

import io.openems.common.types.OptionsEnum;

public enum LBC_RefusetoSleep implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	RESERVED_0(0, "Reserved"), //
	REFUSE_TO_SLEEP(1, "LBC refuse to sleep"), //
	READY_TO_SLEEP(2, "LBC ready to sleep"), //
	RESERVED_1(3, "Reserved");
	
	
	private int value;
	private String name;

	private LBC_RefusetoSleep(int value, String name) {
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
