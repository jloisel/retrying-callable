package com.jloisel.concurrent;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.Callable;

import com.jloisel.concurrent.backoff.BackOffPolicies;
import com.jloisel.concurrent.backoff.BackOffPolicy;
import com.jloisel.concurrent.retry.RetryPolicies;
import com.jloisel.concurrent.retry.RetryPolicy;

/**
 * Builds a retrying {@link Callable} that performs retries of a 
 * delegate {@link Callable} when it returns an unexpected result.
 * 
 * Not thread-safe.
 * 
 * @author jerome
 *
 * @param <V> type of object being returned
 */
public final class RetryOnReturnCallableBuilder<V> {
	private final Callable<V> delegate;
	private RetryPolicy<V> retry = RetryPolicies.never();
	private BackOffPolicy backOff = BackOffPolicies.immediate();
	
	/**
	 * @param toRetry callable to retry
	 * @throws NullPointerException if {@code toRetry} is {@code null}
	 */
	public RetryOnReturnCallableBuilder(final Callable<V> toRetry) {
		super();
		this.delegate = checkNotNull(toRetry, "callable is NULL");
	}
	
	/**
	 * Sets a {@link RetryPolicy} checking the result of {@link Callable#call()} 
	 * returned by retried {@link Callable}.
	 * 
	 * @param retry retry policy checking returned result
	 * @return the builder itself
	 */
	public RetryOnReturnCallableBuilder<V> retry(final RetryPolicy<V> retry) {
		this.retry = checkNotNull(retry, "retry policy is NULL");
		return this;
	}
	
	/**
	 * Sets a {@link BackOffPolicy}.
	 * 
	 * @param backOff backoff policy to set
	 * @return the builder itself
	 */
	public RetryOnReturnCallableBuilder<V> backOff(final BackOffPolicy backOff) {
		this.backOff = checkNotNull(backOff, "backoff policy is NULL");
		return this;
	}
	
	/**
	 * Creates a new instance of {@link Callable} wrapping 
	 * retried {@link Callable}, performing retries depending 
	 * on returned results.
	 * 
	 * @return new {@link Callable} retrying on returned result
	 */
	public Callable<V> build() {
		return new RetryOnReturnCallable<>(delegate, retry, backOff);
	}
}
