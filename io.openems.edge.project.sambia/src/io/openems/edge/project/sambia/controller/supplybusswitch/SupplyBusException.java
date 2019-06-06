package io.openems.edge.project.sambia.controller.supplybusswitch;

import java.util.List;

import io.openems.common.exceptions.OpenemsException;

public class SupplyBusException extends OpenemsException {

	private static final long serialVersionUID = -3868360492918860902L;

	public final List<String> activeEssIds;
	public final SupplyBus supplybus;

	public SupplyBusException(String string, List<String> activeEssIds, SupplyBus supplybus) {
		super(string);
		this.activeEssIds = activeEssIds;
		this.supplybus = supplybus;
	}

}
