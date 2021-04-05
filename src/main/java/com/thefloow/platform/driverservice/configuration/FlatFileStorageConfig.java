package com.thefloow.platform.driverservice.configuration;

import com.thefloow.platform.driverservice.model.Driver;
import com.thefloow.platform.driverservice.repositories.DriverRepository;
import com.thefloow.platform.driverservice.repositories.FlatFileDriverRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("flat-file-storage")
@Configuration
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class FlatFileStorageConfig {

	@Bean
	DriverRepository getFlatFileDriverRepository(@Value("${storage-location}") String storageLocation) {

		FlatFileDriverRepository driverRepository = new FlatFileDriverRepository(storageLocation + "/flat-file", Driver.class
			.getPackageName());
		if (!driverRepository.collectionExists(Driver.class)) {
			driverRepository.createCollection(Driver.class);
		}
		return driverRepository;
	}
}
