# Project Setup Guide

This project is a Spring Boot application with a MySQL database.  
Follow the steps below to set up the database, configure the application, and run the service.

# Prerequisites
- Java 21+
- Maven 3.6+
- MySQL 8+
---

## 1. Database Setup

Create local MySQL database.  
Run the following SQL statements to create the required tables:

```mysql
CREATE TABLE tax_rules
(
    trader_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tax_type  enum ('General', 'Winnings') NOT NULL,
    tax_mode  enum ('Rate', 'Amount')      NOT NULL,
    value     DECIMAL(10, 2)               NOT NULL
);

INSERT INTO tax_rules (trader_id, tax_type, tax_mode, value)
VALUES (1, 'General', 'Rate', 0.10),
       (2, 'General', 'Amount', 2.00),
       (3, 'Winnings', 'Rate', 0.10),
       (4, 'Winnings', 'Amount', 1.00);


create table match_events
(
    match_id    varchar(50)                         null,
    market_id   int                                 null,
    outcome_id  varchar(100)                        null,
    specifiers  text                                null,
    date_insert timestamp default CURRENT_TIMESTAMP null
);
CREATE INDEX idx_match_id
    ON match_events (match_id);
```

# Project setup
- Update your database credentials in **src/main/resources/application.properties**
- Replace {URL}, {DB_NAME}, {USERNAME} and {PASSWORD}
```
# DB
spring.datasource.url=jdbc:mysql://{URL}:3306/{DB_NAME}?useSSL=false&serverTimezone=UTC
spring.datasource.username={USERNAME}
spring.datasource.password={PASSWORD}

# DB Batch
batch.datasource.jdbc-url=jdbc:mysql://{URL}:3306/{DB_NAME}?useSSL=false&serverTimezone=UTC&useServerPrepStmts=false&rewriteBatchedStatements=true
batch.datasource.username={USERNAME}
batch.datasource.password={PASSWORD}
```

# Build the project
```
mvn clean install
```

# Run the application
```
mvn spring-boot:run
```

# Task 1
Place a Bet
```
Method: POST
URL: localhost:8080/api/betting/bet
Request: 
{
    "traderId": 1, 
    "playedAmount": 5,
    "odd": 1.5
}

Options for traderId = 1,2,3,4
```

# Task 2
Process events and insert them to database
```
Method: POST
URL: localhost:8080/api/event/processEvents
```