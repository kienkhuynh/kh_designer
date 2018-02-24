package kh.resource;

import java.security.Principal;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import kh.entities.Customer;
import kh.entities.CustomerOrder;
import kh.entities.Item;
import kh.springcontext.ApplicationContextUtils;
import kh.web.core.CustomerOrderManager;

@Path("/customerorders")
public class CustomerOrderResource {
  
	private static String CUSTOMER_SESSION = "CUSTOMER_SESSION";
	
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getCustomerOrder(@PathParam("id") int id, @Context ServletContext servletContext) {
    	CustomerOrderManager customerOrderMgr = ApplicationContextUtils.customerOrderMgr(servletContext);
        CustomerOrder order = customerOrderMgr.find(id);
        if (order != null) {
        	return Response.ok(customerOrderMgr.toJsonString(order), MediaType.APPLICATION_JSON).build();
        } else {
        	return Response.status(Response.Status.NOT_FOUND).entity(String.format("Order %d not found", id)).build();
        }
    }
 
    @GET
    @Path("/current")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getCustomerOrder(@Context HttpServletRequest request) {
    	CustomerOrderManager customerOrderMgr = ApplicationContextUtils.customerOrderMgr(request.getServletContext());
    	Customer customer = getCurrentCustomer(request);
    	CustomerOrder currentOrder = null;
    	
    	if (customer != null) {
    		currentOrder = customerOrderMgr.getCurrentOrder(customer.getCustomerId());
    	} else {
    		currentOrder = (CustomerOrder) request.getServletContext().getAttribute(CustomerOrderManager.ANONYMOUS_CUSTOMER_ORDER_SESSION_ATTRIBUTE);
    	}
    	if (currentOrder == null) {
    		currentOrder = new CustomerOrder();
    		currentOrder.setProcessStatus(false);
    		currentOrder.setCustomerOrderCode(customerOrderMgr.genCustomerOrderCode());
    		if (customer != null) {
    			currentOrder.setCustomer(customer);
    			customerOrderMgr.addOrUpdate(currentOrder);
    		}
    	}
    	request.getServletContext().setAttribute(CustomerOrderManager.ANONYMOUS_CUSTOMER_ORDER_SESSION_ATTRIBUTE, currentOrder);
    	return Response.ok(customerOrderMgr.toJsonString(currentOrder), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/order/{inventory_id}")
    @Consumes({ MediaType.APPLICATION_JSON})
    public Response updateCustomerOrder(Item item, @PathParam("inventory_id") int inventoryId, @QueryParam("quantity") int quantity, @Context HttpServletRequest request) {
    	CustomerOrderManager customerOrderMgr = ApplicationContextUtils.customerOrderMgr(request.getServletContext());
    	Customer customer = getCurrentCustomer(request);
    	
    	// Retrieve the current customer order.
    	CustomerOrder currentOrder;
    	if (customer != null) {
    		currentOrder = customerOrderMgr.getCurrentOrder(customer.getCustomerId());
        	if (currentOrder == null) {
        		currentOrder = new CustomerOrder();
        		currentOrder.setCustomer(customer);
        	}
    	} else {
    		currentOrder = (CustomerOrder) request.getServletContext().getAttribute(CustomerOrderManager.ANONYMOUS_CUSTOMER_ORDER_SESSION_ATTRIBUTE);
    		if (currentOrder == null) {
    			currentOrder = new CustomerOrder();
        		currentOrder.setCustomerOrderCode(customerOrderMgr.genCustomerOrderCode());
    		}
    	}
    	
    	// Update or create the item to be ordered.
    	customerOrderMgr.createOrUpdateOrderByItem(customer, currentOrder, inventoryId, quantity == 0 ? 1 : quantity);
    	request.getServletContext().setAttribute(CustomerOrderManager.ANONYMOUS_CUSTOMER_ORDER_SESSION_ATTRIBUTE, currentOrder);
    	return Response.ok(currentOrder).build();
    }
    
    private Customer getCurrentCustomer(HttpServletRequest request) {
    	Principal loggedInUser = request.getUserPrincipal();
    	if (loggedInUser == null) {
    		return null;
    	} else {
    		return (Customer) request.getServletContext().getAttribute(CUSTOMER_SESSION);
    	}
    }
}