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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.channel.AccessMode;
import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.bridge.modbus.api.AbstractOpenemsModbusComponent;
import io.openems.edge.bridge.modbus.api.BridgeModbus;
import io.openems.edge.bridge.modbus.api.ElementToChannelConverter;
import io.openems.edge.bridge.modbus.api.ModbusProtocol;
import io.openems.edge.bridge.modbus.api.element.DummyRegisterElement;
import io.openems.edge.bridge.modbus.api.element.SignedWordElement;
import io.openems.edge.bridge.modbus.api.element.UnsignedWordElement;
import io.openems.edge.bridge.modbus.api.task.FC16WriteRegistersTask;
import io.openems.edge.bridge.modbus.api.task.FC3ReadRegistersTask;
import io.openems.edge.common.channel.EnumWriteChannel;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.common.modbusslave.ModbusSlave;
import io.openems.edge.common.modbusslave.ModbusSlaveNatureTable;
import io.openems.edge.common.modbusslave.ModbusSlaveTable;
import io.openems.edge.common.taskmanager.Priority;

@Designate(ocd = Config.class, factory = true)
@Component( //
		name = "Airconditioner.Envicool", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE, //
		property = { EventConstants.EVENT_TOPIC + "=" + EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE })

public class Envicool extends AbstractOpenemsModbusComponent implements OpenemsComponent, ModbusSlave, EventHandler {

	private final Logger log = LoggerFactory.getLogger(Envicool.class);
	
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

