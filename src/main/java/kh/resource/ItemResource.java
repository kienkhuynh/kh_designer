package kh.resource;

import java.util.List;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import kh.web.core.InventoryManager;

/*
 * Define REST apis to retrieve/update/delete
 * 
 */
@Path("/items")
public class ItemResource {
  
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getItem(@PathParam("id") int id, @Context ServletContext servletContext) {
    	InventoryManager inventoryMgr = ApplicationContextUtils.inventoryMgr(servletContext);
        Item item = inventoryMgr.find(id);
        if (item != null) {
        	return Response.ok(inventoryMgr.toJsonString(item), MediaType.APPLICATION_JSON).build();
        } else {
        	return Response.status(Response.Status.NOT_FOUND).entity(String.format("Item %d not found", id)).build();
        }
    }
 
    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getItems(@QueryParam("search") String search, @Context ServletContext servletContext) {
    	InventoryManager inventoryMgr = ApplicationContextUtils.inventoryMgr(servletContext);
    	List<Item> items = (search != null) ? inventoryMgr.searchItems(search)
    										  : inventoryMgr.all();
    	return Response.ok(inventoryMgr.toJsonString(items), MediaType.APPLICATION_JSON).build();
    }
 
    @PUT
    @Path("/")
    @Consumes({ MediaType.APPLICATION_JSON})
    public Response addItem(Item item, @Context UriInfo uriInfo, @Context ServletContext servletContext) {
    	InventoryManager inventoryMgr = ApplicationContextUtils.inventoryMgr(servletContext);
    	if (inventoryMgr.addOrUpdate(item)) {
            return Response.status(Response.Status.CREATED.getStatusCode())
                    .header( "Location", String.format("%s/%s",uriInfo.getAbsolutePath().toString(), 
                      item.getItemId())).build();
    	} else {
    		return Response.serverError().build();
    	}
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") int id, @Context ServletContext servletContext) {
    	InventoryManager inventoryMgr = ApplicationContextUtils.inventoryMgr(servletContext);
        Item item = inventoryMgr.find(id);
        if (item != null) {
        	if (inventoryMgr.delete(item)) {
            	return Response.ok(String.format("Item %d deleted", id)).build();
        	} else {
        		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        	}
        } else {
        	return Response.status(Response.Status.NOT_FOUND).entity(String.format("Item %d not found", id)).build();
        }
    }
}