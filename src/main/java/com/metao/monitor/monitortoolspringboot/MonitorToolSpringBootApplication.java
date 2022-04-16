package com.metao.monitor.monitortoolspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MonitorToolSpringBootApplication {

	// enable this to find blocking calls in the application in debug mode 
	// static {		
	// 	BlockHound.install();
	// }

	public static void main(String[] args) {
		SpringApplication.run(MonitorToolSpringBootApplication.class, args);
	}	

}
