package com.jloisel.concurrent.retry;

import com.google.common.base.Predicate;

/**
 * Determines whenever retry should be performed based on a input.
 * Implement this interface to create your own retry policy.
 * 
 * Retry policy may only throw unchecked exceptions. Any exception thrown 
 * is propagated to the caller of the retrying callable.
 * 
 * Retry policy is not thread-safe until the implementation 
 * tells so. Therefore, sharing {@link RetryPolicy} 
 * with multiple threads is not recommended unless your 
 * are advised to do so.
 * 
 * @author jerome
 * 
 * @param <T> type of input being tested
 */
public interface RetryPolicy<T> extends Predicate<T> {

}
