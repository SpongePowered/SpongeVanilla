package com.mythicmc.mythic;

import com.mythicmc.mythic.utils.Logger;
import com.mythicmc.mythic.utils.ServerComposite;
import com.mythicmc.mythic.utils.ClassLoader;

import java.io.File;
import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        //load the server jar into the default classloader
        try {
            File jarFile = new File("./minecraft_server.jar");
            ClassLoader.addURL(jarFile.toURI().toURL());
            Logger.info("Loaded server: " + jarFile.getName());
        } catch (IOException e) {
            Logger.error("Failed to load minecraft_server.jar . Please make sure it exists in the same directory.");
            e.printStackTrace();
            return;
        }

        //now that classes are loaded, let's attempt to create the composite layer
        ServerComposite.create(args);

    }
}
