package com.jloisel.concurrent.retry;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nullable;

/**
 * Retry a task a given number of attempts.
 * 
 * Although this implementation is Thread-safe, it's not 
 * intended to be used by multiple threads.
 * Sharing this instance among multiple threads will result in 
 * unexpected behavior.
 * 
 * @author jerome
 *
 * @param <T> type of object as input
 */
final class Attempts<T> implements RetryPolicy<T> {
	private final AtomicInteger attempts;
	
	/**
	 * @param attempts number of attempts
	 * @throws IllegalArgumentException if {@code attempts} is <= 0
	 */
	public Attempts(final int attempts) {
		super();
		checkArgument(attempts > 0, "attempts must be > 0, invalid: %s",attempts);
		this.attempts = new AtomicInteger(attempts);
	}
	
	/**
	 * @return {@code true} until attempts reach zero
	 */
	@Override
	public boolean apply(@Nullable T input) {
		return attempts.getAndDecrement() > 0;
	}
}
