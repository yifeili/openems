package io.openems.edge.ess.mr.gridcon;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.types.ChannelAddress;
import io.openems.edge.common.channel.BooleanReadChannel;
import io.openems.edge.ess.mr.gridcon.enums.InverterCount;
import io.openems.edge.ess.mr.gridcon.enums.PControlMode;
import io.openems.edge.ess.mr.gridcon.writeutils.CcuControlParameters;
import io.openems.edge.ess.mr.gridcon.writeutils.CommandControlRegisters;
import io.openems.edge.meter.api.SymmetricMeter;

public class GoingOngridHandler {

	private final Logger log = LoggerFactory.getLogger(GoingOngridHandler.class);

	private final StateMachine parent;

	public GoingOngridHandler(StateMachine parent) {
		this.parent = parent;
	}

	public void initialize() {
	}

	protected StateMachine.State run() throws IllegalArgumentException, OpenemsNamedException {
		System.out.println("GoingOngridHandler.run");

		// Are we still Off-Grid?
		BooleanReadChannel inputNAProtection1 = parent.parent.componentManager
				.getChannel(ChannelAddress.fromString(parent.parent.config.inputNAProtection1()));
		BooleanReadChannel inputNAProtection2 = parent.parent.componentManager
				.getChannel(ChannelAddress.fromString(parent.parent.config.inputNAProtection2()));

		Optional<Boolean> isInputNAProtection1 = inputNAProtection1.value().asOptional();
		Optional<Boolean> isInputNAProtection2 = inputNAProtection2.value().asOptional();

		if (isInputNAProtection1.isPresent() && isInputNAProtection1.get()) {

			if (isInputNAProtection2.isPresent() && isInputNAProtection2.get()) {
				return StateMachine.State.ONGRID;
			}
		} else {
			return StateMachine.State.OFFGRID;
		}

		this.doBlackStartGoingOnGrid();

		return StateMachine.State.GOING_ONGRID;
	}

	/**
	 * Handle BlackStart GoingOnGrid.
	 * 
	 * @param gridFreq
	 * @param gridVolt
	 * @throws IllegalArgumentException
	 * @throws OpenemsNamedException
	 */
	private void doBlackStartGoingOnGrid() throws IllegalArgumentException, OpenemsNamedException {

		// Always set OutputSyncDeviceBridge ON in Off-Grid state
		this.parent.parent.setOutputSyncDeviceBridge(true);

		SymmetricMeter gridMeter = this.parent.parent.componentManager.getComponent(this.parent.parent.config.meter());

		Optional<Integer> gridFreqOpt = gridMeter.getFrequency().asOptional();
		Optional<Integer> gridVoltOpt = gridMeter.getVoltage().asOptional();

		this.log.info(
				"GoingOngridHandler.doBlackStartGoingOnGrid() GridFreq: " + gridFreqOpt + ", GridVolt: " + gridVoltOpt);

		if (!gridFreqOpt.isPresent() || !gridVoltOpt.isPresent()) {
			// Cannot set anything without values
			return;
		}

		int gridFreq = gridFreqOpt.get();
		int gridVolt = gridVoltOpt.get();

		int invSetFreq = gridFreq + this.parent.parent.config.overFrequency();
		int invSetVolt = gridVolt + this.parent.parent.config.overVoltage();

		float invSetFreqNormalized = invSetFreq / 50_000f;
		float invSetVoltNormalized = invSetVolt / 231_000f;

		log.info("OffgridHandler.doBlackStartGoingOnGrid() Going On-Grid -> F/U " + invSetFreq + ", " + invSetVolt
				+ ", " + invSetFreqNormalized + ", " + invSetVoltNormalized);

		InverterCount inverterCount = this.parent.parent.config.inverterCount();
		new CommandControlRegisters() //
				.play(true) //
				.ready(false) //
				.acknowledge(false) //
				.stop(false) //
				.syncApproval(false) //
				.blackstartApproval(true) //
				.shortCircuitHandling(false) //
				.modeSelection(CommandControlRegisters.Mode.VOLTAGE_CONTROL) //
				.enableIpus(inverterCount) //
				.parameterU0(invSetVoltNormalized) //
				.parameterF0(invSetFreqNormalized) //
				.writeToChannels(this.parent.parent);
		new CcuControlParameters() //
				.pControlMode(PControlMode.DISABLED) //
				.qLimit(1f) //
				.writeToChannels(this.parent.parent);
		this.parent.parent.setIpuControlSettings();
	}

}
