package io.openems.edge.battery.soltaro.versionb.runnable_device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state.Starting;
import io.openems.edge.battery.soltaro.versionb.runnable_device.helper.DeviceFactory;

public class TestStateStarting {

	private static final int MAX_TIME_TO_START = 1;

	@Test
	public final void testStarting() {
		Starting sut = new Starting(DeviceFactory.getStoppedDevice(), MAX_TIME_TO_START);
		assertNotNull(sut);
		assertNull(sut.getStateBefore());
	}

	@Test
	public final void testGetNextStateStarting() {
		Starting sut = new Starting(DeviceFactory.getStoppedDevice(), MAX_TIME_TO_START);
		// According to the state machine the next state is starting when device is not
		// running
		assertEquals(StateEnum.STARTING, sut.getNextState());
		try {
			sut.act();
		} catch (OpenemsException e) {
			fail("No exception should happen because in undefined there is nothing to do");
		}
		assertEquals(StateEnum.STARTING, sut.getNextState());
	}

	@Test
	public final void testGetNextStateRunning() {
		// According to the state machine the next state is RUNNING when device is
		// running
		Starting sut = new Starting(DeviceFactory.getRunningDevice(), MAX_TIME_TO_START);
		assertEquals(StateEnum.RUNNING, sut.getNextState());
	}

	@Test
	public final void testGetNextStateErrorTimeWasTooLong() {
		// According to the state machine the next state is error when time to
		// start was too long
		Starting sut = new Starting(DeviceFactory.getStoppedDevice(), MAX_TIME_TO_START);
		try {
			sut.act();
			assertEquals(StateEnum.STARTING, sut.getNextState());
			Thread.sleep(MAX_TIME_TO_START * 1000 / 2);
			// State should still be starting
			assertEquals(StateEnum.STARTING, sut.getNextState());
			Thread.sleep(MAX_TIME_TO_START * 1000 / 2 + MAX_TIME_TO_START * 1);
			assertEquals(StateEnum.ERROR, sut.getNextState());

		} catch (InterruptedException | OpenemsException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public final void testGetNextStateError() {
		// According to the state machine the next state is error when device has an
		// error
		Starting sut = new Starting(DeviceFactory.getErrorDevice(), MAX_TIME_TO_START);
		assertEquals(StateEnum.ERROR, sut.getNextState());
	}

	@Test
	public final void testAct() {
		try {
			Starting sut = new Starting(DeviceFactory.getStoppedDevice(), MAX_TIME_TO_START);
			sut.act();
		} catch (Exception e) {
			fail("No exception should happen because in undefined there is nothing to do");
		}
	}

	@Test
	public final void testGetStateEnum() {
		Starting sut = new Starting(null, MAX_TIME_TO_START);
		assertEquals(StateEnum.STARTING, sut.getStateEnum());
	}

}
