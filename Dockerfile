# ===========================
#  JavaFX Dockerfile for Xming
# ===========================
FROM openjdk:17-slim

WORKDIR /app

# Install required GUI/X11 libraries
RUN apt-get update && apt-get install -y \
    libx11-6 \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libgtk-3-0 \
    mesa-utils \
    wget \
    unzip \
    && rm -rf /var/lib/apt/lists/*

# Download and setup JavaFX SDK
RUN mkdir -p /javafx-sdk \
    && wget -O javafx.zip https://download2.gluonhq.com/openjfx/21.0.2/openjfx-21.0.2_linux-x64_bin-sdk.zip \
    && unzip javafx.zip -d /javafx-sdk \
    && mv /javafx-sdk/javafx-sdk-21.0.2/lib /javafx-sdk/lib \
    && rm -rf /javafx-sdk/javafx-sdk-21.0.2 javafx.zip

# Copy the fat JAR built with maven-shade-plugin
COPY target/notesApp.jar app.jar

# Set DISPLAY for Xming (Windows)
ENV DISPLAY=host.docker.internal:0.0
ENV DB_HOST=host.docker.internal
ENV JAVA_TOOL_OPTIONS="-Dprism.order=sw"

# Run JavaFX app with module path pointing to JavaFX SDK
CMD ["java", "--module-path", "/javafx-sdk/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "app.jar"]


