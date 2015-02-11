package org.granitepowered.granite.mc;

@Implement(name = "EntityPigZombie")
public interface MCEntityPigZombie extends MCEntityZombie {

    int fieldGet$angerLevel();

    void fieldSet$angerLevel(int anger);
}
