package io.openems.edge.battery.soltaro.single.versionb_runnable_device;

import java.util.Collection;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.CommunicationDevice;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.RunnableDevice;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.SoltaroBMS;
import io.openems.edge.bridge.modbus.api.AbstractOpenemsModbusComponent;
import io.openems.edge.bridge.modbus.api.task.Task;

public class SoltaroComponent implements RunnableDevice, CommunicationDevice {

//	private static final double MAX_TOLERANCE_CELL_VOLTAGE_CHANGES_MILLIVOLT = 50;
//	private static final double MAX_TOLERANCE_CELL_VOLTAGES_MILLIVOLT = 400;

	private SoltaroBMS bms;
	
	

	public SoltaroBMS getBms() {
		return bms;
	}

	public SoltaroComponent(SoltaroBMS bms) {
		this.bms = bms;
	}
	
	@Override
	public boolean isRunning() {
		return bms.isRunning();
	}

	@Override
	public boolean isStopped() {
		return bms.isStopped();
	}

	@Override
	public boolean isError() {
		// Sum all error cases
		return bms.isErrorAlarmLevel2() || bms.isSlaveCommunicationError() || bms.isSlaveCommunicationError();				
	}

	@Override
	public void start() throws OpenemsException {
		// To avoid hardware damages do not send start command if system has already
		// started
		if (bms.isRunning() || bms.isInitiating()) {
			return;
		}
		bms.writeStartCommand();
	}

	@Override
	public void stop() throws OpenemsException {
		// To avoid hardware damages do not send stop command if system has already
		// stopped
		if (bms.isStopped()) {
			return;
		}
		bms.writeStopCommand();
	}

	@Override
	public boolean isCommunicationAvailable() {		
		return bms.isCommunicationAvailable();
	}

	public boolean isErrorAlarmLevel2() {
		return bms.isErrorAlarmLevel2();
	}

	Collection<Task> getTasks(AbstractOpenemsModbusComponent modbusComponent) {
		//TODO cast is unsafe
		return ((SingleRack) bms).getTasks(modbusComponent);  
	}	
}
