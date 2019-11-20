package io.openems.edge.airconditioner.envicool;

import io.openems.edge.common.channel.Doc;
import io.openems.common.channel.AccessMode;
import io.openems.common.channel.Level;
import io.openems.common.channel.Unit;
import io.openems.common.types.OpenemsType;

public enum EnvicoolChannelId implements io.openems.edge.common.channel.ChannelId {

	// EnumReadChannels
	UNIT_RUNNING_STATUS(Doc.of(WorkState.values())), //	
	INTERNAL_FAN_STATUS(Doc.of(WorkState.values())), //
	EXTERNAL_FAN_STATUS(Doc.of(WorkState.values())), //
	COMPRESSOR_STATUS(Doc.of(WorkState.values())), //

	// IntegerReadChannels Remark: Temp., voltage and current ratio: x10. 
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

	// IntegerWriteChannels Remark: temperature and humidity setting ratio: x10.
	MODBUS_SLAVE_ID(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY)), //
	BAUD(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY)), //

	COOLING_SET_POINT(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //
	COOLING_SENSITIVITY_SET_POINT(
			Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //
	HIGH_TEMP_ALARM_POINT(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //
	LOW_TEMP_ALARM_POINT(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //

	DC_OVERVOLTAGE_ALARM(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.VOLT)), //
	DC_UNDERVOLTAGE_ALARM(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.VOLT)), //
	DC_OUTAGE_VOLTAGE(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.VOLT)), //
	AC_OVERVOLTAGE_ALARM(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.VOLT)), //
	AC_UNDERVOLTAGE_ALARM(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.VOLT)), //

	HEAT_SET_POINT(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //
	HEAT_SENSITIVITY_SET_POINT(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //

	// Control parameter
	RESTORE_FACTORY_SETTINGS(Doc.of(OpenemsType.BOOLEAN).accessMode(AccessMode.WRITE_ONLY)), //
	REMOTE_ON_OFF(Doc.of(OnOf.values()).accessMode(AccessMode.WRITE_ONLY)), //

	// Alarms
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
