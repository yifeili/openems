package io.openems.edge.battery.soltaro.single.versionb_runnable_device;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.channel.AccessMode;
import io.openems.common.channel.Unit;
import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.api.Battery;
import io.openems.edge.battery.soltaro.ChannelIdImpl;
import io.openems.edge.battery.soltaro.ModuleParameters;
import io.openems.edge.battery.soltaro.ResetState;
import io.openems.edge.battery.soltaro.State;
import io.openems.edge.battery.soltaro.single.versionb.Enums.AutoSetFunction;
import io.openems.edge.battery.soltaro.single.versionb.Enums.ContactorControl;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.SoltaroBMS;
import io.openems.edge.bridge.modbus.api.AbstractOpenemsModbusComponent;
import io.openems.edge.bridge.modbus.api.BridgeModbus;
import io.openems.edge.bridge.modbus.api.ElementToChannelConverter;
import io.openems.edge.bridge.modbus.api.ModbusProtocol;
import io.openems.edge.bridge.modbus.api.element.AbstractModbusElement;
import io.openems.edge.bridge.modbus.api.element.BitsWordElement;
import io.openems.edge.bridge.modbus.api.element.DummyRegisterElement;
import io.openems.edge.bridge.modbus.api.element.ModbusElement;
import io.openems.edge.bridge.modbus.api.element.SignedWordElement;
import io.openems.edge.bridge.modbus.api.element.UnsignedWordElement;
import io.openems.edge.bridge.modbus.api.task.FC16WriteRegistersTask;
import io.openems.edge.bridge.modbus.api.task.FC3ReadRegistersTask;
import io.openems.edge.bridge.modbus.api.task.FC6WriteRegisterTask;
import io.openems.edge.bridge.modbus.api.task.Task;
import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.channel.EnumReadChannel;
import io.openems.edge.common.channel.EnumWriteChannel;
import io.openems.edge.common.channel.IntegerDoc;
import io.openems.edge.common.channel.IntegerReadChannel;
import io.openems.edge.common.channel.IntegerWriteChannel;
import io.openems.edge.common.channel.StateChannel;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.modbusslave.ModbusSlave;
import io.openems.edge.common.modbusslave.ModbusSlaveTable;
import io.openems.edge.common.taskmanager.Priority;


public class SingleRack implements SoltaroBMS { 
		
		// , // JsonApi // TODO

	private int numberOfSlaves;
	
	protected static final int SYSTEM_ON = 1;
	protected final static int SYSTEM_OFF = 0;
	

	private static final String KEY_TEMPERATURE = "_TEMPERATURE";
	private static final String KEY_VOLTAGE = "_VOLTAGE";
	private static final Integer SYSTEM_RESET = 0x1;
	private static final String NUMBER_FORMAT = "%03d"; // creates string number with leading zeros
	private static final double MAX_TOLERANCE_CELL_VOLTAGE_CHANGES_MILLIVOLT = 50;
	private static final double MAX_TOLERANCE_CELL_VOLTAGES_MILLIVOLT = 400;
//	private static final int SECONDS_TOLERANCE_CELL_DRIFT = 15 * 60;

//	@Reference
//	protected ConfigurationAdmin cm;

	private final Logger log = LoggerFactory.getLogger(SingleRack.class);
//	private String modbusBridgeId;
	// if configuring is needed this is used to go through the necessary steps
//	private ConfiguringProcess nextConfiguringProcess = ConfiguringProcess.NONE;
//	private Config config;
//	private Map<String, Channel<?>> channelMap;
	// If an error has occurred, this indicates the time when next action could be
	// done
//	private LocalDateTime errorDelayIsOver = null;
//	private int unsuccessfulStarts = 0;
//	private LocalDateTime startAttemptTime = null;

//	private LocalDateTime timeAfterAutoId = null;
//	private LocalDateTime configuringFinished = null;
//	private int DELAY_AUTO_ID_SECONDS = 5;
//	private int DELAY_AFTER_CONFIGURING_FINISHED = 5;

	// Remind last min and max cell voltages to register a cell drift
	private double lastMinCellVoltage = Double.MIN_VALUE;
	private double lastMaxCellVoltage = Double.MIN_VALUE;
	private ResetState resetState = ResetState.NONE;
	
	private LocalDateTime handleOneCellDriftHandlingStarted = null;
	private int handleOneCellDriftHandlingCounter = 0;

//	private LocalDateTime pendingTimestamp;

