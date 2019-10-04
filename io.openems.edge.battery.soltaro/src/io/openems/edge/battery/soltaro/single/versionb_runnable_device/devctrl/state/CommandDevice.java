package io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.common.channel.ChannelId;

public interface CommandDevice {
	
	void setWriteValue(ChannelId channelId, Object value) throws OpenemsNamedException;
	
	void setValue(ChannelId channelId, Object value);
	
	<T> T readValue(ChannelId channelId);

}
