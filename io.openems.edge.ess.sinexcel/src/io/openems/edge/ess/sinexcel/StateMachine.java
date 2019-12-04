package io.openems.edge.ess.sinexcel;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.types.OptionsEnum;
import io.openems.edge.common.channel.EnumReadChannel;
import io.openems.edge.common.sum.GridMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This state machine is a starting point of running the sinexcel in any grid mode(on/ off grid).
 * 
 * There are 6 states {@link #State}, First state is always undefined state,  
 * <p><ul>
 * <li> {@link #handleUndefined()} Method would set the statemachine state to on/ off/ undefined state
 * <li> {@link #offgridHandler} and {@link #ongridHandler} would run the sinexcel in the respective modes
 * <li> {@link #goingOffgridHandler} and {@link #goingOngridHandler} are the transitional state while switching gridmodes
 * </ul><p>
 */
public class StateMachine {

	protected final EssSinexcel parent;
	private final Logger log = LoggerFactory.getLogger(StateMachine.class);
	private final OngridHandler ongridHandler = new OngridHandler(this);
	private final OffgridHandler offgridHandler = new OffgridHandler(this);
	private final GoingOngridHandler goingOngridHandler = new GoingOngridHandler(this);
	private final GoingOffgridHandler goingOffgridHandler = new GoingOffgridHandler(this);
	private State state = State.UNDEFINED;

	public StateMachine(EssSinexcel parent) throws OpenemsNamedException {
		this.parent = parent;
	}

	public void run() throws IllegalArgumentException, OpenemsNamedException {
		boolean stateChanged;
		do {
			stateChanged = false;
			switch (state) {
			case UNDEFINED:
				stateChanged = changeState(this.handleUndefined());
				break;
			case GOING_ONGRID:
				stateChanged = changeState(this.goingOngridHandler.run());
				break;
			case ONGRID:
				stateChanged = changeState(this.ongridHandler.run());
				break;
			case GOING_OFFGRID:
				stateChanged = changeState(this.goingOffgridHandler.run());
				break;
			case OFFGRID:
				stateChanged = changeState(this.offgridHandler.run());
				break;
			// This is state not usually reached
			case ERROR:
				stateChanged = changeState(this.errorHandler());
				break;
			}
		} while (stateChanged);
	}
	
	/**
	 * A flag to maintain change in the mode
	 * 
	 * @param nextmode the target mode
	 * @return Flag that the mode is changed or not
	 */
	private boolean changeState(State nextState) {
		if (this.state != nextState) {
			this.state = nextState;
			return true;
		} else
			return false;
	}

	/**
	 * This method is a handler for Error in the state machine. 
	 * it is just a places holding for errorstate, most of the error handling is done in the respective gridModes
	 * 
	 * @return
	 */
	private State errorHandler() {
		CurrentState currentState = getSinexcelState();
		this.log.info("in error handler [" + currentState + "]");
		
		GridMode gridMode = this.parent.getGridMode().getNextValue().asEnum();
		this.log.info("mode is  [" + gridMode + "]");
		
		switch (gridMode) {
		case ON_GRID:
			return State.ONGRID;
		case OFF_GRID:
			return State.OFFGRID;
		case UNDEFINED:
			this.log.info("In handleUndefined(), Grid-Mode is [" + gridMode + "]");
			return State.UNDEFINED;
		}		
		return State.ERROR;
	}

	/**
	 * Evaluates the current State.
	 * 
	 * @return
	 * 
	 * @throws OpenemsNamedException
	 * @throws IllegalArgumentException
	 */
	private State handleUndefined() {
		GridMode gridMode = this.parent.getGridMode().getNextValue().asEnum();
		CurrentState currentState = getSinexcelState();
		if (currentState == CurrentState.FAULT) {
			return State.ERROR;
		}
		switch (gridMode) {
		case ON_GRID:
			return State.ONGRID;
		case OFF_GRID:
			return State.OFFGRID;
		case UNDEFINED:
			return State.UNDEFINED;
		}
		return State.UNDEFINED;
	}

	/**
	 * States used in sinexcel, There are 6 state; 4 concrete states, 2 transitional states
	 * The transitional states are used to perform actions while switching the "on and off gridmodes" 
	 *
	 */
	public enum State implements OptionsEnum {
		UNDEFINED(-1, "Undefined"), //
		GOING_ONGRID(1, "Going On-Grid"), //
		ONGRID(2, "On-Grid"), //
		GOING_OFFGRID(3, "Going Off-Grid"), //
		OFFGRID(4, "Off-Grid"), //
		ERROR(5, "Error");

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

	/**
	 * This method get the current state of the Sinexcel
	 * {@link #CurrentState}
	 * 
	 * @return
	 */
	protected CurrentState getSinexcelState() {
		EnumReadChannel currentState = this.parent.channel(SinexcelChannelId.SINEXCEL_STATE);
		CurrentState curState = currentState.value().asEnum();
		System.out.println("[Current State is : " + curState.toString() + "]");
		return curState;
	}
}