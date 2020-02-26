package io.openems.edge.predictor.similardaymodel.production;

import java.util.Collection;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.metatype.annotations.Designate;

import io.openems.common.OpenemsConstants;
import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.common.sum.Sum;
import io.openems.edge.predictor.api.ProductionHourlyPredictor;
import io.openems.edge.predictor.api.HourlyPrediction;
import io.openems.edge.predictor.similardaymodel.AbstractSimilardayModelPredictor;
import io.openems.edge.timedata.api.Timedata;

@Designate(ocd = Config.class, factory = true)
@Component(name = "Predictor.Production.SimilardayModel", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE, //
		property = EventConstants.EVENT_TOPIC + "=" + EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE)

public class ProductionPredictor extends AbstractSimilardayModelPredictor
		implements ProductionHourlyPredictor, OpenemsComponent, EventHandler {

	@Reference
	protected ComponentManager componentManager;
	
	@Reference
	protected Timedata timedata = null;

	public ProductionPredictor() {
		super(OpenemsConstants.SUM_ID, Sum.ChannelId.CONSUMPTION_ACTIVE_ENERGY);
	}

	@Activate
	void activate(ComponentContext context, Config config) throws OpenemsNamedException {
		super.activate(context, config.id(), config.alias(), config.enabled());
	}

	@Deactivate
	protected void deactivate() {
		super.deactivate();
	}

	@Override
	protected ComponentManager getComponentManager() {
		return this.componentManager;
	}
	
	@Override
	protected Timedata getTimedata() {
		return this.timedata;
	}

	@Override
	public HourlyPrediction get24hPrediction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String id() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String alias() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ComponentContext getComponentContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Channel<?> _channel(String channelName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Channel<?>> channels() {
		// TODO Auto-generated method stub
		return null;
	}

}
