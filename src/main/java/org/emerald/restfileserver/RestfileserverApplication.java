package org.emerald.restfileserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties()
@SpringBootApplication
public class RestfileserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestfileserverApplication.class, args);
	}

}
