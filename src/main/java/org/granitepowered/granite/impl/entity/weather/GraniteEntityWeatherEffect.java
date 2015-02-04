package org.granitepowered.granite.impl.entity.weather;

import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.mc.MCEntityWeatherEffect;
import org.spongepowered.api.entity.weather.WeatherEffect;

public abstract class GraniteEntityWeatherEffect<T extends MCEntityWeatherEffect> extends GraniteEntity<T> implements WeatherEffect {

    public GraniteEntityWeatherEffect(T obj) {
        super(obj);
    }
}
