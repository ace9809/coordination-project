plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.2")
	implementation("org.springframework.boot:spring-boot-starter-web:3.4.2")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.0")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")
	runtimeOnly("com.h2database:h2:2.3.232")
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.2")
	implementation("org.springframework.boot:spring-boot-starter-cache:3.4.2")
	implementation("com.github.ben-manes.caffeine:caffeine:3.2.0")
	testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
	testImplementation("io.kotest:kotest-assertions-core:5.9.1")
	testImplementation("io.mockk:mockk:1.13.16")
	testImplementation("com.appmattus.fixture:fixture:1.2.0")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
