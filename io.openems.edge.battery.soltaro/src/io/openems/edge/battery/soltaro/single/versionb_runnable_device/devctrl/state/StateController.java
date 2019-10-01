package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state;

import java.util.HashMap;
import java.util.Map;

import io.openems.edge.battery.soltaro.single.versionb_runnable_device.Config;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.SoltaroBMS;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.State;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;

public class StateController {
	
	private static Map<StateEnum, State> states;
	
	private StateController () {
	}

	
	public static void init(SoltaroBMS device, Config config) {
		states = new HashMap<>();
		
		states.put(StateEnum.UNDEFINED, new Undefined(device));
		states.put(StateEnum.STOPPED, new Stopped(device));
		states.put(StateEnum.STARTING, new Starting(device, config.maxStartTime()));
		states.put(StateEnum.RUNNING, new Running(device));
		states.put(StateEnum.ERROR, new Error(device, config.maxStartAppempts(), config.startUnsuccessfulDelay(), config.errorLevel2Delay()));
	
	}
		
	public static State getState(StateEnum nextState) {
		return states.get(nextState);
	}
}
