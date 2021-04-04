package com.thefloow.platform.driverservice.model;

public class DriverExistsResponse extends ResourceResponse<Driver> {

	public DriverExistsResponse(Driver driver) {

		super("ERROR - Driver already exists in records", driver);
	}

	public Driver getDriver() {

		return resource;
	}
}
