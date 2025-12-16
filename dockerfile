FROM eclipse-temurin:25-jdk-alpine
WORKDIR /app
COPY target/clothesshop-0.0.1-SNAPSHOT.war /app/clothesshop-0.0.1-SNAPSHOT.war
EXPOSE 8080
CMD ["java", "-jar", "/app/clothesshop-0.0.1-SNAPSHOT.war"]