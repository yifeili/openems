package io.openems.edge.project.sambia.controller.supplybusswitch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.exceptions.OpenemsException;
import io.openems.common.types.ChannelAddress;
import io.openems.edge.common.channel.BooleanWriteChannel;
import io.openems.edge.common.channel.IntegerWriteChannel;
import io.openems.edge.common.sum.GridMode;
import io.openems.edge.ess.api.SymmetricEss;
import io.openems.edge.ess.fenecon.commercial40.EssFeneconCommercial40Impl;
import io.openems.edge.ess.fenecon.commercial40.EssFeneconCommercial40Impl.ChannelId;
import io.openems.edge.ess.fenecon.commercial40.SystemState;

public class SupplyBus {

	private final static int MIN_SOC = 15;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final SupplyBusSwitchController parent;
	private final Map<String, ChannelAddress> switchEssMapping; // maps Ess-ID to IO-ChannelAddress
	private final String name; // the name of this Bus
	private final String primaryEssId; // Component-ID of first ESS
	private final long switchDelay;
	private final ChannelAddress supplybusOnIndication;
	private final List<ChannelAddress> loads;

	private long timeLoadSwitched = 0L;
	private long lastTimeDisconnected;

	private String activeEssId; // Component-ID of the Active-ESS

	enum State {
		CONNECTED, DISCONNECTING, DISCONNECTED, CONNECTING, UNKNOWN
	}

	private State state = State.UNKNOWN;

	public SupplyBus(SupplyBusSwitchController parent, Map<String, ChannelAddress> switchEssMapping, String name,
			String primaryEssId, long switchDelay, ChannelAddress supplybusOnIndication, List<ChannelAddress> loads) {
		this.parent = parent;
		this.switchEssMapping = switchEssMapping;
		this.name = name;
		this.primaryEssId = primaryEssId;
		this.switchDelay = switchDelay;
		this.supplybusOnIndication = supplybusOnIndication;
		this.loads = loads;
	}

	public String getName() {
		return name;
	}

	public void run() throws OpenemsNamedException {
		switch (this.state) {
		case CONNECTED: {
			String actualActiveEssId = this.getActiveEssId();
			if (actualActiveEssId == null || actualActiveEssId != this.activeEssId) {
				this.switchState(State.DISCONNECTING);

			} else {
				// check if Ess is completely discharged
				EssFeneconCommercial40Impl ess = this.parent.componentManager.getComponent(actualActiveEssId);
				GridMode gridMode = ess.getGridMode().value().asEnum();
				int soc = ess.getSoc().value().orElse(Integer.MAX_VALUE);
//				SystemState systemState = ess.getSystemState();
				SystemState systemState = ess.channel(ChannelId.SYSTEM_STATE).value().asEnum();
				if ((gridMode.equals(GridMode.OFF_GRID) && soc < MIN_SOC) || systemState.equals(SystemState.FAULT)
						|| systemState.equals(SystemState.STOP)) {
					this.switchState(State.DISCONNECTING);

				} else {
					this.setSupplybusOnIndication(1);
				}
			}
		}
			break;

		case CONNECTING: {
			// if not connected send connect command again
			if (this.isConnected()) {
				this.setSupplybusOnIndication(1);

				// connect all loads after ess connected and started
				if (this.connectLoads()) {
					this.switchState(State.CONNECTED);
				}
			} else {
				if (this.lastTimeDisconnected + this.switchDelay <= System.currentTimeMillis()) {
					if (this.activeEssId != null) {
						this.connect(activeEssId);
					} else {
						this.switchState(State.DISCONNECTING);
					}
				}
			}
		}
			break;

		case DISCONNECTED: {
			String essId = this.getEssIdWithLargestSoc();
			// only connect if soc is larger than minSoc + 5 or Ess is On-Grid
			if (essId != null) {
				SymmetricEss ess = this.parent.componentManager.getComponent(essId);
				if (ess.getSoc().value().orElse(Integer.MIN_VALUE) > MIN_SOC + 5) {
					// connect(mostLoad);
					this.activeEssId = essId;
					// TODO activeEss.start();
					this.parent.addActiveEssIdForSupplyBus(essId);
					this.lastTimeDisconnected = System.currentTimeMillis();
					this.switchState(State.CONNECTING);
				}
			} else {
				// all ess empty -> check if On-Grid
				List<String> onGridEssIds = this.getOnGridEssIds();
				if (onGridEssIds.isEmpty()) {
					this.parent.logError(this.log, "No On-Grid Ess!");
				} else {
					// connect(mostLoad);
					this.activeEssId = onGridEssIds.get(0);
					// TODO activeEss.start();
					this.parent.addActiveEssIdForSupplyBus(essId);
					this.lastTimeDisconnected = System.currentTimeMillis();
					this.switchState(State.CONNECTING);
				}
			}

			this.setSupplybusOnIndication(0);
		}
			break;

		case DISCONNECTING: {
			// if not disconnected send disconnect command again
			if (this.isDisconnected()) {
				this.switchState(State.DISCONNECTED);

			} else {
				// disconnect all loads before disconnection
				if (this.disconnectLoads()) {
					this.disconnect();
					try {
						String activeEssId = this.getActiveEssId();
						if (activeEssId != null && !activeEssId.equals(this.primaryEssId)) {
							this.parent.logInfo(this.log, "TODO Would set [" + activeEssId + "] to standby");
							// TODO active.standby();
						}
					} catch (SupplyBusException e) {
						this.parent.logError(this.log, "Get Active Ess failed: " + e.getMessage());
					}
					this.setSupplybusOnIndication(0);
				}
			}
		}
			break;

		case UNKNOWN: {
			try {
				this.activeEssId = this.getActiveEssId();
				if (this.activeEssId != null /* TODO && activeEss.getActiveSupplybus() == null */) {
					this.switchState(State.CONNECTED);
					this.parent.addActiveEssIdForSupplyBus(this.activeEssId);
					// this.activeEss.start();
				} else {
					this.switchState(State.DISCONNECTING);
				}
			} catch (SupplyBusException e) {
				this.disconnect();
			}
		}
			break;
		default:
			break;

		}
		// TODO primaryEss.start();
	}

