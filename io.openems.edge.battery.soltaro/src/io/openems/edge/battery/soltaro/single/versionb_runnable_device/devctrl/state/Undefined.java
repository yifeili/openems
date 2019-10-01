package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.SoltaroBMS;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.State;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;

public class Undefined extends BaseState implements State {
	
	public Undefined(SoltaroBMS device) {
		super(device);
	}

	@Override
	public StateEnum getNextState() {
		if (device.isRunning()) {
			return StateEnum.RUNNING;
		} else if (device.isStopped()) {
			return StateEnum.STOPPED;
		} else if (device.isError()) {
			return StateEnum.ERROR;
		}
		
		return StateEnum.UNDEFINED;
	}

	@Override
	public void act() throws OpenemsException {

	}

	@Override
	public StateEnum getStateEnum() {
		return StateEnum.UNDEFINED;	
	}

}
