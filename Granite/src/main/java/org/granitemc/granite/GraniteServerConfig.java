package org.granitemc.granite;

import org.granitemc.granite.api.ServerConfig;
import org.granitemc.granite.api.config.CfgFile;

import java.io.File;
import java.io.IOException;

public class GraniteServerConfig implements ServerConfig {
    CfgFile file;

    public GraniteServerConfig() {
        file = new CfgFile(new File("granite.conf"));

        file.addDefault("plugin-directory", "plugins/");
        file.addDefault("plugin-data-directory", "plugins/");

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
}
