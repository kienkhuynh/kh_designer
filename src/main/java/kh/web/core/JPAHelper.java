package kh.web.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kh.monitor.MonitorReporter;
import kh.monitor.MonitorReporter.MetricKPI;

@Component
public class JPAHelper {
	
	Logger log = Logger.getLogger(JPAHelper.class);
	
	private EntityManager entityMgr;
	
	@Autowired SystemConfiguration config;
	
	@Autowired MonitorReporter monitor;
	
	public JPAHelper() {
		
	}
	
	@PostConstruct
	protected void init() {
		Map<String, String> props = new HashMap<String, String>(4);
		props.put("javax.persistence.jdbc.driver", config.driver);
		props.put("javax.persistence.jdbc.schema", config.schema);
		props.put("javax.persistence.jdbc.user", config.dbUser);
		props.put("javax.persistence.jdbc.password", config.dbPassword);
		props.put("javax.persistence.jdbc.url", config.databaseUrl);

		try {
			entityMgr = Persistence.createEntityManagerFactory("kh.jpa", props)
								.createEntityManager();
			log.info("JPAHelper.init: Entity Manager created.");
			monitor.report(MetricKPI.DATABASE_STATUS, 0f);
		} catch (Exception e) {
			log.debug(e);
			e.printStackTrace();
			monitor.report(MetricKPI.DATABASE_STATUS, 1f);
		}
	}
 
	protected EntityManager entityMgr() {
		return entityMgr;
	}

	public <T> List<T> findAll(Class<T> clazz) {
		EntityManager entityMgr = entityMgr();
		if (entityMgr != null) {
			return entityMgr.createNamedQuery(clazz.getSimpleName() + ".findAll", clazz).getResultList();
		}
		return Collections.emptyList();
	}
	public <T> List<T> get(String sql, String[] params, Object[] values, Class<T> clazz) {
		EntityManager entityMgr = entityMgr();
		if (entityMgr != null) {
			TypedQuery<T> query = entityMgr.createQuery(sql, clazz);
			for (int i = 0; params != null && values != null && i < params.length; i++) {
				query.setParameter(params[i], values[i]);
			}
			try {
				return query.getResultList();
			} catch (Exception e) {
				log.error(e);
			}
		}
		return Collections.emptyList();
	}
	public <T> List<T> query(String sql, String[] params, Object[] values, Class<T> clazz) {
		try {
			TypedQuery<T> query = entityMgr().createQuery(sql, clazz);
			for (int i = 0; params != null && values != null && i < params.length; i++) {
				query.setParameter(params[i], values[i]);
			}
			return query.getResultList();
		} catch (Exception e) {
			log.error(e);
		} finally {
		}
		return Collections.emptyList();
	}

	public <T> T find(String id, Class<T> clazz) {
		try {
			return find(Integer.parseInt(id), clazz);
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public <T> T find(Integer id, Class<T> clazz) {
		return entityMgr().find(clazz, id);
	}
	
	public  boolean remove(Object object) {
		try {
			entityMgr().getTransaction().begin();
			if (!entityMgr.contains(object)) {
				object = entityMgr.merge(object);
			}
			entityMgr.remove(object);
			entityMgr.getTransaction().commit();
			return true;
		} catch (Exception e) {
			log.error(e);
		} finally {
			try {
				if (entityMgr.getTransaction().isActive()) {
					entityMgr.getTransaction().rollback();
				}
			} catch (Exception e) {
			}
		}
		return false;
	} 
	public boolean persist(Object object) {
		try {
			entityMgr().getTransaction().begin();
			if (!entityMgr.contains(object)) {
				object = entityMgr.merge(object);
			}
			entityMgr.persist(object);
			entityMgr.getTransaction().commit();
			return true;
		} catch (Exception e) {
			log.error(e);
		} finally {
			try {
				if (entityMgr.getTransaction().isActive()) {
					entityMgr.getTransaction().rollback();
				}
			} catch (Exception e) {
			}
		}
		return false;
	} 
}