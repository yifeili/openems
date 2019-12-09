package io.openems.edge.bridge.lmnwired.api.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.edge.bridge.lmnwired.api.AbstractOpenEmsLMNWiredComponent;
import io.openems.edge.bridge.lmnwired.api.BridgeLMNWired;
import io.openems.edge.bridge.lmnwired.api.Device;
import io.openems.edge.bridge.lmnwired.hdlc.HdlcDataRequest;
import io.openems.edge.bridge.lmnwired.hdlc.HdlcFrame;
import io.openems.edge.bridge.lmnwired.hdlc.HdlcFrameDeviceDataRequest;
import io.openems.edge.common.channel.Channel;
import io.openems.edge.meter.api.SymmetricMeter;

import java.util.HashMap;
import java.util.Objects;

public class LMNWiredTask {

	protected AbstractOpenEmsLMNWiredComponent abstractOpenEmsLMNWiredComponent;
	protected BridgeLMNWired bridgeLMNWired;
	Device device;
	String obisPart;
	String obis;
	String serialNumber;
	HdlcFrameDeviceDataRequest hdlcFrameDeviceDataRequest;
	byte hdlcData[];
	int hdlcDataLength;
	SymmetricMeter.ChannelId channelId;
	int millisForTimeout = 5;
	private final Logger log = LoggerFactory.getLogger(LMNWiredTask.class);
	public boolean timeOutOccured = true;
	Float fData;

	long lastDataTimestamps[] = new long[10];
	float lastDatas[] = new float[10];
	Channel<Float> channel;
	long lastDataTimestamp = 0;
	int position = 0;
	HashMap<String, SymmetricMeter.ChannelId> obisChannelMapping;
	String[] obisList;

	public LMNWiredTask(AbstractOpenEmsLMNWiredComponent abstractOpenEmsLMNWiredComponent,
			BridgeLMNWired bridgeLMNWired, String serialNumber, String[] obisList,
			HashMap<String, SymmetricMeter.ChannelId> obisChannelMapping) {
		this.abstractOpenEmsLMNWiredComponent = abstractOpenEmsLMNWiredComponent;
		this.bridgeLMNWired = bridgeLMNWired;
		this.obisList = obisList;
		this.obisChannelMapping = obisChannelMapping;
		this.serialNumber = serialNumber;

		resetHistory();

	}

	private void resetHistory() {
		for (int i = 0; i < 10; i++) {
			lastDatas[i] = 0;
			lastDataTimestamps[i] = 0;
		}
		position = 0;
		lastDataTimestamp = 0;
	}

	public Device getDevice() {
		return device;
	}

	public HdlcFrameDeviceDataRequest gethdlcFrameDeviceDataRequest() {
		return hdlcFrameDeviceDataRequest;
	}

	public byte[] getHdlcData() {
		return hdlcData;
	}

	public int getHdlcDataLength() {
		return hdlcDataLength;
	}

	public boolean getRequest() {

		if (!bridgeLMNWired.getDeviceList().isEmpty())
			for (Device tmpDevice : bridgeLMNWired.getDeviceList()) {
				if (new String(tmpDevice.getSerialNumber()).equals(serialNumber)) {
					device = tmpDevice;

					log.info("Request Data for channels (" + String.join(";", obisList) + ")");

					hdlcFrameDeviceDataRequest = new HdlcFrameDeviceDataRequest(device, String.join(";", obisList));
					bridgeLMNWired.getPackageHandler().addHdlcDataRequest(hdlcFrameDeviceDataRequest, 10,
							HdlcDataRequest.RequestSource.DATA, this);

					return true;
				}
			}

		return false;
	}

	/**
	 * 
	 * @param hdlcFrame Raw Data Frame
	 */
	public void setResponse(HdlcFrame hdlcFrame) {
		try {

			String lines[] = new String(hdlcFrame.getData()).split("\n");

			for (String line : lines) {

				String[] tmpString = line.split(";");
				String[] arrData = tmpString[1].split("\\*");

				System.out.println(line);

				try {
					if (arrData[1].contentEquals("kWh")) {
						// Update value to Ws from kWh if needed
						fData = (Float.parseFloat(arrData[0])) * 1000 * 3600; // KWH in WH and Wh to Ws
					} else {
						fData = Float.parseFloat(arrData[0]);
					}
				} catch (Exception e) {
					log.info(line);
					continue;
				}
				// Calculate current power from history data
				if (lastDataTimestamp == 0) {
					lastDataTimestamp = System.currentTimeMillis();
					lastDatas[position % 10] = fData;
					lastDataTimestamps[position % 10] = System.currentTimeMillis();
					continue;
				}
				if (Objects.equals(lastDatas[position % 10], fData)) {
					if (lastDataTimestamp - System.currentTimeMillis() > 30000) {
						resetHistory();
					}
					continue;
				}

				float lastData = lastDatas[(position + 1) % 10];
				float deltaT = System.currentTimeMillis() - lastDataTimestamps[(position + 1) % 10];

				if (lastDatas[(position + 1) % 10] == 0) {
					lastData = lastDatas[0];
				}

				float val = (fData - lastData) / ((deltaT) / 1000);

//			log.info("New Value: " + val);
				channelId = obisChannelMapping.get(tmpString[0]);
				channel = this.abstractOpenEmsLMNWiredComponent.channel(channelId);
				channel.setNextValue(val);

				if (channelId == SymmetricMeter.ChannelId.ACTIVE_CONSUMPTION_ENERGY) {
					this.abstractOpenEmsLMNWiredComponent.channel(SymmetricMeter.ChannelId.ACTIVE_POWER)
							.setNextValue(val);
				}
				if (channelId == SymmetricMeter.ChannelId.ACTIVE_PRODUCTION_ENERGY) {
					this.abstractOpenEmsLMNWiredComponent.channel(SymmetricMeter.ChannelId.ACTIVE_POWER)
							.setNextValue(-val);
				}
			}
			lastDataTimestamp = System.currentTimeMillis();
			position++;
			lastDatas[position % 10] = fData;
			lastDataTimestamps[position % 10] = System.currentTimeMillis();
		} catch (Exception e) {
			log.info(e.getMessage());
		}

	}

}
