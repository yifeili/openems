package io.openems.edge.battery.renaultzoe;

import io.openems.common.types.OptionsEnum;

public enum HvBatState implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	NOT_USED(0, "Not used"), //
	SLOW_CHARGE(1, "Slow Charge"), //
	FAST_CHARGE(2, "Fast Charge"), //
	INIT(3, "Init"),
	TRANSITORY(4, "Transitory"),
	NORMAL(5, "Normal"),
	QUICK_DROP(6, "QuickDrop"),
	UNAVAILABLE_VALUE(7, "Unavailable Value"); //
	
	private int value;
	private String name;

	private HvBatState(int value, String name) {
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
