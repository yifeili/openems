package io.openems.edge.controller.symmetric.selfconsumption;

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
import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.channel.IntegerReadChannel;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.sum.GridMode;
import io.openems.edge.controller.api.Controller;
import io.openems.edge.ess.api.ManagedSymmetricEss;
import io.openems.edge.ess.power.api.Phase;
import io.openems.edge.ess.power.api.Pwr;
import io.openems.edge.ess.power.api.Relationship;
import io.openems.edge.meter.api.SymmetricMeter;

@Designate(ocd = Config.class, factory = true)
@Component(name = "Controller.Ess.SellToGridLimit", immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class EssSellToGridLimit extends AbstractOpenemsComponent implements Controller, OpenemsComponent {

	private final Logger log = LoggerFactory.getLogger(EssSellToGridLimit.class);

	@Reference
	private ComponentManager componentManager;

	private Config config = null;

	public enum ChannelId implements io.openems.edge.common.channel.ChannelId {
		GRID_POWER(Doc.of(OpenemsType.INTEGER) //
				.text("Actual Grid Power")); //

		private final Doc doc;

		private ChannelId(Doc doc) {
			this.doc = doc;
		}

		@Override
		public Doc doc() {
			return this.doc;
		}
	}

	public EssSellToGridLimit() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				Controller.ChannelId.values(), //
				ChannelId.values());
	}

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

		// Get required variables
		ManagedSymmetricEss ess = this.componentManager.getComponent(this.config.ess_id());
		SymmetricMeter meter = this.componentManager.getComponent(this.config.meter_id());

		/*
		 * Check that we are On-Grid (and warn on undefined Grid-Mode)
		 */
		GridMode gridMode = ess.getGridMode();
		if (gridMode.isUndefined()) {
			this.logWarn(this.log, "Grid-Mode is [UNDEFINED]");
		}
		if (gridMode != GridMode.ON_GRID) {
			return;
		}

		// Calculating the actual grid power
		int gridPower = meter.getActivePower().orElse(0); /* current buy-from/sell-to grid */

		// Setting the Actual Grid-Power channel
		IntegerReadChannel actualGridPower = this.channel(ChannelId.GRID_POWER);
		actualGridPower.setNextValue(gridPower);

		// Checking if the grid power is above the maximum feed-in
		if (gridPower * -1 > this.config.maximumSellToGridPower()) {

			// Adjusting the limit to the maximum feed-in.
			int calculatedPower = gridPower + this.config.maximumSellToGridPower();

			// set result
			ess.addPowerConstraintAndValidate("Controller.Ess.SellToGridLimit", Phase.ALL, Pwr.ACTIVE,
					Relationship.GREATER_OR_EQUALS, calculatedPower); //
		}
	}
}
