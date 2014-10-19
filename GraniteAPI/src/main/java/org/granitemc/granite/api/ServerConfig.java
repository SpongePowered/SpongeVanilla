package org.granitemc.granite.api;

import java.io.File;

public interface ServerConfig {
    File getPluginDataDirectory();

    File getPluginDirectory();
}
