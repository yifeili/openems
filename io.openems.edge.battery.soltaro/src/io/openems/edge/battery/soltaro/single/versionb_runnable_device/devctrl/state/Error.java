package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.SoltaroComponent;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.State;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;

/**
 * This class handles errors occurring
 */
public class Error extends BaseState implements State {

	int maxStartAppempts;
	int startUnsuccessfulDelaySeconds;
	int errorLevel2DelaySeconds;
	private Queue<StateEnum> statesBefore;

	private LocalDateTime startsUnsuccessfulTime;
	private LocalDateTime errorLevel2DelayTime;

	public Error( //
			SoltaroComponent device, //
			int maxStartAttempts, //
			int startUnsuccessfulDelaySeconds, //
			int errorLevel2DelaySeconds //
	) {
		super(device);
		this.maxStartAppempts = maxStartAttempts;
		this.startUnsuccessfulDelaySeconds = startUnsuccessfulDelaySeconds;
		this.errorLevel2DelaySeconds = errorLevel2DelaySeconds;
		this.statesBefore = new LinkedList<>();
	}

	@Override
	public StateEnum getNextState() {

		// No data from the device
		if (this.isDeviceUndefined()) {
			this.resetInternalVariables();
			return StateEnum.UNDEFINED;
		}

		if (this.startsUnsuccessfulTime != null) {
			if (LocalDateTime.now().minusSeconds(this.startUnsuccessfulDelaySeconds).isAfter(startsUnsuccessfulTime)) {
				// waiting period is over
				this.startsUnsuccessfulTime = null;
				return StateEnum.STOPPED;
			} else {
				return StateEnum.ERROR;
			}
		}

		// If the last 'maxStartAppempts' states were 'STARTING'
		// system should remain in error state for 'startUnsuccessfulDelaySeconds'
		// seconds
		if (isMaxStartAttemptsReached()) {
			startsUnsuccessfulTime = LocalDateTime.now();
			return StateEnum.ERROR;
		}

		if (this.errorLevel2DelayTime != null) {
			if (LocalDateTime.now().minusSeconds(this.errorLevel2DelaySeconds).isAfter(errorLevel2DelayTime)) {
				// waiting period is over
				this.errorLevel2DelayTime = null;
				return StateEnum.STOPPED;
			} else {
				return StateEnum.ERROR;
			}
		}

		// If there is an error level 2
		// system should remain in error state for 'errorLevel2DelaySeconds' seconds
		if (this.device.isErrorAlarmLevel2()) {
			errorLevel2DelayTime = LocalDateTime.now();
			return StateEnum.ERROR;
		}

		if (this.device.isStopped()) {
			return StateEnum.STOPPED;
		}

		return null;
	}

	private void resetInternalVariables() {
		this.startsUnsuccessfulTime = null;
		this.errorLevel2DelayTime = null;
		this.statesBefore.clear();
	}

	private boolean isMaxStartAttemptsReached() {
		if (this.statesBefore.size() <= this.maxStartAppempts) {
			return false;
		}

		List<StateEnum> list = new ArrayList<StateEnum>(this.statesBefore);
		for (int i = list.size() - 1; i >= list.size() - this.maxStartAppempts; i--) {
			if (list.get(i) != StateEnum.STARTING) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void act() throws OpenemsException {

	}

	@Override
	public StateEnum getStateEnum() {
		return StateEnum.ERROR;
	}

	@Override
	public void setStateBefore(StateEnum stateBefore) {
		super.setStateBefore(stateBefore);
		this.statesBefore.add(stateBefore);
	}

}
