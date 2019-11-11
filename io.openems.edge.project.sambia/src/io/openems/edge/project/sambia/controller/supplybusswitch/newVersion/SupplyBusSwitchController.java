package io.openems.edge.project.sambia.controller.supplybusswitch.newVersion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.exceptions.OpenemsException;
import io.openems.common.types.ChannelAddress;
import io.openems.common.utils.JsonUtils;
import io.openems.edge.common.channel.BooleanReadChannel;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.channel.IntegerWriteChannel;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.sum.GridMode;
import io.openems.edge.controller.api.Controller;
import io.openems.edge.ess.api.ManagedSymmetricEss;
import io.openems.edge.ess.api.SymmetricEss;
import io.openems.edge.ess.fenecon.commercial40.EssFeneconCommercial40Impl;
import io.openems.edge.ess.fenecon.commercial40.SystemState;

@Designate(ocd = Config.class, factory = true)
@Component(name = "Project.Sambia.Controller.SupplyBusSwitch.NewVersion", immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class SupplyBusSwitchController extends AbstractOpenemsComponent implements Controller, OpenemsComponent {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private Config config;

//	private Map<String, ChannelAddress> bus1SwitchEssMapping; // maps Ess-ID to IO-ChannelAddress for Bus1
//	private Map<String, ChannelAddress> bus2SwitchEssMapping; // maps Ess-ID to IO-ChannelAddress for Bus2
//	private String primaryEssId; // Component-ID of first ESS
//	private long switchDelay;
//	private ChannelAddress supplybus1OnIndication;
//	private ChannelAddress supplybus2OnIndication;
//	private List<ChannelAddress> loads;
//	private long timeLoadSwitched = 0L;
//	private long lastTimeDisconnected;

//	private EssAndBusState state = EssAndBusState.UNDEFINED;
//	private final static int MIN_SOC = 5;

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

	public SupplyBusSwitchController() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				Controller.ChannelId.values(), //
				ChannelId.values() //
		);
	}

	@Reference
	public ComponentManager componentManager;

	@Activate
	void activate(ComponentContext context, Config config) throws OpenemsNamedException {
		super.activate(context, config.id(), config.alias(), config.enabled());
		this.config = config;
	}

	@Deactivate
	protected void deactivate() {
		super.deactivate();
	}

	@Override
	public void run() throws OpenemsNamedException {
		Channels channels = this.generateChannels();
		switch (this.getGridMode(channels)) {
		case UNDEFINED:
		case OFF_GRID:
			/*
			 * Grid-Mode is undefined -> wait till we have some clear information
			 */
			break;
		case ON_GRID:
			/*
			 * All ESS are On-Grid
			 */
			this.handleOnGrid(channels);
			break;
		}
	}

	private void handleOnGrid(Channels channels) throws IllegalArgumentException, OpenemsNamedException {
		switch (this.getTheLargestSocEssId(channels)) {
		case ESS1_BUS1_ESS2_BUS2:
			if (this.switchStates(channels) != EssAndBusState.ESS1_BUS1_ESS2_BUS2) {
				this.disconnectAllSwitches(channels);
				this.setOutput(channels.ess1ToBus1Contactor, Operation.CLOSE);
				this.setOutput(channels.ess2ToBus2Contactor, Operation.CLOSE);
			}
			break;
		case ESS1_BUS1_ESS3_BUS2:
			this.setOutput(channels.ess1ToBus1Contactor, Operation.CLOSE);
			this.setOutput(channels.ess3ToBus2Contactor, Operation.CLOSE);
			break;
		case ESS1_BUS1_ESS4_BUS2:
			this.setOutput(channels.ess1ToBus1Contactor, Operation.CLOSE);
			this.setOutput(channels.ess4ToBus2Contactor, Operation.CLOSE);
			break;
		case ESS2_BUS1_ESS1_BUS2:
			this.setOutput(channels.ess2ToBus1Contactor, Operation.CLOSE);
			this.setOutput(channels.ess1ToBus2Contactor, Operation.CLOSE);
			break;
		case ESS2_BUS1_ESS3_BUS2:
			this.setOutput(channels.ess2ToBus1Contactor, Operation.CLOSE);
			this.setOutput(channels.ess3ToBus2Contactor, Operation.CLOSE);
			break;
		case ESS2_BUS1_ESS4_BUS2:
			this.setOutput(channels.ess2ToBus1Contactor, Operation.CLOSE);
			this.setOutput(channels.ess4ToBus2Contactor, Operation.CLOSE);
			break;
		case ESS3_BUS1_ESS1_BUS2:
			this.setOutput(channels.ess3ToBus1Contactor, Operation.CLOSE);
			this.setOutput(channels.ess2ToBus2Contactor, Operation.CLOSE);
			break;
		case ESS3_BUS1_ESS2_BUS2:
			this.setOutput(channels.ess3ToBus1Contactor, Operation.CLOSE);
			this.setOutput(channels.ess2ToBus2Contactor, Operation.CLOSE);
			break;
		case ESS3_BUS1_ESS4_BUS2:
			this.setOutput(channels.ess3ToBus1Contactor, Operation.CLOSE);
			this.setOutput(channels.ess4ToBus2Contactor, Operation.CLOSE);
			break;
		case ESS4_BUS1_ESS1_BUS2:
			this.setOutput(channels.ess4ToBus1Contactor, Operation.CLOSE);
			this.setOutput(channels.ess1ToBus2Contactor, Operation.CLOSE);
			break;
		case ESS4_BUS1_ESS2_BUS2:
			this.setOutput(channels.ess4ToBus1Contactor, Operation.CLOSE);
			this.setOutput(channels.ess2ToBus2Contactor, Operation.CLOSE);
			break;
		case ESS4_BUS1_ESS3_BUS2:
			this.setOutput(channels.ess3ToBus1Contactor, Operation.CLOSE);
			this.setOutput(channels.ess3ToBus2Contactor, Operation.CLOSE);
			break;
		case UNDEFINED:
			// Get highest soc state inverters
			// Get System State
			// only connect if soc is larger than minSoc + 5 or in On-grid regardless
			// connect all loads after ess connected and started
			break;
		}
	}

	/**
	 * Gets the two ess_Id with the largest State of charge.
	 * 
	 * @return
	 * @throws OpenemsNamedException
	 */
	public EssAndBusState getTheLargestSocEssId(Channels channel) throws OpenemsNamedException {
		ManagedSymmetricEss ess;
		Optional<Integer> soc;
		Map<String, Integer> essSoc = new HashMap<String, Integer>();
		for (String essId : channel.essIds) {
			ess = this.componentManager.getComponent(essId);
			soc = ess.getSoc().value().asOptional();
			essSoc.put(essId, soc.get());
		}
		Map<String, Integer> result = essSoc.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.naturalOrder())).collect(Collectors.toMap(
						Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		channel.essIds.clear();
		essSoc.clear();
		int size = result.size() - 1;
		String largestSocEss = Iterables.get(result.entrySet(), size).getKey();
		String secondLargestSocEss = Iterables.get(result.entrySet(), size - 1).getKey();
		if (largestSocEss.equals("ess1") && secondLargestSocEss.equals("ess2")) {
			return EssAndBusState.ESS1_BUS1_ESS2_BUS2;
		}
		if (largestSocEss.equals("ess1") && secondLargestSocEss.equals("ess3")) {
			return EssAndBusState.ESS1_BUS1_ESS3_BUS2;
		}
		if (largestSocEss.equals("ess1") && secondLargestSocEss.equals("ess4")) {
			return EssAndBusState.ESS1_BUS1_ESS4_BUS2;
		}
		if (largestSocEss.equals("ess2") && secondLargestSocEss.equals("ess1")) {
			return EssAndBusState.ESS4_BUS1_ESS1_BUS2;
		}
		if (largestSocEss.equals("ess2") && secondLargestSocEss.equals("ess3")) {
			return EssAndBusState.ESS2_BUS1_ESS3_BUS2;
		}
		if (largestSocEss.equals("ess2") && secondLargestSocEss.equals("ess4")) {
			return EssAndBusState.ESS2_BUS1_ESS4_BUS2;
		}
		if (largestSocEss.equals("ess3") && secondLargestSocEss.equals("ess1")) {
			return EssAndBusState.ESS3_BUS1_ESS1_BUS2;
		}
		if (largestSocEss.equals("ess3") && secondLargestSocEss.equals("ess2")) {
			return EssAndBusState.ESS3_BUS1_ESS2_BUS2;
		}
		if (largestSocEss.equals("ess3") && secondLargestSocEss.equals("ess4")) {
			return EssAndBusState.ESS3_BUS1_ESS4_BUS2;
		}
		if (largestSocEss.equals("ess4") && secondLargestSocEss.equals("ess1")) {
			return EssAndBusState.ESS4_BUS1_ESS1_BUS2;
		}
		if (largestSocEss.equals("ess4") && secondLargestSocEss.equals("ess2")) {
			return EssAndBusState.ESS4_BUS1_ESS2_BUS2;
		}
		if (largestSocEss.equals("ess4") && secondLargestSocEss.equals("ess3")) {
			return EssAndBusState.ESS4_BUS1_ESS3_BUS2;
		}
		return EssAndBusState.UNDEFINED;
	}

	private void disconnectAllSwitches(Channels channels) throws IllegalArgumentException, OpenemsNamedException {
		this.setOutput(channels.ess1ToBus1Contactor, Operation.OPEN);
		this.setOutput(channels.ess1ToBus2Contactor, Operation.OPEN);
		this.setOutput(channels.ess2ToBus1Contactor, Operation.OPEN);
		this.setOutput(channels.ess2ToBus2Contactor, Operation.OPEN);
		this.setOutput(channels.ess3ToBus1Contactor, Operation.OPEN);
		this.setOutput(channels.ess3ToBus2Contactor, Operation.OPEN);
		this.setOutput(channels.ess4ToBus1Contactor, Operation.OPEN);
		this.setOutput(channels.ess4ToBus2Contactor, Operation.OPEN);
	}

	/**
	 * Disconnect all loads.
	 * 
	 * @return true if all loads are disconnected
	 * @throws OpenemsNamedException
	 */
	private boolean disconnectLoads() throws OpenemsNamedException {
//		if (this.timeLoadSwitched + this.switchDelay <= System.currentTimeMillis()) {
//			for (ChannelAddress load : this.loads) {
//				if (this.setOutput(load, 0)) {
//					this.timeLoadSwitched = System.currentTimeMillis();
//					// FIXME return false;
//				}
//			}
//			return true;
//		}
		return false;
	}

	/**
	 * Connect all loads.
	 * 
	 * @return true if all loads are connected
	 * @throws OpenemsNamedException
	 * @throws IllegalArgumentException
	 */
	private boolean connectLoads() throws IllegalArgumentException, OpenemsNamedException {
//		if (this.timeLoadSwitched + this.switchDelay <= System.currentTimeMillis()) {
//			for (ChannelAddress load : this.loads) {
//				if (this.setOutput(load, Operation.CLOSE)) {
//					this.timeLoadSwitched = System.currentTimeMillis();
//					// FIXME return false;
//				}
//			}
//			return true;
//		}
		return false;
	}

	/**
	 * Writes the value to supplybusOnIndication channel.
	 * 
	 * @param value
	 * @throws IllegalArgumentException
	 * @throws OpenemsNamedException
	 */
	public void setSupplybusOnIndication(int value) throws IllegalArgumentException, OpenemsNamedException {
//		IntegerWriteChannel supplybusOnIndication = this.parent.componentManager.getChannel(this.supplybusOnIndication);
//		supplybusOnIndication.setNextWriteValue(value);
	}

	private SystemState essSystemState(String essId) throws OpenemsNamedException {
		EssFeneconCommercial40Impl ess = this.componentManager.getComponent(essId);
		SystemState systemState = ess.channel(EssFeneconCommercial40Impl.ChannelId.SYSTEM_STATE).value().asEnum();
		if (systemState.equals(SystemState.FAULT)) {
			return SystemState.FAULT;
		}
		if (systemState.equals(SystemState.STOP)) {
			return SystemState.STOP;
		}
		if (systemState.equals(SystemState.START)) {
			return SystemState.START;
		}
		if (systemState.equals(SystemState.STANDBY)) {
			return SystemState.STANDBY;
		}
		return SystemState.UNDEFINED;
	}

	public EssAndBusState switchStates(Channels channel) throws IllegalArgumentException, OpenemsNamedException {

		if (this.isEssToBusContactorClosed(channel.ess1ToBus1Contactor)
				&& this.isEssToBusContactorClosed(channel.ess2ToBus2Contactor)) {
			return EssAndBusState.ESS1_BUS1_ESS2_BUS2;
		}
		if (this.isEssToBusContactorClosed(channel.ess1ToBus1Contactor)
				&& this.isEssToBusContactorClosed(channel.ess3ToBus2Contactor)) {
			return EssAndBusState.ESS1_BUS1_ESS3_BUS2;
		}
		if (this.isEssToBusContactorClosed(channel.ess1ToBus1Contactor)
				&& this.isEssToBusContactorClosed(channel.ess4ToBus2Contactor)) {
			return EssAndBusState.ESS1_BUS1_ESS4_BUS2;
		}
		if (this.isEssToBusContactorClosed(channel.ess2ToBus1Contactor)
				&& this.isEssToBusContactorClosed(channel.ess1ToBus2Contactor)) {
			return EssAndBusState.ESS2_BUS1_ESS1_BUS2;
		}
		if (this.isEssToBusContactorClosed(channel.ess2ToBus1Contactor)
				&& this.isEssToBusContactorClosed(channel.ess3ToBus2Contactor)) {
			return EssAndBusState.ESS2_BUS1_ESS3_BUS2;
		}
		if (this.isEssToBusContactorClosed(channel.ess2ToBus1Contactor)
				&& this.isEssToBusContactorClosed(channel.ess4ToBus2Contactor)) {
			return EssAndBusState.ESS2_BUS1_ESS4_BUS2;
		}
		if (this.isEssToBusContactorClosed(channel.ess3ToBus1Contactor)
				&& this.isEssToBusContactorClosed(channel.ess1ToBus2Contactor)) {
			return EssAndBusState.ESS3_BUS1_ESS1_BUS2;
		}
		if (this.isEssToBusContactorClosed(channel.ess3ToBus1Contactor)
				&& this.isEssToBusContactorClosed(channel.ess2ToBus2Contactor)) {
			return EssAndBusState.ESS3_BUS1_ESS2_BUS2;
		}
		if (this.isEssToBusContactorClosed(channel.ess3ToBus1Contactor)
				&& this.isEssToBusContactorClosed(channel.ess4ToBus2Contactor)) {
			return EssAndBusState.ESS3_BUS1_ESS4_BUS2;
		}
		if (this.isEssToBusContactorClosed(channel.ess4ToBus1Contactor)
				&& this.isEssToBusContactorClosed(channel.ess1ToBus2Contactor)) {
			return EssAndBusState.ESS4_BUS1_ESS1_BUS2;
		}
		if (this.isEssToBusContactorClosed(channel.ess4ToBus1Contactor)
				&& this.isEssToBusContactorClosed(channel.ess2ToBus2Contactor)) {
			return EssAndBusState.ESS4_BUS1_ESS2_BUS2;
		}
		if (this.isEssToBusContactorClosed(channel.ess4ToBus1Contactor)
				&& this.isEssToBusContactorClosed(channel.ess4ToBus2Contactor)) {
			return EssAndBusState.ESS4_BUS1_ESS3_BUS2;
		}
		return EssAndBusState.UNDEFINED;
	}

	private boolean isEssToBusContactorClosed(ChannelAddress channel)
			throws IllegalArgumentException, OpenemsNamedException {
		if (channel == null) {
			return false;
		}
		BooleanReadChannel essToBusContactor;
		try {
			essToBusContactor = this.componentManager.getChannel(channel);
		} catch (OpenemsNamedException e) {
			throw new OpenemsException("contactor is not exist!");
		}
		return essToBusContactor.value().orElse(false);
	}

	enum EssAndBusState {
		UNDEFINED, //
		ESS1_BUS1_ESS2_BUS2, //
		ESS1_BUS1_ESS3_BUS2, //
		ESS1_BUS1_ESS4_BUS2, //

		ESS2_BUS1_ESS1_BUS2, //
		ESS2_BUS1_ESS3_BUS2, //
		ESS2_BUS1_ESS4_BUS2, //

		ESS3_BUS1_ESS1_BUS2, //
		ESS3_BUS1_ESS2_BUS2, //
		ESS3_BUS1_ESS4_BUS2, //

		ESS4_BUS1_ESS1_BUS2, //
		ESS4_BUS1_ESS2_BUS2, //
		ESS4_BUS1_ESS3_BUS2, //
	}

	private static class Channels {
		private ChannelAddress ess1ToBus1Contactor;
		private ChannelAddress ess2ToBus1Contactor;
		private ChannelAddress ess3ToBus1Contactor;
		private ChannelAddress ess4ToBus1Contactor;
		private ChannelAddress ess1ToBus2Contactor;
		private ChannelAddress ess2ToBus2Contactor;
		private ChannelAddress ess3ToBus2Contactor;
		private ChannelAddress ess4ToBus2Contactor;

		private GridMode ess1GridMode;
		private GridMode ess2GridMode;
		private GridMode ess3GridMode;
		private GridMode ess4GridMode;

		private List<String> essIds = new ArrayList<String>();
	}

	private Channels generateChannels() throws OpenemsNamedException {
		Map<String, ChannelAddress> bus1SwitchEssMapping = new HashMap<>();
		Map<String, ChannelAddress> bus2SwitchEssMapping = new HashMap<>();
		JsonArray supplyBus1Config = JsonUtils.getAsJsonArray(JsonUtils.parse(this.config.supplyBus1Config()));
		JsonArray supplyBus2Config = JsonUtils.getAsJsonArray(JsonUtils.parse(this.config.supplyBus2Config()));
		Channels result = new Channels();
		for (JsonElement switchElement : supplyBus1Config) {
			String essId = JsonUtils.getAsString(switchElement, "ess");
			ChannelAddress channel = ChannelAddress.fromString(JsonUtils.getAsString(switchElement, "switchAddress"));
			bus1SwitchEssMapping.put(essId, channel);
			result.essIds.add(essId);
		}
		for (JsonElement switchElement2 : supplyBus2Config) {
			String essId = JsonUtils.getAsString(switchElement2, "ess");
			ChannelAddress channel = ChannelAddress.fromString(JsonUtils.getAsString(switchElement2, "switchAddress"));
			bus2SwitchEssMapping.put(essId, channel);
		}
		for (Entry<String, ChannelAddress> busSwitch : bus1SwitchEssMapping.entrySet())
			if (busSwitch.getKey().equals("ess1")) {
				result.ess1ToBus1Contactor = busSwitch.getValue();
			} else if (busSwitch.getKey().equals("ess2")) {
				result.ess2ToBus1Contactor = busSwitch.getValue();
			} else if (busSwitch.getKey().equals("ess3")) {
				result.ess3ToBus1Contactor = busSwitch.getValue();
			} else if (busSwitch.getKey().equals("ess4")) {
				result.ess4ToBus1Contactor = busSwitch.getValue();
			} else {
				throw new OpenemsException("Ess Ids Does Not Match On Bus1SwitchEssMapping !!!!! ");
			}
		for (Entry<String, ChannelAddress> busSwitch : bus2SwitchEssMapping.entrySet())
			if (busSwitch.getKey().equals("ess1")) {
				result.ess1ToBus2Contactor = busSwitch.getValue();
			} else if (busSwitch.getKey().equals("ess2")) {
				result.ess2ToBus2Contactor = busSwitch.getValue();
			} else if (busSwitch.getKey().equals("ess3")) {
				result.ess3ToBus2Contactor = busSwitch.getValue();
			} else if (busSwitch.getKey().equals("ess4")) {
				result.ess4ToBus2Contactor = busSwitch.getValue();
			} else {
				throw new OpenemsException("Ess Ids Does Not Match Bus2SwitchEssMapping !!!!! ");
			}
		return result;
	}

	/**
	 * Gets the Grid-Mode of All ESS.
	 * 
	 * @return the Grid-Mode
	 * @throws OpenemsNamedException
	 */
	private GridMode getGridMode(Channels channels) throws OpenemsNamedException {
		for (String essId : channels.essIds) {
			if (essId.equals("ess1")) {
				channels.ess1GridMode = this.getOneEssGridMode(essId);
			} else if (essId.equals("ess2")) {
				channels.ess2GridMode = this.getOneEssGridMode(essId);
			} else if (essId.equals("ess3")) {
				channels.ess3GridMode = this.getOneEssGridMode(essId);
			} else if (essId.equals("ess4")) {
				channels.ess4GridMode = this.getOneEssGridMode(essId);
			} else {
				throw new OpenemsException("Ess Id Doesnt Match On getGridMode!!!");
			}
		}
		if (channels.essIds.size() == 2) {
			if (channels.ess1GridMode.equals(GridMode.ON_GRID) && channels.ess2GridMode.equals(GridMode.ON_GRID)) {
				return GridMode.ON_GRID;
			}
		} else if (channels.essIds.size() == 3) {
			if (channels.ess1GridMode.equals(GridMode.ON_GRID) && channels.ess2GridMode.equals(GridMode.ON_GRID)
					&& channels.ess3GridMode.equals(GridMode.ON_GRID)) {
				return GridMode.ON_GRID;
			}
		} else if (channels.essIds.size() == 4) {
			if (channels.ess1GridMode.equals(GridMode.ON_GRID) && channels.ess2GridMode.equals(GridMode.ON_GRID)
					&& channels.ess3GridMode.equals(GridMode.ON_GRID)
					&& channels.ess4GridMode.equals(GridMode.ON_GRID)) {
				return GridMode.ON_GRID;
			}
		}
		return GridMode.UNDEFINED;
	}

	/**
	 * Gets the Grid-Mode of One ESS.
	 * 
	 * @return the Grid-Mode
	 * @throws OpenemsNamedException
	 */
	private GridMode getOneEssGridMode(String essId) throws OpenemsNamedException {
		SymmetricEss ess;
		try {
			ess = this.componentManager.getComponent(essId);
		} catch (OpenemsNamedException e) {
			return GridMode.UNDEFINED;
		}
		GridMode gridMode = ess.getGridMode().value().asEnum();
		if (gridMode.equals(GridMode.ON_GRID)) {
			return GridMode.ON_GRID;
		} else if (gridMode.equals(GridMode.OFF_GRID)) {
			return GridMode.OFF_GRID;
		}
		return GridMode.UNDEFINED;
	}

	private enum OneBatteryState {
		EMPTY, LOW, NORMAL, FULL, UNDEFINED;
	}

	/**
	 * Gets the Battery-State of one Ess.
	 * 
	 * @param essId the ID of the Ess
	 * @return the Battery-State
	 */
	private OneBatteryState getOneBatteryState(String essId) {
		ManagedSymmetricEss ess;
		try {
			ess = this.componentManager.getComponent(essId);
		} catch (OpenemsNamedException e) {
			return OneBatteryState.UNDEFINED;
		}

		Optional<Integer> allowedChargePower = ess.getAllowedCharge().value().asOptional();
		Optional<Integer> allowedDischargePower = ess.getAllowedDischarge().value().asOptional();
		Optional<Integer> soc = ess.getSoc().value().asOptional();
		if (allowedChargePower.isPresent() && allowedChargePower.get() == 0) {
			return OneBatteryState.FULL;
		} else if (allowedDischargePower.isPresent() && allowedDischargePower.get() == 0) {
			return OneBatteryState.EMPTY;
		} else if (soc.isPresent()) {
			if (soc.get() <= 5) {
				return OneBatteryState.LOW;
			} else {
				return OneBatteryState.NORMAL;
			}
		} else {
			return OneBatteryState.UNDEFINED;
		}
	}

	@Override
	protected void logInfo(Logger log, String message) {
		super.logInfo(log, message);
	}

	@Override
	protected void logError(Logger log, String message) {
		super.logError(log, message);
	}

	/**
	 * Helper function to switch an output if it was not switched before.
	 * 
	 * @return true if output was actually set
	 */
	private void setOutput(ChannelAddress channelAddress, Operation operation)
			throws IllegalArgumentException, OpenemsNamedException {
		try {
			IntegerWriteChannel outputChannel = this.componentManager.getChannel(channelAddress);
			Optional<Integer> currentValueOpt = outputChannel.value().asOptional();
			if (!currentValueOpt.isPresent()) {
				switch (operation) {
				case CLOSE:
					outputChannel.setNextWriteValue(0);
					this.logInfo(this.log, "Set output [" + outputChannel.address() + "] CLOSE");
					break;
				case OPEN:
					outputChannel.setNextWriteValue(1);
					this.logInfo(this.log, "Set output [" + outputChannel.address() + "] OPEN");
					break;
				case UNDEFINED:
					break;

				}
			}
		} catch (OpenemsNamedException e) {
			this.logError(this.log, "Unable to set output: [" + channelAddress + "] " + e.getMessage());
		}
	}

//	private void switchState(State nextState) {
//		this.parent.logInfo(this.log,
//				"Bus [" + this.name + "] switching from [" + this.state + "] to [" + nextState + "]");
//		this.state = nextState;
//		return;
//	}

}
