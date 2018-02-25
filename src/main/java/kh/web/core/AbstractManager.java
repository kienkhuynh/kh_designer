package kh.web.core;

import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Implements 4 generic interfaces for any JPA entities: 
 * 		all: list all instances of an entity type
 * 		find: find instance of the entity type using primary key
 * 		remove: remove an instance of the entity type 
 * 		add: add an instance
 * 		
 * @author: kh
 */
public abstract class AbstractManager<T> {
	
	static Logger log = Logger.getLogger(AbstractManager.class);
	
	private final Class<T> type;
	
	public AbstractManager(Class<T> type) {
		this.type = type;
	}
	
	/**
	 * Retrieves all the items
	 * 
	 * @return list of items
	 */
	public List<T> all() {
		return jpaHelper().findAll(type);
	}
	
	/**
	 * Finds a particular item
	 * 
	 * @param id item id
	 * @return item and its inventory
	 */
	public T find(int id) {
		return jpaHelper().find(id, type);
	}
	
	/**
	 * Adds a new item
	 * 
	 * @param item item to be added
	 * @return creation status
	 */
	public boolean addOrUpdate(T t) {
		return jpaHelper().persist(t);
	}
	
	/**
	 * Removes an item
	 * 
	 * @param item item to be added
	 * @return creation status
	 */
	public boolean delete(T t) {
		return jpaHelper().remove(t);
	}
	
	/**
	 * Maps an entity to json string.
	 * 
	 * @param obj the entity
	 * @return the json formattted string for the entity.
	 */
	public String toJsonString(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			log.error(e);
		}
		return null;
	}
	
	
	public abstract JPAHelper jpaHelper();
}
