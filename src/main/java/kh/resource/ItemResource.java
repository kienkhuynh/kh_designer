package kh.resource;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import kh.entities.Item;
import kh.springcontext.ApplicationContextUtils;
import kh.web.core.InventoryRepository;

@Path("/items")
public class ItemResource {
  
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getItem(@PathParam("id") int id, @Context ServletContext servletContext) {
    	InventoryRepository repo = ApplicationContextUtils.repo(servletContext);
        Item item = repo.findItem(id);
        if (item != null) {
        	return Response.ok(item, MediaType.APPLICATION_JSON_TYPE).build();
        } else {
        	return Response.status(Response.Status.NOT_FOUND).entity(String.format("Item %d not found", id)).build();
        }
    }
 
    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getItems(@QueryParam("search") String search, @Context ServletContext servletContext) {
    	InventoryRepository repo = ApplicationContextUtils.repo(servletContext);
    	List<Item> items = (search != null) ? repo.searchItems(search)
    										  : repo.items();
    	return Response.ok(items, MediaType.APPLICATION_JSON_TYPE).build();
    }
 
    @PUT
    @Consumes({ MediaType.APPLICATION_JSON})
    public Response addItem(Item item, @Context UriInfo uriInfo, @Context ServletContext servletContext) {
    	InventoryRepository repo = ApplicationContextUtils.repo(servletContext);
    	if (repo.addItem(item)) {
            return Response.status(Response.Status.CREATED.getStatusCode())
                    .header( "Location", String.format("%s/%s",uriInfo.getAbsolutePath().toString(), 
                      item.getItemId())).build();
    	} else {
    		return Response.serverError().build();
    	}
    }
}