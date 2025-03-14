# Project: Desafio3 - Tickets and Events Microservices

## Overview
This repository contains two microservices developed in **Java 17** using **Spring Boot 3.4.3**, and managed with **Maven**.
The microservices are:
- **Tickets Service**: This api manage the tickets.
- **Events Service**: This api mange the events and also for integration with the external **ViaCep** api.

Both microservices interact with each other to provide complete functionality to the system.

---

## Technologies Used
- **Java 17**
- **Spring Boot 3.4.3**
- **Spring Web** (for APIs REST)
- **MongoDb Atlas** (for data persistence)
- **Spring Cloud OpenFeign** (for internal communication)
- **Docker** (for containers)
- **Maven** (for dependency management and build)

---

## How to run the project

### **Prerequisites**
Before starting, make sure you have the following installed:
- **Java 17**
- **Maven**

### **Steps to run locally**
1. Clone the repository:
   ```sh
   git clone git@github.com:GuilhermeW1/Desafio3.git
   cd desafio3
   ```
2. Before running the project
   - Open the application.properties file in the tickets service and change/add the port from 8080 to 8081.
   - Still in the tickets service, go to EventService and change the FeignClient URL from http://10.0.17.142:8080/api/events/v1 to http://localhost:8080/api/events/v1.
   - Now go to /events and change the FeignClient URL from http://10.0.21.226:8080/api/tickets/v1 to http://localhost:8081/api/tickets/v1.
   - You will also need to configure your MongoDB connection URL.
   
3. **Run the microservices individually**
   ```sh
   cd tickets
   mvn spring-boot:run
   ```
   In another terminal:
   ```sh
   cd ../events
   mvn spring-boot:run
   ```

### **Running with Docker**
1. **Build the images**
   ```sh
   docker build -t desafio3-tickets ./tickets
   docker build -t desafio3-events ./events
   ```

2. **Execute the containers**
   ```sh
   docker run -d -p 8080:8080 desafio3-events
   docker run -d -p 8081:8081 desafio3-tickets 
   ```

---

## Main endpoints

### **Tickets Service**
- `POST /api/tickets/v1/create-ticket` - Create a new ticket
- `GET /api/tickets/v1/get-ticket/{id}` - Get ticket by ID
- `GET /api/tickets/v1/get-ticket-by-cpf/{cpf}` - Get tickets by CPF
- `GET /api/tickets/v1//check-tickets-by-event/{eventId}` - Get tickets by EventId
- `PUT /api/tickets/v1/update-ticket/{id}` - Update ticket
- `DELETE /api/tickets/v1/cancel-ticket/{id}` - Remove a ticket

### **Events Service**
- `POST /api/events/v1/create-event` - Create event
- `GET /api/events/v1/get-event/{id}` - Get by ID
- `GET /api/events/v1/get-all-events` - Get all
- `GET /api/events/v1/get-all-events/sorted` - Get all events sorted by event name
- `PUT /api/events/v1/update-event/{id}` - Update event
- `DELETE /api/events/v1/delete-event/{id}` - Remove event

---

## Tests
Unit and integration tests were implemented using:
- **JUnit 5**
- **Mockito**
- **Testcontainers** (for testing in an isolated environment with a real database)

To run the tests:
```sh
mvn test
```

---

## Notes
- The **Events Service** consumes data from **ViaCEP** to get addresses based on the provided postal code (CEP).
- The microservices use **Feign Clients** for internal communication.
- Logs have been added for monitoring key operations.

---

## AWS Configuration

In AWS, I created a **VPC (Virtual Private Cloud)** and a **security group**, allowing inbound traffic on ports **8080, 80, and 443** (standard internet ports). Additionally, I enabled **port 22** for SSH access.

I launched **two EC2 instances** and configured them to use the **VPC** and the **security group**.

Next, I built my application using **Docker** and pushed the images to **Docker Hub**.  
Then, I connected to my instances via **SSH**, installed **Docker**, and pulled the images from **Docker Hub**.

- The applications communicate internally within the VPC, allowing both to run on **port 8080** without conflicts.
- The **Events** service points to the internal IP of the **Tickets** service.
- The **Tickets** service points to the internal IP of the **Events** service.

### Internal Endpoints

The API endpoints remain the same, but the base URLs differ:

- **Events Service:** `http://10.0.17.142:8080/api/events/v1`
- **Tickets Service:** `http://10.0.21.226:8080/api/tickets/v1`  

