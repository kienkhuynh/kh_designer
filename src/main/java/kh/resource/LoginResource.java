package kh.resource;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import kh.entities.Customer;
import kh.springcontext.ApplicationContextUtils;
import kh.web.core.PasswordEncryptor;
/*
 * Login module
 */
@Path("/login")
public class LoginResource {

	@POST
    @Path("/")
	@Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response login(Customer customer, @Context HttpServletRequest request) {
    	String email = customer.getEmail();
    	String password = customer.getPassword();
    	if (email == null || password == null) {
    		return Response.status(Response.Status.FORBIDDEN).entity("Must provide login information").build();
    	} else {
    		PasswordEncryptor encoder = ApplicationContextUtils.bean(request.getServletContext(), PasswordEncryptor.class);
    		email = email.toLowerCase();
    		String encryptedPassword = encoder.hash(password);
    		try {
    			request.logout();
				request.login(email, encryptedPassword);
				return Response.ok().build();
			} catch (Exception e) {
			}
    		return Response.status(Response.Status.FORBIDDEN).build();
    	}
    }
	 
}
