package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state;

import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.RunnableDevice;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.State;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;

public abstract class BaseState implements State {
	
	RunnableDevice device;
	private StateEnum stateBefore;
	

	public BaseState(RunnableDevice device) {
		super();
		this.device = device;
	}

	@Override
	public StateEnum getStateBefore() {
		return stateBefore;
	}

	@Override
	public void setStateBefore(StateEnum stateBefore) {
		this.stateBefore = stateBefore;
	}
	

}
