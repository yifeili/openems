package io.openems.edge.ess.sinexcel;

//import java.util.Optional;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

//import io.openems.common.exceptions.OpenemsException;
import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.common.sum.GridMode;
import io.openems.edge.ess.sinexcel.StateMachine.State;

public class OffgridHandler {

//	private final Logger log = LoggerFactory.getLogger(OngridHandler.class);
	private final StateMachine parent;


	public OffgridHandler(StateMachine parent) {
		this.parent = parent;
	}

	protected StateMachine.State run() throws IllegalArgumentException, OpenemsNamedException {
		System.out.println("Inside Off grid");
		GridMode gridMode = this.parent.parent.getGridMode().getNextValue().asEnum();
		switch (gridMode) {
		case ON_GRID:
			return StateMachine.State.GOING_ONGRID;
		case UNDEFINED:
			return StateMachine.State.UNDEFINED;
		case OFF_GRID:
			return this.doOffgrid();
		}
		return StateMachine.State.OFFGRID;
	}

	private State doOffgrid() throws OpenemsNamedException {
		
		CurrentState currentState = this.parent.getSinexcelState();

		switch (currentState) {
		
		case SLEEPING:
		case MPPT:
		case THROTTLED:
		case STARTED:
		case STANDBY:
			this.parent.parent.softStart(true);
			return State.OFFGRID;
		case UNDEFINED:
		case SHUTTINGDOWN:
		case FAULT:		
		case OFF:
		default:
			this.parent.parent.softStart(false);
			return State.ERROR;
			

		}
		//return State.OFFGRID;
	}

}
