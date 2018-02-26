package kh.web.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import kh.entities.CustomerOrder;
import kh.entities.Inventory;
import kh.entities.OrderByItem;

/*
 * This class manages customer orders
 * 
 * @author kh
 */
@Component
public class CustomerOrderManager extends AbstractManager<CustomerOrder> {
	
	Logger log = Logger.getLogger(CustomerOrderManager.class);
	
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
					orderedItemsByInventoryIdMap.put(orderedItem.getInventory().getInventoryId(), orderedItem);
				}
				for (OrderByItem mergedItem :  order.getOrderByItems()) {
					OrderByItem orderedItem = orderedItemsByInventoryIdMap.get(mergedItem.getInventory().getInventoryId());
					orderedItem.setQuantity(orderedItem.getQuantity() + mergedItem.getQuantity());
				}
				addOrUpdate(currentOrder);
			}
			if (order.getCustomerOrderId() != null || order.getCustomerOrderId() > 0) {
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
		List<CustomerOrder> orders = jpaHelper.query("SELECT o FROM CustomerOrder o WHERE o.customerId = :customerId AND o.processStatus = :processStatus", 
				new String[] {"customerId", "processStatus"}, 
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
	 * @param inventory item to be ordered
	 * @param quantity order quanity
	 * @return The order item
	 */
	public OrderByItem createOrUpdateOrderByItem(Integer customerId, CustomerOrder customerOrder, Inventory inventory, int quantity) {
		List<OrderByItem> orderedItems = customerOrder.getOrderByItems();
		OrderByItem orderedItem = null;
		
		// Looking for the same item and update the quantity.
		if (orderedItems != null) {
			for (OrderByItem item : orderedItems) {
				if (item.getInventory().getInventoryId() == inventory.getInventoryId()) {
					orderedItem = item;
					orderedItem.setQuantity(orderedItem.getQuantity() + quantity);
					break;
				}
			}
		} else {
			customerOrder.setOrderByItems(new ArrayList<OrderByItem>());
		}
		
		// If none is found, create a new one.
		if (orderedItem == null) {
			orderedItem = new OrderByItem();
			orderedItem.setCustomerOrder(customerOrder);
			orderedItem.setQuantity(quantity);
			orderedItem.setInventory(inventory);
			customerOrder.addOrderByItem(orderedItem);
		}
		
		// Update the order item if the order belong to a non-anonymous customer.
		if (customerId != null) {
			jpaHelper().persist(customerOrder);
		}
		return orderedItem;
	}
	
	/**
	 * Updates order quantity
	 * 
	 * @param customerOrder the customer order that this order belongs to
	 * @param inventory item to be ordered
	 * @param quantity order quanity
	 * @return The order item
	 */
	public OrderByItem updateOrderQuantity(Integer customerId, CustomerOrder customerOrder, Inventory inventory, int quantity) {
		List<OrderByItem> orderedItems = customerOrder.getOrderByItems();
		OrderByItem orderedItem = null;
		
		// Looking for the same item and update the quantity.
		if (orderedItems != null) {
			for (OrderByItem item : orderedItems) {
				if (item.getInventory().getInventoryId() == inventory.getInventoryId()) {
					orderedItem = item;
					break;
				}
			}
		}
		
		if (orderedItem != null) {
			if (quantity == 0) {
				customerOrder.removeOrderByItem(orderedItem);
			} else if (quantity > 0 ) {
				orderedItem.setQuantity(quantity);
			}
			if (customerId != null && orderedItem.getItemOrderId() != null) {
				jpaHelper.remove(orderedItem);
				jpaHelper.persist(customerOrder);
			}
		}
		
		return orderedItem;
	} 
	
	@Override
	public JPAHelper jpaHelper() {
		return jpaHelper;
	}
	
}
