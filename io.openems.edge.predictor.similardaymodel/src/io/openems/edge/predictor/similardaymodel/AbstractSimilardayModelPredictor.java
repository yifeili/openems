package io.openems.edge.predictor.similardaymodel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.types.ChannelAddress;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.predictor.api.HourlyPrediction;
import io.openems.edge.predictor.api.HourlyPredictor;
import io.openems.edge.timedata.api.Timedata;

public abstract class AbstractSimilardayModelPredictor extends AbstractOpenemsComponent implements HourlyPredictor {

	private final Logger log = LoggerFactory.getLogger(AbstractSimilardayModelPredictor.class);

	private final ChannelAddress channelAddress;
	private final Clock clock;
	private boolean executed;
	private long currentEnergy;
	LocalDateTime prevHour = LocalDateTime.now();

	private final TreeMap<LocalDateTime, Integer> hourlyEnergyData = new TreeMap<LocalDateTime, Integer>();

	protected AbstractSimilardayModelPredictor(Clock clock, String componentId,
			io.openems.edge.common.channel.ChannelId channelId) {
		super(//
				OpenemsComponent.ChannelId.values(), //
				PredictorChannelId.values() //
		);
		this.channelAddress = new ChannelAddress(componentId, channelId.id());
		this.clock = clock;
	}

	protected AbstractSimilardayModelPredictor(String componentId,
			io.openems.edge.common.channel.ChannelId channelId) {
		this(Clock.systemDefaultZone(), componentId, channelId);
	}

	protected abstract ComponentManager getComponentManager();
	
	protected abstract Timedata getTimedata();

