import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val diskordVersion: String by project
val kotlinxVersion: String by project

plugins {
    kotlin("jvm") version "1.5.30"
    `maven-publish`
}

group = "com.github.myraBot"
val id = "Slasher"
version = "0.8"

repositories {
    mavenCentral()
    maven(url = "https://m5rian.jfrog.io/artifactory/java")
}
dependencies {
    val kotlinxGroup = "org.jetbrains.kotlinx"

    compileOnly(group = "com.github.myraBot", name = "Diskord", version = diskordVersion) // Discord Wrapper
    compileOnly(kotlin("reflect"))
    compileOnly(group = "org.reflections", name = "reflections", version = "0.10")
    compileOnly(group = kotlinxGroup, name = "kotlinx-coroutines-core")
    compileOnly(group = kotlinxGroup, name = "kotlinx-coroutines-jdk8", version = kotlinxVersion)

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
        maven {
            name = "jfrog"
            url = uri("https://m5rian.jfrog.io/artifactory/java")
            credentials {
                username = System.getenv("JFROG_USERNAME")
                password = System.getenv("JFROG_PASSWORD")
            }
        }
    }

    publications {
        create<MavenPublication>("jfrog") {
            from(components["java"])

            group = project.group as String
            version = project.version as String
            artifactId = id

            artifact(sourcesJar)
        }
    }
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}