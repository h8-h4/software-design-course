plugins {
    id("java")
}

group = "ru.akirakozov.sd"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.findbugs:jsr305:3.0.2")

    implementation("org.xerial:sqlite-jdbc:3.8.11.2")
    implementation("org.eclipse.jetty:jetty-server:9.4.21.v20190926")
    implementation("org.eclipse.jetty:jetty-servlet:9.4.21.v20190926")

    compileOnly("org.projectlombok:lombok:1.18.24")


    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}