package com.thefloow.platform.driverservice.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.f4b6a3.uuid.UuidCreator;
import io.jsondb.annotation.Document;

@Entity
@Document(collection = "drivers", schemaVersion = "1.0")
public class Driver {

	/**
	 * A unique ID for the driver
	 */
	@io.jsondb.annotation.Id
	@javax.persistence.Id
	private UUID id;

	/**
	 * First Name
	 */
	@JsonProperty("firstname")
	private String firstName;

	/**
	 * Last Name
	 */
	@JsonProperty("lastname")
	private String lastName;

	/**
	 * Date of Birth (YYYY-MM-DD format)
	 */
	@JsonProperty("date_of_birth")
	private LocalDate dateOfBirth;

	private Instant creationDate;

	public Driver() {

	}

	public Driver(String firstName, String lastName, LocalDate dateOfBirth) {

		// The reasonable assumption is that if 2 drivers have the same name and birthday - then they are the same driver.
		this.id = UuidCreator.getNameBasedSha1(firstName + "&" + lastName + "&" + dateOfBirth.toString());
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.creationDate = Instant.now();
	}

	public UUID getId() {

		return id;
	}

	public void setId(UUID id) {

		this.id = id;
	}

	public String getFirstName() {

		return firstName;
	}

	public void setFirstName(String firstName) {

		this.firstName = firstName;
	}

	public String getLastName() {

		return lastName;
	}

	public void setLastName(String lastName) {

		this.lastName = lastName;
	}

	public LocalDate getDateOfBirth() {

		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {

		this.dateOfBirth = dateOfBirth;
	}

	public Instant getCreationDate() {

		return creationDate;
	}

	public void setCreationDate(Instant creationDate) {

		this.creationDate = creationDate;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Driver driver = (Driver) o;
		return Objects.equals(id, driver.id) &&
				Objects.equals(firstName, driver.firstName) &&
				Objects.equals(lastName, driver.lastName) &&
				Objects.equals(dateOfBirth, driver.dateOfBirth);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id, firstName, lastName, dateOfBirth);
	}

	@Override
	public String toString() {

		return "Driver{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", dateOfBirth=" + dateOfBirth +
				", creationDate=" + creationDate +
				'}';
	}
}
