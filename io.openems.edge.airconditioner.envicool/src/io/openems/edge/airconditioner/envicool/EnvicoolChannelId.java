package io.openems.edge.airconditioner.envicool;

import io.openems.edge.common.channel.Doc;
import io.openems.common.channel.AccessMode;
import io.openems.common.channel.Level;
import io.openems.common.channel.Unit;
import io.openems.common.types.OpenemsType;

public enum EnvicoolChannelId implements io.openems.edge.common.channel.ChannelId {

	/*
	 * EnumReadChannels
	 * 
	 * Remark: Running Status (-1:Undefined, 1:Standby, 2:Running, 3:Fault)
	 */
	UNIT_RUNNING_STATUS(Doc.of(WorkState.values())), //
	INTERNAL_FAN_STATUS(Doc.of(WorkState.values())), //
	EXTERNAL_FAN_STATUS(Doc.of(WorkState.values())), //
	COMPRESSOR_STATUS(Doc.of(WorkState.values())), //

	/*
	 * IntegerReadChannels
	 */
	INSIDE_RETURN_TEMP(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS)), //
	PUMP_STATUS(Doc.of(OpenemsType.INTEGER)), //
	OUTSIDE_TEMP(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS)), //
	CONDENSER_TEMP(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS)), //
	EVAPORATOR_TEMP(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS)), //
	INTERNAL_FAN_SPEED(Doc.of(OpenemsType.INTEGER)), //
	EXTERNAL_FAN_SPEED(Doc.of(OpenemsType.INTEGER)), //
	AC_INPUT_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT)), //
	DC_INPUT_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT)), //
	AC_RUNNING_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE)), //
	UNIT_RUNNING_TIME_HIGH(Doc.of(OpenemsType.INTEGER).unit(Unit.HOUR)), //
	UNIT_RUNNING_TIME_LOW(Doc.of(OpenemsType.INTEGER).unit(Unit.HOUR)), //
	COMPRESSOR_RUNNING_TIME_HIGH(Doc.of(OpenemsType.INTEGER).unit(Unit.HOUR)), //
	COMPRESSOR_RUNNING_TIME_LOW(Doc.of(OpenemsType.INTEGER).unit(Unit.HOUR)), //
	INTERNAL_FAN_RUNNING_TIME_HIGH(Doc.of(OpenemsType.INTEGER).unit(Unit.HOUR)), //
	INTERNAL_FAN_RUNNING_TIME_LOW(Doc.of(OpenemsType.INTEGER).unit(Unit.HOUR)), //
	COMPRESSOR_ACTION_TIMES_HIGH(Doc.of(OpenemsType.INTEGER)), //
	COMPRESSOR_ACTION_TIMES_LOW(Doc.of(OpenemsType.INTEGER)), //
	SUPPLY_AIR_TEMPERATURE(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS)), //
	INSIDE_RETURN_HUM(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT)), //

	/*
	 * To be sure that the id and the baud cannot be set via openems they are
	 * read-only.
	 */
	MODBUS_SLAVE_ID(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.READ_ONLY)), //
	BAUD(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.READ_ONLY)), //

	/*
	 * IntegerWriteChannels
	 */

	/**
	 * Cooling set point.
	 * 
	 * <p>
	 * Allowed range: 7 - 50 Default: 35
	 */
	COOLING_SET_POINT(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //

	/**
	 * Cooling hysteresis.
	 * 
	 * <p>
	 * Allowed range: 1 - 15 Default: 3
	 */
	COOLING_HYSTERESIS(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //

	/**
	 * Heating set point.
	 * 
	 * <p>
	 * Allowed range: (-40) - 5 Default: 0
	 */
	HEATING_SET_POINT(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //

	/**
	 * Heating hysteresis.
	 * 
	 * <p>
	 * Allowed range: 1 - 15 Default: 3
	 */
	HEATING_HYSTERESIS(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //

	/**
	 * High temp. alarm point.
	 * 
	 * <p>
	 * Allowed range: 20 - 80 Default: 55
	 */
	HIGH_TEMP_ALARM_POINT(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //

	/**
	 * Low temp. alarm point.
	 * 
	 * <p>
	 * Allowed range: (-40) - 5 Default: -40
	 */
	LOW_TEMP_ALARM_POINT(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //

	/**
	 * Restore factory settings.
	 * 
	 * <p>
	 * Allowed value: 0x0001 for restore
	 */
	RESTORE_FACTORY_SETTINGS(Doc.of(OpenemsType.BOOLEAN).accessMode(AccessMode.WRITE_ONLY)), //

	/**
	 * Remote on and off.
	 * 
	 * <p>
	 * Allowed values: 1:On, 2:Off, Others:Invalid
	 */
	REMOTE_ON_OFF(Doc.of(OnOf.values()).accessMode(AccessMode.WRITE_ONLY)), //

	DC_OVERVOLTAGE_ALARM(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.VOLT)), //
	DC_UNDERVOLTAGE_ALARM(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.VOLT)), //
	DC_OUTAGE_VOLTAGE(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.VOLT)), //
	AC_OVERVOLTAGE_ALARM(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.VOLT)), //
	AC_UNDERVOLTAGE_ALARM(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.VOLT)), //

	/*
	 * Alarms
	 * 
	 * Remark: Normal:0, Alarm:1, Invalid:0xFF
	 */
	STATE_1(Doc.of(Level.WARNING).text("High Temp. alarm")), //
	STATE_2(Doc.of(Level.FAULT).text("Internal fan failure alarm")), //
	STATE_3(Doc.of(Level.FAULT).text("External fan failure")),
	STATE_4(Doc.of(Level.FAULT).text("Compressor failure alarm")), //
	STATE_5(Doc.of(Level.FAULT).text("InsideTemp. sensor failure")), //
	STATE_6(Doc.of(Level.WARNING).text("High pressure")), //
	STATE_7(Doc.of(Level.WARNING).text("Low Temp. alarm")), //
	STATE_8(Doc.of(Level.WARNING).text("DC overvoltage alarm")), //
	STATE_9(Doc.of(Level.WARNING).text("DC undervoltage alarm")), //
	STATE_10(Doc.of(Level.WARNING).text("AC overvoltage alarm")), //
	STATE_11(Doc.of(Level.WARNING).text("AC undervoltage alarm")), //
	STATE_12(Doc.of(Level.FAULT).text("AC power supply failure")), //
	STATE_13(Doc.of(Level.FAULT).text("Evaporator Temp. sensor failure")), //
	STATE_14(Doc.of(Level.FAULT).text("Condenser Temp. sensor failure")), //
	STATE_15(Doc.of(Level.FAULT).text("Outside Temp. sensor failure")), //
	STATE_16(Doc.of(Level.WARNING).text("Evaporator Frost Protection")), //
	STATE_17(Doc.of(Level.WARNING).text("High Pressure Locked")); //

	private final Doc doc;

	private EnvicoolChannelId(Doc doc) {
		this.doc = doc;
	}

	@Override
	public Doc doc() {
		return this.doc;
	}
}
