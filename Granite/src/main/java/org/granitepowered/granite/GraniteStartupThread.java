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
import org.granitepowered.granite.bytecode.classes.*;
import org.granitepowered.granite.impl.event.state.*;
import org.granitepowered.granite.impl.guice.GraniteGuiceModule;
import org.granitepowered.granite.impl.plugin.GranitePluginManager;
import org.granitepowered.granite.impl.text.chat.GraniteChatType;
import org.granitepowered.granite.impl.text.format.GraniteTextColor;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.util.ReflectionUtils;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Server;
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
import java.util.*;
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
                    .info("Starting Granite implementationVersion " + this.serverVersion + " build " + this.buildNumber + " implementing API implementationVersion " + this.apiVersion);

            Granite.getInstance().setImplementationVersion(this.serverVersion);
            Granite.getInstance().setApiVersion(this.apiVersion);

            Granite.getInstance().setServerConfig(new ServerConfig());
            Granite.getInstance().setClassPool(ClassPool.getDefault());

            Granite.getInstance().getEventManager().post(new GraniteConstructionEvent());

            Granite.getInstance().setClassesDir(new File("classes/"));
            Granite.getInstance().getClassesDir().mkdirs();

            Granite.getInstance().createGson();

            loadMinecraftToClassPool();

            Mappings.load();

            modifyBytecode();

            loadClasses();

            bootstrap();

            postLoadClasses();

            //TODO: Create new Registering Class with hard coded sounds (even though i hate hard coding soo uch);

            injectSpongeFields();

            ((GranitePluginManager) Granite.getInstance().getPluginManager()).loadPlugins();

            Granite.getInstance().setServer(injector.getInstance(Server.class));

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

    private void loadClasses() {
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);

            method.invoke(ClassLoader.getSystemClassLoader(), Granite.getInstance().getClassesDir().toURI().toURL());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IOException e) {
            Throwables.propagate(e);
        }
    }

    private void postLoadClasses() {
        modifier.post();
    }

    private void injectSpongeFields() {
        Granite.getInstance().getLogger().info("Injecting Sponge fields");

        injectConstant(Messages.class, "factory", new GraniteMessageFactory());
        injectConstant(TextStyles.class, "factory", new GraniteTextFormatFactory());
        injectConstant(TextActions.class, "factory", new GraniteTextActionFactory());
        injectConstant(Translations.class, "factory", new GraniteTranslationFactory());
        injectConstant(ChatTypes.class, "factory", new GraniteChatTypeFactory());
        injectConstant(Titles.class, "factory", new GraniteTitleFactory());

        injectEnumConstants(TextColors.class, GraniteTextColor.class);
        injectEnumConstants(ChatTypes.class, GraniteChatType.class);

        Map<String, TextStyle.Base> styles = new HashMap<>();
        for (Map.Entry<String, TextStyle.Base> entry : GraniteTextFormatFactory.styles.entrySet()) {
            styles.put(entry.getKey().toUpperCase(), entry.getValue());
        }
        injectConstants(TextStyles.class, styles);
        //injectConstants(ParticleTypes.class, Granite.getInstance().getRegistry().particleTypes);
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
        File buildNumberFile = new File(Granite.getInstance().getClassesDir(), "buildnumber.txt");
        try {
            modifier = new BytecodeModifier();

            modifier.add(new CommandHandlerClass());
            modifier.add(new DedicatedServerClass());
            modifier.add(new EntityMinecartClass());
            modifier.add(new EntityClass());
            modifier.add(new EntityBoatClass());
            modifier.add(new EntityEggClass());
            modifier.add(new EntityEnderPearlClass());
            modifier.add(new EntityFishHookClass());
            modifier.add(new EntityLightningBoltClass());
            modifier.add(new EntityLivingBaseClass());
            modifier.add(new EntityPlayerMPClass());
            modifier.add(new EntitySnowballClass());
            modifier.add(new EntityXFireballClass("EntitySmallFireball"));
            modifier.add(new EntityXFireballClass("EntityLargeFireball"));
            modifier.add(new ItemInWorldManagerClass());
            modifier.add(new ItemStackClass());
            modifier.add(new NetHandlerHandshakeTCPClass());
            modifier.add(new NetHandlerPlayServerClass());
            modifier.add(new ServerConfigurationManagerClass());
            modifier.add(new WorldProviderClass());

            modifier.add(new InstantiatorClass());

            if (buildNumber.equals("UNKNOWN") || !buildNumberFile.exists() || !Objects
                    .equals(FileUtils.readFileToString(buildNumberFile), buildNumber)) {
                Granite.getInstance().getLogger().info("Modifying bytecode");

                if (Granite.getInstance().getClassesDir().exists()) {
                    File oldClassesDir = new File(Granite.getInstance().getClassesDir().getParentFile(), Granite.getInstance().getClassesDir().getName() + "_old");

                    if (oldClassesDir.exists()) {
                        forceDeleteFolder(oldClassesDir);
                    }

                    FileUtils.moveDirectory(Granite.getInstance().getClassesDir(), oldClassesDir);
                    forceDeleteFolder(oldClassesDir);
                }

                try {
                    JarFile file = new JarFile("minecraft_server." + Granite.getInstance().getMinecraftVersion().getName().split(" ")[1] + ".jar");
                    Enumeration<JarEntry> entries = file.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();

                        File f = new File(Granite.getInstance().getClassesDir() + java.io.File.separator + entry.getName());

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
                Granite.getInstance().getLogger().info("Found pre-modified bytecode in classes/, loading that");
            }
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

    private void bootstrap() {
        Granite.getInstance().getLogger().info("Bootstrapping Minecraft");

        Mappings.invokeStatic("Bootstrap", "register");
    }

    private void loadMinecraftToClassPool() {
        String version = "1.8.3";
        File minecraftJar = new File("minecraft_server." + version + ".jar");

        if (!minecraftJar.exists()) {
            Granite.getInstance().getLogger().warn("Could not find Minecraft .jar, downloading");
            HttpRequest req = HttpRequest.get("https://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/minecraft_server." + version + ".jar");
            if (req.code() == 404) {
                throw new RuntimeException("404 error whilst trying to download Minecraft");
            } else if (req.code() == 200) {
                req.receive(minecraftJar);
                Granite.getInstance().getLogger().info("Minecraft Downloaded");
            }
        }

        String minecraftVersion = minecraftJar.getName().replace("minecraft_server.", "Minecraft ").replace(".jar", "");

        //TODO: Register minecraft version after minecraft has loaded so there no linger needs to be any hard coded information.

        Granite.getInstance().getLogger().info("Loading " + minecraftVersion);

        try {
            Granite.getInstance().getClassPool().insertClassPath(minecraftJar.getName());
        } catch (NotFoundException e) {
            Throwables.propagate(e);
        }
    }

    private void forceDeleteFolder(File folder) {
        try {
            FileUtils.deleteDirectory(folder);
        } catch (IOException e) {
            if (e.getMessage().startsWith("Unable to delete directory")) {
                Granite.getInstance().getLogger().info("Unable to delete old classes, retrying in one second");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                forceDeleteFolder(folder);
            }
        }
    }
}

