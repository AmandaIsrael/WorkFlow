# syntax=docker/dockerfile:1

################################################################################

# Etapa 1: Baixar dependências
FROM eclipse-temurin:22-jdk-jammy AS deps

WORKDIR /build

COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/

RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:go-offline -DskipTests

################################################################################

# Etapa 2: Compilar o projeto
FROM deps AS package

WORKDIR /build

COPY ./src src/
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw package -DskipTests && \
    mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

################################################################################

# Etapa 3: Extrair camadas do Spring Boot (para otimizar cache do Docker)
FROM package AS extract

WORKDIR /build

RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

################################################################################

# Etapa 4: Criar container final para execução
FROM eclipse-temurin:22-jre-jammy AS final

WORKDIR /app

# Usar um usuário não root do sistema (padrão das imagens OpenJDK)
USER nobody

# Copiar as camadas extraídas da etapa "extract"
COPY --from=extract /build/target/extracted/dependencies/ ./
COPY --from=extract /build/target/extracted/spring-boot-loader/ ./
COPY --from=extract /build/target/extracted/snapshot-dependencies/ ./
COPY --from=extract /build/target/extracted/application/ ./

EXPOSE 8080

# Definir entrada do container
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]