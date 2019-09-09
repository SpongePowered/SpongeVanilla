import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    `kotlin-dsl`
    `java-library`
    idea
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
    implementation("net.minecraftforge.gradle:ForgeGradle:3.0.141")
    implementation(group = "org.spongepowered", name = "SpongeGradle", version = "0.11.0-SNAPSHOT")
}

//tasks {
//    val copyApi by creating(Copy::class) {
//        from("../SpongeCommon/SpongeAPI/buildSrc/src/main/kotlin") {
//            include("*.kt", "*.kts")
//        }
//        from("../SpongeAPI/buildSrc/src/main/kotlin") {
//            include("*.kt", "*.kts")
//        }
//        into("src/main/kotlin")
//    }
//
//    compileKotlin {
//        dependsOn(copyApi)
//    }
//}
//val proj = this
//subprojects {
//    configurations.forEach {
//        dependencies {
//            add(it.name, proj)
//        }
//    }
//}
//

//sourceSets {
//    val main by getting {
//        withConvention(KotlinSourceSet::class) {
//            kotlin.srcDirs("../SpongeCommon/buildSrc/src/main/kotlin", "../SpongeCommon/SpongeAPI/buildSrc/src/main/kotlin")
//        }
//    }
//}


