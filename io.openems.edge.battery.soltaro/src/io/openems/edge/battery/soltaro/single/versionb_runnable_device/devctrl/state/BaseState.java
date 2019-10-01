package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state;

import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.SoltaroBMS;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.State;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;

public abstract class BaseState implements State {
	
	SoltaroBMS device;
	private StateEnum stateBefore;
	

	public BaseState(SoltaroBMS device) {
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
	
	protected boolean isDeviceUndefined() {
		if (this.device == null) {
			return true;
		}
		
		if (!this.device.isCommunicationAvailable()) {
			return true;
		}
		
		return !(this.device.isError() || this.device.isRunning() || this.device.isStopped());
	}

}
