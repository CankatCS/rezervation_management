
# Reservation Management App

Welcome to the **Reservation Management App** – a robust, scalable solution designed for managing reservations across multiple companies and users. Whether it's scheduling resources or handling bookings, this application makes the reservation process seamless and efficient, equipped with essential features to support complex business needs.

## Features

- **Multi-Company & Multi-User Support**: 
  - Our application can manage reservations for multiple companies, each with their own set of employees, making it ideal for organizations of all sizes.
  - Assign specific roles and permissions to users within each company, allowing for a fully customized reservation experience.

- **Advanced Reservation Controls**:
  - **Time Frame Management**: Set specific reservation periods (e.g., 2 weeks) to control and optimize booking schedules.
  - **Conflict Detection**: Automated detection and management of conflicting reservations prevent double-booking, keeping schedules clear and organized.

- **Exception Handling & Validation**:
  - Comprehensive exception handling during the reservation process ensures that users are alerted to any scheduling conflicts or invalid entries.
  - Built-in checks for reservation duration, availability, and user permissions reduce errors and enhance reliability.

- **Data Persistence & Security**:
  - All reservation data is securely stored, ensuring that information is saved and accessible even after unexpected system interruptions.
  - Data privacy measures are built-in to maintain the confidentiality of each company’s reservation details.

- **Unit Tests**:
  - Includes comprehensive unit tests to verify the correctness of each module and ensure stability across updates.

## Technologies Used

- **Java**
- **Spring Boot**
- **JUnit** (for unit testing)
- **Database**: (Specify your chosen database, e.g., MySQL, PostgreSQL)
- **Maven** (for dependency management)

### Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Test (includes JUnit for testing)
- (Add any other dependencies here as needed)

## Prerequisites

- **Java** (JDK 11 or higher recommended)
- **Maven**
- **Database Setup**: Ensure you have a supported database installed and configured locally or in your environment.

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/CankatCS/rezervation_management.git
cd rezervation_management
```

### Build the Project

Compile the project using Maven:

```bash
mvn clean install
```

### Configure Database

Edit the `application.properties` file to connect your database:

```properties
spring.datasource.url=jdbc:<your-database-url>
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
```

### Run the Application

After configuring the database, start the application:

```bash
mvn spring-boot:run
```

The app will be available at `http://localhost:8080`.

## Usage

- **Creating and Managing Reservations**:
  - Users can create new reservations for various resources, specifying details such as dates, times, and duration.
  - The system automatically validates the requested reservation period, only allowing bookings within the permitted 2-week window.
  - Users receive immediate feedback if a reservation conflicts with an existing one.

- **Updating and Canceling Reservations**:
  - Modify or cancel reservations as needed, with all changes securely saved to the database.
  - The app tracks reservation history, allowing administrators to review changes over time.

## Exception Handling

- **Error Alerts**: The app’s advanced exception handling captures any errors during reservation creation, providing clear messages to guide users in resolving issues.
- **Automated Rechecks**: The system verifies all inputs and reservation conditions before saving, preventing invalid or conflicting reservations.

## Contributing

Please feel free to fork this repository, submit issues, or open pull requests. Collaborative improvements and new features are always appreciated.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

For inquiries, collaboration opportunities, or feedback, contact [Cankat Sezer](mailto:cankatsezer55@gmail.com).
