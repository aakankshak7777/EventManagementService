 package com.kmbl.eventmanagementservice.Config;

 import org.springframework.context.annotation.Configuration;
 import org.springframework.context.annotation.Import;
 import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

 @Configuration
 @Import({ SetupConfig.class})
 public class ConfigForTests extends WebMvcConfigurationSupport {
 }
