package io.openems.edge.battery.soltaro.versionb.runnable_device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state.Stopped;
import io.openems.edge.battery.soltaro.versionb.runnable_device.helper.CommandDevice;
import io.openems.edge.battery.soltaro.versionb.runnable_device.helper.DeviceFactory;

public class TestStateStopped {

	private Stopped sut;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		sut = new Stopped(DeviceFactory.getStoppedDevice());
	}

	@Test
	public final void testStopped() {
		assertNotNull(sut);
		assertNull(sut.getStateBefore());
	}

	@Test
	public final void testGetNextStateStarting() {
		// According to the state machine the next state is always starting
		assertEquals(StateEnum.STARTING, sut.getNextState());
	}

	@Test
	public final void testAct() {
	//Test should assert that start command is given
		CommandDevice dev = DeviceFactory.getCommandDevice();
		sut = new Stopped(dev);
		assertFalse(dev.isStartCommand());
		try {
			sut.act();
		} catch (OpenemsException e) {
			fail(e.getMessage());
		}
		assertTrue(dev.isStartCommand());
	}

	@Test
	public final void testGetStateEnum() {
		assertEquals(StateEnum.STOPPED, sut.getStateEnum());
	}

}
