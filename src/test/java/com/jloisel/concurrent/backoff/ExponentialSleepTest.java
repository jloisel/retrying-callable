package com.jloisel.concurrent.backoff;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.collect.Range;

/**
 * Tests {@link ExponentialSleepTest}.
 * 
 * @author jerome
 *
 */
public class ExponentialSleepTest {

	@Test(expected=NullPointerException.class)
	public void testNullPointer() {
		assertNotNull(new ExponentialSleep(0, 0, null));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testIllegalArgument() {
		assertNotNull(new ExponentialSleep(0, -1, TimeUnit.SECONDS));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testIllegalArgument2() {
		assertNotNull(new ExponentialSleep(-1, 0, TimeUnit.SECONDS));
	}

	@Test
	public void testInterrupted() throws InterruptedException {
		final ExponentialSleep sleep = new ExponentialSleep(10, 30, TimeUnit.SECONDS);

		final CountDownLatch latch = new CountDownLatch(1);
		final Thread t = new Thread() {
			@Override
			public void run() {
				try {
					sleep.apply();
				} catch (final InterruptedException e) {
					latch.countDown();
				}
			}
		};
		t.start();
		
		t.interrupt();
		assertTrue(latch.await(1, TimeUnit.SECONDS));
	}

	@Test
	public void testCorrectness() throws InterruptedException {
		final ExponentialSleep sleep = new ExponentialSleep(800, 1000, TimeUnit.MILLISECONDS);
		assertNotNull(sleep);

		measure(sleep, 800);
	}

	@Test
	public void testCorrectness2() throws InterruptedException {
		final ExponentialSleep sleep = new ExponentialSleep(1, 10, TimeUnit.SECONDS);
		assertNotNull(sleep);

		measure(sleep, 1000);
		measure(sleep, 2000);
		measure(sleep, 7000);
		measure(sleep, 10000);
	}

	private static void measure(final BackOffPolicy policy, final long expected) throws InterruptedException {
		final long start = System.currentTimeMillis();
		policy.apply();
		final long measured = System.currentTimeMillis() - start;
		final Range<Long> range = Range.closed(expected - 100, expected + 100);
		assertTrue("measured=" + measured + ", expected=" + range, range.apply(measured));
	}
}
