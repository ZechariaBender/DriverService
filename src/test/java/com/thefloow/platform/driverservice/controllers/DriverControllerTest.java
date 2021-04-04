package com.thefloow.platform.driverservice.controllers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Validation;
import javax.validation.Validator;

import com.thefloow.platform.driverservice.model.CreateDriverRequest;
import com.thefloow.platform.driverservice.model.Driver;
import com.thefloow.platform.driverservice.model.DriverCreatedResponse;
import com.thefloow.platform.driverservice.model.DriverExistsResponse;
import com.thefloow.platform.driverservice.model.ResourceResponse;
import com.thefloow.platform.driverservice.repositories.DriverRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class DriverControllerTest {

	@Mock
	DriverRepository driverRepository;
	DriverController driverController;
	private String firstName = "John";
	private String lastName = "Smith";
	private LocalDate dateOfBirth = LocalDate.parse("1980-05-01");
	private Driver driver = new Driver(firstName, lastName, dateOfBirth);
	@Spy
	private Validator validator = Validation.buildDefaultValidatorFactory()
		.getValidator();

	public DriverControllerTest() {

	}

	@Before
	public void setUp() {

		MockitoAnnotations.openMocks(this);
		driverController = new DriverController(driverRepository);
	}

	private void initForCreateDriver(Optional<Driver> optionalDriver) {

		Mockito.when(driverRepository.findById(driver.getId()))
			.thenReturn(optionalDriver);
		Mockito.when(driverRepository.save(driver))
			.thenReturn(driver);
	}

	@Test
	public void createDriverPositiveTest() {

		initForCreateDriver(Optional.empty());
		CreateDriverRequest createDriverRequest = new CreateDriverRequest(firstName, lastName, dateOfBirth);
		ResponseEntity<ResourceResponse<Driver>> responseEntity = driverController.createDriver(createDriverRequest);
		assertThat(responseEntity.getBody()).satisfies(resourceResponse -> {
			assertThat(resourceResponse.getMessage()).isEqualTo("SUCCESS - Created new driver record");
			assertThat(resourceResponse.getTimestamp()).isAfterOrEqualTo(Instant.now()
				.minusSeconds(1));
			assertThat(resourceResponse).isExactlyInstanceOf(DriverCreatedResponse.class);
			assertThat(((DriverCreatedResponse) resourceResponse).getDriver()).satisfies(d -> {
				assertThat(d.getFirstName()).isEqualTo(firstName);
				assertThat(d.getLastName()).isEqualTo(lastName);
				assertThat(d.getDateOfBirth()).isEqualTo(dateOfBirth);
				assertThat(d).isEqualTo(driver);
			});
		});
	}

	@Test
	public void createDriverAlreadyExistsTest() {

		initForCreateDriver(Optional.of(driver));
		CreateDriverRequest createDriverRequest = new CreateDriverRequest(firstName, lastName, dateOfBirth);
		ResponseEntity<ResourceResponse<Driver>> responseEntity = driverController.createDriver(createDriverRequest);
		assertThat(responseEntity).isNotNull();
		assertThat(responseEntity.getBody()).satisfies(resourceResponse -> {
			assertThat(resourceResponse.getMessage()).isEqualTo("ERROR - Driver already exists in records");
			assertThat(resourceResponse.getTimestamp()).isAfterOrEqualTo(Instant.now()
				.minusSeconds(1));
			assertThat(resourceResponse).isExactlyInstanceOf(DriverExistsResponse.class);
			assertThat(((DriverExistsResponse) resourceResponse).getDriver()).satisfies(d -> {
				assertThat(d.getFirstName()).isEqualTo(firstName);
				assertThat(d.getLastName()).isEqualTo(lastName);
				assertThat(d.getDateOfBirth()).isEqualTo(dateOfBirth);
				assertThat(d).isEqualTo(driver);
			});
		});
	}

	@Test
	public void createDriverFutureDateOfBirthTest() throws NoSuchMethodException {

		LocalDate futureDate = LocalDate.parse("2918-05-01");
		driver = new Driver(firstName, lastName, futureDate);
		initForCreateDriver(Optional.empty());
		CreateDriverRequest createDriverRequest = new CreateDriverRequest(firstName, lastName, futureDate);
		System.out.println(validator.forExecutables()
			.validateParameters(driverController, DriverController.class.getMethod("createDriver",
				CreateDriverRequest.class), new Object[] { createDriverRequest }));
		assertThat(validator.forExecutables()
			.validateParameters(driverController, DriverController.class.getMethod("createDriver",
				CreateDriverRequest.class), new Object[] { createDriverRequest })).hasSize(1)
					.allSatisfy(driverControllerConstraintViolation -> assertThat(driverControllerConstraintViolation
						.getMessage()).isEqualTo("must be a past date"));
	}

	@Test(expected = DateTimeParseException.class)
	public void createDriverWrongDateFormatTest() {

		driver.setDateOfBirth(LocalDate.parse("05-01-1980"));
		initForCreateDriver(Optional.of(driver));
		CreateDriverRequest createDriverRequest = new CreateDriverRequest(firstName, lastName, dateOfBirth);
		assertThatExceptionOfType(DateTimeParseException.class).isThrownBy(() -> driverController.createDriver(
			createDriverRequest))
			.withMessage("java.time.format.DateTimeParseException: Text '05-01-1980' could not be parsed at index 0");
	}

	private void initForGetDrivers(Collection<Driver> drivers) {

		Mockito.when(driverRepository.findAll())
			.thenReturn(drivers);
	}

	@Test
	public void getDriversTest() {

		List<Driver> drivers = List.of();
		initForGetDrivers(drivers);
		ResponseEntity<Collection<Driver>> responseEntity = driverController.getDrivers();
		List<Driver> finalDrivers = drivers;
		assertThat(responseEntity.getBody()).satisfies(driversList -> assertThat(driversList).isEqualTo(finalDrivers));

		drivers = List.of(driver);
		initForGetDrivers(drivers);
		responseEntity = driverController.getDrivers();
		List<Driver> finalDrivers1 = drivers;
		assertThat(responseEntity.getBody()).satisfies(driversList -> assertThat(driversList).isEqualTo(finalDrivers1));

		drivers = List.of(
			new Driver("Bob", "Black", LocalDate.parse("1969-03-12")),
			new Driver("Alice", "White", LocalDate.parse("2003-11-24")));
		initForGetDrivers(drivers);
		responseEntity = driverController.getDrivers();
		List<Driver> finalDrivers2 = drivers;
		assertThat(responseEntity.getBody()).satisfies(driversList -> assertThat(driversList).isEqualTo(finalDrivers2));
	}

	private void initForGetDriversByDate(Collection<Driver> drivers, Instant i) {

		Mockito.when(driverRepository.findAllCreatedAfterDate(i))
			.thenReturn(drivers.stream()
				.filter(driver -> driver.getCreationDate()
					.isAfter(i))
				.collect(Collectors.toList()));
	}

	@Test
	public void getDriversByDate() {

		Driver oldDriver = new Driver("Bob", "Black", LocalDate.parse("1969-03-12"));
		Driver newDriver = new Driver("Alice", "White", LocalDate.parse("2003-11-24"));
		LocalDate referenceDate = LocalDate.parse("2000-01-01");
		oldDriver.setCreationDate(Instant.from(referenceDate.minusYears(10)
			.atStartOfDay(ZoneId.systemDefault())));
		List<Driver> drivers = List.of(oldDriver, newDriver);
		Instant i = Instant.from(referenceDate.atStartOfDay(ZoneId.systemDefault()));
		Mockito.when(driverRepository.findAllCreatedAfterDate(i))
				.thenReturn(drivers.stream()
						.filter(driver -> driver.getCreationDate()
								.isAfter(i))
						.collect(Collectors.toList()));
		ResponseEntity<Collection<Driver>> responseEntity = driverController.getDriversByDate(referenceDate);
		assertThat(responseEntity.getBody()).satisfies(driversList -> assertThat(driversList).isEqualTo(List.of(newDriver)));
	}
}
