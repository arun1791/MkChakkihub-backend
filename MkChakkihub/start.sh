#!/bin/bash
./mvnw clean package -DskipTests
java -jar target/MkChakkihub-backend-0.0.1-SNAPSHOT.jar