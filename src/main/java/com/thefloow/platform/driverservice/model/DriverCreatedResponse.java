package com.thefloow.platform.driverservice.model;

public class DriverCreatedResponse extends ResourceResponse<Driver> {

	public DriverCreatedResponse(Driver driver) {

		super("SUCCESS - Created new driver record", driver);
	}

	public Driver getDriver() {

		return resource;
	}
}
