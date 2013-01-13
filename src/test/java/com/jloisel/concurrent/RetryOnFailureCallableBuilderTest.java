package com.jloisel.concurrent;

import static junit.framework.TestCase.assertNotNull;

import org.junit.Test;


/**
 * Tests {@link RetryOnFailureCallableBuilder}.
 * 
 * @author jerome
 *
 */
public class RetryOnFailureCallableBuilderTest {

	@Test(expected=NullPointerException.class)
	public void testNullPointer() {
		assertNotNull(new RetryOnFailureCallableBuilder<>(null));
	}
}