	public SingleRack(int numberOfSlaves) {
		this.numberOfSlaves = numberOfSlaves;
//		super(//
//				OpenemsComponent.ChannelId.values(), //
//				Battery.ChannelId.values(), //
//				SingleRackChannelId.values() //
//		);
	}



////	@Activate
//	void activate(ComponentContext context, Config config) {
//		this.config = config;
//
//		// adds dynamically created channels and save them into a map to access them
//		// when modbus tasks are created
//		channelMap = createDynamicChannels();
//
//		super.activate(context, config.id(), config.alias(), config.enabled(), config.modbusUnitId(), this.cm, "Modbus",
//				config.modbus_id());
//		this.modbusBridgeId = config.modbus_id();
//		initializeCallbacks();
//
//		setWatchdog(config.watchdog());
//		setSoCLowAlarm(config.SoCLowAlarm());
//		setCapacity();
//	}

//	private void setCapacity() {
//		int capacity = this.config.numberOfSlaves() * ModuleParameters.CAPACITY_WH.getValue() / 1000;
//		this.channel(Battery.ChannelId.CAPACITY).setNextValue(capacity);
//	}

//	private void handleStateMachine() {
//		switch (this.getStateMachineState()) {
//		case ERROR:
//			if (handleOneCellDriftHandlingCounter > 5) {
//				// this cell drift error seems to be not removable
//				// systems remains in error state, can only be  removed by restarting component
//			}	else {	
//				stopSystem();
//				errorDelayIsOver = LocalDateTime.now().plusSeconds(config.errorLevel2Delay());
//				setStateMachineState(State.ERRORDELAY);
//			}
//			break;
//
//		case ERRORDELAY:
//			if (LocalDateTime.now().isAfter(errorDelayIsOver)) {
//				errorDelayIsOver = null;
//				if (this.isError()) {
//					this.setStateMachineState(State.ERROR);
//				} else {
//					this.setStateMachineState(State.OFF);
//				}
//			}
//			break;
//		case INIT:
//			if (this.isSystemRunning()) {
//				this.setStateMachineState(State.RUNNING);
//				unsuccessfulStarts = 0;
//				startAttemptTime = null;
//			} else {
//				if (startAttemptTime.plusSeconds(config.maxStartTime()).isBefore(LocalDateTime.now())) {
//					startAttemptTime = null;
//					unsuccessfulStarts++;
//					this.stopSystem();
//					this.setStateMachineState(State.STOPPING);
//					if (unsuccessfulStarts >= config.maxStartAppempts()) {
//						errorDelayIsOver = LocalDateTime.now().plusSeconds(config.startUnsuccessfulDelay());
//						this.setStateMachineState(State.ERRORDELAY);
//						unsuccessfulStarts = 0;
//					}
//				}
//			}
//			break;
//		case OFF:
//			log.debug("in case 'OFF'; try to start the system");
//			this.startSystem();
//			log.debug("set state to 'INIT'");
//			this.setStateMachineState(State.INIT);
//			startAttemptTime = LocalDateTime.now();
//			break;
//		case RUNNING:
//
//			if ( // if it has run 15 minutes in normal mode, reset timer and counter
//				this.handleOneCellDriftHandlingStarted != null && // 
//				this.handleOneCellDriftHandlingStarted.plusSeconds(SECONDS_TOLERANCE_CELL_DRIFT).isBefore(LocalDateTime.now()) //
//			) { //
//				this.handleOneCellDriftHandlingStarted = null;
//				this.handleOneCellDriftHandlingCounter = 0;
//			}
//
//			if (this.isError()) {
//				this.setStateMachineState(State.ERROR);
//			} else if (!this.isSystemRunning()) {
//				this.setStateMachineState(State.UNDEFINED);
//			} else if (this.isCellVoltagesDrift()) {
//				this.setStateMachineState(State.ERROR_CELL_VOLTAGES_DRIFT);
//			} else if (this.isOneCellDrifting()) {
//				this.setStateMachineState(State.ONE_CELL_DRIFTING);
//			} else {
//				this.setStateMachineState(State.RUNNING);
//			}
//			break;
//		case STOPPING:
//			if (this.isError()) {
//				this.setStateMachineState(State.ERROR);
//			} else {
//				if (this.isSystemStopped()) {
//					this.setStateMachineState(State.OFF);
//				}
//			}
//			break;
//		case UNDEFINED:
//			if (this.isError()) {
//				this.setStateMachineState(State.ERROR);
//			} else if (this.isSystemStopped()) {
//				this.setStateMachineState(State.OFF);
//			} else if (this.isSystemRunning()) {
//				this.setStateMachineState(State.RUNNING);
//			} else if (this.isSystemStatePending()) {
//				this.setStateMachineState(State.PENDING);
//			}
//			break;
//		case PENDING:
//			if (this.pendingTimestamp == null) {
//				this.pendingTimestamp = LocalDateTime.now();
//			}
//			if (this.pendingTimestamp.plusSeconds(this.config.pendingTolerance()).isBefore(LocalDateTime.now())) {
//				// System state could not be determined, stop and start it
//				this.pendingTimestamp = null;
//				this.stopSystem();
//				this.setStateMachineState(State.OFF);
//			} else {
//				if (this.isError()) {
//					this.setStateMachineState(State.ERROR);
//					this.pendingTimestamp = null;
//				} else if (this.isSystemStopped()) {
//					this.setStateMachineState(State.OFF);
//					this.pendingTimestamp = null;
//				} else if (this.isSystemRunning()) {
//					this.setStateMachineState(State.RUNNING);
//					this.pendingTimestamp = null;
//				}
//			}
//			break;
//		case ERROR_CELL_VOLTAGES_DRIFT:
//			this.handleCellDrift();
//			break;
//		case ONE_CELL_DRIFTING:
//			this.handleOneCellDrifting();
//			break;
//		}
//
//		
//	}

//	private void handleOneCellDrifting() {
//		if (this.handleOneCellDriftHandlingStarted == null) {	
//			this.handleOneCellDriftHandlingStarted = LocalDateTime.now();			
//		}
//		
//		if (this.resetState == ResetState.NONE) {
//			handleOneCellDriftHandlingCounter++; // only increase one time per reset cycle
//		}
//		
//		this.handleCellDrift();
//		
//		
//		if (this.handleOneCellDriftHandlingCounter > 5 && this.handleOneCellDriftHandlingStarted.plusSeconds(SECONDS_TOLERANCE_CELL_DRIFT).isAfter(LocalDateTime.now())) {
//			this.setStateMachineState(State.ERROR);
//		}
//	}

//	private boolean isOneCellDrifting() {
//		/*
//		 * If voltage of one cell is going down immediately(Cell Voltage Low) and the
//		 * other cells do not (Cell diff high) that's an indicator for this error
//		 */
//		StateChannel cellVoltLowChannel = this.channel(SingleRackChannelId.ALARM_LEVEL_1_CELL_VOLTAGE_LOW);
//		StateChannel cellDiffHighChannel = this.channel(SingleRackChannelId.ALARM_LEVEL_1_CELL_VOLTAGE_DIFF_HIGH);
//
//		Optional<Boolean> cellVoltLowOpt = cellVoltLowChannel.getNextValue().asOptional();
//		Optional<Boolean> cellDiffHighOpt = cellDiffHighChannel.getNextValue().asOptional();
//
//		if (!cellVoltLowOpt.isPresent() || !cellDiffHighOpt.isPresent()) {
//			return false;
//		}
//
//		return cellVoltLowOpt.get() && cellDiffHighOpt.get();
//	}

//	private void handleCellDrift() {
//		// To reset the cell drift phenomenon, first sleep and then reset the system
//		switch (this.resetState) {
//		case NONE:
//			this.resetState = ResetState.SLEEP;
//			break;
//		case SLEEP:
//			this.sleepSystem();
//			this.resetState = ResetState.RESET;
//			break;
//		case RESET:
//			this.resetSystem();
//			this.resetState = ResetState.FINISHED;
//			break;
//		case FINISHED:
//			this.resetState = ResetState.NONE;
//			this.setStateMachineState(State.UNDEFINED);
//			break;
//		}
//	}
//
//	private void resetSystem() {
//
//		IntegerWriteChannel resetChannel = this.channel(SingleRackChannelId.SYSTEM_RESET);
//		try {
//			resetChannel.setNextWriteValue(SYSTEM_RESET);
//		} catch (OpenemsNamedException e) {
//			System.out.println("Error while trying to reset the system!");
//		}
//	}
//
//	private void sleepSystem() {
//
//		IntegerWriteChannel sleepChannel = this.channel(SingleRackChannelId.SLEEP);
//		try {
//			sleepChannel.setNextWriteValue(0x1);
//		} catch (OpenemsNamedException e) {
//			System.out.println("Error while trying to sleep the system!");
//		}
//
//	}
//
//	/*
//	 * This function tries to find out if cell voltages has been drifted away see
//	 * \doc\cell_drift.png If this phenomenon has happened, a system reset is
//	 * necessary
//	 */
//	private boolean isCellVoltagesDrift() {
//
//		Optional<Integer> maxCellVoltageOpt = this.getMaxCellVoltage().getNextValue().asOptional();
//		Optional<Integer> minCellVoltageOpt = this.getMinCellVoltage().getNextValue().asOptional();
//
//		if (!maxCellVoltageOpt.isPresent() || !minCellVoltageOpt.isPresent()) {
//			return false; // no new values, comparison not possible
//		}
//
//		double currentMaxCellVoltage = maxCellVoltageOpt.get();
//		double currentMinCellVoltage = minCellVoltageOpt.get();
//
//		if (lastMaxCellVoltage == Double.MIN_VALUE || lastMinCellVoltage == Double.MIN_VALUE) {
//			// Not all values has been set yet, check is not possible
//			lastMaxCellVoltage = currentMaxCellVoltage;
//			lastMinCellVoltage = currentMinCellVoltage;
//
//			return false;
//		}
//		double deltaMax = lastMaxCellVoltage - currentMaxCellVoltage;
//		double deltaMin = lastMinCellVoltage - currentMinCellVoltage;
//		double deltaMinMax = currentMaxCellVoltage - currentMinCellVoltage;
//
//		lastMaxCellVoltage = currentMaxCellVoltage;
//		lastMinCellVoltage = currentMinCellVoltage;
//
//		if (deltaMax < 0 && deltaMin > 0) { // max cell rises, min cell falls
//			// at least one of them changes faster than typically
//			if (deltaMinMax > MAX_TOLERANCE_CELL_VOLTAGES_MILLIVOLT && //
//					(Math.abs(deltaMax) > MAX_TOLERANCE_CELL_VOLTAGE_CHANGES_MILLIVOLT
//							|| Math.abs(deltaMin) > MAX_TOLERANCE_CELL_VOLTAGE_CHANGES_MILLIVOLT)) {
//
//				// If cells are neighbours then there is a drift error
//				Optional<Integer> minCellVoltageIdOpt = this.channel(SingleRackChannelId.CLUSTER_1_MIN_CELL_VOLTAGE_ID);
//				Optional<Integer> maxCellVoltageIdOpt = this.channel(SingleRackChannelId.CLUSTER_1_MAX_CELL_VOLTAGE_ID);
//
//				if (!minCellVoltageIdOpt.isPresent() || !maxCellVoltageIdOpt.isPresent()) {
//					return false;
//				}
//
//				int minCellVoltageId = minCellVoltageIdOpt.get();
//				int maxCellVoltageId = maxCellVoltageIdOpt.get();
//
//				if (Math.abs(minCellVoltageId - maxCellVoltageId) == 1) {
//					return true;
//				}
//
//			}
//
//		}
//
//		return false;
//	}

