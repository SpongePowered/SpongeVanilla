package org.granitepowered.granite.impl.meta;

import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.mc.MCEnumBannerPattern;
import org.spongepowered.api.block.meta.BannerPatternShape;

public class GraniteBannerPatternShape extends Composite<MCEnumBannerPattern> implements BannerPatternShape {

    public GraniteBannerPatternShape(Object obj) {
        super(obj);
    }

    @Override
    public String getName() {
        return obj.fieldGet$name();
    }

    @Override
    public String getId() {
        return obj.fieldGet$id();
    }
}
