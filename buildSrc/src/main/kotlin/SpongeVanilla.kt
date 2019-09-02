import org.gradle.api.Project

object SpongeVanilla {

    var project: Project? = null

    val isClient = false
    val minecraftGroup = VanillaLibs.Groups.minecraft
    val minecraftDep = VanillaLibs.Groups.minecraft
    var version = VanillaVersions.minecraft + "-" + "8" + "0"

}

object VanillaPlugins {
    const val forgeGradle = VanillaLibs.Groups.forgegradle
}

object VanillaLibs {


    object Groups {
        const val forge = "net.minecraftforge"
        const val forgegradle = "${forge}.gradle"
        const val minecraft = "net.minecraft"

    }

    object Modules {
        const val forgegradle = "ForgeGradle"
        const val server = "server"
        const val joined = "joined"
        const val client = "client"

    }
}

object VanillaVersions {
    const val api = "7.2.0-SNAPSHOT"
    const val forgeGradle = "3.0.105"
    const val guava = "21.0"
    const val gson = "2.8.0"
    const val errorprone = "2.0.15"
    const val slf4j = "1.7.25"
    const val caffeine = "2.5.4"
    const val configurate = "3.6"
    const val `flow-math` = "1.0.3"
    const val `flow-noise` = "1.0.1-SNAPSHOT"
    const val `event-impl-gen` = "5.7.0"
    const val spongegradle = "0.8.1"
    const val asm = "5.2"
    const val licenser = "0.4.1"
    const val shadow = "5.0.0"
    const val apache_commons = "3.5"
    const val jsr305 = "3.0.1"
    const val guice = "4.1.0"
    const val plugin_meta = "0.4.1"
    const val minecraft = "1.14.3"

    object Test {
        const val junit = "4.12"
        const val hamcrest = "1.3"
        const val mockito = "2.8.47"
    }
}