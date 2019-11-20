package io.openems.edge.battery.renaultzoe;

import io.openems.common.types.OptionsEnum;

public enum HvPowerConnection implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	NOT_USED(0, "Not used"), //
	CLOSING_OF_POWER_CONTACTS_ALLOWED(1, "Closing of Power Contacts Allowed"), //
	CLOSING_OF_POWER_CONTACTS_NOT_ALLOWED(2, "Closing of Power Contacts not Allowed"), //
	UNAVAILABLE_VALUE(3, "Unavailable Value");
	
	
	private int value;
	private String name;

	private HvPowerConnection(int value, String name) {
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
