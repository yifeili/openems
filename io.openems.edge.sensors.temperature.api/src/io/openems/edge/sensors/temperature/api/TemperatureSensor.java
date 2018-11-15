package io.openems.edge.sensors.temperature.api;

import org.osgi.annotation.versioning.ProviderType;

import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.channel.doc.Doc;
import io.openems.edge.common.channel.doc.Unit;
import io.openems.edge.common.component.OpenemsComponent;

/**
 * Represents a Temperature Sensor.
 */
@ProviderType
public interface TemperatureSensor extends OpenemsComponent {

	public enum ChannelId implements io.openems.edge.common.channel.doc.ChannelId {
		/**
		 * Temperature
		 * 
		 * <ul>
		 * <li>Interface: TemperatureSensor
		 * <li>Type: Integer
		 * <li>Unit: Degree Celsius
		 * </ul>
		 */
		TEMPERATURE(new Doc().type(OpenemsType.INTEGER).unit(Unit.DEZIDEGREE_CELSIUS)); //

		private final Doc doc;

		private ChannelId(Doc doc) {
			this.doc = doc;
		}

		public Doc doc() {
			return this.doc;
		}
	}

	/**
	 * Gets the Temperature in [dezidegree celsius].
	 * 
	 * @return
	 */
	default Channel<Integer> getTemperature() {
		return this.channel(ChannelId.TEMPERATURE);
	}
}
