package org.granitepowered.granite.impl.meta;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.api.service.persistence.DataSource;
import org.spongepowered.api.service.persistence.data.DataContainer;

public class GraniteMeta {

    public final int type;
    public final String name;

    public GraniteMeta(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }

    public DataContainer toContainer() {
        throw new NotImplementedException("");
    }

    public void serialize(DataSource dataSource) {
        throw new NotImplementedException("");
    }
}
