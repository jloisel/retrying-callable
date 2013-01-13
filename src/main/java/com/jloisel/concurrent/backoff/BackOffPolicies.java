package com.jloisel.concurrent.backoff;

import java.util.concurrent.TimeUnit;


/**
 * Utility Static factory pertaining to the 
 * {@link BackOffPolicy} interface.
 * 
 * All existing {@link BackOffPolicy} implementations are 
 * available here.
 * 
 * To create your own back off policy, implement {@link BackOffPolicy}.
 * 
 * @author jerome
 *
 */
public final class BackOffPolicies {
	/**
	 * Utility classes are not intended to be instantiated.
	 */
	private BackOffPolicies() {
		throw new IllegalAccessError();
	}
	
	/**
	 * @return {@link BackOffPolicy} implementation that does nothing.
	 */
	public static BackOffPolicy immediate() {
		return Immediate.INSTANCE;
	}
	
	/**
	 * Sleeps for a fixed duration.
	 * 
	 * @param timeout minimum timeout to sleep
	 * @param unit timeout unit
	 * @throws NullPointerException if {@code unit} is {@code null}
	 * @throws IllegalArgumentException if {@code timeout} is < 0
	 */
	public static BackOffPolicy sleep(final long timeout, final TimeUnit unit) {
		return new FixedSleep(timeout, unit);
	}
	
	/**
	 * Sleeps for a duration which increases exponentially between each 
	 * retry attempt.
	 * 
	 * @param initialTimeout initial timeout
	 * @param maxTimeout maximum timeout not to exceed
	 * @param unit initial and maximum timeout unit
	 * @return back off policy
	 * @throws NullPointerException if {@code unnit} is {@code null}
	 * @throws IllegalArgumentException if {@code initialTimeout} is < 0, 
	 *         or {@code maxTimeout} < {@code initialTimeout}
	 */
	public static BackOffPolicy exponentialSleep(
			final long initialTimeout, 
			final long maxTimeout, 
			final TimeUnit unit) {
		return new ExponentialSleep(initialTimeout, maxTimeout, unit);
	}
}
