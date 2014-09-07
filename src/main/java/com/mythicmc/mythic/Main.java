package com.mythicmc.mythic;

import java.io.File;
import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        //load the server jar into the default classloader
        try {
            File jarfile = new File("./minecraft_server.jar");
            SystemClassLoader.addURL(jarfile.toURI().toURL());
            Logger.info("Loaded server: " + jarfile.getName());
        } catch (IOException e) {
            Logger.error("Failed to load minecraft_server.jar . Please make sure it exists in the same directory.");
            e.printStackTrace();
            return;
        }

        //now that classes are loaded, let's attempt to create the composite layer
        ServerComposite.create(args);

    }
}
