package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.SoltaroBMS;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.State;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;

public class Running extends BaseState implements State {
	
	public Running(SoltaroBMS device) {
		super(device);
	}

	@Override
	public StateEnum getNextState() {		
		if (this.isDeviceUndefined()) {
			return StateEnum.UNDEFINED;
		}
		
		if (this.device.isError()) {
			return StateEnum.ERROR;
		}
		
		return StateEnum.RUNNING;
	}

	@Override
	public void act() throws OpenemsException {

		
		//do. s.th... if system is running there is nothing to do?!
		// check cell voltages, print warning, reduce max power....
		
	}

	@Override
	public StateEnum getStateEnum() {
		return StateEnum.RUNNING;	
	}

}
