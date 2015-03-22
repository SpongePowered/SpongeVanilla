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

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Throwables;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javassist.ClassPool;
import mc.Bootstrap;
import org.granitepowered.granite.impl.event.state.GraniteConstructionEvent;
import org.granitepowered.granite.impl.event.state.GraniteInitializationEvent;
import org.granitepowered.granite.impl.event.state.GraniteLoadCompleteEvent;
import org.granitepowered.granite.impl.event.state.GranitePostInitializationEvent;
import org.granitepowered.granite.impl.event.state.GranitePreInitializationEvent;
import org.granitepowered.granite.impl.guice.GraniteGuiceModule;
import org.granitepowered.granite.impl.plugin.GranitePluginManager;
import org.granitepowered.granite.loader.DeobfuscatorTransformer;
import org.granitepowered.granite.loader.GraniteTweaker;
import org.granitepowered.granite.loader.Mappings;
import org.granitepowered.granite.loader.MappingsLoader;
import org.granitepowered.granite.loader.MinecraftLoader;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;

public class GraniteStartup extends Thread {

    String[] args;

    Mappings mappings;
    File minecraftJar;

    String serverVersion;
    String apiVersion;
    String buildNumber = "UNKNOWN";

    public GraniteStartup(String[] args) {
        this.args = args;
        this.setName("Granite Startup");
    }

    public static void main(String[] args) {
        new GraniteStartup(args).run();
    }

    public void run() {
        Injector injector = Guice.createInjector(new GraniteGuiceModule());
        Granite.setInstance(injector.getInstance(Granite.class));
        Granite.getInstance().setLogger(LoggerFactory.getLogger("Granite"));

        try {
            Properties versionProp = new Properties();
            InputStream versionIn = ClassLoader.getSystemResourceAsStream("version.properties");
            if (versionIn != null) {
                try {
                    versionProp.load(versionIn);

                    this.serverVersion = versionProp.getProperty("server", "UNKNOWN");
                    this.apiVersion = versionProp.getProperty("api", "UNKNOWN");

                    String build = versionProp.getProperty("build");
                    if (build != null && !build.equals("NA")) {
                        this.buildNumber = build;
                    }
                } catch (IOException ignored) {
                } finally {
                    try {
                        versionIn.close();
                    } catch (IOException ignored) {
                    }
                }
            }
            Granite.getInstance().getLogger()
                    .info("Starting Granite implementationVersion " + this.serverVersion + " build " + this.buildNumber
                            + " implementing API implementationVersion " + this.apiVersion);

            Granite.getInstance().setImplementationVersion(this.serverVersion);
            Granite.getInstance().setApiVersion(this.apiVersion);

            Granite.getInstance().setServerConfig(new ServerConfig());
            Granite.getInstance().setClassPool(ClassPool.getDefault());

            Granite.getInstance().getEventManager().post(new GraniteConstructionEvent());

            Granite.getInstance().createGson();

            loadMinecraft();

            loadMappings();

            DeobfuscatorTransformer.mappings = mappings;
            DeobfuscatorTransformer.minecraftJar = minecraftJar;
            DeobfuscatorTransformer.init();

            bootstrap();

            // TODO: Create new Registering Class with hard coded sounds (even though i hate hard coding soo much);

            ((GranitePluginManager) Granite.getInstance().getPluginManager()).loadPlugins();

            // TODO: Set the Server instance
            //Granite.getInstance().setServer();

            Granite.getInstance().getEventManager().post(new GranitePreInitializationEvent());
            Granite.getInstance().getEventManager().post(new GraniteInitializationEvent());
            Granite.getInstance().getEventManager().post(new GranitePostInitializationEvent());
            Granite.getInstance().getEventManager().post(new GraniteLoadCompleteEvent());

            Date date = new Date();
            String day = new SimpleDateFormat("dd").format(date);
            String month = new SimpleDateFormat("MM").format(date);
            String year = new SimpleDateFormat("yyyy").format(date);
            if (Objects.equals(day + month, "0101")) {
                Granite.getInstance().getLogger().info("HAPPY NEW YEAR!");
            }
            if (Objects.equals(day + month, "2208")) {
                Granite.getInstance().getLogger().info("Happy Birthday Voltasalt!");
            }
            if (Objects.equals(day + month, "0709")) {
                String start = "2014";
                Granite.getInstance().getLogger()
                        .info("Happy Birthday Granite! Granite is " + Integer.toString(Integer.parseInt(year) - Integer.parseInt(start)) + " today!");
            }
            if (Objects.equals(day + month, "2310")) {
                Granite.getInstance().getLogger().info("Happy Birthday AzureusNation!");
            }
            if (Objects.equals(day + month, "3110")) {
                Granite.getInstance().getLogger().info("Happy Halloween!");
            }
            if (Objects.equals(day + month, "2412")) {
                Granite.getInstance().getLogger().info("Santa is getting ready!");
            }
            if (Objects.equals(day + month, "2512")) {
                Granite.getInstance().getLogger().info("Merry Christmas/Happy Holidays!");
            }
            if (Objects.equals(day + month, "3112")) {
                Granite.getInstance().getLogger().info("New Years Eve. Make way for " + Integer.toString(Integer.parseInt(year) + 1) + "!");
            }
        } catch (Throwable t) {
            Granite.error("We did a REALLY BIG boo-boo :'(", t);
        }
    }

