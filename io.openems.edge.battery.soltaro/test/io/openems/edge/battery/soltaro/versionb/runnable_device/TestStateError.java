package io.openems.edge.battery.soltaro.versionb.runnable_device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.StateEnum;
import io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state.Error;
import io.openems.edge.battery.soltaro.versionb.runnable_device.helper.DeviceFactory;

public class TestStateError {

	private static final int LEVEL_2_DELAY = 1;
	private static final int START_UNSUCCESSFUL_DELAY = 2;
	private static final int MAX_START_ATTEMPTS = 2;
	private static final int SECURITY_TIME_BUFFER_MS = 10;

	private io.openems.edge.battery.soltaro.single.versionb_runnable_device.devctrl.state.Error sut;
	
	@Before
	public void setUp() throws Exception {
		sut = new Error(DeviceFactory.getErrorDevice(), MAX_START_ATTEMPTS, START_UNSUCCESSFUL_DELAY, LEVEL_2_DELAY);
	}

	@Test
	public final void testError() {
		assertNotNull(sut);
		assertNull(sut.getStateBefore());
	}

	@Test
	public final void testGetNextStateUndefined() {
		//next state is error when we are in a delay
		sut = new Error(DeviceFactory.getUndefinedDevice(), MAX_START_ATTEMPTS, START_UNSUCCESSFUL_DELAY, LEVEL_2_DELAY);
		StateEnum nextState = sut.getNextState();
		assertEquals(StateEnum.UNDEFINED, nextState);
	}
	
	@Test
	public final void testGetNextStateStopped() {
		//next state is error when we are in a delay
		sut = new Error(DeviceFactory.getStoppedDevice(), MAX_START_ATTEMPTS, START_UNSUCCESSFUL_DELAY, LEVEL_2_DELAY);
		StateEnum nextState = sut.getNextState();
		assertEquals(StateEnum.STOPPED, nextState);
	}
	
	@Test
	public final void testGetNextStateErrorMaxStartAttempts() {
		//next state is error when we are in a delay
		try {
			// 1st try to start, next state should be stopped
		sut = new Error(DeviceFactory.getStoppedDevice(), MAX_START_ATTEMPTS, START_UNSUCCESSFUL_DELAY, LEVEL_2_DELAY);
		sut.setStateBefore(StateEnum.STARTING);
		sut.act();
		StateEnum nextState = sut.getNextState();
		assertEquals(StateEnum.STOPPED, nextState);
		
		// 2nd try to start, next state should be stopped
		sut.setStateBefore(StateEnum.STARTING);
		sut.act();
		nextState = sut.getNextState();
		assertEquals(StateEnum.STOPPED, nextState);
		
		// 3rd try to start, more than MAX_START_ATTEMPTS, next state should be error for the defined time START_UNSUCCESSFUL_DELAY
		sut.setStateBefore(StateEnum.STARTING);
		sut.act();
		nextState = sut.getNextState();
		assertEquals(StateEnum.ERROR, nextState);
		
		} catch (OpenemsException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public final void testGetNextStateStoppedAfterMaxStartAttemptsWaitingPeriod() {
		//next state is error when we are in a delay
		try {
			// 1st try to start, next state should be stopped
		sut = new Error(DeviceFactory.getStoppedDevice(), MAX_START_ATTEMPTS, START_UNSUCCESSFUL_DELAY, LEVEL_2_DELAY);
		sut.setStateBefore(StateEnum.STARTING);
		sut.act();
		StateEnum nextState = sut.getNextState();
		assertEquals(StateEnum.STOPPED, nextState);
		
		// 2nd try to start, next state should be stopped
		sut.setStateBefore(StateEnum.STARTING);
		sut.act();
		nextState = sut.getNextState();
		assertEquals(StateEnum.STOPPED, nextState);
		
		// 3rd try to start, more than MAX_START_ATTEMPTS, next state should be error for the defined time START_UNSUCCESSFUL_DELAY
		sut.setStateBefore(StateEnum.STARTING);
		sut.act();
		nextState = sut.getNextState();
		assertEquals(StateEnum.ERROR, nextState);
		
		// Wait half of the period, state should still be error
		Thread.sleep(START_UNSUCCESSFUL_DELAY * 1000 / 2);
		nextState = sut.getNextState();
		assertEquals(StateEnum.ERROR, nextState);
				
		// Wait the other half of the period + buffer, state should be STOPPED
		Thread.sleep(START_UNSUCCESSFUL_DELAY * 1000 / 2 + SECURITY_TIME_BUFFER_MS);
		nextState = sut.getNextState();
		assertEquals(StateEnum.STOPPED, nextState);
		
		} catch (OpenemsException | InterruptedException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public final void testAct() {
	}

	@Test
	public final void testGetStateEnum() {
		assertEquals(StateEnum.ERROR, sut.getStateEnum());
	}

}
