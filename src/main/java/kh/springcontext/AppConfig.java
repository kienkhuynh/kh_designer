package kh.springcontext;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Specification of where spring can start to scan for beans
 * 
 * 
 * @author kien
 */
@Configuration 
@ComponentScan({"kh.*" })
@Import({ SecurityConfig.class })
public class AppConfig {

}