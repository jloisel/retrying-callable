package com.jloisel.concurrent;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.Callable;

import com.jloisel.concurrent.backoff.BackOffPolicies;
import com.jloisel.concurrent.backoff.BackOffPolicy;
import com.jloisel.concurrent.retry.RetryPolicies;
import com.jloisel.concurrent.retry.RetryPolicy;

/**
 * Builds a retrying {@link Callable} that performs retries of a 
 * delegate {@link Callable} when it fails.
 * 
 * @author jerome
 *
 * @param <V> type of object being returned
 */
public final class RetryOnFailureCallableBuilder<V> {
	private final Callable<V> delegate;
	private RetryPolicy<Exception> retry = RetryPolicies.never();
	private BackOffPolicy backOff = BackOffPolicies.immediate();
	
	/**
	 * @param toRetry callable to retry
	 * @throws NullPointerException if {@code toRetry} is {@code null}
	 */
	public RetryOnFailureCallableBuilder(final Callable<V> toRetry) {
		super();
		this.delegate = checkNotNull(toRetry, "callable is NULL");
	}
	
	/**
	 * Sets the retry on exception policy.
	 * This policy is applied when an exception is thrown by the 
	 * wrapped {@link Callable}.
	 * 
	 * @param retry retry policy on exception
	 * @return the builder itself
	 */
	public RetryOnFailureCallableBuilder<V> retry(final RetryPolicy<Exception> retry) {
		this.retry = checkNotNull(retry, "retry policy is NULL");
		return this;
	}
	
	/**
	 * Sets the backoff policy.
	 * 
	 * @param backOff back off policy
	 * @return the builder itself
	 */
	public RetryOnFailureCallableBuilder<V> backOff(final BackOffPolicy backOff) {
		this.backOff = checkNotNull(backOff, "backoff policy is NULL");
		return this;
	}
	
	/**
	 * Builds a new {@link Callable} wrapping the {@link Callable} to 
	 * retry, performing retries when an exception is thrown.
	 * @return new {@link Callable} retrying on exception
	 */
	public Callable<V> build() {
		return new RetryOnFailureCallable<>(delegate, retry, backOff);
	}
}