	/**
	 * Evaluates the SwitchEssMapping IOs and gets the currently connected Ess.
	 * 
	 * @return the active Ess
	 * @throws OpenemsNamedException if not exactly one Ess is connected
	 */
	public String getActiveEssId() throws OpenemsNamedException {
		List<String> activeEssIds = new ArrayList<>();
		for (Entry<String, ChannelAddress> entry : this.switchEssMapping.entrySet()) {
			BooleanWriteChannel ioChannel = this.parent.componentManager.getChannel(entry.getValue());
			if (ioChannel.value().asOptional().orElse(false)) { // IO is active
				activeEssIds.add(entry.getKey());
			}
		}
		if (activeEssIds.size() > 1) {
			throw new SupplyBusException("There are more than one Ess connected to the supply bus.", activeEssIds,
					this);
		} else if (activeEssIds.size() == 0) {
			return null;
		} else {
			return activeEssIds.get(0);
		}
	}

	public void disconnect() throws IllegalArgumentException, OpenemsNamedException {
		for (Entry<String, ChannelAddress> entry : switchEssMapping.entrySet()) {
			BooleanWriteChannel channel = this.parent.componentManager.getChannel(entry.getValue());
			channel.setNextWriteValue(false);
		}
		if (this.activeEssId != null) {
			this.parent.removeActiveEssIdForSupplyBus(this.activeEssId);
		}
		this.activeEssId = null;
	}

	/**
	 * Is the Active-ESS actually connected?
	 * 
	 * @return true if it is connected
	 * @throws OpenemsNamedException
	 * @throws IllegalArgumentException
	 */
	public boolean isConnected() throws IllegalArgumentException, OpenemsNamedException {
		if (this.activeEssId == null) {
			return false;
		}

		BooleanWriteChannel sOn = this.parent.componentManager.getChannel(this.switchEssMapping.get(this.activeEssId));
		return sOn.value().asOptional().orElse(false);
	}

	public boolean isDisconnected() throws OpenemsNamedException {
		try {
			if (this.getActiveEssId() == null) {
				return true;
			}
		} catch (SupplyBusException e) {
			return false;
		}
		return false;
	}

	/**
	 * Actually connect this Ess.
	 * 
	 * @param essId
	 * @throws OpenemsNamedException
	 */
	public void connect(String essId) throws OpenemsNamedException {
		if (this.getActiveEssId() != null || essId == null) {
			return;
		}
		this.parent.addActiveEssIdForSupplyBus(essId);
		this.activeEssId = essId;
		BooleanWriteChannel sOn = this.parent.componentManager.getChannel(this.switchEssMapping.get(essId));
		sOn.setNextWriteValue(true);
	}

