package io.openems.edge.predictor.ann.model;

import java.io.IOException;
import java.time.Clock;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import io.openems.common.types.ChannelAddress;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.predictor.api.HourlyPredictor;

public abstract class AbstractAnnModelPredictor extends AbstractOpenemsComponent implements HourlyPredictor {

//	private final Logger log = LoggerFactory.getLogger(AbstractAnnModelPredictor.class);
	
	private final ChannelAddress channelAddress;
	private final Clock clock;
	



//	private final TreeMap<LocalDateTime, EnergyData> hourlyEnergyData = new TreeMap<LocalDateTime, EnergyData>();
	protected AbstractAnnModelPredictor(Clock clock, String componentId,
			io.openems.edge.common.channel.ChannelId channelId) {
		super(//
				OpenemsComponent.ChannelId.values(), //
				PredictorChannelId.values() //
		);
		this.channelAddress = new ChannelAddress(componentId, channelId.id());
		this.clock = clock;
	}
	
	protected AbstractAnnModelPredictor(String componentId,
			io.openems.edge.common.channel.ChannelId channelId) {
		this(Clock.systemDefaultZone(), componentId, channelId);
	}

	protected abstract ComponentManager getComponentManager();

	/**
	 * Collects the Ann model data on every cycle.
	 * 
	 * @param event the Event provided by {@link EventHandler}.
	 * @throws IOException 
	 */
	public void handleEvent(Event event)   {
		if (!this.isEnabled()) {
			return;
		}
		switch (event.getTopic()) {
		case EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE:
	    //TODO Implement for logic to run every cycle - Cycle typically runs every 3 seconds
//			try {
//
//				
//				dummy();
//				
//				this.channel(PredictorChannelId.UNABLE_TO_PREDICT).setNextValue(false);
//			} catch (OpenemsException e) {
//				this.logError(this.log, e.getMessage());
//				this.channel(PredictorChannelId.UNABLE_TO_PREDICT).setNextValue(true);
//			} 
			break;
		}
	}
	

}
