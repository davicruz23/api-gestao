# Stage de build
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copia pom.xml e baixa dependências (cache layer)
COPY pom.xml .
RUN apt-get update && apt-get install -y maven && mvn dependency:go-offline

# Copia o restante do código e gera o .jar
COPY src ./src
RUN mvn clean package -DskipTests

# Stage final, apenas para rodar o app
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
