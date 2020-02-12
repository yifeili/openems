package io.openems.edge.airconditioner.envicool;

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
import org.osgi.service.metatype.annotations.Designate;

import io.openems.common.channel.AccessMode;
import io.openems.edge.bridge.modbus.api.AbstractOpenemsModbusComponent;
import io.openems.edge.bridge.modbus.api.BridgeModbus;
import io.openems.edge.bridge.modbus.api.ElementToChannelConverter;
import io.openems.edge.bridge.modbus.api.ModbusProtocol;
import io.openems.edge.bridge.modbus.api.element.SignedWordElement;
import io.openems.edge.bridge.modbus.api.element.UnsignedWordElement;
import io.openems.edge.bridge.modbus.api.task.FC16WriteRegistersTask;
import io.openems.edge.bridge.modbus.api.task.FC3ReadRegistersTask;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.common.modbusslave.ModbusSlave;
import io.openems.edge.common.modbusslave.ModbusSlaveNatureTable;
import io.openems.edge.common.modbusslave.ModbusSlaveTable;
import io.openems.edge.common.taskmanager.Priority;

@Designate(ocd = Config.class, factory = true)
@Component( //
		name = "AirConditioner.Envicool", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE, //
		property = { EventConstants.EVENT_TOPIC + "=" + EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE })

public class Envicool extends AbstractOpenemsModbusComponent implements OpenemsComponent, ModbusSlave, EventHandler {

	@Reference
	protected ConfigurationAdmin cm;

