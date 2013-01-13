package com.jloisel.concurrent.retry;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

/**
 * Delegates retry policy decision to a {@link Predicate}.
 * 
 * @author jerome
 * 
 * @param <T> type of object as input
 */
final class RetryPolicyDecorator<T> implements RetryPolicy<T> {
	private final Predicate<T> predicate;

	/**
	 * @param predicate delegate predicate
	 * @throws NullPointerException if {@code predicate} is {@code null}
	 */
	RetryPolicyDecorator(final Predicate<T> predicate) {
		super();
		this.predicate = checkNotNull(predicate);
	}
	
	/**
	 * @return call to delegate {@link Predicate#apply(Object)}
	 */
	@Override
	public boolean apply(@Nullable final T input) {
		return predicate.apply(input);
	}

}
