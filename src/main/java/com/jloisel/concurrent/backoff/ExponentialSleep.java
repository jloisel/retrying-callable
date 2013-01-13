package com.jloisel.concurrent.backoff;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

/**
 * {@link BackOffPolicy} that increases the back off sleep time 
 * between each retry attempt.
 * 
 * This implementation is not Thread-safe.
 * 
 * @author jerome
 *
 */
final class ExponentialSleep implements BackOffPolicy {
	private long timeout;
	private final long maxTimeout;
	private final TimeUnit unit;
	private @Nonnull BackOffPolicy sleep;
	
	/**
	 * @param initialTimeout initial timeout
	 * @param maxTimeout maximum timeout
	 * @param unit unit of timeout and maximum timeout
	 * @throws IllegalArgumentException if {@code maxTimeout} < {@code initialTimeout}
	 * @throws NullPointerException if {@code unit} is {@code null}
	 */
	ExponentialSleep(
			final long initialTimeout, 
			final long maxTimeout, 
			final TimeUnit unit) {
		super();
		this.timeout = initialTimeout;
		checkArgument(maxTimeout >= initialTimeout, "max timeout '%s' should be >= to initial timeout '%s'", maxTimeout, initialTimeout);
		this.maxTimeout = maxTimeout;
		this.unit = checkNotNull(unit, "unit is NULL");
		this.sleep = BackOffPolicies.sleep(timeout, unit);
	}

	/**
	 * Sleeps for a duration which increases exponentially.
	 */
	@Override
	public void apply() throws InterruptedException {
		try {
			sleep.apply();
		} finally {
			sleep = nextSleep();
		}
	}
	
	/**
	 * Increments exponentially the sleep duration.
	 * 
	 * @return incremented sleep back off policy
	 */
	private BackOffPolicy nextSleep() {
		timeout = (long) Math.min(Math.exp(timeout), maxTimeout);
		return BackOffPolicies.sleep(timeout, unit);
	}
}
