/*
 * License (MIT)
 *
 * Copyright (c) 2014-2015 Granite Team
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

package org.granitepowered.granite;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValueFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class ServerConfig {

    public Config config;
    public File configLocation;

    public ServerConfig() {
        configLocation = new File("granite.conf");
        config = ConfigFactory.parseFile(configLocation);

        try (Reader confReader = new InputStreamReader(getClass().getResourceAsStream("/granite.conf"))) {
            config = config.withFallback(ConfigFactory.parseReader(confReader));
        } catch (IOException e) {
            e.printStackTrace();
        }

        save();

        getPluginDataDirectory().mkdirs();
        getPluginDirectory().mkdirs();
        getLibrariesDirectory().mkdirs();
    }

    public void save() {
        String saved = config.root().render(ConfigRenderOptions.defaults().setOriginComments(false));

        try {
            FileUtils.write(configLocation, saved);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getPluginDataDirectory() {
        return new File(config.getString("plugin-data-directory"));
    }

    public File getPluginDirectory() {
        return new File(config.getString("plugin-directory"));
    }

    public File getMinecraftJar() {
        return new File(config.getString("minecraft-jar"));
    }

    public File getLibrariesDirectory() {
        return new File(config.getString("libraries-directory"));
    }

    public File getMappingsFile() {
        return new File(config.getString("mappings-file"));
    }

    public String getLatestMappingsEtag() {
        return config.getString("latest-mappings-etag");
    }

    public boolean getAutomaticMappingsUpdating() {
        return config.getBoolean("automatic-mappings-updating");
    }

    public void set(String key, Object value) {
        config = config.withValue(key, ConfigValueFactory.fromAnyRef(value));
    }
}