	/*
	 * creates a map containing channels for voltage and temperature depending on
	 * the number of modules
	 */
//	private Map<String, Channel<?>> createDynamicChannels() {
//		Map<String, Channel<?>> map = new HashMap<>();
//
//		int voltSensors = ModuleParameters.VOLTAGE_SENSORS_PER_MODULE.getValue();
//		for (int i = 0; i < this.config.numberOfSlaves(); i++) {
//			for (int j = i * voltSensors; j < (i + 1) * voltSensors; j++) {
//				String key = getSingleCellPrefix(j) + KEY_VOLTAGE;
//				IntegerDoc doc = new IntegerDoc();
//				io.openems.edge.common.channel.ChannelId channelId = new ChannelIdImpl(key, doc.unit(Unit.MILLIVOLT));
//				IntegerReadChannel integerReadChannel = (IntegerReadChannel) this.addChannel(channelId);
//				map.put(key, integerReadChannel);
//			}
//		}
//
//		int tempSensors = ModuleParameters.TEMPERATURE_SENSORS_PER_MODULE.getValue();
//		for (int i = 0; i < this.config.numberOfSlaves(); i++) {
//			for (int j = i * tempSensors; j < (i + 1) * tempSensors; j++) {
//				String key = getSingleCellPrefix(j) + KEY_TEMPERATURE;
//
//				IntegerDoc doc = new IntegerDoc();
//				io.openems.edge.common.channel.ChannelId channelId = new ChannelIdImpl(key,
//						doc.unit(Unit.DEZIDEGREE_CELSIUS));
//				IntegerReadChannel integerReadChannel = (IntegerReadChannel) this.addChannel(channelId);
//				map.put(key, integerReadChannel);
//			}
//		}
//		return map;
//	}
//
//	private String getSingleCellPrefix(int num) {
//		return "CLUSTER_1_BATTERY_" + String.format(NUMBER_FORMAT, num);
//	}
//
//	private void setWatchdog(int time_seconds) {
//		try {
//			IntegerWriteChannel c = this.channel(SingleRackChannelId.EMS_COMMUNICATION_TIMEOUT);
//			c.setNextWriteValue(time_seconds);
//		} catch (OpenemsNamedException e) {
//			log.error("Error while setting ems timeout!\n" + e.getMessage());
//		}
//	}

////	@Deactivate
//	protected void deactivate() {
//		// Remove dynamically created channels when component is deactivated
//		for (Channel<?> c : this.channelMap.values()) {
//			this.removeChannel(c);
//		}
//		super.deactivate();
//	}

//	private void initializeCallbacks() {
//
//		this.channel(SingleRackChannelId.CLUSTER_1_VOLTAGE).onChange((oldValue, newValue) -> {
//			@SuppressWarnings("unchecked")
//			Optional<Integer> vOpt = (Optional<Integer>) newValue.asOptional();
//			if (!vOpt.isPresent()) {
//				return;
//			}
//			int voltage_volt = (int) (vOpt.get() * 0.001);
//			log.debug("callback voltage, value: " + voltage_volt);
//			this.channel(Battery.ChannelId.VOLTAGE).setNextValue(voltage_volt);
//		});
//
//		this.channel(SingleRackChannelId.CLUSTER_1_MIN_CELL_VOLTAGE).onChange((oldValue, newValue) -> {
//			@SuppressWarnings("unchecked")
//			Optional<Integer> vOpt = (Optional<Integer>) newValue.asOptional();
//			if (!vOpt.isPresent()) {
//				return;
//			}
//			int voltage_millivolt = vOpt.get();
//			log.debug("callback min cell voltage, value: " + voltage_millivolt);
//			this.channel(Battery.ChannelId.MIN_CELL_VOLTAGE).setNextValue(voltage_millivolt);
//		});
//
//		// write battery ranges to according channels in battery api
//		// MAX_VOLTAGE x2082
//		this.channel(SingleRackChannelId.WARN_PARAMETER_SYSTEM_OVER_VOLTAGE_ALARM).onChange((oldValue, newValue) -> {
//			@SuppressWarnings("unchecked")
//			Optional<Integer> vOpt = (Optional<Integer>) newValue.asOptional();
//			if (!vOpt.isPresent()) {
//				return;
//			}
//			int max_charge_voltage = (int) (vOpt.get() * 0.001);
//			log.debug("callback battery range, max charge voltage, value: " + max_charge_voltage);
//			this.channel(Battery.ChannelId.CHARGE_MAX_VOLTAGE).setNextValue(max_charge_voltage);
//		});
//
//		// DISCHARGE_MIN_VOLTAGE 0x2088
//		this.channel(SingleRackChannelId.WARN_PARAMETER_SYSTEM_UNDER_VOLTAGE_ALARM).onChange((oldValue, newValue) -> {
//			@SuppressWarnings("unchecked")
//			Optional<Integer> vOpt = (Optional<Integer>) newValue.asOptional();
//			if (!vOpt.isPresent()) {
//				return;
//			}
//			int min_discharge_voltage = (int) (vOpt.get() * 0.001);
//			log.debug("callback battery range, min discharge voltage, value: " + min_discharge_voltage);
//			this.channel(Battery.ChannelId.DISCHARGE_MIN_VOLTAGE).setNextValue(min_discharge_voltage);
//		});
//
//		// CHARGE_MAX_CURRENT 0x2160
//		this.channel(SingleRackChannelId.SYSTEM_MAX_CHARGE_CURRENT).onChange((oldValue, newValue) -> {
//			@SuppressWarnings("unchecked")
//			Optional<Integer> cOpt = (Optional<Integer>) newValue.asOptional();
//			if (!cOpt.isPresent()) {
//				return;
//			}
//			int max_current = (int) (cOpt.get() * 0.001);
//			log.debug("callback battery range, max charge current, value: " + max_current);
//			this.channel(Battery.ChannelId.CHARGE_MAX_CURRENT).setNextValue(max_current);
//		});
//
//		// DISCHARGE_MAX_CURRENT 0x2161
//		this.channel(SingleRackChannelId.SYSTEM_MAX_DISCHARGE_CURRENT).onChange((oldValue, newValue) -> {
//			@SuppressWarnings("unchecked")
//			Optional<Integer> cOpt = (Optional<Integer>) newValue.asOptional();
//			if (!cOpt.isPresent()) {
//				return;
//			}
//			int max_current = (int) (cOpt.get() * 0.001);
//			log.debug("callback battery range, max discharge current, value: " + max_current);
//			this.channel(Battery.ChannelId.DISCHARGE_MAX_CURRENT).setNextValue(max_current);
//		});
//
//	}
//
//	private void handleBatteryState() {
//		switch (config.batteryState()) {
//		case DEFAULT:
//			handleStateMachine();
//			break;
//		case OFF:
//			stopSystem();
//			break;
//		case ON:
//			startSystem();
//			break;
//		case CONFIGURE:
//			configureSlaves();
//			break;
//
//		}
//	}
//
//	private void configureSlaves() {
//		if (nextConfiguringProcess == ConfiguringProcess.NONE) {
//			nextConfiguringProcess = ConfiguringProcess.CONFIGURING_STARTED;
//		}
//
//		switch (nextConfiguringProcess) {
//		case CONFIGURING_STARTED:
//			System.out.println(" ===> CONFIGURING STARTED: setNumberOfModules() <===");
//			setNumberOfModules();
//			break;
//		case SET_ID_AUTO_CONFIGURING:
//			System.out.println(" ===> SET_ID_AUTO_CONFIGURING: setIdAutoConfiguring() <===");
//			setIdAutoConfiguring();
//			break;
//		case CHECK_ID_AUTO_CONFIGURING:
//			if (timeAfterAutoId != null) {
//				if (timeAfterAutoId.plusSeconds(DELAY_AUTO_ID_SECONDS).isAfter(LocalDateTime.now())) {
//					break;
//				} else {
//					timeAfterAutoId = null;
//				}
//			}
//			System.out.println(" ===> CHECK_ID_AUTO_CONFIGURING: checkIdAutoConfiguring() <===");
//			checkIdAutoConfiguring();
//			break;
//		case SET_TEMPERATURE_ID_AUTO_CONFIGURING:
//			System.out.println(" ===> SET_TEMPERATURE_ID_AUTO_CONFIGURING: setTemperatureIdAutoConfiguring() <===");
//			setTemperatureIdAutoConfiguring();
//			break;
//		case CHECK_TEMPERATURE_ID_AUTO_CONFIGURING:
//			if (timeAfterAutoId != null) {
//				if (timeAfterAutoId.plusSeconds(DELAY_AUTO_ID_SECONDS).isAfter(LocalDateTime.now())) {
//					break;
//				} else {
//					timeAfterAutoId = null;
//				}
//			}
//			System.out.println(" ===> CHECK_TEMPERATURE_ID_AUTO_CONFIGURING: checkTemperatureIdAutoConfiguring() <===");
//			checkTemperatureIdAutoConfiguring();
//			break;
//		case SET_VOLTAGE_RANGES:
//			System.out.println(" ===> SET_VOLTAGE_RANGES: setVoltageRanges() <===");
//			setVoltageRanges();
//
//			break;
//		case CONFIGURING_FINISHED:
//			System.out.println("====>>> Configuring successful! <<<====");
//
//			if (configuringFinished == null) {
//				nextConfiguringProcess = ConfiguringProcess.RESTART_AFTER_SETTING;
//			} else {
//				if (configuringFinished.plusSeconds(DELAY_AFTER_CONFIGURING_FINISHED).isAfter(LocalDateTime.now())) {
//					System.out.println(">>> Delay time after configuring!");
//				} else {
//					System.out.println("Delay time after configuring is over, reset system");
//					IntegerWriteChannel resetChannel = this.channel(SingleRackChannelId.SYSTEM_RESET);
//					try {
//						resetChannel.setNextWriteValue(SYSTEM_RESET);
//						configuringFinished = null;
//					} catch (OpenemsNamedException e) {
//						System.out.println("Error while trying to reset the system!");
//					}
//				}
//			}
//			break;
//		case RESTART_AFTER_SETTING:
//			// A manual restart is needed
//			System.out.println("====>>>  Please restart system manually!");
//		case NONE:
//			break;
//		}
//	}
//
//	private void setVoltageRanges() {
//
//		try {
//			IntegerWriteChannel level1OverVoltageChannel = this
//					.channel(SingleRackChannelId.WARN_PARAMETER_SYSTEM_OVER_VOLTAGE_ALARM);
//			level1OverVoltageChannel.setNextWriteValue(
//					this.config.numberOfSlaves() * ModuleParameters.LEVEL_1_TOTAL_OVER_VOLTAGE_MILLIVOLT.getValue());
//
//			IntegerWriteChannel level1OverVoltageChannelRecover = this
//					.channel(SingleRackChannelId.WARN_PARAMETER_SYSTEM_OVER_VOLTAGE_RECOVER);
//			level1OverVoltageChannelRecover.setNextWriteValue(this.config.numberOfSlaves()
//					* ModuleParameters.LEVEL_1_TOTAL_OVER_VOLTAGE_RECOVER_MILLIVOLT.getValue());
//
//			IntegerWriteChannel level1LowVoltageChannel = this
//					.channel(SingleRackChannelId.WARN_PARAMETER_SYSTEM_UNDER_VOLTAGE_ALARM);
//			level1LowVoltageChannel.setNextWriteValue(
//					this.config.numberOfSlaves() * ModuleParameters.LEVEL_1_TOTAL_LOW_VOLTAGE_MILLIVOLT.getValue());
//
//			IntegerWriteChannel level1LowVoltageChannelRecover = this
//					.channel(SingleRackChannelId.WARN_PARAMETER_SYSTEM_UNDER_VOLTAGE_RECOVER);
//			level1LowVoltageChannelRecover.setNextWriteValue(this.config.numberOfSlaves()
//					* ModuleParameters.LEVEL_1_TOTAL_LOW_VOLTAGE_RECOVER_MILLIVOLT.getValue());
//
//			IntegerWriteChannel level2OverVoltageChannel = this
//					.channel(SingleRackChannelId.STOP_PARAMETER_SYSTEM_OVER_VOLTAGE_PROTECTION);
//			level2OverVoltageChannel.setNextWriteValue(
//					this.config.numberOfSlaves() * ModuleParameters.LEVEL_2_TOTAL_OVER_VOLTAGE_MILLIVOLT.getValue());
//
//			IntegerWriteChannel level2OverVoltageChannelRecover = this
//					.channel(SingleRackChannelId.STOP_PARAMETER_SYSTEM_OVER_VOLTAGE_RECOVER);
//			level2OverVoltageChannelRecover.setNextWriteValue(this.config.numberOfSlaves()
//					* ModuleParameters.LEVEL_2_TOTAL_OVER_VOLTAGE_RECOVER_MILLIVOLT.getValue());
//
//			IntegerWriteChannel level2LowVoltageChannel = this
//					.channel(SingleRackChannelId.STOP_PARAMETER_SYSTEM_UNDER_VOLTAGE_PROTECTION);
//			level2LowVoltageChannel.setNextWriteValue(
//					this.config.numberOfSlaves() * ModuleParameters.LEVEL_2_TOTAL_LOW_VOLTAGE_MILLIVOLT.getValue());
//
//			IntegerWriteChannel level2LowVoltageChannelRecover = this
//					.channel(SingleRackChannelId.STOP_PARAMETER_SYSTEM_UNDER_VOLTAGE_RECOVER);
//			level2LowVoltageChannelRecover.setNextWriteValue(this.config.numberOfSlaves()
//					* ModuleParameters.LEVEL_2_TOTAL_LOW_VOLTAGE_RECOVER_MILLIVOLT.getValue());
//
//			nextConfiguringProcess = ConfiguringProcess.CONFIGURING_FINISHED;
//			configuringFinished = LocalDateTime.now();
//
//		} catch (OpenemsNamedException e) {
//			log.error("Setting voltage ranges not successful!");
//		}
//
//	}
//
//	private void checkTemperatureIdAutoConfiguring() {
//		IntegerReadChannel autoSetTemperatureSlavesIdChannel = this
//				.channel(SingleRackChannelId.AUTO_SET_SLAVES_TEMPERATURE_ID);
//		Optional<Integer> autoSetTemperatureSlavesIdOpt = autoSetTemperatureSlavesIdChannel.value().asOptional();
//		if (!autoSetTemperatureSlavesIdOpt.isPresent()) {
//			return;
//		}
//		int autoSetTemperatureSlaves = autoSetTemperatureSlavesIdOpt.get();
//		if (autoSetTemperatureSlaves == Enums.AutoSetFunction.FAILURE.getValue()) {
//			log.error("Auto set temperature slaves id failed! Start configuring process again!");
//			// Auto set failed, try again
//			nextConfiguringProcess = ConfiguringProcess.CONFIGURING_STARTED;
//		} else if (autoSetTemperatureSlaves == Enums.AutoSetFunction.SUCCES.getValue()) {
//			log.info("Auto set temperature slaves id succeeded!");
//			nextConfiguringProcess = ConfiguringProcess.SET_VOLTAGE_RANGES;
//		}
//	}
//
//	private void setTemperatureIdAutoConfiguring() {
//
//		IntegerWriteChannel autoSetSlavesTemperatureIdChannel = this
//				.channel(SingleRackChannelId.AUTO_SET_SLAVES_TEMPERATURE_ID);
//		try {
//			autoSetSlavesTemperatureIdChannel.setNextWriteValue(AutoSetFunction.START_AUTO_SETTING.getValue());
//			timeAfterAutoId = LocalDateTime.now();
//			nextConfiguringProcess = ConfiguringProcess.CHECK_TEMPERATURE_ID_AUTO_CONFIGURING;
//		} catch (OpenemsNamedException e) {
//			log.error("Setting temperature id auto set not successful"); // Set was not successful, it will be tried
//																			// until it succeeded
//		}
//	}
//
//	private void checkIdAutoConfiguring() {
//		IntegerReadChannel autoSetSlavesIdChannel = this.channel(SingleRackChannelId.AUTO_SET_SLAVES_ID);
//		Optional<Integer> autoSetSlavesIdOpt = autoSetSlavesIdChannel.value().asOptional();
//		if (!autoSetSlavesIdOpt.isPresent()) {
//			return;
//		}
//		int autoSetSlaves = autoSetSlavesIdOpt.get();
//		if (autoSetSlaves == Enums.AutoSetFunction.FAILURE.getValue()) {
//			log.error("Auto set slaves id failed! Start configuring process again!");
//			// Auto set failed, try again
//			nextConfiguringProcess = ConfiguringProcess.CONFIGURING_STARTED;
//		} else if (autoSetSlaves == Enums.AutoSetFunction.SUCCES.getValue()) {
//			log.info("Auto set slaves id succeeded!");
//			nextConfiguringProcess = ConfiguringProcess.SET_TEMPERATURE_ID_AUTO_CONFIGURING;
//		}
//	}
//
//	private void setIdAutoConfiguring() {
//		// Set number of modules
//		IntegerWriteChannel autoSetSlavesIdChannel = this.channel(SingleRackChannelId.AUTO_SET_SLAVES_ID);
//		try {
//			autoSetSlavesIdChannel.setNextWriteValue(AutoSetFunction.START_AUTO_SETTING.getValue());
//			timeAfterAutoId = LocalDateTime.now();
//			nextConfiguringProcess = ConfiguringProcess.CHECK_ID_AUTO_CONFIGURING;
//		} catch (OpenemsNamedException e) {
//			log.error("Setting slave numbers not successful"); // Set was not successful, it will be tried until it
//																// succeeded
//		}
//	}
//
//	private void setNumberOfModules() {
//		// Set number of modules
//		IntegerWriteChannel numberOfSlavesChannel = this
//				.channel(SingleRackChannelId.WORK_PARAMETER_PCS_COMMUNICATION_RATE);
//		try {
//			numberOfSlavesChannel.setNextWriteValue(this.config.numberOfSlaves());
//			nextConfiguringProcess = ConfiguringProcess.SET_ID_AUTO_CONFIGURING;
//		} catch (OpenemsNamedException e) {
//			log.error("Setting slave numbers not successful"); // Set was not successful, it will be tried until it
//																// succeeded
//		}
//	}
//
//	private enum ConfiguringProcess {
//		NONE, CONFIGURING_STARTED, SET_ID_AUTO_CONFIGURING, CHECK_ID_AUTO_CONFIGURING,
//		SET_TEMPERATURE_ID_AUTO_CONFIGURING, CHECK_TEMPERATURE_ID_AUTO_CONFIGURING, SET_VOLTAGE_RANGES,
//		CONFIGURING_FINISHED, RESTART_AFTER_SETTING
//	}
//
//	private boolean isSystemRunning() {
//		EnumReadChannel contactorControlChannel = this.channel(SingleRackChannelId.BMS_CONTACTOR_CONTROL);
//		ContactorControl cc = contactorControlChannel.value().asEnum();
//		return cc == ContactorControl.ON_GRID;
//	}
//
//	private boolean isSystemStopped() {
//		EnumReadChannel contactorControlChannel = this.channel(SingleRackChannelId.BMS_CONTACTOR_CONTROL);
//		ContactorControl cc = contactorControlChannel.value().asEnum();
//		return cc == ContactorControl.CUT_OFF;
//	}
//
//	/**
//	 * Checks whether system has an undefined state, e.g. rack 1 & 2 are configured,
//	 * but only rack 1 is running. This state can only be reached at startup coming
//	 * from state undefined
//	 */
//	private boolean isSystemStatePending() {
//		return !isSystemRunning() && !isSystemStopped();
//	}

