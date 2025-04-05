# Etapa de build
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia os arquivos necessários para compilar o projeto
COPY pom.xml .
COPY src ./src

# Compila o projeto (sem rodar os testes)
# vem de Threaded builds (construções com múltiplos threads).
# use 2 vezes o número de núcleos da CPU disponíveis.
RUN mvn clean package -DskipTests -T 2C

# Etapa de runtime
FROM openjdk:17-jdk-alpine

# Define o diretório de trabalho no container final
WORKDIR /home/default

# Copia o .jar gerado na build para o container final
COPY --from=builder /app/target/*.jar /app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "/app.jar"]
