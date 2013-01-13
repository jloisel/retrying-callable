package com.jloisel.concurrent.backoff;

/**
 * Performs no back off. Retries in a given set proceed 
 * one after the other.
 * 
 * Thread-safe. Can be freely share by multiple Threads.
 * 
 * @author jerome
 *
 */
final class Immediate implements BackOffPolicy {
	/**
	 * Singleton instance as this object is stateless.
	 */
	static final BackOffPolicy INSTANCE = new Immediate();
	
	/**
	 * Use {@link Immediate#INSTANCE}.
	 */
	private Immediate() {
		super();
	}
	
	/**
	 * No operation: returns immediately.
	 */
	@Override
	public void apply() throws InterruptedException {
		
	}
}
