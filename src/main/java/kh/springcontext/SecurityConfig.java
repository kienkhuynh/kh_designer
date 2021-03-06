package kh.springcontext; 
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Configuration for security/authentication manager
 * 
 * @author kien
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	static Logger log = Logger.getLogger(SecurityConfig.class);
	
	public SecurityConfig() {
		log.info(this.getClass() + " instantiated.");
	}
	
	@Autowired AuthenticationService authenticationService;
	
	@Autowired
	public AuthenticationService userDetailsService() {
		return authenticationService;
	}
	
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { 
		auth.userDetailsService(userDetailsService());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/js/*");
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.rememberMe().
		and().
		csrf().disable()
			.authorizeRequests()
			.antMatchers("/js/**", "/skins/**").permitAll()
			.antMatchers("/admin/**", "/editor/**", "/ops/**").hasAuthority("admin")
			.antMatchers("/editor/**", "/ops/**").hasAuthority("editor")
			.antMatchers("/ops/**").hasAuthority("ops")
			.antMatchers("/api/**", "/services/**").permitAll();
	}
}