package io.openems.edge.battery.soltaro.single.versionb_runnable_device;

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
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.channel.AccessMode;
import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.api.Battery;
import io.openems.edge.battery.soltaro.ModuleParameters;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.CommunicationBridge;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.State;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state.StateController;
import io.openems.edge.bridge.modbus.AbstractModbusBridge;
import io.openems.edge.bridge.modbus.api.AbstractOpenemsModbusComponent;
import io.openems.edge.bridge.modbus.api.BridgeModbus;
import io.openems.edge.bridge.modbus.api.ModbusProtocol;
import io.openems.edge.bridge.modbus.api.task.Task;
import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.channel.IntegerWriteChannel;
import io.openems.edge.common.channel.WriteChannel;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.common.modbusslave.ModbusSlave;
import io.openems.edge.common.modbusslave.ModbusSlaveTable;

@Designate(ocd = Config.class, factory = true)
@Component( //
		name = "OpenEms.Component.For.Bms.Soltaro.SingleRack.VersionB.NewImplementation", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE, //
		property = EventConstants.EVENT_TOPIC + "=" + EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE //
)
public class OsgiComponent extends AbstractOpenemsModbusComponent implements EventHandler, OpenemsComponent, Battery, ModbusSlave, CommunicationBridge {

	// , // JsonApi // TODO
	
	SoltaroComponent soltaroComponent;	
	
	private State currentState;
	private Config config;
	@Reference
	protected ConfigurationAdmin cm;
	
	@Reference
	protected ComponentManager componentManager;
	
	private final Logger log = LoggerFactory.getLogger(OsgiComponent.class);

