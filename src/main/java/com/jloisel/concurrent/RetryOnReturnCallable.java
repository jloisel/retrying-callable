package com.jloisel.concurrent;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.Callable;

import com.jloisel.concurrent.backoff.BackOffPolicy;
import com.jloisel.concurrent.retry.RetryPolicy;


/**
 * Retries a {@link Callable} by checking the value returned 
 * by the delegate callable when calling {@link Callable#call()}.
 * 
 * This implementation is not Thread-safe.
 * 
 * @author jerome
 *
 * @param <V> type of object being returned as result
 */
final class RetryOnReturnCallable<V> implements Callable<V> {
	private final Callable<V> toRetry;
	private final RetryPolicy<V> retry;
	private final BackOffPolicy backOff;

	/**
	 * @param toRetry delegate callable to retry
	 * @param retry retry policy
	 * @param backOff back off policy
	 * @throws NullPointerException if {@code toRetry}, {@code retry} or 
	 *         {@code backOff} is {@code null}
	 */
	RetryOnReturnCallable(
			final Callable<V> toRetry,
			final RetryPolicy<V> retry, 
			final BackOffPolicy backOff) {
		super();
		this.toRetry = checkNotNull(toRetry, "callable is NULL");
		this.retry = checkNotNull(retry, "retry policy is NULL");
		this.backOff = checkNotNull(backOff, "back off policy is NULL");
	}

	/**
	 * Calls the delegate {@link Callable} and performs retry 
	 * when it returns depending on retry and back off policies.
	 * 
	 * It polls regularly the {@link Thread} interrupted state 
	 * and throws an {@link InterruptedException} if the current 
	 * {@link Thread} has been interrupted.
	 * 
	 * Any {@link Exception} thrown by the delegate {@link Callable}, 
	 * {@link RetryPolicy} or {@link BackOffPolicy} is propagated to 
	 * the caller.
	 * 
	 * @return the value returned by the delegate {@link Callable} when successful
	 */
	@Override
	public V call() throws Exception {
		while(!Thread.currentThread().isInterrupted()) {
			final V value = toRetry.call();
			if(retry.apply(value)) {
				backOff.apply();
				continue;
			}
			return value;
		}
		throw new InterruptedException();
	}
}