	public Envicool() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				EnvicoolChannelId.values() //
		);
	}

	@Override
	@Reference(policy = ReferencePolicy.STATIC, policyOption = ReferencePolicyOption.GREEDY, cardinality = ReferenceCardinality.MANDATORY)
	protected void setModbus(BridgeModbus modbus) {
		super.setModbus(modbus);
	}

	@Activate
	void activate(ComponentContext context, Config config) {
		super.activate(context, config.id(), config.alias(), config.enabled(), config.modbusUnitId(), this.cm, "Modbus",
				config.modbus_id());
	}

	@Deactivate
	protected void deactivate() {
		super.deactivate();
	}

	@Override
	protected ModbusProtocol defineModbusProtocol() {
		return new ModbusProtocol(this,
				new FC3ReadRegistersTask(0x1000, Priority.HIGH, //
						m(EnvicoolChannelId.UNIT_RUNNING_STATUS, new UnsignedWordElement(0x1000)), //
						m(EnvicoolChannelId.INTERNAL_FAN_STATUS, new UnsignedWordElement(0x1002)), //
						m(EnvicoolChannelId.EXTERNAL_FAN_STATUS, new UnsignedWordElement(0x1004)), //
						m(EnvicoolChannelId.COMPRESSOR_STATUS, new UnsignedWordElement(0x1006)), //
						m(EnvicoolChannelId.INSIDE_RETURN_TEMP, new SignedWordElement(0x1008),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
						m(EnvicoolChannelId.PUMP_STATUS, new UnsignedWordElement(0x100A)), //
						m(EnvicoolChannelId.OUTSIDE_TEMP, new SignedWordElement(0x100C),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
						m(EnvicoolChannelId.CONDENSER_TEMP, new SignedWordElement(0x100E),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
						m(EnvicoolChannelId.EVAPORATOR_TEMP, new SignedWordElement(0x1010),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
						m(EnvicoolChannelId.INTERNAL_FAN_SPEED, new UnsignedWordElement(0x1012),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
						m(EnvicoolChannelId.EXTERNAL_FAN_SPEED, new UnsignedWordElement(0x1014),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
						m(EnvicoolChannelId.AC_INPUT_VOLTAGE, new UnsignedWordElement(0x1016),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
						m(EnvicoolChannelId.DC_INPUT_VOLTAGE, new UnsignedWordElement(0x1018),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
						m(EnvicoolChannelId.AC_RUNNING_CURRENT, new UnsignedWordElement(0x101A),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
						m(EnvicoolChannelId.UNIT_RUNNING_TIME_HIGH, new UnsignedWordElement(0x101C)), //
						m(EnvicoolChannelId.UNIT_RUNNING_TIME_LOW, new UnsignedWordElement(0x101D)), //
						m(EnvicoolChannelId.COMPRESSOR_RUNNING_TIME_HIGH, new UnsignedWordElement(0x1020)), //
						m(EnvicoolChannelId.COMPRESSOR_RUNNING_TIME_LOW, new UnsignedWordElement(0x1021)), //
						m(EnvicoolChannelId.INTERNAL_FAN_RUNNING_TIME_HIGH, new UnsignedWordElement(0x1024)), //
						m(EnvicoolChannelId.INTERNAL_FAN_RUNNING_TIME_LOW, new UnsignedWordElement(0x1025)), //
						m(EnvicoolChannelId.COMPRESSOR_ACTION_TIMES_HIGH, new UnsignedWordElement(0x1028)), //
						m(EnvicoolChannelId.COMPRESSOR_ACTION_TIMES_LOW, new UnsignedWordElement(0x1029)), //
						m(EnvicoolChannelId.SUPPLY_AIR_TEMPERATURE, new UnsignedWordElement(0xA004),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
						m(EnvicoolChannelId.INSIDE_RETURN_HUM, new SignedWordElement(0xA013)) //

				), new FC16WriteRegistersTask(0x07, //
						m(EnvicoolChannelId.MODBUS_SLAVE_ID, new UnsignedWordElement(0x07)), //
						m(EnvicoolChannelId.BAUD, new UnsignedWordElement(0x08)), //
						m(EnvicoolChannelId.HIGH_TEMP_ALARM_POINT, new UnsignedWordElement(0x0E),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
						m(EnvicoolChannelId.LOW_TEMP_ALARM_POINT, new UnsignedWordElement(0x10),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
						m(EnvicoolChannelId.DC_OVERVOLTAGE_ALARM, new UnsignedWordElement(0x12)), //
						m(EnvicoolChannelId.DC_UNDERVOLTAGE_ALARM, new UnsignedWordElement(0x14)), //
						m(EnvicoolChannelId.DC_OUTAGE_VOLTAGE, new UnsignedWordElement(0x16)), //
						m(EnvicoolChannelId.AC_OVERVOLTAGE_ALARM, new UnsignedWordElement(0x18)), //
						m(EnvicoolChannelId.AC_UNDERVOLTAGE_ALARM, new UnsignedWordElement(0x1A)), //
						m(EnvicoolChannelId.HEATING_SET_POINT, new UnsignedWordElement(0x1C),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
						m(EnvicoolChannelId.HEATING_HYSTERESIS, new UnsignedWordElement(0x1E),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1) //
				),
				new FC16WriteRegistersTask(0x8202,
						m(EnvicoolChannelId.COOLING_SET_POINT, new SignedWordElement(0x8202),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
						m(EnvicoolChannelId.COOLING_HYSTERESIS, new UnsignedWordElement(0x8204),
								ElementToChannelConverter.SCALE_FACTOR_MINUS_1) //
				),
				new FC16WriteRegistersTask(0x200,
						m(EnvicoolChannelId.RESTORE_FACTORY_SETTINGS, new UnsignedWordElement(0x200)), //
						m(EnvicoolChannelId.REMOTE_ON_OFF, new UnsignedWordElement(0x202)) //

				),
				// Alarms
				new FC3ReadRegistersTask(0x300, Priority.LOW,
						m(EnvicoolChannelId.STATE_1, new UnsignedWordElement(0x300)), //
						m(EnvicoolChannelId.STATE_2, new UnsignedWordElement(0x301)), //
						m(EnvicoolChannelId.STATE_3, new UnsignedWordElement(0x302)), //
						m(EnvicoolChannelId.STATE_4, new UnsignedWordElement(0x303)), //
						m(EnvicoolChannelId.STATE_5, new UnsignedWordElement(0x304)), //
						m(EnvicoolChannelId.STATE_6, new UnsignedWordElement(0x305)), //
						m(EnvicoolChannelId.STATE_7, new UnsignedWordElement(0x306)), //
						m(EnvicoolChannelId.STATE_8, new UnsignedWordElement(0x307)), //
						m(EnvicoolChannelId.STATE_9, new UnsignedWordElement(0x308)), //
						m(EnvicoolChannelId.STATE_10, new UnsignedWordElement(0x309)), //
						m(EnvicoolChannelId.STATE_11, new UnsignedWordElement(0x30A)), //
						m(EnvicoolChannelId.STATE_12, new UnsignedWordElement(0x30B)), //
						m(EnvicoolChannelId.STATE_13, new UnsignedWordElement(0x30C)), //
						m(EnvicoolChannelId.STATE_14, new UnsignedWordElement(0x30D)), //
						m(EnvicoolChannelId.STATE_15, new UnsignedWordElement(0x30E)), //
						m(EnvicoolChannelId.STATE_16, new UnsignedWordElement(0x30F)), //
						m(EnvicoolChannelId.STATE_17, new UnsignedWordElement(0x310)) //
				));
	}

	@Override
	public ModbusSlaveTable getModbusSlaveTable(AccessMode accessMode) {
		return new ModbusSlaveTable( //
				OpenemsComponent.getModbusSlaveNatureTable(accessMode), //
				ModbusSlaveNatureTable.of(Envicool.class, accessMode, 300) //
						.build());
	}

	@Override
	public void handleEvent(Event event) {

	}
}
