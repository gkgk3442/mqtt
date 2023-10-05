import com.github.gradle.node.npm.task.NpmTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    id("org.graalvm.buildtools.native") version "0.9.23"

//    id("com.github.node-gradle.node") version "5.0.0"
    id("com.gorylenko.gradle-git-properties") version "2.4.1"

    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
}

group = "com.naonworks"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

configurations {
    configureEach {
        this.exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // log4j2
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("com.lmax:disruptor:3.4.4")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")
    implementation("org.yaml:snakeyaml:2.0")

    // kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // spring
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.1.0")

    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

springBoot {
    buildInfo()
}

tasks.jar {
    enabled = false
}

tasks.bootJar {
    enabled = true

    launchScript {
        properties["initInfoRequiredStart"] = "\$remote_fs \$syslog \$network"
        properties["initInfoRequiredStop"] = "\$remote_fs \$syslog \$network"
    }

//	archiveBaseName = ""
//	archiveAppendix = ""
//	archiveVersion = ""
    archiveClassifier = ""
//	archiveExtension = "jar"
//	archiveFileName = project.name + ".jar";
    manifest {
        attributes["Implementation-Title"] = project.name
        attributes["Implementation-Version"] = project.version
    }
}

//========================================================
// git properties
gitProperties {
//    extProperty = "gitProps"
    keys = arrayListOf("git.branch", "git.commit.id", "git.commit.id.abbrev", "git.commit.time", "git.tags")
}

tasks.generateGitProperties {
    val gitBranch = generatedProperties["git.branch"].toString().split("/").last()
    val gitCommitId = generatedProperties["git.commit.id"].toString()
    val commitTime = generatedProperties["git.commit.time"].toString()

    doFirst {
        val commitTimeParse = OffsetDateTime.parse(commitTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"))
        val commitDateStr = commitTimeParse.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_Z"))

        project.version = gitCommitId
        tasks.bootJar.configure {
            archiveAppendix = gitBranch
            archiveVersion = "${commitDateStr}-${gitCommitId}"
        }
    }
}

tasks["bootBuildInfo"].dependsOn(tasks.generateGitProperties)

//========================================================
// node, npm
val homePath = System.getProperty("user.home")
val webappDir = "${project.projectDir}/../frontend"

node {
    version = "18.15.0"
    npmVersion = "9.6.6"

    workDir = file("${homePath}/.gradle/nodejs")
    npmWorkDir = file("${homePath}/.gradle/npm")
    nodeProjectDir = file(webappDir)

    download = true
}

val npmBuildTask = tasks.register("npmBuild", NpmTask::class.java) {
    dependsOn(tasks.npmInstall)

    workingDir = file(webappDir)

    args = arrayListOf("run", "build")

    doLast {
        copy {
            from("${webappDir}/dist")
            into("${buildDir}/resources/main/static")
        }
    }
}

if (file(webappDir).exists()) {
    tasks.processResources.configure {
        finalizedBy(npmBuildTask)
    }
}