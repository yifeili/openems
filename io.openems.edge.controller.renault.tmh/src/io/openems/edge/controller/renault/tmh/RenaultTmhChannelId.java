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
	 * Battery Pack Level Points
	 */
	BATTERY_1_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
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
	
	BATTERY_2_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
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
	
	BATTERY_3_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
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
	
	BATTERY_4_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
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
	
	BATTERY_5_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
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
	
	BATTERY_6_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
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
	
	BATTERY_7_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
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
	
	BATTERY_8_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_STATUS_8(Doc.of(BatteryPackStatus.values())), //
	STATE_OF_CHARGE_BATTERY_8(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	DAILY_ENERGY_THROUGHPUT_BATTERY_8(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT_HOURS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_8_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_8_CELL_VOLTAGE_MAXIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_8_CELL_VOLTAGE_MINIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_8_PACK_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_8_PACK_TEMPERATUR(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_8_STATE_OF_HEALTH(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_8_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_8_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_8_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_8_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	BATTERY_9_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_STATUS_9(Doc.of(BatteryPackStatus.values())), //
	STATE_OF_CHARGE_BATTERY_9(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	DAILY_ENERGY_THROUGHPUT_BATTERY_9(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT_HOURS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_9_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_9_CELL_VOLTAGE_MAXIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_9_CELL_VOLTAGE_MINIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_9_PACK_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_9_PACK_TEMPERATUR(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_9_STATE_OF_HEALTH(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_9_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_9_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_9_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_9_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	BATTERY_10_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_STATUS_10(Doc.of(BatteryPackStatus.values())), //
	STATE_OF_CHARGE_BATTERY_10(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	DAILY_ENERGY_THROUGHPUT_BATTERY_10(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT_HOURS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_10_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_10_CELL_VOLTAGE_MAXIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_10_CELL_VOLTAGE_MINIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_10_PACK_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_10_PACK_TEMPERATUR(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_10_STATE_OF_HEALTH(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_10_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_10_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_10_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_10_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	BATTERY_11_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_STATUS_11(Doc.of(BatteryPackStatus.values())), //
	STATE_OF_CHARGE_BATTERY_11(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	DAILY_ENERGY_THROUGHPUT_BATTERY_11(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT_HOURS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_11_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_11_CELL_VOLTAGE_MAXIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_11_CELL_VOLTAGE_MINIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_11_PACK_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_11_PACK_TEMPERATUR(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_11_STATE_OF_HEALTH(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_11_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_11_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_11_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_11_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	BATTERY_12_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_STATUS_12(Doc.of(BatteryPackStatus.values())), //
	STATE_OF_CHARGE_BATTERY_12(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	DAILY_ENERGY_THROUGHPUT_BATTERY_12(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT_HOURS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_12_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_12_CELL_VOLTAGE_MAXIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_PACK_12_CELL_VOLTAGE_MINIMUM(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_12_PACK_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_12_PACK_TEMPERATUR(Doc.of(OpenemsType.INTEGER).unit(Unit.DEGREE_CELSIUS).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_12_STATE_OF_HEALTH(Doc.of(OpenemsType.INTEGER).unit(Unit.PERCENT).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_12_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_12_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_12_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	BATTERY_12_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	/*
	 * Inverter Points (n)
	 */
	INVERTER_1_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_1_STATUS(Doc.of(InverterStatus.values())), //
	INVERTER_1_DC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_1_AC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_1_AC_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_1_ACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_1_REACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_1_APPARENT_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_1_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_1_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_1_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_1_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	INVERTER_2_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_2_STATUS(Doc.of(InverterStatus.values())), //
	INVERTER_2_DC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_2_AC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_2_AC_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_2_ACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_2_REACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_2_APPARENT_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_2_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_2_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_2_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_2_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	INVERTER_3_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_3_STATUS(Doc.of(InverterStatus.values())), //
	INVERTER_3_DC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_3_AC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_3_AC_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_3_ACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_3_REACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_3_APPARENT_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_3_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_3_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_3_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_3_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	INVERTER_4_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_4_STATUS(Doc.of(InverterStatus.values())), //
	INVERTER_4_DC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_4_AC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_4_AC_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_4_ACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_4_REACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_4_APPARENT_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_4_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_4_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_4_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_4_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	INVERTER_5_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_5_STATUS(Doc.of(InverterStatus.values())), //
	INVERTER_5_DC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_5_AC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_5_AC_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_5_ACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_5_REACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_5_APPARENT_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_5_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_5_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_5_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_5_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	INVERTER_6_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_6_STATUS(Doc.of(InverterStatus.values())), //
	INVERTER_6_DC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_6_AC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_6_AC_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_6_ACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_6_REACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_6_APPARENT_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_6_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_6_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_6_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_6_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	INVERTER_7_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_7_STATUS(Doc.of(InverterStatus.values())), //
	INVERTER_7_DC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_7_AC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_7_AC_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_7_ACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_7_REACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_7_APPARENT_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_7_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_7_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_7_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_7_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	INVERTER_8_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_8_STATUS(Doc.of(InverterStatus.values())), //
	INVERTER_8_DC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_8_AC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_8_AC_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_8_ACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_8_REACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_8_APPARENT_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_8_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_8_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_8_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_8_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	INVERTER_9_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_9_STATUS(Doc.of(InverterStatus.values())), //
	INVERTER_9_DC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_9_AC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_9_AC_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_9_ACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_9_REACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_9_APPARENT_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_9_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_9_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_9_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_9_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	INVERTER_10_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_10_STATUS(Doc.of(InverterStatus.values())), //
	INVERTER_10_DC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_10_AC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_10_AC_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_10_ACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_10_REACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_10_APPARENT_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_10_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_10_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_10_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_10_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	INVERTER_11_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_11_STATUS(Doc.of(InverterStatus.values())), //
	INVERTER_11_DC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_11_AC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_11_AC_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_11_ACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_11_REACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_11_APPARENT_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_11_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_11_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_11_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_11_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	
	INVERTER_12_ID(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_12_STATUS(Doc.of(InverterStatus.values())), //
	INVERTER_12_DC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_12_AC_VOLTAGE(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_12_AC_CURRENT(Doc.of(OpenemsType.INTEGER).unit(Unit.AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_12_ACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.WATT).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_12_REACTIVE_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE_REACTIVE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_12_APPARENT_POWER(Doc.of(OpenemsType.INTEGER).unit(Unit.VOLT_AMPERE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_12_SPARE_A(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_12_SPARE_B(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_12_SPARE_C(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)), //
	INVERTER_12_SPARE_D(Doc.of(OpenemsType.INTEGER).unit(Unit.NONE).accessMode(AccessMode.WRITE_ONLY)) //
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
