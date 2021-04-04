package com.thefloow.platform.driverservice.model;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

public class CreateDriverRequest {

	@NotBlank(message = "First name is mandatory")
	@JsonProperty("firstname")
	private String firstName;

	@NotBlank(message = "Last name is mandatory")
	@JsonProperty("lastname")
	private String lastName;

	@Past
	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@JsonProperty("date_of_birth")
	private LocalDate dateOfBirth;

	public CreateDriverRequest(@NotBlank(message = "First name is mandatory") String firstName,
			@NotBlank(message = "Last name is mandatory") String lastName, @Past @NotNull LocalDate dateOfBirth) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
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

	@Override
	public String toString() {

		return "CreateDriverRequest{" +
				"firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", dateOfBirth=" + dateOfBirth +
				'}';
	}
}
