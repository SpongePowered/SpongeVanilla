package org.granitepowered.granite.impl.entity.weather;

import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.mc.MCWeatherEffect;
import org.spongepowered.api.entity.weather.WeatherEffect;

public abstract class GraniteWeatherEffect<T extends MCWeatherEffect> extends GraniteEntity<T> implements WeatherEffect {

    public GraniteWeatherEffect(T obj) {
        super(obj);
    }
}
