package kh.resource;

import java.security.Principal;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import kh.entities.Customer;
import kh.entities.CustomerOrder;
import kh.entities.Inventory;
import kh.springcontext.ApplicationContextUtils;
import kh.web.core.CustomerOrderManager;
import kh.web.core.InventoryManager;

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

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_HTML)
    public Response getNumberOfItemsInCart(@Context HttpServletRequest request) {
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
    	if (currentOrder.getOrderByItems() != null) {
    		return Response.ok(String.valueOf(currentOrder.getOrderByItems().size()), MediaType.TEXT_HTML_TYPE).build();
    	} else {
    		return Response.ok("0", MediaType.TEXT_HTML_TYPE).build();
    	}
    }
    
    @POST
    @Path("/order/{inventory_id}")
    @Consumes({ MediaType.APPLICATION_JSON})
    public Response updateCustomerOrder(@PathParam("inventory_id") int inventoryId, @QueryParam("quantity") int quantity, @Context HttpServletRequest request) {
    	CustomerOrderManager customerOrderMgr = ApplicationContextUtils.customerOrderMgr(request.getServletContext());
    	InventoryManager inventoryMgr = ApplicationContextUtils.inventoryMgr(request.getServletContext());
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
    	Inventory inventory = inventoryMgr.findByInventoryId(inventoryId);
    	if (inventory != null) {
        	customerOrderMgr.createOrUpdateOrderByItem(customer, currentOrder, inventory, quantity == 0 ? 1 : quantity);
        	request.getServletContext().setAttribute(CustomerOrderManager.ANONYMOUS_CUSTOMER_ORDER_SESSION_ATTRIBUTE, currentOrder);
        	return Response.ok(customerOrderMgr.toJsonString(currentOrder), MediaType.APPLICATION_JSON).build();
    	} else {
    		return Response.status(Response.Status.NOT_FOUND).entity(String.format("Inventory %d no longer exists", inventoryId)).build();
    	}
    }

    @PUT
    @Path("/order/{inventory_id}")
    @Consumes({ MediaType.APPLICATION_JSON})
    public Response updateOrderedItemQuantity(@PathParam("inventory_id") int inventoryId, @QueryParam("quantity") int quantity, @Context HttpServletRequest request) {
    	CustomerOrderManager customerOrderMgr = ApplicationContextUtils.customerOrderMgr(request.getServletContext());
    	InventoryManager inventoryMgr = ApplicationContextUtils.inventoryMgr(request.getServletContext());
    	Customer customer = getCurrentCustomer(request);
    	
    	// Retrieve the current customer order.
    	CustomerOrder currentOrder;
    	if (customer != null) {
    		currentOrder = customerOrderMgr.getCurrentOrder(customer.getCustomerId());
    	} else {
    		currentOrder = (CustomerOrder) request.getServletContext().getAttribute(CustomerOrderManager.ANONYMOUS_CUSTOMER_ORDER_SESSION_ATTRIBUTE);
    	}
    	
    	if (currentOrder != null) {
        	// Update or create the item to be ordered.
        	Inventory inventory = inventoryMgr.findByInventoryId(inventoryId);
        	if (inventory != null) {
                customerOrderMgr.updateOrderQuantity(customer, currentOrder, inventory, quantity);
            	request.getServletContext().setAttribute(CustomerOrderManager.ANONYMOUS_CUSTOMER_ORDER_SESSION_ATTRIBUTE, currentOrder);
        	}
    	}
    	return Response.ok().build();
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