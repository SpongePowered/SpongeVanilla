package org.granitemc.granite.api;

import java.io.File;

public interface ServerConfig {
    File getPluginDataDirectory();

    File getPluginDirectory();

    File getMinecraftJar();

    File getLibrariesDirectory();

    File getMappingsFile();

    String getLatestMappingsEtag();

    boolean getAutomaticMappingsUpdating();
}
