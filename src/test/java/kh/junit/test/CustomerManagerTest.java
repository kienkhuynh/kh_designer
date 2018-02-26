package kh.junit.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import kh.entities.Customer;
import kh.springcontext.AppConfig;
import kh.springcontext.SecurityConfig;
import kh.web.core.CustomerManager;
import kh.web.core.JPAHelper;

/*
 * Junit test for CustomerManager
 */
public class CustomerManagerTest {
	
	/**
	 * Customer manager instance
	 */
	private CustomerManager mgr = null;
	
	/**
	 * Direct access to the database
	 */
	private JPAHelper jpaHelper;
	
	@Before
	public void runBefore() {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(AppConfig.class, SecurityConfig.class);
		rootContext.refresh();
		mgr = rootContext.getBean(CustomerManager.class);
		jpaHelper = rootContext.getBean(JPAHelper.class);
	}
	
	@Test
    public void testCustomerCreation() {
		Assert.assertNotNull("Customer Manager not created.", mgr);
		Assert.assertNotNull("JPA Helper not created.", jpaHelper);
		String email = "test@gmail.com";
		// Delete the customer
		Customer c = mgr.findByEmail(email);
		if (c != null) {
			Assert.assertTrue("Must be able to delete customer.", mgr.delete(c));
		}
				
		// Customer should be inserted
		c = new Customer();
		c.setEmail(email);
		c.setFullName("test");
		c.setPassword("test-password");
		Assert.assertTrue("User should be created.", mgr.addOrUpdate(c));
		
		c = mgr.findByEmail(email);
		// Verify that customer does exists in the database and primary key can be retrieved
		Assert.assertNotNull("User must exist", jpaHelper.find(c.getCustomerId(), Customer.class));
		
		// Customer with same email should not be inserted.
		Customer c1 = new Customer();
		c.setEmail(email);
		c.setFullName("new name");
		c.setPassword("test-password");
		Assert.assertFalse("Shouldn't be able to insert same email", mgr.addOrUpdate(c1));
	
		// Delete the customer
		Assert.assertTrue("Must be able to delete customer.", mgr.delete(c));
		
	}
	
	@After
	public void runAfter() {
		
	}
}