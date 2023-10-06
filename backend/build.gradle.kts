import com.github.gradle.node.npm.task.NpmTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Configuration
import org.jooq.meta.jaxb.Property
import org.springframework.boot.gradle.tasks.run.BootRun
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jooq:jooq-codegen:${project.property("buildJooqVersion")}")
        classpath("org.jooq:jooq-meta:${project.property("buildJooqVersion")}")
        classpath("org.jooq:jooq-meta-extensions:${project.property("buildJooqVersion")}")
        classpath("com.h2database:h2:${project.property("h2Version")}")
    }
}

plugins {
    val springBootVersion = "3.1.3"
    val springDependencyManagement = "1.1.3"
    val kotlinVersion = "1.8.22"

    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version springDependencyManagement
//    id("org.graalvm.buildtools.native") version "0.9.23"

    id("com.github.node-gradle.node") version "5.0.0"
    id("com.gorylenko.gradle-git-properties") version "2.4.1"

    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
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

    // db r2dbc
    implementation("io.r2dbc:r2dbc-spi")
    implementation("io.r2dbc:r2dbc-pool")

    runtimeOnly("org.mariadb:r2dbc-mariadb:1.1.3")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    testRuntimeOnly("com.h2database:h2")
    testRuntimeOnly("io.r2dbc:r2dbc-h2")

    // jooq r2dbc
    implementation("org.jooq:jooq-kotlin:${project.property("jooqVersion")}")
    implementation("org.jooq:jooq-kotlin-coroutines:${project.property("jooqVersion")}")

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

    // mapstruct
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")
    kaptTest("org.mapstruct:mapstruct-processor:1.5.3.Final")

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

//import org.springframework.boot.gradle.tasks.run.BootRun
tasks.register("bootRunLocal", BootRun::class.java) {
    group = "Application"

    tasks.nodeSetup.get().enabled = false
    tasks.npmSetup.get().enabled = false
    tasks.npmInstall.get().enabled = false
    npmBuildTask.get().enabled = false

    sourceSets.main {
        kotlin.srcDirs(kotlin.srcDirs, file("$projectDir/src/test/kotlin/local"))
        resources.srcDirs(resources.srcDirs, file("$projectDir/src/test/resources"))
    }

    this.mainClass = tasks.bootRun.get().mainClass
    this.classpath = sourceSets.main.get().runtimeClasspath

    args(
        "--spring.profiles.active=local"
    )

    jvmArgs(
        "-Xms128m",
        "-Xmx128m",
        "-XX:HeapDumpPath=/tmp/webDump.hprof",
    )
}

tasks.register("bootRunDev", BootRun::class.java) {
    group = "Application"

    sourceSets.main {
        kotlin.srcDirs(kotlin.srcDirs, file("$projectDir/src/test/kotlin/local"))
        resources.srcDirs(resources.srcDirs, file("$projectDir/src/test/resources"))
    }

    this.mainClass = tasks.bootRun.get().mainClass
    this.classpath = sourceSets.main.get().runtimeClasspath

    args(
        "--spring.profiles.active=dev"
    )

    jvmArgs(
        "-Xms128m",
        "-Xmx128m",
        "-XX:HeapDumpPath=/tmp/webDump.hprof",
    )
}

//========================================================
// git properties
gitProperties {
    keys = arrayListOf("git.branch", "git.commit.id", "git.commit.id.abbrev", "git.commit.time", "git.tags")
}

//import java.time.OffsetDateTime
//import java.time.format.DateTimeFormatter
tasks.generateGitProperties {
    val gitBranch = generatedProperties["git.branch"].toString().split("/").last()
    val gitCommitId = generatedProperties["git.commit.id"].toString()
    val commitTime = generatedProperties["git.commit.time"].toString()

    doFirst {
        val commitTimeParse = OffsetDateTime.parse(commitTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"))
        val commitDateStr = commitTimeParse.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_Z"))

        tasks.bootJar.get().archiveAppendix = gitBranch
        tasks.bootJar.get().archiveVersion = "${commitDateStr}-${gitCommitId}"
        tasks.bootJar.get().manifest.attributes["Implementation-Version"] = gitCommitId
    }
}

tasks["bootBuildInfo"].dependsOn(tasks.generateGitProperties)

//========================================================
// jooq
//import org.jooq.codegen.GenerationTool
//import org.jooq.meta.jaxb.*
val jooqSchemaFile = "$projectDir/src/test/resources/db/h2/gen-*.sql"
val jooqSrcDir = "${layout.buildDirectory.get()}/generated/jooq"
val jooqPkg = "${group}.entity"

val jooqTableSet = MatchersTableType()
    .withExpression("^(.+)\$")
    .withTableIdentifier(MatcherRule().withTransform(MatcherTransformType.AS_IS).withExpression("\$0"))
    .withTableClass(MatcherRule().withTransform(MatcherTransformType.PASCAL).withExpression("\$0_TABLE"))
    .withRecordClass(MatcherRule().withTransform(MatcherTransformType.PASCAL).withExpression("\$0_RECORD"))
    .withInterfaceClass(MatcherRule().withTransform(MatcherTransformType.PASCAL).withExpression("\$0_INF"))
    .withDaoClass(MatcherRule().withTransform(MatcherTransformType.PASCAL).withExpression("\$0_DAO"))
    .withPojoClass(MatcherRule().withTransform(MatcherTransformType.PASCAL).withExpression("\$0_POJO"))

val jooqFieldSet = MatchersFieldType()
    .withExpression("^(.+)\$")
    .withFieldIdentifier(MatcherRule().withTransform(MatcherTransformType.AS_IS).withExpression("\$0"))
    .withFieldMember(MatcherRule().withTransform(MatcherTransformType.CAMEL).withExpression("\$0"))
    .withFieldSetter(MatcherRule().withTransform(MatcherTransformType.CAMEL).withExpression("set_\$0"))
    .withFieldGetter(MatcherRule().withTransform(MatcherTransformType.CAMEL).withExpression("get_\$0"))

val jooqConfig = Configuration()
    .withLogging(org.jooq.meta.jaxb.Logging.TRACE)
    .withGenerator(
        Generator()
//                        .withName("org.jooq.codegen.KotlinGenerator")
            .withDatabase(
                Database()
                    .withName("org.jooq.meta.extensions.ddl.DDLDatabase")
                    .withIncludes(".*")
                    .withExcludes("")
                    .withProperties(
                        Property().withKey("scripts").withValue(jooqSchemaFile),
                        Property().withKey("sort").withValue("semantic"),
                        Property().withKey("scriptunqualifiedSchema").withValue("none"),
                    )
            ).withGenerate(
                Generate()
                    .withTables(true)
                    .withInterfaces(true)
                    .withRecords(true)
                    .withPojos(true)
                    .withSerializablePojos(true)
                    .withDaos(false)
                    .withSpringDao(false)

//                                .withKotlinSetterJvmNameAnnotationsOnIsPrefix(false)
//                                .withPojosAsKotlinDataClasses(true)
//                                .withKotlinNotNullInterfaceAttributes(false)
//                                .withKotlinNotNullPojoAttributes(false)
//                                .withKotlinNotNullRecordAttributes(false)

            ).withStrategy(
                Strategy()
                    .withMatchers(
                        Matchers()
                            .withTables(jooqTableSet)
                            .withFields(jooqFieldSet)
                    )
            ).withTarget(
                org.jooq.meta.jaxb.Target()
                    .withClean(true)
                    .withPackageName(jooqPkg)
                    .withDirectory(jooqSrcDir)
            )
    )

val generateJooqTask = tasks.register("generateJooq") {
    group = "jooq"

    GenerationTool.generate(jooqConfig)

    sourceSets.main {
        java.srcDirs(java.srcDirs, file(jooqSrcDir))
    }
}

tasks.compileJava.get().dependsOn(generateJooqTask)
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
            into("${layout.buildDirectory.get()}/resources/main/static")
        }
    }
}

if (file(webappDir).exists()) {
    tasks.processResources.get().group = "build"
    tasks.processResources.get().duplicatesStrategy = DuplicatesStrategy.INCLUDE
    tasks.processResources.get().finalizedBy(npmBuildTask)
}