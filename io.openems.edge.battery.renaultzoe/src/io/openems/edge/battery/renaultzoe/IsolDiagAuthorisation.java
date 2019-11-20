package io.openems.edge.battery.renaultzoe;

import io.openems.common.types.OptionsEnum;

public enum IsolDiagAuthorisation implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	NOT_USED(0, "Not used"), //
	DIAG_OF_ISOLATION_ALLOWED(1, "Diag of Isolation Allowed"), //
	DIAG_OF_ISOLATION_NOT_ALLOWED(2, "Diag of Isolation not Allowed"), //
	UNAVAILABLE_VALUE(3, "Unavailable Value");
	
	
	private int value;
	private String name;

	private IsolDiagAuthorisation(int value, String name) {
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
