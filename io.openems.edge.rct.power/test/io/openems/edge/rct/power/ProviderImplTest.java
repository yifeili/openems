package io.openems.edge.rct.power;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.openems.edge.rct.power.RCTPowerImpl;

/*
 * Example JUNit test case
 *
 */

public class ProviderImplTest {

	/*
	 * Example test method
	 */

	@Test
	public void simple() {
		RCTPowerImpl impl = new RCTPowerImpl();
		assertNotNull(impl);
	}

}
