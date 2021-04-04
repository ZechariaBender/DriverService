package com.thefloow.platform.driverservice;

import com.thefloow.platform.driverservice.model.Driver;
import com.thefloow.platform.driverservice.repositories.DriverRepository;
import com.thefloow.platform.driverservice.repositories.FlatFileDriverRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@Profile("flat-file-storage")
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class
})
public class FlatFileDriverServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(FlatFileDriverServiceApplication.class, args);
	}

	@Bean
	DriverRepository getFlatFileDriverRepository(@Value("${storage-location}") String storageLocation) {

		FlatFileDriverRepository driverRepository = new FlatFileDriverRepository(storageLocation + "/flat-file", this.getClass()
			.getPackageName());
		if (!driverRepository.collectionExists(Driver.class)) {
			driverRepository.createCollection(Driver.class);
		}
		return driverRepository;
	}
}
