package io.openems.edge.battery.soltaro.single.versionb_runnable_device;

import java.util.Collection;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.channel.AccessMode;
import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.api.Battery;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.State;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state.StateController;
import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.common.modbusslave.ModbusSlave;
import io.openems.edge.common.modbusslave.ModbusSlaveTable;

@Designate(ocd = Config.class, factory = true)
@Component( //
		name = "OpenEms.Component.For.Bms.Soltaro.SingleRack.VersionB", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE, //
		property = EventConstants.EVENT_TOPIC + "=" + EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE //
)
public class OsgiComponent implements EventHandler, OpenemsComponent, Battery, ModbusSlave {

	private SingleRack openEmsComponent;
	private State currentState;
	
	private final Logger log = LoggerFactory.getLogger(OsgiComponent.class);
	
	
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


	@Activate
	void activate(ComponentContext context, Config config) {
		openEmsComponent = new SingleRack();
		openEmsComponent.activate(context, config);
		StateController.init(openEmsComponent, config);
	}
	
	@Deactivate
	protected void deactivate() {
		openEmsComponent.deactivate();
		openEmsComponent = null;
	}


	@Override
	public String id() {
		return openEmsComponent.id();
	}

	@Override
	public String alias() {
		return openEmsComponent.alias();
	}


	@Override
	public boolean isEnabled() {
		return openEmsComponent.isEnabled();
	}


	@Override
	public ComponentContext getComponentContext() {
		return openEmsComponent.getComponentContext();
	}


	@Override
	public Channel<?> _channel(String channelName) {
		return openEmsComponent._channel(channelName);
	}


	@Override
	public Collection<Channel<?>> channels() {
		return openEmsComponent.channels();
	}

	@Override
	public ModbusSlaveTable getModbusSlaveTable(AccessMode accessMode) {
		return openEmsComponent.getModbusSlaveTable(accessMode);
	}
}
