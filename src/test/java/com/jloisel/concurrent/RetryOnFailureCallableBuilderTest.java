package com.jloisel.concurrent;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.junit.Test;

import com.jloisel.concurrent.backoff.BackOffPolicy;
import com.jloisel.concurrent.retry.RetryPolicies;
import com.jloisel.concurrent.retry.RetryPolicy;


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
	
	@Test(expected=NullPointerException.class)
	public void testNullPointerRetry() {
		final Callable<?> callable = mock(Callable.class);
		final RetryOnFailureCallableBuilder<?> builder = new RetryOnFailureCallableBuilder<>(callable);
		builder.retry(null);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRetry() {
		final Callable<Object> callable = mock(Callable.class);
		final RetryOnFailureCallableBuilder<Object> builder = new RetryOnFailureCallableBuilder<>(callable);
		final RetryPolicy<Exception> retry = mock(RetryPolicy.class);
		builder.retry(retry);
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullPointerBackOff() {
		final Callable<?> callable = mock(Callable.class);
		final RetryOnFailureCallableBuilder<?> builder = new RetryOnFailureCallableBuilder<>(callable);
		builder.backOff(null);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testBackOff() {
		final Callable<Object> callable = mock(Callable.class);
		final RetryOnFailureCallableBuilder<Object> builder = new RetryOnFailureCallableBuilder<>(callable);
		final BackOffPolicy backOff = mock(BackOffPolicy.class);
		builder.backOff(backOff);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCorrectnessRetry() throws Exception {
		final Callable<Object> callable = mock(Callable.class);
		doThrow(new IOException()).when(callable).call();
		
		final RetryOnFailureCallableBuilder<Object> builder = new RetryOnFailureCallableBuilder<>(callable);
		
		final RetryPolicy<Exception> retry = mock(RetryPolicy.class);
		builder.retry(retry);
		
		final Callable<Object> retrying = builder.build();
		assertEquals(RetryOnFailureCallable.class, retrying.getClass());
		
		verify(retry, never()).apply(any(Exception.class));
		verify(callable, never()).call();
		
		try {
			retrying.call();
			fail("IOException should be thrown");
		} catch(final IOException e) {
			assertNotNull(e);
		}
		
		verify(retry).apply(any(Exception.class));
		verify(callable).call();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCorrectnessBackOff() throws Exception {
		final Callable<Object> callable = mock(Callable.class);
		doThrow(new IOException()).when(callable).call();
		
		final RetryOnFailureCallableBuilder<Object> builder = new RetryOnFailureCallableBuilder<>(callable);
		
		final RetryPolicy<Exception> retry = RetryPolicies.attempts(1);
		builder.retry(retry);
		
		final BackOffPolicy backOff = mock(BackOffPolicy.class);
		builder.backOff(backOff);
		
		final Callable<Object> retrying = builder.build();
		assertEquals(RetryOnFailureCallable.class, retrying.getClass());
		
		verify(backOff, never()).apply();
		verify(callable, never()).call();
		
		try {
			retrying.call();
			fail("IOException should be thrown");
		} catch(final IOException e) {
			assertNotNull(e);
		}
		
		verify(backOff).apply();
		verify(callable, times(2)).call();
	}
}