	public OsgiComponent() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				Battery.ChannelId.values(), //
				SingleRackChannelId.values() //
		);
	}

	@Reference(policy = ReferencePolicy.STATIC, policyOption = ReferencePolicyOption.GREEDY, cardinality = ReferenceCardinality.MANDATORY)
	protected void setModbus(BridgeModbus modbus) {
		super.setModbus(modbus);
	}	
	
	@Override
	public void handleEvent(Event event) {
		
		if (!this.isEnabled()) {
			return;
		}
		switch (event.getTopic()) {

		case EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE:
			
			handle();
			break;
		}
	}


	private void handle() {
		State newState = StateController.getState(currentState.getNextState());
		newState.setStateBefore(currentState.getStateEnum());
		currentState = newState;
		
		this.setReadyForWorkingChannel(currentState.getStateEnum() == StateEnum.RUNNING);
		
		try {
			currentState.act();
		} catch (OpenemsException e) {
			log.error(e.getMessage());
		}
	}
		
	private void setReadyForWorkingChannel(boolean b) {		
		this.getReadyForWorking().setNextValue(b);
		
	}

	@Override
	public String debugLog() {
		return "SoC:" + this.getSoc().value() //
				+ "|Discharge:" + this.getDischargeMinVoltage().value() + ";" + this.getDischargeMaxCurrent().value() //
				+ "|Charge:" + this.getChargeMaxVoltage().value() + ";" + this.getChargeMaxCurrent().value() //
				+ "|State:" + this.currentState.getStateEnum();
	}

	@Activate
	void activate(ComponentContext context, Config config) throws OpenemsNamedException {		  
		SingleRack bms = new SingleRack(config.numberOfSlaves(), config.ReduceTasks(), this, this);
		soltaroComponent = new SoltaroComponent(bms);		
		this.config = config;
		StateController.init(soltaroComponent, config);
		currentState = StateController.getUndefinedState();

		super.activate(context, config.id(), config.alias(), config.enabled(), config.modbusUnitId(), this.cm, "Modbus",
				config.modbus_id());

		initializeCallbacks();
		setWatchdog(config.watchdog());
		setSoCLowAlarm(config.SoCLowAlarm());
		setCapacity();		
	
	}
	
	@Deactivate
	protected void deactivate() {
		soltaroComponent = null;
		// TODO remove channels!?
		super.deactivate();
	}

	@Override
	public ModbusSlaveTable getModbusSlaveTable(AccessMode accessMode) {
		return new ModbusSlaveTable( //
				OpenemsComponent.getModbusSlaveNatureTable(accessMode), //
				Battery.getModbusSlaveNatureTable(accessMode) //
		);
	}

	private void setCapacity() {
		int capacity = this.config.numberOfSlaves() * ModuleParameters.CAPACITY_WH.getValue() / 1000;
		this.channel(Battery.ChannelId.CAPACITY).setNextValue(capacity);
	}

	private void setWatchdog(int time_seconds) {
		try {
			IntegerWriteChannel c = this.channel(SingleRackChannelId.EMS_COMMUNICATION_TIMEOUT);
			c.setNextWriteValue(time_seconds);
		} catch (OpenemsNamedException e) {
			log.error("Error while setting ems timeout!\n" + e.getMessage());
		}
	}
	
	private void setSoCLowAlarm(int soCLowAlarm) {
		try {
			((IntegerWriteChannel) this.channel(SingleRackChannelId.STOP_PARAMETER_SOC_LOW_PROTECTION))
					.setNextWriteValue(soCLowAlarm);
			((IntegerWriteChannel) this.channel(SingleRackChannelId.STOP_PARAMETER_SOC_LOW_PROTECTION_RECOVER))
					.setNextWriteValue(soCLowAlarm);
		} catch (OpenemsNamedException e) {
			log.error("Error while setting parameter for soc low protection!" + e.getMessage());
		}
	}
	
	private void initializeCallbacks() {

		this.channel(SingleRackChannelId.CLUSTER_1_SOC).onChange((oldValue, newValue) -> {
			@SuppressWarnings("unchecked")
			Optional<Integer> socOpt = (Optional<Integer>) newValue.asOptional();
			if (!socOpt.isPresent()) {
				return;
			}
			int soc = socOpt.get();
			log.debug("callback soc, value: " + soc);
			this.channel(Battery.ChannelId.SOC).setNextValue(soc);
		});
		
		this.channel(SingleRackChannelId.CLUSTER_1_VOLTAGE).onChange((oldValue, newValue) -> {
			@SuppressWarnings("unchecked")
			Optional<Integer> vOpt = (Optional<Integer>) newValue.asOptional();
			if (!vOpt.isPresent()) {
				return;
			}
			int voltage_volt = (int) (vOpt.get() * 0.001);
			log.debug("callback voltage, value: " + voltage_volt);
			this.channel(Battery.ChannelId.VOLTAGE).setNextValue(voltage_volt);
		});
		
		this.channel(SingleRackChannelId.CLUSTER_1_MIN_CELL_VOLTAGE).onChange((oldValue, newValue) -> {
			@SuppressWarnings("unchecked")
			Optional<Integer> vOpt = (Optional<Integer>) newValue.asOptional();
			if (!vOpt.isPresent()) {
				return;
			}
			int voltage_millivolt = vOpt.get();
			log.debug("callback min cell voltage, value: " + voltage_millivolt);
			this.channel(Battery.ChannelId.MIN_CELL_VOLTAGE).setNextValue(voltage_millivolt);
		});

		// write battery ranges to according channels in battery api
		// MAX_VOLTAGE x2082
		this.channel(SingleRackChannelId.WARN_PARAMETER_SYSTEM_OVER_VOLTAGE_ALARM).onChange((oldValue, newValue) -> {
			@SuppressWarnings("unchecked")
			Optional<Integer> vOpt = (Optional<Integer>) newValue.asOptional();
			if (!vOpt.isPresent()) {
				return;
			}
			int max_charge_voltage = (int) (vOpt.get() * 0.001);
			log.debug("callback battery range, max charge voltage, value: " + max_charge_voltage);
			this.channel(Battery.ChannelId.CHARGE_MAX_VOLTAGE).setNextValue(max_charge_voltage);
		});

		// DISCHARGE_MIN_VOLTAGE 0x2088
		this.channel(SingleRackChannelId.WARN_PARAMETER_SYSTEM_UNDER_VOLTAGE_ALARM).onChange((oldValue, newValue) -> {
			@SuppressWarnings("unchecked")
			Optional<Integer> vOpt = (Optional<Integer>) newValue.asOptional();
			if (!vOpt.isPresent()) {
				return;
			}
			int min_discharge_voltage = (int) (vOpt.get() * 0.001);
			log.debug("callback battery range, min discharge voltage, value: " + min_discharge_voltage);
			this.channel(Battery.ChannelId.DISCHARGE_MIN_VOLTAGE).setNextValue(min_discharge_voltage);
		});

		// CHARGE_MAX_CURRENT 0x2160
		this.channel(SingleRackChannelId.SYSTEM_MAX_CHARGE_CURRENT).onChange((oldValue, newValue) -> {
			@SuppressWarnings("unchecked")
			Optional<Integer> cOpt = (Optional<Integer>) newValue.asOptional();
			if (!cOpt.isPresent()) {
				return;
			}
			int max_current = (int) (cOpt.get() * 0.001);
			log.debug("callback battery range, max charge current, value: " + max_current);
			this.channel(Battery.ChannelId.CHARGE_MAX_CURRENT).setNextValue(max_current);
		});

		// DISCHARGE_MAX_CURRENT 0x2161
		this.channel(SingleRackChannelId.SYSTEM_MAX_DISCHARGE_CURRENT).onChange((oldValue, newValue) -> {
			@SuppressWarnings("unchecked")
			Optional<Integer> cOpt = (Optional<Integer>) newValue.asOptional();
			if (!cOpt.isPresent()) {
				return;
			}
			int max_current = (int) (cOpt.get() * 0.001);
			log.debug("callback battery range, max discharge current, value: " + max_current);
			this.channel(Battery.ChannelId.DISCHARGE_MAX_CURRENT).setNextValue(max_current);
		});

	}

	@Override
	protected ModbusProtocol defineModbusProtocol() {
		ModbusProtocol protocol = new ModbusProtocol(this, this.soltaroComponent.getTasks(this).toArray(new Task[] {})); 
		return protocol; 
	}

	@Override
	public void setWriteValue(io.openems.edge.common.channel.ChannelId channelId, Object value) throws OpenemsNamedException {
		WriteChannel<?> wc = this.channel(channelId);
		wc.setNextWriteValueFromObject(value);
	}

	@Override
	public void setValue(io.openems.edge.common.channel.ChannelId channelId, Object value) {
		Channel<?> wc = this.channel(channelId);
		wc.setNextValue(value);
	}

	@Override
	public <T> T readValue(io.openems.edge.common.channel.ChannelId channelId) {
		Channel<T> channel = this.channel(channelId);
		return channel.getNextValue().get();
	}

	@Override
	public boolean isCommunicationAvailable() {
		
		AbstractModbusBridge modbusBridge = null;
		try {
			modbusBridge = this.componentManager.getComponent(config.modbus_id());
		} catch (OpenemsNamedException e) {
			log.error("Error while getting the modbus component");
			return false;
		}
		
		if (modbusBridge == null) {
			return false;
		}

		Channel<Boolean> slaveCommunicationFailedChannel = modbusBridge.getSlaveCommunicationFailedChannel();
		Optional<Boolean> communicationFailedOpt = slaveCommunicationFailedChannel.value().asOptional();

		// If the channel value is present and it is set then the communication is
		// broken
		return communicationFailedOpt.isPresent() && !communicationFailedOpt.get();			 
	}
}
