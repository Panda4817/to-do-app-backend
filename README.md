# To Do App Backend Service
Backend service that can interact with both SQL and NoSQL databases.

## Tech Stack

* Java 17
* Spring Boot 3
* Maven
* Spring Data

## Running in development

Start databases using docker. Can use the script provided:

```
bash ./development/start-database.sh
```

Then start application:

```
mvn spring-boot:run
```

Run the following to stop databases:

```
bash ./development/stop-database.sh 
```

## Build and Run tests

```
mvn clean install
```