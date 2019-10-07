package io.openems.edge.battery.soltaro.versionb.runnable_device.helper;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.SoltaroBMS;

public class DeviceFactory {

	private DeviceFactory() {}
	
	public static SoltaroBMS getRunningDevice() {
		return new SoltaroBMS() {
			@Override public void stop() throws OpenemsException {}
			@Override public void start() throws OpenemsException {}
			@Override public boolean isStopped() { return false; }
			@Override public boolean isRunning() { return true; }
			@Override public boolean isError() { return false; }
			@Override public boolean isCommunicationAvailable() { return true; }
			@Override public boolean isErrorAlarmLevel2() { return false; }
		};
	}
	
	public static SoltaroBMS getStoppedDevice() {
		return new SoltaroBMS() {
			@Override public void stop() throws OpenemsException {}
			@Override public void start() throws OpenemsException {}
			@Override public boolean isStopped() { return true; }
			@Override public boolean isRunning() { return false; }
			@Override public boolean isError() { return false; }
			@Override public boolean isCommunicationAvailable() { return true; }
			@Override public boolean isErrorAlarmLevel2() { return false; }
		};
	}
	
	public static SoltaroBMS getErrorDevice() {
		return new SoltaroBMS() {
			@Override public void stop() throws OpenemsException {}
			@Override public void start() throws OpenemsException {}
			@Override public boolean isStopped() { return false; }
			@Override public boolean isRunning() { return false; }
			@Override public boolean isError() { return true; }
			@Override public boolean isCommunicationAvailable() { return true; }
			@Override public boolean isErrorAlarmLevel2() { return true; }
		};
	}
	
	public static SoltaroBMS getUndefinedDevice() {
		return new SoltaroBMS() {
			@Override public void stop() throws OpenemsException {}
			@Override public void start() throws OpenemsException {}
			@Override public boolean isStopped() { return false; }
			@Override public boolean isRunning() { return false; }
			@Override public boolean isError() { return false; }
			@Override public boolean isCommunicationAvailable() { return false; }
			@Override public boolean isErrorAlarmLevel2() { return false; }
		};
	}

	public static CommandDevice getCommandDevice() {
		return new CommandDevice();
	}
	
}