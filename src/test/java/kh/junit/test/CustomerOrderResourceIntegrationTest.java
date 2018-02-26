package kh.junit.test;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import kh.springcontext.AppConfig;
import kh.springcontext.SecurityConfig;

//@ContextConfiguration(classes = { AppConfig.class, SecurityConfig.class})
//@WebAppConfiguration
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerOrderResourceIntegrationTest {

    @Autowired TestRestTemplate restTemplate;

    public void before() {
    }
    public void getCurrentCustomerOrder() {
    	ResponseEntity<Response> r = restTemplate.getForEntity("/customerorders/current", Response.class);
    }
}