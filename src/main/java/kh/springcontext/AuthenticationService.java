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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.sun.security.auth.UserPrincipal;

 

@Component
@SuppressWarnings("deprecation")

public class AuthenticationService implements UserDetailsService {
  
	private static String SESSION_OWNED_SERVICES = "SESSION_OWNED_SERVICES";
	private static int ATTEMPT_MAX = 10;
	private static long ONE_DAY = 1000 * 60 * 60 * 24;
	public static long FIV_YEARS =  5 * 1000 * 60 * 60 * 24 * 365L ;
	private boolean isDev = false;

	
	public AuthenticationService() {
		isDev = Boolean.valueOf(System.getProperty("devEnvironment"));
		if (isDev) {
			System.out.println("This is a dev environment.");			
		}
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		email = StringUtils.trim(StringUtils.lowerCase(email));
		return null;
		
		/*Token token = findUserToken(email);
		
		if (token != null) {
			UserRole role = findUserRole(email);
			return new BGUserDetails(token, role);			
		} else {
			return null;
		}*/
	} 
 
	protected void performLogin(String email, String token, final Authorisation authorisation) { 
		// Try email and password basic authentication
		List<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>();
		authorityList.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return authorisation.primaryRole;
			}
		});
		 
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(new UserPrincipal(email), token, authorityList);
		SecurityContextImpl securityContext = new SecurityContextImpl();
		securityContext.setAuthentication(authentication);
		
		SecurityContextHolder.setContext(securityContext);
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
	public static void setAuthorisationSession(HttpServletRequest request, Authorisation authorisation) {
		HttpSession session = request.getSession();
		if (session != null) {
			session.setAttribute(SESSION_OWNED_SERVICES, authorisation);
		}
	}
	public static Authorisation getAuthorisationFromRequest(HttpServletRequest request) {
		Authorisation authorisation = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			authorisation = (Authorisation) session.getAttribute(SESSION_OWNED_SERVICES);
		}
		return authorisation;
	}
	
	public static class Authorisation {
		protected String primaryRole;
		protected Map<AuthorisationType, List<String>> authorisedServices;
		protected boolean isBlocked = false;
		protected String token;
		public Authorisation(String primaryRole, String token) {
			this.primaryRole = primaryRole;
			this.token = token;
		}
		public Authorisation(String primaryRole, boolean isBlocked) {
			this.isBlocked = isBlocked;
		}
		public String getToken() {
			return token;
		}
		public boolean isBlocked() {
			return isBlocked;
		}
		public boolean isAdmin() {
			return "admin".equals(primaryRole);
		}
		public boolean isEditor() {
			return "editor".equals(primaryRole);
		}
		public boolean isOps() {
			return "ops".equals(primaryRole);
		}
		public boolean isAuthorised() {
			return !isBlocked && ("ops".equals(primaryRole) || isAdmin() || isEditor());
		}
		public String addAuthorisedService(AuthorisationType type, String team) {
			if (!StringUtils.isEmpty(team)) {
				if (authorisedServices == null) authorisedServices = new HashMap<AuthorisationType, List<String>>();
				
				List<String> services; 
				if (authorisedServices.containsKey(AuthorisationType.UCD)) {
					services = authorisedServices.get(AuthorisationType.UCD);
				} else {
					services = new ArrayList<String>();
					authorisedServices.put(type, services);
				}
				services.add(team);
				return team;
			}
			return "none";
		}
		public boolean authorisedForService(AuthorisationType type, String service) {
			if (authorisedServices == null || !authorisedServices.containsKey(type)) {
				return false;
			}
			List<String> teams = authorisedServices.get(type);
			return teams.contains(service.toLowerCase());
		}
		
		public List<String> getAuthorisedServices(AuthorisationType type) {
			if (authorisedServices == null || !authorisedServices.containsKey(type)) return Collections.emptyList();
			return authorisedServices.get(type);
		}
		public String getAuthorisedServicesList(AuthorisationType ucd) {
			List<String> services = getAuthorisedServices(ucd);
			return ArrayUtils.toString(services.toArray(), "Null");
		} 
	}
	public static class Token  implements Serializable {
		private static final long serialVersionUID = 1L;
		private String email;
		private long expiration;
		private int attempt;
		private String token;
		
		
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public long getExpiration() {
			return expiration;
		}
		public void setExpiration(long expiration) {
			this.expiration = expiration;
		}
		public int getAttempts() {
			return attempt;
		}
		public void setAttempts(int attempt) {
			this.attempt = attempt;
		}
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
	}
	public static class BGUserDetails implements UserDetails {

		private Token token;
		private Collection<? extends GrantedAuthority> authories;
		
		BGUserDetails(Token token) {
			 this.token = token;
			 this.authories = Collections.singleton(new GrantedAuthority() {
				@Override
				public String getAuthority() {
					return "default";					
				}
			});
		}
		
		
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return authories;
		}

		@Override
		public String getPassword() {
			return token.getToken();
		}

		@Override
		public String getUsername() {
			return token.getEmail();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return "DISABLED".equals(token);
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return System.currentTimeMillis() > token.getExpiration();
		}

		@Override
		public boolean isEnabled() {
			return "DISABLED".equals(token);
		}
	}
	
	public static enum AuthorisationType {
		UCD,
		BLUEMIX_ID
	}
}
