package org.granitepowered.granite.impl.entity.living.monster;

import org.granitepowered.granite.mc.MCEntityCaveSpider;
import org.granitepowered.granite.mc.MCEntitySpider;
import org.spongepowered.api.entity.living.monster.CaveSpider;
import org.spongepowered.api.entity.living.monster.Spider;

public class GraniteEntityCaveSpider extends GraniteEntitySpider<MCEntityCaveSpider> implements CaveSpider {

    public GraniteEntityCaveSpider(MCEntityCaveSpider obj) {
        super(obj);
    }
}
