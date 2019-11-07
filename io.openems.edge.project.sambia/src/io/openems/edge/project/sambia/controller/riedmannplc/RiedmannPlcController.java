package io.openems.edge.project.sambia.controller.riedmannplc;

import java.util.Optional;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.channel.IntegerWriteChannel;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.sum.GridMode;
import io.openems.edge.controller.api.Controller;
import io.openems.edge.ess.api.SymmetricEss;
import io.openems.edge.project.sambia.riedmannplc.RiedmannPlc;

@Designate(ocd = Config.class, factory = true)
@Component(name = "Project.Sambia.Controller.RiedmannPLC", immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class RiedmannPlcController extends AbstractOpenemsComponent implements Controller, OpenemsComponent {

	private final Logger log = LoggerFactory.getLogger(RiedmannPlcController.class);

	public enum ChannelId implements io.openems.edge.common.channel.ChannelId {
		;
		private final Doc doc;

		private ChannelId(Doc doc) {
			this.doc = doc;
		}

		@Override
		public Doc doc() {
			return this.doc;
		}
	}

	public RiedmannPlcController() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				Controller.ChannelId.values(), //
				ChannelId.values() //
		);
	}

	@Reference
	protected ComponentManager componentManager;

	private Config config;

	private boolean watchdogState = false;

	@Activate
	void activate(ComponentContext context, Config config) {
		super.activate(context, config.id(), config.alias(), config.enabled());
		this.config = config;
	}

	@Deactivate
	protected void deactivate() {
		super.deactivate();
	}

	@Override
	public void run() throws OpenemsNamedException {
		// Check if all parameters are available
		SymmetricEss ess = this.componentManager.getComponent(this.config.ess_id());
		RiedmannPlc plc = this.componentManager.getComponent(this.config.plc_id());

		// Watchdog
		IntegerWriteChannel watchdog = plc.channel(RiedmannPlc.ChannelId.SIGNAL_WATCHDOG);
		if (this.watchdogState) {
			watchdog.setNextWriteValue(0);
			watchdogState = false;
		} else {
			watchdog.setNextWriteValue(1);
			watchdogState = true;
		}
		int essSoc = ess.getSoc().value().getOrError();
		GridMode gridMode = ess.getGridMode().value().asEnum();
		if (gridMode.equals(GridMode.OFF_GRID)) {
			this.setOutput(plc, RiedmannPlc.ChannelId.SIGNAL_GRID_ON, 0);
		} else {
			this.setOutput(plc, RiedmannPlc.ChannelId.SIGNAL_GRID_ON, 1);
		}

		// Water level
		this.setOutput(plc, RiedmannPlc.ChannelId.WATERLEVEL_BOREHOLE1_OFF, this.config.setWaterLevelBorehole1Off());
		this.setOutput(plc, RiedmannPlc.ChannelId.WATERLEVEL_BOREHOLE1_ON, this.config.setWaterLevelBorehole1On());

		this.setOutput(plc, RiedmannPlc.ChannelId.WATERLEVEL_BOREHOLE2_OFF, this.config.setWaterLevelBorehole2Off());
		this.setOutput(plc, RiedmannPlc.ChannelId.WATERLEVEL_BOREHOLE2_ON, this.config.setWaterLevelBorehole2On());

		this.setOutput(plc, RiedmannPlc.ChannelId.WATERLEVEL_BOREHOLE3_OFF, this.config.setWaterLevelBorehole3Off());
		this.setOutput(plc, RiedmannPlc.ChannelId.WATERLEVEL_BOREHOLE3_ON, this.config.setWaterLevelBorehole3On());

		/*
		 * Load switching
		 */

		// Load1
		if (essSoc >= this.config.socLoad1Off() + this.config.socHysteresis()
				|| (gridMode.isUndefined() || gridMode.equals(GridMode.ON_GRID))) {
			this.setOutput(plc, RiedmannPlc.ChannelId.CLIMA1_ON, 1);
			this.setOutput(plc, RiedmannPlc.ChannelId.CLIMA2_ON, 1);
		} else if (essSoc <= this.config.socLoad1Off()) {
			this.setOutput(plc, RiedmannPlc.ChannelId.CLIMA1_ON, 0);
			this.setOutput(plc, RiedmannPlc.ChannelId.CLIMA2_ON, 0);
		}

		// Load2
		if (essSoc >= this.config.socLoad2Off() + this.config.socHysteresis()
				|| (gridMode.isUndefined() || gridMode.equals(GridMode.ON_GRID))) {
			this.setOutput(plc, RiedmannPlc.ChannelId.PIVOT_ON, 1);
		} else if (essSoc <= this.config.socLoad2Off()) {
			this.setOutput(plc, RiedmannPlc.ChannelId.PIVOT_ON, 0);
		}

		// Load3
		if (essSoc >= this.config.socLoad3Off() + this.config.socHysteresis()
				|| (gridMode.isUndefined() || gridMode.equals(GridMode.ON_GRID))) {
			this.setOutput(plc, RiedmannPlc.ChannelId.BOREHOLE1_ON, 1);
			this.setOutput(plc, RiedmannPlc.ChannelId.BOREHOLE2_ON, 1);
			this.setOutput(plc, RiedmannPlc.ChannelId.BOREHOLE3_ON, 1);
		} else if (essSoc <= this.config.socLoad3Off()) {
			this.setOutput(plc, RiedmannPlc.ChannelId.BOREHOLE1_ON, 0);
			this.setOutput(plc, RiedmannPlc.ChannelId.BOREHOLE2_ON, 0);
			this.setOutput(plc, RiedmannPlc.ChannelId.BOREHOLE3_ON, 0);
		}

		// Load4
		if (essSoc >= this.config.socLoad4Off() + this.config.socHysteresis()
				|| (gridMode.isUndefined() || gridMode.equals(GridMode.ON_GRID))) {
			this.setOutput(plc, RiedmannPlc.ChannelId.OFFICE_ON, 1);
			this.setOutput(plc, RiedmannPlc.ChannelId.TRAINEE_CENTER_ON, 1);
		} else if (essSoc <= this.config.socLoad4Off()) {
			this.setOutput(plc, RiedmannPlc.ChannelId.OFFICE_ON, 0);
			this.setOutput(plc, RiedmannPlc.ChannelId.TRAINEE_CENTER_ON, 0);
		}
	}

	/**
	 * Helper function to switch an output if it was not switched before.
	 */
	private void setOutput(RiedmannPlc plc, RiedmannPlc.ChannelId channelId, int value)
			throws IllegalArgumentException, OpenemsNamedException {
		try {
			IntegerWriteChannel outputChannel = plc.channel(channelId);
			Optional<Integer> currentValueOpt = outputChannel.value().asOptional();
			if (!currentValueOpt.isPresent() || currentValueOpt.get() != value) {
				this.logInfo(this.log, "Set output [" + outputChannel.address() + "] value [" + value + "].");
				outputChannel.setNextWriteValue(value);
			}
		} catch (OpenemsException e) {
			this.logError(this.log, "Unable to set output: [" + channelId.id() + "] " + e.getMessage());
		}
	}

}
