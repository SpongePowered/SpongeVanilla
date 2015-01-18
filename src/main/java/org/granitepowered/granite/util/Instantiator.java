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

package org.granitepowered.granite.util;

import org.granitepowered.granite.mc.*;

import java.util.UUID;

public class Instantiator {

    private static final InstantiatorInterface instance = null;

    public static InstantiatorInterface get() {
        return instance;
    }

    public interface InstantiatorInterface {

        MCPacketChat newPacketChat(MCChatComponent chatComponent, byte type);

        MCBlockPos newBlockPos(int x, int y, int z);

        MCGameRules newGameRules();

        MCPacketTitle newPacketTitle(MCPacketTitleType type, MCChatComponent component);

        MCPacketTitle newPacketTitle(int fadeIn, int stay, int fadeOut);

        MCPacketBlockChange newPacketBlockChange(MCWorld world, MCBlockPos pos);

        MCPotionEffect newPotionEffect(int id, int effectDuration, int effectAmplifier, boolean ambient, boolean showParticles);

        MCPacketParticles newPacketParticles(Enum p_i45977_1_, boolean b, float f, float f2, float f3, float f4, float f5, float f6, float f7, int i_, int ...i2);

        MCRotations newRotations(float x, float y, float z);

        MCEntityArrow newEntityArrow(MCWorld world, MCEntityLivingBase shooter, float something);

        MCGameProfile newGameProfile(UUID uuid, String name);
    }
}
