package io.openems.edge.demo.arduino;

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

import com.fazecast.jSerialComm.SerialPort;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.channel.Unit;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;

@Designate(ocd = Config.class, factory = true)
@Component( //
		name = "Demo.Arduino", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE, //
		property = EventConstants.EVENT_TOPIC + "=" + EdgeEventConstants.TOPIC_CYCLE_BEFORE_PROCESS_IMAGE //
)
public class ArduinoDemo extends AbstractOpenemsComponent implements OpenemsComponent, EventHandler {

	private Worker worker = null;

	public enum ChannelId implements io.openems.edge.common.channel.ChannelId {
		VOLTAGE(Doc.of(OpenemsType.DOUBLE).unit(Unit.VOLT));

		private final Doc doc;

		private ChannelId(Doc doc) {
			this.doc = doc;
		}

		public Doc doc() {
			return this.doc;
		}
	}

	public ArduinoDemo() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				ChannelId.values() //
		);
	}

	@Activate
	void activate(ComponentContext context, Config config) throws OpenemsNamedException {
		super.activate(context, config.id(), config.enabled());

		if (config.enabled()) {
			this.worker = new Worker(this, SerialPort.getCommPort(config.portName()));
			this.worker.activate(config.id());
		}
	}

	@Deactivate
	protected void deactivate() {
		if (this.worker != null) {
			this.worker.deactivate();
		}
		super.deactivate();
	}

	@Override
	public String debugLog() {
		return "Voltage:" + this.channel(ChannelId.VOLTAGE).value().asString();
	}

	@Override
	public void handleEvent(Event event) {
		if (!this.isEnabled()) {
			return;
		}
		switch (event.getTopic()) {
		case EdgeEventConstants.TOPIC_CYCLE_BEFORE_PROCESS_IMAGE:
			if (this.worker != null) {
				this.worker.triggerNextRun();
			}
		}
	}

	@Override
	protected void logError(Logger log, String message) {
		super.logError(log, message);
	}
}
