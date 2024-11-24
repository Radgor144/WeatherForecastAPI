# Używamy oficjalnego obrazu Javy, np. Eclipse Temurin (OpenJDK)
FROM eclipse-temurin:17-jdk-focal

# Zdefiniuj zmienną ARG wskazującą na plik JAR
ARG JAR_FILE=target/*.jar

# Kopiujemy plik JAR do kontenera
COPY ${JAR_FILE} app.jar

# Otwieramy port 8080 dla aplikacji Spring Boot
EXPOSE 8080

# Uruchamiamy aplikację Spring Boot
ENTRYPOINT ["java", "-jar", "/app.jar"]
