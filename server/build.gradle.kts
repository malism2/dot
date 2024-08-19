plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kserialization)
    application
}

group = "com.malism.dot"
version = "1.0.0"
application {
    mainClass.set("com.malism.dot.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.json)
    implementation(libs.ktor.server.negotiation)
    implementation(libs.ktor.server.host)
    implementation(libs.ktor.server.status)
    implementation(libs.ktor.server.config)
    implementation(libs.h2database)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
//    testImplementation(libs.ktor.server.tests)
//    testImplementation(libs.kotlin.test.junit)
}