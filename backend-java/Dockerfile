FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

EXPOSE 8000

CMD ["java", "-jar", "target/phonebook-backend-0.0.1-SNAPSHOT.jar"]