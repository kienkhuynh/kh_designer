package kh.resource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Root class for all resources.
 * 
 * @author kien
 */
@ApplicationPath("/resources")
public class ResourceConfiguration extends Application {
	
	/**
	 * Returns a list of resource classes to generate proper servlet mappings.
	 */
    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>(
          Arrays.asList(
            ItemResource.class,
            CustomerOrderResource.class));
    }
}