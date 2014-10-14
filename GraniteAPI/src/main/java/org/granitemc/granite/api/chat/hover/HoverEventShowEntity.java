package org.granitemc.granite.api.chat.hover;

import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.chat.HoverEvent;
import org.granitemc.granite.api.entity.Entity;

public class HoverEventShowEntity extends HoverEvent {
    Entity entity;

    public HoverEventShowEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    protected String getAction() {
        return "show_entity";
    }

    @Override
    public Object getValue() {
        return Granite.getAPIHelper().getEntityNBTCompoundString(entity);
    }
}
