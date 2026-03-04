## Architecture Overview

This project demonstrates a microservices architecture built with Spring Boot and Spring Cloud, implementing Chaos Engineering principles using Chaos Monkey. The system consists of multiple interconnected services that showcase resilience patterns and fault tolerance testing.

### Services Architecture

```mermaid
graph TB
    Client[Client Applications]
    Gateway[Gateway Service<br>:8090]
    Discovery[Discovery Service<br>Eureka Server<br>:8761]
    Customer[Customer Service<br>:8093]
    Order[Order Service<br>:8091]
    Product[Product Service<br>:8092]
    DB1[(MySQL<br>Customer DB)]
    DB2[(MySQL<br>Order DB)]
    DB3[(MySQL<br>Product DB)]
    LoadTest[Performance Tests<br>Gatling]

    Client --o Gateway
    Gateway --o Discovery
    Gateway --o Customer
    Gateway --o Order
    Gateway --o Product

    Customer --o Discovery
    Order --o Discovery
    Product --o Discovery

    Customer --o DB1
    Order --o DB2
    Product --o DB3

    Order -.->|Feign Client| Customer
    Order -.->|Feign Client| Product

    LoadTest -.->|HTTP Requests| Gateway

    classDef service fill:#e1f5fe
    classDef database fill:#f3e5f5
    classDef infrastructure fill:#e8f5e8
    classDef test fill:#fff3e0

    class Customer Order Product service
    class DB1 DB2 DB3 database
    class Gateway Discovery infrastructure
    class LoadTest test
```

### Service Details

| Service               | Port | Purpose           | Key Features                                   |
|-----------------------|------|-------------------|------------------------------------------------|
| **Discovery Service** | 8761 | Service Registry  | Netflix Eureka Server, Service Discovery       |
| **Gateway Service**   | 8090 | API Gateway       | Spring Cloud Gateway, Load Balancing, Routing  |
| **Customer Service**  | 8093 | Customer Management | REST API, MySQL, Chaos Monkey, JPA           |
| **Order Service**     | 8091 | Order Processing  | REST API, MySQL, Feign Clients, Chaos Monkey   |
| **Product Service**   | 8092 | Product Catalog   | REST API, MySQL, Chaos Monkey, JPA             |
| **Performance Tests** | —    | Load Testing      | Gatling Framework, API Testing                 |

### Technology Stack

- **Framework**: Spring Boot 3.4.5  
- **Cloud**: Spring Cloud 2024.0.1  
- **Java Version**: 21  
- **Service Discovery**: Netflix Eureka  
- **API Gateway**: Spring Cloud Gateway  
- **HTTP Client**: Spring Cloud OpenFeign  
- **Database**: MySQL with Spring Data JPA  
- **Chaos Engineering**: Chaos Monkey for Spring Boot 3.2.2  
- **Testing**: Testcontainers, Gatling  
- **Build Tools**: Maven (services), Gradle (performance tests)  

### Chaos Engineering Features

Each business service (Customer, Order, Product) is configured with Chaos Monkey to simulate various failure scenarios:

- **Latency Attacks**: Introduces delays (1–10 seconds)  
- **Application Killer**: Randomly terminates services  
- **Watchers**: Monitors repositories and REST controllers  
- **Management Endpoints**: Chaos Monkey controls via actuator endpoints  

## Local Development Setup

### Prerequisites

Before running the application locally, ensure you have the following installed:

- **Java 21** or later  
- **Maven 3.6+**  
- **MySQL 8.0+**  
- **Git**  
- **Gradle** (for performance tests)  

### Database Setup

1. **Install MySQL** (if not already installed):  
   ```bash
   # macOS
   brew install mysql

   # Ubuntu/Debian
   sudo apt-get install mysql-server

   # Windows
   # Download from https://dev.mysql.com/downloads/mysql/
   ```
2. **Start MySQL service**:  
   ```bash
   # macOS
   brew services start mysql

   # Ubuntu/Debian
   sudo systemctl start mysql

   # Windows
   # Start from Services or MySQL Workbench
   ```
