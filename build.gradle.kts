import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.jetbrains.dokka") version "1.8.10"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
}

group = "com.citi"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17


val ACCESS_KEY_ID: String by project
val SECRET_ACCESS_KEY: String by project
//println("$ACCESS_KEY_ID == $SECRET_ACCESS_KEY")

repositories {
    mavenCentral()
    //maven("https://maven.aliyun.com/nexus/content/groups/public")
}

val awsSdkKotlinVersion = "0.21.4-beta"
val okHttpVersion = "5.0.0-alpha.10" //aws sdk kotlin requires OkHttp 5.0.0-alpha.10

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("software.amazon.awssdk:dynamodb")
    implementation("software.amazon.awssdk:dynamodb-enhanced")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    /*implementation("aws.sdk.kotlin:s3-jvm:$awsSdkKotlinVersion")
    implementation("aws.sdk.kotlin:dynamodb-jvm:$awsSdkKotlinVersion")
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion") {
        version {
            strictly(okHttpVersion)
        }
    }*/
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

extra["springCloudAwsVersion"] = "3.0.0-RC2"

dependencyManagement {
    imports {
        mavenBom("io.awspring.cloud:spring-cloud-aws-dependencies:${property("springCloudAwsVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.bootJar{
    archiveName = "runner.jar"
}

//@See https://github.com/Kotlin/dokka/tree/master/examples/gradle
tasks.dokkaHtml{
    moduleName.set("nebular dokka")
    outputDirectory.set(File("$buildDir/dokka"))
}