/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.server.launch;

import static org.spongepowered.server.launch.VanillaCommandLine.HELP;
import static org.spongepowered.server.launch.VanillaCommandLine.NO_DOWNLOAD;
import static org.spongepowered.server.launch.VanillaCommandLine.NO_VERIFY_CLASSPATH;
import static org.spongepowered.server.launch.VanillaCommandLine.TWEAK_CLASS;
import static org.spongepowered.server.launch.VanillaCommandLine.VERSION;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import joptsimple.BuiltinHelpFormatter;
import joptsimple.OptionSet;
import net.minecraft.launchwrapper.Launch;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public final class VanillaServerMain {

    private static final String LIBRARIES_DIR = "libraries";

    private static final String MINECRAFT_SERVER_VERSION = "1.12.2";
    private static final String MINECRAFT_SERVER_LOCAL = "minecraft_server." + MINECRAFT_SERVER_VERSION + ".jar";
    private static final String MINECRAFT_MANIFEST_REMOTE = "https://launchermeta.mojang.com/mc/game/version_manifest.json";

    private static final String LAUNCHWRAPPER_PATH = "/net/minecraft/launchwrapper/1.12/launchwrapper-1.12.jar";
    private static final String LAUNCHWRAPPER_LOCAL = LIBRARIES_DIR + LAUNCHWRAPPER_PATH;
    private static final String LAUNCHWRAPPER_REMOTE = "https://libraries.minecraft.net" + LAUNCHWRAPPER_PATH;

    private static final String TWEAK_ARGUMENT = "--tweakClass";
    private static final String TWEAKER = "org.spongepowered.server.launch.VanillaServerTweaker";

    private VanillaServerMain() {
    }

    public static void main(String[] args) throws Exception {
        OptionSet options = VanillaCommandLine.parse(args);
        if (options.has(HELP)) {
            if (System.console() == null) {
                // We have no supported terminal, print help with default terminal width
                VanillaCommandLine.printHelp(System.err);
            } else {
                // Terminal is (very likely) supported, use the terminal width provided by jline
                Terminal terminal = TerminalBuilder.builder().dumb(true).build();
                VanillaCommandLine.printHelp(new BuiltinHelpFormatter(terminal.getWidth(), 3), System.err);
            }
            return;
        } else if (options.has(VERSION)) {
            final Package pack = VanillaServerMain.class.getPackage();
            System.out.println(pack.getImplementationTitle() + ' ' + pack.getImplementationVersion());
            System.out.println(pack.getSpecificationTitle() + ' ' + pack.getSpecificationVersion());
            return;
        }

        // Download/verify Minecraft server installation if necessary and not disabled
        if (!options.has(NO_VERIFY_CLASSPATH)) {
            // Get the location of our jar
            Path base = Paths.get(VanillaServerMain.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();

            try {
                // Download dependencies
                if (!downloadMinecraft(base, !options.has(NO_DOWNLOAD))) {
                    System.err.println("Failed to load all required dependencies. Please download them manually:");
                    System.err.println("Download the Minecraft server version " + MINECRAFT_SERVER_VERSION + " and copy it to "
                            + base.resolve(MINECRAFT_SERVER_LOCAL).toAbsolutePath());
                    System.err.println("Download " + LAUNCHWRAPPER_REMOTE + " and copy it to "
                            + base.resolve(LAUNCHWRAPPER_LOCAL).toAbsolutePath());
                    System.exit(1);
                    return;
                }
            } catch (IOException e) {
                System.err.println("Failed to download required dependencies. Please try again later.");
                e.printStackTrace();
                System.exit(1);
                return;
            }
        } else {
            System.err.println("Classpath verification is disabled. The server may NOT start properly unless you have all required dependencies on "
                    + "the classpath!");
        }

        Launch.main(getLaunchArguments(TWEAKER, options.valuesOf(TWEAK_CLASS)));
    }

    private static String[] getLaunchArguments(String primaryTweaker, List<String> tweakers) {
        if (tweakers.isEmpty()) {
            return new String[]{TWEAK_ARGUMENT, primaryTweaker};
        }

        String[] result = new String[tweakers.size() * 2 + 2];
        result[0] = TWEAK_ARGUMENT;
        result[1] = primaryTweaker;

        int i = 2;
        for (String tweaker : tweakers) {
            result[i++] = TWEAK_ARGUMENT;
            result[i++] = tweaker;
        }

        return result;
    }

    private static boolean downloadMinecraft(Path base, boolean autoDownload) throws IOException, NoSuchAlgorithmException {
        // Make sure the Minecraft server is available, or download it otherwise
        Path path = base.resolve(MINECRAFT_SERVER_LOCAL);
        if (Files.notExists(path)) {
            if (!autoDownload) {
                return false;
            }

            System.out.println("Downloading the versions manifest...");

            // Download the file with all of the Minecraft versions information
            JsonValue versions = downloadJson(MINECRAFT_MANIFEST_REMOTE);

            String versionManifestRemote = null;

            // Find the current version manifest URL
            for (JsonValue versionInfo : versions.asObject().get("versions").asArray()) {
                JsonObject obj = versionInfo.asObject();

                String versionId = obj.get("id").asString();
                if (versionId.equals(MINECRAFT_SERVER_VERSION)) {
                    versionManifestRemote = obj.get("url").asString();
                    break;
                }
            }

            if (versionManifestRemote == null) {
                throw new RuntimeException("Could not find " + MINECRAFT_SERVER_VERSION + "'s manifest URL");
            }

            JsonValue versionManifest = downloadJson(versionManifestRemote);
            JsonObject serverObj = versionManifest.asObject()
                    .get("downloads").asObject()
                    .get("server").asObject();

            // Find the server URL and SHA-1 digest
            String serverRemote = serverObj.get("url").asString();
            String sha1 = serverObj.get("sha1").asString();

            downloadAndVerify(serverRemote, path, sha1);
        }

        path = base.resolve(LAUNCHWRAPPER_LOCAL);

        if (!Files.exists(path)) {
            if (!autoDownload) {
                return false;
            }

            // Make sure Launchwrapper is available, or download it otherwise
            download(LAUNCHWRAPPER_REMOTE, path);
        }

        return true;
    }

    private static JsonValue downloadJson(String remote) throws IOException {
        URLConnection con = new URL(remote).openConnection();
        JsonValue json;

        try (BufferedInputStream source = new BufferedInputStream(con.getInputStream())) {
            json = Json.parse(new InputStreamReader(source, StandardCharsets.UTF_8));
        }

        return json;
    }

    /**
     * Downloads a file and verify its digest.
     *
     * @param remote The file URL
     * @param path The local path
     * @param sha1 The SHA-1 expected digest
     * @throws IOException If there is a problem while downloading the file
     */
    private static void downloadAndVerify(String remote, Path path, String sha1) throws IOException {
        Files.createDirectories(path.getParent());

        String name = path.getFileName().toString();
        URL url = new URL(remote);

        System.out.println("Downloading " + name + "... This can take a while.");
        System.out.println(url);

        URLConnection con = url.openConnection();
        BufferedInputStream stream = new BufferedInputStream(con.getInputStream());
        MessageDigest digest;

        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            // This cannot happen because JVM is required to support SHA-1
            throw new RuntimeException(e);
        }

        try (ReadableByteChannel source = Channels.newChannel(new DigestInputStream(stream, digest));
             FileChannel out = FileChannel.open(path, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            out.transferFrom(source, 0, Long.MAX_VALUE);
        }

        String fileDigest = toHexString(digest.digest());
        if (sha1.equals(fileDigest)) {
            System.out.println("Successfully downloaded " + name + " and verified checksum!");
        } else {
            Files.delete(path);
            throw new IOException("Checksum verification failed: Expected " + sha1 +
                    ", got " + fileDigest);
        }
    }

    private static void download(String remote, Path path) throws IOException {
        Files.createDirectories(path.getParent());

        String name = path.getFileName().toString();
        URL url = new URL(remote);

        System.out.println("Downloading " + name + "... This can take a while.");
        System.out.println(url);

        URLConnection con = url.openConnection();
        BufferedInputStream stream = new BufferedInputStream(con.getInputStream());

        try (ReadableByteChannel in = Channels.newChannel(stream);
             FileChannel out = FileChannel.open(path, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            out.transferFrom(in, 0, Long.MAX_VALUE);
        }

        System.out.println("Successfully downloaded " + name + "!");
    }

    // From http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java
    private static final char[] hexArray = "0123456789abcdef".toCharArray();

    private static String toHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
