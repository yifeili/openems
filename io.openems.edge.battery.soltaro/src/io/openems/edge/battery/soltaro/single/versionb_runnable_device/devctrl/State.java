package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl;

import io.openems.common.exceptions.OpenemsException;

public interface State {
	
	StateEnum getStateBefore();
	void setStateBefore(StateEnum stateEnum);
	StateEnum getNextState();
	void act() throws OpenemsException;
	StateEnum getStateEnum();
	
}
