package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl;

public interface SoltaroBMS {
	
	boolean isErrorAlarmLevel2();
	boolean isSlaveCommunicationError();	
	boolean isCommunicationAvailable();
	
	void writeStartCommand();
	void writeStopCommand();
	void writeSleepCommand();
	void writeResetCommand();
	void writeWatchdog(int seconds);
	
	boolean isRunning();
	boolean isStopped();
	boolean isInitiating();
	
}
