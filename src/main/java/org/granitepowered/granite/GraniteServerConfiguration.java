package org.granitepowered.granite;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ash on 02/12/2014.
 */
public class GraniteServerConfiguration {

    /*public CfgFile file;

    public GraniteServerConfiguration() {
        file = new CfgFile(new File("granite.conf"));

        file.addDefault("plugin-directory", "plugins/");
        file.addDefault("plugin-data-directory", "plugins/");
        file.addDefault("minecraft-jar", "minecraft_server.jar");
        file.addDefault("libraries-directory", "lib/");
        file.addDefault("mappings-file", "mappings.json");
        file.addDefault("latest-mappings-etag", "");
        file.addDefault("automatic-mappings-updating", true);

        try {
            file.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        getPluginDataDirectory().mkdirs();
        getPluginDirectory().mkdirs();
        getLibrariesDirectory().mkdirs();
    }

    @Override
    public File getPluginDataDirectory() {
        return new File(file.getString("plugin-data-directory"));
    }

    @Override
    public File getPluginDirectory() {
        return new File(file.getString("plugin-directory"));
    }

    @Override
    public File getMinecraftJar() {
        return new File(file.getString("minecraft-jar"));
    }

    @Override
    public File getLibrariesDirectory() {
        return new File(file.getString("libraries-directory"));
    }

    @Override
    public File getMappingsFile() {
        return new File(file.getString("mappings-file"));
    }

    @Override
    public String getLatestMappingsEtag() {
        return file.getString("latest-mappings-etag");
    }

    @Override
    public boolean getAutomaticMappingsUpdating() {
        return file.getBoolean("automatic-mappings-updating");
    }*/

}
