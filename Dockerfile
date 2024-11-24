# Etap budowania (build stage)
FROM eclipse-temurin:latest AS build

# Skopiowanie kodu źródłowego do kontenera
COPY . .

# Budowanie aplikacji bez uruchamiania testów
RUN mvn clean package -DskipTests

# Etap uruchamiania aplikacji (runtime stage)
FROM openjdk:21-jdk-slim

# Skopiowanie wygenerowanego pliku JAR z etapu build
COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar demo.jar

# Otworzenie portu 8080
EXPOSE 8080

# Uruchomienie aplikacji Spring Boot
ENTRYPOINT ["java", "-jar", "demo.jar"]

