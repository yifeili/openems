package io.openems.edge.predictor.annmodel.production;

import java.util.Collection;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.metatype.annotations.Designate;

import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.predictor.api.HourlyPrediction;
import io.openems.edge.predictor.api.ProductionHourlyPredictor;



@Designate(ocd = Config.class, factory = true)
@Component(name = "Predictor.Production.Ann.Model", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE, //
		property = EventConstants.EVENT_TOPIC + "=" + EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE)
public class ProductionPredictor extends AbstractOpenemsComponent
implements ProductionHourlyPredictor, OpenemsComponent, EventHandler{

	protected ProductionPredictor(io.openems.edge.common.channel.ChannelId[] firstInitialChannelIds,
			io.openems.edge.common.channel.ChannelId[][] furtherInitialChannelIds) {
		super(firstInitialChannelIds, furtherInitialChannelIds);
		// TODO Auto-generated constructor stub
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
