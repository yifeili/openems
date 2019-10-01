package io.openems.edge.battery.soltaro.versionb.runnable_device.helper;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.RunnableDevice;

public class ErrorLevel2Device implements RunnableDevice {
	
	@Override
	public void start() throws OpenemsException {
	}

	@Override
	public void stop() throws OpenemsException {
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isError() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isErrorLevel2() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isStopped() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCommunicationAvailable() {
		// TODO Auto-generated method stub
		return true;
	}

}
