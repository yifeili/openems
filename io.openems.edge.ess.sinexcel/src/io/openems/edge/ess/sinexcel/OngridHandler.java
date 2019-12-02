package io.openems.edge.ess.sinexcel;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.types.OptionsEnum;
import io.openems.edge.common.sum.GridMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ongrid handler, One of the states in sinexcel running,
 * When there is no change in the gridmode, It does the operations for Ongrid  mode
 * <p><ul>
 * <li> Set the digital output {@link io.openems.edge.ess.sinexcel.EssSinexcel#setDigitalOutputInOngrid()} , see "commercial 30 on grid.pdf" in "doc" folder
 * <li> First state is undefined {@link #doUndefined()}, 
 * which checks the first round check of the contactorOk or not {@link io.openems.edge.ess.sinexcel.EssSinexcel#isFirstCheckContactorOkInOngrid}
 * <li> Runs the Sinexcel in the ongrid mode
 * <li> If there is a error, the sinexcel is switched off in {@link #switchOff()}
 * </ul><p>
 * 
 */

public class OngridHandler {
	private final Logger log = LoggerFactory.getLogger(OngridHandler.class);
	private final StateMachine parent;

	private State state = State.UNDEFINED;

	public OngridHandler(StateMachine parent) {
		this.parent = parent;
	}

	protected StateMachine.State run() throws IllegalArgumentException, OpenemsNamedException {
		log.info("[Inside ongrid run]");
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

	/**
	 * This method Switches of the Inverter and sets the digital output channels to specific values,
	 * values are shown in "commercial 30 on grid.pdf" in "doc" folder
	 * 
	 * @return
	 * @throws OpenemsNamedException
	 */
	private State switchOff() throws OpenemsNamedException {
		log.info("in ongridhandler, in siwtch off method");
		this.parent.parent.inverterOff();
		this.parent.parent.digitalOutputAfterInverterOffInOngrid();
		return State.ERROR_SWITCHOFF;
	}

	/**
	 * This method would starting point in ongrid functions,
	 * This would do checks 
	 * <p><ul>
	 * <li>  
	 * <li>	 
	 * </ul><p>
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 * @throws OpenemsNamedException
	 */
	private State doUndefined() throws IllegalArgumentException, OpenemsNamedException {
		log.info("In ongrid handler do undefined method");
		Boolean contactorOk = this.parent.parent.isFirstCheckContactorOkInOngrid();
		if (contactorOk) {
			this.state = State.RUN_ONGRID;
		} else {
			if (this.parent.parent.isSecondCheckRequestContactorFault()) {
				this.state = State.GO_TO_OFFGRID;
			} else {
				this.state = State.ERROR_SWITCHOFF;
			}
		}
		return state;
	}

	private State doOngrid() throws OpenemsNamedException {
		log.info("In ongrid handler , doongrid method");
		CurrentState currentState = this.parent.getSinexcelState();
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
		GO_TO_OFFGRID(2, "Go to the off grid"), //
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
