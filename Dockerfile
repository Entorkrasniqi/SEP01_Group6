FROM maven:latest
LABEL authors="aldul"

WORKDIR /app
COPY pom.xml /app
COPY . /app
RUN mvn package
CMD ["java", "-jar", "target/notesApp.jar"]