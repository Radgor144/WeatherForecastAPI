FROM eclipse-temurin:latest

# Skopiowanie pliku JAR do obrazu. Upewnij się, że Render będzie generował plik JAR
# Na Render.com proces budowania aplikacji uruchomi odpowiednie komendy (np. mvn clean package)
COPY target/*.jar app.jar


# Otworzenie portu 8080 dla aplikacji Spring Boot
EXPOSE 8080

# Uruchomienie aplikacji Spring Boot
ENTRYPOINT ["java", "-jar", "/app.jar"]
