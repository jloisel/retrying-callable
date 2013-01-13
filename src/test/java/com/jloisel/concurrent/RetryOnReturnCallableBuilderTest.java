package com.jloisel.concurrent;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.Callable;

import org.junit.Test;

import com.jloisel.concurrent.backoff.BackOffPolicy;
import com.jloisel.concurrent.retry.RetryPolicies;
import com.jloisel.concurrent.retry.RetryPolicy;


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
	
	@Test(expected=NullPointerException.class)
	public void testNullPointerRetry() {
		final Callable<?> callable = mock(Callable.class);
		final RetryOnReturnCallableBuilder<?> builder = new RetryOnReturnCallableBuilder<>(callable);
		builder.retry(null);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRetry() {
		final Callable<Object> callable = mock(Callable.class);
		final RetryOnReturnCallableBuilder<Object> builder = new RetryOnReturnCallableBuilder<>(callable);
		final RetryPolicy<Object> retry = mock(RetryPolicy.class);
		builder.retry(retry);
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullPointerBackOff() {
		final Callable<?> callable = mock(Callable.class);
		final RetryOnReturnCallableBuilder<?> builder = new RetryOnReturnCallableBuilder<>(callable);
		builder.backOff(null);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testBackOff() {
		final Callable<Object> callable = mock(Callable.class);
		final RetryOnReturnCallableBuilder<Object> builder = new RetryOnReturnCallableBuilder<>(callable);
		final BackOffPolicy backOff = mock(BackOffPolicy.class);
		builder.backOff(backOff);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCorrectnessRetry() throws Exception {
		final Callable<Object> callable = mock(Callable.class);
		final RetryOnReturnCallableBuilder<Object> builder = new RetryOnReturnCallableBuilder<>(callable);
		
		final RetryPolicy<Object> retry = mock(RetryPolicy.class);
		builder.retry(retry);
		
		final Callable<Object> retrying = builder.build();
		assertEquals(RetryOnReturnCallable.class, retrying.getClass());
		
		verify(retry, never()).apply(any());
		verify(callable, never()).call();
		
		retrying.call();
		
		verify(retry).apply(any());
		verify(callable).call();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCorrectnessBackOff() throws Exception {
		final Callable<Object> callable = mock(Callable.class);
		final RetryOnReturnCallableBuilder<Object> builder = new RetryOnReturnCallableBuilder<>(callable);
		
		final RetryPolicy<Object> retry = RetryPolicies.attempts(1);
		builder.retry(retry);
		
		final BackOffPolicy backOff = mock(BackOffPolicy.class);
		builder.backOff(backOff);
		
		final Callable<Object> retrying = builder.build();
		assertEquals(RetryOnReturnCallable.class, retrying.getClass());
		
		verify(backOff, never()).apply();
		verify(callable, never()).call();
		
		retrying.call();
		verify(backOff).apply();
		verify(callable, times(2)).call();
	}
}
