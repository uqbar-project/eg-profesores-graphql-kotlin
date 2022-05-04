import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
	kotlin("plugin.jpa") version "1.6.10"
	jacoco
}

group = "com.uqbar"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_14
java.targetCompatibility = JavaVersion.VERSION_14

repositories {
	mavenCentral()
}

val springVersion = "2.6.7"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springVersion")
	implementation("org.springframework.boot:spring-boot-starter-data-rest:$springVersion")
	implementation("org.springframework.boot:spring-boot-starter-hateoas:$springVersion")
	implementation("org.springframework.boot:spring-boot-starter-web-services:$springVersion")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.2")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.2")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springdoc:springdoc-openapi-ui:1.6.7")
	implementation("org.springframework.boot:spring-boot-devtools:$springVersion")
	implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:latest.release"))
	implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
	testImplementation("com.h2database:h2:2.1.212")
	testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
	runtimeOnly("mysql:mysql-connector-java:8.0.28")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "14"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
}

jacoco {
	toolVersion = "0.8.7"
}

tasks.jacocoTestReport {
	classDirectories.setFrom(
		files(classDirectories.files.map {
			fileTree(it) {
				exclude("**/config/**", "**/entity/**", "**/*Application*.*", "**/ServletInitializer.*")
			}
		})
	)
	reports {
		xml.required.set(true)
		csv.required.set(false)
		html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
	}
}

tasks.register("runOnGitHub") {
	dependsOn("jacocoTestReport")
	group = "custom"
	description = "$ ./gradlew runOnGitHub # runs on GitHub Action"
}
