plugins {
    id("org.spongepowered.gradle.sponge.impl")
    id("net.minecraftforge.gradle")
}

val commonProj = project(":SpongeCommon")


dependencies {
    minecraft("net.minecraft:server:1.14.4")
}

minecraft {
    mappings("snapshot", commonProj.properties["mcpMappings"]!! as String)
//
//    runs {
//        server {
//            workingDirectory = project.file("../run")
//
//        }
//    }
}


spongeDev {
}
