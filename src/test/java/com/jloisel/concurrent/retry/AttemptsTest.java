package com.jloisel.concurrent.retry;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import org.junit.Test;

/**
 * Tests {@link Attempts}.
 * 
 * @author jerome
 *
 */
public class AttemptsTest {

	@Test(expected=IllegalArgumentException.class)
	public void testIllegalArgumentException() {
		assertNotNull(new Attempts<>(-1));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIllegalArgumentException2() {
		assertNotNull(new Attempts<>(0));
	}
	
	@Test
	public void testConstructor() {
		assertNotNull(new Attempts<>(1));
	}
	
	@Test
	public void testCorrectness() {
		for(int i = 1; i < 10; i++) {
			final Attempts<?> attempts = new Attempts<>(i);
			for(int j = 0; j < i; j++) {
				assertTrue(attempts.apply(null));
			}
			assertFalse(attempts.apply(null));
		}
	}
}
