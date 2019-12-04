package io.openems.edge.airconditioner.envicool;

import io.openems.common.channel.AccessMode;
import io.openems.common.channel.Level;
import io.openems.common.channel.Unit;
import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.Doc;

public enum EnvicoolChannelId implements io.openems.edge.common.channel.ChannelId {
	
	/*
	 *  EnumReadChannels
	 *  
	 *  Remark: Running Status (0:Stop,1:Running, 2:Invalid)
	 */
	UNIT_RUNNING_STATUS(Doc.of(WorkState.values())), //
	INTERNAL_FAN_STATUS(Doc.of(WorkState.values())), //
	EXTERNAL_FAN_STATUS(Doc.of(WorkState.values())), //
	COMPRESSOR_STATUS(Doc.of(WorkState.values())), //
	HEATER_STATUS(Doc.of(WorkState.values())), //
	EMERGENCY_FAN_STATUS(Doc.of(WorkState.values())), //

	/*
	 * IntegerReadChannels 
	 * 
	 * Remark:  The invalid value of Temp. is 2000. 
	 * 			The invalid value of humidity is 120. 
	 * 			The invalid value of humidity is 32767
	 */
	SOFTWARE_VERSION(Doc.of(OpenemsType.INTEGER)), //
	EVAPORATOR_TEMP(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS)), //
	OUTDOOR_TEMP(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS)), //
	CONDENSER_TEMP(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS)), //
	INDOOR_TEMP(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS)), //
	HUMIDITY(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT)), //
	DISCHARGE_TEMP(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS)), //
	AC_RUNNING_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.MILLIAMPERE)), //
	AC_INPUT_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT)), //
	DC_INPUT_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT)), //

	/* Alarms
	 * 
	 * Remark: Normal:0, Fault:1
	 */
	STATE_1(Doc.of(Level.WARNING).text("High Temp. alarm")), //
	STATE_2(Doc.of(Level.WARNING).text("Low Temp. alarm")), //
	STATE_3(Doc.of(Level.WARNING).text("High humidity alarm")), //
	STATE_4(Doc.of(Level.WARNING).text("Low humidity alarm")), //
	STATE_5(Doc.of(Level.WARNING).text("Coil freeze protection")), //
	STATE_6(Doc.of(Level.WARNING).text("High exhaust Temp. alarm")), //
	STATE_7(Doc.of(Level.FAULT).text("Evaporator Temp. sensor failure")), //
	STATE_8(Doc.of(Level.FAULT).text("Condenser Temp. sensor failure")), //
	STATE_9(Doc.of(Level.FAULT).text("Condenser Temp. sensor failure")), //
	STATE_10(Doc.of(Level.FAULT).text("Indoor Temp. sensor failure")), //
	STATE_11(Doc.of(Level.FAULT).text("Exhaust Temp. sensor failure")), //
	STATE_12(Doc.of(Level.FAULT).text("Humidity sensor failure")), //
	STATE_13(Doc.of(Level.FAULT).text("Internal fan failure alarm")), //
	STATE_14(Doc.of(Level.FAULT).text("External fan failure")),
	STATE_15(Doc.of(Level.FAULT).text("Compressor failure alarm")), //
	STATE_16(Doc.of(Level.FAULT).text("Heater failure alarm")), //
	STATE_17(Doc.of(Level.FAULT).text("Emergency fan failure alarm")), //
	STATE_18(Doc.of(Level.WARNING).text("HP. alarm")), //
	STATE_19(Doc.of(Level.WARNING).text("LP. alarm")), //
	STATE_20(Doc.of(Level.WARNING).text("Water alarm")), //
	STATE_21(Doc.of(Level.WARNING).text("Fire alarm")), //
	STATE_22(Doc.of(Level.WARNING).text("Gating alarm")), //
	STATE_23(Doc.of(Level.WARNING).text("HP. lock")), //
	STATE_24(Doc.of(Level.WARNING).text("LP. lock")), //
	STATE_25(Doc.of(Level.WARNING).text("High exhaust Temp. lock")), //
	STATE_26(Doc.of(Level.WARNING).text("AC over voltage alarm")), //
	STATE_27(Doc.of(Level.WARNING).text("AC under voltage alarm")), //
	STATE_28(Doc.of(Level.FAULT).text("AC power supply failure")), //
	STATE_29(Doc.of(Level.FAULT).text("Lose phase alarm")), //
	STATE_30(Doc.of(Level.WARNING).text("Freq.fault")), //
	STATE_31(Doc.of(Level.FAULT).text("Anti phase alarm")), //
	STATE_32(Doc.of(Level.WARNING).text("DC over voltage alarm")), //
	STATE_33(Doc.of(Level.WARNING).text("DC under voltage alarm")), //


	/*
	 * IntegerWriteChannels 
	 */
	
	/**
	 * Refrigeration stop point.
	 * 
	 * <p>
	 * Allowed range: 15 - 50
	 */
	REFRIGERATION_STOP_POINT(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //
	
	/**
	 * Refrigeration band.
	 * 
	 * <p>
	 * Allowed range: 1 - 10
	 */
	REFRIGERATION_BAND(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //
	
	/**
	 * Heating stop point.
	 * 
	 * <p>
	 * Allowed range: (-15) - (15)
	 */
	HEATING_STOP_POINT(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //
	
	/**
	 * Heating band.
	 * 
	 * <p>
	 * Allowed range: 1 - 10
	 */
	HEATING_BAND(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //
	
	/**
	 * Dehumidification stop point.
	 * 
	 * <p>
	 * Allowed range: 40 - 90 %
	 */
	DEHUMIDIFICATION_STOP_POINT(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.PERCENT)), //
	
	/**
	 * Dehumidification band.
	 * 
	 * <p>
	 * Allowed range: 1 - 30 %
	 */
	DEHUMIDIFICATION_BAND(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.PERCENT)), //
	
	/**
	 * High temp. point.
	 * 
	 * <p>
	 * Allowed range: 25 - 80
	 */
	HIGH_TEMP_POINT(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //
	
	/**
	 * Low temp. point.
	 * 
	 * <p>
	 * Allowed range: (-20) - (15)
	 */
	LOW_TEMP_POINT(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //
	
	/**
	 * High humidity point
	 * 
	 * <p>
	 * Allowed range: 0 - 100 %
	 */
	HIGH_HUMIDITY_POINT(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.PERCENT)), //
	
	/**
	 * Internal fan stop point
	 * 
	 * <p>
	 * Allowed range: (-20) - (50)
	 */
	INTERNAL_FAN_STOP_POINT(Doc.of(OpenemsType.INTEGER).accessMode(AccessMode.WRITE_ONLY).unit(Unit.DEGREE_CELSIUS)), //
	
	/*
	 *  Control parameter
	 */
	REMOTE_ON_OFF(Doc.of(OnOf.values()).accessMode(AccessMode.WRITE_ONLY)); //

	private final Doc doc;

	private EnvicoolChannelId(Doc doc) {
		this.doc = doc;
	}

	@Override
	public Doc doc() {
		return this.doc;
	}
}
