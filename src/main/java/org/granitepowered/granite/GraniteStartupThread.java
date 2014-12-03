/*
 * License (MIT)
 *
 * Copyright (c) 2014 Granite Team
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
import org.granitepowered.granite.impl.GraniteServer;
import org.granitepowered.granite.mappings.Mappings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

public class GraniteStartupThread extends Thread {

    String[] args;

    public GraniteStartupThread(String args[]) {
        this.args = args;
        this.setName("Granite Startup");
    }

    public void run() {
        String version = null;

        Properties mavenProp = new Properties();
        InputStream in = java.lang.ClassLoader.getSystemClassLoader().getResourceAsStream("META-INF/maven/org.granitemc/granite/pom.properties");
        if (in != null) {
            try {
                mavenProp.load(in);

                version = mavenProp.getProperty("version");
            } catch (IOException ignored) {
            } finally {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }

        if (version == null) version = "UNKNOWN";

        Granite.instance = new Granite();
        Granite.instance.version = version;

        Granite.instance.serverConfig = new ServerConfig();

        loadMinecraft();

        Mappings.load();

        bootstrap();

        GraniteServer server = new GraniteServer();
        Granite.instance.server = server;

        System.out.println("Starting Granite version " + version);
    }

    private void bootstrap() {
        System.out.println("Bootstrapping Minecraft");

        Mappings.invokeStatic("Bootstrap", "func_151354_b");
    }

    private void loadMinecraft() {
        File minecraftJar = Granite.instance.getServerConfig().getMinecraftJar();

        if (!minecraftJar.exists()) {
            System.out.println("Could not find Minecraft .jar, downloading");
            HttpRequest req = HttpRequest.get("https://s3.amazonaws.com/Minecraft.Download/versions/1.8.1/minecraft_server.1.8.1.jar");
            req.receive(minecraftJar);
        }

        System.out.println("Loading " + minecraftJar.getName());

        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(ClassLoader.getSystemClassLoader(), Granite.instance.getServerConfig().getMinecraftJar().toURI().toURL());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
