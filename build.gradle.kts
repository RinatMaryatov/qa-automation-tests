
plugins {
    java
    id("io.qameta.allure") version "4.3.0"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {

    // JUnit 5 — запуск автотестов
    testImplementation(platform("org.junit:junit-bom:5.14.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // REST API
    testImplementation("io.rest-assured:rest-assured:5.5.7")

    // UI
    testImplementation("com.codeborne:selenide:7.19.0")

    // Assertions
    testImplementation("org.assertj:assertj-core:3.27.7")

    // JSON и DTO
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.22.3")

    // PostgreSQL
    testImplementation("org.postgresql:postgresql:42.7.14")

    // Apache Kafka
    testImplementation("org.apache.kafka:kafka-clients:4.4.0")

    // Ожидание асинхронных операций
    testImplementation("org.awaitility:awaitility:4.3.0")


    // Конфигурация окружений
    testImplementation("org.aeonbits.owner:owner:1.0.12")

    // Allure
    testImplementation(platform("io.qameta.allure:allure-bom:2.35.3"))
    testImplementation("io.qameta.allure:allure-jupiter")
    testImplementation("io.qameta.allure:allure-rest-assured")
    testImplementation("io.qameta.allure:allure-selenide")

    // Логирование
    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.20")

    // Тестовые данные
    testImplementation("net.datafaker:datafaker:2.4.4")
}

allure {
    adapter {
        allureJavaVersion.set("2.35.3")
    }
}

tasks.test {
    useJUnitPlatform()
    maxHeapSize = "1g"
    testLogging {
        events("passed", "failed", "skipped")
    }
}


val healthCheck by tasks.registering(JavaExec::class) {

    group = "verification"
    description = "Check services before running tests"

    dependsOn(tasks.testClasses)

    classpath = sourceSets["test"].runtimeClasspath

    mainClass.set(
        "ru.qa.infrastructure.health.HealthCheckRunner"
    )
}

tasks.named<Test>("test") {
    dependsOn(healthCheck)
}

