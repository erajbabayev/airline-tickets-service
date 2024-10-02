Airline Ticket Reservation Service

This is a Spring Boot-based Airline Ticket Reservation Service, which includes APIs for holding, reserving, and managing seats in various levels. The application is built using Maven as the build tool.

Prerequisites

Ensure you have the following installed:

	•	Java 17
	•	Maven 3.2.10

Build Instructions

To build the project, navigate to the root directory of the project and run the following command in your terminal:

mvn clean install

This will:

	1.	Clean the previous builds (if any).
	2.	Compile the source code.
	3.	Package the code into a JAR file.

The packaged application will be located in the target folder as a .jar file.

Running the Application

To start the application after building, use the following command:

mvn spring-boot:run

This will start the Spring Boot application, and the server will be available at http://localhost:8080.

Using H2 and Flyway

The project uses an H2 in-memory database for easier testing and development. Flyway is used for database migrations. Upon running the application, Flyway automatically creates the required tables and inserts some default data for easier testing.

Running the Tests

To execute the unit tests, use the following command:

mvn test

This will run all the tests in the project, including unit tests and integration tests.

API Documentation

Once the application is running, Swagger UI is available at:

http://localhost:8080/swagger-ui/index.html

Use this to explore and test the APIs directly from the browser.

Summary

This project demonstrates a Spring Boot-based solution for managing airline seat reservations using H2 for the database and Flyway for schema management and data seeding. It includes REST APIs for creating and managing reservations with unit tests for coverage.