	private boolean isAlarmLevel2Error() {
		return (readValueFromBooleanChannel(SingleRackChannelId.ALARM_LEVEL_2_CELL_VOLTAGE_HIGH)
				|| readValueFromBooleanChannel(SingleRackChannelId.ALARM_LEVEL_2_TOTAL_VOLTAGE_HIGH)
				|| readValueFromBooleanChannel(SingleRackChannelId.ALARM_LEVEL_2_CHA_CURRENT_HIGH)
				|| readValueFromBooleanChannel(SingleRackChannelId.ALARM_LEVEL_2_CELL_VOLTAGE_LOW)
				|| readValueFromBooleanChannel(SingleRackChannelId.ALARM_LEVEL_2_TOTAL_VOLTAGE_LOW)
				|| readValueFromBooleanChannel(SingleRackChannelId.ALARM_LEVEL_2_DISCHA_CURRENT_HIGH)
				|| readValueFromBooleanChannel(SingleRackChannelId.ALARM_LEVEL_2_CELL_CHA_TEMP_HIGH)
				|| readValueFromBooleanChannel(SingleRackChannelId.ALARM_LEVEL_2_CELL_CHA_TEMP_LOW)
				|| readValueFromBooleanChannel(SingleRackChannelId.ALARM_LEVEL_2_SOC_LOW)
				|| readValueFromBooleanChannel(SingleRackChannelId.ALARM_LEVEL_2_TEMPERATURE_DIFFERENCE_HIGH)
				|| readValueFromBooleanChannel(SingleRackChannelId.ALARM_LEVEL_2_POLES_TEMPERATURE_DIFFERENCE_HIGH)
				|| readValueFromBooleanChannel(SingleRackChannelId.ALARM_LEVEL_2_CELL_VOLTAGE_DIFFERENCE_HIGH)
				|| readValueFromBooleanChannel(SingleRackChannelId.ALARM_LEVEL_2_INSULATION_LOW)
				|| readValueFromBooleanChannel(SingleRackChannelId.ALARM_LEVEL_2_TOTAL_VOLTAGE_DIFFERENCE_HIGH)
				|| readValueFromBooleanChannel(SingleRackChannelId.ALARM_LEVEL_2_CELL_DISCHA_TEMP_HIGH)
				|| readValueFromBooleanChannel(SingleRackChannelId.ALARM_LEVEL_2_CELL_DISCHA_TEMP_LOW));
	}

