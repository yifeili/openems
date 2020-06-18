package io.openems.edge.battery.soltaro.controller.state;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.types.OptionsEnum;
import io.openems.edge.battery.soltaro.SoltaroBattery;
import io.openems.edge.battery.soltaro.controller.BatteryHandlingController;
import io.openems.edge.battery.soltaro.controller.IState;
import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.channel.value.Value;
import io.openems.edge.common.type.CircularTreeMap;
import io.openems.edge.ess.api.ManagedSymmetricEss;
import io.openems.edge.ess.power.api.Phase;
import io.openems.edge.ess.power.api.Pwr;

public abstract class BaseState implements IState {

	private final Logger log = LoggerFactory.getLogger(BaseState.class);
	private ManagedSymmetricEss ess;
	private SoltaroBattery bms;

	public BaseState(ManagedSymmetricEss ess, SoltaroBattery bms) {
		this.ess = ess;
		this.bms = bms;
	}

	protected void denyCharge() {
		Integer calculatedPower = 0;
		calculatedPower = ess.getPower().fitValueIntoMinMaxPower(BatteryHandlingController.class.getName(), ess,
				Phase.ALL, Pwr.ACTIVE, calculatedPower);
		try {
			ess.setActivePowerGreaterOrEquals(calculatedPower);
		} catch (OpenemsNamedException e) {
			this.log.error(e.getMessage());
		}
	}

	protected void denyDischarge() {
		Integer calculatedPower = 0;
		calculatedPower = ess.getPower().fitValueIntoMinMaxPower(BatteryHandlingController.class.getName(), ess,
				Phase.ALL, Pwr.ACTIVE, calculatedPower);
		try {
			ess.setActivePowerLessOrEquals(calculatedPower);
		} catch (OpenemsNamedException e) {
			this.log.error(e.getMessage());
		}
	}

	protected void chargeEssWithPercentOfMaxPower(int chargePowerPercent) {
		int maxCharge = ess.getPower().getMinPower(ess, Phase.ALL, Pwr.ACTIVE);
		int calculatedPower = maxCharge / 100 * chargePowerPercent;
		try {
			ess.setActivePowerLessOrEquals(calculatedPower);
		} catch (OpenemsNamedException e) {
			log.error(e.getMessage());
		}
	}

	protected boolean bmsNeedsFullCharge(long timeInSeconds) {
		return false;
		// Map<LocalDateTime, ?> values = getValuesInTimeSpan(timeInSeconds);

		// if (values.size() == 0) {
		// // No values present in time span
		// return false;
		// }
		// return !hasBmsBeenChargedOrDischarged(values);
	}

	@SuppressWarnings("unused")
	private Map<LocalDateTime, ?> getValuesInTimeSpan(long timeInSeconds) {
		Map<LocalDateTime, Object> values = new HashMap<>();
		Map<LocalDateTime, ?> pastValues = this.getChargeIndicationValues();

		for (LocalDateTime key : pastValues.keySet()) {
			if (key.plusSeconds(timeInSeconds).isAfter(LocalDateTime.now())) {
				// entry is in the time span
				Object o = pastValues.get(key);
				if (o != null && o instanceof Value<?>) {
					Value<?> v = (Value<?>) o;
					if (v.get() != null) {
						values.put(key, o);
					}
				}
			}
		}
		return values;
	}

	@SuppressWarnings("unused")
	private boolean hasBmsBeenChargedOrDischarged(Map<LocalDateTime, ?> values) {
		for (LocalDateTime dateTime : values.keySet()) {
			try {
				Object x = ((Value<?>) values.get(dateTime)).get();
				System.out.println("Value: " + x + " is in time: " + dateTime);
				if (x instanceof Integer) {
					if ((Integer) x > 0) {
						return true;
					}
				}
				if (x instanceof OptionsEnum) {
					if (((OptionsEnum) x).getValue() > 0) {
						return true;
					}
				}

			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		return false;
	}

	protected boolean isNextStateUndefined() {
		if (ess == null || bms == null) {
			return true;
		}

		Value<Integer> minCellVoltageOpt = bms.getMinCellVoltage();
		if (!minCellVoltageOpt.isDefined()) {
			return true;
		}

		Value<Integer> maxCellVoltageOpt = bms.getMaxCellVoltage();
		if (!maxCellVoltageOpt.isDefined()) {
			return true;
		}

		Value<Integer> maxCellTemperature = bms.getMaxCellTemperature();
		if (!maxCellTemperature.isDefined()) {
			return true;
		}

		Value<Integer> minCellTemperature = bms.getMinCellTemperature();
		if (minCellTemperature.isDefined()) {
			return true;
		}

		Value<Integer> soc = bms.getSoc();
		if (!soc.isDefined()) {
			return true;
		}

		return false;
	}

	protected int getBmsSoC() {
		return bms.getSoc().get(); // TODO this will throw a NullPointerException!
	}

	protected int getBmsMinCellTemperature() {
		return bms.getMinCellTemperature().get(); // TODO this will throw a NullPointerException!
	}

	protected int getBmsMaxCellTemperature() {
		return bms.getMaxCellTemperature().get(); // TODO this will throw a NullPointerException!
	}

	protected int getBmsMinCellVoltage() {
		return bms.getMinCellVoltage().get(); // TODO this will throw a NullPointerException!
	}

	protected int getBmsMaxCellVoltage() {
		return bms.getMaxCellVoltage().get(); // TODO this will throw a NullPointerException!
	}

	public ManagedSymmetricEss getEss() {
		return ess;
	}

	public CircularTreeMap<LocalDateTime, ?> getChargeIndicationValues() {
		CircularTreeMap<LocalDateTime, ?> pastValues = null;

		Channel<?> channel = bms.getChargeIndication();
		if (channel != null) {
			pastValues = channel.getPastValues();
		}
		return pastValues;
	}
}