# UniApp-JDBC: University Application Management System

## Academic Project - Database Interoperability via JDBC

This is an **academic project** focusing on Java Database Connectivity (JDBC) and database interoperability patterns.

---

## Overview

UniApp-JDBC is a comprehensive Java application that demonstrates best practices for database interoperability using JDBC. It implements a complete data access layer for a university application management system, featuring secure database operations, parameterized SQL queries, and object-oriented data modeling.

The project serves as an educational resource for understanding:
- **JDBC Connection Management**: Establishing and managing database connections
- **Data Access Objects (DAO) Pattern**: Separating business logic from database operations
- **SQL Injection Prevention**: Using parameterized prepared statements for security
- **Object-Relational Mapping**: Mapping database records to Java model classes
- **Database Transactions**: Managing complex multi-table operations

---

## Features

✅ **Complete JDBC Implementation**
- Connection pooling and management via `ConnectionManager`
- Prepared statements for all SQL operations to prevent SQL injection

✅ **Data Access Objects (DAO)**
- `UserDao` - Base user management
- `ApplicantDao` - Applicant-specific operations
- `ReviewerDao` - Reviewer management
- `RatingDao` - Rating management with update operations

✅ **Model Classes with Inheritance**
- `User` - Base user entity
- `Applicant` - User subtype with program specialization
- `Reviewer` - User subtype with review capabilities
- `Rating` - Application ratings

✅ **Security Features**
- SQL injection prevention through parameterized queries
- Secure credential management via environment variables

✅ **Complete CRUD Operations**
- Create, Read, Update, and Delete operations for all entities
- Complex queries (filtering by program, retrieving by ID, etc.)

---

## Project Requirements

This academic project implements the following requirements:

### Model Classes
- **User**: Base user model with username, firstName, lastName, and email
- **Applicant**: Extends User with program selection and essay
- **Reviewer**: Extends User with review program specialization
- **Rating**: Stores ratings between reviewers and applicants

### Data Access Classes
- **UserDao**: Static methods for user creation and deletion
- **ApplicantDao**: Full CRUD operations for applicants, filtering by program
- **ReviewerDao**: Creation and retrieval of reviewer records
- **RatingDao**: Rating creation, retrieval, and updates

### Driver Application
- Demonstrates all CRUD operations
- Re-creates database schema on startup
- Exercises all data access methods

---

## Architecture

```
src/main/java/HW8_Applications/
├── model/                    # Data model classes
│   ├── User.java            # Base user class
│   ├── Applicant.java       # Applicant with Program enum
│   ├── Reviewer.java        # Reviewer with Program enum
│   └── Rating.java          # Rating entity
├── dal/                      # Data Access Layer
│   ├── UserDao.java         # User operations
│   ├── ApplicantDao.java    # Applicant operations
│   ├── ReviewerDao.java     # Reviewer operations
│   ├── RatingDao.java       # Rating operations
│   └── Utils.java           # Utility functions
├── Driver.java              # Main application demonstrating all operations
└── ConnectionManager.java   # Database connection management
```

---

## Prerequisites

- **Java Development Kit (JDK)**: Version 11 or higher
- **MySQL Server**: Running on localhost:3306
- **Gradle**: For building and running the project (included via gradle wrapper)
- **MySQL JDBC Driver**: Automatically managed by Gradle

### Environment Configuration

Set the following environment variable before running:
```bash
export CS5200_MYSQL_PASSWORD=your_mysql_password
```

The application connects to:
- **Host**: localhost
- **Port**: 3306
- **User**: root
- **Database**: cs5200

---

## Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/UniApp-JDBC.git
cd UniApp-JDBC
```

### 2. Configure MySQL Database
Ensure MySQL is running and create the database (Driver will auto-create tables):
```bash
mysql -u root -p
mysql> CREATE DATABASE cs5200;
```

### 3. Set Environment Variable
```bash
export CS5200_MYSQL_PASSWORD=your_root_password
```

### 4. Build the Project
```bash
./gradlew clean build
```

---

## Usage

### Running the Application

Execute the main Driver application to demonstrate all CRUD operations:

```bash
./gradlew run
```

This will:
1. Connect to the MySQL database
2. Delete existing schema (if present)
3. Create fresh tables from SQL definitions
4. Demonstrate all CRUD operations:
   - Create users, applicants, and reviewers
   - Retrieve records by ID and filter criteria
   - Update ratings
   - Delete records with cascading deletes

### Example Output
The application will demonstrate:
- Creating applicants for different programs (CS, EE, etc.)
- Creating reviewers with specializations
- Adding ratings
- Retrieving applicants by program
- Updating ratings
- Deleting records with proper cascading

---

## Security Considerations

### SQL Injection Prevention
All database operations use **parameterized prepared statements** to prevent SQL injection attacks:

```java
// ✅ SECURE - Uses parameterized query
String query = "INSERT INTO User (username, firstName, lastName, email) VALUES (?, ?, ?, ?)";
PreparedStatement pstmt = cxn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
pstmt.setString(1, username);
pstmt.setString(2, firstName);
// ...
```

### Credential Management
Database passwords are managed securely:
- Read from environment variables (`CS5200_MYSQL_PASSWORD`)
- Never hardcoded in source files
- Connection details centralized in `ConnectionManager`

---

## Project Structure & Key Classes

### Model Classes (package: `HW8_Applications.model`)
Each model class represents a database entity with getters, setters, and appropriate constructors.

**Applicant.java**:
```java
public class Applicant extends User {
    public enum Program { CS, EE, BIO, ... }
    private Program program;
    private String essay;
    // getters, setters, constructors
}
```

### Data Access Layer (package: `HW8_Applications.dal`)
Implements the DAO pattern for type-safe database operations.

**ApplicantDao.java** - Key methods:
- `create(Connection, String username, ..., Program program, String essay)`
- `getApplicantByUserID(Connection, int userID)`
- `getApplicantsByProgram(Connection, Program program)`
- `delete(Connection, Applicant applicant)`

### Connection Management
**ConnectionManager.java**: Centralized database connection configuration and management.

---

## Database Schema

The project uses the following main tables:

```sql
CREATE TABLE User (
    userID INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    email VARCHAR(255)
);

CREATE TABLE Applicant (
    applicantID INT PRIMARY KEY,
    program VARCHAR(50),
    essay TEXT,
    FOREIGN KEY (applicantID) REFERENCES User(userID)
);

CREATE TABLE Reviewer (
    reviewerID INT PRIMARY KEY,
    program VARCHAR(50),
    FOREIGN KEY (reviewerID) REFERENCES User(userID)
);

CREATE TABLE Rating (
    ratingID INT AUTO_INCREMENT PRIMARY KEY,
    reviewerID INT,
    applicantID INT,
    rating INT,
    FOREIGN KEY (reviewerID) REFERENCES Reviewer(reviewerID),
    FOREIGN KEY (applicantID) REFERENCES Applicant(applicantID)
);
```

---

## Learning Outcomes

This academic project demonstrates proficiency in:

1. **JDBC Fundamentals**
   - Connection management
   - SQL execution
   - Result set processing

2. **Database Design**
   - Inheritance mapping (single-table, multi-table inheritance)
   - Foreign key relationships
   - Data integrity

3. **Software Architecture**
   - DAO (Data Access Object) design pattern
   - Separation of concerns
   - Reusable component design

4. **Security**
   - SQL injection prevention
   - Parameterized queries
   - Secure credential handling

5. **Java Best Practices**
   - Exception handling
   - Resource management (try-with-resources)
   - Enum usage for domain values

---

## Troubleshooting

### "Connection refused" Error
Ensure MySQL is running:
```bash
mysql -u root -p -e "SELECT 1;"
```

### "Unknown database 'cs5200'" Error
The application will create the database automatically on first run, or create it manually:
```bash
mysql -u root -p -e "CREATE DATABASE cs5200;"
```

### "Access denied for user 'root'" Error
Check that:
1. MySQL root password is set correctly
2. Environment variable `CS5200_MYSQL_PASSWORD` is properly configured
3. MySQL is using port 3306

---

## Academic Integrity

This project is provided as an educational resource for learning JDBC and database design patterns. When referencing this work:

- Acknowledge the original assignment requirements from CS5200
- Do not submit this code as your own in coursework
- Use it as a learning reference or starting point for your own implementations

---

## Acknowledgments

This academic project demonstrates best practices in JDBC database interoperability and the Data Access Object (DAO) design pattern.

---

## License

This academic project is provided for educational purposes. See LICENSE file for details.

---

## Contact & Support

For questions about this academic implementation or to report issues, please open an issue on the GitHub repository.

**Note**: This is an academic project and is not intended for production use. It serves as an educational example of JDBC database interoperability.
