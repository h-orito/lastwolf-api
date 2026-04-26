import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.spring") version "2.1.21"
    id("com.google.cloud.tools.jib") version "3.4.4"
}

group = "com.ort"
version = "0.0.1-SNAPSHOT"

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
}

sourceSets {
    getByName("main").java.setSrcDirs(listOf("src/main/kotlin"))
    getByName("test").java.setSrcDirs(listOf("src/test"))
    getByName("main").resources.setSrcDirs(listOf("src/main/resources"))
    getByName("test").resources.setSrcDirs(listOf("src/test/resources"))
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    // dbflute
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.dbflute:dbflute-runtime:1.3.1")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    // mysql
    val mysqlConnectorVersion = System.getenv("MYSQL_CONNECTOR_VERSION") ?: "9.2.0"
    implementation("com.mysql:mysql-connector-j:$mysqlConnectorVersion")
    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")
    implementation("com.google.firebase:firebase-admin:9.4.2")
    // twitter
    implementation("org.twitter4j:twitter4j-core:4.0.7")
    // slack
    implementation("com.slack.api:slack-api-client:1.45.3")
    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // mockito
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

jib {
    from {
        image = "eclipse-temurin:21-jre"
        platforms {
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
    to {
        image = "ghcr.io/h-orito/lastwolf"
    }
    container {
        jvmFlags = listOf(
            "-server",
            "-Djava.awt.headless=true",
            "-Dspring.profiles.active=production"
        )
        creationTime = "USE_CURRENT_TIMESTAMP"
    }
}
