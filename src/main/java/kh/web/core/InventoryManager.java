package kh.web.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import kh.entities.Item;

/*
 * This class manages items and their inventories.
 * 
 * @author kh
 */
@Component
public class InventoryManager extends AbstractManager<Item> {
	
	Logger log = Logger.getLogger(InventoryManager.class);
	
	@Autowired JPAHelper jpaHelper;
	
	/**
	 * Initializes the entity type that this manager manages.
	 */
	public InventoryManager() {
		super(Item.class);
		log.info("InventoryRepository initailized");
	}
	
	/**
	 * Searches for a particular item using a search key.
	 * 
	 * @param searchKey a single search key
	 * @return list of matched items.
	 */
	public List<Item> searchItems(String searchKey) {
		return jpaHelper.query("SELECT i FROM Item i WHERE i.searchKeywords like :searchKeywords", 
				new String[] { "searchKeywords"},
				new String[] { String.format("%%|%s|%%", searchKey) }, Item.class);
	}
	
	@Override
	public JPAHelper jpaHelper() {
		return jpaHelper;
	}
}
