package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.RunnableDevice;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.State;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;

public class Error extends BaseState implements State {
	


	public Error(RunnableDevice device) {
		super(device);
	}

	@Override
	public StateEnum getNextState() {		
		return null;
	}

	@Override
	public void act() throws OpenemsException {

	}

	@Override
	public StateEnum getStateEnum() {
		return StateEnum.ERROR;	
	}

}