		setIntegerChannel(EnvicoolChannelId.HEATING_STOP_POINT, config.heatingStopPointTemperature(), -15, 15);
		setIntegerChannel(EnvicoolChannelId.HEATING_BAND, config.heatingHysteresis(), 1, 10);
		setIntegerChannel(EnvicoolChannelId.REFRIGERATION_STOP_POINT, config.coolingStopPointTemperature(), 15, 50);
		setIntegerChannel(EnvicoolChannelId.REFRIGERATION_BAND, config.coolingHysteresis(), 1, 10);
		setIntegerChannel(EnvicoolChannelId.DEHUMIDIFICATION_STOP_POINT, config.dehumidificationStopPoint(), 40, 90);
		setIntegerChannel(EnvicoolChannelId.DEHUMIDIFICATION_BAND, config.dehumidificationHysteresis(), 1, 30);
	}

	@Override
	@Deactivate
	protected void deactivate() {
		super.deactivate();
	}

	@Override
	protected ModbusProtocol defineModbusProtocol() {
		return new ModbusProtocol(this, new FC3ReadRegistersTask(0x0000, Priority.LOW,
				m(EnvicoolChannelId.SOFTWARE_VERSION, new UnsignedWordElement(0x0000)) //
		), new FC3ReadRegistersTask(0x100, Priority.LOW,
				m(EnvicoolChannelId.UNIT_RUNNING_STATUS, new UnsignedWordElement(0x100)), //
				m(EnvicoolChannelId.INTERNAL_FAN_STATUS, new UnsignedWordElement(0x101)), //
				m(EnvicoolChannelId.EXTERNAL_FAN_STATUS, new UnsignedWordElement(0x102)), //
				m(EnvicoolChannelId.COMPRESSOR_STATUS, new UnsignedWordElement(0x103)), //
				m(EnvicoolChannelId.HEATER_STATUS, new UnsignedWordElement(0x104)), //
				m(EnvicoolChannelId.EMERGENCY_FAN_STATUS, new UnsignedWordElement(0x105)) //

		), new FC3ReadRegistersTask(0x0500, Priority.HIGH,

				m(EnvicoolChannelId.EVAPORATOR_TEMP, new SignedWordElement(0x0500),
						ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
				m(EnvicoolChannelId.OUTDOOR_TEMP, new SignedWordElement(0x0501),
						ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
				m(EnvicoolChannelId.CONDENSER_TEMP, new SignedWordElement(0x0502),
						ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
				m(EnvicoolChannelId.INDOOR_TEMP, new SignedWordElement(0x0503),
						ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
				m(EnvicoolChannelId.HUMIDITY, new UnsignedWordElement(0x0504)), //
				m(EnvicoolChannelId.DISCHARGE_TEMP, new SignedWordElement(0x0505),
						ElementToChannelConverter.SCALE_FACTOR_MINUS_1), //
				m(EnvicoolChannelId.AC_RUNNING_CURRENT, new SignedWordElement(0x0506)), //
				m(EnvicoolChannelId.AC_INPUT_VOLTAGE, new SignedWordElement(0x0507)), //
				m(EnvicoolChannelId.DC_INPUT_VOLTAGE, new SignedWordElement(0x0508),
						ElementToChannelConverter.SCALE_FACTOR_MINUS_1) //

		), new FC3ReadRegistersTask(0x0600, Priority.LOW,

				m(EnvicoolChannelId.STATE_1, new UnsignedWordElement(0x0600)), //
				m(EnvicoolChannelId.STATE_2, new UnsignedWordElement(0x0601)), //
				m(EnvicoolChannelId.STATE_3, new UnsignedWordElement(0x0602)), //
				m(EnvicoolChannelId.STATE_4, new UnsignedWordElement(0x0603)), //
				m(EnvicoolChannelId.STATE_5, new UnsignedWordElement(0x0604)), //
				m(EnvicoolChannelId.STATE_6, new UnsignedWordElement(0x0605)), //
				m(EnvicoolChannelId.STATE_7, new UnsignedWordElement(0x0606)), //
				m(EnvicoolChannelId.STATE_8, new UnsignedWordElement(0x0607)), //
				m(EnvicoolChannelId.STATE_9, new UnsignedWordElement(0x0608)), //
				m(EnvicoolChannelId.STATE_10, new UnsignedWordElement(0x069)), //
				m(EnvicoolChannelId.STATE_11, new UnsignedWordElement(0x060A)), //
				m(EnvicoolChannelId.STATE_12, new UnsignedWordElement(0x060B)), //
				m(EnvicoolChannelId.STATE_13, new UnsignedWordElement(0x060C)), //
				m(EnvicoolChannelId.STATE_14, new UnsignedWordElement(0x060D)), //
				m(EnvicoolChannelId.STATE_15, new UnsignedWordElement(0x060E)), //
				m(EnvicoolChannelId.STATE_16, new UnsignedWordElement(0x060F)), //
				m(EnvicoolChannelId.STATE_17, new UnsignedWordElement(0x0610)), //
				m(EnvicoolChannelId.STATE_18, new UnsignedWordElement(0x0611)), //
				m(EnvicoolChannelId.STATE_19, new UnsignedWordElement(0x0612)), //
				m(EnvicoolChannelId.STATE_20, new UnsignedWordElement(0x0613)), //
				m(EnvicoolChannelId.STATE_21, new UnsignedWordElement(0x0614)), //
				m(EnvicoolChannelId.STATE_22, new UnsignedWordElement(0x0615)), //
				m(EnvicoolChannelId.STATE_23, new UnsignedWordElement(0x0616)), //
				m(EnvicoolChannelId.STATE_24, new UnsignedWordElement(0x0617)), //
				m(EnvicoolChannelId.STATE_25, new UnsignedWordElement(0x0618)), //
				m(EnvicoolChannelId.STATE_26, new UnsignedWordElement(0x0619)), //
				m(EnvicoolChannelId.STATE_27, new UnsignedWordElement(0x061A)), //
				m(EnvicoolChannelId.STATE_28, new UnsignedWordElement(0x061B)), //
				m(EnvicoolChannelId.STATE_29, new UnsignedWordElement(0x061C)), //
				m(EnvicoolChannelId.STATE_30, new UnsignedWordElement(0x061D)), //
				m(EnvicoolChannelId.STATE_31, new UnsignedWordElement(0x061E)), //
				m(EnvicoolChannelId.STATE_32, new UnsignedWordElement(0x061F)), //
				m(EnvicoolChannelId.STATE_33, new UnsignedWordElement(0x0620)) //

		), new FC3ReadRegistersTask(0x0700, Priority.HIGH,
				m(EnvicoolChannelId.REFRIGERATION_STOP_POINT, new SignedWordElement(0x0700)), //
				m(EnvicoolChannelId.REFRIGERATION_BAND, new SignedWordElement(0x0701)), //
				m(EnvicoolChannelId.HEATING_STOP_POINT, new SignedWordElement(0x0702)), //
				m(EnvicoolChannelId.HEATING_BAND, new SignedWordElement(0x0703)), //
				m(EnvicoolChannelId.DEHUMIDIFICATION_STOP_POINT, new SignedWordElement(0x0704)), //
				m(EnvicoolChannelId.DEHUMIDIFICATION_BAND, new SignedWordElement(0x0705)), //
				m(EnvicoolChannelId.HIGH_TEMP_POINT, new SignedWordElement(0x0706)), //
				m(EnvicoolChannelId.LOW_TEMP_POINT, new SignedWordElement(0x0707)), //
				m(EnvicoolChannelId.HIGH_HUMIDITY_POINT, new SignedWordElement(0x0708)), //
				new DummyRegisterElement(0x0709),
				m(EnvicoolChannelId.INTERNAL_FAN_STOP_POINT, new SignedWordElement(0x070A)) //

		), new FC16WriteRegistersTask(0x0700,
				m(EnvicoolChannelId.REFRIGERATION_STOP_POINT, new SignedWordElement(0x0700)), //
				m(EnvicoolChannelId.REFRIGERATION_BAND, new SignedWordElement(0x0701)), //
				m(EnvicoolChannelId.HEATING_STOP_POINT, new SignedWordElement(0x0702)), //
				m(EnvicoolChannelId.HEATING_BAND, new SignedWordElement(0x0703)), //
				m(EnvicoolChannelId.DEHUMIDIFICATION_STOP_POINT, new SignedWordElement(0x0704)), //
				m(EnvicoolChannelId.DEHUMIDIFICATION_BAND, new SignedWordElement(0x0705)), //
				m(EnvicoolChannelId.HIGH_TEMP_POINT, new SignedWordElement(0x0706)), //
				m(EnvicoolChannelId.LOW_TEMP_POINT, new SignedWordElement(0x0707)), //
				m(EnvicoolChannelId.HIGH_HUMIDITY_POINT, new SignedWordElement(0x0708)), //
				new DummyRegisterElement(0x0709),
				m(EnvicoolChannelId.INTERNAL_FAN_STOP_POINT, new SignedWordElement(0x070A)) //

		), new FC3ReadRegistersTask(0x0800, Priority.HIGH, new DummyRegisterElement(0x0800), //
				m(EnvicoolChannelId.REMOTE_ON_OFF, new UnsignedWordElement(0x0801)) //

		), new FC16WriteRegistersTask(0x0800, //
				new DummyRegisterElement(0x0800), m(EnvicoolChannelId.REMOTE_ON_OFF, new UnsignedWordElement(0x0801)) //
		));
	}

	/**
	 * Set the given channel if the configured value is between min and max.
	 * 
	 * @param channel     EnvicoolChannelId
	 * @param configValue Configured value that should be set
	 * @param min         Minimum allowed value for that channel
	 * @param max         Maximum allowed value for that channel
	 */
	private void setIntegerChannel(EnvicoolChannelId channel, int configValue, int min, int max) {

		if (min <= configValue && configValue <= max) {
			this.channel(channel).setNextValue(configValue);
		} else {
			this.logInfo(this.log, "The configured value wasn't in the allowed range");
		}
	}
	

	@Override
	public void handleEvent(Event event) {
		if (!this.isEnabled()) {
			return;
		}
		switch (event.getTopic()) {
		case EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE:
			this.activateRemoteMode();
		}
	}

	/**
	 * Activates the Remote-Mode
	 */
	private void activateRemoteMode() {
		try {
			EnumWriteChannel remoteOnOffChannel = this.channel(EnvicoolChannelId.REMOTE_ON_OFF);
			OnOf isRemoteOn = remoteOnOffChannel.value().asEnum();
			
			if ((isRemoteOn) != OnOf.ON) {
			
				// If Mode is not "Remote"
				remoteOnOffChannel.setNextWriteValue(OnOf.ON);	
			}
		} catch (OpenemsNamedException e) {
			this.logError(log, "Unable to activate Remote-Mode: " + e.getMessage());
		}
	}

	@Override
	public ModbusSlaveTable getModbusSlaveTable(AccessMode accessMode) {
		return new ModbusSlaveTable( //
				OpenemsComponent.getModbusSlaveNatureTable(accessMode), //
				ModbusSlaveNatureTable.of(Envicool.class, accessMode, 100) //
						.build());
	}
}
