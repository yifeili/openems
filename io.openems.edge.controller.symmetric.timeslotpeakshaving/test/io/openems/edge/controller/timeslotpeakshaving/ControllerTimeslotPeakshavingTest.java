package io.openems.edge.controller.timeslotpeakshaving;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

import io.openems.common.types.ChannelAddress;
import io.openems.edge.common.sum.GridMode;
import io.openems.edge.common.test.AbstractComponentConfig;
import io.openems.edge.common.test.AbstractComponentTest.TestCase;
import io.openems.edge.common.test.DummyComponentManager;
import io.openems.edge.common.test.TimeLeapClock;
import io.openems.edge.controller.test.ControllerTest;
import io.openems.edge.ess.api.ManagedSymmetricEss;
import io.openems.edge.ess.test.DummyManagedSymmetricEss;
import io.openems.edge.meter.api.SymmetricMeter;
import io.openems.edge.meter.test.DummySymmetricMeter;

public class ControllerTimeslotPeakshavingTest {

	@SuppressWarnings("all")
	private static class MyConfig extends AbstractComponentConfig implements Config {

		private final String essId;
		private final String meterId;
		private final int peakShavingPower;
		private final int rechargePower;
		private final int chargePower;
		private final String startDate;
		private final String endDate;
		private final String startTime;
		private final String endTime;
		private final String slowStartTime;
		private final int hysteresisSoc;

		private final boolean monday;
		private final boolean tuesday;
		private final boolean wednesday;
		private final boolean thursday;
		private final boolean friday;
		private final boolean saturday;
		private final boolean sunday;

		public MyConfig(String id, String essId, String meterId, int peakShavingPower, int rechargePower,
				int chargePower, String startDate, String endDate, String startTime, String endTime,
				String slowStartTime, int hysteresisSoc, boolean monday, boolean tuesday, boolean wednesday,
				boolean thursday, boolean friday, boolean saturday, boolean sunday) {
			super(Config.class, id);
			this.essId = essId;
			this.meterId = meterId;
			this.peakShavingPower = peakShavingPower;
			this.rechargePower = rechargePower;
			this.chargePower = chargePower;
			this.startDate = startDate;
			this.endDate = endDate;
			this.startTime = startTime;
			this.slowStartTime = slowStartTime;
			this.endTime = endTime;
			this.hysteresisSoc = hysteresisSoc;

			this.monday = monday;
			this.tuesday = tuesday;
			this.wednesday = wednesday;
			this.thursday = thursday;
			this.friday = friday;
			this.saturday = saturday;
			this.sunday = sunday;

		}

		@Override
		public String ess() {
			return this.essId;
		}

		@Override
		public String meter_id() {
			return this.meterId;
		}

		@Override
		public String startDate() {
			return this.startDate;
		}

		@Override
		public String endDate() {
			return this.endDate;
		}

		@Override
		public String startTime() {
			return this.startTime;
		}

		@Override
		public String endTime() {
			return this.endTime;
		}

		@Override
		public int peakShavingPower() {
			return this.peakShavingPower;
		}

		@Override
		public int rechargePower() {
			return this.rechargePower;
		}

		@Override
		public int hysteresisSoc() {
			return this.hysteresisSoc;
		}

		@Override
		public int slowChargePower() {
			return this.chargePower;
		}

		@Override
		public String slowChargeStartTime() {
			return this.slowStartTime;
		}

		@Override
		public boolean monday() {
			return this.monday;
		}

		@Override
		public boolean tuesday() {
			return this.tuesday;
		}

		@Override
		public boolean wednesday() {
			return this.wednesday;
		}

		@Override
		public boolean thursday() {
			return this.thursday;
		}

		@Override
		public boolean friday() {
			return this.friday;
		}

		@Override
		public boolean saturday() {
			return this.saturday;
		}

		@Override
		public boolean sunday() {
			return this.sunday;
		}
	}

	@Test
	public void test() throws Exception {
		Instant inst = Instant.parse("2020-02-03T08:30:00.00Z");
		TimeLeapClock clock = new TimeLeapClock(inst, ZoneOffset.UTC);

		// initialize the controller
		TimeslotPeakshaving ctrl = new TimeslotPeakshaving(clock);
		// Add referenced services
		DummyComponentManager componentManager = new DummyComponentManager();
		ctrl.componentManager = componentManager;

		MyConfig config = new MyConfig("ctrl0", "ess0", "meter0", 100000, 50000, 50000, "01.01.2020", "30.04.2020",
				"10:00", "11:00", "09:00", 95, true, true, true, true, true, true, true);

		ctrl.activate(null, config);
		ctrl.activate(null, config);

		ChannelAddress gridMode = new ChannelAddress("ess0", "GridMode");
		ChannelAddress ess = new ChannelAddress("ess0", "ActivePower");
		ChannelAddress grid = new ChannelAddress("meter0", "ActivePower");
		ChannelAddress soc = new ChannelAddress("ess0", "Soc");
		ChannelAddress essSetPower = new ChannelAddress("ess0", "SetActivePowerEquals");

		ManagedSymmetricEss ess0 = new DummyManagedSymmetricEss("ess0");
		SymmetricMeter meter = new DummySymmetricMeter("meter0");

		// Current time is --> 8:30
		new ControllerTest(ctrl, componentManager, ess0, meter).next(new TestCase() //
				.input(gridMode, GridMode.ON_GRID) // This test case should run in normal state machine
				.input(ess, 0) //
				.input(soc, 90).input(grid, 120000) //
				.output(essSetPower, null)) //
				.next(new TestCase() //
						.timeleap(clock, 31, ChronoUnit.MINUTES)/* current time is 09:31, run in slow charge state */
						.input(soc, 98).input(gridMode, GridMode.ON_GRID) //
						.input(ess, 5000) //
						.input(grid, 120000) //
						.output(essSetPower, 13500)) //
				.next(new TestCase() //
						.timeleap(clock, 31, ChronoUnit.MINUTES)/* current time is 09:31, run in hysterisis state */
						.input(soc, 100).input(gridMode, GridMode.ON_GRID) //
						.input(ess, 5000) //
						.input(grid, 120000)) // nothing set on, Ess's setActivePower
				.next(new TestCase() //
						.input(gridMode, GridMode.ON_GRID) /* current time is 09:31, run in slow charge state */
						.input(soc, 94).input(ess, 5000) //
						.input(grid, 120000) //
						.output(essSetPower, 27000)) //
				.next(new TestCase() //
						.timeleap(clock, 75, ChronoUnit.MINUTES)/* current time is 10:47, run in high threshold state */
						.input(gridMode, GridMode.ON_GRID) //
						.input(ess, 5000) //
						.input(grid, 120000) //
						.output(essSetPower, 33000)) //
				.next(new TestCase() //
						.timeleap(clock, 75, ChronoUnit.MINUTES)/* current time is 12:02 run in normal state */
						.input(gridMode, GridMode.ON_GRID) //
						.input(ess, 5000) //
						.input(grid, 120000)) // nothing set on, Ess's setActivePower
				.run();
	}
}
