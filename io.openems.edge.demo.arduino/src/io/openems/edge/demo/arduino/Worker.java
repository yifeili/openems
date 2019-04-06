package io.openems.edge.demo.arduino;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;

import io.openems.common.exceptions.OpenemsException;
import io.openems.common.worker.AbstractCycleWorker;
import io.openems.edge.common.channel.DoubleReadChannel;

public class Worker extends AbstractCycleWorker {

	private final Logger log = LoggerFactory.getLogger(Worker.class);
	private final ArduinoDemo parent;
	private final SerialPort port;

	private Scanner data = null;

	public Worker(ArduinoDemo parent, SerialPort port) {
		this.parent = parent;
		this.port = port;
	}

	@Override
	protected void forever() {
		DoubleReadChannel voltageChannel = this.parent.channel(ArduinoDemo.ChannelId.VOLTAGE);
		try {
			Scanner data = this.openAndGetScanner();
			if (!data.hasNextLine()) {
				// Set Channel to UNDEFINED
				voltageChannel.setNextValue(null);
			} else {

				// calculate voltage
				double bare = 0;
				bare = Integer.parseInt(data.nextLine());
				double factor = 5.02;
				double result = bare / 1023 * factor;

				// Set the Channel-Value
				voltageChannel.setNextValue(result);
			}
		} catch (OpenemsException e) {
			// log error and set Channel UNDEFINED
			this.parent.logError(this.log, e.getMessage());
			voltageChannel.setNextValue(null);
		}
	}

	/**
	 * Opens and Gets the Data Scanner.
	 * 
	 * @return the Scanner instance
	 * @throws OpenemsException on error
	 */
	private Scanner openAndGetScanner() throws OpenemsException {
		if (this.data == null) {
			SerialPort port = this.openAndGetPort();
			this.data = new Scanner(port.getInputStream());
		}
		return this.data;
	}

	/**
	 * Opens and Gets the SerialPort.
	 * 
	 * @return the SerialPort instance
	 * @throws OpenemsException on error
	 */
	private SerialPort openAndGetPort() throws OpenemsException {
		SerialPort port = this.port;
		port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		if (port.isOpen()) {
			// Port was already open -> return
			return port;
		}

		if (this.port.openPort()) {
			// Successfully opened Port
			return port;
		}

		throw new OpenemsException("Unable to open the port [" + port.getSystemPortName() + "]");
	}

}
