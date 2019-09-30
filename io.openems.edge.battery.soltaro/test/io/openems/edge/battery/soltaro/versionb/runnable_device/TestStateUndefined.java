package io.openems.edge.battery.soltaro.versionb.runnable_device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state.Undefined;
import io.openems.edge.battery.soltaro.versionb.runnable_device.helper.DeviceFactory;

public class TestStateUndefined {

	private Undefined sut;

	@Before
	public void setUp() throws Exception {
		sut = new Undefined(DeviceFactory.getRunningDevice());
	}

	@Test
	public final void testUndefined() {
		assertNotNull(sut);
		assertNull(sut.getStateBefore());
	}

	@Test
	public final void testGetNextStateRunning() {
		assertEquals(StateEnum.RUNNING, sut.getNextState());
	}

	@Test
	public final void testGetNextStateStopped() {
		sut = new Undefined(DeviceFactory.getStoppedDevice());
		assertEquals(StateEnum.STOPPED, sut.getNextState());
	}

	@Test
	public final void testGetNextStateError() {
		sut = new Undefined(DeviceFactory.getErrorDevice());
		assertEquals(StateEnum.ERROR, sut.getNextState());
	}

	@Test
	public final void testGetNextStateUndefined() {
		sut = new Undefined(DeviceFactory.getUndefinedDevice());
		assertEquals(StateEnum.UNDEFINED, sut.getNextState());
	}

	@Test
	public final void testAct() {
		try {
			sut.act();
		} catch (Exception e) {
			fail("No exception should happen because in undefined there is nothing to do");
		}

	}

	@Test
	public final void testGetStateEnum() {
		assertEquals(StateEnum.UNDEFINED, sut.getStateEnum());
	}

}
