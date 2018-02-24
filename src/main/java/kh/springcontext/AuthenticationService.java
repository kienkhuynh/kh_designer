package kh.springcontext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.sun.security.auth.UserPrincipal;

import kh.entities.Customer;
import kh.web.core.CustomerManager;

@Component
public class AuthenticationService implements UserDetailsService {
  
	private static String CUSTOM_INFO = "CUSTOM_INFO";

	@Autowired CustomerManager customerManager;
	
	public AuthenticationService() {
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		email = StringUtils.trim(StringUtils.lowerCase(email));
		Customer customer = customerManager.findByEmail(email);
		if (customer != null) {
			return new KHUserDetails(customer);
		} else {
			return null;
		}
	} 
 
	public static String getUserRoleFromRequest(HttpServletRequest request) {
		String role = null;
		if (request.getUserPrincipal() instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) request.getUserPrincipal();
			if (user.isAuthenticated()) {
				for (GrantedAuthority authority : user.getAuthorities()) {
					role = authority.getAuthority();
					break;
				}
				
			}
		}
		return role;
	}
	
	/*
	 * Logged in customer information object
	 */
	public static class KHUserDetails implements UserDetails {
		private static final long serialVersionUID = 1L;
		private Customer customer;
		private Collection<? extends GrantedAuthority> authories;
		
		KHUserDetails(Customer customer) {
			 this.authories = Collections.singleton(new GrantedAuthority() {
				@Override
				public String getAuthority() {
					return "user";					
				}
			});
		}
		
		
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return authories;
		}

		@Override
		public String getPassword() {
			return customer.getPassword();
		}

		@Override
		public String getUsername() {
			return customer.getEmail();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return false;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return false;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
	}
}
