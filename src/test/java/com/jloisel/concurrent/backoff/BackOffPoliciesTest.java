package com.jloisel.concurrent.backoff;

import static com.jloisel.unittest.UnitTests.assertNotInstantiable;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * Tests {@link BackOffPolicies}.
 * 
 * @author jerome
 *
 */
public class BackOffPoliciesTest {

	@Test
	public void testNotInstantiable() {
		assertNotInstantiable(BackOffPolicies.class);
	}
	
	@Test
	public void testImmediate() {
		assertEquals(Immediate.INSTANCE, BackOffPolicies.immediate());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSleepIllegalArgument() {
		assertNotNull(BackOffPolicies.sleep(-1, TimeUnit.SECONDS));
	}
	
	@Test(expected=NullPointerException.class)
	public void testSleepNullPointer() {
		assertNotNull(BackOffPolicies.sleep(1, null));
	}
	
	@Test
	public void testSleep() {
		assertEquals(FixedSleep.class, BackOffPolicies.sleep(1, TimeUnit.SECONDS).getClass());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testExpSleepIllegalArgument() {
		assertNotNull(BackOffPolicies.exponentialSleep(-1, 1, TimeUnit.SECONDS));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testExpSleepIllegalArgument2() {
		assertNotNull(BackOffPolicies.exponentialSleep(1, 0, TimeUnit.SECONDS));
	}
	
	@Test(expected=NullPointerException.class)
	public void testExpSleepNullPointer() {
		assertNotNull(BackOffPolicies.exponentialSleep(1, 1, null));
	}
	
	@Test
	public void testExpSleep() {
		assertEquals(ExponentialSleep.class, BackOffPolicies.exponentialSleep(1, 1, TimeUnit.SECONDS).getClass());
	}
}
