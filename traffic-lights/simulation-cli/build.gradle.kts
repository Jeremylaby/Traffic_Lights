plugins {
    id("java")
    id("application")
}

group = "pl.jeremy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":simulation-core"))
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.21.1")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
application {
    mainClass.set("pl.jeremy.Main")
}
tasks.named<JavaExec>("run") {
    workingDir = rootProject.projectDir
}