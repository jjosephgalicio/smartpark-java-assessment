# Coding Assessment "SmartPark" - Galicio, John Joseph
**SmartPark - Automated Parking Lot Management System**

SmartPark is a Spring Boot 3 RESTful API developed as part of a technical coding assessment. The system models a real-time, multi-lot vehicle parking platform featuring dynamic space allocation, fee calculation, automated session evictions, JWT-based security, and thorough unit test coverage using JUnit 5 and Mockito.

---

## 🛠️ Architecture & Tech Stack

* **Java Version**: 17+
* **Framework**: Spring Boot 3.2.4
* **Security**: Spring Security + JWT (JSON Web Tokens)
* **Persistence**: Spring Data JPA / Hibernate
* **Database**: H2 In-Memory Database
* **Testing**: JUnit 5, Mockito
* **Build Tool**: Maven

---

## ⚙️ Key Business Rules & Architectural Requirements

1. **JWT Authentication & Authorization**:
  * All functional API routes are protected via custom JwtAuthenticationFilter.
  * Unauthenticated requests return 401 Unauthorized.
  * Token lifetime is configured for 24 hours.

2. **Capacity & Constraint Enforcement**:
  * **Capacity Enforcement**: Prevents check-in requests if occupiedSpaces >= capacity for a given ParkingLot.
  * **Single Active Session Rule**: Ensures a license plate cannot be checked in if it currently holds an active ParkingSession.

3. **Automated 15-Minute Eviction Scheduler**:
  * A background task managed via Spring's @Scheduled annotation executes periodically.
  * Scans active parking sessions exceeding 15 minutes, marks them as inactive, and decrements the corresponding lot's occupied space count.

4. **Dynamic Fee Calculation**:
  * Rates are calculated dynamically at checkout: totalCost = durationInMinutes * ratePerMinute.

---

## 🔑 Assessment Default Credentials & Seed Data

### Default User Credentials
* **Username**: galicio
* **Password**: smartpark

### Seeded Database Reference Data (data.sql)
* **Parking Lots**:
  - LOT-001 — Downtown Center (Capacity: 5 | Rate: $1.50/min)
  - LOT-002 — Airport Terminal 1 (Capacity: 10 | Rate: $2.00/min)
* **Vehicles**:
  - ABC-1234 — Type: CAR (Owner: John Doe)
  - XYZ-9876 — Type: MOTORCYCLE (Owner: Jane Smith)

---

## 🌐 API Route Specification

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| POST | /api/auth/login | Issues JWT Token given valid credentials | No |
| POST | /api/parking/lots | Registers a new parking lot | Yes |
| GET | /api/parking/lots/{lotId}/occupancy | Returns current lot occupancy & available capacity | Yes |
| GET | /api/parking/lots/{lotId}/vehicles | Lists all active vehicles currently in the lot | Yes |
| POST | /api/parking/vehicles | Registers a new vehicle | Yes |
| POST | /api/parking/check-in | Checks in a vehicle to an active parking lot | Yes |
| POST | /api/parking/check-out/{licensePlate} | Checks out a vehicle, calculates total cost, and restores capacity | Yes |

---

## 🧪 Automated Testing & Edge Case Verification

The codebase includes test coverage across the application layers:

### Unit Tests Covered:
* **Service Layer (ParkingServiceTest.java)**:
  - testCheckIn_Success: Verifies successful check-in and capacity updates.
  - testCheckIn_LotFull_ThrowsException: Verifies lot capacity enforcement.
  - testCheckOut_Success: Verifies checkout duration, fee calculation, and lot space release.
  - testCheckIn_VehicleAlreadyParked_ThrowsException (Edge Case): Blocks duplicate active check-ins for the same license plate.
  - testCheckOut_VehicleNotFound_ThrowsException (Edge Case): Validates exception handling for unregistered vehicles.
  - testCheckOut_NoActiveSession_ThrowsException (Edge Case): Handles checkout requests for vehicles not currently parked.
* **Controller Layer (AuthControllerTest.java)**:
  - testLogin_ValidCredentials_ReturnsToken: Validates JWT token generation.
  - testLogin_InvalidCredentials_ReturnsUnauthorized: Confirms 401 Unauthorized handling on bad credentials.

### Executing Tests
Execute the test suite via the Maven wrapper:
./mvnw clean test

---

## 🚀 Postman Collection & Manual Testing

A complete Postman collection is included in the project root:
SmartPark_API.postman_collection.json

### Usage Instructions:
1. Start the application: ./mvnw spring-boot:run
2. Import SmartPark_API.postman_collection.json into Postman.
3. Run "1. Login (Obtain Token)" under the "1. Authentication" folder. A Postman test script will automatically store the returned token in the {{jwtToken}} collection variable.
4. Execute subsequent endpoints; authentication header Authorization: Bearer {{jwtToken}} will automatically be injected.

---

## 🗄️ Database Console Access

Inspect the in-memory H2 database directly via browser while the app is running:

* **URL**: http://localhost:8080/h2-console
* **JDBC URL**: jdbc:h2:mem:testdb
* **Username**: sa
* **Password**: (leave blank)

---

## 📋 Sample Request Payloads (JSON)

### 1. Authentication (`POST /api/auth/login`)
{
"username": "galicio",
"password": "smartpark"
}

### 2. Register Parking Lot (`POST /api/parking/lots`)
{
"lotId": "LOT-003",
"location": "Shopping Mall Annex",
"capacity": 20,
"ratePerMinute": 1.00
}

### 3. Register Vehicle (`POST /api/parking/vehicles`)
{
"licensePlate": "TEST-9999",
"type": "CAR",
"ownerName": "John Joseph"
}

### 4. Vehicle Check-In (`POST /api/parking/check-in`)
{
"licensePlate": "ABC-1234",
"lotId": "LOT-001"
}

---

## 📊 Unit Test Results & Execution Output

Maven Surefire execution output confirming 100% test coverage pass rate across service logic, auth controller, and edge case scenarios:

* [INFO] Running com.galicio.smartpark.controller.AuthControllerTest
* [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.812 s - in com.galicio.smartpark.controller.AuthControllerTest
* [INFO] Running com.galicio.smartpark.service.ParkingServiceTest
* [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.542 s - in com.galicio.smartpark.service.ParkingServiceTest
* [INFO]
* [INFO] Results:
* [INFO]
* **[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0**
* [INFO]
* [INFO] ------------------------------------------------------------------------
* **[INFO] BUILD SUCCESS**
* [INFO] ------------------------------------------------------------------------