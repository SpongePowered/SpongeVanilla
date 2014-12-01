package org.granitemc.granite.api.item;

/*
 * License (MIT)
 *
 * Copyright (c) 2014. Granite Team
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

public enum EnchantmentType {

    PROTECTION(0), FIRE_PROTECTION(1), FEATHER_FALLING(2), BLAST_PROTECTION(3), PROJECTILE_PROTECTION(4), RESPIRATION(5), AQUA_AFFINITY(6), THORNS(7), DEPTH_STRIDER(8),
    SHARPNESS(16), SMITE(17), BANE_OF_ARTHROPODS(18), KNOCKBACK(19), FIRE_ASPECT(20), LOOTING(21),
    EFFICIENCY(32), SILK_TOUCH(33), UNBREAKING(34), FORTUNE(35),
    POWER(48), PUNCH(49), FLAME(50), INFINITY(51),
    LUCK_OF_THE_SEA(61), LURE(62);

    int id;

    EnchantmentType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
