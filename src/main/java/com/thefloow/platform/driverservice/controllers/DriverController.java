package com.thefloow.platform.driverservice.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.PastOrPresent;

import com.thefloow.platform.driverservice.model.CreateDriverRequest;
import com.thefloow.platform.driverservice.model.Driver;
import com.thefloow.platform.driverservice.model.DriverCreatedResponse;
import com.thefloow.platform.driverservice.model.DriverExistsResponse;
import com.thefloow.platform.driverservice.model.ResourceResponse;
import com.thefloow.platform.driverservice.repositories.DriverRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class DriverController {

	private static final Logger logger = LoggerFactory.getLogger(DriverController.class);

	private final DriverRepository driverRepository;

	public DriverController(DriverRepository driverRepository) {

		this.driverRepository = driverRepository;
	}

	@PostMapping(value = "/driver/create", consumes = { "application/json" }, produces = "application/json")
	public ResponseEntity<ResourceResponse<Driver>> createDriver(@Valid @RequestBody CreateDriverRequest createDriverRequest) {

		logger.info("Incoming POST request: method=createDriver request-body='{}'", createDriverRequest);
		Driver driver = new Driver(createDriverRequest.getFirstName(), createDriverRequest.getLastName(), createDriverRequest
			.getDateOfBirth());
		Optional<Driver> optionalDriver = driverRepository.findById(driver.getId());
		if (optionalDriver.isPresent()) {
			Driver existingDriver = optionalDriver.get();
			logger.error("Driver already exists: '{}'", existingDriver);
			return new ResponseEntity<>(new DriverExistsResponse(existingDriver), HttpStatus.CONFLICT);
		}
		Driver savedDriver = driverRepository.save(driver);
		logger.info("New driver created: '{}'", savedDriver);
		return new ResponseEntity<>(new DriverCreatedResponse(savedDriver), HttpStatus.CREATED);
	}

	@GetMapping(value = "/drivers", produces = { "application/json" })
	public ResponseEntity<Collection<Driver>> getDrivers() {

		logger.info("Incoming GET request: method=getDrivers");
		return new ResponseEntity<>(((Collection<Driver>) driverRepository.findAll()), HttpStatus.OK);
	}

	@GetMapping(value = "/drivers/byDate", produces = { "application/json" })
	public ResponseEntity<Collection<Driver>> getDriversByDate(
			@RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PastOrPresent LocalDate date) {

		logger.info("Incoming GET request: method=getDriversByDate request-param='{}'", date);
		return new ResponseEntity<>(((Collection<Driver>) driverRepository.findAllCreatedAfterDate(date.atStartOfDay(ZoneId
			.systemDefault())
			.toInstant())), HttpStatus.OK);
	}

	// if validation constraints are violated, label error as 400 - Bad Request
	// instead of less appropriate 500 - Internal Server Error
	@ExceptionHandler(ConstraintViolationException.class)
	void labelAsBadRequest(HttpServletResponse response) throws IOException {

		response.sendError(HttpStatus.BAD_REQUEST.value());
	}
}
