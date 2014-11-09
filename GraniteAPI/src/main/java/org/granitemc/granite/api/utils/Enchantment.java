package org.granitemc.granite.api.utils;

public enum Enchantment {

    PROTECTION(0), FIRE_PROTECTION(1), FEATHER_FALLING(2), BLAST_PROTECTION(3), PROJECTILE_PROTECTION(4), RESPIRATION(5), AQUA_AFFINITY(6), THORNS(7), DEPTH_STRIDER(8),
    SHARPNESS(16), SMITE(17), BANE_OF_ARTHROPODS(18), KNOCKBACK(19), FIRE_ASPECT(20), LOOTING(21),
    EFFICIENCY(32), SILK_TOUCH(33), UNBREAKING(34), FORTUNE(35),
    POWER(48), PUNCH(49), FLAME(50), INFINITY(51),
    LUCK_OF_THE_SEA(61), LURE(62);

    int id;
    Enchantment(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
