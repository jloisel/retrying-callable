package com.jloisel.concurrent;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.fail;
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
 * Tests {@link RetryOnReturnCallable}.
 * 
 * @author jerome
 *
 */
@SuppressWarnings("unchecked")
public class RetryOnReturnCallableTest {
	
	@Test(expected=NullPointerException.class)
	public void testNullPointerCallable() {
		assertNotNull(new RetryOnReturnCallable<>(null, mock(RetryPolicy.class), mock(BackOffPolicy.class)));
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullPointerRetry() {
		assertNotNull(new RetryOnReturnCallable<>(mock(Callable.class), null, mock(BackOffPolicy.class)));
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullPointerBackOff() {
		assertNotNull(new RetryOnReturnCallable<>(mock(Callable.class), mock(RetryPolicy.class), null));
	}

	@Test
	public void testCorrectnessRetry() throws Exception {
		final Callable<Object> callable = mock(Callable.class);
		
		final RetryPolicy<Object> retry = mock(RetryPolicy.class);
		final BackOffPolicy backOff = mock(BackOffPolicy.class);
		
		final Callable<Object> retrying = new RetryOnReturnCallable<>(callable, retry, backOff);
		
		verify(retry, never()).apply(any(Exception.class));
		verify(callable, never()).call();
		
		retrying.call();
		
		verify(retry).apply(any(Exception.class));
		verify(callable).call();
	}
	
	@Test
	public void testCorrectnessBackOff() throws Exception {
		final Callable<Object> callable = mock(Callable.class);
		
		final RetryPolicy<Object> retry = RetryPolicies.attempts(1);
		final BackOffPolicy backOff = mock(BackOffPolicy.class);
		
		final Callable<Object> retrying = new RetryOnReturnCallable<>(callable, retry, backOff);
		
		verify(backOff, never()).apply();
		verify(callable, never()).call();
		
		retrying.call();
		
		verify(backOff).apply();
		verify(callable, times(2)).call();
	}
	
	@Test
	public void testInterruption() throws Exception {
		final Callable<Object> callable = mock(Callable.class);
		final RetryPolicy<Object> retry = mock(RetryPolicy.class);
		final BackOffPolicy backOff = mock(BackOffPolicy.class);
		
		final Callable<Object> retrying = new RetryOnReturnCallable<>(callable, retry, backOff);
		
		final Thread t = new Thread() {
			@Override
			public void run() {
				try {
					Thread.currentThread().interrupt();
					retrying.call();
					fail("Interrupted exception should be thrown");
				} catch(final InterruptedException e) {
					assertNotNull(e);
				}
				catch (final Exception e) {
					fail(e.getMessage());
				}
			}
		};
		
		t.start();
		t.join();
	}
}