	/**
	 * Collects the persistence model data on every cycle.
	 * 
	 * @param event the Event provided by {@link EventHandler}.
	 * @throws IOException 
	 */
	public void handleEvent(Event event) throws IOException {
		if (!this.isEnabled()) {
			return;
		}
		switch (event.getTopic()) {
		case EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE:
			try {
				//this.calculateEnergyValue();
				this.getData();
				this.channel(PredictorChannelId.UNABLE_TO_PREDICT).setNextValue(false);
			} catch (OpenemsNamedException e) {
				this.logError(this.log, e.getMessage());
				this.channel(PredictorChannelId.UNABLE_TO_PREDICT).setNextValue(true);
			}

		}
	}
	
	

	
	private void getData() throws OpenemsNamedException, IOException {
		String PYTHON_ABSOLUTE_PATH = "C:\\Users\\pooran.c\\AppData\\Local\\Continuum\\anaconda3\\python.exe";
		//String script_path = "src/resources/TfTest.py";
		String labels = "src/resources/labels_mobilenet_quant_v1_224";
		String tflite = "src/resources/mobilenet_v1_1.0_224_quant.tflite";
		String[] command = new String[]{PYTHON_ABSOLUTE_PATH,tflite,labels};
		Process p = Runtime.getRuntime().exec(command);
		
		
	    InputStream stdout = p.getInputStream();
	    String s = null;
	    BufferedReader in = new BufferedReader(new InputStreamReader(stdout));
	    while ((s = in.readLine()) != null) {
	    	System.out.println(s);
	    }
	}
	

//	
//	@SuppressWarnings("null")
//	private void getData() throws OpenemsNamedException {
//		
//		ZonedDateTime fromDate = ZonedDateTime.of(2020, 02, 10, 0, 0, 0, 0, ZoneId.of("UTC"));
//		ZonedDateTime toDate = ZonedDateTime.of(2020, 02, 12, 0, 0, 0, 0, ZoneId.of("UTC"));
//		TreeSet<ChannelAddress> channels = new TreeSet<>();
//		channels.add(this.channelAddress);
//		
//		SortedMap<ZonedDateTime, SortedMap<ChannelAddress, JsonElement>> Data = this.getTimedata()
//				.queryHistoricData(null, fromDate, toDate, channels,
//						60 * 60 /* 15 Minutes */);
//		JsonObject result = getResult(Data);
////		JsonArray jArray = (JsonArray) result.get("timestamps");
////		rework(jArray);
//	}
//
////	public void rework(JsonArray str) {
////		int len = str.size();
////		for (int i = 0; i <= len ; i++) {
////			String[] newArr1  = Arrays.copyOfRange(str, 0, len/2);
////			String[] newArr2  = Arrays.copyOfRange(str, str.length/2, str.length);
////			
////			System.out.println(Arrays.toString(newArr1));
////			System.out.println(Arrays.toString(newArr2));
////		}
////	}
//
//			
//			
//			
//			
//			
//
//	
//	public JsonObject getResult(SortedMap<ZonedDateTime, SortedMap<ChannelAddress, JsonElement>> table) {
//		JsonObject result = new JsonObject();
//
//		JsonArray timestamps = new JsonArray();
//		for (ZonedDateTime timestamp : table.keySet()) {
//			timestamps.add(timestamp.format(DateTimeFormatter.ISO_INSTANT));
//		}
//		result.add("timestamps", timestamps);
//
//		JsonObject data = new JsonObject();
//		for (Entry<ZonedDateTime, SortedMap<ChannelAddress, JsonElement>> rowEntry : table.entrySet()) {
//			for (Entry<ChannelAddress, JsonElement> colEntry : rowEntry.getValue().entrySet()) {
//				String channelAddress = colEntry.getKey().toString();
//				JsonElement value = colEntry.getValue();
//				JsonElement channelValuesElement = data.get(channelAddress);
//				JsonArray channelValues;
//				if (channelValuesElement != null) {
//					channelValues = channelValuesElement.getAsJsonArray();
//				} else {
//					channelValues = new JsonArray();
//				}
//				channelValues.add(value);
//				data.add(channelAddress, channelValues);
//			}
//		}
//		result.add("data", data);
//
//		return result;
//	}
//	
//
//
//	
//
//	
//	
////	/*
////	 * This method gets the value from the Channel every one hour and updates the
////	 * TreeMap.
////	 */
////	private void calculateEnergyValue() throws OpenemsNamedException {
////		LongReadChannel channel = this.getComponentManager().getChannel(this.channelAddress);
////		Optional<Long> energyOpt = channel.value().asOptional();
////
////		// Stop early if there is no energy available (yet)
////		if (!energyOpt.isPresent()) {
////			return;
////		}
////		long energy = energyOpt.get();
////
////		LocalDateTime currentHour = LocalDateTime.now(this.clock).withNano(0).withMinute(0).withSecond(0);
////
////		if (!executed) {
////			// First time execution - Map is still empty
////			this.currentEnergy = energy;
////			this.prevHour = currentHour;
////			this.executed = true;
////		} else if (currentHour.isAfter(this.prevHour)) {
////			// hour changed -> calculate delta and record value
////			int delta = (int) (energy - this.currentEnergy);
////			this.hourlyEnergyData.put(this.prevHour, delta);
////			this.prevHour = currentHour;
////			this.currentEnergy = energy;
////		} else {
////			// hour did not change -> return
////			return;
////		}
////
////		// We added an entry to the map. Implement circular buffer.
////		if (this.hourlyEnergyData.size() > 24) {
////			this.hourlyEnergyData.remove(this.hourlyEnergyData.firstKey());
////		}
////	}

	@Override
	public HourlyPrediction get24hPrediction() {
		Integer[] values = new Integer[24];
		int i = Math.max(0, 24 - this.hourlyEnergyData.size());

		for (Entry<LocalDateTime, Integer> entry : this.hourlyEnergyData.entrySet()) {
			values[i++] = entry.getValue();
		}
		LocalDateTime currentHour = LocalDateTime.now(this.clock).withNano(0).withMinute(0).withSecond(0);

		HourlyPrediction hourlyPrediction = new HourlyPrediction(values, currentHour);
		return hourlyPrediction;
	}

}
