package kh.web.core;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import kh.entities.Customer;
import kh.entities.CustomerOrder;
import kh.entities.OrderByItem;

/*
 * This class manages customer orders
 * 
 * @author kh
 */
@Component
public class CustomerOrderManager extends AbstractManager<CustomerOrder> {
	
	Logger log = Logger.getLogger(CustomerOrderManager.class);
	
	/**
	 * Attribute used to store anonymous customer order
	 */
	public static String ANONYMOUS_CUSTOMER_ORDER_SESSION_ATTRIBUTE = "ANONYMOUS_CUSTOMER_ORDER_SESSION_ATTRIBUTE";
	
	@Autowired JPAHelper jpaHelper;
	
	/**
	 * Initializes the entity type that this manager manages.
	 */
	public CustomerOrderManager() {
		super(CustomerOrder.class);
		log.info("CustomerOrderManager initailized");
	} 
	
	/**
	 * Used to merge two customer orders. One was created before custoemr logged in and the order is the current
	 * customer order.
	 * 
	 * @param anonymousOrder
	 * @param existingOrder
	 * @return
	 */
	public CustomerOrder mergeOrderToCurrentOrder(CustomerOrder order, Integer customerId) {
		CustomerOrder currentOrder = getCurrentOrder(customerId);
		if (currentOrder != null) {
			if (!order.getOrderByItems().isEmpty()) {
				// Create a map from inventory id to the order item to easily merge all inventory
				List<OrderByItem> currentItems = currentOrder.getOrderByItems();
				Map<Integer, OrderByItem> orderedItemsByInventoryIdMap = new HashMap<Integer, OrderByItem>(currentItems.size());
				for (OrderByItem orderedItem : currentItems) {
					orderedItemsByInventoryIdMap.put(orderedItem.getInventoryId(), orderedItem);
				}
				for (OrderByItem mergedItem :  order.getOrderByItems()) {
					OrderByItem orderedItem = orderedItemsByInventoryIdMap.get(mergedItem.getInventoryId());
					orderedItem.setQuantity(orderedItem.getQuantity() + mergedItem.getQuantity());
				}
				addOrUpdate(currentOrder);
			}
			if (order.getId() != null || order.getId() > 0) {
				delete(order);
			}
		} else {
			// Make this order the current order by persisting it
			addOrUpdate(currentOrder);
		}
		return currentOrder;
	}
	
	/**
	 * Gets the current order - an order with processStatus = false. Technically there should be only one order with processStatus = false
	 * 
	 * @return the current order.
	 */
	public CustomerOrder getCurrentOrder(Integer customerId) {
		List<CustomerOrder> orders = jpaHelper.query("SELECT o FROM CustomerOrder o WHERE o.customer = :customer AND o.processStatus = :processStatus", 
				new String[] {"customer", "processStatus"}, 
				new Object[] {customerId, false},
				CustomerOrder.class);
		
		// Technically there should only 1 current order.
		if (!orders.isEmpty()) {
			return orders.get(0);
		}
		return null;
	}
	
	/**
	 * Order the item
	 * 
	 * @param customerOrder the customer order that this order belongs to
	 * @param inventoryId item to be ordered
	 * @param quantity order quanity
	 * @return The order item
	 */
	public OrderByItem createOrUpdateOrderByItem(Customer customer, CustomerOrder customerOrder, int inventoryId, int quantity) {
		List<OrderByItem> orderedItems = customerOrder.getOrderByItems();
		OrderByItem orderedItem = null;
		
		// Looking for the same item and update the quantity.
		for (OrderByItem item : orderedItems) {
			if (item.getInventoryId() == inventoryId) {
				orderedItem = item;
				orderedItem.setQuantity(orderedItem.getQuantity() + quantity);
				break;
			}
		}
		
		// If none is found, create a new one.
		if (orderedItem == null) {
			orderedItem = new OrderByItem();
			orderedItem.setCustomerOrder(customerOrder);
			orderedItem.setQuantity(quantity);
			orderedItem.setInventoryId(quantity);
			orderedItem.setOrderDate(new Timestamp(System.currentTimeMillis()));
			customerOrder.addOrderByItem(orderedItem);
		}
		
		// Update the order item if the order belong to a non-anonymous customer.
		if (customer != null) {
			jpaHelper.entityMgr().persist(orderedItem);
		}
		return orderedItem;
	}
	
	/**
	 * Generate the customer order code;
	 * 
	 * @return order uuid.
	 */
	public String genCustomerOrderCode() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	@Override
	public JPAHelper jpaHelper() {
		return jpaHelper;
	}
	
}