	private boolean isSlaveCommunicationError() {
		boolean b = false;
		switch (this.numberOfSlaves) {
		case 20:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_20_COMMUNICATION_ERROR);
		case 19:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_19_COMMUNICATION_ERROR);
		case 18:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_18_COMMUNICATION_ERROR);
		case 17:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_17_COMMUNICATION_ERROR);
		case 16:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_16_COMMUNICATION_ERROR);
		case 15:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_15_COMMUNICATION_ERROR);
		case 14:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_14_COMMUNICATION_ERROR);
		case 13:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_13_COMMUNICATION_ERROR);
		case 12:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_12_COMMUNICATION_ERROR);
		case 11:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_11_COMMUNICATION_ERROR);
		case 10:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_10_COMMUNICATION_ERROR);
		case 9:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_9_COMMUNICATION_ERROR);
		case 8:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_8_COMMUNICATION_ERROR);
		case 7:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_7_COMMUNICATION_ERROR);
		case 6:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_6_COMMUNICATION_ERROR);
		case 5:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_5_COMMUNICATION_ERROR);
		case 4:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_4_COMMUNICATION_ERROR);
		case 3:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_3_COMMUNICATION_ERROR);
		case 2:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_2_COMMUNICATION_ERROR);
		case 1:
			b = b || readValueFromBooleanChannel(SingleRackChannelId.SLAVE_1_COMMUNICATION_ERROR);
		}

		return b;
	}

	@Override
	public boolean isError() {
		return isAlarmLevel2Error() || isSlaveCommunicationError();
	}

	private boolean readValueFromBooleanChannel(SingleRackChannelId singleRackChannelId) {
//		StateChannel r = this.channel(singleRackChannelId);
//		Optional<Boolean> bOpt = r.value().asOptional();
		return false;//bOpt.isPresent() && bOpt.get();
	}

