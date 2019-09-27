package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl;

import io.openems.common.types.OptionsEnum;

public enum StateEnum implements OptionsEnum {
	
	UNDEFINED("Undefined", -1), //
//	PENDING("Pending", 0), //
	STOPPED("Stopped", 1), //
	STARTING("Running", 2), //
	RUNNING("Running", 3), //
	STOPPING("Stopping", 4), //
	ERROR("Error", 5), // 
	STANDBY("Standby", 6), //
	
	;

	private StateEnum(String name, int value) {
		this.name = name;
		this.value = value;
	}

	private int value;
	private String name;

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
		return UNDEFINED;
	}
}
