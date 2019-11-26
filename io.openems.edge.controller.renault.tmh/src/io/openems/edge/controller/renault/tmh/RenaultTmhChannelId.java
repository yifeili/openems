package io.openems.edge.controller.renault.tmh;

import io.openems.common.channel.AccessMode;
import io.openems.common.channel.Unit;
import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.Doc;


public enum RenaultTmhChannelId implements io.openems.edge.common.channel.ChannelId {

	/**
	 * TMH to ESS
	 */

	/*
	 * Technical Unit Level Points
	 */
	SYSTEM_STATUS_TMH(Doc.of(SystemStatus.values())), //
	POWER_REQUEST_ACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.READ_ONLY)), //
	POWER_REQUEST_REACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.READ_ONLY)), //
	ERROR_RESET(Doc.of(ErrorReset.values())), //
	BATTERY_ERROR_DATA_REQUEST(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.READ_ONLY)), //
	ALIVE_COUNTER_TMH(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.READ_ONLY)), //

	/*
	 * Energy Storage Unit Level Points (i)
	 */
	ESU_STATUS_i_TMH(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.READ_ONLY)), //
	POWER_REQUEST_ACTIVE_POWER_i(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.READ_ONLY)), //
	POWER_REQUEST_REACTIVE_POWER_i(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.READ_ONLY)), //

	/*
	 * Inverter Level Points (n)
	 */
	INVERTER_STATUS_n(Doc.of(InverterStatus.values())), //
	POWER_REQUEST_ACTIVE_POWER_n(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.READ_ONLY)), //
	POWER_REQUEST_REACTIVE_POWER_n(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.READ_ONLY)), //
	
	
	/**
	 * ESS to TMH
	 */
	
	/*
	 * Technical Unit Level Points
	 */
	TECHNICAL_UNIT_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	SYSTEM_STATUS_ESS(Doc.of(SystemStatus.values())), //
	CURRENT_MEASURED_ACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.WRITE_ONLY)), //
	CURRENT_MEASURED_REACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.WRITE_ONLY)), //
	ALIVE_COUNTER_ESS(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	MAXIMUM_AVAILABLE_POWER_DISCHARGE(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.WRITE_ONLY)), //
	MAXIMUM_AVAILABLE_POWER_CHARGE(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.WRITE_ONLY)), //
	AVAILABLE_ENERGY_DISCHARGE(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT_HOURS).accessMode(AccessMode.WRITE_ONLY)), //
	AVAILABLE_ENERGY_CHARGE(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT_HOURS).accessMode(AccessMode.WRITE_ONLY)), //
	STORAGE_SYSTEM_ERROR(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	PAD_REGISTER_0(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	PAD_REGISTER_1(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	TECHNICAL_UNIT_ENERGY_THROUGHPUT_YTD(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT_HOURS).accessMode(AccessMode.WRITE_ONLY)), //
	INDOOR_AMBIENT_TEMPERATURE(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS).accessMode(AccessMode.WRITE_ONLY)), //

	/*
	 * Energy Storage Unit Level Points (i)
	 */
	ESU_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	ESU_STATUS_i_ESS(Doc.of(EsuStatus.values())), //

	/*
	 * Battery Pack Level Points (m)
	 */
	BATTERY_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_STATUS_m(Doc.of(BatteryPackStatus.values())), //
	STATE_OF_CHARGE_BATTERY_m(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	DAILY_ENERGY_THROUGHPUT_BATTERY_m(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT_HOURS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_m_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_m_CELL_VOLTAGE_MAXIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_m_CELL_VOLTAGE_MINIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_m_PACK_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_m_PACK_TEMPERATUR(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_m_STATE_OF_HEALTH(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_m_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_m_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_m_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_m_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	/*
	 * Inverter Points (n)
	 */
	INVERTER_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_n_STATUS(Doc.of(InverterStatus.values())), //
	INVERTER_n_DC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_n_AC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_n_AC_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_n_ACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_n_REACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_n_APPARENT_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_n_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_n_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_n_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_n_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	;
	private final Doc doc;

	private RenaultTmhChannelId(Doc doc) {
		this.doc = doc;
	}
	
	@Override
	public Doc doc() {
		return this.doc;	
		
	}
}
