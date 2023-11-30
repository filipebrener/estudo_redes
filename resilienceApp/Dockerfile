# Use uma imagem base do Java
FROM openjdk:21

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o JAR da aplicação para o contêiner
COPY ./target/resilience-app.jar /app/resilience-app.jar

# Comando para executar a aplicação quando o contêiner iniciar
CMD ["java", "-jar", "resilience-app.jar"]