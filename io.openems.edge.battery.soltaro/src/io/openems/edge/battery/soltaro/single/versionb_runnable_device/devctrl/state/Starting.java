package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state;

import java.time.LocalDateTime;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.RunnableDevice;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.State;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;

public class Starting extends BaseState implements State {
			
	// Time when starting has started has to be set
	LocalDateTime startTime = null;
	int maxTimeToStartInSeconds;
	
	public Starting(RunnableDevice device, int maxTimeToStartInSeconds) {
		super(device);
		this.maxTimeToStartInSeconds = maxTimeToStartInSeconds;
	}

	@Override
	public StateEnum getNextState() {
		if (startTime == null) {
			startTime = LocalDateTime.now();
		}
		
		StateEnum nextState = StateEnum.STARTING;
		
		if (LocalDateTime.now().minusSeconds(this.maxTimeToStartInSeconds).isAfter(startTime)) {
			nextState = StateEnum.ERROR;
		} else if (device.isRunning()) {
			nextState = StateEnum.RUNNING;
		} else if (device.isError()) {
			nextState = StateEnum.ERROR;
		}
		
		if (nextState != StateEnum.STARTING) {
			this.startTime = null;
		}
		
		return nextState;
	}

	@Override
	public void act() throws OpenemsException {
	
	}

	@Override
	public StateEnum getStateEnum() {
		return StateEnum.STARTING;	
	}

}