//	public String getModbusBridgeId() {
//		return modbusBridgeId;
//	}



//	private void startSystem() {
//		EnumWriteChannel contactorControlChannel = this.channel(SingleRackChannelId.BMS_CONTACTOR_CONTROL);
//		ContactorControl cc = contactorControlChannel.value().asEnum();
//		// To avoid hardware damages do not send start command if system has already
//		// started
//		if (cc == ContactorControl.ON_GRID || cc == ContactorControl.CONNECTION_INITIATING) {
//			return;
//		}
//
//		try {
//			log.debug("write value to contactor control channel: value: " + SYSTEM_ON);
//			contactorControlChannel.setNextWriteValue(SYSTEM_ON);
//		} catch (OpenemsNamedException e) {
//			log.error("Error while trying to start system\n" + e.getMessage());
//		}
//	}
//
//	private void stopSystem() {
//		EnumWriteChannel contactorControlChannel = this.channel(SingleRackChannelId.BMS_CONTACTOR_CONTROL);
//		ContactorControl cc = contactorControlChannel.value().asEnum();
//		// To avoid hardware damages do not send stop command if system has already
//		// stopped
//		if (cc == ContactorControl.CUT_OFF) {
//			return;
//		}
//
//		try {
//			log.debug("write value to contactor control channel: value: " + SYSTEM_OFF);
//			contactorControlChannel.setNextWriteValue(SYSTEM_OFF);
//		} catch (OpenemsNamedException e) {
//			log.error("Error while trying to stop system\n" + e.getMessage());
//		}
//	}


//	private void setSoCLowAlarm(int soCLowAlarm) {
//		try {
//			((IntegerWriteChannel) this.channel(SingleRackChannelId.STOP_PARAMETER_SOC_LOW_PROTECTION))
//					.setNextWriteValue(soCLowAlarm);
//			((IntegerWriteChannel) this.channel(SingleRackChannelId.STOP_PARAMETER_SOC_LOW_PROTECTION_RECOVER))
//					.setNextWriteValue(soCLowAlarm);
//		} catch (OpenemsNamedException e) {
//			log.error("Error while setting parameter for soc low protection!" + e.getMessage());
//		}
//	}

	Collection<TaskDescription> geTaskDescriptions(boolean reduceTasks) {
				
		//TODO
		return null;
	}
	
	
	
	//TODO to make it more simple remove "impl modbus" and return in this component only the tasks		

	

	@Override
	public void start() throws OpenemsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() throws OpenemsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStopped() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCommunicationAvailable() {
		//TODO get the bridge and check for availability
		
		return false;
	}

	@Override
	public boolean isErrorLevel2() {
		// TODO Auto-generated method stub
		return false;
	}
}
