# ===== Build stage =====
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Сначала копируем Maven Wrapper и pom.xml для кеширования зависимостей
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw

RUN mkdir -p /root/.m2 && \
    printf '%s\n' \
    '<?xml version="1.0" encoding="UTF-8"?>' \
    '<toolchains xmlns="http://maven.apache.org/TOOLCHAINS/1.1.0">' \
    '  <toolchain>' \
    '    <type>jdk</type>' \
    '    <provides>' \
    '      <version>21</version>' \
    '    </provides>' \
    '    <configuration>' \
    '      <jdkHome>/opt/java/openjdk</jdkHome>' \
    '    </configuration>' \
    '  </toolchain>' \
    '</toolchains>' \
    > /root/.m2/toolchains.xml

RUN ./mvnw dependency:go-offline -B

# Копируем исходники и собираем приложение
COPY src src

RUN ./mvnw clean package -DskipTests -B

# ===== Runtime stage =====
FROM eclipse-temurin:21-jre

WORKDIR /app

RUN useradd --system --create-home appuser

COPY --from=build /app/target/*.jar app.jar

RUN chown appuser:appuser app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]