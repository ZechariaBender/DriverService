package com.thefloow.platform.driverservice.repositories;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.thefloow.platform.driverservice.model.Driver;
import io.jsondb.JsonDBTemplate;

public class FlatFileDriverRepository extends JsonDBTemplate implements DriverRepository {

	public FlatFileDriverRepository(String dbFilesLocationString, String baseScanPackage) {

		super(dbFilesLocationString, baseScanPackage);
	}

	@Override
	public <S extends Driver> S save(S s) {

		super.upsert(s);
		return s;
	}

	@Override
	public <S extends Driver> Iterable<S> saveAll(Iterable<S> iterable) {

		super.upsert(((Collection<S>) iterable), Driver.class);
		return iterable;
	}

	@Override
	public Optional<Driver> findById(UUID uuid) {

		return Optional.ofNullable(super.findById(uuid, Driver.class));
	}

	@Override
	public boolean existsById(UUID uuid) {

		return findById(uuid).isPresent();
	}

	@Override
	public Iterable<Driver> findAll() {

		return super.findAll(Driver.class);
	}

	@Override
	public Iterable<Driver> findAllById(Iterable<UUID> iterable) {

		Collection<Driver> drivers = new ArrayList<>();
		iterable.forEach(uuid -> findById(uuid).ifPresent(drivers::add));
		return drivers;
	}

	@Override
	public long count() {

		return super.findAll(Driver.class).size();
	}

	@Override
	public void deleteById(UUID uuid) {

		final Optional<Driver> driver = findById(uuid);
		driver.ifPresent(value -> super.remove(value, Driver.class));
	}

	@Override
	public void delete(Driver driver) {

		super.remove(driver, Driver.class);
	}

	@Override
	public void deleteAll(Iterable<? extends Driver> iterable) {

		super.remove(iterable, Driver.class);
	}

	@Override
	public void deleteAll() {

		super.remove(findAll(Driver.class), Driver.class);
	}

	@Override
	public Iterable<Driver> findAllCreatedAfterDate(Instant i) {

		return ((Collection<Driver>) findAll()).stream()
			.filter(driver -> driver.getCreationDate()
				.isAfter(i))
			.collect(Collectors.toList());
	}
}
