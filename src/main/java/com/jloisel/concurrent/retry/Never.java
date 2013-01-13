package com.jloisel.concurrent.retry;

import javax.annotation.Nullable;

/**
 * Never retry a task.
 * 
 * @author jerome
 * 
 * @param <T> type of object as input
 */
final class Never<T> implements RetryPolicy<T> {
	/**
	 * Singleton instance as this object is stateless.
	 */
	static final RetryPolicy<Object> INSTANCE = new Never<>();

	/**
	 * Not intended to be instantiated, use 
	 * {@link Never#INSTANCE} instead.
	 */
	private Never() {
		super();
	}

	/**
	 * @return Always {@code false}
	 */
	@Override
	public boolean apply(@Nullable final T input) {
		return false;
	}
}