	/**
	 * Gets the Ess with the largest State of charge.
	 * 
	 * @return
	 * @throws OpenemsNamedException
	 */
	public String getEssIdWithLargestSoc() throws OpenemsNamedException {
		Integer largestSoc = null;
		String result = null;
		for (String essId : this.switchEssMapping.keySet()) {
			EssFeneconCommercial40Impl ess = this.parent.componentManager.getComponent(essId);
//			SystemState systemState = ess.getSystemState();
			SystemState systemState = ess.channel(ChannelId.SYSTEM_STATE).value().asEnum();
			Optional<Integer> socOpt = ess.getSoc().value().asOptional();
			if (socOpt.isPresent()) {
				int soc = socOpt.get();
				if (soc > MIN_SOC && systemState != SystemState.FAULT
						&& !this.parent.isActiveEssIdForSupplyBus(essId)) {
					// this ess could be used
					if (largestSoc == null || largestSoc < soc) {
						// this ess has the largest Soc
						result = essId;
						largestSoc = soc;
					}
				}
			}
		}
		return result;
	}

	/**
	 * Gets all On-Grid Ess.
	 * 
	 * @return
	 * @throws OpenemsNamedException
	 */
	private List<String> getOnGridEssIds() throws OpenemsNamedException {

		List<String> result = new ArrayList<>();
		for (String essId : this.switchEssMapping.keySet()) {
			EssFeneconCommercial40Impl ess = this.parent.componentManager.getComponent(essId);
			SystemState systemState = ess.channel(ChannelId.SYSTEM_STATE).value().asEnum();
			GridMode gridMode = ess.getGridMode().value().asEnum();

//			if (gridMode.equals(GridMode.OFF_GRID) || ess.getSystemState().equals(SystemState.FAULT)
//					|| this.activeEssId == essId) {
//				continue;
//			}
			if (gridMode.equals(GridMode.OFF_GRID) || systemState.equals(SystemState.FAULT)
					|| this.activeEssId == essId) {
				continue;
			}

			result.add(essId);
		}
		return result;
	}

	/**
	 * Disconnect all loads.
	 * 
	 * @return true if all loads are disconnected
	 * @throws OpenemsNamedException
	 */
	private boolean disconnectLoads() throws OpenemsNamedException {
		if (this.timeLoadSwitched + this.switchDelay <= System.currentTimeMillis()) {
			for (ChannelAddress load : this.loads) {
				if (this.setOutput(load, 0)) {
					this.timeLoadSwitched = System.currentTimeMillis();
					// FIXME return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Connect all loads.
	 * 
	 * @return true if all loads are connected
	 * @throws OpenemsNamedException
	 * @throws IllegalArgumentException
	 */
	private boolean connectLoads() throws IllegalArgumentException, OpenemsNamedException {
		if (this.timeLoadSwitched + this.switchDelay <= System.currentTimeMillis()) {
			for (ChannelAddress load : this.loads) {
				if (this.setOutput(load, 1)) {
					this.timeLoadSwitched = System.currentTimeMillis();
					// FIXME return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Writes the value to supplybusOnIndication channel.
	 * 
	 * @param value
	 * @throws IllegalArgumentException
	 * @throws OpenemsNamedException
	 */
	public void setSupplybusOnIndication(int value) throws IllegalArgumentException, OpenemsNamedException {
		IntegerWriteChannel supplybusOnIndication = this.parent.componentManager.getChannel(this.supplybusOnIndication);
		supplybusOnIndication.setNextWriteValue(value);
	}

	/**
	 * Helper function to switch an output if it was not switched before.
	 * 
	 * @return true if output was actually set
	 */
	private boolean setOutput(ChannelAddress channelAddress, int value)
			throws IllegalArgumentException, OpenemsNamedException {
		try {
			IntegerWriteChannel outputChannel = this.parent.componentManager.getChannel(channelAddress);
			Optional<Integer> currentValueOpt = outputChannel.value().asOptional();
			if (!currentValueOpt.isPresent() || currentValueOpt.get() != value) {
				this.parent.logInfo(this.log, "Set output [" + outputChannel.address() + "] value [" + value + "].");
				outputChannel.setNextWriteValue(value);
				return true;
			}
		} catch (OpenemsException e) {
			this.parent.logError(this.log, "Unable to set output: [" + channelAddress + "] " + e.getMessage());
		}
		return false;
	}

	private void switchState(State nextState) {
		this.parent.logInfo(this.log,
				"Bus [" + this.name + "] switching from [" + this.state + "] to [" + nextState + "]");
		this.state = nextState;
	}

}
