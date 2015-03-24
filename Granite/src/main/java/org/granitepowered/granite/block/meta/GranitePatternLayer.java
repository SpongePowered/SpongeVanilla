package org.granitepowered.granite.block.meta;

import org.spongepowered.api.block.data.Banner;
import org.spongepowered.api.block.meta.BannerPatternShape;
import org.spongepowered.api.item.DyeColor;
import org.spongepowered.api.service.persistence.data.DataContainer;
import org.spongepowered.api.service.persistence.data.DataQuery;
import org.spongepowered.api.service.persistence.data.MemoryDataContainer;

public class GranitePatternLayer implements Banner.PatternLayer {

    private final BannerPatternShape bannerPatternShape;
    private final DyeColor dyeColor;

    public GranitePatternLayer(BannerPatternShape bannerPatternShape, DyeColor dyeColor) {
        this.bannerPatternShape = bannerPatternShape;
        this.dyeColor = dyeColor;
    }

    @Override
    public BannerPatternShape getId() {
        return this.bannerPatternShape;
    }

    @Override
    public DyeColor getColor() {
        return this.dyeColor;
    }

    @Override
    public DataContainer toContainer() {
        DataContainer dataContainer = new MemoryDataContainer();
        dataContainer.set(new DataQuery("id"), bannerPatternShape.getId());
        dataContainer.set(new DataQuery("color"), dyeColor.getName());
        return dataContainer;
    }
}
