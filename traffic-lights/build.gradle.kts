plugins {
    id("com.diffplug.spotless") version "8.3.0"
    id("checkstyle")
}

group = "pl.jeremy"
version = "1.0-SNAPSHOT"

subprojects {
    plugins.withId("java") {
        apply(plugin = "com.diffplug.spotless")
        apply(plugin = "checkstyle")

        spotless {
            java {
                target("src/**/*.java")
                importOrder("java", "javax", "", "org", "com", "pl")
                removeUnusedImports()
                cleanthat()
                palantirJavaFormat().formatJavadoc(true)
                forbidWildcardImports()
                trimTrailingWhitespace()
                endWithNewline()
            }
        }

        checkstyle {
            toolVersion = "10.12.4"
            configFile = rootProject.file("config/checkstyle/checkstyle.xml")
        }

        tasks.named("check") {
            dependsOn("spotlessCheck")
        }
    }
}

repositories {
    mavenCentral()
}


