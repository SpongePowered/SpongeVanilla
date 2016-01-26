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
package org.spongepowered.server.launch.transformer.deobf.reader;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;

public final class SrgReader {

    private static final char SEPARATOR = ' ';
    private static final Splitter SEPARATOR_SPLITTER = Splitter.on(SEPARATOR);

    private static final char KEY_SEPARATOR = ':';

    private static final String CLASS_MAPPING_KEY = "CL";
    private static final String FIELD_MAPPING_KEY = "FD";
    private static final String METHOD_MAPPING_KEY = "MD";

    private static final char MEMBER_SEPARATOR = '/';

    private static final int MIN_LINE_LENGTH = 7;

    private final ImmutableBiMap.Builder<String, String> classes = ImmutableBiMap.builder();
    private final ImmutableTable.Builder<String, String, String> fields = ImmutableTable.builder();
    private final ImmutableTable.Builder<String, String, String> methods = ImmutableTable.builder();

    public void read(URL resource) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream(), UTF_8))) {
            read(reader);
        }
    }

    public void read(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if ((line = line.trim()).length() < MIN_LINE_LENGTH || line.charAt(2) != KEY_SEPARATOR || line.charAt(3) != SEPARATOR) {
                continue; // TODO: Warning?
            }

            Iterator<String> parts = SEPARATOR_SPLITTER.split(line).iterator();
            String prefix = parts.next();

            if (prefix.startsWith(CLASS_MAPPING_KEY)) {
                this.classes.put(parts.next(), parts.next());
            } else if (prefix.startsWith(FIELD_MAPPING_KEY)) {
                String[] source = parseMember(parts.next());
                String[] mapped = parseMember(parts.next());
                this.fields.put(source[0], source[1], mapped[1]);
            } else if (prefix.startsWith(METHOD_MAPPING_KEY)) {
                // Note: Destination descriptor is ignored on purpose (we don't need it)
                String[] source = parseMember(parts.next());
                String desc = parts.next();
                String[] mapped = parseMember(parts.next());
                this.methods.put(source[0], source[1].concat(desc), mapped[1]);
            }
        }
    }

    private static String[] parseMember(String member) {
        int pos = member.lastIndexOf(MEMBER_SEPARATOR);
        return new String[]{member.substring(0, pos), member.substring(pos + 1)};
    }

    public ImmutableBiMap<String, String> getClasses() {
        return this.classes.build();
    }

    public ImmutableTable<String, String, String> getFields() {
        return this.fields.build();
    }

    public ImmutableTable<String, String, String> getMethods() {
        return this.methods.build();
    }

}