3. **Create database and user**:  
   ```sql
   CREATE DATABASE chaos;
   CREATE USER 'chaos'@'localhost' IDENTIFIED BY 'chaos123';
   GRANT ALL PRIVILEGES ON chaos.* TO 'chaos'@'localhost';
   FLUSH PRIVILEGES;
   ```

### Running the Services

#### Option 1: Manual Startup (Recommended for Development)

```bash
git clone https://github.com/piomin/sample-spring-chaosmonkey.git
cd sample-spring-chaosmonkey
```

Start services in order:

**Step 1: Discovery Service**  
```bash
cd discovery-service
mvn spring-boot:run
```
Wait for it to register at http://localhost:8761

**Step 2: Gateway Service**  
```bash
cd gateway-service
mvn spring-boot:run
```

**Step 3: Business Services** (parallel terminals)  
```bash
# Terminal 1
cd customer-service && mvn spring-boot:run

# Terminal 2
cd product-service && mvn spring-boot:run

# Terminal 3
cd order-service && mvn spring-boot:run
```

#### Option 2: Build and Run JAR Files

```bash
mvn clean package
```
Run each service:

```bash
java -jar discovery-service/target/discovery-service-1.0-SNAPSHOT.jar
java -jar gateway-service/target/gateway-service-1.0-SNAPSHOT.jar
java -jar customer-service/target/customer-service-1.1-SNAPSHOT.jar
java -jar product-service/target/product-service-1.1-SNAPSHOT.jar
java -jar order-service/target/order-service-1.1-SNAPSHOT.jar
```

### Verification

1. **Eureka Dashboard**: http://localhost:8761  
   - All services should be registered  
2. **Service Health Checks**:  
   ```bash
   curl http://localhost:8093/actuator/health  # Customer
   curl http://localhost:8091/actuator/health  # Order
   curl http://localhost:8092/actuator/health  # Product
   ```
3. **API Gateway Routes**:  
   ```bash
   curl http://localhost:8090/customer/actuator/health
   curl http://localhost:8090/order/actuator/health
   curl http://localhost:8090/product/actuator/health
   ```
4. **Chaos Monkey Endpoints**:  
   ```bash
   curl http://localhost:8093/actuator/chaosmonkey/status
   curl http://localhost:8091/actuator/chaosmonkey/status
   curl http://localhost:8092/actuator/chaosmonkey/status
   ```

### Testing

#### Unit and Integration Tests

```bash
mvn test
```
Uses Testcontainers for automatic MySQL containers.

#### Performance Testing with Gatling

```bash
cd performance-test
gradle loadTest
```
Reports in `build/gatling-results`.

### Chaos Engineering Experiments

1. **Enable/Disable Chaos Monkey**:  
   ```bash
   curl -X POST http://localhost:8093/actuator/chaosmonkey/enable
   curl -X POST http://localhost:8093/actuator/chaosmonkey/disable
   ```
2. **Check Configuration**:  
   ```bash
   curl http://localhost:8093/actuator/chaosmonkey
   ```
3. **Modify Assaults**:  
   ```bash
   curl -X POST http://localhost:8093/actuator/chaosmonkey/assaults \
     -H 'Content-Type: application/json' \
     -d '{"level":5,"latencyRangeStart":2000,"latencyRangeEnd":5000}'
   ```

### Troubleshooting

**Common Issues:**
1. Port conflicts on 8090–8093, 8761  
2. MySQL not running or wrong credentials  
3. Services taking time (30–60s) to register in Eureka  
4. Schema auto-update is enabled—check logs if table creation fails  

**Logs:**
- Check each service’s console output  
- Chaos Monkey logs appear at TRACE level in order-service  

For more on Chaos Engineering with Spring Boot, see the [original blog post](https://piotrminkowski.com/2018/05/23/chaos-monkey-for-spring-boot-microservices/).