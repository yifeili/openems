package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl;

import io.openems.common.exceptions.OpenemsException;

public interface RunnableDevice {
	
	void start() throws OpenemsException;
	void stop() throws OpenemsException;
	void standby() throws OpenemsException;
	
	boolean isRunning();
	boolean isError();
	boolean isStopped();
	boolean isStandBy();

}
