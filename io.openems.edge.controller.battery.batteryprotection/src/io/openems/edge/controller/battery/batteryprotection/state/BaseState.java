package io.openems.edge.controller.battery.batteryprotection.state;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.battery.api.Battery;
import io.openems.edge.controller.battery.batteryprotection.BatteryProtectionController;
import io.openems.edge.controller.battery.batteryprotection.IState;
import io.openems.edge.ess.api.ManagedSymmetricEss;
import io.openems.edge.ess.power.api.Phase;
import io.openems.edge.ess.power.api.Pwr;

public abstract class BaseState implements IState {

	private final Logger log = LoggerFactory.getLogger(BaseState.class);
	private ManagedSymmetricEss ess;
	private Battery bms;

	public BaseState(ManagedSymmetricEss ess, Battery bms) {
		this.ess = ess;
		this.bms = bms;
	}

	protected void denyCharge() {
		Integer calculatedPower = 0;
		calculatedPower = ess.getPower().fitValueIntoMinMaxPower(BatteryProtectionController.class.getName(), ess,
				Phase.ALL, Pwr.ACTIVE, calculatedPower);
		try {
			ess.getSetActivePowerGreaterOrEquals().setNextWriteValue(calculatedPower);
		} catch (OpenemsNamedException e) {
			this.log.error(e.getMessage());
		}
	}

	protected void denyDischarge() {
		Integer calculatedPower = 0;
		calculatedPower = ess.getPower().fitValueIntoMinMaxPower(BatteryProtectionController.class.getName(), ess,
				Phase.ALL, Pwr.ACTIVE, calculatedPower);
		try {
			ess.getSetActivePowerLessOrEquals().setNextWriteValue(calculatedPower);
		} catch (OpenemsNamedException e) {
			this.log.error(e.getMessage());
		}
	}

	protected void chargeEssWithPercentOfMaxPower(int chargePowerPercent) {
		int maxCharge = ess.getPower().getMinPower(ess, Phase.ALL, Pwr.ACTIVE);
		int calculatedPower = maxCharge / 100 * chargePowerPercent;
		try {
			ess.getSetActivePowerLessOrEquals().setNextWriteValue(calculatedPower);
		} catch (OpenemsNamedException e) {
			log.error(e.getMessage());
		}
	}

	protected boolean isNextStateUndefined() {
		if (ess == null || bms == null) {
			return true;
		}

		Optional<Integer> minCellVoltageOpt = bms.getMinCellVoltage().value().asOptional();
		if (!minCellVoltageOpt.isPresent()) {
			return true;
		}

		Optional<Integer> maxCellVoltageOpt = bms.getMaxCellVoltage().value().asOptional();
		if (!maxCellVoltageOpt.isPresent()) {
			return true;
		}

		Optional<Integer> maxCellTemperatureOpt = bms.getMaxCellTemperature().value().asOptional();
		if (!maxCellTemperatureOpt.isPresent()) {
			return true;
		}

		Optional<Integer> minCellTemperatureOpt = bms.getMinCellTemperature().value().asOptional();
		if (!minCellTemperatureOpt.isPresent()) {
			return true;
		}

		Optional<Integer> socOpt = bms.getSoc().value().asOptional();
		if (!socOpt.isPresent()) {
			return true;
		}

		return false;
	}

	protected int getBmsSoC() {
		return bms.getSoc().value().get();
	}

	protected int getBmsMinCellTemperature() {
		return bms.getMinCellTemperature().value().get();
	}

	protected int getBmsMaxCellTemperature() {
		return bms.getMaxCellTemperature().value().get();
	}

	protected int getBmsMinCellVoltage() {
		return bms.getMinCellVoltage().value().get();
	}

	protected int getBmsMaxCellVoltage() {
		return bms.getMaxCellVoltage().value().get();
	}

	public ManagedSymmetricEss getEss() {
		return ess;
	}
}