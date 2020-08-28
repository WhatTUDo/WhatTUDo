# WhatTUDo

An interactive, cooperative calendar platform for universities and student organizations. 

Originally a project for the _Software Engineering and Project Management_ course at TU Vienna, Spring 2020. 

## Getting Started

### Backend

To start the backend application with Maven, use `./mvnw spring-boot:run` in backend/ (or the equivalent 
command for your system). 
The application will start listening on port :8080 by default. 

The application can also be started as a normal Spring Boot Application. The base class is in 
backend/src/main/java/at/ac/tuwien/sepm/groupphase/backend/BackendApplication.java. 

To generate test data, run the application as a Spring Boot Application with the profile 'generateData'. Make sure to 
replace the existing database folder inside /backend. 

### Frontend

Before compiling the frontend, excecute `npm install` in frontend/. When all dependencies are installed, use `ng serve`
to compile and run the frontend. By default, the frontend listens on port :4200. 

## Tests

To run the Test Suite located at backend/src/test/java/at/ac/tuwien/sepm/groupphase/backend, use `./mvnw test` (or the 
equivalent command for your system). Alternatively, execute them as JUNIT tests. 

## Deployment

Dockerization is planned. 

## License

The project is licensed under the MIT License. https://choosealicense.com/licenses/mit/#

## Developers & Acknowledgements

The original developers of this project are: 
* Yizhou "Andi" Cui
* Luca Eichler
* Andreas Hausberger
* Andreas Huber
* Sophie Martine Kaider
* Anda Selimi
