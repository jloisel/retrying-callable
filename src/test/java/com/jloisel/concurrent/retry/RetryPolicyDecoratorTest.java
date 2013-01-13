package com.jloisel.concurrent.retry;

import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.google.common.base.Predicate;

/**
 * Tests {@link RetryPolicyDecorator}.
 * 
 * @author jerome
 *
 */
public class RetryPolicyDecoratorTest {

	@Test(expected=NullPointerException.class)
	public void testNullPointer() {
		assertNotNull(new RetryPolicyDecorator<>(null));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testDelegate() {
		final Predicate<Object> predicate = mock(Predicate.class);
		final RetryPolicy<Object> retry = new RetryPolicyDecorator<>(predicate);
		
		verify(predicate, never()).apply(any());
		
		retry.apply(mock(Object.class));
		
		verify(predicate).apply(any());
	}
}
