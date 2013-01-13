package com.jloisel.concurrent.retry;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.Lock;

import org.junit.Test;

import com.google.common.base.Predicate;

/**
 * Tests {@link RetryPolicies}.
 * 
 * @author jerome
 *
 */
public class RetryPoliciesTest {
	
	@Test
	public void testNeverInstance() {
		assertEquals(RetryPolicyDecorator.class, RetryPolicies.never().getClass());
	}
	
	@Test
	public void testNever() {
		assertEquals(false, RetryPolicies.never().apply(null));
		assertEquals(false, RetryPolicies.never().apply(mock(Object.class)));
	}
	
	@Test
	public void testAlwaysInstance() {
		assertEquals(RetryPolicyDecorator.class, RetryPolicies.always().getClass());
	}
	
	@Test
	public void testAlways() {
		assertEquals(true, RetryPolicies.always().apply(null));
		assertEquals(true, RetryPolicies.always().apply(mock(Object.class)));
	}
	
	@Test(expected=NullPointerException.class)
	public void testInstanceOfNullPointer() {
		assertNotNull(RetryPolicies.instanceOf(null));
	}
	
	@Test
	public void testInstanceOfInstance() {
		assertEquals(RetryPolicyDecorator.class, RetryPolicies.instanceOf(Object.class).getClass());
	}
	
	@Test
	public void testInstanceOf() {
		assertTrue(RetryPolicies.instanceOf(Collection.class).apply(mock(ArrayList.class)));
		assertFalse(RetryPolicies.instanceOf(Collection.class).apply(mock(Lock.class)));
	}
	
	@Test
	public void testIsNullInstance() {
		assertEquals(RetryPolicyDecorator.class, RetryPolicies.isNull().getClass());
	}
	
	@Test
	public void testIsNull() {
		RetryPolicy<Object> retry = RetryPolicies.isNull();
		assertFalse(retry.apply(mock(Object.class)));
		assertTrue(retry.apply(null));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testToRetryPolicyInstance() {
		assertEquals(RetryPolicyDecorator.class, RetryPolicies.toRetryPolicy(mock(Predicate.class)).getClass());
	}
}
