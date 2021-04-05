package com.thefloow.platform.driverservice.repositories;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.thefloow.platform.driverservice.model.Driver;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * A CrudRepository for Driver objects.
 * If Spring Data JPA is enabled it will automatically implement this interface.
 */
public interface DriverRepository extends CrudRepository<Driver, UUID> {

    @Query("SELECT d FROM Driver d WHERE d.creationDate > :instant")
    Iterable<Driver> findAllCreatedAfterDate(@Param("instant") Instant i);
}