package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl;

public interface SoltaroBMS extends RunnableDevice, CommunicationDevice {
	
	boolean isErrorAlarmLevel2();
	
//	void writeStartCommand();
//	void writeStopCommand();
//	void writeSleepCommand();
//	void writeResetCommand();
//	void writeWatchdog(int seconds);
//	.....

}
