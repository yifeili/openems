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
	 * Inverter Level Points (n)
	 */
	INVERTER_STATUS_1(Doc.of(InverterStatus.values())), //
	POWER_REQUEST_ACTIVE_POWER_1(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.READ_ONLY)), //
	POWER_REQUEST_REACTIVE_POWER_1(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.READ_ONLY)), //
	
	INVERTER_STATUS_2(Doc.of(InverterStatus.values())), //
	POWER_REQUEST_ACTIVE_POWER_2(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.READ_ONLY)), //
	POWER_REQUEST_REACTIVE_POWER_2(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.READ_ONLY)), //
	
	INVERTER_STATUS_3(Doc.of(InverterStatus.values())), //
	POWER_REQUEST_ACTIVE_POWER_3(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.READ_ONLY)), //
	POWER_REQUEST_REACTIVE_POWER_3(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.READ_ONLY)), //
	
	INVERTER_STATUS_4(Doc.of(InverterStatus.values())), //
	POWER_REQUEST_ACTIVE_POWER_4(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.READ_ONLY)), //
	POWER_REQUEST_REACTIVE_POWER_4(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.READ_ONLY)), //
	
	INVERTER_STATUS_5(Doc.of(InverterStatus.values())), //
	POWER_REQUEST_ACTIVE_POWER_5(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.READ_ONLY)), //
	POWER_REQUEST_REACTIVE_POWER_5(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.READ_ONLY)), //
	
	INVERTER_STATUS_6(Doc.of(InverterStatus.values())), //
	POWER_REQUEST_ACTIVE_POWER_6(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.READ_ONLY)), //
	POWER_REQUEST_REACTIVE_POWER_6(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.READ_ONLY)), //
	
	INVERTER_STATUS_7(Doc.of(InverterStatus.values())), //
	POWER_REQUEST_ACTIVE_POWER_7(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.READ_ONLY)), //
	POWER_REQUEST_REACTIVE_POWER_7(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.READ_ONLY)), //
	
	INVERTER_STATUS_8(Doc.of(InverterStatus.values())), //
	POWER_REQUEST_ACTIVE_POWER_8(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.READ_ONLY)), //
	POWER_REQUEST_REACTIVE_POWER_8(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.READ_ONLY)), //
	
	INVERTER_STATUS_9(Doc.of(InverterStatus.values())), //
	POWER_REQUEST_ACTIVE_POWER_9(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.READ_ONLY)), //
	POWER_REQUEST_REACTIVE_POWER_9(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.READ_ONLY)), //
	
	INVERTER_STATUS_10(Doc.of(InverterStatus.values())), //
	POWER_REQUEST_ACTIVE_POWER_10(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.READ_ONLY)), //
	POWER_REQUEST_REACTIVE_POWER_10(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.READ_ONLY)), //
	
	INVERTER_STATUS_11(Doc.of(InverterStatus.values())), //
	POWER_REQUEST_ACTIVE_POWER_11(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.READ_ONLY)), //
	POWER_REQUEST_REACTIVE_POWER_11(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.READ_ONLY)), //
	
	INVERTER_STATUS_12(Doc.of(InverterStatus.values())), //
	POWER_REQUEST_ACTIVE_POWER_12(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.READ_ONLY)), //
	POWER_REQUEST_REACTIVE_POWER_12(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.READ_ONLY)), //
	
	
	
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
	 * Battery Pack Level Points (m)
	 */
	BATTERY_ID_1(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_STATUS_1(Doc.of(BatteryPackStatus.values())), //
	STATE_OF_CHARGE_BATTERY_1(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	DAILY_ENERGY_THROUGHPUT_BATTERY_1(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT_HOURS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_1_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_1_CELL_VOLTAGE_MAXIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_1_CELL_VOLTAGE_MINIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_1_PACK_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_1_PACK_TEMPERATUR(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_1_STATE_OF_HEALTH(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_1_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_1_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_1_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_1_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	BATTERY_ID_2(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_STATUS_2(Doc.of(BatteryPackStatus.values())), //
	STATE_OF_CHARGE_BATTERY_2(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	DAILY_ENERGY_THROUGHPUT_BATTERY_2(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT_HOURS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_2_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_2_CELL_VOLTAGE_MAXIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_2_CELL_VOLTAGE_MINIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_2_PACK_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_2_PACK_TEMPERATUR(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_2_STATE_OF_HEALTH(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_2_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_2_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_2_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_2_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	BATTERY_ID_3(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_STATUS_3(Doc.of(BatteryPackStatus.values())), //
	STATE_OF_CHARGE_BATTERY_3(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	DAILY_ENERGY_THROUGHPUT_BATTERY_3(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT_HOURS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_3_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_3_CELL_VOLTAGE_MAXIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_3_CELL_VOLTAGE_MINIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_3_PACK_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_3_PACK_TEMPERATUR(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_3_STATE_OF_HEALTH(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_3_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_3_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_3_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_3_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	BATTERY_ID_4(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_STATUS_4(Doc.of(BatteryPackStatus.values())), //
	STATE_OF_CHARGE_BATTERY_4(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	DAILY_ENERGY_THROUGHPUT_BATTERY_4(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT_HOURS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_4_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_4_CELL_VOLTAGE_MAXIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_4_CELL_VOLTAGE_MINIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_4_PACK_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_4_PACK_TEMPERATUR(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_4_STATE_OF_HEALTH(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_4_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_4_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_4_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_4_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	BATTERY_ID_5(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_STATUS_5(Doc.of(BatteryPackStatus.values())), //
	STATE_OF_CHARGE_BATTERY_5(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	DAILY_ENERGY_THROUGHPUT_BATTERY_5(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT_HOURS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_5_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_5_CELL_VOLTAGE_MAXIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_5_CELL_VOLTAGE_MINIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_5_PACK_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_5_PACK_TEMPERATUR(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_5_STATE_OF_HEALTH(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_5_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_5_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_5_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_5_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	BATTERY_ID_6(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_STATUS_6(Doc.of(BatteryPackStatus.values())), //
	STATE_OF_CHARGE_BATTERY_6(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	DAILY_ENERGY_THROUGHPUT_BATTERY_6(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT_HOURS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_6_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_6_CELL_VOLTAGE_MAXIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_6_CELL_VOLTAGE_MINIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_6_PACK_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_6_PACK_TEMPERATUR(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_6_STATE_OF_HEALTH(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_6_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_6_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_6_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_6_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	BATTERY_ID_7(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_STATUS_7(Doc.of(BatteryPackStatus.values())), //
	STATE_OF_CHARGE_BATTERY_7(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	DAILY_ENERGY_THROUGHPUT_BATTERY_7(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT_HOURS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_7_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_7_CELL_VOLTAGE_MAXIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_7_CELL_VOLTAGE_MINIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_7_PACK_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_7_PACK_TEMPERATUR(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_7_STATE_OF_HEALTH(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_7_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_7_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_7_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_7_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	
	
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
