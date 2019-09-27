package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state;

import java.util.HashMap;
import java.util.Map;

import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.RunnableDevice;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.State;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;

public class StateController {
	
	private static Map<StateEnum, State> states;
	
	private StateController () {
	}

	
	public static void init(RunnableDevice device) {
		states = new HashMap<>();
		
		states.put(StateEnum.UNDEFINED, new Undefined(device));
		states.put(StateEnum.STOPPED, new Stopped(device));
		states.put(StateEnum.STARTING, new Starting(device));
		states.put(StateEnum.RUNNING, new Running(device));
		states.put(StateEnum.STOPPING, new Stopping(device));
		states.put(StateEnum.STANDBY, new Standby(device));
		states.put(StateEnum.ERROR, new Error(device));
	
	}
		
	public static State getState(StateEnum nextState) {
		return states.get(nextState);
	}
}
