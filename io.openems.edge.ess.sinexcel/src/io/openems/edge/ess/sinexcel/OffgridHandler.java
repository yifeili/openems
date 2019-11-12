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
	
	
	private State switchOff() throws OpenemsNamedException {
		System.out.println("inside off grid handler, in switch off method");
		boolean contactorOk = this.parent.parent.isContactorOkInOffgrid();
		if (contactorOk) {
			System.out.println(" ppppppppppppppppppppppppppp");
			this.parent.parent.inverterOn();
			this.state =  State.RUN_OFFGRID;
			
		} else {
			System.out.println(" rrrrrrrrrrrrrrrrrrrrrrrrrrr");
			this.parent.parent.inverterOff();
			this.parent.parent.digitalOutputAfterInverterOffInOffgrid();
			this.state =  State.ERROR_SWITCHOFF;
		}
		return state;
	}
	
	private State doUndefined() throws IllegalArgumentException, OpenemsNamedException {
		System.out.println("inside off grid handler, in doundefined method");		
		boolean contactorChk = this.parent.parent.isFirstCheckContactorFault();
		if (contactorChk) {
			System.out.println("first if");
			return State.RUN_OFFGRID;
		}else {
			System.out.println("second else");
			return State.ERROR_SWITCHOFF;			
		}
	}	
	
	private State doOffgrid() throws OpenemsNamedException {
		System.out.println("inside off grid handler, in dooffgrid method");
		// set this relais when in off grid mode
		this.parent.parent.setDigitalOutputInOffgrid();
		boolean contactorOk = this.parent.parent.isContactorOkInOffgrid();
		System.out.println("in doff gridmode the contactor ok is : " + contactorOk);
		if(contactorOk) {
			System.out.println("ooooooooooooooooooooooooooooooooooooooooo");
			//Set the freq
			parent.parent.setFreq();
			
			CurrentState currentState = this.parent.getSinexcelState();
			// GridMode gridMode = this.parent.parent.getGridMode().getNextValue().asEnum();

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
			}
			this.state = State.RUN_OFFGRID;
		}else {
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
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
