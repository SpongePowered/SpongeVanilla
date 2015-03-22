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

package org.granitepowered.granite.impl.status;

import org.granitepowered.granite.ProtocolMinecraftVersion;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.impl.GraniteMinecraftVersion;
import mc.MCMinecraftProtocolVersionIdentifier;
import org.spongepowered.api.MinecraftVersion;

public class GraniteMinecraftProtocolVersionIdentifier extends Composite<MCMinecraftProtocolVersionIdentifier> implements ProtocolMinecraftVersion {

    public GraniteMinecraftProtocolVersionIdentifier(Object obj) {
        super(obj);
    }

    @Override
    public int getProtocol() {
        return obj.protocol;
    }

    @Override
    public String getName() {
        return obj.name;
    }

    @Override
    public boolean isLegacy() {
        return false;
    }

    @Override
    public int compareTo(MinecraftVersion minecraftVersion) {
        return GraniteMinecraftVersion.compare(this, minecraftVersion);
    }
}
