package org.granitemc.granite;

import org.granitemc.granite.api.ServerConfig;
import org.granitemc.granite.api.config.CfgFile;

import java.io.File;
import java.io.IOException;

public class GraniteServerConfig implements ServerConfig {
    public CfgFile file;

    public GraniteServerConfig() {
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
    }

}
