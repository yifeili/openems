package io.openems.edge.ess.sinexcel;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.common.sum.GridMode;
import io.openems.edge.ess.sinexcel.StateMachine.State;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class OngridHandler {
	// private final Logger log = LoggerFactory.getLogger(OngridHandler.class);
	private final StateMachine parent;

	public OngridHandler(StateMachine parent) {
		this.parent = parent;
	}

	protected StateMachine.State run() throws IllegalArgumentException, OpenemsNamedException {
		System.out.println("Inside ongrid run");
		GridMode gridMode = this.parent.parent.getGridMode().getNextValue().asEnum();
		switch (gridMode) {
		case ON_GRID:
			return this.doOngrid();
		case UNDEFINED:
			return this.doOngrid();
		case OFF_GRID:
			return StateMachine.State.GOING_OFFGRID;
		}
		return StateMachine.State.ONGRID;
	}

	private State doOngrid() throws OpenemsNamedException {

		CurrentState currentState = this.parent.getSinexcelState();
		GridMode gridMode = this.parent.parent.getGridMode().getNextValue().asEnum();

		switch (currentState) {
		case UNDEFINED:
		case SLEEPING:
		case MPPT:
		case THROTTLED:
		case STARTED:
			this.parent.parent.softStart(true);
			break;
		case SHUTTINGDOWN:
		case FAULT:
		case STANDBY:
		case OFF:
		default:
			this.parent.parent.softStart(false);

			switch (gridMode) {
			case ON_GRID:
				return State.ONGRID;
			case UNDEFINED:
				return State.UNDEFINED;
			case OFF_GRID:
				return State.OFFGRID;
			}
		}
		return State.ONGRID;
	}

	// Assumptions : undefined and ongrid mode is same, as there is only one
	// operations, which is to Softstart()

}
