FROM openjdk:17-jdk-slim AS build
WORKDIR /app

# Copia pom.xml e baixa dependências (cache layer)
COPY pom.xml .
RUN apt-get update && apt-get install -y maven && mvn dependency:go-offline

# Copia o código e builda
COPY src ./src
RUN mvn clean package -DskipTests

# Build final
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
