package kh.resource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.ibm.json.java.JSONObject;

import kh.entities.Customer;
import kh.springcontext.ApplicationContextUtils;
import kh.web.core.CustomerManager;
import kh.web.core.PasswordEncryptor;
import kh.web.core.SessionAttributes;
/*
 * Login module
 */
@Path("/login")
public class LoginResource {

	@POST
    @Path("/in")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response login(@Context HttpServletRequest request) {
		try {
			JSONObject data = JSONObject.parse(request.getInputStream());
			String email = (String) data.get("email");
	    	String password = (String) data.get("password");
	    	if (email == null || password == null) {
	    		return Response.status(Response.Status.FORBIDDEN).entity("Must provide login information").build();
	    	} else {
	    		PasswordEncryptor encoder = ApplicationContextUtils.bean(request.getServletContext(), PasswordEncryptor.class);
	    		CustomerManager customerMgr = ApplicationContextUtils.bean(request.getServletContext(), CustomerManager.class);
	    		try {
	    			email = email.toLowerCase();
		    		Customer customer = customerMgr.findByEmail(email);
		    		if (customer != null) {
		    			request.logout();
						request.login(email, encoder.hash(password));
						if (request.getSession() == null) {
							request.getSession(true);
						}
						request.getSession().setAttribute(SessionAttributes.CUSTOMER_ID, customer.getCustomerId());
						return Response.ok(customer.getFullName(), MediaType.TEXT_PLAIN).build();
		    		}
				} catch (Exception e) {
					e.printStackTrace();
				}
	    		
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.FORBIDDEN).build();
    }
	
	@GET
    @Path("/out")
    public void logout(@Context HttpServletRequest request) {
		try {
			request.logout();
			Response.seeOther(new URI("/#home"));
		} catch (URISyntaxException | ServletException e) {
		}
    }
}
