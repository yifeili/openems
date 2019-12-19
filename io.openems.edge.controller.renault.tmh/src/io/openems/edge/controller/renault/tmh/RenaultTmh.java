package io.openems.edge.controller.renault.tmh;

import java.util.Optional;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import io.openems.edge.bridge.modbus.api.AbstractOpenemsModbusComponent;
import io.openems.edge.bridge.modbus.api.BridgeModbus;
import io.openems.edge.bridge.modbus.api.ElementToChannelConverter;
import io.openems.edge.bridge.modbus.api.ModbusProtocol;
import io.openems.edge.bridge.modbus.api.element.SignedDoublewordElement;
import io.openems.edge.bridge.modbus.api.element.SignedWordElement;
import io.openems.edge.bridge.modbus.api.element.UnsignedDoublewordElement;
import io.openems.edge.bridge.modbus.api.element.UnsignedWordElement;
import io.openems.edge.bridge.modbus.api.task.FC16WriteRegistersTask;
import io.openems.edge.bridge.modbus.api.task.FC3ReadRegistersTask;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.common.modbusslave.ModbusSlave;
import io.openems.edge.common.modbusslave.ModbusSlaveTable;
import io.openems.edge.common.taskmanager.Priority;
import io.openems.edge.controller.api.Controller;

import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.channel.AccessMode;
import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;




@Designate(ocd = Config.class, factory = true)
@Component(//
		name = "Controller.Renault.TMH", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE, //
		property = { EventConstants.EVENT_TOPIC + "=" + EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE,//
		}
)


