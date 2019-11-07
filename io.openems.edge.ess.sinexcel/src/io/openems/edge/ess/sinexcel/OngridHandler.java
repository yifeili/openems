package io.openems.edge.ess.sinexcel;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.types.OptionsEnum;
import io.openems.edge.common.sum.GridMode;

public class OngridHandler {
	// private final Logger log = LoggerFactory.getLogger(OngridHandler.class);
	private final StateMachine parent;

	private State state = State.UNDEFINED;

	public OngridHandler(StateMachine parent) {
		this.parent = parent;
	}

	protected StateMachine.State run() throws IllegalArgumentException, OpenemsNamedException {
		System.out.println("Inside ongrid run");
		GridMode gridMode = this.parent.parent.getGridMode().getNextValue().asEnum();
		switch (gridMode) {
		case ON_GRID:
			break;
		case UNDEFINED:
			return StateMachine.State.UNDEFINED;
		case OFF_GRID:
			return StateMachine.State.GOING_OFFGRID;
		}

		// Set this Digital output when in on-grid mode
		this.parent.parent.setDigitalOutputInOngrid();

		switch (this.state) {
		case UNDEFINED:
			this.state = this.doUndefined();
			break;

		case RUN_ONGRID:
			this.state = this.doOngrid();
			break;

		case GO_TO_OFFGRID:
			return StateMachine.State.GOING_OFFGRID;

		case ERROR_SWITCHOFF:

			this.switchOff();
			break;
		}

		return StateMachine.State.ONGRID;
	}	

	private State switchOff() throws OpenemsNamedException {
		this.parent.parent.inverterOff();
		this.parent.parent.digitalOutputAfterInverterOffInOngrid();
		return null;
	}

	private State doUndefined() throws IllegalArgumentException, OpenemsNamedException {
		
		Boolean contactorOk = this.parent.parent.isContactorOkInOngrid();
		if (!contactorOk) {
			this.state = State.RUN_ONGRID;
		} else {
			if (this.parent.parent.isRequestContactorFault()) {
				this.state = State.GO_TO_OFFGRID;
			} else {
				this.state = State.ERROR_SWITCHOFF;
			}
		}
		return state;
	}

	private State doOngrid() throws OpenemsNamedException {

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
		return State.RUN_ONGRID;
	}

	public enum State implements OptionsEnum {
		UNDEFINED(-1, "Undefined"), //
		RUN_ONGRID(1, "Run on on-grid mode"), //
		GO_TO_OFFGRID(2, "Go to the off grid"),//
		ERROR_SWITCHOFF(3, "Safety control, switch of the inverter");

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
