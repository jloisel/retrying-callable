package com.jloisel.concurrent;

import static junit.framework.TestCase.assertNotNull;

import org.junit.Test;


/**
 * Tests {@link RetryOnReturnCallableBuilder}.
 * 
 * @author jerome
 *
 */
public class RetryOnReturnCallableBuilderTest {

	@Test(expected=NullPointerException.class)
	public void testNullPointer() {
		assertNotNull(new RetryOnReturnCallableBuilder<>(null));
	}
}
