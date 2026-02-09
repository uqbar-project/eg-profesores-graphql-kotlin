plugins {
	id("org.springframework.boot") version "3.5.10"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("jvm") version "2.3.0"
	kotlin("plugin.spring") version "2.3.0"
	kotlin("plugin.jpa") version "2.3.0"
	jacoco
}

group = "org.uqbar"
version = "0.0.1-SNAPSHOT"

kotlin {
	jvmToolchain(21)

	compilerOptions {
		freeCompilerArgs.addAll(
			"-Xjsr305=strict",
		)
	}
}

tasks.withType<JavaCompile> {
	targetCompatibility = "21"
	sourceCompatibility = "21"
}

repositories {
	mavenCentral()
}

dependencies {
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// básicos de cualquier proyecto Spring Boot
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-hateoas")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// conexión a la base de datos
	runtimeOnly("org.postgresql:postgresql")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// testing
	testImplementation("com.h2database:h2:2.4.240")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// graphql - la versión 10 rompe retrocompatibilidad
	implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:9.2.2"))
	implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
}

dependencyManagement {
	imports {
		mavenBom("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:9.2.2")
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
	toolVersion = "0.8.14"
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
