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
import io.openems.edge.bridge.modbus.api.element.StringWordElement;
import io.openems.edge.bridge.modbus.api.element.UnsignedWordElement;
import io.openems.edge.bridge.modbus.api.task.FC16WriteRegistersTask;
import io.openems.edge.bridge.modbus.api.task.FC3ReadRegistersTask;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.common.modbusslave.ModbusSlave;
import io.openems.edge.common.modbusslave.ModbusSlaveTable;
import io.openems.edge.common.taskmanager.Priority;

import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.channel.AccessMode;




@Designate(ocd = Config.class, factory = true)
@Component(//
		name = "Renault.TMH", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE, //
		property = { EventConstants.EVENT_TOPIC + "=" + EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE,//
		}

)


public class RenaultTmh extends AbstractOpenemsModbusComponent
		implements OpenemsComponent, EventHandler, ModbusSlave {

	private final Logger log = LoggerFactory.getLogger(RenaultTmh.class);
	private Config config;
	
	public static final int DEFAULT_UNIT_ID = 1;
	
	@Reference
	protected ConfigurationAdmin cm;
	
	public RenaultTmh() {
		super(//
				OpenemsComponent.ChannelId.values()//
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
						m(RenaultTmhChannelId.SYSTEM_STATUS_TMH, new UnsignedWordElement(0)),
						m(RenaultTmhChannelId.POWER_REQUEST_ACTIVE_POWER, new UnsignedWordElement(1)),
						m(RenaultTmhChannelId.POWER_REQUEST_REACTIVE_POWER, new UnsignedWordElement(2))
						
						
						
						),
				
				
				new FC16WriteRegistersTask(SUNSPEC_123 + 22, //
						m(REFUStore88KChannelId.VAR_PCT_ENA, new UnsignedWordElement(SUNSPEC_123 + 22)))
				);
	}
	
}
