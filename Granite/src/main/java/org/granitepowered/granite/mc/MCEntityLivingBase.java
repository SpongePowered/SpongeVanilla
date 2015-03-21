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
public class MCEntityLivingBase extends MCEntity {

    public MCEntityLivingBase fieldGet$lastAttacker() {
        return null;
    }

    public void fieldSet$lastAttacker(MCEntityLivingBase living) {
    }

    public float fieldGet$lastDamage() {
        return Float.parseFloat(null);
    }

    public void fieldSet$lastDamage(float damage) {
    }

    public int fieldGet$maxHurtResistantTime() {
        return Integer.parseInt(null);
    }

    public void fieldSet$maxHurtResistantTime(int halfTicks) {
    }

    public MCCombatTracker fieldGet$_combatTracker() {
        return null;
    }

    public void damageEntity(MCDamageSource source, float amount) {
    }

    public float getMaxHealth() {
        return Float.parseFloat(null);
    }

    public void addPotionEffect(MCPotionEffect potionEffect) {
    }

    public void removePotionEffect(int potionId) {
    }

    public Map fieldGet$activePotions() {
        return null;
    }

    public MCBaseAttributeMap fieldGet$attributeMap() {
        return null;
    }
}
