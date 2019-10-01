package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl;

public interface SoltaroBMS extends RunnableDevice, CommunicationDevice {
	
	boolean isErrorLevel2();

}
