import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


val springBootVersion = "2.5.1"


plugins {
	id("org.springframework.boot") version "2.5.1"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.10"
	kotlin("plugin.spring") version "1.5.10"
}

group = "com.reactive"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("io.github.microutils:kotlin-logging:2.0.8")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")

	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive:2.5.1")

    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "16"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
