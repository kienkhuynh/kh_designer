package kh.web.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import kh.entities.Customer;

@Component
public class CustomerManager extends AbstractManager<Customer> {

	Logger log = Logger.getLogger(CustomerManager.class);
	
	public CustomerManager() {
		super(Customer.class);
		log.info(CustomerManager.class + " initialized.");
	}

	@Autowired JPAHelper jpaHelper;
	
	@Override
	public JPAHelper jpaHelper() {
		return jpaHelper;
	}

	/**
	 * Find a customer by email.
	 * 
	 * @param email customer email
	 * @return customer
	 */
	public Customer findByEmail(String email) {
		List<Customer> customers = jpaHelper.query("SELECT c FROM Customer c WHERE email = :email" , 
				new String[] {"email"}, 
				new String[] {email},
				Customer.class);
		// Tehcnically there should only zero or one customer found
		if (!customers.isEmpty()) {
			return customers.get(0);
		} else {
			return null;
		}
	}

}
