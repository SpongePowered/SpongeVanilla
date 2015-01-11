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

package org.granitepowered.granite.mc;

import java.util.Map;

@Implement(name = "EntityLivingBase")
public interface MCEntityLivingBase extends MCEntity {

    MCEntityLivingBase fieldGet$lastAttacker();

    void fieldSet$lastAttacker(MCEntityLivingBase living);

    float fieldGet$lastDamage();

    void fieldSet$lastDamage(float damage);

    int fieldGet$maxHurtResistantTime();

    void fieldSet$maxHurtResistantTime(int halfTicks);

    MCCombatTracker fieldGet$_combatTracker();

    void damageEntity(MCDamageSource source, float amount);

    float getHealth();

    void setHealth(float health);

    float getMaxHealth();

    void addPotionEffect(MCPotionEffect potionEffect);

    void removePotionEffect(int potionId);

    Map fieldGet$activePotions();
}
