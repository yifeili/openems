package io.openems.edge.battery.soltaro.versionb.runnable_device.helper;

import io.openems.edge.battery.soltaro.single.versionb_runnable_device.SoltaroComponent;

public class DeviceFactory {

	private DeviceFactory() {}
	
	public static SoltaroComponent getRunningDevice() {
		return new SoltaroComponent(
				new SoltaroDummyImpl(true, false, false, false, false, true)
		);
			
	}
	
	public static SoltaroComponent getStoppedDevice() {
		return new SoltaroComponent(
				new SoltaroDummyImpl(false, true, false, false, false, true)
		);
	}
	
	public static SoltaroComponent getErrorDevice() {
		return new SoltaroComponent(
				new SoltaroDummyImpl(false, false, false, true, true, true)
		);
	}
	
	public static SoltaroComponent getUndefinedDevice() {
		return new SoltaroComponent(
				new SoltaroDummyImpl(false, false, false, false, false, false)
		);
	}
	
}