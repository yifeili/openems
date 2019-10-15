package io.openems.edge.battery.soltaro.versionb.runnable_device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.SoltaroComponent;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state.Running;
import io.openems.edge.battery.soltaro.versionb.runnable_device.helper.DeviceFactory;

public class TestStateRunning {

	private Running sut;
	
	@Before
	public void setUp() throws Exception {
		sut = new Running(DeviceFactory.getRunningDevice());
	}

	@Test
	public final void testRunning() {
		assertNotNull(sut);
		assertNull(sut.getStateBefore());
	}

	@Test
	public final void testGetNextStateRunning() {
		// According to the state machine if there is no error the next state is running
		assertEquals(StateEnum.RUNNING, sut.getNextState());
	}
	
	@Test
	public final void testGetNextStateError() {
		// According to the state machine if there is an error the next state is error
		sut = new Running(DeviceFactory.getErrorDevice());
		assertEquals(StateEnum.ERROR, sut.getNextState());
	}
	
	@Test
	public final void testGetNextStateUndefined() {
		// According to the state machine if device is undefined the next state is undefined
		sut = new Running(DeviceFactory.getUndefinedDevice());
		assertEquals(StateEnum.UNDEFINED, sut.getNextState());
	}

	@Test
	public final void testAct() {
		SoltaroComponent dev = DeviceFactory.getRunningDevice();
		sut = new Running(dev);		
		try {
			sut.act();
		} catch (OpenemsException e) {
			fail(e.getMessage());
		}		
	}

	@Test
	public final void testGetStateEnum() {
		assertEquals(StateEnum.RUNNING, sut.getStateEnum());
	}

}
