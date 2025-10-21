FROM openjdk:21-jdk-slim

# Instalar fuentes necesarias para JasperReports en modo headless
RUN apt-get update && apt-get install -y \
    fontconfig \
    libfreetype6 \
    fonts-dejavu-core \
    fonts-dejavu-extra \
    && rm -rf /var/lib/apt/lists/*

VOLUME /tmp
ADD build/libs/apex-core-service-0.0.1.jar apex-core-service.jar
EXPOSE 9090
RUN bash -c 'touch /apex-core-service.jar'
ENTRYPOINT ["java","-Djava.awt.headless=true","-Dspring.profiles.active=docker","-jar","apex-core-service.jar"]
