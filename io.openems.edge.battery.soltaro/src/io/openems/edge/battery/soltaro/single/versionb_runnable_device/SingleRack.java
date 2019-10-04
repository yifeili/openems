package io.openems.edge.battery.soltaro.single.versionb_runnable_device;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.channel.Unit;
import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.api.Battery;
import io.openems.edge.battery.soltaro.ChannelIdImpl;
import io.openems.edge.battery.soltaro.ModuleParameters;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.Enums.ContactorControl;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.SoltaroBMS;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state.CommandDevice;
import io.openems.edge.bridge.modbus.api.AbstractOpenemsModbusComponent;
import io.openems.edge.bridge.modbus.api.ElementToChannelConverter;
import io.openems.edge.bridge.modbus.api.element.AbstractModbusElement;
import io.openems.edge.bridge.modbus.api.element.BitsWordElement;
import io.openems.edge.bridge.modbus.api.element.DummyRegisterElement;
import io.openems.edge.bridge.modbus.api.element.SignedWordElement;
import io.openems.edge.bridge.modbus.api.element.UnsignedWordElement;
import io.openems.edge.bridge.modbus.api.task.FC16WriteRegistersTask;
import io.openems.edge.bridge.modbus.api.task.FC3ReadRegistersTask;
import io.openems.edge.bridge.modbus.api.task.FC6WriteRegisterTask;
import io.openems.edge.bridge.modbus.api.task.Task;
import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.channel.ChannelId;
import io.openems.edge.common.channel.IntegerDoc;
import io.openems.edge.common.channel.IntegerReadChannel;
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

	private static final Object SYSTEM_SLEEP = 0x1;

	private final Logger log = LoggerFactory.getLogger(SingleRack.class);

	private Map<String, Channel<?>> channelMap;

	private CommandDevice commandDevice;

	private boolean reduceTasks;


	public SingleRack(int numberOfSlaves, boolean reduceTasks, CommandDevice commandDevice,
			AbstractOpenemsModbusComponent modbusComponent) {
		this.numberOfSlaves = numberOfSlaves;
		this.reduceTasks = reduceTasks;
		this.commandDevice = commandDevice;
		channelMap = createDynamicChannels(modbusComponent);
	}

	private void setCapacity() {
		int capacity = this.numberOfSlaves * ModuleParameters.CAPACITY_WH.getValue() / 1000;
		this.commandDevice.setValue(Battery.ChannelId.CAPACITY, capacity);
	}

	private void writeValue(ChannelId channelId, Object value) {
		try {
			this.commandDevice.setWriteValue(channelId, value);
		} catch (Exception e) {
			System.out.println("Error while trying to write value '" + value + "' to channel " + channelId + "!");
		}
	}

	private Object readValue(ChannelId channelId) {
		try {
			return this.commandDevice.readValue(channelId);
		} catch (Exception e) {
			System.out.println("Error while trying to read value from channel " + channelId + "!");
		}
		return null;
	}

	private void resetSystem() {
		writeValue(SingleRackChannelId.SYSTEM_RESET, SYSTEM_RESET);
	}

	private void sleepSystem() {
		writeValue(SingleRackChannelId.SLEEP, SYSTEM_SLEEP);
	}

	private void setWatchdog(int time_seconds) {
		writeValue(SingleRackChannelId.EMS_COMMUNICATION_TIMEOUT, time_seconds);
	}

	private boolean isSystemRunning() {
		ContactorControl cc = (ContactorControl) readValue(SingleRackChannelId.BMS_CONTACTOR_CONTROL);
		return cc == ContactorControl.ON_GRID;
	}

	private boolean isSystemStopped() {
		ContactorControl cc = (ContactorControl) readValue(SingleRackChannelId.BMS_CONTACTOR_CONTROL);
		return cc == ContactorControl.CUT_OFF;
	}

	/**
	 * Checks whether system has an undefined state, e.g. rack 1 & 2 are configured,
	 * but only rack 1 is running. This state can only be reached at startup coming
	 * from state undefined
	 */
	private boolean isSystemStatePending() {
		return !isSystemRunning() && !isSystemStopped();
	}

	private boolean isAlarmLevel2Error() {

		return (boolean) readValue(SingleRackChannelId.ALARM_LEVEL_2_CELL_VOLTAGE_HIGH)
				|| (boolean) readValue(SingleRackChannelId.ALARM_LEVEL_2_TOTAL_VOLTAGE_HIGH)
				|| (boolean) readValue(SingleRackChannelId.ALARM_LEVEL_2_CHA_CURRENT_HIGH)
				|| (boolean) readValue(SingleRackChannelId.ALARM_LEVEL_2_CELL_VOLTAGE_LOW)
				|| (boolean) readValue(SingleRackChannelId.ALARM_LEVEL_2_TOTAL_VOLTAGE_LOW)
				|| (boolean) readValue(SingleRackChannelId.ALARM_LEVEL_2_DISCHA_CURRENT_HIGH)
				|| (boolean) readValue(SingleRackChannelId.ALARM_LEVEL_2_CELL_CHA_TEMP_HIGH)
				|| (boolean) readValue(SingleRackChannelId.ALARM_LEVEL_2_CELL_CHA_TEMP_LOW)
				|| (boolean) readValue(SingleRackChannelId.ALARM_LEVEL_2_SOC_LOW)
				|| (boolean) readValue(SingleRackChannelId.ALARM_LEVEL_2_TEMPERATURE_DIFFERENCE_HIGH)
				|| (boolean) readValue(SingleRackChannelId.ALARM_LEVEL_2_POLES_TEMPERATURE_DIFFERENCE_HIGH)
				|| (boolean) readValue(SingleRackChannelId.ALARM_LEVEL_2_CELL_VOLTAGE_DIFFERENCE_HIGH)
				|| (boolean) readValue(SingleRackChannelId.ALARM_LEVEL_2_INSULATION_LOW)
				|| (boolean) readValue(SingleRackChannelId.ALARM_LEVEL_2_TOTAL_VOLTAGE_DIFFERENCE_HIGH)
				|| (boolean) readValue(SingleRackChannelId.ALARM_LEVEL_2_CELL_DISCHA_TEMP_HIGH)
				|| (boolean) readValue(SingleRackChannelId.ALARM_LEVEL_2_CELL_DISCHA_TEMP_LOW);
	}

	private boolean isSlaveCommunicationError() {
		boolean b = false;
		switch (this.numberOfSlaves) {
		case 20:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_20_COMMUNICATION_ERROR);
		case 19:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_19_COMMUNICATION_ERROR);
		case 18:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_18_COMMUNICATION_ERROR);
		case 17:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_17_COMMUNICATION_ERROR);
		case 16:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_16_COMMUNICATION_ERROR);
		case 15:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_15_COMMUNICATION_ERROR);
		case 14:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_14_COMMUNICATION_ERROR);
		case 13:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_13_COMMUNICATION_ERROR);
		case 12:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_12_COMMUNICATION_ERROR);
		case 11:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_11_COMMUNICATION_ERROR);
		case 10:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_10_COMMUNICATION_ERROR);
		case 9:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_9_COMMUNICATION_ERROR);
		case 8:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_8_COMMUNICATION_ERROR);
		case 7:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_7_COMMUNICATION_ERROR);
		case 6:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_6_COMMUNICATION_ERROR);
		case 5:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_5_COMMUNICATION_ERROR);
		case 4:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_4_COMMUNICATION_ERROR);
		case 3:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_3_COMMUNICATION_ERROR);
		case 2:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_2_COMMUNICATION_ERROR);
		case 1:
			b = b || (boolean) readValue(SingleRackChannelId.SLAVE_1_COMMUNICATION_ERROR);
		}

		return b;
	}

	@Override
	public boolean isError() {
		return isAlarmLevel2Error() || isSlaveCommunicationError();
	}

	private void startSystem() {
		ContactorControl cc = (ContactorControl) readValue(SingleRackChannelId.BMS_CONTACTOR_CONTROL);

		// To avoid hardware damages do not send start command if system has already
		// started
		if (cc == ContactorControl.ON_GRID || cc == ContactorControl.CONNECTION_INITIATING) {
			return;
		}

		try {
			log.debug("write value to contactor control channel: value: " + SYSTEM_ON);
			writeValue(SingleRackChannelId.BMS_CONTACTOR_CONTROL, SYSTEM_ON);
		} catch (Exception e) {
			log.error("Error while trying to start system\n" + e.getMessage());
		}
	}

	private void stopSystem() {
		ContactorControl cc = (ContactorControl) readValue(SingleRackChannelId.BMS_CONTACTOR_CONTROL);
		// To avoid hardware damages do not send stop command if system has already
		// stopped
		if (cc == ContactorControl.CUT_OFF) {
			return;
		}

		try {
			log.debug("write value to contactor control channel: value: " + SYSTEM_OFF);
			writeValue(SingleRackChannelId.BMS_CONTACTOR_CONTROL, SYSTEM_OFF);
		} catch (Exception e) {
			log.error("Error while trying to stop system\n" + e.getMessage());
		}
	}

	private void setSoCLowAlarm(int soCLowAlarm) {
		try {
			writeValue(SingleRackChannelId.STOP_PARAMETER_SOC_LOW_PROTECTION, soCLowAlarm);
			writeValue(SingleRackChannelId.STOP_PARAMETER_SOC_LOW_PROTECTION_RECOVER, soCLowAlarm);
		} catch (Exception e) {
			log.error("Error while setting parameter for soc low protection!" + e.getMessage());
		}
	}
	

	@Override
	public void start() throws OpenemsException {
		this.startSystem();
	}

	@Override
	public void stop() throws OpenemsException {
		this.stopSystem();
	}

	@Override
	public boolean isRunning() {
		return this.isSystemRunning();
	}

	@Override
	public boolean isStopped() {
		return this.isSystemStopped();
	}

	@Override
	public boolean isCommunicationAvailable() {
		return false; // TODO
	}

	@Override
	public boolean isErrorLevel2() {
		return this.isAlarmLevel2Error();
	}

	Collection<Task> getTasks(AbstractOpenemsModbusComponent modbusComponent) {
		Collection<Task> tasks = new ArrayList<>();

		// Main switch
		tasks.add(new FC6WriteRegisterTask(0x2010,
				modbusComponent.m(SingleRackChannelId.BMS_CONTACTOR_CONTROL, new UnsignedWordElement(0x2010))));

		// System reset
		tasks.add(new FC6WriteRegisterTask(0x2004,
				modbusComponent.m(SingleRackChannelId.SYSTEM_RESET, new UnsignedWordElement(0x2004))));

		// EMS timeout --> Watchdog
		tasks.add(new FC6WriteRegisterTask(0x201C,
				modbusComponent.m(SingleRackChannelId.EMS_COMMUNICATION_TIMEOUT, new UnsignedWordElement(0x201C)))); //

		// Sleep
		tasks.add(new FC6WriteRegisterTask(0x201D,
				modbusComponent.m(SingleRackChannelId.SLEEP, new UnsignedWordElement(0x201D)))); //

		// Work parameter
		tasks.add(new FC6WriteRegisterTask(0x20C1, modbusComponent
				.m(SingleRackChannelId.WORK_PARAMETER_PCS_COMMUNICATION_RATE, new UnsignedWordElement(0x20C1)))); //

		// Paramaeters for configuring
		tasks.add(new FC6WriteRegisterTask(0x2014,
				modbusComponent.m(SingleRackChannelId.AUTO_SET_SLAVES_ID, new UnsignedWordElement(0x2014))));
		tasks.add(new FC6WriteRegisterTask(0x2019, modbusComponent.m(SingleRackChannelId.AUTO_SET_SLAVES_TEMPERATURE_ID,
				new UnsignedWordElement(0x2019))));

		// Control registers
		tasks.add(new FC3ReadRegistersTask(0x2000, Priority.HIGH, //
				modbusComponent.m(SingleRackChannelId.FAN_STATUS, new UnsignedWordElement(0x2000)), //
				modbusComponent.m(SingleRackChannelId.MAIN_CONTACTOR_STATE, new UnsignedWordElement(0x2001)), //
				modbusComponent.m(SingleRackChannelId.DRY_CONTACT_1_EXPORT, new UnsignedWordElement(0x2002)), //
				modbusComponent.m(SingleRackChannelId.DRY_CONTACT_2_EXPORT, new UnsignedWordElement(0x2003)), //
				modbusComponent.m(SingleRackChannelId.SYSTEM_RESET, new UnsignedWordElement(0x2004)), //
				modbusComponent.m(SingleRackChannelId.SYSTEM_RUN_MODE, new UnsignedWordElement(0x2005)), //
				modbusComponent.m(SingleRackChannelId.PRE_CONTACTOR_STATUS, new UnsignedWordElement(0x2006)), //
				modbusComponent.m(new BitsWordElement(0x2007, modbusComponent) //
						.bit(15, SingleRackChannelId.ALARM_FLAG_STATUS_DISCHARGE_TEMPERATURE_LOW) //
						.bit(14, SingleRackChannelId.ALARM_FLAG_STATUS_DISCHARGE_TEMPERATURE_HIGH) //
						.bit(13, SingleRackChannelId.ALARM_FLAG_STATUS_VOLTAGE_DIFFERENCE) //
						.bit(12, SingleRackChannelId.ALARM_FLAG_STATUS_INSULATION_LOW) //
						.bit(11, SingleRackChannelId.ALARM_FLAG_STATUS_CELL_VOLTAGE_DIFFERENCE) //
						.bit(10, SingleRackChannelId.ALARM_FLAG_STATUS_ELECTRODE_TEMPERATURE_HIGH) //
						.bit(9, SingleRackChannelId.ALARM_FLAG_STATUS_TEMPERATURE_DIFFERENCE) //
						.bit(8, SingleRackChannelId.ALARM_FLAG_STATUS_SOC_LOW) //
						.bit(7, SingleRackChannelId.ALARM_FLAG_STATUS_CELL_OVER_TEMPERATURE) //
						.bit(6, SingleRackChannelId.ALARM_FLAG_STATUS_CELL_LOW_TEMPERATURE) //
						.bit(5, SingleRackChannelId.ALARM_FLAG_STATUS_DISCHARGE_OVER_CURRENT) //
						.bit(4, SingleRackChannelId.ALARM_FLAG_STATUS_SYSTEM_LOW_VOLTAGE) //
						.bit(3, SingleRackChannelId.ALARM_FLAG_STATUS_CELL_LOW_VOLTAGE) //
						.bit(2, SingleRackChannelId.ALARM_FLAG_STATUS_CHARGE_OVER_CURRENT) //
						.bit(1, SingleRackChannelId.ALARM_FLAG_STATUS_SYSTEM_OVER_VOLTAGE) //
						.bit(0, SingleRackChannelId.ALARM_FLAG_STATUS_CELL_OVER_VOLTAGE) //
				).build(), //
				modbusComponent.m(new BitsWordElement(0x2008, modbusComponent) //
						.bit(15, SingleRackChannelId.PROTECT_FLAG_STATUS_DISCHARGE_TEMPERATURE_LOW) //
						.bit(14, SingleRackChannelId.PROTECT_FLAG_STATUS_DISCHARGE_TEMPERATURE_HIGH) //
						.bit(13, SingleRackChannelId.PROTECT_FLAG_STATUS_VOLTAGE_DIFFERENCE) //
						.bit(12, SingleRackChannelId.PROTECT_FLAG_STATUS_INSULATION_LOW) //
						.bit(11, SingleRackChannelId.PROTECT_FLAG_STATUS_CELL_VOLTAGE_DIFFERENCE) //
						.bit(10, SingleRackChannelId.PROTECT_FLAG_STATUS_ELECTRODE_TEMPERATURE_HIGH) //
						.bit(9, SingleRackChannelId.PROTECT_FLAG_STATUS_TEMPERATURE_DIFFERENCE) //
						.bit(8, SingleRackChannelId.PROTECT_FLAG_STATUS_SOC_LOW) //
						.bit(7, SingleRackChannelId.PROTECT_FLAG_STATUS_CELL_OVER_TEMPERATURE) //
						.bit(6, SingleRackChannelId.PROTECT_FLAG_STATUS_CELL_LOW_TEMPERATURE) //
						.bit(5, SingleRackChannelId.PROTECT_FLAG_STATUS_DISCHARGE_OVER_CURRENT) //
						.bit(4, SingleRackChannelId.PROTECT_FLAG_STATUS_SYSTEM_LOW_VOLTAGE) //
						.bit(3, SingleRackChannelId.PROTECT_FLAG_STATUS_CELL_LOW_VOLTAGE) //
						.bit(2, SingleRackChannelId.PROTECT_FLAG_STATUS_CHARGE_OVER_CURRENT) //
						.bit(1, SingleRackChannelId.PROTECT_FLAG_STATUS_SYSTEM_OVER_VOLTAGE) //
						.bit(0, SingleRackChannelId.PROTECT_FLAG_STATUS_CELL_OVER_VOLTAGE) //
				).build(), //
				modbusComponent.m(SingleRackChannelId.ALARM_FLAG_REGISTER_1, new UnsignedWordElement(0x2009)), //
				modbusComponent.m(SingleRackChannelId.ALARM_FLAG_REGISTER_2, new UnsignedWordElement(0x200A)), //
				modbusComponent.m(SingleRackChannelId.PROTECT_FLAG_REGISTER_1, new UnsignedWordElement(0x200B)), //
				modbusComponent.m(SingleRackChannelId.PROTECT_FLAG_REGISTER_2, new UnsignedWordElement(0x200C)), //
				modbusComponent.m(SingleRackChannelId.SHORT_CIRCUIT_FUNCTION, new UnsignedWordElement(0x200D)), //
				modbusComponent.m(SingleRackChannelId.TESTING_IO, new UnsignedWordElement(0x200E)), //
				modbusComponent.m(SingleRackChannelId.SOFT_SHUTDOWN, new UnsignedWordElement(0x200F)), //
				modbusComponent.m(SingleRackChannelId.BMS_CONTACTOR_CONTROL, new UnsignedWordElement(0x2010)), //
				modbusComponent.m(SingleRackChannelId.CURRENT_BOX_SELF_CALIBRATION, new UnsignedWordElement(0x2011)), //
				modbusComponent.m(SingleRackChannelId.PCS_ALARM_RESET, new UnsignedWordElement(0x2012)), //
				modbusComponent.m(SingleRackChannelId.INSULATION_SENSOR_FUNCTION, new UnsignedWordElement(0x2013)), //
				modbusComponent.m(SingleRackChannelId.AUTO_SET_SLAVES_ID, new UnsignedWordElement(0x2014)), //
				new DummyRegisterElement(0x2015, 0x2018), //
				modbusComponent.m(SingleRackChannelId.AUTO_SET_SLAVES_TEMPERATURE_ID, new UnsignedWordElement(0x2019)), //
				modbusComponent.m(SingleRackChannelId.TRANSPARENT_MASTER, new UnsignedWordElement(0x201A)), //
				modbusComponent.m(SingleRackChannelId.SET_EMS_ADDRESS, new UnsignedWordElement(0x201B)), //
				modbusComponent.m(SingleRackChannelId.EMS_COMMUNICATION_TIMEOUT, new UnsignedWordElement(0x201C)), //
				modbusComponent.m(SingleRackChannelId.SLEEP, new UnsignedWordElement(0x201D)), //
				modbusComponent.m(SingleRackChannelId.VOLTAGE_LOW_PROTECTION, new UnsignedWordElement(0x201E)) //
		));

		// Voltage ranges
		tasks.add(new FC3ReadRegistersTask(0x2082, Priority.LOW, //
				modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SYSTEM_OVER_VOLTAGE_ALARM,
						new UnsignedWordElement(0x2082), ElementToChannelConverter.SCALE_FACTOR_2), //
				new DummyRegisterElement(0x2083, 0x2087),
				modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SYSTEM_UNDER_VOLTAGE_ALARM,
						new UnsignedWordElement(0x2088), ElementToChannelConverter.SCALE_FACTOR_2) //
		));

		// Summary state
		tasks.add(new FC3ReadRegistersTask(0x2100, Priority.LOW,
				modbusComponent.m(SingleRackChannelId.CLUSTER_1_VOLTAGE, new UnsignedWordElement(0x2100),
						ElementToChannelConverter.SCALE_FACTOR_2), //
				modbusComponent.m(SingleRackChannelId.CLUSTER_1_CURRENT, new UnsignedWordElement(0x2101),
						ElementToChannelConverter.SCALE_FACTOR_2), //
				modbusComponent.m(SingleRackChannelId.CLUSTER_1_CHARGE_INDICATION, new UnsignedWordElement(0x2102)),
				modbusComponent.m(Battery.ChannelId.SOC, new UnsignedWordElement(0x2103)),
				modbusComponent.m(SingleRackChannelId.CLUSTER_1_SOH, new UnsignedWordElement(0x2104)),
				modbusComponent.m(SingleRackChannelId.CLUSTER_1_MAX_CELL_VOLTAGE_ID, new UnsignedWordElement(0x2105)), //
				modbusComponent.m(SingleRackChannelId.CLUSTER_1_MAX_CELL_VOLTAGE, new UnsignedWordElement(0x2106)), //
				modbusComponent.m(SingleRackChannelId.CLUSTER_1_MIN_CELL_VOLTAGE_ID, new UnsignedWordElement(0x2107)), //
				modbusComponent.m(SingleRackChannelId.CLUSTER_1_MIN_CELL_VOLTAGE, new UnsignedWordElement(0x2108)), //
				modbusComponent.m(SingleRackChannelId.CLUSTER_1_MAX_CELL_TEMPERATURE_ID,
						new UnsignedWordElement(0x2109)), //
				modbusComponent.m(SingleRackChannelId.CLUSTER_1_MAX_CELL_TEMPERATURE, new UnsignedWordElement(0x210A)), //
				modbusComponent.m(SingleRackChannelId.CLUSTER_1_MIN_CELL_TEMPERATURE_ID,
						new UnsignedWordElement(0x210B)), //
				modbusComponent.m(SingleRackChannelId.CLUSTER_1_MIN_CELL_TEMPERATURE, new UnsignedWordElement(0x210C)), //
				modbusComponent.m(SingleRackChannelId.MAX_CELL_RESISTANCE_ID, new UnsignedWordElement(0x210D)), //
				modbusComponent.m(SingleRackChannelId.MAX_CELL_RESISTANCE, new UnsignedWordElement(0x210E),
						ElementToChannelConverter.SCALE_FACTOR_1), //
				modbusComponent.m(SingleRackChannelId.MIN_CELL_RESISTANCE_ID, new UnsignedWordElement(0x210F)), //
				modbusComponent.m(SingleRackChannelId.MIN_CELL_RESISTANCE, new UnsignedWordElement(0x2110),
						ElementToChannelConverter.SCALE_FACTOR_1), //
				modbusComponent.m(SingleRackChannelId.POSITIVE_INSULATION, new UnsignedWordElement(0x2111)), //
				modbusComponent.m(SingleRackChannelId.NEGATIVE_INSULATION, new UnsignedWordElement(0x2112)), //
				modbusComponent.m(SingleRackChannelId.MAIN_CONTACTOR_FLAG, new UnsignedWordElement(0x2113)), //
				new DummyRegisterElement(0x2114),
				modbusComponent.m(SingleRackChannelId.ENVIRONMENT_TEMPERATURE, new UnsignedWordElement(0x2115)), //
				modbusComponent.m(SingleRackChannelId.SYSTEM_INSULATION, new UnsignedWordElement(0x2116)), //
				modbusComponent.m(SingleRackChannelId.CELL_VOLTAGE_DIFFERENCE, new UnsignedWordElement(0x2117)), //
				modbusComponent.m(SingleRackChannelId.TOTAL_VOLTAGE_DIFFERENCE, new UnsignedWordElement(0x2118),
						ElementToChannelConverter.SCALE_FACTOR_2), //
				modbusComponent.m(SingleRackChannelId.POWER_TEMPERATURE, new UnsignedWordElement(0x2119)), //
				modbusComponent.m(SingleRackChannelId.POWER_SUPPLY_VOLTAGE, new UnsignedWordElement(0x211A)))); //

		// Critical state
		tasks.add(new FC3ReadRegistersTask(0x2140, Priority.HIGH, //
				modbusComponent.m(new BitsWordElement(0x2140, modbusComponent) //
						.bit(0, SingleRackChannelId.ALARM_LEVEL_2_CELL_VOLTAGE_HIGH) //
						.bit(1, SingleRackChannelId.ALARM_LEVEL_2_TOTAL_VOLTAGE_HIGH) //
						.bit(2, SingleRackChannelId.ALARM_LEVEL_2_CHA_CURRENT_HIGH) //
						.bit(3, SingleRackChannelId.ALARM_LEVEL_2_CELL_VOLTAGE_LOW) //
						.bit(4, SingleRackChannelId.ALARM_LEVEL_2_TOTAL_VOLTAGE_LOW) //
						.bit(5, SingleRackChannelId.ALARM_LEVEL_2_DISCHA_CURRENT_HIGH) //
						.bit(6, SingleRackChannelId.ALARM_LEVEL_2_CELL_CHA_TEMP_HIGH) //
						.bit(7, SingleRackChannelId.ALARM_LEVEL_2_CELL_CHA_TEMP_LOW) //
						.bit(8, SingleRackChannelId.ALARM_LEVEL_2_SOC_LOW) //
						.bit(9, SingleRackChannelId.ALARM_LEVEL_2_TEMPERATURE_DIFFERENCE_HIGH) //
						.bit(10, SingleRackChannelId.ALARM_LEVEL_2_POLES_TEMPERATURE_DIFFERENCE_HIGH) //
						.bit(11, SingleRackChannelId.ALARM_LEVEL_2_CELL_VOLTAGE_DIFFERENCE_HIGH) //
						.bit(12, SingleRackChannelId.ALARM_LEVEL_2_INSULATION_LOW) //
						.bit(13, SingleRackChannelId.ALARM_LEVEL_2_TOTAL_VOLTAGE_DIFFERENCE_HIGH) //
						.bit(14, SingleRackChannelId.ALARM_LEVEL_2_CELL_DISCHA_TEMP_HIGH) //
						.bit(15, SingleRackChannelId.ALARM_LEVEL_2_CELL_DISCHA_TEMP_LOW) //
				).build(), //
				modbusComponent.m(new BitsWordElement(0x2141, modbusComponent) //
						.bit(0, SingleRackChannelId.ALARM_LEVEL_1_CELL_VOLTAGE_HIGH) //
						.bit(1, SingleRackChannelId.ALARM_LEVEL_1_TOTAL_VOLTAGE_HIGH) //
						.bit(2, SingleRackChannelId.ALARM_LEVEL_1_CHA_CURRENT_HIGH) //
						.bit(3, SingleRackChannelId.ALARM_LEVEL_1_CELL_VOLTAGE_LOW) //
						.bit(4, SingleRackChannelId.ALARM_LEVEL_1_TOTAL_VOLTAGE_LOW) //
						.bit(5, SingleRackChannelId.ALARM_LEVEL_1_DISCHA_CURRENT_HIGH) //
						.bit(6, SingleRackChannelId.ALARM_LEVEL_1_CELL_CHA_TEMP_HIGH) //
						.bit(7, SingleRackChannelId.ALARM_LEVEL_1_CELL_CHA_TEMP_LOW) //
						.bit(8, SingleRackChannelId.ALARM_LEVEL_1_SOC_LOW) //
						.bit(9, SingleRackChannelId.ALARM_LEVEL_1_CELL_TEMP_DIFF_HIGH) //
						.bit(10, SingleRackChannelId.ALARM_LEVEL_1_POLE_TEMPERATURE_TOO_HIGH) //
						.bit(11, SingleRackChannelId.ALARM_LEVEL_1_CELL_VOLTAGE_DIFF_HIGH) //
						.bit(12, SingleRackChannelId.ALARM_LEVEL_1_INSULATION_LOW) //
						.bit(13, SingleRackChannelId.ALARM_LEVEL_1_TOTAL_VOLTAGE_DIFF_HIGH) //
						.bit(14, SingleRackChannelId.ALARM_LEVEL_1_CELL_DISCHA_TEMP_HIGH) //
						.bit(15, SingleRackChannelId.ALARM_LEVEL_1_CELL_DISCHA_TEMP_LOW) //
				).build(), //
				modbusComponent.m(SingleRackChannelId.CLUSTER_RUN_STATE, new UnsignedWordElement(0x2142)), //

				modbusComponent.m(SingleRackChannelId.MAXIMUM_CELL_VOLTAGE_NUMBER_WHEN_ALARM,
						new UnsignedWordElement(0x2143)), //
				modbusComponent.m(SingleRackChannelId.MAXIMUM_CELL_VOLTAGE_WHEN_ALARM, new UnsignedWordElement(0x2144)), //
				modbusComponent.m(SingleRackChannelId.MAXIMUM_CELL_VOLTAGE_NUMBER_WHEN_STOPPED,
						new UnsignedWordElement(0x2145)), //
				modbusComponent.m(SingleRackChannelId.MAXIMUM_CELL_VOLTAGE_WHEN_STOPPED,
						new UnsignedWordElement(0x2146)), //
				modbusComponent.m(SingleRackChannelId.MINIMUM_CELL_VOLTAGE_NUMBER_WHEN_ALARM,
						new UnsignedWordElement(0x2147)), //
				modbusComponent.m(SingleRackChannelId.MINIMUM_CELL_VOLTAGE_WHEN_ALARM, new UnsignedWordElement(0x2148)), //
				modbusComponent.m(SingleRackChannelId.MINIMUM_CELL_VOLTAGE_NUMBER_WHEN_STOPPED,
						new UnsignedWordElement(0x2149)), //
				modbusComponent.m(SingleRackChannelId.MINIMUM_CELL_VOLTAGE_WHEN_STOPPED,
						new UnsignedWordElement(0x214A)), //
				modbusComponent.m(SingleRackChannelId.OVER_VOLTAGE_VALUE_WHEN_ALARM, new UnsignedWordElement(0x214B)), //
				modbusComponent.m(SingleRackChannelId.OVER_VOLTAGE_VALUE_WHEN_STOPPED, new UnsignedWordElement(0x214C)), //
				modbusComponent.m(SingleRackChannelId.UNDER_VOLTAGE_VALUE_WHEN_ALARM, new UnsignedWordElement(0x214D)), //
				modbusComponent.m(SingleRackChannelId.UNDER_VOLTAGE_VALUE_WHEN_STOPPED,
						new UnsignedWordElement(0x214E)), //
				modbusComponent.m(SingleRackChannelId.OVER_CHARGE_CURRENT_WHEN_ALARM, new UnsignedWordElement(0x214F)), //
				modbusComponent.m(SingleRackChannelId.OVER_CHARGE_CURRENT_WHEN_STOPPED,
						new UnsignedWordElement(0x2150)), //
				modbusComponent.m(SingleRackChannelId.OVER_DISCHARGE_CURRENT_WHEN_ALARM,
						new UnsignedWordElement(0x2151)), //
				modbusComponent.m(SingleRackChannelId.OVER_DISCHARGE_CURRENT_WHEN_STOPPED,
						new UnsignedWordElement(0x2152)), //
				modbusComponent.m(SingleRackChannelId.NUMBER_OF_TEMPERATURE_WHEN_ALARM,
						new UnsignedWordElement(0x2153)), //
				new DummyRegisterElement(0x2154, 0x215A), //
				modbusComponent.m(SingleRackChannelId.OTHER_ALARM_EQUIPMENT_FAILURE, new UnsignedWordElement(0x215B)), //
				new DummyRegisterElement(0x215C, 0x215F), //
				modbusComponent.m(SingleRackChannelId.SYSTEM_MAX_CHARGE_CURRENT, new UnsignedWordElement(0x2160),
						ElementToChannelConverter.SCALE_FACTOR_2), // TODO Check if correct!
				modbusComponent.m(SingleRackChannelId.SYSTEM_MAX_DISCHARGE_CURRENT, new UnsignedWordElement(0x2161),
						ElementToChannelConverter.SCALE_FACTOR_2) // TODO Check if correct!
		));

		// Cluster info
		tasks.add(new FC3ReadRegistersTask(0x2180, Priority.LOW, //
				modbusComponent.m(SingleRackChannelId.CYCLE_TIME, new UnsignedWordElement(0x2180)), //
				modbusComponent.m(SingleRackChannelId.TOTAL_CAPACITY_HIGH_BITS, new UnsignedWordElement(0x2181)), //
				modbusComponent.m(SingleRackChannelId.TOTAL_CAPACITY_LOW_BITS, new UnsignedWordElement(0x2182)), //
				modbusComponent.m(new BitsWordElement(0x2183, modbusComponent) //
						.bit(3, SingleRackChannelId.SLAVE_20_COMMUNICATION_ERROR)//
						.bit(2, SingleRackChannelId.SLAVE_19_COMMUNICATION_ERROR)//
						.bit(1, SingleRackChannelId.SLAVE_18_COMMUNICATION_ERROR)//
						.bit(0, SingleRackChannelId.SLAVE_17_COMMUNICATION_ERROR)//
				).build(), //
				modbusComponent.m(new BitsWordElement(0x2184, modbusComponent) //
						.bit(15, SingleRackChannelId.SLAVE_16_COMMUNICATION_ERROR)//
						.bit(14, SingleRackChannelId.SLAVE_15_COMMUNICATION_ERROR)//
						.bit(13, SingleRackChannelId.SLAVE_14_COMMUNICATION_ERROR)//
						.bit(12, SingleRackChannelId.SLAVE_13_COMMUNICATION_ERROR)//
						.bit(11, SingleRackChannelId.SLAVE_12_COMMUNICATION_ERROR)//
						.bit(10, SingleRackChannelId.SLAVE_11_COMMUNICATION_ERROR)//
						.bit(9, SingleRackChannelId.SLAVE_10_COMMUNICATION_ERROR)//
						.bit(8, SingleRackChannelId.SLAVE_9_COMMUNICATION_ERROR)//
						.bit(7, SingleRackChannelId.SLAVE_8_COMMUNICATION_ERROR)//
						.bit(6, SingleRackChannelId.SLAVE_7_COMMUNICATION_ERROR)//
						.bit(5, SingleRackChannelId.SLAVE_6_COMMUNICATION_ERROR)//
						.bit(4, SingleRackChannelId.SLAVE_5_COMMUNICATION_ERROR)//
						.bit(3, SingleRackChannelId.SLAVE_4_COMMUNICATION_ERROR)//
						.bit(2, SingleRackChannelId.SLAVE_3_COMMUNICATION_ERROR)//
						.bit(1, SingleRackChannelId.SLAVE_2_COMMUNICATION_ERROR)//
						.bit(0, SingleRackChannelId.SLAVE_1_COMMUNICATION_ERROR)//
				).build(), //
				modbusComponent.m(new BitsWordElement(0x2185, modbusComponent) //
						.bit(0, SingleRackChannelId.FAILURE_SAMPLING_WIRE)//
						.bit(1, SingleRackChannelId.FAILURE_CONNECTOR_WIRE)//
						.bit(2, SingleRackChannelId.FAILURE_LTC6803)//
						.bit(3, SingleRackChannelId.FAILURE_VOLTAGE_SAMPLING)//
						.bit(4, SingleRackChannelId.FAILURE_TEMP_SAMPLING)//
						.bit(5, SingleRackChannelId.FAILURE_TEMP_SENSOR)//
						.bit(6, SingleRackChannelId.FAILURE_GR_T)//
						.bit(7, SingleRackChannelId.FAILURE_PCB)//
						.bit(8, SingleRackChannelId.FAILURE_BALANCING_MODULE)//
						.bit(9, SingleRackChannelId.FAILURE_TEMP_SAMPLING_LINE)//
						.bit(10, SingleRackChannelId.FAILURE_INTRANET_COMMUNICATION)//
						.bit(11, SingleRackChannelId.FAILURE_EEPROM)//
						.bit(12, SingleRackChannelId.FAILURE_INITIALIZATION)//
				).build(), //
				modbusComponent.m(SingleRackChannelId.SYSTEM_TIME_HIGH, new UnsignedWordElement(0x2186)), //
				modbusComponent.m(SingleRackChannelId.SYSTEM_TIME_LOW, new UnsignedWordElement(0x2187)), //
				new DummyRegisterElement(0x2188, 0x218E), //
				modbusComponent.m(SingleRackChannelId.LAST_TIME_CHARGE_CAPACITY_LOW_BITS,
						new UnsignedWordElement(0x218F), ElementToChannelConverter.SCALE_FACTOR_1), //
				modbusComponent.m(SingleRackChannelId.LAST_TIME_CHARGE_END_TIME_HIGH_BITS,
						new UnsignedWordElement(0x2190)), //
				modbusComponent.m(SingleRackChannelId.LAST_TIME_CHARGE_END_TIME_LOW_BITS,
						new UnsignedWordElement(0x2191)), //
				new DummyRegisterElement(0x2192), //
				modbusComponent.m(SingleRackChannelId.LAST_TIME_DISCHARGE_CAPACITY_LOW_BITS,
						new UnsignedWordElement(0x2193), ElementToChannelConverter.SCALE_FACTOR_1), //
				modbusComponent.m(SingleRackChannelId.LAST_TIME_DISCHARGE_END_TIME_HIGH_BITS,
						new UnsignedWordElement(0x2194)), //
				modbusComponent.m(SingleRackChannelId.LAST_TIME_DISCHARGE_END_TIME_LOW_BITS,
						new UnsignedWordElement(0x2195)), //
				modbusComponent.m(SingleRackChannelId.CELL_OVER_VOLTAGE_STOP_TIMES, new UnsignedWordElement(0x2196)), //
				modbusComponent.m(SingleRackChannelId.BATTERY_OVER_VOLTAGE_STOP_TIMES, new UnsignedWordElement(0x2197)), //
				modbusComponent.m(SingleRackChannelId.BATTERY_CHARGE_OVER_CURRENT_STOP_TIMES,
						new UnsignedWordElement(0x2198)), //
				modbusComponent.m(SingleRackChannelId.CELL_VOLTAGE_LOW_STOP_TIMES, new UnsignedWordElement(0x2199)), //
				modbusComponent.m(SingleRackChannelId.BATTERY_VOLTAGE_LOW_STOP_TIMES, new UnsignedWordElement(0x219A)), //
				modbusComponent.m(SingleRackChannelId.BATTERY_DISCHARGE_OVER_CURRENT_STOP_TIMES,
						new UnsignedWordElement(0x219B)), //
				modbusComponent.m(SingleRackChannelId.BATTERY_OVER_TEMPERATURE_STOP_TIMES,
						new UnsignedWordElement(0x219C)), //
				modbusComponent.m(SingleRackChannelId.BATTERY_TEMPERATURE_LOW_STOP_TIMES,
						new UnsignedWordElement(0x219D)), //
				modbusComponent.m(SingleRackChannelId.CELL_OVER_VOLTAGE_ALARM_TIMES, new UnsignedWordElement(0x219E)), //
				modbusComponent.m(SingleRackChannelId.BATTERY_OVER_VOLTAGE_ALARM_TIMES,
						new UnsignedWordElement(0x219F)), //
				modbusComponent.m(SingleRackChannelId.BATTERY_CHARGE_OVER_CURRENT_ALARM_TIMES,
						new UnsignedWordElement(0x21A0)), //
				modbusComponent.m(SingleRackChannelId.CELL_VOLTAGE_LOW_ALARM_TIMES, new UnsignedWordElement(0x21A1)), //
				modbusComponent.m(SingleRackChannelId.BATTERY_VOLTAGE_LOW_ALARM_TIMES, new UnsignedWordElement(0x21A2)), //
				modbusComponent.m(SingleRackChannelId.BATTERY_DISCHARGE_OVER_CURRENT_ALARM_TIMES,
						new UnsignedWordElement(0x21A3)), //
				modbusComponent.m(SingleRackChannelId.BATTERY_OVER_TEMPERATURE_ALARM_TIMES,
						new UnsignedWordElement(0x21A4)), //
				modbusComponent.m(SingleRackChannelId.BATTERY_TEMPERATURE_LOW_ALARM_TIMES,
						new UnsignedWordElement(0x21A5)), //
				modbusComponent.m(SingleRackChannelId.SYSTEM_SHORT_CIRCUIT_PROTECTION_TIMES,
						new UnsignedWordElement(0x21A6)), //
				modbusComponent.m(SingleRackChannelId.SYSTEM_GR_OVER_TEMPERATURE_STOP_TIMES,
						new UnsignedWordElement(0x21A7)), //
				new DummyRegisterElement(0x21A8), //
				modbusComponent.m(SingleRackChannelId.SYSTEM_GR_OVER_TEMPERATURE_ALARM_TIMES,
						new UnsignedWordElement(0x21A9)), //
				new DummyRegisterElement(0x21AA), //
				modbusComponent.m(SingleRackChannelId.BATTERY_VOLTAGE_DIFFERENCE_ALARM_TIMES,
						new UnsignedWordElement(0x21AB)), //
				modbusComponent.m(SingleRackChannelId.BATTERY_VOLTAGE_DIFFERENCE_STOP_TIMES,
						new UnsignedWordElement(0x21AC)), //
				new DummyRegisterElement(0x21AD, 0x21B3), //
				modbusComponent.m(SingleRackChannelId.SLAVE_TEMPERATURE_COMMUNICATION_ERROR_HIGH,
						new UnsignedWordElement(0x21B4)), //
				modbusComponent.m(SingleRackChannelId.SLAVE_TEMPERATURE_COMMUNICATION_ERROR_LOW,
						new UnsignedWordElement(0x21B5)) //
		));

		if (!this.reduceTasks) {

			// Add tasks to read/write work and warn parameters
			// Stop parameter
			Task writeStopParameters = new FC16WriteRegistersTask(0x2040, //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_OVER_VOLTAGE_PROTECTION,
							new UnsignedWordElement(0x2040)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_OVER_VOLTAGE_RECOVER,
							new UnsignedWordElement(0x2041)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SYSTEM_OVER_VOLTAGE_PROTECTION,
							new UnsignedWordElement(0x2042), ElementToChannelConverter.SCALE_FACTOR_2), // TODO
																										// Check if
																										// correct!
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SYSTEM_OVER_VOLTAGE_RECOVER,
							new UnsignedWordElement(0x2043), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SYSTEM_CHARGE_OVER_CURRENT_PROTECTION,
							new UnsignedWordElement(0x2044), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SYSTEM_CHARGE_OVER_CURRENT_RECOVER,
							new UnsignedWordElement(0x2045), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_UNDER_VOLTAGE_PROTECTION,
							new UnsignedWordElement(0x2046)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_UNDER_VOLTAGE_RECOVER,
							new UnsignedWordElement(0x2047)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SYSTEM_UNDER_VOLTAGE_PROTECTION,
							new UnsignedWordElement(0x2048), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SYSTEM_UNDER_VOLTAGE_RECOVER,
							new UnsignedWordElement(0x2049), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SYSTEM_DISCHARGE_OVER_CURRENT_PROTECTION,
							new UnsignedWordElement(0x204A), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SYSTEM_DISCHARGE_OVER_CURRENT_RECOVER,
							new UnsignedWordElement(0x204B), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_OVER_TEMPERATURE_PROTECTION,
							new UnsignedWordElement(0x204C)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_OVER_TEMPERATURE_RECOVER,
							new UnsignedWordElement(0x204D)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_UNDER_TEMPERATURE_PROTECTION,
							new UnsignedWordElement(0x204E)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_UNDER_TEMPERATURE_RECOVER,
							new UnsignedWordElement(0x204F)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SOC_LOW_PROTECTION,
							new UnsignedWordElement(0x2050)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SOC_LOW_PROTECTION_RECOVER,
							new UnsignedWordElement(0x2051)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SOC_HIGH_PROTECTION,
							new UnsignedWordElement(0x2052)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SOC_HIGH_PROTECTION_RECOVER,
							new UnsignedWordElement(0x2053)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CONNECTOR_TEMPERATURE_HIGH_PROTECTION,
							new UnsignedWordElement(0x2054)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CONNECTOR_TEMPERATURE_HIGH_PROTECTION_RECOVER,
							new UnsignedWordElement(0x2055)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_INSULATION_PROTECTION,
							new UnsignedWordElement(0x2056)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_INSULATION_PROTECTION_RECOVER,
							new UnsignedWordElement(0x2057)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_VOLTAGE_DIFFERENCE_PROTECTION,
							new UnsignedWordElement(0x2058)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_VOLTAGE_DIFFERENCE_PROTECTION_RECOVER,
							new UnsignedWordElement(0x2059)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_TOTAL_VOLTAGE_DIFFERENCE_PROTECTION,
							new UnsignedWordElement(0x205A), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_TOTAL_VOLTAGE_DIFFERENCE_PROTECTION_RECOVER,
							new UnsignedWordElement(0x205B), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_DISCHARGE_TEMPERATURE_HIGH_PROTECTION,
							new UnsignedWordElement(0x205C)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_DISCHARGE_TEMPERATURE_HIGH_PROTECTION_RECOVER,
							new UnsignedWordElement(0x205D)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_DISCHARGE_TEMPERATURE_LOW_PROTECTION,
							new UnsignedWordElement(0x205E)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_DISCHARGE_TEMPERATURE_LOW_PROTECTION_RECOVER,
							new UnsignedWordElement(0x205F)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_TEMPERATURE_DIFFERENCE_PROTECTION,
							new UnsignedWordElement(0x2060)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_TEMPERATURE_DIFFERENCE_PROTECTION_RECOVER,
							new UnsignedWordElement(0x2061)) //
			);

			// Warn parameter
			Task writeWarnParameters = new FC16WriteRegistersTask(0x2080, //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_OVER_VOLTAGE_ALARM,
							new UnsignedWordElement(0x2080)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_OVER_VOLTAGE_RECOVER,
							new UnsignedWordElement(0x2081)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SYSTEM_OVER_VOLTAGE_ALARM,
							new UnsignedWordElement(0x2082), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SYSTEM_OVER_VOLTAGE_RECOVER,
							new UnsignedWordElement(0x2083), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SYSTEM_CHARGE_OVER_CURRENT_ALARM,
							new UnsignedWordElement(0x2084), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SYSTEM_CHARGE_OVER_CURRENT_RECOVER,
							new UnsignedWordElement(0x2085), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_UNDER_VOLTAGE_ALARM,
							new UnsignedWordElement(0x2086)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_UNDER_VOLTAGE_RECOVER,
							new UnsignedWordElement(0x2087)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SYSTEM_UNDER_VOLTAGE_ALARM,
							new UnsignedWordElement(0x2088), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SYSTEM_UNDER_VOLTAGE_RECOVER,
							new UnsignedWordElement(0x2089), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SYSTEM_DISCHARGE_OVER_CURRENT_ALARM,
							new UnsignedWordElement(0x208A), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SYSTEM_DISCHARGE_OVER_CURRENT_RECOVER,
							new UnsignedWordElement(0x208B), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_OVER_TEMPERATURE_ALARM,
							new UnsignedWordElement(0x208C)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_OVER_TEMPERATURE_RECOVER,
							new UnsignedWordElement(0x208D)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_UNDER_TEMPERATURE_ALARM,
							new UnsignedWordElement(0x208E)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_UNDER_TEMPERATURE_RECOVER,
							new UnsignedWordElement(0x208F)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SOC_LOW_ALARM,
							new UnsignedWordElement(0x2090)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SOC_LOW_ALARM_RECOVER,
							new UnsignedWordElement(0x2091)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SOC_HIGH_ALARM,
							new UnsignedWordElement(0x2092)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SOC_HIGH_ALARM_RECOVER,
							new UnsignedWordElement(0x2093)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CONNECTOR_TEMPERATURE_HIGH_ALARM,
							new UnsignedWordElement(0x2094)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CONNECTOR_TEMPERATURE_HIGH_ALARM_RECOVER,
							new UnsignedWordElement(0x2095)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_INSULATION_ALARM,
							new UnsignedWordElement(0x2096)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_INSULATION_ALARM_RECOVER,
							new UnsignedWordElement(0x2097)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_VOLTAGE_DIFFERENCE_ALARM,
							new UnsignedWordElement(0x2098)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_VOLTAGE_DIFFERENCE_ALARM_RECOVER,
							new UnsignedWordElement(0x2099)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_TOTAL_VOLTAGE_DIFFERENCE_ALARM,
							new UnsignedWordElement(0x209A), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_TOTAL_VOLTAGE_DIFFERENCE_ALARM_RECOVER,
							new UnsignedWordElement(0x209B), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_DISCHARGE_TEMPERATURE_HIGH_ALARM,
							new UnsignedWordElement(0x209C)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_DISCHARGE_TEMPERATURE_HIGH_ALARM_RECOVER,
							new UnsignedWordElement(0x209D)), //
					new DummyRegisterElement(0x209E),
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_DISCHARGE_TEMPERATURE_LOW_ALARM,
							new UnsignedWordElement(0x209F)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_DISCHARGE_TEMPERATURE_LOW_ALARM_RECOVER,
							new UnsignedWordElement(0x20A0)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_TEMPERATURE_DIFFERENCE_ALARM,
							new UnsignedWordElement(0x20A1)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_TEMPERATURE_DIFFERENCE_ALARM_RECOVER,
							new UnsignedWordElement(0x20A2)) //
			);

			// Stop parameter
			Task readStopParameters = new FC3ReadRegistersTask(0x2040, Priority.LOW, //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_OVER_VOLTAGE_PROTECTION,
							new UnsignedWordElement(0x2040)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_OVER_VOLTAGE_RECOVER,
							new UnsignedWordElement(0x2041)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SYSTEM_OVER_VOLTAGE_PROTECTION,
							new UnsignedWordElement(0x2042), ElementToChannelConverter.SCALE_FACTOR_2), // TODO
																										// Check if
																										// correct!
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SYSTEM_OVER_VOLTAGE_RECOVER,
							new UnsignedWordElement(0x2043), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SYSTEM_CHARGE_OVER_CURRENT_PROTECTION,
							new UnsignedWordElement(0x2044), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SYSTEM_CHARGE_OVER_CURRENT_RECOVER,
							new UnsignedWordElement(0x2045), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_UNDER_VOLTAGE_PROTECTION,
							new UnsignedWordElement(0x2046)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_UNDER_VOLTAGE_RECOVER,
							new UnsignedWordElement(0x2047)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SYSTEM_UNDER_VOLTAGE_PROTECTION,
							new UnsignedWordElement(0x2048), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SYSTEM_UNDER_VOLTAGE_RECOVER,
							new UnsignedWordElement(0x2049), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SYSTEM_DISCHARGE_OVER_CURRENT_PROTECTION,
							new UnsignedWordElement(0x204A), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SYSTEM_DISCHARGE_OVER_CURRENT_RECOVER,
							new UnsignedWordElement(0x204B), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_OVER_TEMPERATURE_PROTECTION,
							new UnsignedWordElement(0x204C)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_OVER_TEMPERATURE_RECOVER,
							new UnsignedWordElement(0x204D)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_UNDER_TEMPERATURE_PROTECTION,
							new UnsignedWordElement(0x204E)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_UNDER_TEMPERATURE_RECOVER,
							new UnsignedWordElement(0x204F)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SOC_LOW_PROTECTION,
							new UnsignedWordElement(0x2050)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SOC_LOW_PROTECTION_RECOVER,
							new UnsignedWordElement(0x2051)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SOC_HIGH_PROTECTION,
							new UnsignedWordElement(0x2052)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_SOC_HIGH_PROTECTION_RECOVER,
							new UnsignedWordElement(0x2053)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CONNECTOR_TEMPERATURE_HIGH_PROTECTION,
							new UnsignedWordElement(0x2054)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CONNECTOR_TEMPERATURE_HIGH_PROTECTION_RECOVER,
							new UnsignedWordElement(0x2055)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_INSULATION_PROTECTION,
							new UnsignedWordElement(0x2056)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_INSULATION_PROTECTION_RECOVER,
							new UnsignedWordElement(0x2057)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_VOLTAGE_DIFFERENCE_PROTECTION,
							new UnsignedWordElement(0x2058)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_CELL_VOLTAGE_DIFFERENCE_PROTECTION_RECOVER,
							new UnsignedWordElement(0x2059)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_TOTAL_VOLTAGE_DIFFERENCE_PROTECTION,
							new UnsignedWordElement(0x205A), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_TOTAL_VOLTAGE_DIFFERENCE_PROTECTION_RECOVER,
							new UnsignedWordElement(0x205B), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_DISCHARGE_TEMPERATURE_HIGH_PROTECTION,
							new UnsignedWordElement(0x205C)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_DISCHARGE_TEMPERATURE_HIGH_PROTECTION_RECOVER,
							new UnsignedWordElement(0x205D)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_DISCHARGE_TEMPERATURE_LOW_PROTECTION,
							new UnsignedWordElement(0x205E)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_DISCHARGE_TEMPERATURE_LOW_PROTECTION_RECOVER,
							new UnsignedWordElement(0x205F)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_TEMPERATURE_DIFFERENCE_PROTECTION,
							new UnsignedWordElement(0x2060)), //
					modbusComponent.m(SingleRackChannelId.STOP_PARAMETER_TEMPERATURE_DIFFERENCE_PROTECTION_RECOVER,
							new UnsignedWordElement(0x2061)) //
			);

