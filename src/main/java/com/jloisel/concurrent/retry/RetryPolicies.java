package com.jloisel.concurrent.retry;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

/**
 * Utility static factory pertaining to the 
 * {@link RetryPolicy} interface.
 * 
 * All existing {@link RetryPolicy} implementations are 
 * available here.
 * 
 * To create your own retry policy, implement {@link RetryPolicy}.
 * 
 * @author jerome
 *
 */
public final class RetryPolicies {
	/**
	 * Utility classes are not intended to be instantiated.
	 */
	private RetryPolicies() {
		throw new IllegalAccessError();
	}

	/**
	 * Returns a {@link RetryPolicy} that never performs retries.
	 * 
	 * @return retry policy that never accepts any retry
	 */
	public static <T> RetryPolicy<T> never() {
		final Predicate<T> alwaysFalse = Predicates.alwaysFalse();
		return toRetryPolicy(alwaysFalse);
	}
	
	/**
	 * Returns a {@link RetryPolicy} that always performs retries.
	 * 
	 * @return retry policy that always accepts retries
	 */
	public static <T> RetryPolicy<T> always() {
		final Predicate<T> alwaysTrue = Predicates.alwaysTrue();
		return toRetryPolicy(alwaysTrue);
	}
	
	/**
	 * Returns a {@link RetryPolicy} that evaluates to true if the object being tested is 
	 * an instance of the given class. If the object being tested is null 
	 * this {@link RetryPolicy} evaluates to false.
	 * 
	 * @param clazz class to be instance of
	 * @return retry policy checking input class
	 * @throws NullPointerException if {@code clazz} is {@code null}
	 */
	public static RetryPolicy<Object> instanceOf(final Class<?> clazz) {
		final Predicate<Object> instanceOf = Predicates.instanceOf(clazz);
		return toRetryPolicy(instanceOf);
	}
	
	/**
	 * Returns a {@link RetryPolicy} that evaluates to true if the object being 
	 * tested is {@code null}.
	 * 
	 * @return retry policy returning {@code true} when input is {@code null}
	 */
	public static <T> RetryPolicy<T> isNull() {
		final Predicate<T> isNull = Predicates.isNull();
		return toRetryPolicy(isNull);
	}

	/**
	 * Decorates a {@link Predicate} as {@link RetryPolicy} by 
	 * delegating the retry decision.
	 * 
	 * @param predicate delegate predicate
	 * @return retry policy decorating the {@code predicate}
	 * @throws NullPointerException if {@code predicate} is {@code null}
	 */
	public static <T> RetryPolicy<T> toRetryPolicy(final Predicate<T> predicate) {
		return new RetryPolicyDecorator<>(predicate);
	}
}
