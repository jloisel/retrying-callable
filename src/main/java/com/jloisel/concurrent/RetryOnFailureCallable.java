package com.jloisel.concurrent;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.Callable;

import com.jloisel.concurrent.backoff.BackOffPolicy;
import com.jloisel.concurrent.retry.RetryPolicy;


/**
 * Retries a delegate {@link Callable} when an exception 
 * is thrown.
 * 
 * This implementation is not Thread-safe.
 * 
 * @author jerome
 *
 * @param <V> type of object being returned as result
 */
final class RetryOnFailureCallable<V> implements Callable<V> {
	private final Callable<V> toRetry;
	private final RetryPolicy<Exception> retry;
	private final BackOffPolicy backOff;

	/**
	 * @param toRetry delegate callable to retry
	 * @param retry retry policy
	 * @param backOff back off policy
	 * @throws NullPointerException if {@code toRetry}, {@code retry} or 
	 *         {@code backOff} is NULL
	 */
	RetryOnFailureCallable(
			final Callable<V> toRetry,
			final RetryPolicy<Exception> retry, 
			final BackOffPolicy backOff) {
		super();
		this.toRetry = checkNotNull(toRetry, "callable is NULL");
		this.retry = checkNotNull(retry, "retry policy is NULL");
		this.backOff = checkNotNull(backOff, "back off policy is NULL");
	}

	/**
	 * Calls the delegate {@link Callable} and performs retry 
	 * when it fails depending on retry and back off policies.
	 * 
	 * It polls regularly the {@link Thread} interrupted state 
	 * and throws an {@link InterruptedException} if the current 
	 * {@link Thread} has been interrupted.
	 * 
	 * Any {@link Exception} thrown by the {@link RetryPolicy} or 
	 * the {@link BackOffPolicy} is propagated to the caller.
	 */
	@Override
	public V call() throws Exception {
		while(!Thread.currentThread().isInterrupted()) {
			try {
				return toRetry.call();
			} catch(final Exception e) {
				if(retry.apply(e)) {
					backOff.apply();
					continue;
				}
				throw e;
			}
		}
		throw new InterruptedException();
	}

}
