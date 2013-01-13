package com.jloisel.concurrent.backoff;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.TimeUnit;

/**
 * Sleeps for a fixed duration on each {@link BackOffPolicy#apply()} 
 * call.
 * 
 * In case of interruption while sleeping, an {@link InterruptedException} is 
 * thrown.
 * 
 * Thread-safe.
 * 
 * @author jerome
 *
 */
final class FixedSleep implements BackOffPolicy {
	private final long timeout;
	private final TimeUnit unit;
	
	/**
	 * @param timeout the minimum time to sleep, if less or equal to zero, no sleep
	 * @param unit unit of the timeout
	 * @throws NullPointerException if {@code unit} is {@code null}
	 * @throws IllegalArgumentException if {@code timeout} is < 0
	 */
	FixedSleep(final long timeout, final TimeUnit unit) {
		super();
		checkArgument(timeout >= 0, "timeout must be >= 0, invalid: %s", timeout);
		this.timeout = timeout;
		this.unit = checkNotNull(unit, "unit is NULL");
	}
	
	/**
	 * Sleeps for a fixed duration.
	 * @throws InterruptedException if interrupted while sleeping
	 */
	@Override
	public void apply() throws InterruptedException {
		unit.sleep(timeout);
	}
}
