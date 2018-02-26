package kh.resource;

import java.security.Principal;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

import com.ibm.json.java.JSONObject;

import kh.entities.Customer;
import kh.entities.CustomerOrder;
import kh.entities.Inventory;
import kh.springcontext.ApplicationContextUtils;
import kh.web.core.CustomerManager;
import kh.web.core.CustomerOrderManager;
import kh.web.core.InventoryManager;
import kh.web.core.SessionAttributes;

@Path("/customerorders")
public class CustomerOrderResource {
  
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
    	Integer customerId = getCurrentCustomerId(request);
    	CustomerOrder currentOrder = null;
    	
    	if (customerId != null) {
    		currentOrder = customerOrderMgr.getCurrentOrder(customerId);
    	} else {
    		currentOrder = getSessionAttribute(request, SessionAttributes.CUSTOMER_CURRENT_ORDER);
    	}
    	if (currentOrder == null) {
    		currentOrder = new CustomerOrder();
    		currentOrder.setProcessStatus(false);
    		if (customerId != null) {
    			customerOrderMgr.addOrUpdate(currentOrder);
    		}
    	}
    	setSessionAttribute(request, SessionAttributes.CUSTOMER_CURRENT_ORDER, currentOrder);
    	return Response.ok(customerOrderMgr.toJsonString(currentOrder), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/customerInfo")
    @Produces(MediaType.TEXT_HTML)
    public Response getCustomerInfo(@Context HttpServletRequest request) {
    	CustomerOrderManager customerOrderMgr = ApplicationContextUtils.customerOrderMgr(request.getServletContext());
    	Integer customerId = getCurrentCustomerId(request);
    	
    	// Retrieve the current customer order.
    	CustomerOrder currentOrder;
    	if (customerId != null) {
    		currentOrder = customerOrderMgr.getCurrentOrder(customerId);
        	if (currentOrder == null) {
        		currentOrder = new CustomerOrder();
        		currentOrder.setCustomerId(customerId);
        		currentOrder.setProcessStatus(false);
        		customerOrderMgr.addOrUpdate(currentOrder);
        	}
    	} else {
    		currentOrder = (CustomerOrder) request.getSession(true).getAttribute(SessionAttributes.CUSTOMER_CURRENT_ORDER);
    		if (currentOrder == null) {
    			currentOrder = new CustomerOrder();
    		}
    	}
    	JSONObject data = new JSONObject();
    	if (currentOrder.getOrderByItems() != null) {
    		data.put("items", currentOrder.getOrderByItems().size());
    		if (customerId != null) {
    			CustomerManager customerMgr = ApplicationContextUtils.customerMgr(request.getServletContext());
    			Customer customer = customerMgr.find(customerId);
    			data.put("customerId", customerId);
    			data.put("customerName", customer.getFullName());
    		}
    	}
    	return Response.ok(data, MediaType.APPLICATION_JSON_TYPE).build();
    }
    
    @POST
    @Path("/order/{inventory_id}")
    @Consumes({ MediaType.APPLICATION_JSON})
    public Response updateCustomerOrder(@PathParam("inventory_id") int inventoryId, @QueryParam("quantity") int quantity, @Context HttpServletRequest request) {
    	CustomerOrderManager customerOrderMgr = ApplicationContextUtils.customerOrderMgr(request.getServletContext());
    	InventoryManager inventoryMgr = ApplicationContextUtils.inventoryMgr(request.getServletContext());
    	Integer customerId = getCurrentCustomerId(request);
    	
    	// Retrieve the current customer order.
    	CustomerOrder currentOrder;
    	if (customerId != null) {
    		currentOrder = customerOrderMgr.getCurrentOrder(customerId);
        	if (currentOrder == null) {
        		currentOrder = new CustomerOrder();
        		currentOrder.setCustomerId(customerId);
        		customerOrderMgr.addOrUpdate(currentOrder);
        	}
    	} else {
    		currentOrder = getSessionAttribute(request, SessionAttributes.CUSTOMER_CURRENT_ORDER);
    		if (currentOrder == null) {
    			currentOrder = new CustomerOrder();
    		}
    	}
    	
    	// Update or create the item to be ordered.
    	Inventory inventory = inventoryMgr.findByInventoryId(inventoryId);
    	if (inventory != null) {
        	customerOrderMgr.createOrUpdateOrderByItem(customerId, currentOrder, inventory, quantity == 0 ? 1 : quantity);
        	setSessionAttribute(request, SessionAttributes.CUSTOMER_CURRENT_ORDER, currentOrder);
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
    	Integer customerId = getCurrentCustomerId(request);
    	
    	// Retrieve the current customer order.
    	CustomerOrder currentOrder;
    	if (customerId != null) {
    		currentOrder = customerOrderMgr.getCurrentOrder(customerId);
    	} else {
    		currentOrder = getSessionAttribute(request, SessionAttributes.CUSTOMER_CURRENT_ORDER);
    	}
    	
    	if (currentOrder != null) {
        	// Update or create the item to be ordered.
        	Inventory inventory = inventoryMgr.findByInventoryId(inventoryId);
        	if (inventory != null) {
                customerOrderMgr.updateOrderQuantity(customerId, currentOrder, inventory, quantity);
                setSessionAttribute(request, SessionAttributes.CUSTOMER_CURRENT_ORDER, currentOrder);
        	}
    	}
    	return Response.ok().build();
    }
    
    @POST
    @Path("/{id}/submit")
    @Consumes({ MediaType.APPLICATION_JSON})
    public Response submitOrder(@PathParam("id") int customerOrderId, @Context HttpServletRequest request) {
    	CustomerOrderManager customerOrderMgr = ApplicationContextUtils.customerOrderMgr(request.getServletContext());
    	Integer customerId = getCurrentCustomerId(request);
    	
    	if (customerId != null) {
    		CustomerOrder currentOrder = customerOrderMgr.find(customerOrderId);
	    	if (currentOrder != null 
	    			&& currentOrder.getProcessStatus() != null && !currentOrder.getProcessStatus()
	    			&& customerId == currentOrder.getCustomerId()) {
	    		currentOrder.setProcessStatus(true);
	    		customerOrderMgr.addOrUpdate(currentOrder);
	    	}
    	}
    	// should throw error for unexpected case
    	return Response.ok().build();
    }
    @POST
    @Path("/submit")
    @Consumes({ MediaType.APPLICATION_JSON})
    public Response submitCurrentOrder(@Context HttpServletRequest request) {
    	CustomerOrderManager customerOrderMgr = ApplicationContextUtils.customerOrderMgr(request.getServletContext());
    	Integer customerId = getCurrentCustomerId(request);
    	
    	if (customerId != null) {
    		CustomerOrder currentOrder = customerOrderMgr.getCurrentOrder(customerId);
	    	if (currentOrder != null && currentOrder.getProcessStatus() != null && !currentOrder.getProcessStatus()) {
	    		currentOrder.setProcessStatus(true);
	    		customerOrderMgr.addOrUpdate(currentOrder);
	    	}
    	}
    	// should throw error for unexpected case
    	return Response.ok().build();
    }
    private Integer getCurrentCustomerId(HttpServletRequest request) {
    	Principal loggedInUser = request.getUserPrincipal();
    	if (loggedInUser == null) {
    		return null;
    	} else {
    		return (Integer) request.getSession(true).getAttribute(SessionAttributes.CUSTOMER_ID);
    	}
    }
    
    private <T> T getSessionAttribute(HttpServletRequest request, String attr) {
    	HttpSession session = request.getSession();
    	if (session != null) {
    		return (T) session.getAttribute(attr);
    	}
    	return null;
    }
    private void setSessionAttribute(HttpServletRequest request, String attr, Object value) {
    	HttpSession session = request.getSession();
    	if (session == null) {
    		session = request.getSession(true);
    	}
    	session.setAttribute(attr, value);
    }
}