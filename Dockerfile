# ---------- Etapa 1: compilar el proyecto con Maven ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B package -DskipTests

# ---------- Etapa 2: imagen final, solo con Java y el jar ----------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/cards4play.jar app.jar

# Variables de entorno (se pueden cambiar al ejecutar con -e)
ENV PORT=4567
ENV DATA_DIR=/app/data
ENV TASA_DEFECTO=4000
ENV ADMIN_USUARIO=admin
ENV ADMIN_CLAVE=admin123

# Los archivos JSON se guardan en este volumen, así sobreviven si se borra el contenedor
VOLUME /app/data

EXPOSE 4567
CMD ["java", "-jar", "app.jar"]
