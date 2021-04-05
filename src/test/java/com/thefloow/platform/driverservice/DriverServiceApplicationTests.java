package com.thefloow.platform.driverservice;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import com.thefloow.platform.driverservice.controllers.DriverController;
import com.thefloow.platform.driverservice.model.CreateDriverRequest;
import com.thefloow.platform.driverservice.model.Driver;
import com.thefloow.platform.driverservice.model.DriverCreatedResponse;
import com.thefloow.platform.driverservice.model.DriverExistsResponse;
import com.thefloow.platform.driverservice.repositories.DriverRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = { "storage-location=src/test/resources/test-storage" })
@ActiveProfiles("default")
public class DriverServiceApplicationTests {

	@Autowired
	private DriverRepository driverRepository;

	@Autowired
	private DriverController driverController;

	@Test
	public void endToEndTest() {

		String firstName1 = "John";
		String lastName1 = "Smith";
		LocalDate dateOfBirth1 = LocalDate.parse("1980-05-01");
		Driver driver1 = new Driver(firstName1, lastName1, dateOfBirth1);

		String firstName2 = "Alice";
		String lastName2 = "White";
		LocalDate dateOfBirth2 = LocalDate.parse("2003-11-24");
		Driver driver2 = new Driver(firstName2, lastName2, dateOfBirth2);

		assertNewDriverCreatedResponse(new CreateDriverRequest(firstName1, lastName1, dateOfBirth1), driver1);

		assertListOfDriversResponse(List.of(driver1));

		assertDriverExistsResponse(new CreateDriverRequest(firstName1, lastName1, dateOfBirth1), driver1);

		assertNewDriverCreatedResponse(new CreateDriverRequest(firstName2, lastName2, dateOfBirth2), driver2);

		assertListOfDriversResponse(List.of(driver1, driver2));

		clearStorage();
	}

	private void assertNewDriverCreatedResponse(CreateDriverRequest createDriverRequest, Driver driver) {

		assertThat(driverController.createDriver(createDriverRequest)
			.getBody()).satisfies(resourceResponse -> {
				assertThat(resourceResponse.getMessage()).isEqualTo("SUCCESS - Created new driver record");
				assertThat(resourceResponse.getTimestamp()).isAfterOrEqualTo(Instant.now()
					.minusSeconds(1));
				assertThat(resourceResponse).isExactlyInstanceOf(DriverCreatedResponse.class);
				assertThat(((DriverCreatedResponse) resourceResponse).getDriver()).isEqualTo(driver);
			});
	}

	private void assertListOfDriversResponse(List<Driver> driver12) {

		assertThat(driverController.getDrivers()
			.getBody()).satisfies(driversList -> assertThat(driversList).isEqualTo(driver12));
	}

	private void assertDriverExistsResponse(CreateDriverRequest createDriverRequest, Driver driver) {

		assertThat(driverController.createDriver(createDriverRequest)
			.getBody()).satisfies(resourceResponse -> {
				assertThat(resourceResponse.getMessage()).isEqualTo("ERROR - Driver already exists in records");
				assertThat(resourceResponse).isExactlyInstanceOf(DriverExistsResponse.class);
				assertThat(((DriverExistsResponse) resourceResponse).getDriver()).isEqualTo(driver);
			});
	}

	private void clearStorage() {

		driverRepository.deleteAll();
	}

}
