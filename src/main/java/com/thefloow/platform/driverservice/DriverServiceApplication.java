package com.thefloow.platform.driverservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A simple REST service application that manages details of drivers for use within a hypothetical insurance service
 */
@SpringBootApplication()
public class DriverServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(DriverServiceApplication.class, args);
	}
}
