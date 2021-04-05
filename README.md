# DriverService
A simple REST service application that manages details of drivers for use within a hypothetical insurance service.
A driver record consists of the following information:
* A unique ID for the driver
* First Name
* Last Name
* Date of Birth
* Creation Date

## Building the Project

### Java Version: 11
This project compiles with Java version 11 or higher.

### Build with Maven
This project is compiled with the Maven build tool, this can be done via any Java IDE
or by running `mvn clean install` in the command line under the project directory:
```
$ mvn clean install
```

## Starting the DriverService Application
Start the application by running the [DriverServiceApplication.java](src/main/java/com/thefloow/platform/driverservice/DriverServiceApplication.java)
class the IDE or by running this maven command in the command line:
```
$ mvn spring-boot:run
```

## Data Persistence 

The data persistence method of his application can be configured to one of two options:
*   flat file storage - where the driver details are stored in a simple JSON file
*   database storage - for a real-world use case where the data is stored in a database

These options can be configured using [Spring Boot Profiles](https://docs.spring.io/spring-boot/docs/1.2.0.M1/reference/html/boot-features-profiles.html).

### Flat File Storage
If the 'flat-file-storage' profile is active in the [`application.properties`](src/main/resources/application.properties) file:
```properties
spring.profiles.active=flat-file-storage
```
then the data persistence method will be flat file storage.
The storage file, [`drivers.json`](src/main/resources/storage/flat-file/drivers.json)
will located under the `src/main/resources/storage/flat-file` directory.

### Database Storage
If the 'flat-file-storage' profile is **not** active in the [`application.properties`](src/main/resources/application.properties) file,
then the data persistence method will be database storage.
This can be achieved by commenting out the `spring.profiles.active=flat-file-storage` line:
```properties
#spring.profiles.active=flat-file-storage
```
or setting spring.profiles.active to 'default':
```properties
spring.profiles.active=default
```
In case of database storage, the persistence is abstracted with Spring Data JPA which is compatible with a large variety of database vendors.
The out-of-the-box implementation is with the H2 in-memory database, persisted to a db file.
The db file, `driver-service-h2.mv.db`, will located under the `src/main/resources/storage/h2` directory.
The database vendor and properties can be changed in the [`application.properties`](src/main/resources/application.properties) file.
