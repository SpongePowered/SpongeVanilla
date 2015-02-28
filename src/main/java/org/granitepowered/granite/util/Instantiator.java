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

        MCBlockPos newBlockPos(int x, int y, int z);

        MCBlockRailBase newBlockRailBase(boolean powered);

        MCEntityArrow newEntityArrow(MCWorld world, MCEntityLivingBase shooter, float something);

        MCEntityEgg newEntityEgg(MCWorld world, MCEntityLivingBase shooter);

        MCEntitySmallFireball newEntitySmallFireball(MCWorld world, MCEntityLivingBase shooter, double something1, double something2,
                                                     double something3);

        MCEntityLargeFireball newEntityLargeFireball(MCWorld world, MCEntityLivingBase shooter, double something1, double something2,
                                                     double something3);

        MCGameProfile newGameProfile(UUID uuid, String name);

        MCGameRules newGameRules();

        MCMerchantRecipe newMerchantRecipe(MCItemStack firstSellingItem, MCItemStack secondSellingItem, MCItemStack buyingItem, int uses,
                                           int maxUses);

        MCPacketChat newPacketChat(MCIChatComponent chatComponent, byte type);

        MCPacketBlockChange newPacketBlockChange(MCWorld world, MCBlockPos pos);

        MCPacketParticles newPacketParticles(Enum p_i45977_1_, boolean b, float f, float f2, float f3, float f4, float f5, float f6, float f7, int i_,
                                             int... i2);

        MCPacketTitle newPacketTitle(int fadeIn, int stay, int fadeOut);

        MCPacketTitle newPacketTitle(MCPacketTitleType type, MCIChatComponent component);

        MCPotionEffect newPotionEffect(int id, int effectDuration, int effectAmplifier, boolean ambient, boolean showParticles);

        MCRotations newRotations(float x, float y, float z);
    }
}
