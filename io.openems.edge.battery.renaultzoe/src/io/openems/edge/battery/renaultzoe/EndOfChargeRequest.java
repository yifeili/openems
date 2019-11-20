package io.openems.edge.battery.renaultzoe;

import io.openems.common.types.OptionsEnum;

public enum EndOfChargeRequest implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	NOT_USED(0, "Not used"), //
	END_OF_CHARGE_NOT_REQUEST(1, "End of Charge Not Requested"), //
	END_OF_CHARGE_REQUEST(2, "End of Charge Requested"), //
	UNAVAILABLE_VALUE(3, "Unavailable Value"); //
	
	private int value;
	private String name;

	private EndOfChargeRequest(int value, String name) {
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
