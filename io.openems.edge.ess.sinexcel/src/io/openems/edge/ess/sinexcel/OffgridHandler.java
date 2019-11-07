package io.openems.edge.ess.sinexcel;

//import java.util.Optional;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

//import io.openems.common.exceptions.OpenemsException;
import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.types.OptionsEnum;
import io.openems.edge.common.sum.GridMode;

public class OffgridHandler {

//	private final Logger log = LoggerFactory.getLogger(OngridHandler.class);
	private final StateMachine parent;
	
	private State state = State.UNDEFINED;

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
			break;
		}
		

		switch (this.state) {
		case UNDEFINED:
			this.state = this.doUndefined();
			break;

		case RUN_OFFGRID:
			this.state = this.doOffgrid();
			break;

		case GO_TO_ONGRID:
			return StateMachine.State.GOING_ONGRID;

		case ERROR_SWITCHOFF:
			this.switchOff();
			break;
		}
		
		
		
		return StateMachine.State.OFFGRID;
	}
	
	private State doUndefined() throws IllegalArgumentException, OpenemsNamedException {
		boolean fault = this.parent.parent.isRequestContactorFault();
		if (fault) {
			return State.ERROR_SWITCHOFF;
		}else {
			return State.RUN_OFFGRID;
		}
	}
	
	private State switchOff() throws OpenemsNamedException {
		this.parent.parent.inverterOff();
		this.parent.parent.digitalOutputAfterInverterOffInOffgrid();
		return null;
	}
	
	
	private State doOffgrid() throws OpenemsNamedException {
		// set this relais when in off grid mode
		this.parent.parent.setDigitalOutputInOffgrid();
		boolean contactorOk = this.parent.parent.isContactorOkInOffgrid();
		if(!contactorOk) {
			//Set the freq
			parent.parent.setFreq();

			
			this.state = State.RUN_OFFGRID;
		}else {
			this.state = State.ERROR_SWITCHOFF;
		}
		return state;
	}


//	private State doOffgrid() throws OpenemsNamedException {
//
//		CurrentState currentState = this.parent.getSinexcelState();
//		GridMode gridMode = this.parent.parent.getGridMode().getNextValue().asEnum();
//		switch (currentState) {
//
//		case SLEEPING:
//		case MPPT:
//		case THROTTLED:
//		case STARTED:
//		case STANDBY:
//			this.parent.parent.softStart(true);
//			return State.OFFGRID;
//		case UNDEFINED:
//		case SHUTTINGDOWN:
//		case FAULT:
//		case OFF:
//		default:
//			this.parent.parent.softStart(false);
//			switch (gridMode) {
//			case ON_GRID:
//				return State.ONGRID;
//			case UNDEFINED:
//				return State.UNDEFINED;
//			case OFF_GRID:
//				return State.OFFGRID;
//			}
//		}
//		return State.OFFGRID;
//		
//	}

	
	public enum State implements OptionsEnum {
		UNDEFINED(-1, "Undefined"), //
		RUN_OFFGRID(1, "Run on on-grid mode"), //
		GO_TO_ONGRID(2, "Go to the off grid"), //
		ERROR_SWITCHOFF(3, "Safety control, switch off the inverter");

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
