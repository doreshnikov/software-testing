import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.qameta.allure.gradle.AllureExtension

val ktorVersion: String by project
val kotlinVersion: String by project
val exposedVersion: String by project
val jupiterVersion: String by project
val allureVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.4.21"
    id("io.qameta.allure") version "2.8.1"
}

group = "doreshnikov"
version = "0.0.1"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

    implementation("ch.qos.logback:logback-classic:1.2.1")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")

    implementation("org.jsoup:jsoup:1.13.1")

    implementation("com.zaxxer:HikariCP:4.0.1")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.postgresql:postgresql:42.2.2")
    implementation("com.h2database:h2:1.4.200")

    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")

    testImplementation("org.testcontainers:junit-jupiter:1.15.1")
    testImplementation("org.testcontainers:postgresql:1.15.1")
    testImplementation("org.mockito:mockito-inline:3.7.7")

    testImplementation("io.qameta.allure:allure-java-commons:$allureVersion")

    testImplementation("org.seleniumhq.selenium:selenium-java:3.141.59")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")

tasks.withType<Test> {
    useJUnitPlatform()

    systemProperty("junit.jupiter.execution.parallel.enabled", "true")
    systemProperty("junit.jupiter.execution.parallel.config.strategy", "dynamic")
    systemProperty("junit.jupiter.extensions.autodetection.enabled", "true")

    try {
        project.property("selenium")
        filter {
            includeTestsMatching("*Selenium*")
        }
    } catch (e: groovy.lang.MissingPropertyException) {
        filter {
            excludeTestsMatching("*Selenium*")
        }
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

configure<AllureExtension> {
    autoconfigure = true
    version = allureVersion

    clean = true

    useJUnit5 {
        version = allureVersion
    }
}