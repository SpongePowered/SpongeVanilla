package org.granitepowered.granite.impl.entity.explosive;

import com.google.common.base.Optional;
import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.mc.MCPrimedTNT;
import org.spongepowered.api.entity.explosive.PrimedTNT;
import org.spongepowered.api.entity.living.Living;

public class GranitePrimedTNT extends GraniteEntity<MCPrimedTNT> implements PrimedTNT {
    public GranitePrimedTNT(MCPrimedTNT obj) {
        super(obj);
    }

    @Override
    public Optional<Living> getDetonator() {
        return (Optional<Living>) obj.fieldGet$tntTriggeredBy();
    }

    @Override
    public int getFuseDuration() {
        return obj.fieldGet$fuse();
    }

    @Override
    public void setFuseDuration(int ticks) {
        obj.fieldSet$fuse(ticks);
    }

    @Override
    public void detonate() {
        this.setFuseDuration(0);
    }
}
