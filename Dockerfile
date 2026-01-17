# --- Estágio 1: Build ---
# Usamos uma imagem JDK completa para rodar o Wrapper
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copia os arquivos do Maven Wrapper e o pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Dá permissão de execução ao wrapper e baixa as dependências
# O flag '-B' (Batch mode) reduz o log desnecessário
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copia o código fonte e faz o build (pulando testes, pois rodarão no CI)
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# --- Estágio 2: Execução ---
# Imagem JRE leve apenas para rodar
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Cria usuário sem privilégios (Segurança)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]