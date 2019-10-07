package io.openems.edge.battery.soltaro.versionb.runnable_device.helper;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.SoltaroBMS;

public class CommandDevice implements SoltaroBMS {

	private boolean startCommand = false;
	private boolean stopCommand = false;
	
	@Override
	public void start() throws OpenemsException {
		startCommand = true;
	}

	@Override
	public void stop() throws OpenemsException {
		stopCommand = true;
	}

	public boolean isStartCommand() {
		return startCommand;
	}

	public boolean isStopCommand() {
		return stopCommand;
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isError() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStopped() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCommunicationAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isErrorAlarmLevel2() {
		// TODO Auto-generated method stub
		return false;
	}

}
