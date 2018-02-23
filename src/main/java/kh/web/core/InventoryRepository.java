package kh.web.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kh.entities.Item;

/**
 * This class provides entry points (updating, deleting, and so on) to all inventories.
 * 
 * @author kh
 */
@Component
public class InventoryRepository {

	@Autowired JPAHelper jpaHelper;
	
	public InventoryRepository() {
		System.out.println("Inventory initailized");
	}
	
	public List<Item> items() {
		return jpaHelper.findAll(Item.class);
	}
	public Item findItem(int id) {
		return jpaHelper.find(id, Item.class);
	}
	public boolean addItem(Item item) {
		return jpaHelper.persist(item);
	}
	public List<Item> searchItems(String searchKey) {
		return jpaHelper.query("SELECT i FROM Item i WHERE i.searchKeywords like %:searchKeywords%", 
				new String[] { "searchKeywords"},
				new String[] { searchKey }, Item.class);
	}
}
