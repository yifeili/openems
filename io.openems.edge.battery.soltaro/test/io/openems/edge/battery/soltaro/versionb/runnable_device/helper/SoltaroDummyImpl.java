package io.openems.edge.battery.soltaro.versionb.runnable_device.helper;

import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.SoltaroBMS;

public class SoltaroDummyImpl implements SoltaroBMS {

	private boolean startCommandSet = false;
	private boolean stopCommandSet = false;
	private boolean resetCommandSet = false;
	private boolean sleepCommandSet = false;
	private int watchdog = -1;

	private boolean running;
	private boolean stopped;
	private boolean initiating;
	private boolean errorL2;
	private boolean slaveCommError;
	private boolean commAvailable;
	
	
	
	public SoltaroDummyImpl(boolean running, boolean stopped, boolean initiating, boolean errorL2,
			boolean slaveCommError, boolean commAvailable) {
		super();
		this.running = running;
		this.stopped = stopped;
		this.initiating = initiating;
		this.errorL2 = errorL2;
		this.slaveCommError = slaveCommError;
		this.commAvailable = commAvailable;
	}

	@Override
	public void writeStartCommand() {
		startCommandSet = true;
	}

	@Override
	public void writeStopCommand() {
		stopCommandSet = true;
	}

	@Override
	public void writeSleepCommand() {
		sleepCommandSet = true;
	}

	@Override
	public void writeResetCommand() {
		resetCommandSet = true;
	}

	@Override
	public void writeWatchdog(int seconds) {
		watchdog = seconds;
	}

	public boolean isStartCommandSet() {
		return startCommandSet;
	}

	public boolean isStopCommandSet() {
		return stopCommandSet;
	}

	public boolean isSleepCommandSet() {
		return sleepCommandSet;
	}

	public boolean isResetCommandSet() {
		return resetCommandSet;
	}

	public int getWatchDog() {
		return watchdog;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public boolean isStopped() {
		return stopped;
	}

	@Override
	public boolean isInitiating() {
		return initiating;
	}

	@Override
	public boolean isErrorAlarmLevel2() {
		return errorL2;
	}

	@Override
	public boolean isSlaveCommunicationError() {
		return slaveCommError;
	}

	@Override
	public boolean isCommunicationAvailable() {
		return commAvailable;
	}
}
