import org.yaml.snakeyaml.Yaml

buildscript {
	repositories {
		jcenter() // also works with mavenCentral()
		mavenCentral()
	}
	dependencies {
		classpath("org.yaml", "snakeyaml", "1.19")
	}
}

plugins {
	java
	idea
	maven
	`maven-publish`
	id("com.github.johnrengelman.shadow") version "5.2.0"
	kotlin("jvm") version "1.4.20"
}

val cfg: Map<String, Any> = Yaml().load(File("src/main/resources/plugin.yml").inputStream())

group = "com.codelezz.instances"
version = cfg["version"] ?: ""

java.sourceCompatibility = JavaVersion.VERSION_1_8

idea {
	module {
		isDownloadJavadoc = true
	}
}

val minecraftVersion = "1.8.8-R0.1-SNAPSHOT"

publishing {
	repositories {
		maven {
			name = "GitHubPackages"
			url = uri("https://maven.pkg.github.com/codelezz/spigot-instance")
			credentials {
				username = System.getenv("GITHUB_ACTOR")
				password = System.getenv("GITHUB_TOKEN")
			}
		}
	}
	publications {
		create<MavenPublication>("gpr") {
			artifactId = "codelezz-spigot-instance"
			from(components["java"])
		}
		create<MavenPublication>("mavenJava") {
			artifactId = "codelezz-spigot-instance"
			from(components["java"])
		}
	}
}

repositories {
	jcenter()
	mavenCentral()
	maven(url = "http://nexus.okkero.com/repository/maven-releases/")
	maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
	maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
	maven(url = "https://libraries.minecraft.net/")
	maven(url = "https://repo.codemc.org/repository/maven-public/")
	mavenLocal()
}

dependencies {
	compileOnly("org.spigotmc:spigot-api:${minecraftVersion}")
	compileOnly("com.mojang:authlib:1.5.21")

	implementation(kotlin("stdlib"))
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

	implementation("com.codelezz.instances:codelezz-kotlin-instance:+")
	implementation("com.okkero.skedule:skedule:1.2.6")

	testImplementation("junit", "junit", "4.12")
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
	manifest {
		attributes(mapOf("Main-Class" to "$group/Main"))
	}
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions {
		jvmTarget = "1.8"
	}
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
}

