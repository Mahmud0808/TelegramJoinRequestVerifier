plugins {
    kotlin("jvm") version "2.2.0"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.drdisagree.joinreqbot"
version = "1.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("dev.inmo:tgbotapi:5.1.0")
    implementation("org.slf4j:slf4j-nop:1.7.36")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("MainKt")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveBaseName.set("joinreqbot")
        archiveClassifier.set("")
        archiveVersion.set(version.toString())

        mergeServiceFiles()
        minimize {
            exclude(dependency("io.ktor:.*:.*"))
            exclude(dependency("org.slf4j:slf4j-api:.*"))
            exclude(dependency("org.slf4j:slf4j-nop:.*"))
        }
    }
}

application {
    mainClass.set("MainKt")
}