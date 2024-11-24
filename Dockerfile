# Etap 1: Budowanie aplikacji
FROM maven:3.3.6-openjdk-17 as build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etap 2: Uruchomienie aplikacji
FROM eclipse-temurin:17-jdk AS runtime

WORKDIR /app
COPY --from=build /app/target/WeatherForecast-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8080
CMD ["java", "-jar", "demo.jar"]
