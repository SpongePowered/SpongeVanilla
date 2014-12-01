package org.granitemc.granite;

/*
 * License (MIT)
 *
 * Copyright (c) 2014. Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
    }

}
