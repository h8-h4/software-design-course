import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java")
    id("application")
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("exchange.Application")
}

dependencies {
    testImplementation("org.testcontainers:testcontainers:1.17.6")
    testImplementation("org.testcontainers:junit-jupiter:1.17.6")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("com.h2database:h2:2.1.214")
    testImplementation("com.h2database:h2:2.1.214")

    implementation("com.google.code.findbugs:jsr305:3.0.2")

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    testCompileOnly("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

val bootJar: BootJar by tasks

tasks.getByName<Test>("test") {
    useJUnitPlatform()

    systemProperty("distribution.dir", bootJar.destinationDir.absolutePath)
    systemProperty("archive.name", bootJar.archiveName)

    dependsOn(tasks.getByName("assemble"))
}