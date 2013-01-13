package com.jloisel.concurrent.backoff;

import static junit.framework.TestCase.assertNotNull;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class FixedSleepTest {

	@Test(expected=NullPointerException.class)
	public void testNullPointer() {
		assertNotNull(new FixedSleep(0, null));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIllegalArgument() {
		assertNotNull(new FixedSleep(-1, TimeUnit.SECONDS));
	}
}
