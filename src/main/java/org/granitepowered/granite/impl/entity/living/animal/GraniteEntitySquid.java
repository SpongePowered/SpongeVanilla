package org.granitepowered.granite.impl.entity.living.animal;

import org.granitepowered.granite.impl.entity.living.GraniteEntityWaterMob;
import org.granitepowered.granite.mc.MCEntitySquid;
import org.spongepowered.api.entity.living.Squid;

public class GraniteEntitySquid extends GraniteEntityWaterMob<MCEntitySquid> implements Squid {

    public GraniteEntitySquid(MCEntitySquid obj) {
        super(obj);
    }
}
