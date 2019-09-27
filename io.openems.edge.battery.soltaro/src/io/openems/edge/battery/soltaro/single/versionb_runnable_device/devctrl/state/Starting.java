package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.RunnableDevice;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.State;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;

public class Starting extends BaseState implements State {
			
	public Starting(RunnableDevice device) {
		super(device);
	}

	@Override
	public StateEnum getNextState() {
		if (device.isRunning())
			return StateEnum.RUNNING;
 		else if (device.isError())
			return StateEnum.ERROR;
		
		return StateEnum.STARTING;
	}

	@Override
	public void act() throws OpenemsException {
		
	}

	@Override
	public StateEnum getStateEnum() {
		return StateEnum.STARTING;	
	}

}
