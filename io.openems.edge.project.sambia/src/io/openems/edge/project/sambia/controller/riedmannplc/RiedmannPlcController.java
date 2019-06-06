package io.openems.edge.project.sambia.controller.riedmannplc;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
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
	private boolean updateWaterLevelBorehole1On = false;
	private boolean updateWaterLevelBorehole1Off = false;
	private boolean updateWaterLevelBorehole2On = false;
	private boolean updateWaterLevelBorehole2Off = false;
	private boolean updateWaterLevelBorehole3On = false;
	private boolean updateWaterLevelBorehole3Off = false;
	private boolean load1On = true;
	private boolean load2On = true;
	private boolean load3On = true;
	private boolean load4On = true;

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
		RiedmannPlc sps = this.componentManager.getComponent(this.config.plc_id());

		// Watchdog
		IntegerWriteChannel watchdog = sps.channel(RiedmannPlc.ChannelId.SIGNAL_WATCHDOG);
		if (this.watchdogState) {
			watchdog.setNextWriteValue(0);
			watchdogState = false;
		} else {
			watchdog.setNextWriteValue(1);
			watchdogState = true;
		}

		// Water level
		if (this.updateWaterLevelBorehole1Off) {
			IntegerWriteChannel setWaterLevelBorehole1Off = sps
					.channel(RiedmannPlc.ChannelId.SET_WATERLEVEL_BOREHOLE1_OFF);
			setWaterLevelBorehole1Off.setNextWriteValue(this.config.setWaterLevelBorehole1Off());
			this.updateWaterLevelBorehole1Off = false;
		}

		if (this.updateWaterLevelBorehole1On) {
			IntegerWriteChannel setWaterLevelBorehole1On = sps
					.channel(RiedmannPlc.ChannelId.SET_WATERLEVEL_BOREHOLE1_ON);
			setWaterLevelBorehole1On.setNextWriteValue(this.config.setWaterLevelBorehole1On());
			this.updateWaterLevelBorehole1On = false;
		}

		if (this.updateWaterLevelBorehole2Off) {
			IntegerWriteChannel setWaterLevelBorehole2Off = sps
					.channel(RiedmannPlc.ChannelId.SET_WATERLEVEL_BOREHOLE2_OFF);
			setWaterLevelBorehole2Off.setNextWriteValue(this.config.setWaterLevelBorehole2Off());
			this.updateWaterLevelBorehole2Off = false;
		}

		if (this.updateWaterLevelBorehole2On) {
			IntegerWriteChannel setWaterLevelBorehole2On = sps
					.channel(RiedmannPlc.ChannelId.SET_WATERLEVEL_BOREHOLE2_ON);
			setWaterLevelBorehole2On.setNextWriteValue(this.config.setWaterLevelBorehole2On());
			this.updateWaterLevelBorehole2On = false;
		}

		if (this.updateWaterLevelBorehole3Off) {
			IntegerWriteChannel setWaterLevelBorehole3Off = sps
					.channel(RiedmannPlc.ChannelId.SET_WATERLEVEL_BOREHOLE3_OFF);
			setWaterLevelBorehole3Off.setNextWriteValue(this.config.setWaterLevelBorehole3Off());
			this.updateWaterLevelBorehole3Off = false;
		}

		if (this.updateWaterLevelBorehole3On) {
			IntegerWriteChannel setWaterLevelBorehole3On = sps
					.channel(RiedmannPlc.ChannelId.SET_WATERLEVEL_BOREHOLE3_ON);
			setWaterLevelBorehole3On.setNextWriteValue(this.config.setWaterLevelBorehole3On());
			this.updateWaterLevelBorehole3On = false;
		}

		/*
		 * Load switching
		 */
		int essSoc = ess.getSoc().value().getOrError();
		GridMode gridMode = ess.getGridMode().value().asEnum();

		// Load1
		if (essSoc >= this.config.socLoad1Off() + this.config.socHysteresis()
				|| (gridMode.isUndefined() || gridMode.equals(GridMode.ON_GRID))) {
			this.load1On = true;
		} else if (essSoc <= this.config.socLoad1Off()) {
			this.load1On = false;
		}
		{
			IntegerWriteChannel setClima1On = sps.channel(RiedmannPlc.ChannelId.SET_CLIMA1_ON);
			IntegerWriteChannel setClima2On = sps.channel(RiedmannPlc.ChannelId.SET_CLIMA2_ON);
			if (this.load1On) {
				setClima1On.setNextWriteValue(1);
				setClima2On.setNextWriteValue(1);
			} else {
				setClima1On.setNextWriteValue(0);
				setClima2On.setNextWriteValue(0);
			}
		}

		// Load2
		if (essSoc >= this.config.socLoad2Off() + this.config.socHysteresis()
				|| (gridMode.isUndefined() || gridMode.equals(GridMode.ON_GRID))) {
			this.load2On = true;
		} else if (essSoc <= this.config.socLoad2Off()) {
			this.load2On = false;
		}
		{
			IntegerWriteChannel setPivotOn = sps.channel(RiedmannPlc.ChannelId.SET_PIVOT_ON);
			if (this.load2On) {
				setPivotOn.setNextWriteValue(1);
			} else {
				setPivotOn.setNextWriteValue(0);
			}
		}
		// Load3
		if (essSoc >= this.config.socLoad3Off() + this.config.socHysteresis()
				|| (gridMode.isUndefined() || gridMode.equals(GridMode.ON_GRID))) {
			this.load3On = true;
		} else if (essSoc <= this.config.socLoad3Off()) {
			this.load3On = false;
		}
		{
			IntegerWriteChannel setBorehole1On = sps.channel(RiedmannPlc.ChannelId.SET_BOREHOLE1_ON);
			IntegerWriteChannel setBorehole2On = sps.channel(RiedmannPlc.ChannelId.SET_BOREHOLE2_ON);
			IntegerWriteChannel setBorehole3On = sps.channel(RiedmannPlc.ChannelId.SET_BOREHOLE3_ON);
			if (this.load3On) {
				setBorehole1On.setNextWriteValue(1);
				setBorehole2On.setNextWriteValue(1);
				setBorehole3On.setNextWriteValue(1);
			} else {
				setBorehole1On.setNextWriteValue(0);
				setBorehole2On.setNextWriteValue(0);
				setBorehole3On.setNextWriteValue(0);
			}
		}
		// Load4
		if (essSoc >= this.config.socLoad4Off() + this.config.socHysteresis()
				|| (gridMode.isUndefined() || gridMode.equals(GridMode.ON_GRID))) {
			this.load4On = true;
		} else if (essSoc <= this.config.socLoad4Off()) {
			this.load4On = false;
		}
		{
			IntegerWriteChannel setOfficeOn = sps.channel(RiedmannPlc.ChannelId.SET_OFFICE_ON);
			IntegerWriteChannel setTraineeCenterOn = sps.channel(RiedmannPlc.ChannelId.SET_TRAINEE_CENTER_ON);
			if (this.load4On) {
				setOfficeOn.setNextWriteValue(1);
				setTraineeCenterOn.setNextWriteValue(1);
			} else {
				setOfficeOn.setNextWriteValue(0);
				setTraineeCenterOn.setNextWriteValue(0);
			}
		}
	}

}