public class RenaultTmh extends AbstractOpenemsModbusComponent
		implements OpenemsComponent, EventHandler, ModbusSlave, Controller {

	private final Logger log = LoggerFactory.getLogger(RenaultTmh.class);
	private Config config;
	
	
	public static final int DEFAULT_UNIT_ID = 1;
	
	@Reference
	protected ConfigurationAdmin cm;
	
	
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
	
	public RenaultTmh() {
		super(//
				OpenemsComponent.ChannelId.values(),//
				Controller.ChannelId.values(), //
				ChannelId.values() //
		);
	}
	
	
	
	@Reference(policy = ReferencePolicy.STATIC, policyOption = ReferencePolicyOption.GREEDY, cardinality = ReferenceCardinality.MANDATORY)
	protected void setModbus(BridgeModbus modbus) {
		super.setModbus(modbus);
	}
	
	
	
	@Activate
	void activate(ComponentContext context, Config config) {
		super.activate(context, config.id(), config.alias(), config.enabled(), DEFAULT_UNIT_ID, this.cm, "Modbus",
				config.modbus_id()); //
	}
	
	
	
	@Deactivate
	protected void deactivate() {
		super.deactivate();
	}
	
	
	@Override
	public void handleEvent(Event event) {
		if (!this.isEnabled()) {
			return;
		}
		switch (event.getTopic()) {
		case EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE:
			break;
		}
	}

	
	@Override
	public void run() throws OpenemsNamedException {
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
	private void setAliveCounter() {
		// TODO Auto-generated method stub
		
	}
	
	private SystemStatus determineSystemStatus() {
		// Read State from Inverter and Battery
		SystemStatus state = SystemStatus.UNDEFINED;
		
		
		
		
		return state;
	}



	@Override
	public ModbusSlaveTable getModbusSlaveTable(AccessMode accessMode) {
		return new ModbusSlaveTable( //
				OpenemsComponent.getModbusSlaveNatureTable(accessMode) //
		);
	}

	
	private final static int ESS_MODBUS_ADDRESS_OFFSET_TU_LEVEL_POINT = 0;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_1_LEVEL_POINT = 26;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_2_LEVEL_POINT = 41;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_3_LEVEL_POINT = 56;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_4_LEVEL_POINT = 71;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_5_LEVEL_POINT = 86;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_6_LEVEL_POINT = 101;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_7_LEVEL_POINT = 116;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_8_LEVEL_POINT = 131;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_9_LEVEL_POINT = 146;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_10_LEVEL_POINT = 161;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_11_LEVEL_POINT = 176;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_12_LEVEL_POINT = 191;
	
	
	private final static int ESS_MODBUS_ADDRESS_OFFSET_Inverter_1_LEVEL_POINT = 206;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_Inverter_2_LEVEL_POINT = 219;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_Inverter_3_LEVEL_POINT = 232;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_Inverter_4_LEVEL_POINT = 245;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_Inverter_5_LEVEL_POINT = 258;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_Inverter_6_LEVEL_POINT = 271;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_Inverter_7_LEVEL_POINT = 284;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_Inverter_8_LEVEL_POINT = 297;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_Inverter_9_LEVEL_POINT = 310;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_Inverter_10_LEVEL_POINT = 323;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_Inverter_11_LEVEL_POINT = 336;
	private final static int ESS_MODBUS_ADDRESS_OFFSET_Inverter_12_LEVEL_POINT = 349;
	
	
	@Override
	protected ModbusProtocol defineModbusProtocol() {
		// TODO Auto-generated method stub
		return new ModbusProtocol(this, //
				new FC3ReadRegistersTask(0, Priority.HIGH, //
						
						/**
						 * TMH to ESS
						 */
						
						/*
						 * Technical Unit Level Points
						 */
						m(RenaultTmhChannelId.SYSTEM_STATUS_TMH, new UnsignedWordElement(0)),
						m(RenaultTmhChannelId.POWER_REQUEST_ACTIVE_POWER, new SignedDoublewordElement(1)),
						m(RenaultTmhChannelId.POWER_REQUEST_REACTIVE_POWER, new SignedDoublewordElement(3)),
						m(RenaultTmhChannelId.ERROR_RESET, new UnsignedWordElement(5)),
						m(RenaultTmhChannelId.BATTERY_ERROR_DATA_REQUEST, new UnsignedWordElement(6)),
						m(RenaultTmhChannelId.ALIVE_COUNTER_TMH, new UnsignedWordElement(7)),
						
						/*
						 * Inverter Level Points (n)
						 */
						m(RenaultTmhChannelId.INVERTER_STATUS_1, new UnsignedWordElement(8)),
						m(RenaultTmhChannelId.POWER_REQUEST_ACTIVE_POWER_1, new SignedDoublewordElement(9)),
						m(RenaultTmhChannelId.POWER_REQUEST_REACTIVE_POWER_1, new SignedDoublewordElement(11)),
						
						m(RenaultTmhChannelId.INVERTER_STATUS_2, new UnsignedWordElement(13)),
						m(RenaultTmhChannelId.POWER_REQUEST_ACTIVE_POWER_2, new SignedDoublewordElement(14)),
						m(RenaultTmhChannelId.POWER_REQUEST_REACTIVE_POWER_2, new SignedDoublewordElement(16)),
						
						m(RenaultTmhChannelId.INVERTER_STATUS_3, new UnsignedWordElement(18)),
						m(RenaultTmhChannelId.POWER_REQUEST_ACTIVE_POWER_3, new SignedDoublewordElement(19)),
						m(RenaultTmhChannelId.POWER_REQUEST_REACTIVE_POWER_3, new SignedDoublewordElement(21)),
						
						m(RenaultTmhChannelId.INVERTER_STATUS_4, new UnsignedWordElement(23)),
						m(RenaultTmhChannelId.POWER_REQUEST_ACTIVE_POWER_4, new SignedDoublewordElement(24)),
						m(RenaultTmhChannelId.POWER_REQUEST_REACTIVE_POWER_4, new SignedDoublewordElement(26)),
						
						m(RenaultTmhChannelId.INVERTER_STATUS_5, new UnsignedWordElement(28)),
						m(RenaultTmhChannelId.POWER_REQUEST_ACTIVE_POWER_5, new SignedDoublewordElement(29)),
						m(RenaultTmhChannelId.POWER_REQUEST_REACTIVE_POWER_5, new SignedDoublewordElement(31)),
						
						m(RenaultTmhChannelId.INVERTER_STATUS_6, new UnsignedWordElement(33)),
						m(RenaultTmhChannelId.POWER_REQUEST_ACTIVE_POWER_6, new SignedDoublewordElement(34)),
						m(RenaultTmhChannelId.POWER_REQUEST_REACTIVE_POWER_6, new SignedDoublewordElement(36)),
						
						m(RenaultTmhChannelId.INVERTER_STATUS_7, new UnsignedWordElement(38)),
						m(RenaultTmhChannelId.POWER_REQUEST_ACTIVE_POWER_7, new SignedDoublewordElement(39)),
						m(RenaultTmhChannelId.POWER_REQUEST_REACTIVE_POWER_7, new SignedDoublewordElement(41)),
						
						m(RenaultTmhChannelId.INVERTER_STATUS_8, new UnsignedWordElement(43)),
						m(RenaultTmhChannelId.POWER_REQUEST_ACTIVE_POWER_8, new SignedDoublewordElement(44)),
						m(RenaultTmhChannelId.POWER_REQUEST_REACTIVE_POWER_8, new SignedDoublewordElement(46)),
						
						m(RenaultTmhChannelId.INVERTER_STATUS_9, new UnsignedWordElement(48)),
						m(RenaultTmhChannelId.POWER_REQUEST_ACTIVE_POWER_9, new SignedDoublewordElement(49)),
						m(RenaultTmhChannelId.POWER_REQUEST_REACTIVE_POWER_9, new SignedDoublewordElement(51)),
						
						m(RenaultTmhChannelId.INVERTER_STATUS_10, new UnsignedWordElement(53)),
						m(RenaultTmhChannelId.POWER_REQUEST_ACTIVE_POWER_10, new SignedDoublewordElement(54)),
						m(RenaultTmhChannelId.POWER_REQUEST_REACTIVE_POWER_10, new SignedDoublewordElement(56)),
						
						m(RenaultTmhChannelId.INVERTER_STATUS_11, new UnsignedWordElement(58)),
						m(RenaultTmhChannelId.POWER_REQUEST_ACTIVE_POWER_11, new SignedDoublewordElement(59)),
						m(RenaultTmhChannelId.POWER_REQUEST_REACTIVE_POWER_11, new SignedDoublewordElement(61)),
						
						m(RenaultTmhChannelId.INVERTER_STATUS_12, new UnsignedWordElement(63)),
						m(RenaultTmhChannelId.POWER_REQUEST_ACTIVE_POWER_12, new SignedDoublewordElement(64)),
						m(RenaultTmhChannelId.POWER_REQUEST_REACTIVE_POWER_12, new SignedDoublewordElement(66))),
				
				
				new FC16WriteRegistersTask(0, //
						
						/**
						 * ESS to TMH
						 */
						
						/*
						 * Technical Unit Level Points
						 */						
						m(RenaultTmhChannelId.TECHNICAL_UNIT_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_TU_LEVEL_POINT)),
						m(RenaultTmhChannelId.SYSTEM_STATUS_ESS, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_TU_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.CURRENT_MEASURED_ACTIVE_POWER, new SignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_TU_LEVEL_POINT + 3)),
						m(RenaultTmhChannelId.CURRENT_MEASURED_REACTIVE_POWER, new SignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_TU_LEVEL_POINT + 5)),
						m(RenaultTmhChannelId.ALIVE_COUNTER_ESS, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_TU_LEVEL_POINT + 7)),
						m(RenaultTmhChannelId.MAXIMUM_AVAILABLE_POWER_DISCHARGE, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_TU_LEVEL_POINT + 8)),
						m(RenaultTmhChannelId.MAXIMUM_AVAILABLE_POWER_CHARGE, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_TU_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.AVAILABLE_ENERGY_DISCHARGE, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_TU_LEVEL_POINT + 12)),
						m(RenaultTmhChannelId.AVAILABLE_ENERGY_CHARGE, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_TU_LEVEL_POINT + 14)),
						m(RenaultTmhChannelId.STORAGE_SYSTEM_ERROR, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_TU_LEVEL_POINT + 16)),
						m(RenaultTmhChannelId.PAD_REGISTER_0, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_TU_LEVEL_POINT + 18)),
						m(RenaultTmhChannelId.PAD_REGISTER_1, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_TU_LEVEL_POINT + 20)),
						m(RenaultTmhChannelId.TECHNICAL_UNIT_ENERGY_THROUGHPUT_YTD, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_TU_LEVEL_POINT + 22)),
						m(RenaultTmhChannelId.INDOOR_AMBIENT_TEMPERATURE, new SignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_TU_LEVEL_POINT + 24)),

						/*
						 * Battery Pack Level Points (m)
						 */
						m(RenaultTmhChannelId.BATTERY_1_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_1_LEVEL_POINT)),
						m(RenaultTmhChannelId.BATTERY_PACK_STATUS_1, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_1_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.STATE_OF_CHARGE_BATTERY_1, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_1_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_1),						
						m(RenaultTmhChannelId.DAILY_ENERGY_THROUGHPUT_BATTERY_1, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_1_LEVEL_POINT + 4)),
						m(RenaultTmhChannelId.BATTERY_PACK_1_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_1_LEVEL_POINT + 5)),
						m(RenaultTmhChannelId.BATTERY_PACK_1_CELL_VOLTAGE_MAXIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_1_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_PACK_1_CELL_VOLTAGE_MINIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_1_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_1_PACK_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_1_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.BATTERY_1_PACK_TEMPERATUR, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_1_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.BATTERY_1_STATE_OF_HEALTH, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_1_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.BATTERY_1_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_1_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.BATTERY_1_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_1_LEVEL_POINT + 12)),
						m(RenaultTmhChannelId.BATTERY_1_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_1_LEVEL_POINT + 13)),
						m(RenaultTmhChannelId.BATTERY_1_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_1_LEVEL_POINT + 14)),
						
						m(RenaultTmhChannelId.BATTERY_2_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_2_LEVEL_POINT)),
						m(RenaultTmhChannelId.BATTERY_PACK_STATUS_2, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_2_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.STATE_OF_CHARGE_BATTERY_2, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_2_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_1),						
						m(RenaultTmhChannelId.DAILY_ENERGY_THROUGHPUT_BATTERY_2, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_2_LEVEL_POINT + 4)),
						m(RenaultTmhChannelId.BATTERY_PACK_2_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_2_LEVEL_POINT + 5)),
						m(RenaultTmhChannelId.BATTERY_PACK_2_CELL_VOLTAGE_MAXIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_2_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_PACK_2_CELL_VOLTAGE_MINIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_2_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_2_PACK_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_2_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.BATTERY_2_PACK_TEMPERATUR, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_2_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.BATTERY_2_STATE_OF_HEALTH, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_2_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.BATTERY_2_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_2_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.BATTERY_2_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_2_LEVEL_POINT + 12)),
						m(RenaultTmhChannelId.BATTERY_2_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_2_LEVEL_POINT + 13)),
						m(RenaultTmhChannelId.BATTERY_2_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_2_LEVEL_POINT + 14)),
						
						m(RenaultTmhChannelId.BATTERY_3_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_3_LEVEL_POINT)),
						m(RenaultTmhChannelId.BATTERY_PACK_STATUS_3, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_3_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.STATE_OF_CHARGE_BATTERY_3, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_3_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_1),						
						m(RenaultTmhChannelId.DAILY_ENERGY_THROUGHPUT_BATTERY_3, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_3_LEVEL_POINT + 4)),
						m(RenaultTmhChannelId.BATTERY_PACK_3_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_3_LEVEL_POINT + 5)),
						m(RenaultTmhChannelId.BATTERY_PACK_3_CELL_VOLTAGE_MAXIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_3_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_PACK_3_CELL_VOLTAGE_MINIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_3_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_3_PACK_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_3_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.BATTERY_3_PACK_TEMPERATUR, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_3_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.BATTERY_3_STATE_OF_HEALTH, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_3_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.BATTERY_3_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_3_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.BATTERY_3_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_3_LEVEL_POINT + 12)),
						m(RenaultTmhChannelId.BATTERY_3_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_3_LEVEL_POINT + 13)),
						m(RenaultTmhChannelId.BATTERY_3_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_3_LEVEL_POINT + 14)),
						
						m(RenaultTmhChannelId.BATTERY_4_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_4_LEVEL_POINT)),
						m(RenaultTmhChannelId.BATTERY_PACK_STATUS_4, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_4_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.STATE_OF_CHARGE_BATTERY_4, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_4_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_1),						
						m(RenaultTmhChannelId.DAILY_ENERGY_THROUGHPUT_BATTERY_4, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_4_LEVEL_POINT + 4)),
						m(RenaultTmhChannelId.BATTERY_PACK_4_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_4_LEVEL_POINT + 5)),
						m(RenaultTmhChannelId.BATTERY_PACK_4_CELL_VOLTAGE_MAXIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_4_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_PACK_4_CELL_VOLTAGE_MINIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_4_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_4_PACK_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_4_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.BATTERY_4_PACK_TEMPERATUR, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_4_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.BATTERY_4_STATE_OF_HEALTH, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_4_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.BATTERY_4_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_4_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.BATTERY_4_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_4_LEVEL_POINT + 12)),
						m(RenaultTmhChannelId.BATTERY_4_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_4_LEVEL_POINT + 13)),
						m(RenaultTmhChannelId.BATTERY_4_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_4_LEVEL_POINT + 14)),
						
						m(RenaultTmhChannelId.BATTERY_5_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_5_LEVEL_POINT)),
						m(RenaultTmhChannelId.BATTERY_PACK_STATUS_5, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_5_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.STATE_OF_CHARGE_BATTERY_5, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_5_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_1),						
						m(RenaultTmhChannelId.DAILY_ENERGY_THROUGHPUT_BATTERY_5, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_5_LEVEL_POINT + 4)),
						m(RenaultTmhChannelId.BATTERY_PACK_5_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_5_LEVEL_POINT + 5)),
						m(RenaultTmhChannelId.BATTERY_PACK_5_CELL_VOLTAGE_MAXIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_5_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_PACK_5_CELL_VOLTAGE_MINIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_5_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_5_PACK_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_5_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.BATTERY_5_PACK_TEMPERATUR, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_5_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.BATTERY_5_STATE_OF_HEALTH, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_5_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.BATTERY_5_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_5_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.BATTERY_5_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_5_LEVEL_POINT + 12)),
						m(RenaultTmhChannelId.BATTERY_5_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_5_LEVEL_POINT + 13)),
						m(RenaultTmhChannelId.BATTERY_5_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_5_LEVEL_POINT + 14)),
						
						m(RenaultTmhChannelId.BATTERY_6_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_6_LEVEL_POINT)),
						m(RenaultTmhChannelId.BATTERY_PACK_STATUS_6, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_6_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.STATE_OF_CHARGE_BATTERY_6, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_6_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_1),						
						m(RenaultTmhChannelId.DAILY_ENERGY_THROUGHPUT_BATTERY_5, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_6_LEVEL_POINT + 4)),
						m(RenaultTmhChannelId.BATTERY_PACK_6_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_6_LEVEL_POINT + 5)),
						m(RenaultTmhChannelId.BATTERY_PACK_6_CELL_VOLTAGE_MAXIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_6_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_PACK_6_CELL_VOLTAGE_MINIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_6_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_6_PACK_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_6_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.BATTERY_6_PACK_TEMPERATUR, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_6_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.BATTERY_6_STATE_OF_HEALTH, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_6_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.BATTERY_6_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_6_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.BATTERY_6_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_6_LEVEL_POINT + 12)),
						m(RenaultTmhChannelId.BATTERY_6_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_6_LEVEL_POINT + 13)),
						m(RenaultTmhChannelId.BATTERY_6_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_6_LEVEL_POINT + 14)),
						
						m(RenaultTmhChannelId.BATTERY_7_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_7_LEVEL_POINT)),
						m(RenaultTmhChannelId.BATTERY_PACK_STATUS_7, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_7_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.STATE_OF_CHARGE_BATTERY_7, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_7_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_1),						
						m(RenaultTmhChannelId.DAILY_ENERGY_THROUGHPUT_BATTERY_7, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_7_LEVEL_POINT + 4)),
						m(RenaultTmhChannelId.BATTERY_PACK_7_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_7_LEVEL_POINT + 5)),
						m(RenaultTmhChannelId.BATTERY_PACK_7_CELL_VOLTAGE_MAXIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_7_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_PACK_7_CELL_VOLTAGE_MINIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_7_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_7_PACK_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_7_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.BATTERY_7_PACK_TEMPERATUR, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_7_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.BATTERY_7_STATE_OF_HEALTH, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_7_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.BATTERY_7_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_7_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.BATTERY_7_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_7_LEVEL_POINT + 12)),
						m(RenaultTmhChannelId.BATTERY_7_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_7_LEVEL_POINT + 13)),
						m(RenaultTmhChannelId.BATTERY_7_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_7_LEVEL_POINT + 14)),
						
						m(RenaultTmhChannelId.BATTERY_8_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_8_LEVEL_POINT)),
						m(RenaultTmhChannelId.BATTERY_PACK_STATUS_8, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_8_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.STATE_OF_CHARGE_BATTERY_8, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_8_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_1),						
						m(RenaultTmhChannelId.DAILY_ENERGY_THROUGHPUT_BATTERY_8, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_8_LEVEL_POINT + 4)),
						m(RenaultTmhChannelId.BATTERY_PACK_8_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_8_LEVEL_POINT + 5)),
						m(RenaultTmhChannelId.BATTERY_PACK_8_CELL_VOLTAGE_MAXIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_8_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_PACK_8_CELL_VOLTAGE_MINIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_8_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_8_PACK_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_8_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.BATTERY_8_PACK_TEMPERATUR, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_8_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.BATTERY_8_STATE_OF_HEALTH, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_8_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.BATTERY_8_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_8_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.BATTERY_8_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_8_LEVEL_POINT + 12)),
						m(RenaultTmhChannelId.BATTERY_8_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_8_LEVEL_POINT + 13)),
						m(RenaultTmhChannelId.BATTERY_8_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_8_LEVEL_POINT + 14)),
						
						m(RenaultTmhChannelId.BATTERY_9_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_9_LEVEL_POINT)),
						m(RenaultTmhChannelId.BATTERY_PACK_STATUS_9, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_9_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.STATE_OF_CHARGE_BATTERY_9, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_9_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_1),						
						m(RenaultTmhChannelId.DAILY_ENERGY_THROUGHPUT_BATTERY_9, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_9_LEVEL_POINT + 4)),
						m(RenaultTmhChannelId.BATTERY_PACK_9_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_9_LEVEL_POINT + 5)),
						m(RenaultTmhChannelId.BATTERY_PACK_9_CELL_VOLTAGE_MAXIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_9_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_PACK_9_CELL_VOLTAGE_MINIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_9_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_9_PACK_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_9_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.BATTERY_9_PACK_TEMPERATUR, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_9_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.BATTERY_9_STATE_OF_HEALTH, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_9_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.BATTERY_9_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_9_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.BATTERY_9_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_9_LEVEL_POINT + 12)),
						m(RenaultTmhChannelId.BATTERY_9_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_9_LEVEL_POINT + 13)),
						m(RenaultTmhChannelId.BATTERY_9_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_9_LEVEL_POINT + 14)),
												
						m(RenaultTmhChannelId.BATTERY_10_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_10_LEVEL_POINT)),
						m(RenaultTmhChannelId.BATTERY_PACK_STATUS_10, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_10_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.STATE_OF_CHARGE_BATTERY_10, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_10_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_1),						
						m(RenaultTmhChannelId.DAILY_ENERGY_THROUGHPUT_BATTERY_10, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_10_LEVEL_POINT + 4)),
						m(RenaultTmhChannelId.BATTERY_PACK_10_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_10_LEVEL_POINT + 5)),
						m(RenaultTmhChannelId.BATTERY_PACK_10_CELL_VOLTAGE_MAXIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_10_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_PACK_10_CELL_VOLTAGE_MINIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_10_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_10_PACK_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_10_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.BATTERY_10_PACK_TEMPERATUR, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_10_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.BATTERY_10_STATE_OF_HEALTH, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_10_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.BATTERY_10_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_10_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.BATTERY_10_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_10_LEVEL_POINT + 12)),
						m(RenaultTmhChannelId.BATTERY_10_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_10_LEVEL_POINT + 13)),
						m(RenaultTmhChannelId.BATTERY_10_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_10_LEVEL_POINT + 14)),
						
						m(RenaultTmhChannelId.BATTERY_11_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_11_LEVEL_POINT)),
						m(RenaultTmhChannelId.BATTERY_PACK_STATUS_11, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_11_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.STATE_OF_CHARGE_BATTERY_11, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_11_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_1),						
						m(RenaultTmhChannelId.DAILY_ENERGY_THROUGHPUT_BATTERY_11, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_11_LEVEL_POINT + 4)),
						m(RenaultTmhChannelId.BATTERY_PACK_11_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_11_LEVEL_POINT + 5)),
						m(RenaultTmhChannelId.BATTERY_PACK_11_CELL_VOLTAGE_MAXIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_11_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_PACK_11_CELL_VOLTAGE_MINIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_11_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_11_PACK_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_11_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.BATTERY_11_PACK_TEMPERATUR, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_11_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.BATTERY_11_STATE_OF_HEALTH, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_11_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.BATTERY_11_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_11_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.BATTERY_11_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_11_LEVEL_POINT + 12)),
						m(RenaultTmhChannelId.BATTERY_11_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_11_LEVEL_POINT + 13)),
						m(RenaultTmhChannelId.BATTERY_11_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_11_LEVEL_POINT + 14)),
						
						m(RenaultTmhChannelId.BATTERY_12_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_12_LEVEL_POINT)),
						m(RenaultTmhChannelId.BATTERY_PACK_STATUS_12, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_12_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.STATE_OF_CHARGE_BATTERY_12, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_12_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_1),						
						m(RenaultTmhChannelId.DAILY_ENERGY_THROUGHPUT_BATTERY_12, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_12_LEVEL_POINT + 4)),
						m(RenaultTmhChannelId.BATTERY_PACK_12_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_12_LEVEL_POINT + 5)),
						m(RenaultTmhChannelId.BATTERY_PACK_12_CELL_VOLTAGE_MAXIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_12_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_PACK_12_CELL_VOLTAGE_MINIMUM, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_12_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_3),
						m(RenaultTmhChannelId.BATTERY_12_PACK_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_12_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.BATTERY_12_PACK_TEMPERATUR, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_12_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.BATTERY_12_STATE_OF_HEALTH, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_12_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.BATTERY_12_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_12_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.BATTERY_12_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_12_LEVEL_POINT + 12)),
						m(RenaultTmhChannelId.BATTERY_12_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_12_LEVEL_POINT + 13)),
						m(RenaultTmhChannelId.BATTERY_12_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_BATTERY_PACK_12_LEVEL_POINT + 14)),
						
						
		
						
						
						/*
						 * Inverter Points
						 */
						m(RenaultTmhChannelId.INVERTER_1_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_1_LEVEL_POINT)),
						m(RenaultTmhChannelId.INVERTER_1_STATUS, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_1_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.INVERTER_1_DC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_1_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_1_AC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_1_LEVEL_POINT + 4),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_2),
						m(RenaultTmhChannelId.INVERTER_1_AC_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_1_LEVEL_POINT + 5),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_1_ACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_1_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_1_REACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_1_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_1_APPARENT_POWER, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_1_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_1_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_1_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.INVERTER_1_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_1_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.INVERTER_1_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_1_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.INVERTER_1_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_1_LEVEL_POINT + 12)),
						
						m(RenaultTmhChannelId.INVERTER_2_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_2_LEVEL_POINT)),
						m(RenaultTmhChannelId.INVERTER_2_STATUS, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_2_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.INVERTER_2_DC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_2_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_2_AC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_2_LEVEL_POINT + 4),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_2),
						m(RenaultTmhChannelId.INVERTER_2_AC_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_2_LEVEL_POINT + 5),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_2_ACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_2_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_2_REACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_2_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_2_APPARENT_POWER, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_2_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_2_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_2_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.INVERTER_2_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_2_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.INVERTER_2_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_2_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.INVERTER_2_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_2_LEVEL_POINT + 12)),
						
						m(RenaultTmhChannelId.INVERTER_3_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_3_LEVEL_POINT)),
						m(RenaultTmhChannelId.INVERTER_3_STATUS, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_3_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.INVERTER_3_DC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_3_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_3_AC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_3_LEVEL_POINT + 4),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_2),
						m(RenaultTmhChannelId.INVERTER_3_AC_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_3_LEVEL_POINT + 5),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_3_ACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_3_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_3_REACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_3_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_3_APPARENT_POWER, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_3_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_3_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_3_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.INVERTER_3_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_3_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.INVERTER_3_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_3_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.INVERTER_3_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_3_LEVEL_POINT + 12)),
						
						m(RenaultTmhChannelId.INVERTER_4_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_4_LEVEL_POINT)),
						m(RenaultTmhChannelId.INVERTER_4_STATUS, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_4_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.INVERTER_4_DC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_4_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_4_AC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_4_LEVEL_POINT + 4),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_2),
						m(RenaultTmhChannelId.INVERTER_4_AC_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_4_LEVEL_POINT + 5),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_4_ACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_4_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_4_REACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_4_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_4_APPARENT_POWER, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_4_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_4_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_4_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.INVERTER_4_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_4_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.INVERTER_4_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_4_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.INVERTER_4_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_4_LEVEL_POINT + 12)),
						
						m(RenaultTmhChannelId.INVERTER_5_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_5_LEVEL_POINT)),
						m(RenaultTmhChannelId.INVERTER_5_STATUS, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_5_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.INVERTER_5_DC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_5_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_5_AC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_5_LEVEL_POINT + 4),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_2),
						m(RenaultTmhChannelId.INVERTER_5_AC_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_5_LEVEL_POINT + 5),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_5_ACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_5_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_5_REACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_5_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_5_APPARENT_POWER, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_5_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_5_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_5_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.INVERTER_5_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_5_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.INVERTER_5_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_5_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.INVERTER_5_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_5_LEVEL_POINT + 12)),
						
						m(RenaultTmhChannelId.INVERTER_6_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_6_LEVEL_POINT)),
						m(RenaultTmhChannelId.INVERTER_6_STATUS, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_6_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.INVERTER_6_DC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_6_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_6_AC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_6_LEVEL_POINT + 4),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_2),
						m(RenaultTmhChannelId.INVERTER_6_AC_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_6_LEVEL_POINT + 5),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_6_ACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_6_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_6_REACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_6_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_6_APPARENT_POWER, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_6_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_6_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_6_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.INVERTER_6_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_6_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.INVERTER_6_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_6_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.INVERTER_6_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_6_LEVEL_POINT + 12)),
						
						m(RenaultTmhChannelId.INVERTER_7_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_7_LEVEL_POINT)),
						m(RenaultTmhChannelId.INVERTER_7_STATUS, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_7_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.INVERTER_7_DC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_7_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_7_AC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_7_LEVEL_POINT + 4),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_2),
						m(RenaultTmhChannelId.INVERTER_7_AC_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_7_LEVEL_POINT + 5),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_7_ACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_7_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_7_REACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_7_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_7_APPARENT_POWER, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_7_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_7_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_7_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.INVERTER_7_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_7_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.INVERTER_7_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_7_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.INVERTER_7_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_7_LEVEL_POINT + 12)),
						
						m(RenaultTmhChannelId.INVERTER_8_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_8_LEVEL_POINT)),
						m(RenaultTmhChannelId.INVERTER_8_STATUS, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_8_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.INVERTER_8_DC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_8_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_8_AC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_8_LEVEL_POINT + 4),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_2),
						m(RenaultTmhChannelId.INVERTER_8_AC_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_8_LEVEL_POINT + 5),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_8_ACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_8_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_8_REACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_8_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_8_APPARENT_POWER, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_8_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_8_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_8_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.INVERTER_8_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_8_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.INVERTER_8_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_8_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.INVERTER_8_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_8_LEVEL_POINT + 12)),
						
						m(RenaultTmhChannelId.INVERTER_9_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_9_LEVEL_POINT)),
						m(RenaultTmhChannelId.INVERTER_9_STATUS, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_9_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.INVERTER_9_DC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_9_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_9_AC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_9_LEVEL_POINT + 4),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_2),
						m(RenaultTmhChannelId.INVERTER_9_AC_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_9_LEVEL_POINT + 5),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_9_ACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_9_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_9_REACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_9_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_9_APPARENT_POWER, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_9_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_9_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_9_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.INVERTER_9_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_9_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.INVERTER_9_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_9_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.INVERTER_9_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_9_LEVEL_POINT + 12)),
						
						m(RenaultTmhChannelId.INVERTER_10_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_10_LEVEL_POINT)),
						m(RenaultTmhChannelId.INVERTER_10_STATUS, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_10_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.INVERTER_10_DC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_10_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_10_AC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_10_LEVEL_POINT + 4),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_2),
						m(RenaultTmhChannelId.INVERTER_10_AC_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_10_LEVEL_POINT + 5),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_10_ACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_10_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_10_REACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_10_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_10_APPARENT_POWER, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_10_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_10_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_10_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.INVERTER_10_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_10_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.INVERTER_10_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_10_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.INVERTER_10_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_10_LEVEL_POINT + 12)),
						
						m(RenaultTmhChannelId.INVERTER_11_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_11_LEVEL_POINT)),
						m(RenaultTmhChannelId.INVERTER_11_STATUS, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_11_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.INVERTER_11_DC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_11_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_11_AC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_11_LEVEL_POINT + 4),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_2),
						m(RenaultTmhChannelId.INVERTER_11_AC_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_11_LEVEL_POINT + 5),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_11_ACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_11_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_11_REACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_11_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_11_APPARENT_POWER, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_11_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_11_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_11_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.INVERTER_11_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_11_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.INVERTER_11_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_11_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.INVERTER_11_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_11_LEVEL_POINT + 12)),
						
						m(RenaultTmhChannelId.INVERTER_12_ID, new UnsignedDoublewordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_12_LEVEL_POINT)),
						m(RenaultTmhChannelId.INVERTER_12_STATUS, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_12_LEVEL_POINT + 2)),
						m(RenaultTmhChannelId.INVERTER_12_DC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_12_LEVEL_POINT + 3),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_12_AC_VOLTAGE, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_12_LEVEL_POINT + 4),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_2),
						m(RenaultTmhChannelId.INVERTER_12_AC_CURRENT, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_12_LEVEL_POINT + 5),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1),
						m(RenaultTmhChannelId.INVERTER_12_ACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_12_LEVEL_POINT + 6),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_12_REACTIVE_POWER, new SignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_12_LEVEL_POINT + 7),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_12_APPARENT_POWER, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_12_LEVEL_POINT + 8),
								ElementToChannelConverter.SCALE_FACTOR_1),
						m(RenaultTmhChannelId.INVERTER_12_SPARE_A, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_12_LEVEL_POINT + 9)),
						m(RenaultTmhChannelId.INVERTER_12_SPARE_B, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_12_LEVEL_POINT + 10)),
						m(RenaultTmhChannelId.INVERTER_12_SPARE_C, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_12_LEVEL_POINT + 11)),
						m(RenaultTmhChannelId.INVERTER_12_SPARE_D, new UnsignedWordElement(ESS_MODBUS_ADDRESS_OFFSET_Inverter_12_LEVEL_POINT + 12)))
				);
	}




	
}
