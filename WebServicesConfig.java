package com.micda.BankAPI.ServicesConfig;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServicesConfig {

	 @Bean
	 public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context) {
	        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
	        servlet.setApplicationContext(context);
	        servlet.setTransformWsdlLocations(true);
	        return new ServletRegistrationBean<>(servlet, "/ws/*");
	    }
	 @Bean(name = "accounts")
	    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema accountsSchema) {
	        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
	        definition.setPortTypeName("AccountsPort");
	        definition.setLocationUri("/ws");
	        definition.setTargetNamespace("http://banque.com/accounts");
	        definition.setSchema(accountsSchema);
	        return definition;
	    }

	    @Bean
	    public XsdSchema accountsSchema() {
	        return new SimpleXsdSchema(new ClassPathResource("accounts.xsd"));
	    }
}
