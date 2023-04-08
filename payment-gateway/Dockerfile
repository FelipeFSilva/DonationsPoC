FROM maven:3.8.6-eclipse-temurin-17
COPY . ./payment-gateway
WORKDIR ./payment-gateway
EXPOSE 8084
CMD ["mvn", "spring-boot:run"]