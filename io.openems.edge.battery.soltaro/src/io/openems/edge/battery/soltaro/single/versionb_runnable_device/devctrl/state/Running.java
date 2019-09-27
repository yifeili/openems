package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.RunnableDevice;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.State;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;

public class Running extends BaseState implements State {
	
	public Running(RunnableDevice device) {
		super(device);
	}

	@Override
	public StateEnum getNextState() {		
		return null;
	}

	@Override
	public void act() throws OpenemsException {

		//do. s.th... if system is running there is nothing to do?!
		
	}

	@Override
	public StateEnum getStateEnum() {
		return StateEnum.RUNNING;	
	}

}