    private void bootstrap() {
        Granite.getInstance().getLogger().info("Bootstrapping Minecraft");

        Bootstrap.register();
    }

    private void downloadMappings(File mappingsFile, String url, HttpRequest req) {
        Granite.getInstance().getLogger().warn("Downloading mappings from " + url);
        if (req.code() == 404) {
            throw new RuntimeException("GitHub 404 error whilst trying to download mappings");
        } else if (req.code() == 200) {
            req.receive(mappingsFile);
            Granite.getInstance().getServerConfig().set("latest-mappings-etag", req.eTag());
            Granite.getInstance().getServerConfig().save();
        }
    }

    private void loadMappings() {
        File mappingsFile = new File(Granite.getInstance().getServerConfig().getMappingsFile().getAbsolutePath());
        String url = "https://raw.githubusercontent.com/GraniteTeam/GraniteMappings/mixin/1.8.3.json";
        try {
            HttpRequest req = HttpRequest.get(url);

            if (Granite.getInstance().getServerConfig().getAutomaticMappingsUpdating()) {
                Granite.getInstance().getLogger().info("Querying Granite for updates");
                if (!mappingsFile.exists()) {
                    Granite.getInstance().getLogger().warn("Could not find mappings.json");
                    downloadMappings(mappingsFile, url, req);
                } else if (!Objects.equals(req.eTag(), Granite.getInstance().getServerConfig().getLatestMappingsEtag())) {
                    Granite.getInstance().getLogger().info("Update found");
                    downloadMappings(mappingsFile, url, req);
                }
            }
        } catch (HttpRequest.HttpRequestException e) {
            Granite.getInstance().getLogger().warn("Could not reach Granite mappings, falling back to local");

            if (!mappingsFile.exists()) {
                Granite.getInstance().getLogger()
                        .warn("Could not find local mappings file. Obtain it (somehow) and place it in the server's root directory called "
                                + "\"mappings.json\"");
                Throwables.propagate(e);
            } else {
                Granite.error(e);
            }
        }
        mappings = MappingsLoader.load(mappingsFile);
    }

    private void loadMinecraft() {
        String version = "1.8.3";
        minecraftJar = new File("minecraft_server." + version + ".jar");

        if (!minecraftJar.exists()) {
            Granite.getInstance().getLogger().warn("Could not find Minecraft .jar, downloading");
            HttpRequest
                    req =
                    HttpRequest.get("https://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/minecraft_server." + version + ".jar");
            if (req.code() == 404) {
                throw new RuntimeException("404 error whilst trying to download Minecraft");
            } else if (req.code() == 200) {
                req.receive(minecraftJar);
                Granite.getInstance().getLogger().info("Minecraft Downloaded");
            }
        }

        String minecraftVersion = minecraftJar.getName().replace("minecraft_server.", "Minecraft ").replace(".jar", "");

        MinecraftLoader.createPool(minecraftJar);
        try {
            GraniteTweaker.loader.addURL(minecraftJar.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}

