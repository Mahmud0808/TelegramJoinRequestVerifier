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
    implementation("dev.inmo:tgbotapi:30.0.2")
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
        minimize()
    }
}

application {
    mainClass.set("MainKt")
}