retrying-callable
=================

Wrap any Callable<T> to retry it with configurable retry and wait policies.

Usage:

	import static com.jloisel.concurrent.retry.RetryPolicies.*;
	import static com.jloisel.concurrent.backoff.BackOffPolicies.*;
	
	final Callable<T> callable = ...;
	final RetryOnFailureCallableBuilder<T> b = new RetryOnFailureCallableBuilder<>(callable);
	
	final Callable<T> retrying = b
		.retry(attempts(3))
		.backoff(sleep(5, TimeUnit.SECONDS))
		.build();
