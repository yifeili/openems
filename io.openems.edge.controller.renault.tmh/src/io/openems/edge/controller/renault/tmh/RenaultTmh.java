package io.openems.edge.controller.renault.tmh;

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
import io.openems.edge.bridge.modbus.api.ModbusProtocol;
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
	public ModbusSlaveTable getModbusSlaveTable(AccessMode accessMode) {
		return new ModbusSlaveTable( //
				OpenemsComponent.getModbusSlaveNatureTable(accessMode) //
		);
	}


	
	@Override
	protected ModbusProtocol defineModbusProtocol() {
		// TODO Auto-generated method stub
		return new ModbusProtocol(this, //
				new FC3ReadRegistersTask(0, Priority.ONCE, //
						
						/**
						 * TMH to ESS
						 */
						
						/*
						 * Technical Unit Level Points
						 */
						m(RenaultTmhChannelId.SYSTEM_STATUS_TMH, new UnsignedWordElement(0)),
						m(RenaultTmhChannelId.POWER_REQUEST_ACTIVE_POWER, new ),
						m(RenaultTmhChannelId.POWER_REQUEST_REACTIVE_POWER, new UnsignedWordElement(3)),
						m(RenaultTmhChannelId.ERROR_RESET, new UnsignedWordElement(5)),
						m(RenaultTmhChannelId.ALIVE_COUNTER_TMH, new UnsignedWordElement(6)),
						
						/*
						 * Energy Storage Unit Level Points (i)
						 */
						m(RenaultTmhChannelId.ESU_STATUS_i_TMH, new UnsignedWordElement(7)),
						m(RenaultTmhChannelId.POWER_REQUEST_ACTIVE_POWER_i, new UnsignedWordElement(8)),
						m(RenaultTmhChannelId.POWER_REQUEST_REACTIVE_POWER_i, new UnsignedWordElement(9)),
						
						/*
						 * Inverter Level Points (n)
						 */
						m(RenaultTmhChannelId.INVERTER_STATUS_n, new UnsignedWordElement(10)),
						m(RenaultTmhChannelId.POWER_REQUEST_ACTIVE_POWER_n, new UnsignedWordElement(11)),
						m(RenaultTmhChannelId.POWER_REQUEST_REACTIVE_POWER_n, new UnsignedWordElement(12))),
				
				new FC16WriteRegistersTask(0, //
						
						/**
						 * ESS to TMH
						 */
						
						/*
						 * Technical Unit Level Points
						 */						
						m(RenaultTmhChannelId.TECHNICAL_UNIT_ID, new UnsignedWordElement(0)),
						m(RenaultTmhChannelId.SYSTEM_STATUS_ESS, new UnsignedWordElement(1)),
						m(RenaultTmhChannelId.CURRENT_MEASURED_ACTIVE_POWER, new UnsignedWordElement(2)),
						m(RenaultTmhChannelId.CURRENT_MEASURED_REACTIVE_POWER, new UnsignedWordElement(3)),
						m(RenaultTmhChannelId.ALIVE_COUNTER_ESS, new UnsignedWordElement(4)),
						m(RenaultTmhChannelId.MAXIMUM_AVAILABLE_POWER_DISCHARGE, new UnsignedWordElement(5)),
						m(RenaultTmhChannelId.MAXIMUM_AVAILABLE_POWER_CHARGE, new UnsignedWordElement(6)),
						m(RenaultTmhChannelId.AVAILABLE_ENERGY_DISCHARGE, new UnsignedWordElement(7)),
						m(RenaultTmhChannelId.AVAILABLE_ENERGY_CHARGE, new UnsignedWordElement(8)),
						m(RenaultTmhChannelId.STORAGE_SYSTEM_ERROR, new UnsignedWordElement(9)),
						m(RenaultTmhChannelId.PAD_REGISTER_0, new UnsignedWordElement(10)),
						m(RenaultTmhChannelId.PAD_REGISTER_1, new UnsignedWordElement(11)),
						m(RenaultTmhChannelId.TECHNICAL_UNIT_ENERGY_THROUGHPUT_YTD, new UnsignedWordElement(12)),
						m(RenaultTmhChannelId.INDOOR_AMBIENT_TEMPERATURE, new UnsignedWordElement(13)),

						/*
						 * Energy Storage Unit Level Points (i)
						 */
						m(RenaultTmhChannelId.ESU_ID, new UnsignedWordElement(14)),
						m(RenaultTmhChannelId.ESU_STATUS_i_ESS, new UnsignedWordElement(15)),
						
						/*
						 * Battery Pack Level Points (m)
						 */
						m(RenaultTmhChannelId.BATTERY_ID, new UnsignedWordElement(16)),
						m(RenaultTmhChannelId.BATTERY_PACK_STATUS_m, new UnsignedWordElement(17)),
						m(RenaultTmhChannelId.STATE_OF_CHARGE_BATTERY_m, new UnsignedWordElement(18)),
						m(RenaultTmhChannelId.DAILY_ENERGY_THROUGHPUT_BATTERY_m, new UnsignedWordElement(19)),
						m(RenaultTmhChannelId.BATTERY_PACK_m_VOLTAGE, new UnsignedWordElement(20)),
						m(RenaultTmhChannelId.BATTERY_PACK_m_CELL_VOLTAGE_MAXIMUM, new UnsignedWordElement(21)),
						m(RenaultTmhChannelId.BATTERY_PACK_m_CELL_VOLTAGE_MINIMUM, new UnsignedWordElement(22)),
						m(RenaultTmhChannelId.BATTERY_m_PACK_CURRENT, new UnsignedWordElement(23)),
						m(RenaultTmhChannelId.BATTERY_m_PACK_TEMPERATUR, new UnsignedWordElement(24)),
						m(RenaultTmhChannelId.BATTERY_m_STATE_OF_HEALTH, new UnsignedWordElement(25)),
						m(RenaultTmhChannelId.BATTERY_m_SPARE_A, new UnsignedWordElement(26)),
						m(RenaultTmhChannelId.BATTERY_m_SPARE_B, new UnsignedWordElement(27)),
						m(RenaultTmhChannelId.BATTERY_m_SPARE_C, new UnsignedWordElement(28)),
						m(RenaultTmhChannelId.BATTERY_m_SPARE_D, new UnsignedWordElement(29)),
						
						/*
						 * Inverter Points (n)
						 */
						m(RenaultTmhChannelId.INVERTER_ID, new UnsignedWordElement(30)),
						m(RenaultTmhChannelId.INVERTER_n_STATUS, new UnsignedWordElement(31)),
						m(RenaultTmhChannelId.INVERTER_n_DC_VOLTAGE, new UnsignedWordElement(32)),
						m(RenaultTmhChannelId.INVERTER_n_AC_VOLTAGE, new UnsignedWordElement(33)),
						m(RenaultTmhChannelId.INVERTER_n_AC_CURRENT, new UnsignedWordElement(34)),
						m(RenaultTmhChannelId.INVERTER_n_ACTIVE_POWER, new UnsignedWordElement(35)),
						m(RenaultTmhChannelId.INVERTER_n_REACTIVE_POWER, new UnsignedWordElement(36)),
						m(RenaultTmhChannelId.INVERTER_n_APPARENT_POWER, new UnsignedWordElement(37)),
						m(RenaultTmhChannelId.INVERTER_n_SPARE_A, new UnsignedWordElement(38)),
						m(RenaultTmhChannelId.INVERTER_n_SPARE_B, new UnsignedWordElement(39)),
						m(RenaultTmhChannelId.INVERTER_n_SPARE_C, new UnsignedWordElement(40)),
						m(RenaultTmhChannelId.INVERTER_n_SPARE_D, new UnsignedWordElement(41))
						)
				);
	}



	@Override
	public void run() throws OpenemsNamedException {
		// TODO Auto-generated method stub
		
	}
	
}
