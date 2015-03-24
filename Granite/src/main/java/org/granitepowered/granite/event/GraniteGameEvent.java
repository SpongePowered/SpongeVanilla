package org.granitepowered.granite.event;

import org.granitepowered.granite.Granite;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.GameEvent;

public class GraniteGameEvent extends GraniteEvent implements GameEvent {

    @Override
    public Game getGame() {
        return Granite.getInstance();
    }
}
