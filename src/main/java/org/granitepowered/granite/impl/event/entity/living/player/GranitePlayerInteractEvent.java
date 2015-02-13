package org.granitepowered.granite.impl.event.entity.living.player;

import com.flowpowered.math.vector.Vector3f;
import com.google.common.base.Optional;
import org.granitepowered.granite.impl.entity.player.GranitePlayer;
import org.spongepowered.api.entity.EntityInteractionType;
import org.spongepowered.api.event.entity.living.player.PlayerInteractEvent;

public class GranitePlayerInteractEvent extends GranitePlayerEvent implements PlayerInteractEvent {
    private EntityInteractionType type;
    private Optional<Vector3f> clickedPosition;

    public GranitePlayerInteractEvent(GranitePlayer player, EntityInteractionType type, Optional<Vector3f> clickedPosition) {
        super(player);
        this.type = type;
        this.clickedPosition = clickedPosition;
    }

    @Override
    public EntityInteractionType getInteractionType() {
        return type;
    }

    @Override
    public Optional<Vector3f> getClickedPosition() {
        return clickedPosition;
    }
}
