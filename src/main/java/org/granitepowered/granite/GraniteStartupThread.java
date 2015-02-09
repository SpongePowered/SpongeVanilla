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
import javassist.NotFoundException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.granitepowered.granite.bytecode.BytecodeModifier;
import org.granitepowered.granite.bytecode.classes.CommandHandlerClass;
import org.granitepowered.granite.bytecode.classes.DedicatedServerClass;
import org.granitepowered.granite.bytecode.classes.EntityClass;
import org.granitepowered.granite.bytecode.classes.EntityEggClass;
import org.granitepowered.granite.bytecode.classes.EntityLightningBoltClass;
import org.granitepowered.granite.bytecode.classes.EntityLivingBaseClass;
import org.granitepowered.granite.bytecode.classes.EntityPlayerMPClass;
import org.granitepowered.granite.bytecode.classes.EntityXFireballClass;
import org.granitepowered.granite.bytecode.classes.InstantiatorClass;
import org.granitepowered.granite.bytecode.classes.ItemInWorldManagerClass;
import org.granitepowered.granite.bytecode.classes.ItemStackClass;
import org.granitepowered.granite.bytecode.classes.NetHandlerPlayServerClass;
import org.granitepowered.granite.bytecode.classes.ServerConfigurationManagerClass;
import org.granitepowered.granite.bytecode.classes.WorldProviderClass;
import org.granitepowered.granite.impl.GraniteGameVersion;
import org.granitepowered.granite.impl.GraniteServer;
import org.granitepowered.granite.impl.event.state.GraniteConstructionEvent;
import org.granitepowered.granite.impl.event.state.GraniteInitializationEvent;
import org.granitepowered.granite.impl.event.state.GraniteLoadCompleteEvent;
import org.granitepowered.granite.impl.event.state.GranitePostInitializationEvent;
import org.granitepowered.granite.impl.event.state.GranitePreInitializationEvent;
import org.granitepowered.granite.impl.guice.GraniteGuiceModule;
import org.granitepowered.granite.impl.text.chat.GraniteChatType;
import org.granitepowered.granite.impl.text.format.GraniteTextColor;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.util.ReflectionUtils;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.text.action.GraniteTextActionFactory;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.chat.GraniteChatTypeFactory;
import org.spongepowered.api.text.format.GraniteTextFormatFactory;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.message.GraniteMessageFactory;
import org.spongepowered.api.text.message.Messages;
import org.spongepowered.api.text.title.GraniteTitleFactory;
import org.spongepowered.api.text.title.Titles;
import org.spongepowered.api.text.translation.GraniteTranslationFactory;
import org.spongepowered.api.text.translation.Translations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class GraniteStartupThread extends Thread {

    String[] args;
    BytecodeModifier modifier;

    String serverVersion;
    String apiVersion;
    String buildNumber = "UNKNOWN";

    public GraniteStartupThread(String[] args) {
        this.args = args;
        this.setName("Granite Startup");
    }

    public void run() {
        Injector injector = Guice.createInjector(new GraniteGuiceModule());
        Granite.instance = injector.getInstance(Granite.class);
        Granite.instance.logger = LoggerFactory.getLogger("Granite");

        try {
            Properties versionProp = new Properties();
            InputStream versionIn = ClassLoader.getSystemResourceAsStream("version.properties");
            if (versionIn != null) {
                try {
                    versionProp.load(versionIn);

                    serverVersion = versionProp.getProperty("server", "UNKNOWN");
                    apiVersion = versionProp.getProperty("api", "UNKNOWN");

                    String build = versionProp.getProperty("build");
                    if (build != null && !build.equals("NA")) {
                        buildNumber = build;
                    }
                } catch (IOException ignored) {
                } finally {
                    try {
                        versionIn.close();
                    } catch (IOException ignored) {
                    }
                }
            }

            Granite.instance.version = serverVersion;
            Granite.instance.apiVersion = apiVersion;

            Granite.instance.serverConfig = new ServerConfig();
            Granite.instance.classPool = ClassPool.getDefault();

            Granite.instance.eventManager.post(new GraniteConstructionEvent());

            Granite.instance.classesDir = new File("classes/");
            Granite.instance.classesDir.mkdirs();

            Granite.instance.createGson();

            loadMinecraftToClassPool();

            Mappings.load();

            modifyBytecode();

            loadClasses();

            bootstrap();

            Granite.instance.gameRegistry.register();

            injectSpongeFields();

            Granite.instance.pluginManager.loadPlugins();

            Granite.instance.server = (GraniteServer) injector.getInstance(Game.class);

            Granite.instance.eventManager.post(new GranitePreInitializationEvent());
            Granite.instance.eventManager.post(new GraniteInitializationEvent());
            Granite.instance.eventManager.post(new GranitePostInitializationEvent());
            Granite.instance.eventManager.post(new GraniteLoadCompleteEvent());

            Granite.instance.getLogger()
                    .info("Starting Granite version " + serverVersion + " build " + buildNumber + " implementing API version " + apiVersion);

            Date date = new Date();
            String day = new SimpleDateFormat("dd").format(date);
            String month = new SimpleDateFormat("MM").format(date);
            String year = new SimpleDateFormat("yyyy").format(date);
            if (Objects.equals(day + month, "0101")) {
                Granite.instance.getLogger().info("HAPPY NEW YEAR!");
            }
            if (Objects.equals(day + month, "2208")) {
                Granite.instance.getLogger().info("Happy Birthday Voltasalt!");
            }
            if (Objects.equals(day + month, "0709")) {
                String start = "2014";
                Granite.instance.getLogger()
                        .info("Happy Birthday Granite! Granite is " + Integer.toString(Integer.parseInt(year) - Integer.parseInt(start)) + " today!");
            }
            if (Objects.equals(day + month, "2310")) {
                Granite.instance.getLogger().info("Happy Birthday AzureusNation!");
            }
            if (Objects.equals(day + month, "3110")) {
                Granite.instance.getLogger().info("Happy Halloween!");
            }
            if (Objects.equals(day + month, "2412")) {
                Granite.instance.getLogger().info("Santa is getting ready!");
            }
            if (Objects.equals(day + month, "2512")) {
                Granite.instance.getLogger().info("Merry Christmas/Happy Holidays!");
            }
            if (Objects.equals(day + month, "3112")) {
                Granite.instance.getLogger().info("New Years Eve. Make way for " + Integer.toString(Integer.parseInt(year) + 1) + "!");
            }
        } catch (Throwable t) {
            Granite.error("We did a REALLY BIG boo-boo :'(", t);
        }
    }

    private void loadClasses() {
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);

            method.invoke(ClassLoader.getSystemClassLoader(), Granite.instance.getClassesDir().toURI().toURL());

            modifier.post();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IOException e) {
            Throwables.propagate(e);
        }
    }

    private void injectSpongeFields() {
        Granite.instance.getLogger().info("Injecting Sponge fields");

        injectConstant(Messages.class, "factory", new GraniteMessageFactory());
        injectConstant(TextStyles.class, "factory", new GraniteTextFormatFactory());
        injectConstant(TextActions.class, "factory", new GraniteTextActionFactory());
        injectConstant(Translations.class, "factory", new GraniteTranslationFactory());
        injectConstant(ChatTypes.class, "factory", new GraniteChatTypeFactory());
        injectConstant(Titles.class, "factory", new GraniteTitleFactory());

        injectEnumConstants(TextColors.class, GraniteTextColor.class);

        Map<String, TextStyle.Base> styles = new HashMap<>();
        for (Map.Entry<String, TextStyle.Base> entry : GraniteTextFormatFactory.styles.entrySet()) {
            styles.put(entry.getKey().toUpperCase(), entry.getValue());
        }

        injectConstants(TextStyles.class, styles);
        injectEnumConstants(ChatTypes.class, GraniteChatType.class);

        injectConstants(ParticleTypes.class, Granite.getInstance().getGameRegistry().particleTypes);
    }

    private void injectEnumConstants(Class<?> destination, Class<? extends Enum> source) {
        for (Enum constant : source.getEnumConstants()) {
            injectConstant(destination, constant.name(), constant);
        }
    }

    private void injectConstants(Class<?> clazz, Map<String, ?> objects) {
        for (Map.Entry<String, ?> entry : objects.entrySet()) {
            injectConstant(clazz, entry.getKey(), entry.getValue());
        }
    }

    private void injectConstant(Class<?> clazz, String name, Object value) {
        try {
            Field f = clazz.getDeclaredField(name);
            ReflectionUtils.forceAccessible(f);

            f.set(null, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Throwables.propagate(e);
        }
    }

    private void modifyBytecode() {
        File buildNumberFile = new File(Granite.instance.getClassesDir(), "buildnumber.txt");
        try {
            modifier = new BytecodeModifier();

            modifier.add(new CommandHandlerClass());
            modifier.add(new DedicatedServerClass());
            modifier.add(new EntityClass());
            modifier.add(new EntityEggClass());
            modifier.add(new EntityLightningBoltClass());
            modifier.add(new EntityLivingBaseClass());
            modifier.add(new EntityPlayerMPClass());
            modifier.add(new EntityXFireballClass("EntitySmallFireball"));
            modifier.add(new EntityXFireballClass("EntityLargeFireball"));
            modifier.add(new ItemInWorldManagerClass());
            modifier.add(new ItemStackClass());
            modifier.add(new NetHandlerPlayServerClass());
            modifier.add(new ServerConfigurationManagerClass());
            modifier.add(new WorldProviderClass());

            modifier.add(new InstantiatorClass());

            if (buildNumber.equals("UNKNOWN") || !buildNumberFile.exists() || !Objects
                    .equals(FileUtils.readFileToString(buildNumberFile), buildNumber)) {
                Granite.instance.getLogger().info("Modifying bytecode");

                if (Granite.instance.classesDir.exists()) {
                    File oldClassesDir = new File(Granite.instance.classesDir.getParentFile(), Granite.instance.classesDir.getName() + "_old");

                    if (oldClassesDir.exists()) {
                        FileUtils.deleteDirectory(oldClassesDir);
                    }

                    FileUtils.moveDirectory(Granite.instance.classesDir, oldClassesDir);
                    FileUtils.deleteDirectory(oldClassesDir);
                }

                try {
                    JarFile file = new JarFile(Granite.instance.getServerConfig().getMinecraftJar());
                    Enumeration<JarEntry> entries = file.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();

                        File f = new File(Granite.instance.getClassesDir() + java.io.File.separator + entry.getName());

                        if (entry.isDirectory()) {
                            f.mkdirs();
                        } else {
                            FileOutputStream os = new FileOutputStream(f);
                            IOUtils.copy(file.getInputStream(entry), os);
                            os.close();
                        }
                    }

                    modifier.modify();

                    FileUtils.write(buildNumberFile, buildNumber + "");
                } catch (IOException e) {
                    Throwables.propagate(e);
                }
            } else {
                Granite.instance.getLogger().info("Found pre-modified bytecode in classes/, loading that");
            }
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

    private void bootstrap() {
        Granite.instance.getLogger().info("Bootstrapping Minecraft");

        Mappings.invokeStatic("Bootstrap", "func_151354_b");
    }

    private void loadMinecraftToClassPool() {
        File minecraftJar = Granite.instance.getServerConfig().getMinecraftJar();

        if (!minecraftJar.exists()) {
            Granite.instance.getLogger().warn("Could not find Minecraft .jar, downloading");
            HttpRequest req = HttpRequest.get("https://s3.amazonaws.com/Minecraft.Download/versions/1.8.1/minecraft_server.1.8.1.jar");
            if (req.code() == 404) {
                throw new RuntimeException("404 error whilst trying to download Minecraft");
            } else if (req.code() == 200) {
                req.receive(minecraftJar);
                Granite.instance.getLogger().info("Minecraft Downloaded");
            }
        }

        String minecraftVersion = minecraftJar.getName().replace("minecraft_server.", "Minecraft ").replace(".jar", "");
        int protocol = 47;
        Granite.instance.minecraftVersion = new GraniteGameVersion(minecraftVersion, protocol);

        Granite.instance.getLogger().info("Loading " + minecraftVersion + " with protocol " + protocol);

        try {
            Granite.getInstance().classPool.insertClassPath(minecraftJar.getName());
        } catch (NotFoundException e) {
            Throwables.propagate(e);
        }
    }
}

