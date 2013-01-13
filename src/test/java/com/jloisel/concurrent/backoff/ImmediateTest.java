package com.jloisel.concurrent.backoff;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

import org.junit.Test;

/**
 * Tests {@link Immediate}.
 * 
 * @author jerome
 *
 */
public class ImmediateTest {

	@Test
	public void testInstance() {
		assertNotNull(Immediate.INSTANCE);
		assertEquals(Immediate.class, Immediate.INSTANCE.getClass());
	}
	
	@Test
	public void testApplyNotThrowing() throws InterruptedException {
		Immediate.INSTANCE.apply();
	}
}
