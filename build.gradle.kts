import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val diskordVersion: String by project
val kotlinxVersion: String by project

plugins {
    kotlin("jvm") version "1.5.30"
    `maven-publish`
}

group = "com.github.myraBot"
val id = "Slasher"
version = "1.9"

repositories {
    mavenCentral()
    maven(url = "https://systems.myra.bot/releases/") {
        credentials {
            username = System.getenv("REPO_NAME")
            password = System.getenv("REPO_SECRET")
        }
    }
}
dependencies {
    val kotlinxGroup = "org.jetbrains.kotlinx"

    compileOnly(group = "com.github.myraBot", name = "Diskord", version = diskordVersion) // Discord Wrapper
    compileOnly(kotlin("reflect"))
    compileOnly(group = "org.reflections", name = "reflections", version = "0.10")
    // Coroutines
    compileOnly(group = kotlinxGroup, name = "kotlinx-coroutines-core", version = kotlinxVersion)
    compileOnly(group = kotlinxGroup, name = "kotlinx-coroutines-jdk8", version = kotlinxVersion)
    // JSON serialization
    compileOnly(group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version = "1.3.0") // Serializer

    testImplementation(group = "com.github.myraBot", name = "Diskord", version = diskordVersion) // Discord Wrapper
    testImplementation(kotlin("reflect"))
    testImplementation(group = "org.reflections", name = "reflections", version = "0.10")
    testImplementation(group = kotlinxGroup, name = "kotlinx-coroutines-core")
    testImplementation(group = kotlinxGroup, name = "kotlinx-coroutines-jdk8", version = kotlinxVersion)
}

/* publishing */
val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
    repositories {
        publications {
            create<MavenPublication>("repo") {
                group = project.group as String
                version = project.version as String
                artifactId = id
                from(components["java"])
            }
        }
        maven {
            url = uri("https://systems.myra.bot/releases/")
            name = "repo"
            credentials {
                username = System.getenv("REPO_NAME")
                password = System.getenv("REPO_SECRET")
            }
            authentication { create<BasicAuthentication>("basic") }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}