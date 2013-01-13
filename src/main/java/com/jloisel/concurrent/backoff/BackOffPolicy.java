package com.jloisel.concurrent.backoff;


/**
 * Back off policy applied between each retry.
 * 
 * Back off policy should be sensitive to interruption, 
 * giving back hand if being interrupted.
 * 
 * Back off policy is not thread-safe until the implementation 
 * tells so. Therefore, sharing {@link BackOffPolicy} 
 * with multiple threads is not recommended unless you are advised 
 * to do so.
 * 
 * @author jerome
 */
public interface BackOffPolicy {
	/**
	 * Performs the back off operation.
	 * 
	 * @throws InterruptedException if interrupted while backing off
	 */
	void apply() throws InterruptedException;
}
