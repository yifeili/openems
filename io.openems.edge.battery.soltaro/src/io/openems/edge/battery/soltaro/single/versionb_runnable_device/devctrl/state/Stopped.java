package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.SoltaroComponent;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.State;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;

public class Stopped extends BaseState implements State {

	public Stopped(SoltaroComponent device) {
		super(device);
	}

	@Override
	public StateEnum getNextState() {
		return StateEnum.STARTING;
	}

	@Override
	public void act() throws OpenemsException {
		// Nothing to do 
	}

	@Override
	public StateEnum getStateEnum() {
		return StateEnum.STOPPED;
	}

}
