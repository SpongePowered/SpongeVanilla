import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    `kotlin-dsl`
    `java-library`
}

subprojects {
    dependencies {
        gradleApi()
        gradleKotlinDsl()
    }
//    configurations.all {
//        dependencies {
//            add(name, "org.jetbrains.kotlin:kotlin-script-runtime:1.3.21")
//        }
//    }
    apply(plugin = "org.gradle.kotlin.kotlin-dsl")
}
repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    jcenter()
    maven(url = "https://files.minecraftforge.net/maven")
}
dependencies {
    implementation("net.minecrell.licenser:net.minecrell.licenser.gradle.plugin:0.4.1")
    implementation("com.github.jengelman.gradle.plugins:shadow:5.0.0")
    implementation("org.spongepowered.plugin:org.spongepowered.plugin.gradle.plugin:0.8.1")
    implementation("net.minecraftforge.gradle:ForgeGradle:3.0.105")
    implementation("de.sebastianboegl.gradle.plugins:shadow-log4j-transformer:1.0.1")
    implementation(group = "org.spongepowered", name = "SpongeGradle", version = "0.11.0-SNAPSHOT")
}
tasks {
    val copyApi by creating {
    }
}

//sourceSets {
//    val main by getting {
//        withConvention(KotlinSourceSet::class) {
//            kotlin.srcDirs("../SpongeCommon/buildSrc/src/main/kotlin", "../SpongeCommon/SpongeAPI/buildSrc/src/main/kotlin")
//        }
//    }
//}


