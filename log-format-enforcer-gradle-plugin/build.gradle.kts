plugins {
    java
    signing
    id("com.gradle.plugin-publish") version "1.3.1"
}

group = "com.leandronunes85.lfe"
version = "2.0.1-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

gradlePlugin {
    website = "https://github.com/leandronunes85/log-format-enforcer"
    vcsUrl = "https://github.com/leandronunes85/log-format-enforcer.git"
    plugins {
        create("logFormatEnforcer") {
            id = "com.leandronunes85.log-format-enforcer"
            displayName = "Log Format Enforcer"
            description =
                "The goal of this plugin is to provide teams with a way of enforcing a certain logging message style in their projects."
            tags = setOf("log", "format", "enforce", "observability", "operational", "support")
            implementationClass = "com.leandronunes85.lfe.plugin.gradle.LogFormatEnforcerPlugin"
        }
    }
}

dependencies {
    implementation("com.leandronunes85.lfe:log-format-enforcer-generator:$version")
}