//	// Warn parameter
			Task readWarnParameters = new FC3ReadRegistersTask(0x2080, Priority.LOW, //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_OVER_VOLTAGE_ALARM,
							new UnsignedWordElement(0x2080)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_OVER_VOLTAGE_RECOVER,
							new UnsignedWordElement(0x2081)), //
					new DummyRegisterElement(0x2082),
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SYSTEM_OVER_VOLTAGE_RECOVER,
							new UnsignedWordElement(0x2083), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SYSTEM_CHARGE_OVER_CURRENT_ALARM,
							new UnsignedWordElement(0x2084), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SYSTEM_CHARGE_OVER_CURRENT_RECOVER,
							new UnsignedWordElement(0x2085), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_UNDER_VOLTAGE_ALARM,
							new UnsignedWordElement(0x2086)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_UNDER_VOLTAGE_RECOVER,
							new UnsignedWordElement(0x2087)), //
					new DummyRegisterElement(0x2088),
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SYSTEM_UNDER_VOLTAGE_RECOVER,
							new UnsignedWordElement(0x2089), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SYSTEM_DISCHARGE_OVER_CURRENT_ALARM,
							new UnsignedWordElement(0x208A), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SYSTEM_DISCHARGE_OVER_CURRENT_RECOVER,
							new UnsignedWordElement(0x208B), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_OVER_TEMPERATURE_ALARM,
							new UnsignedWordElement(0x208C)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_OVER_TEMPERATURE_RECOVER,
							new UnsignedWordElement(0x208D)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_UNDER_TEMPERATURE_ALARM,
							new UnsignedWordElement(0x208E)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_UNDER_TEMPERATURE_RECOVER,
							new UnsignedWordElement(0x208F)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SOC_LOW_ALARM,
							new UnsignedWordElement(0x2090)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SOC_LOW_ALARM_RECOVER,
							new UnsignedWordElement(0x2091)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SOC_HIGH_ALARM,
							new UnsignedWordElement(0x2092)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_SOC_HIGH_ALARM_RECOVER,
							new UnsignedWordElement(0x2093)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CONNECTOR_TEMPERATURE_HIGH_ALARM,
							new UnsignedWordElement(0x2094)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CONNECTOR_TEMPERATURE_HIGH_ALARM_RECOVER,
							new UnsignedWordElement(0x2095)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_INSULATION_ALARM,
							new UnsignedWordElement(0x2096)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_INSULATION_ALARM_RECOVER,
							new UnsignedWordElement(0x2097)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_VOLTAGE_DIFFERENCE_ALARM,
							new UnsignedWordElement(0x2098)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_CELL_VOLTAGE_DIFFERENCE_ALARM_RECOVER,
							new UnsignedWordElement(0x2099)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_TOTAL_VOLTAGE_DIFFERENCE_ALARM,
							new UnsignedWordElement(0x209A), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_TOTAL_VOLTAGE_DIFFERENCE_ALARM_RECOVER,
							new UnsignedWordElement(0x209B), ElementToChannelConverter.SCALE_FACTOR_2), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_DISCHARGE_TEMPERATURE_HIGH_ALARM,
							new UnsignedWordElement(0x209C)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_DISCHARGE_TEMPERATURE_HIGH_ALARM_RECOVER,
							new UnsignedWordElement(0x209D)), //
					new DummyRegisterElement(0x209E),
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_DISCHARGE_TEMPERATURE_LOW_ALARM,
							new UnsignedWordElement(0x209F)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_DISCHARGE_TEMPERATURE_LOW_ALARM_RECOVER,
							new UnsignedWordElement(0x20A0)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_TEMPERATURE_DIFFERENCE_ALARM,
							new UnsignedWordElement(0x20A1)), //
					modbusComponent.m(SingleRackChannelId.WARN_PARAMETER_TEMPERATURE_DIFFERENCE_ALARM_RECOVER,
							new UnsignedWordElement(0x20A2)) //
			);

			tasks.add(readStopParameters);
			tasks.add(readWarnParameters);
			tasks.add(writeStopParameters);
			tasks.add(writeWarnParameters);

			// Add tasks for cell voltages and temperatures according to the number of
			// slaves, one task per module is created
			// Cell voltages
			int offset = ModuleParameters.ADDRESS_OFFSET.getValue();
			int voltOffset = ModuleParameters.VOLTAGE_ADDRESS_OFFSET.getValue();
			int voltSensors = ModuleParameters.VOLTAGE_SENSORS_PER_MODULE.getValue();
			for (int i = 0; i < this.numberOfSlaves; i++) {
				Collection<AbstractModbusElement<?>> elements = new ArrayList<>();
				for (int j = i * voltSensors; j < (i + 1) * voltSensors; j++) {
					String key = getSingleCellPrefix(j) + KEY_VOLTAGE;
					UnsignedWordElement uwe = new UnsignedWordElement(offset + voltOffset + j);
					AbstractModbusElement<?> ame = modbusComponent.m(channelMap.get(key).channelId(), uwe);
					elements.add(ame);
				}
				tasks.add(new FC3ReadRegistersTask(offset + voltOffset + i * voltSensors, Priority.LOW,
						elements.toArray(new AbstractModbusElement<?>[0])));
			}

			// Cell temperatures
			int tempOffset = ModuleParameters.TEMPERATURE_ADDRESS_OFFSET.getValue();
			int tempSensors = ModuleParameters.TEMPERATURE_SENSORS_PER_MODULE.getValue();
			for (int i = 0; i < this.numberOfSlaves; i++) {
				Collection<AbstractModbusElement<?>> elements = new ArrayList<>();
				for (int j = i * tempSensors; j < (i + 1) * tempSensors; j++) {
					String key = getSingleCellPrefix(j) + KEY_TEMPERATURE;
					SignedWordElement swe = new SignedWordElement(offset + tempOffset + j);
					AbstractModbusElement<?> ame = modbusComponent.m(channelMap.get(key).channelId(), swe);
					elements.add(ame);
				}
				tasks.add(new FC3ReadRegistersTask(offset + tempOffset + i * tempSensors, Priority.LOW,
						elements.toArray(new AbstractModbusElement<?>[0])));
			}
		}
		return tasks;
	}

	private String getSingleCellPrefix(int num) {
		return "CLUSTER_1_BATTERY_" + String.format(NUMBER_FORMAT, num);
	}

	/*
	 * creates a map containing channels for voltage and temperature depending on
	 * the number of modules
	 */
	private Map<String, Channel<?>> createDynamicChannels(AbstractOpenemsModbusComponent modbusComponent) {
		Map<String, Channel<?>> map = new HashMap<>();

		int voltSensors = ModuleParameters.VOLTAGE_SENSORS_PER_MODULE.getValue();
		for (int i = 0; i < this.numberOfSlaves; i++) {
			for (int j = i * voltSensors; j < (i + 1) * voltSensors; j++) {
				String key = getSingleCellPrefix(j) + KEY_VOLTAGE;
				IntegerDoc doc = new IntegerDoc();
				io.openems.edge.common.channel.ChannelId channelId = new ChannelIdImpl(key, doc.unit(Unit.MILLIVOLT));
				IntegerReadChannel integerReadChannel = (IntegerReadChannel) modbusComponent.addChannel(channelId);
				map.put(key, integerReadChannel);
			}
		}

		int tempSensors = ModuleParameters.TEMPERATURE_SENSORS_PER_MODULE.getValue();
		for (int i = 0; i < this.numberOfSlaves; i++) {
			for (int j = i * tempSensors; j < (i + 1) * tempSensors; j++) {
				String key = getSingleCellPrefix(j) + KEY_TEMPERATURE;

				IntegerDoc doc = new IntegerDoc();
				io.openems.edge.common.channel.ChannelId channelId = new ChannelIdImpl(key,
						doc.unit(Unit.DEZIDEGREE_CELSIUS));
				IntegerReadChannel integerReadChannel = (IntegerReadChannel) modbusComponent.addChannel(channelId);
				map.put(key, integerReadChannel);
			}
		}
		return map;
	}

}
