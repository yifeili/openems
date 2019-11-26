package io.openems.edge.ess.sinexcel;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.types.OptionsEnum;

public class GoingOffgridHandler {

	private final Logger log = LoggerFactory.getLogger(GoingOffgridHandler.class);
	private final StateMachine parent;

	private State state = State.UNDEFINED;

	// WAIT
	private LocalDateTime startedWaiting = null;
	private final static int WAIT_SECONDS = 50;

	public GoingOffgridHandler(StateMachine parent) {
		this.parent = parent;
	}

	public void initialize() {
		this.state = State.UNDEFINED;
		this.startedWaiting = null;
	}

	protected StateMachine.State run() throws OpenemsNamedException {
		switch (this.state) {
		case UNDEFINED:
			this.state = this.doUndefined();
			break;

		case WAIT:
			this.state = this.doWait();
			break;

		case FINISH_GOING_OFFGRID:
			// finish GoingOffgridHandler, switch to OFFGRID-State
			this.initialize();
			return StateMachine.State.OFFGRID;
		}

		return StateMachine.State.GOING_OFFGRID;
	}

	/**
	 * Handle UNDEFINED, i.e. GoingOffgridHandler just started taking over. Starts
	 * with WAIT-State.
	 * 
	 * @return the next state
	 */
	private State doUndefined() {
		return State.WAIT;
	}

	/**
	 * Handle WAIT. Waits WAIT_SECONDS, then switches to FINISH_GOING_OFFGRID
	 * 
	 * @return the next state
	 * @throws OpenemsNamedException
	 */
	private State doWait() throws OpenemsNamedException {
		if (this.startedWaiting == null) {
			this.startedWaiting = LocalDateTime.now();
		}

		/*
		 * To switch the PCS to off-grid mode, requires a stop command to the PCS, and a
		 * physical disconnection to the utility before the PCS can be switched to
		 * off-grid mode, followed with a start command.
		 * 
		 */
		if (this.startedWaiting.plusSeconds(WAIT_SECONDS).isAfter(LocalDateTime.now())) {
			this.log.info("doWaitFirstSeconds() waiting the first seconds, sending the stop command");
			this.parent.parent.inverterOff();
			return State.WAIT;
		}

		// finished waiting
		this.log.info("finished waiting, setting the grid to offgrid state and sending the start command");
		this.parent.parent.hardSetGridOffMode();
		this.parent.parent.inverterOn();
		return State.FINISH_GOING_OFFGRID;
	}

	public enum State implements OptionsEnum {
		UNDEFINED(-1, "Undefined"), //
		WAIT(1, "For the first seconds just wait"), //
		FINISH_GOING_OFFGRID(2, "Finish Going Off-Grid"); //

		private final int value;
		private final String name;

		private State(int value, String name) {
			this.value = value;
			this.name = name;
		}

		@Override
		public int getValue() {
			return value;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public OptionsEnum getUndefined() {
			return UNDEFINED;
		}
	}
}
