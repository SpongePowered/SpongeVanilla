package org.spongepowered.granite.entity;

import com.google.common.base.MoreObjects;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityRegistry.EntityRegistration;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.text.translation.Translation;

public class GraniteEntityType implements EntityType {

    public final int entityTypeId;
    public final String entityName;
    public final String modId;
    public final Class<? extends Entity> entityClass;
    // currently not used
    public int trackingRange;
    public int updateFrequency;
    public boolean sendsVelocityUpdates;

    public GraniteEntityType(int id, String name, Class<? extends Entity> clazz) {
        this(id, name.toLowerCase(), "minecraft", clazz);
    }

    public GraniteEntityType(int id, String name, String modId, Class<? extends Entity> clazz) {
        this.entityTypeId = id;
        this.entityName = name.toLowerCase();
        this.entityClass = clazz;
        this.modId = modId.toLowerCase();
    }

    @Override
    public String getId() {
        return this.modId + ":" + this.entityName;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public String getModId() {
        return this.modId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends org.spongepowered.api.entity.Entity> getEntityClass() {
        return (Class<? extends org.spongepowered.api.entity.Entity>) this.entityClass;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GraniteEntityType other = (GraniteEntityType) obj;
        if (!this.entityName.equals(other.entityName)) {
            return false;
        } else if (!this.entityClass.equals(other.entityClass)) {
            return false;
        } else if (this.entityTypeId != other.entityTypeId) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.entityTypeId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", this.entityTypeId)
                .add("name", this.entityTypeId)
                .add("modid", this.modId)
                .add("class", this.entityClass.getName())
                .toString();
    }

    @Override
    public Translation getTranslation() {
        return null;
    }
}